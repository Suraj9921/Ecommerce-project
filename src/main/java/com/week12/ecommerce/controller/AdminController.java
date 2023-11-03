package com.week12.ecommerce.controller;

import com.week12.ecommerce.dto.ProductDto;
import com.week12.ecommerce.dto.Report;
import com.week12.ecommerce.model.*;
import com.week12.ecommerce.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AdminController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    private UsersSevice usersSevice;

    @Autowired
    ProductService productService;

//    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";
    public static String uploadDir =  "/home/ubuntu/Ecommerce-project/src/main/resources/static/productImages";

    @GetMapping("/admin")
    public String adminHome(){
        return "adminHome";
    }

    @GetMapping("/admin/categories")
    public String getCategory(Model model){
        model.addAttribute("categories",categoryService.getAllCategory());
        return "categories";
    }

    @GetMapping("/admin/categories/add")
    public String getCategoryAdd(Model model){
        model.addAttribute("category",new Category());
        return "categoriesAdd";
    }

    @PostMapping("/admin/categories/add")
    public String postCategoryAdd(@ModelAttribute("category")@Valid Category category, BindingResult result, Model model){
        if (result.hasErrors()){
            return "categoriesAdd";
        }
        if (categoryService.isCategoryExists(category.getName())){
            result.rejectValue("name", "error.category", "Category name already exists");
            return "categoriesAdd";
        }
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id){
        categoryService.removeCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String updateCategory(@PathVariable int id, Model model){
        Optional<Category> category = categoryService.getCategoryById(id);
        if(category.isPresent()){
            model.addAttribute("category",category.get());
            return "categoriesAdd";
        }
        else
            return "404";
    }

    @PostMapping("/admin/categoriespop/add")
    public String postCatPopAdd(@ModelAttribute("category") @Valid Category category,BindingResult result,Model model) {
        if (result.hasErrors()){
            return "categoriesAdd";
        }
        if (categoryService.isCategoryExists(category.getName())){
            result.rejectValue("name", "error.category", "Category name already exists");
            return "redirect:/admin/categories/add";
        }
        categoryService.addCategory(category);
        return "redirect:/admin/products/add";
    }

    //Product section
    @GetMapping("/admin/products")
    public String getProducts(Model model){
        model.addAttribute("products",productService.getAllProduct());
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String getProductsAdd(Model model){
        model.addAttribute("productDTO",new ProductDto());
        model.addAttribute("categories",categoryService.getAllCategory());
        return "productsAdd";
    }
//    @PostMapping("admin/products/add")
//    public String productAddPost(@ModelAttribute("productDTO") ProductDto productDto,
//                                 @RequestParam("productImage")MultipartFile file,
//                                 @RequestParam("imgName")String imgName, BindingResult result, Model model) throws IOException {
//
//        if (productService.isProductExists(productDto.getName())){
//            System.out.println("product exists with same name");
//            result.rejectValue("name","error.product","Product name already exists");
//            return "productsAdd";
//        }
//
//        if (result.hasErrors()) {
//            model.addAttribute("productDTO", productDto);
//            result.rejectValue("name","error.product","There has been an error, try avoiding blank spaces");
//            result.rejectValue("brand","error.product","There has been an error, try avoiding blank spaces");
//            result.rejectValue("imgName","error.product","There has been an error, try uploading png jpg or jpeg files");
//
//            return "productsAdd";
//        }
//
//        Product product = new Product();
//        product.setId(productDto.getId());
//        product.setName(productDto.getName());
//        product.setCategory(categoryService.getCategoryById(productDto.getCategoryId()).get());
//        product.setPrice(productDto.getPrice());
////        product.setWeight(productDto.getWeight());
//        product.setQty(productDto.getQty());
//        product.setDescription(productDto.getDescription());
//        String imageUUID;
//        if(!file.isEmpty()){
//            imageUUID = file.getOriginalFilename();
//            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
//            Files.write(fileNameAndPath,file.getBytes());
//        }else{
//            imageUUID = imgName;
//        }
//        product.setImageName(imageUUID);
//        productService.addProduct(product);
//
//        return "redirect:/admin/products";
//    }

    @PostMapping("admin/products/add")
    public String productAddPost(@ModelAttribute("productDTO") ProductDto productDto,
                                 @RequestParam("productImage") MultipartFile file,
                                 @RequestParam("imgName") String imgName, BindingResult result, Model model) throws IOException {

//        if (productService.isProductExists(productDto.getName())) {
//            System.out.println("Product exists with the same name");
//            result.rejectValue("name", "error.product", "Product name already exists");
//            return "productsAdd";
//        }

        if (result.hasErrors()) {
            model.addAttribute("productDTO", productDto);
            result.rejectValue("name", "error.product", "There has been an error, try avoiding blank spaces");
            result.rejectValue("brand", "error.product", "There has been an error, try avoiding blank spaces");
            result.rejectValue("imgName", "error.product", "There has been an error, try uploading png jpg or jpeg files");
            return "productsAdd";
        }

        Product product = new Product();

        // Check if productDto.getId() is not null before setting it on the Product object
        if (productDto.getId() != null) {
            product.setId(productDto.getId());
        }

        product.setName(productDto.getName());
        product.setCategory(categoryService.getCategoryById(productDto.getCategoryId()).get());
        product.setPrice(productDto.getPrice());
        product.setSalePrice(productDto.getPrice());
        product.setWeight(productDto.getWeight());
        product.setQty(productDto.getQty());
        product.set_activated(true);
        product.setDescription(productDto.getDescription());

        String imageUUID;

        if (!file.isEmpty()) {
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());
        } else {
            imageUUID = imgName;
        }

        product.setImageName(imageUUID);
        productService.addProduct(product);

        return "redirect:/admin/products";
    }


    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id){
        productService.removeProductById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable long id,Model model){
        Product product = productService.getProductById(id).get();
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setCategoryId((product.getCategory().getId()));
        productDto.setPrice(product.getPrice());
        productDto.setSalePrice(product.getSalePrice());
//        productDto.setWeight(product.getWeight());
        productDto.setQty(product.getQty());
        productDto.setActivated(product.is_activated());
        productDto.setDescription(product.getDescription());
        productDto.setImageName(product.getImageName());

        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("productDTO", productDto);

        return "productsAdd";
    }

