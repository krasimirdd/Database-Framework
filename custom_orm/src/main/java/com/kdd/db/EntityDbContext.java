package com.kdd.db;

import com.kdd.db.annotations.Column;
import com.kdd.db.annotations.Entity;
import com.kdd.db.annotations.PrimaryKey;
import com.kdd.db.base.DbContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityDbContext<T> implements DbContext<T> {
    private static final String SELECT_QUERY_TEMPLATE = "SELECT * FROM {0}";
    private static final String SELECT_SINGLE_QUERY_TEMPLATE = "SELECT * FROM {0}";
    private static final String SELECT_SINGLE_WHERE_QUERY_TEMPLATE = "SELECT * FROM {0} WHERE {1}";
    private static final String SELECT_WHERE_QUERY_TEMPLATE = "SELECT * FROM {0} WHERE {1}";
    private static final String WHERE_PRIMARY_KEY = " {0}={1} ";

    private static final String INSERT_QUERY_TEMPLATE = "INSERT INTO {0}({1}) VALUES({2})";

    private static final String UPDATE_QUERY_TEMPLATE = "UPDATE {0} SET {1} WHERE {2}={3}";
    private static final String SET_QUERY_TEMPLATE = "{0}={1}";

    private final Connection connection;
    private final Class<T> klass;

    public EntityDbContext(Connection connection, Class<T> klass) throws SQLException {
        this.connection = connection;
        this.klass = klass;

        if (checksIfTableExists()) {
            updateTable();
        } else {
            createTable();
        }
    }

    public boolean persist(T entity) throws IllegalAccessException, SQLException {
        Field primaryKeyField = getPrimaryKeyField();
        primaryKeyField.setAccessible(true);
        long primaryKey = (long) primaryKeyField.get(entity);

        if (primaryKey > 0) {
            return update(entity);
        }

        return insert(entity);
    }

    private boolean insert(T entity) throws SQLException {
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        getColumnFields()
                .forEach(field -> {
                    try {
                        field.setAccessible(true);

                        String columnName = field.getAnnotation(Column.class)
                                .name();
                        Object value = field.get(entity);

                        columns.add(columnName);
                        values.add(value);

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        String columnNames = String.join(", ", columns);
        String columnValues = values
                .stream()
                .map(value -> {
                    if (value instanceof String) {
                        return "\'" + value + "\'";
                    }

                    return value;
                })
                // .map(Object::toString)
                .map(o -> {
                    if (o == null) {
                        return "NULL";
                    } else {
                        return o.toString();
                    }
                })
                .collect(Collectors.joining(", "));

        String query = MessageFormat.format(
                INSERT_QUERY_TEMPLATE,
                getTableName(),
                columnNames,
                columnValues
        );

        return connection.prepareStatement(query).execute();
    }

    private boolean update(T entity) throws SQLException, IllegalAccessException {
        // UPDATE {0} {1} WHERE {2}={3}
        // SET {0}={1}

        List<String> updateQueries = getColumnFields().stream()
                .map(field -> {
                    try {
                        field.setAccessible(true);

                        String columnName = field.getAnnotation(Column.class)
                                .name();
                        Object value = field.get(entity);

                        if (value instanceof String) {
                            return "\'" + value + "\'";
                        }

                        return MessageFormat.format(
                                SET_QUERY_TEMPLATE,
                                columnName,
                                value
                        );
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    return null;
                })
                .collect(Collectors.toList());

        String updateQueriesString = String.join(", ", updateQueries);

        Field primaryKey = getPrimaryKeyField();
        primaryKey.setAccessible(true);

        String primaryKeyName =
                primaryKey.getAnnotation(PrimaryKey.class)
                        .name();

        long primaryKeyValue =
                (long) primaryKey
                        .get(entity);


        String query = MessageFormat.format(
                UPDATE_QUERY_TEMPLATE,
                getTableName(),
                updateQueriesString,
                primaryKeyName,
                primaryKeyValue
        );

        return connection.prepareStatement(query).execute();
    }

    public List<T> find() throws SQLException, IllegalAccessException, InstantiationException {
        return find(null);
    }

    public List<T> find(String where) throws IllegalAccessException, SQLException, InstantiationException {
        String queryTemplate = where == null
                ? SELECT_QUERY_TEMPLATE
                : SELECT_WHERE_QUERY_TEMPLATE;

        return findByTemplate(queryTemplate, where);
    }

    public T findFirst() throws IllegalAccessException, SQLException, InstantiationException {
        return findFirst(null);
    }

    public T findFirst(String where) throws IllegalAccessException, SQLException, InstantiationException {
        String queryTemplate =
                where == null
                        ? SELECT_SINGLE_QUERY_TEMPLATE
                        : SELECT_SINGLE_WHERE_QUERY_TEMPLATE;

        return findByTemplate(queryTemplate, where).get(0);
    }

    public T findById(long id) throws IllegalAccessException, SQLException, InstantiationException {
        String primaryKeyName =
                getPrimaryKeyField().getAnnotation(PrimaryKey.class)
                        .name();

        String whereString = MessageFormat.format(
                WHERE_PRIMARY_KEY,
                primaryKeyName, id
        );

        return findFirst(whereString);
    }

    @Override
    public boolean delete(String where) throws SQLException {
        String query = String.format(
                "DELETE FROM %s WHERE %s",
                getTableName(),
                where
        );

        this.connection.prepareStatement(query).execute();
        return false;
    }

    private List<T> findByTemplate(String template, String where) throws SQLException, IllegalAccessException, InstantiationException {
        String query = MessageFormat.format(
                template,
                getTableName(),
                where);

        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        return toList(rs);
    }

    private List<T> toList(ResultSet rs) throws SQLException, InstantiationException, IllegalAccessException {
        List<T> result = new ArrayList<>();

        while (rs.next()) {
            T entity = this.createEntity(rs);
            result.add(entity);
        }

        return result;
    }

    private T createEntity(ResultSet rs) throws IllegalAccessException, InstantiationException, SQLException {
        T entity = klass.newInstance();
        List<Field> columnFields = getColumnFields();

        Field primaryKeyField = getPrimaryKeyField();

        primaryKeyField.setAccessible(true);
        primaryKeyField.set(entity,
                rs.getLong(primaryKeyField.getAnnotation(PrimaryKey.class).name()));

        columnFields.forEach(field -> {
            String columnName = field.getAnnotation(Column.class)
                    .name();

            try {
                field.setAccessible(true);

                if (field.getType() == Long.class || field.getType() == long.class) {
                    long value = rs.getLong(columnName);
                    field.set(entity, value);
                } else if (field.getType() == String.class) {
                    String value = rs.getString(columnName);
                    field.set(entity, value);
                }
            } catch (SQLException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return entity;
    }

    private List<Field> getColumnFields() {
        return Arrays.stream(klass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .collect(Collectors.toList());
    }

    private Field getPrimaryKeyField() {
        return Arrays.stream(klass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(PrimaryKey.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Class" + klass + "does not have a primary key annotation"));
    }

    private String getTableName() {
        Annotation annotation = Arrays.stream(klass.getAnnotations())
                .filter(a -> a.annotationType() == Entity.class)
                .findFirst()
                .orElse(null);

        if (annotation == null) {
            return klass.getSimpleName().toLowerCase() + "s";
        }
        return klass.getAnnotation(Entity.class)
                .name();
    }

    private boolean checksIfTableExists() throws SQLException {
        String query = String
                .format(
                        "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = '%s'",
                        getTableName()
                );

        PreparedStatement ps = this.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        return rs.next();
    }

    private void createTable() throws SQLException {
        Field primaryKeyField = getPrimaryKeyField();
        String primaryKeyName = primaryKeyField.getDeclaredAnnotation(PrimaryKey.class)
                .name();
        String primaryKeyType = getColumnType(primaryKeyField);

        String primaryKeyTemplate = String.format(
                "%s %s PRIMARY KEY AUTO_INCREMENT",
                primaryKeyName,
                primaryKeyType
        );
        //***************************************//

        List<Field> columnFields = getColumnFields();
        List<String> columnsWithTypes = new ArrayList<>();

        columnFields.stream()
                .forEach(field -> {
                    String columnName = field.getDeclaredAnnotation(Column.class)
                            .name();
                    String columnType = getColumnType(field);
                    String columnTemplate = String.format(
                            "%s %s",
                            columnName, columnType
                    );

                    columnsWithTypes.add(columnTemplate);
                });
        String createQueryTemplate = String.format(
                "%s, %s",
                primaryKeyTemplate,
                columnsWithTypes.stream()
                        .collect(Collectors.joining(", "))
        );

        String query = String.format("CREATE TABLE %s(%s)",
                getTableName(),
                createQueryTemplate
        );

        this.connection.prepareStatement(query).execute();

    }

    private String getColumnType(Field field) {
        if (field.getType() == long.class || field.getType() == Long.class || field.getType() == int.class) {
            return "INT";
        } else if (field.getType() == String.class) {
            return "VARCHAR(255)";
        }

        return null;
    }

    private void updateTable() throws SQLException {
        List<String> entityColumnsNames = getColumnFields().stream()
                .map(field -> field.getDeclaredAnnotation(Column.class).
                        name()).collect(Collectors.toList());

        entityColumnsNames.add(getPrimaryKeyField().getDeclaredAnnotation(PrimaryKey.class).name());

        List<String> dbColumnsNames = getDbTableColumnsNames();

        List<String> newColumnsNames = entityColumnsNames.stream()
                .filter(c -> !dbColumnsNames.contains(c))
                .collect(Collectors.toList());

        List<Field> newFields = getColumnFields().stream()
                .filter(field -> newColumnsNames.contains(
                        field
                                .getDeclaredAnnotation(Column.class)
                                .name()))
                .collect(Collectors.toList());

        List<String> columnsTemplate = new ArrayList<>();
        newFields.stream()
                .forEach(field -> {
                    String columnTemplate = String.format(
                            "ADD COLUMN %s %s",
                            field.getDeclaredAnnotation(Column.class).name(),
                            getColumnType(field)
                    );

                    columnsTemplate.add(columnTemplate);
                });

        String queryAdd = String.join(", ", columnsTemplate);

        String queryAlter = String.format(
                "ALTER TABLE %s %s",
                getTableName(),
                queryAdd
        );

        this.connection.prepareStatement(queryAlter).execute();
    }

    private List<String> getDbTableColumnsNames() throws SQLException {
        String query = String.format(
                "SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = '%s'",
                getTableName()
        );

        PreparedStatement ps = this.connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        List<String> columnsNames = new ArrayList<>();
        while (rs.next()) {
            columnsNames.add(rs.getString(1));
        }

        return columnsNames;
    }

}
