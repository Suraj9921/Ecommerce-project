package com.week12.ecommerce.service;



import com.week12.ecommerce.dto.OfferDto;
import com.week12.ecommerce.model.Offer;

import java.util.List;

public interface OfferService {

    List<OfferDto> getAllOffers();

    Offer Save(OfferDto offerDto);

    OfferDto findById(long id);

    Offer update(OfferDto offerDto);

    void disable(long id);

    void enable(long id);

    void deleteOffer(long id);



}
