package com.week12.ecommerce.repository;


import com.week12.ecommerce.model.Address;
import com.week12.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepo extends JpaRepository<Address, Integer> {

    List<Address> findAllByUser(Users user);
}
