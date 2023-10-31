package com.week12.ecommerce.service;


import com.week12.ecommerce.model.ReferralOffer;

public interface ReferralOfferService {
    void saveReferral(long id, String token,String email);

    ReferralOffer findByReferralCode(String token);

    void addReferralAmount(String email);

    boolean existsByEmail(String email);

    ReferralOffer findBySenderMail(String email);

    void update(ReferralOffer referralOffer);
}
