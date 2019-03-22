package com.kdd;

import com.kdd.db.EntityDbContext;
import com.kdd.db.base.DbContext;
import com.kdd.entities.Employee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/soft_uni_simple";

    public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException {
        Connection connection = getConnection();
        DbContext<Employee> usersDbContext = getDbContext(connection, Employee.class);

        usersDbContext.find("first_name LIKE 'p%'")
                .forEach(System.out::println);

        usersDbContext.find()
                .forEach(System.out::println);

        System.out.println(usersDbContext.findFirst());
        System.out.println(usersDbContext.findFirst("first_name LIKE 'g%'"));

        System.out.println(usersDbContext.findById(1));
        connection.close();
    }

    private static <T> DbContext<T> getDbContext(Connection connection, Class<T> klass) throws SQLException {
        return new EntityDbContext<>(connection, klass);
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                CONNECTION_STRING, "root", "1810");
    }
}
