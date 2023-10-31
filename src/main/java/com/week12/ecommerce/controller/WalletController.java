package com.week12.ecommerce.controller;


import com.week12.ecommerce.model.Wallet;
import com.week12.ecommerce.security.CustomUser;
import com.week12.ecommerce.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class WalletController {
    private WalletService walletService;
    @GetMapping("user_home/wallet")
    public String loadAllWallet(Authentication authentication,
                                Model model){
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        List<Wallet> walletList = walletService.findAllByUserId(customUser.getId());
        double amountInWallet = walletService.findSumOfWalletAmount(customUser.getId());
        model.addAttribute("walletHistory",walletList);
        model.addAttribute("amountInWallet",amountInWallet);
        model.addAttribute("users",customUser);
        return "wallet";
    }
}
