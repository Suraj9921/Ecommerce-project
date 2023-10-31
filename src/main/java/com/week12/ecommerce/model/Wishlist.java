package com.week12.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "wishlist")
public class Wishlist {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "wishlist_id")
   private Long id;


   @OneToOne
   @JoinColumn(name = "product_id",referencedColumnName = "id")
   private Product product;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "customer_id",referencedColumnName = "id")
   private Users users;



}
