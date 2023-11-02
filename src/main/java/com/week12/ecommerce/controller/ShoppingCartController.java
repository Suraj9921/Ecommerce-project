package com.week12.ecommerce.controller;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.week12.ecommerce.model.*;
import com.week12.ecommerce.service.AddressService;
import com.week12.ecommerce.service.ProductService;
import com.week12.ecommerce.service.UsersSevice;
import com.week12.ecommerce.serviceimpl.OrderServiceImpl;
import com.week12.ecommerce.serviceimpl.ShoppingCartServiceImpl;
import com.week12.ecommerce.serviceimpl.UsersSeviceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
public class ShoppingCartController {


    @Autowired
    private ShoppingCartServiceImpl cartService;

    @Autowired
    private UsersSeviceImpl usersService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private OrderServiceImpl orderService;

//    @Autowired
//    private CouponService couponService;
    @GetMapping("/cart")
    public String getCart(Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        model.addAttribute("name",principal.getName());
        ShoppingCart cart = usersService.findByEmail(principal.getName()).getCart();
        Users user = usersService.findUserByUsername(principal.getName());
        ShoppingCart shoppingCart = user.getCart();
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        model.addAttribute("cartItems",cartItems);
        model.addAttribute("shoppingCart", cart);
        return "shoping-cart";
    }

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable("id") Long id, @RequestParam(value = "qty", required = false, defaultValue = "1") int qty,
                            HttpServletRequest request, Model model, Principal principal, HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        }
        Product product = productService.getProductById(id).get();
        if (qty > product.getQty()){

            model.addAttribute("error", "Ordered quantity exceeds quantity in stock");
            return "redirect:/addToCart/{id}";
        }

//        Product product = productService.getProductByProductId(id).get() ;
        Users user = usersService.findUserByUsername(principal.getName());
        ShoppingCart cart = user.getCart();
        if (cart == null){
            cart = new ShoppingCart();
            cart.setUser(user);
        }
        cartService.addItemToCart(product,qty, principal.getName());
        model.addAttribute("success","added to cart successfully");
        return "redirect:/cart";
    }
    @GetMapping("/cart/removeItem/{id}")
    public String removeFromCart(@PathVariable("id") Long id,
                                 HttpServletRequest request,
                                 Model model, Principal principal,
                                 HttpSession session){
        if(principal == null){
            return "redirect:/login";
        }
        Product product = productService.getProductById(id).orElse(null);
        if (product == null ){
            return "redirect:/cart";
        }
        cartService.removeItemFromCart(product , principal.getName());
        model.addAttribute("success", "Item Removed Succesfully");
        return "redirect:/cart";
    }

//    @DeleteMapping("/cart/removeItem/{id}")
//    public String removeFromCart(@PathVariable("id") Long id, HttpServletRequest request, Model model, Principal principal, HttpSession session) {
//        if (principal == null) {
//            return "redirect:/login";
//        }
//
//        Product product = productService.getProductById(id).orElse(null);
//        if (product == null) {
//            return "redirect:/cart";
//        }
//
//        cartService.removeItemFromCart(product, principal.getName());
//        model.addAttribute("success", "Item Removed Successfully");
//        return "redirect:/cart";
//    }



    @GetMapping("/checkout")
    public String checkout(Model model,
                           Principal principal){
        Users user= usersService.findUserByUsername(principal.getName());
        List<Address> addresses =addressService.getAllAddressesByUser(user);
        ShoppingCart cart = user.getCart();
        double totalprice= cart.getTotalPrice();
        model.addAttribute("user", user);
        model.addAttribute("addresses",addresses);
        model.addAttribute("totalprice", totalprice);
        model.addAttribute("paymentMethod", "CashOnDelivery");
        return "checkout2";
    }

//    @RequestMapping(value = "/check-out/apply-coupon", method = RequestMethod.POST, params = "action=apply")
//    public String applyCoupon(@RequestParam("couponCode")String couponCode,Principal principal,
//                              RedirectAttributes attributes,HttpSession session){
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
//            cartService.updateTotalPrice(newTotalPrice, principal.getName());
//
//            attributes.addFlashAttribute("success", "Coupon applied Successfully");
//            attributes.addAttribute("couponCode", couponCode);
//            attributes.addAttribute("couponOff", coupon.getOffPercentage());
//            return "redirect:/checkout";
//        }else{
//            attributes.addFlashAttribute("error", "Coupon Code invalid");
//            return "redirect:/checkout";
//        }
//
//    }

