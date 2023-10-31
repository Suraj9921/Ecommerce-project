package com.week12.ecommerce.serviceimpl;

import com.week12.ecommerce.model.Address;
import com.week12.ecommerce.model.Users;
import com.week12.ecommerce.repository.AddressRepo;
import com.week12.ecommerce.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressImpl implements AddressService {
    @Autowired
    AddressRepo addressRepo;
    @Override
    public List<Address> getAllAddressesByUser(Users user) {
      return  addressRepo.findAllByUser(user);
    }

    @Override
    public Address addAddress(Address address) {
        return addressRepo.save(address);
    }

    @Override
    public void deleteAddressById(int addressId) {
        addressRepo.deleteById(addressId);
    }
}
