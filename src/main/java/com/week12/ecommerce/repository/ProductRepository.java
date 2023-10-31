package com.week12.ecommerce.repository;

import com.week12.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategory_Id(int id);

    Product findById(long id);

    boolean existsProductByName(String name);

    List<Product> findAllByCategoryId(Long id);

    @Query(value = "select * from product where is_activated=true", nativeQuery = true)
    Optional<List<Product>> findCurrentProducts();

    @Query(value = "select * from product where is_activated = true and category_id = :id", nativeQuery = true)
    List<Product> findAllByActivatedTrue(@Param("id") long id);

    @Query(value = "select * from product where is_activated = true", nativeQuery = true)
    List<Product> findAllByActivatedTrue();

    List<Product> findAllByNameContainingIgnoreCase(String keyword);

    @Query(value = "SELECT * FROM product WHERE is_activated = true ORDER BY CASE WHEN :sort = 'lowToHigh' THEN price END ASC, CASE WHEN :sort = 'highToLow' THEN price END DESC", nativeQuery = true)
    List<Product> findAllByActivatedTrueAndSortBy(@Param("sort") String sort);

    @Query(value = "SELECT p.id, p.name, c.name, " +
            "SUM(od.quantity1) AS total_quantity_ordered, SUM(od.quantity1 * p.price) AS total_revenue " +
            "FROM product p " +
            "JOIN order_details od ON p.id = od.product_id " +
            "JOIN orders o ON od.order_id = o.order_id " +
            "JOIN category c ON p.category_id = c.category_id " +
            "WHERE o.order_Status = 'DELIVERED' " +
            "AND o.order_date BETWEEN :startDate AND :endDate " +
            "GROUP BY p.id, p.name, c.name " +
            "ORDER BY total_revenue DESC",nativeQuery = true)
    List<Object[]> getProductsStatsForConfirmedOrdersBetweenDates(@Param("startDate") Date startDate,@Param("endDate") Date endDate);

    @Query(value = "SELECT p.id, p.name, c.name, " +
            "SUM(od.quantity1) AS total_quantity_ordered, SUM(od.quantity1 * p.price) AS total_revenue " +
            "FROM product p " +
            "JOIN order_details od ON p.id = od.product_id " +
            "JOIN orders o ON od.order_id = o.order_id " +
            "JOIN category c ON p.category_id = c.category_id " +
            "WHERE o.order_Status = 'DELIVERED' " +
            "GROUP BY p.id, p.name, c.name " +
            "ORDER BY total_revenue DESC",nativeQuery = true)
    List<Object[]> getProductStatsForConfirmedOrders();


    @Query(value = "select count(*) from Product")
    Long CountAllProducts();

}
