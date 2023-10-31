package com.week12.ecommerce.model;


import com.week12.ecommerce.enums.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long usersId;
    private double amount;
    private TransactionType transactionType;
    private Date updateOn;


}
