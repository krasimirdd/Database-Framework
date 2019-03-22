package alararestaurant.domain.entities;

import alararestaurant.domain.enums.OrderType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "orders")
public class Order extends BaseEntity {

    private String customer;

    private LocalDate dateTime;

    private OrderType type;

    private Employee employee;

    private List<OrderItem> orderItems;

    public Order() {
    }


    @Column(name = "customer", nullable = false, columnDefinition = "TEXT")
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @Column(name = "date_time", nullable = false, columnDefinition = "DATETIME")
    public LocalDate getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    @Column(name = "type", nullable = false, columnDefinition = "ENUM('ForHere', 'ToGo') default 'ForHere'")
    @Enumerated(value = EnumType.STRING)
    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    @ManyToOne(targetEntity = Employee.class)
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @OneToMany(targetEntity = OrderItem.class, mappedBy = "order")
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
