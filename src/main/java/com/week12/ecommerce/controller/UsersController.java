package com.week12.ecommerce.controller;

import com.week12.ecommerce.model.*;
import com.week12.ecommerce.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {
    private UsersSevice usersService;
    private ReferralOfferService referralOfferService;


    private EmailService emailService;
    private UserOTPService userOTPService;
    private PasswordEncoder passwordEncoder;
//    private CouponService couponService;
    private ShoppingCartService shoppingCartService;
    public UsersController(UsersSevice usersSevice,
                           EmailService emailService,
                           UserOTPService userOTPService,
                           PasswordEncoder passwordEncoder,ReferralOfferService referralOfferService) {
        this.usersService = usersSevice;
        this.emailService = emailService;
        this.userOTPService = userOTPService;
        this.passwordEncoder = passwordEncoder;
        this.referralOfferService = referralOfferService;
    }

    @PostMapping("user-registration")
    public String userRegistration(@ModelAttribute("newUsers") Users newUser,
                                   BindingResult bindingResult, HttpSession httpSession) throws Exception {
        if(usersService.findAlreadyExistUserByEmail(newUser.getEmail())){
            bindingResult.rejectValue("email",null,"There exists user with this username");
        }
        if(bindingResult.hasErrors()){
            return "redirect:/signup?error";
        }else{
            try{
                String encodedPassword = passwordEncoder.encode(newUser.getPassword());
                newUser.setPassword(encodedPassword);
                newUser.setRole("CUSTOMER");
                newUser.setActive(true);
                newUser.setCreatedAt(new Date());
                newUser.setUpdateOn(new Date());
                usersService.saveOrUpdate(newUser);
                UserOTP userOTP = userOTPService.findByEmail(newUser.getEmail());
                if (userOTP != null) {
                    userOTP.setOneTimePassword(null);
                    userOTP.setOtpRequestedTime(new Date()); // Set the current timestamp
                    userOTP.setUpdateOn(new Date()); // Set the current timestamp
                    userOTPService.saveOrUpdate(userOTP);
                }
//                httpSession.setAttribute("message","OTP is send to registered email.Please enter the Message within 5 minutes");
                httpSession.setAttribute("message","You have successfully registered. Try to login");
                return "redirect:/signup?sendEmailOtp";
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("Can not save customer details");

            }

        }
    }

    @GetMapping("/user")
    public String userDash(@ModelAttribute("user")Users user, Model model){
        String name = user.getFirstName();
        model.addAttribute("name",name);
        return "userDashboard";
    }

    @GetMapping("/user/edit")
    public String editUserProfile(Principal principal, Model model) {
        // Get the currently logged-in user's email
        String email = principal.getName();

        // Find the user by email
        Users user = usersService.findUserByEmail(email);

        if (user == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/user";
        }

        model.addAttribute("user", user);
        return "editUser";
    }

    // Add a new endpoint to handle user profile update
    @PostMapping("/user/update")
    public String updateUserProfile(@ModelAttribute("user") Users updatedUser, Principal principal, Model model) {
        // Get the currently logged-in user's email
        String email = principal.getName();

        // Find the user by email
        Users user = usersService.findUserByEmail(email);

        if (user == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/user";
        }

        // Update user details
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setPhoneNumber(updatedUser.getPhoneNumber());

        // Check if the password is being updated
        if (!updatedUser.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
            user.setPassword(encodedPassword);
        }

        usersService.saveOrUpdate(user);

        return "redirect:/user";
    }

    @Autowired
    OrderService orderService;

    @GetMapping("/orders")
    public String getOrders(Model model, Principal principal){
        if (principal != null){
            List<Order> orderList =orderService.getOrderForCurrentUser(principal);
            model.addAttribute("orderList",orderList);
        }
        return "OrderManage";
    }

    @PostMapping("/orders/cancel/{orderId}")
    public String cancelOrder(@PathVariable Long orderId){
        Optional<Order> order = orderService.findOrderById(orderId);
        if (order.isPresent()) {
            System.out.println("order is present");
            orderService.cancel(orderId);
        } else {
            System.out.println("order is not present");
        }
        return "redirect:/orders";
    }

    @Autowired
    AddressService addressService;

    @GetMapping("/address")
    public String getAddress(Model model,Principal principal){
        Users user = usersService.findUserByUsername(principal.getName());
        List<Address> addresses = user.getAddresses();
        model.addAttribute("addresses",addresses);
        return "AddressManage";
    }

    @GetMapping("/addresses/delete/{addressId}")
    public String deleteAddress(@PathVariable int addressId) {
        addressService.deleteAddressById(addressId);
        return "redirect:/address";
    }

    @GetMapping("/users/addAddress")
    public String addAddress(@ModelAttribute ("address") Address address ,Model model){
        return "AddAddress";
    }

    @PostMapping("/users/addAddress")
    public String adAddress(@ModelAttribute("address") Address address, Principal principal, BindingResult result,Model model){
        if (result.hasFieldErrors()){
            model.addAttribute("address",address);
            return "AddAddress";
        }
        Users user = usersService.findUserByUsername(principal.getName());
        address.setUser(user);
        addressService.addAddress(address);
        return "redirect:/address";
    }

//    @RequestMapping(value = "/check-out/apply-coupon", method = RequestMethod.POST, params = "action=apply")
//    public String applyCoupon(@RequestParam("couponCode")String couponCode, Principal principal,
//                              RedirectAttributes attributes, HttpSession session){
//
//
//        if(couponService.findByCouponCode(couponCode.toUpperCase())) {
//
//            Coupon coupon = couponService.findByCode(couponCode.toUpperCase());
//            ShoppingCart cart = usersService.findByEmail(principal.getName()).getCart();
//            Double totalPrice = cart.getTotalPrice();
//            session.setAttribute("totalPrice",totalPrice);
//            Double newTotalPrice = couponService.applyCoupon(couponCode.toUpperCase(), totalPrice);
//
//            shoppingCartService.updateTotalPrice(newTotalPrice, principal.getName());
//
//            attributes.addFlashAttribute("success", "Coupon applied Successfully");
//            attributes.addAttribute("couponCode", couponCode);
//            attributes.addAttribute("couponOff", coupon.getOffPercentage());
//            return "redirect:/shoping-cart";
//        }else{
//            attributes.addFlashAttribute("error", "Coupon Code invalid");
//            return "redirect:/shoping-cart";
//        }
//
//    }
//
//    @RequestMapping(value = "/check-out/apply-coupon", method = RequestMethod.POST, params = "action=remove")
//    public String removeCoupon(Principal principal,RedirectAttributes attributes,
//                               HttpSession session){
//
//        Double totalPrice = (Double) session.getAttribute("totalPrice");
//        shoppingCartService.updateTotalPrice(totalPrice, principal.getName());
//        attributes.addFlashAttribute("success", "Coupon removed Successfully");
////        session.removeAttribute("totalPrice");
//
//        return "redirect:/shoping-cart";
//    }

    @PostMapping("user-registration/referral")
    public String saveReferralUser(@ModelAttribute("newUsers")Users newUser,
                                   BindingResult bindingResult, HttpSession httpSession) throws Exception {
        if(usersService.findAlreadyExistUserByEmail(newUser.getEmail())){
            bindingResult.rejectValue("email",null,"There exists user with this username");
        }
        if(bindingResult.hasErrors()){
            return "redirect:/signup?error";
        }else{
            try{
                String encodedPassword = passwordEncoder.encode(newUser.getPassword());
                newUser.setPassword(encodedPassword);
                newUser.setRole("CUSTOMER");
                newUser.setActive(true);
                newUser.setCreatedAt(new Date());
                newUser.setUpdateOn(new Date());
                usersService.saveOrUpdate(newUser);
                UserOTP userOTP = userOTPService.findByEmail(newUser.getEmail());
                if(userOTP!=null){
                    userOTP.setOneTimePassword(null);
                    userOTP.setOtpRequestedTime(null);
                    userOTP.setUpdateOn(new Date());
                    userOTPService.saveOrUpdate(userOTP);
                }
                httpSession.setAttribute("message","User registered successfully");
                referralOfferService.addReferralAmount(newUser.getEmail());
                return "redirect:/signup?sendEmailOtp";
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("Can not save customer details");

            }

        }
    }
}
