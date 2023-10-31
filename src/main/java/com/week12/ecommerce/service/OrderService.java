package com.week12.ecommerce.service;



import com.week12.ecommerce.model.Order;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    public Order saveOrder(Order order);
    public List<Order> getOrderForCurrentUser(Principal principal);

    void cancel(Long orderId);

    Optional<Order> findOrderById(Long orderId);
    List<Order> getAllOrders();
    void updateOrderStatus(Long orderId, String newStatus);

    public List<Order> getSalesData(Date fromDate, Date toDate);

    Double getTotalOrderAmount();

    Long countTotalConfirmedOrders();

    Double getTotalAmountForMonth();

    List<Long> findAllOrderCountForEachMonth();

    List<Double> getTotalAmountForEachMonth();
}
