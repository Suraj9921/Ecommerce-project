package com.week12.ecommerce.service;


import com.week12.ecommerce.model.Address;
import com.week12.ecommerce.model.Users;

import java.util.List;

public interface AddressService {
    List<Address> getAllAddressesByUser(Users user);
    Address addAddress(Address address);

    void deleteAddressById(int addressId);
}
