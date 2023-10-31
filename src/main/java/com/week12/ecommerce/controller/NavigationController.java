package com.week12.ecommerce.controller;

import com.week12.ecommerce.model.UserOTP;
import com.week12.ecommerce.model.Users;
import com.week12.ecommerce.repository.UsersRepository;
import com.week12.ecommerce.service.UsersSevice;
import com.week12.ecommerce.serviceimpl.UsersSeviceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class NavigationController {

    private final UsersSeviceImpl usersService;

    public  BCryptPasswordEncoder passwordEncoder;

    private final UsersRepository usersRepository;


    public NavigationController(UsersSeviceImpl usersService, BCryptPasswordEncoder passwordEncoder, UsersRepository usersRepository) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/")
    public String showIndex(){
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage(){
        return "login";
    }

    @GetMapping("/signup")
    public  String showSignUp(Model model){
        String email = (String) model.asMap().get("email");
        Users users = new Users();
        users.setEmail(email);
        model.addAttribute("newUsers",users);
        return "signup";
    }
    @GetMapping("/verifyEmail")
    public String showVerifyEmail(){
        return "verifyEmail";
    }
    @GetMapping("/otpvalidation")
    public String showotpvalidationPage(Model model, HttpSession session){
        String email = (String) model.asMap().get("email");
        UserOTP userOTP = new UserOTP();
        userOTP.setEmail(email);
        session.setAttribute("email",email);
        model.addAttribute("userOTP",userOTP);
        return "otpvalidation";
    }


    @GetMapping("/forgot-password")
    public String forgotPasswordOTP(Model model, Users users){

        model.addAttribute("title", "Forgot Password- OTP");
        model.addAttribute("username",users);

        return "enterUsername";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordOTPSend(@ModelAttribute("username") Users users, Model model){
        String otp= usersService.genrateOTPAndSendOnEmail(users.getEmail());
        model.addAttribute("data",users);
        model.addAttribute("otpGenerationTime",System.currentTimeMillis());
        return "otpScreenEmail";
    }

    @PostMapping("/forgot-password/otpVerification")
    public String otpSentEmailPost(@ModelAttribute("data") Users users , Model model, RedirectAttributes attributes) {
        boolean isOTPValid = usersService.verifyOTP(users.getOtp(),users.getEmail());
        if (isOTPValid) {
            model.addAttribute("user",users);
            return "passwordResetPage";
        } else {
            model.addAttribute("error", "Invalid OTP. Please try again.");
            return "otpScreenEmail";
        }
    }

    @PostMapping("/passwordResetPage")
    public String passwordResetPage(@ModelAttribute("user") Users users, Model model, RedirectAttributes redirectAttributes){

        System.out.println(users);
        System.out.println("here");
        System.out.println(users.getPassword());
        System.out.println(users.getRepeatPassword());
        if(users.getPassword().equals(users.getRepeatPassword())){
            Users customer=usersRepository.findByEmail(users.getEmail());
            customer.setPassword(passwordEncoder.encode(users.getPassword()));
            usersRepository.save(customer);
            redirectAttributes.addFlashAttribute("success", "Password is changed");
        }
        else{
            redirectAttributes.addFlashAttribute("error", "Passwords are not same");

        }

        return "redirect:/login";
    }
//    @GetMapping("/forgotpassword")
//    public String showForgotpasswordPage(){
//        return "forgotpassword";
//    }
//
//    @GetMapping("/forgotPasswordOTPLogin")
//    public String showForgotPasswordOTPLogin(Model model,HttpSession session){
//        String email = (String) model.asMap().get("email");
//        UserOTP userOTP = new UserOTP();
//        userOTP.setEmail(email);
//        session.setAttribute("email",email);
//        model.addAttribute("userOTP",userOTP);
//        return "forgotPasswordOTPLogin";
//    }
    @GetMapping("/admin_panel")
    public String showAdminPanel(){
        return "admin_panel";
    }
    @GetMapping("/user_home")
    public String showUserHomePage(){
        return "user_home";
    }

//    @GetMapping("/admin_panel")
//    public String showAdminPanel(Model model, ModelMap modelMap){
//        // code to get total revenue
//        double codRevenue = orderDetailsService.findTotalDeliveredCODRevenue();
//        double onlineRevenue = orderPaymentsService.findPaidRevenue();
//        double totalRevenue = codRevenue+onlineRevenue;
//        model.addAttribute("codRevenue",codRevenue);
//        model.addAttribute("onlineRevenue",onlineRevenue);
//        model.addAttribute("totalRevenue",totalRevenue);
//
//        // code to get total order
//        long cancelledOrder = orderDetailsService.findTotalOrdersByOrderStatus("CANCELLED");
//        long pendigOrder = orderDetailsService.findTotalOrdersByOrderStatus("ORDERED");
//        long deliveredOrder = orderDetailsService.findTotalOrdersByOrderStatus("DELIVERED");
//        long totalOrders=cancelledOrder+pendigOrder+deliveredOrder;
//
//        model.addAttribute("cancelledOrder",cancelledOrder);
//        model.addAttribute("pendigOrder",pendigOrder);
//        model.addAttribute("deliveredOrder",deliveredOrder);
//        model.addAttribute("totalOrders",totalOrders);
//
//        double orderedPercent = (double)(pendigOrder*100/totalOrders);
//        double deliveredPercent = (double)(deliveredOrder*100/totalOrders);
//        double cancelledPercent = (double)(cancelledOrder*100/totalOrders);
//
//        model.addAttribute("orderedPercent",orderedPercent);
//        model.addAttribute("deliveredPercent",deliveredPercent);
//        model.addAttribute("cancelledPercent",cancelledPercent);
//        //code to fetch products count
//        long totalProducts = productService.findTotalNotDeletedProducts();
//        long outOfStockProducts = productService.findOutOfStockProducts();
//        long activeProducts = productService.findActiveProducts();
//        long inactiveProducts = productService.findInActiveProducts();
//
//
//        model.addAttribute("totalProducts",totalProducts);
//        model.addAttribute("outOfStockProducts",outOfStockProducts);
//        model.addAttribute("activeProducts",activeProducts);
//        model.addAttribute("inactiveProducts",inactiveProducts);
//
//        //code to fetch category count
//        long totalCategories = categoryService.findTotalCategoryCount();
//        long blockedCategories = categoryService.findBlockedCategoryCount();
//        long unblockedCategories = categoryService.findUnblockedCategoryCount();
//
//        model.addAttribute("totalCategories",totalCategories);
//        model.addAttribute("blockedCategories",blockedCategories);
//        model.addAttribute("unblockedCategories",unblockedCategories);
//        //code to fetch monthly revenue
//
//        Double monthlyCodRevenue = orderDetailsService.findMonthlyDeliveredCODRevenue();
//        Double monthlyOnlineRevenue = orderPaymentsService.findMonthlyPaidRevenue();
//        Double monthlyRevenue = monthlyCodRevenue+onlineRevenue;
//        model.addAttribute("monthlyCodRevenue",monthlyCodRevenue);
//        model.addAttribute("monthlyOnlineRevenue",monthlyOnlineRevenue);
//        model.addAttribute("monthlyRevenue",monthlyRevenue);
//
//        List<Object[]> objList = orderDetailsService.getMonthlySaleChartDetails();
////
////        List<String> nameList= productService.findAll().stream().map(x->x.getName()).collect(Collectors.toList());
////        List<Long> ageList = productService.findAll().stream().map(x-> x.getCurrentQuantity()).collect(Collectors.toList());
//
//        List<String> datelist = objList.stream()
//                .map(obj -> (String) obj[0])
//                .collect(Collectors.toList());
//
//        List<Double> amountlist = objList.stream()
//                .map(obj -> (Double) obj[1])
//                .collect(Collectors.toList());
//
//        model.addAttribute("month", datelist);
//        model.addAttribute("sale", amountlist);
//        return "admin/admin_panel";
//    }
}
