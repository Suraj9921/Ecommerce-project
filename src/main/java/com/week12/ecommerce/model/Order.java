package com.week12.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;


    private Date orderDate;
    private Date deliveryDate;
    private String orderStatus;
    private double totalPrice;
    private int quantity;
    private String paymentMethod;
    private boolean isAccept;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id", referencedColumnName = "id")
//    private Product product;

    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn( name = "id", referencedColumnName = "id")
    private Users user;

    @ToString.Exclude
    @OneToMany( mappedBy = "order" ,cascade = CascadeType.ALL)
    private List<OrderDetails> orderDetails;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + deliveryDate +
                ", orderStatus='" + orderStatus + '\'' +
                ", totalPrice=" + totalPrice +
                ", quantity=" + quantity +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", isAccept=" + isAccept +
                ", user=" + user +
                '}';
    }
}