//    @Autowired
//    UserServiceImpl userService;

    @GetMapping("/admin/users")
    public String findAllUsers(Model model) throws Exception {
        try{
            List<Users> usersList = usersSevice.findAll();
            model.addAttribute("usersList",usersList);
            model.addAttribute("title", "users");
            model.addAttribute("users", usersList);
            model.addAttribute("size", usersList.size());
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("Can not fetch details" + HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return "viewAllUsers";
    }

    @GetMapping("/admin/users/block/{id}")
    public String blockUser(@PathVariable int id) throws Exception {
        try{
            if(usersSevice.existById(id)){
                Users users = usersSevice.findById(id);
                users.setBlocked(true);
                users.setUpdateOn(new Date());
                try{
                    usersSevice.saveOrUpdate(users);
                }catch (Exception e){
                    e.printStackTrace();
                    throw new Exception("Internal server error");
                }
            }else{
                throw new Exception("User Not found  with this id = "+id);
            }
            //Optional<Users> optionalUsers =Optional.ofNullable((Users) usersSevice.findById(id));
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Internal servererror");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/unblock/{id}")
    public String unblockUser(@PathVariable int id) throws Exception {
        try{
            if(usersSevice.existById(id)){
                Users users = usersSevice.findById(id);
                users.setBlocked(false);
                users.setUpdateOn(new Date());
                try{
                    usersSevice.saveOrUpdate(users);
                }catch (Exception e){
                    e.printStackTrace();
                    throw new Exception("Internal server error");
                }
            }else{
                throw new Exception("User Not found  with this id = "+id);
            }
            //Optional<Users> optionalUsers =Optional.ofNullable((Users) usersSevice.findById(id));
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Internal servererror");
        }
        return "redirect:/admin/users";
    }
    @Autowired
    OrderService orderService;

    @GetMapping("/admin/orders")
    public String adminOrders(Model model){
        List<Order> orderList = orderService.getAllOrders();
        model.addAttribute("orderList" , orderList);
        return "OrdersManage";
    }
    @PostMapping("/admin/orders/update-status/{orderId}")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam("orderStatus") String orderStatus) {
        orderService.updateOrderStatus(orderId,orderStatus);
        return "redirect:/admin/orders";
    }

    @PostMapping("/admin/orders/cancel/{orderId}")
    public String cancelOrder(@PathVariable Long orderId){
        orderService.updateOrderStatus(orderId,"CANCELLED");
        return "redirect:/admin/orders";
    }

    @Autowired
    CouponService couponService;

    @GetMapping("/admin/coupons")
    public String getCoupons(Model model){
        model.addAttribute("coupons",couponService.getAllCoupons());
        return "coupons";
    }

    @GetMapping("/admin/coupons/add")
    public String getCouponAdd(Model model){
        model.addAttribute("coupon", new Coupon());
        return "CouponAdd";
    }

    @PostMapping("/admin/coupons/add")
    public String postCouponAdd(@ModelAttribute("coupon") Coupon coupon, Model model){
        couponService.addCoupon(coupon);
        return "redirect:/admin/coupons";
    }
    @GetMapping("/admin/coupons/toggleActivation/{id}")
    public String toggleActivation(@PathVariable("id") int id) {
        Coupon coupon = couponService.getCouponById(id).get();
        if (coupon != null) {
            coupon.toggleActive();
            couponService.updateCoupon(coupon);
        }
        return "redirect:/admin/coupons";
    }

    @GetMapping("/admin/sales")
    public String sales(Model model){
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "salesReport";
    }
    @PostMapping("/admin/salesReport/")
    public String saleReport(@RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                             @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
                             @RequestParam("dates") String dates, Model model) {
        System.out.println("from date is " + fromDate + " to date is " + toDate);
        List<Order> sales = orderService.getSalesData(fromDate, toDate);
        List<Report> dailySales = generateReport(sales);

        int totalOrders = dailySales.stream().mapToInt(Report::getNum).sum();
        double totalAmount = dailySales.stream().mapToDouble(Report::getTotal).sum();

        model.addAttribute("sales", dailySales);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalAmount", totalAmount);

        return "salesReport";
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
