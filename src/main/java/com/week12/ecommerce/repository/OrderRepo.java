package com.week12.ecommerce.repository;


import com.week12.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByUserEmail(String username);

    List<Order> findByOrderDateBetween(Date fromDate, Date toDate);

    @Query(value = "select COALESCE(SUM(o.totalPrice),0) FROM Order o where o.orderStatus = 'DELIVERED'")
    Double getTotalConfirmedOrdersAmount();

    @Query(value = "select COUNT(*) FROM Order o WHERE o.orderStatus='DELIVERED'")
    Long countAllConfirmedOrders();

    @Query(value = "select COALESCE(SUM(total_price), 0) from orders where order_date between :startDate and :endDate and order_status = :orderStatus",nativeQuery = true)
    Double getTotalConfirmedOrdersAmountForMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("orderStatus") String orderStatus);

    @Query(value = "select count(*) from orders where order_date between :startDate and :endDate and order_status = :orderStatus",nativeQuery = true)
    Long countByOrderDateBetweenAndOrderStatus(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("orderStatus") String orderStatus);

}
