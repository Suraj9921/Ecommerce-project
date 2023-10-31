package com.week12.ecommerce.serviceimpl;

import com.week12.ecommerce.enums.TransactionType;
import com.week12.ecommerce.model.ReferralOffer;
import com.week12.ecommerce.model.Wallet;
import com.week12.ecommerce.repository.ReferralOfferRepository;
import com.week12.ecommerce.service.ReferralOfferService;
import com.week12.ecommerce.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
@AllArgsConstructor
public class ReferralOfferServiceImpl implements ReferralOfferService {
    private ReferralOfferRepository referralOfferRepository;
    private WalletService walletService;
    @Override
    public void saveReferral(long usersId, String token,String email) {

        ReferralOffer referralOffer = new ReferralOffer();
        referralOffer.setReferralCode(token);
        referralOffer.setUsersId(usersId);
        referralOffer.setSendDate(new Date());
        referralOffer.setAmount(100);
        referralOffer.setSenderEmail(email);
        try{
            referralOfferRepository.save(referralOffer);
            //code to sendemail

        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Internalservererror");
        }

    }

    @Override
    public ReferralOffer findByReferralCode(String token) {
        return referralOfferRepository.findByReferralCode(token);
    }

    @Override
    public void addReferralAmount(String email) {
        ReferralOffer referralOffer= referralOfferRepository.findBySenderMail(email);
        if(referralOffer!=null){
            Wallet wallet = new Wallet();
            wallet.setUsersId(referralOffer.getUsersId());
            wallet.setAmount(referralOffer.getAmount());
            wallet.setTransactionType(TransactionType.CREDITED);
            wallet.setUpdateOn(new Date());
            walletService.save(wallet);
            referralOffer.setDeleted(true);
            referralOfferRepository.save(referralOffer);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
       return referralOfferRepository.existsBySenderEmail(email);
    }

    @Override
    public ReferralOffer findBySenderMail(String email) {
        return referralOfferRepository.findBySenderMail(email);
    }

    @Override
    public void update(ReferralOffer referralOffer) {
        referralOfferRepository.save(referralOffer);
    }
}
