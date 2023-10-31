package com.week12.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be a 10-digit number")
    private String phoneNumber;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Role cannot be blank")
    private String role;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 7, message = "Password must be at least 8 characters long")
    private String password;

    private boolean isDelete;

    private boolean isBlocked;

    private long otp;

//    @NotBlank(message="Field cannot be blank")
//    @NotEmpty
    private String repeatPassword;

    private boolean isActive;

    @NotNull(message = "Created at cannot be null")
    private Date createdAt;

    @NotNull(message = "Updated on cannot be null")
    private Date updateOn;



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Address> addresses;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ShoppingCart cart;

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", isDelete=" + isDelete +
                ", isBlocked=" + isBlocked +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updateOn=" + updateOn +
                ", otp=" + otp +
                ", addresses=" + formatAddresses(addresses) +
                '}';
    }

    private String formatAddresses(List<Address> addresses) {
        if (addresses == null) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder("[");
        for (Address address : addresses) {
            builder.append("Address{id=").append(address.getId())
                    .append(", street='").append(address.getStreet()).append('\'')
                    .append(", state='").append(address.getState()).append('\'')
                    .append(", postalcode='").append(address.getPostalcode()).append('\'')
                    .append(", country='").append(address.getCountry()).append('\'')
                    .append("}, ");
        }
        // Remove the trailing comma and space
        if (builder.length() > 1) {
            builder.setLength(builder.length() - 2);
        }
        builder.append("]");
        return builder.toString();
    }

}
