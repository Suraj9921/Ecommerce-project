package com.week12.ecommerce.serviceimpl;

import com.week12.ecommerce.model.Wallet;
import com.week12.ecommerce.repository.WalletRepository;
import com.week12.ecommerce.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {
    private WalletRepository walletRepository;
    @Override
    public List<Wallet> findAllByUserId(long id) {
        return walletRepository.findByUsersId(id);
    }

    @Override
    public double findSumOfWalletAmount(long id) {
        double creditAmount = walletRepository.findCreditedAmount(id);
        double debitedAmount = walletRepository.findDebitedAmount(id);
        double amountInWallet = creditAmount-debitedAmount;
        return amountInWallet;
    }

    @Override
    public void save(Wallet wallet) {
        walletRepository.save(wallet);
    }
}
