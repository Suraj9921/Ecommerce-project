package com.week12.ecommerce.controller;

import com.week12.ecommerce.dto.ProductDto;
import com.week12.ecommerce.model.Category;
import com.week12.ecommerce.model.Product;
import com.week12.ecommerce.service.CategoryService;
import com.week12.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @GetMapping("/index")
    public String home(Model model){
        return  "index";
    }

    @GetMapping("/shop")
    public String shop(Model model){
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("products",productService.getAllProduct());
        return "shop";
    }

    @GetMapping("/shop/category/{id}")
    public String shop(Model model, @PathVariable("id") int id){
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("products",productService.getAllProductsByCategoryId(id));
        System.out.println("category");
        return "shop";
    }

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(Model model, @PathVariable int id) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);
            return "ProductView";
        } else {
            // Handle the case where the product with the given ID doesn't exist
            return "ProductNotFound"; // You can create a separate view for this scenario
        }
    }


    @GetMapping("/about")
    public String about(Model model){
        return  "about";
    }

    @GetMapping("/contact")
    public String contact(Model model){
        return  "contact";
    }

    @GetMapping("/products-list")
    public String getShopPage(@RequestParam(name = "id", required = false, defaultValue = "0") long id, Model model,
                              @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
                              @RequestParam(name = "sort", required = false, defaultValue = "") String sort) {
        List<Category> categories = categoryService.findAllByActivatedTrue();

        Page<ProductDto> products;
        if (id == 0) {
            products = productService.findAllByActivated(pageNo, sort);
            model.addAttribute("sort", sort);
        } else {
            products = productService.findAllByActivated(id, pageNo);
        }
        long totalProducts = products.getTotalElements();
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("size", products.getSize());
        model.addAttribute("categories", categories);


        return "shop";
    }

    @GetMapping("/search-products/{pageNo}")
    public String searchProduct(@PathVariable("pageNo") int pageNo,
                                @RequestParam(name = "keyword") String keyword,
                                Model model)
    {
        Page<ProductDto> products = productService.searchProducts(pageNo, keyword);
        long totalProducts = products.getTotalElements();
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "shop";

    }
}
