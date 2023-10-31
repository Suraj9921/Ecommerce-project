package com.week12.ecommerce.service;

import com.week12.ecommerce.model.Category;
import com.week12.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }

    public Category findById(long id) {
        Optional<Category> optionalCategory = categoryRepository.findById((int) id);
        return optionalCategory.orElse(null);
    }

    public List<Category> findAllCurrentCategories() {
        Optional<List<Category>> optionalCategoryList = categoryRepository.findAllCurrentCategories();
        return optionalCategoryList.orElseThrow(()->new RuntimeException("Couldn't fet category details"));
    }
    public List<Category> findAllByActivatedTrue() {
        return categoryRepository.findAllByActivatedTrue();
    }

    public void addCategory(Category category){
        categoryRepository.save(category);
    }
    public void removeCategoryById(int id){
        categoryRepository.deleteById(id);
    }
    public Optional<Category> getCategoryById(int id){
        return categoryRepository.findById(id);
    }

    public boolean isCategoryExists(String name){return categoryRepository.existsCategoriesByName(name);}

    public Long countAllCategories() {
        return categoryRepository.countAllCategories();
    }
}
