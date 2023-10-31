package com.week12.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class UserOTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "One-time password cannot be blank")
    private String oneTimePassword;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date otpRequestedTime;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date updateOn;
}
