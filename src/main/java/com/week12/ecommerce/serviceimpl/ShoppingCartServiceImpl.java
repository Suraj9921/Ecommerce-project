package com.week12.ecommerce.serviceimpl;

import com.week12.ecommerce.model.CartItem;
import com.week12.ecommerce.model.Product;
import com.week12.ecommerce.model.ShoppingCart;
import com.week12.ecommerce.model.Users;
import com.week12.ecommerce.repository.CartItemRepo;
import com.week12.ecommerce.repository.ShoppingCartRepo;
import com.week12.ecommerce.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private UsersSeviceImpl userService;
    @Autowired
    private CartItemRepo itemRepo;
    @Autowired
    private ShoppingCartRepo shoppingCartRepo;

//    @Override
//    public ShoppingCart addItemToCart(Product product, int qty, String username) {
//        User user = userService.findUserByUsername(username);
//        ShoppingCart shoppingCart = user.getCart();
//        if (shoppingCart == null ){
//            System.out.println("Shopping cart for this user is null, so creating new cart");
//            shoppingCart = new ShoppingCart();
//        }
//        Set<CartItem> cartItems = shoppingCart.getCartItems();
//        CartItem cartItem = find(cartItems, product.getId());
//        double unitPrice = product.getPrice();
//        int itemQty = 0;
//        if (cartItems == null){
//            cartItems = new HashSet<>();
//            if(cartItem == null){
//                cartItem = new CartItem();
//                cartItem.setCart(shoppingCart);
//                cartItem.setProduct(product);
//                cartItem.setQuantity(qty);
//                cartItem.setUnitPrice(unitPrice);
//                cartItems.add(cartItem);
//                itemRepo.save(cartItem);
//            }else{
//                itemQty = cartItem.getQuantity() + qty;
//                cartItem.setQuantity(itemQty);
//                itemRepo.save(cartItem);
//            }
//
//        }else {
//            if (cartItem == null){
//                cartItems = new HashSet<>();
//                cartItem = new CartItem();
//                cartItem.setCart(shoppingCart);
//                cartItem.setProduct(product);
//                cartItem.setQuantity(qty);
//                cartItem.setUnitPrice(unitPrice);
//                cartItems.add(cartItem);
//                itemRepo.save(cartItem);
//            }else {
//                itemQty = cartItem.getQuantity() + qty;
//                cartItem.setQuantity(itemQty);
//                itemRepo.save(cartItem);
//            }
//        }
//        shoppingCart.setCartItems(cartItems);
//
//        double totalPrice = totalPrice(shoppingCart.getCartItems());
//        int totalItem = totalItem(shoppingCart.getCartItems());
//
//        shoppingCart.setTotalItems(totalItem);
//        shoppingCart.setTotalPrice(totalPrice);
//        shoppingCart.setUser(user);
//        return shoppingCartRepo.save(shoppingCart);
//    }

    @Override
    public ShoppingCart addItemToCart(Product product, int qty, String username) {
        Users user = userService.findUserByUsername(username);
        ShoppingCart shoppingCart = user.getCart();
        if (shoppingCart == null) {
            System.out.println("Shopping cart for this user is null, so creating a new cart");
            shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user); // Set the user for the cart
            shoppingCart.setCartItems(new HashSet<>()); // Initialize cartItems
        }

        Set<CartItem> cartItems = shoppingCart.getCartItems();
        double unitPrice = product.getPrice();

        CartItem existing = null;
        for (CartItem item :cartItems){
            if (item.getProduct().getId() == product.getId()){
                existing = item;
                break;
            }
        }
        if (existing != null ){
            existing.setQuantity(existing.getQuantity() + qty);
        }else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(shoppingCart);
            cartItem.setProduct(product);
            cartItem.setQuantity(qty);
            cartItem.setUnitPrice(unitPrice);
            cartItems.add(cartItem);
            itemRepo.save(cartItem);
        }


        double totalPrice = totalPrice(cartItems);
        int totalItem = totalItem(cartItems);
        shoppingCart.setTotalItems(totalItem);
        shoppingCart.setTotalPrice(totalPrice);

        shoppingCart = shoppingCartRepo.save(shoppingCart);

        return shoppingCart;
    }

    @Override
    public ShoppingCart updateTotalPrice(Double newTotalPrice,String username) {
        Users user = userService.findByEmail(username);
        ShoppingCart shoppingCart = user.getCart();
        shoppingCart.setTotalPrice(newTotalPrice);

        shoppingCartRepo.save(shoppingCart);


        return shoppingCart;
    }

    @Override
    public ShoppingCart removeItemFromCart(Product product, String name) {
        Users user = userService.findUserByUsername(name);
        ShoppingCart shoppingCart = user.getCart();
        System.out.println("in remove method");
        if (shoppingCart != null){
            System.out.println("shopping cart not null");
            Set<CartItem> cartItems = shoppingCart.getCartItems();
            CartItem itemToRemove =cartItems.stream().filter(cartItem -> cartItem.getProduct()
                    .getId() == product.getId()).findFirst().orElse(null);
            System.out.println(" " +product.getName());

            if (itemToRemove != null){
                cartItems.remove(itemToRemove);
                itemRepo.delete(itemToRemove);
            }
        }
        return shoppingCart;
    }

    @Override
    public void clearCart(String username) {
        Users user= userService.findUserByUsername(username);
        ShoppingCart cart = user.getCart();
        if (cart != null) {
            Set<CartItem> cartItems = cart.getCartItems();
            for (CartItem cartItem : cartItems) {
                System.out.println("Product ID: " + cartItem.getProduct().getId());
                System.out.println("Quantity: " + cartItem.getQuantity());
                System.out.println("Before clear cart");
                // Print other relevant details
            }
            cartItems.forEach(cartItem -> cartItem.setCart(null));
            cartItems.clear();
            for (CartItem cartItem : cartItems) {
                System.out.println("Product ID: " + cartItem.getProduct().getId());
                System.out.println("Quantity: " + cartItem.getQuantity());
                // Print other relevant details
                System.out.println("After clear cart");
            }
            cart.setTotalItems(0);
            cart.setTotalPrice(0.0);
            shoppingCartRepo.save(cart);
        }
    }


    private int totalItem(Set<CartItem> cartItems) {
        int totalItem = 0;
        for (CartItem item : cartItems) {
            totalItem += item.getQuantity();
        }
        return totalItem;
    }

    private double totalPrice(Set<CartItem> cartItems) {
        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    private CartItem find(Set<CartItem> cartItems, long id){
        if (cartItems == null){
            return null;
        }
        CartItem cartItem =null;
        for (CartItem item : cartItems){
            if (item.getProduct().getId() == id){
                cartItem = item;
            }
        }
        return cartItem;
    }
}
