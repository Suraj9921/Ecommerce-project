package com.week12.ecommerce.serviceimpl;

import com.week12.ecommerce.model.Order;
import com.week12.ecommerce.model.OrderDetails;
import com.week12.ecommerce.model.Product;
import com.week12.ecommerce.repository.OrderRepo;
import com.week12.ecommerce.service.OrderService;
import com.week12.ecommerce.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    ProductService productService;

    @Override
    public Order saveOrder(Order order){
        System.out.println("in save order method");
        return orderRepo.save(order);
    }

    @Override
    public List<Order> getOrderForCurrentUser(Principal principal) {
        if (principal == null ){
            throw new IllegalStateException("No user is logged in as of now");
        }
        String username = principal.getName();
        return orderRepo.findByUserEmail(username);
    }

    @Override
    public List<Order> getSalesData(Date fromDate, Date toDate) {
        List<Order> orders = orderRepo.findByOrderDateBetween(fromDate,toDate);
        orders.sort(Comparator.comparing(Order :: getOrderDate));
        return orders;
    }

    @Override
    public void cancel(Long orderId) {
        Optional<Order> order = orderRepo.findById(orderId);
        if (order.isPresent()){
            Order order1 =order.get();
            List<OrderDetails> orderDetails = order1.getOrderDetails();
            for (OrderDetails orderDetails1 :orderDetails){
                Product product = orderDetails1.getProduct();
                System.out.println(" "+product);
                int ordered = order1.getQuantity();
                int newqty = ordered + product.getQty();
                product.setQty(newqty);
                productService.addProduct(product);
            }
            order1.setOrderStatus("CANCELED");
            orderRepo.save(order1);
        } else {
            throw new RuntimeException("Order not found for ID :" +orderId);
        }

    }

    @Override
    public Double getTotalOrderAmount() {
        return orderRepo.getTotalConfirmedOrdersAmount();
    }

    @Override
    public Optional<Order> findOrderById(Long orderId) {
        return orderRepo.findById(orderId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public void updateOrderStatus(Long orderId, String newStatus) {
        Optional<Order> optionalOrder =orderRepo.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setOrderStatus(newStatus);
            orderRepo.save(order);
        } else {
            throw new RuntimeException("Order not found for ID :" +orderId);
        }
    }

    @Override
    public Long countTotalConfirmedOrders() {
        return orderRepo.countAllConfirmedOrders();
    }

    @Override
    public Double getTotalAmountForMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate=currentDate.withDayOfMonth(1);
        LocalDate endDate=currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        Double totalAmount = orderRepo.getTotalConfirmedOrdersAmountForMonth(startDate,endDate,"DELIVERED");

        return totalAmount;
    }

    @Override
    public List<Long> findAllOrderCountForEachMonth() {
        List<Long>orderCounts=new ArrayList<>();
        LocalDate currentDate = LocalDate.now().withMonth(1);

        for(int i=0 ; i < 12 ; i++){
            LocalDate localStartDate=currentDate.withDayOfMonth(1);
            LocalDate localEndDate=currentDate.withDayOfMonth(currentDate.lengthOfMonth());

            long orderCount= orderRepo.countByOrderDateBetweenAndOrderStatus(localStartDate,localEndDate,"Delivered");
            orderCounts.add(orderCount);
            currentDate = currentDate.plusMonths(1);

        }


        return orderCounts;
    }
    @Override
    public List<Double> getTotalAmountForEachMonth() {

        List<Double> totalRevenuePerMonth = new ArrayList<>();
        LocalDate currentDate = LocalDate.now().withMonth(1);

        for(int i=0 ; i < 12 ; i++){
            LocalDate localStartDate=currentDate.withDayOfMonth(1);
            LocalDate localEndDate=currentDate.withDayOfMonth(currentDate.lengthOfMonth());

            Double totalRevenue = orderRepo.getTotalConfirmedOrdersAmountForMonth(localStartDate,localEndDate,"Delivered");
            totalRevenuePerMonth.add(totalRevenue);
            currentDate = currentDate.plusMonths(1);

        }

        return totalRevenuePerMonth;
    }
}
