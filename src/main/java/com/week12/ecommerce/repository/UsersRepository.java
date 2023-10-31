package com.week12.ecommerce.repository;

import com.week12.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {
    boolean existsByEmail(String email);

    Users findUserByEmail(String email);

    Users findByEmail(String username);

    Users findById(long userId);
}