//    @RequestMapping(value = "/check-out/apply-coupon", method = RequestMethod.POST, params = "action=remove")
//    public String removeCoupon(Principal principal,RedirectAttributes attributes,
//                               HttpSession session){
//
//        Double totalPrice = (Double) session.getAttribute("totalPrice");
//        cartService.updateTotalPrice(totalPrice, principal.getName());
//        attributes.addFlashAttribute("success", "Coupon removed Successfully");
////        session.removeAttribute("totalPrice");
//
//        return "redirect:/checkout";
//    }

    @PostMapping("/checkout/addAddress")
    public String addAddress(Address address, Principal principal){
        Users user =usersService.findUserByUsername(principal.getName());
        address.setUser(user);
        addressService.addAddress(address);
        return "redirect:/checkout";
    }
    @PostMapping(value = "/checkout/confirmOrder")
    public String confirmOrder(Order order,Model model,
                               Principal principal, @RequestParam("paymentMethod") String paymentMethod, RedirectAttributes attributes) {
        System.out.println("in checkout");
        System.out.println("Selected payment method: " + paymentMethod);
        try {
            Users user = usersService.findUserByUsername(principal.getName());
            ShoppingCart cart = user.getCart();
            Set <CartItem> cartItems = cart.getCartItems();
            for (CartItem cartItem : cartItems){
                Product product = cartItem.getProduct();
                int orderQty = cartItem.getQuantity();
                if (orderQty > product.getQty()){
                    attributes.addFlashAttribute("error", "Product " +product.getName() + "is out of stock");
                    model.addAttribute("error", "Product " + product.getName() + "is out of stock");
                    return "redirect:/cart";
                }
                product.setQty(product.getQty() - orderQty);
                productService.addProduct(product);
            }
            order.setOrderDate(new Date());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(order.getOrderDate());
            calendar.add(Calendar.DAY_OF_MONTH,7);
            order.setDeliveryDate(calendar.getTime());

//            order.setPaymentMethod(paymentMethod);
            if ("payNow".equals(paymentMethod)){
                order.setPaymentMethod("Razorpay");
            }else {
                order.setPaymentMethod(paymentMethod);
            }
            order.setOrderStatus("CONFIRMED");
            order.setUser(user);
            System.out.println("order user == " +order.getUser());
            order.setTotalPrice(cart.getTotalPrice());
            System.out.println(" " +order.getOrderDate());
            List<OrderDetails> orderDetails = new ArrayList<>();
            for (CartItem cartItem :cartItems){
                OrderDetails orderDetails1 = new OrderDetails();
                orderDetails1.setOrder(order);
                orderDetails1.setProduct(cartItem.getProduct());
                orderDetails1.setQuantity1(cartItem.getQuantity());
                orderDetails.add(orderDetails1);
//                System.out.println(" order details. product " +orderDetails1.getProduct());
//                System.out.println(" "+cartItem.getProduct()+" in order details for loop");
            }
            cartService.clearCart(user.getEmail());
            order.setOrderDetails(orderDetails);

            orderService.saveOrder(order);

            model.addAttribute("order", order);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "OrderConfirmed";
    }

    @RequestMapping(value = "/payNow",method = RequestMethod.POST)
    @ResponseBody
    public String payNow(@RequestBody Map<String,Object> data) throws RazorpayException {
        System.out.println(data);
        int amt = Integer.parseInt(data.get("amount").toString());
        var client =  new RazorpayClient("rzp_test_p5dhh6XFv0f6Rs","VlIhvQChcCqchZVP56VCSRK2");
        JSONObject ob = new JSONObject();
        ob.put("amount",amt*100);
        ob.put("currency","INR");
        ob.put("receipt","txn_235425");

        com.razorpay.Order order = client.orders.create(ob);
//        System.out.println(order);
        return order.toString();
    }
}
