package com.week12.ecommerce.controller;

import com.week12.ecommerce.dto.Report;
import com.week12.ecommerce.model.Order;
import com.week12.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ChartController {
    @Autowired
    OrderService orderService;
    @GetMapping("/admin/generateSalesChart")
    public List<Report> generateSalesChart(@RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                           @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        List<Order> sales = orderService.getSalesData(fromDate, toDate);
        return generateReport(sales);
    }
    private List<Report> generateReport(List<Order> sales) {
        Map<String, Report> dailySalesMap = new LinkedHashMap<>();

        for (Order order : sales) {
            String orderDate = formatDate(order.getOrderDate());

            double orderTotal = order.getTotalPrice();

            Report report = dailySalesMap.getOrDefault(orderDate, new Report());
            report.setDate(orderDate);
            report.setNum(report.getNum() + 1);
            report.setTotal(report.getTotal() + orderTotal);
            dailySalesMap.put(orderDate, report);
        }

        List<Report> dailySalesReport = new ArrayList<>(dailySalesMap.values());
        return dailySalesReport;
    }
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
