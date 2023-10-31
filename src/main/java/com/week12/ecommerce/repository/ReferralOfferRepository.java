package com.week12.ecommerce.repository;

import com.week12.ecommerce.model.ReferralOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralOfferRepository extends JpaRepository<ReferralOffer,Long> {
    @Query("select r from ReferralOffer r where r.referralCode=?1 and r.deleted = false ")
    ReferralOffer findByReferralCode(String token);

    @Query("select r from ReferralOffer r where r.senderEmail=?1 and r.deleted=false")
    ReferralOffer findBySenderMail(String email);

    boolean existsBySenderEmail(String email);
}
