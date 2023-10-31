package com.week12.ecommerce.repository;

import com.week12.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findCategoryByName(String name);

    boolean existsCategoriesByName(String name);

    @Query(value = "select * from category where is_activated = true", nativeQuery = true)
    List<Category> findAllByActivatedTrue();

    @Query(value = "select * from category where is_activated=true", nativeQuery = true)
    Optional<List<Category>> findAllCurrentCategories();

    @Query(value = "select COUNT(*) FROM Category")
    Long countAllCategories();

}

