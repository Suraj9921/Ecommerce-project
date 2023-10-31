package com.week12.ecommerce.service;


import com.week12.ecommerce.model.Wallet;

import java.util.List;

public interface WalletService {
    List<Wallet> findAllByUserId(long id);

    double findSumOfWalletAmount(long id);

    void save(Wallet wallet);
}
