package com.week12.ecommerce.repository;

import com.week12.ecommerce.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer,Long> {

    Offer findById(long id);


}
