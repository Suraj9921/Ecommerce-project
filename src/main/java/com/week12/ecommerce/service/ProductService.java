package com.week12.ecommerce.service;

import com.week12.ecommerce.dto.ProductDto;
import com.week12.ecommerce.exception.ProductNotFoundException;
import com.week12.ecommerce.model.Product;
import com.week12.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<Product> findProductsByCategory(long id) {
        return productRepository.findAllByCategoryId(id);
    }

    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    public void addProduct(Product product){
        productRepository.save(product);
    }

    private List<ProductDto> transferData(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setQty(product.getQty());
            productDto.setPrice(product.getPrice());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setDescription(product.getDescription());
            productDto.setImageName(product.getImageName());
            productDto.setCategoryId(product.getCategory().getId());
            productDto.setActivated(product.is_activated());
            productDtos.add(productDto);
        }
        return productDtos;
    }

    public List<ProductDto> findAllProducts() {
        List<Product> products=productRepository.findAllByActivatedTrue();
        List<ProductDto>productDtoList = transferData(products);
        return productDtoList;
    }
    public void removeProductById(long id){
        productRepository.deleteById(id);
    }

    public Optional<Product> getProductById(long id){
        return Optional.ofNullable(productRepository.findById(id));
    }

    public List<Product> getAllProductsByCategoryId(int id){
        return productRepository.findAllByCategory_Id(id);
    }

    public boolean isProductExists(String name){return productRepository.existsProductByName(name);}

    public Product findById(Long id) {
        Optional<Product> optionalProduct =productRepository.findById(id);
        return optionalProduct.orElseThrow(()-> new ProductNotFoundException("Can not find product with id : "+id));
    }

    public Page<ProductDto> searchProducts(int pageNo, String keyword) {
        List<Product> products = productRepository.findAllByNameContainingIgnoreCase(keyword);
        List<ProductDto> productDtoList = transferData(products);
        Pageable pageable = PageRequest.of(pageNo, 4);
        Page<ProductDto> dtoPage = toPage(productDtoList, pageable);
        return dtoPage;
    }

    private Page toPage(List list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }

    public Page<ProductDto> findAllByActivated(int pageNo,String sort) {
        List<Product> products;

        if ("lowToHigh".equals(sort)) {
            products = productRepository.findAllByActivatedTrueAndSortBy("lowToHigh");
        } else if ("highToLow".equals(sort)) {
            products = productRepository.findAllByActivatedTrueAndSortBy("highToLow");
        } else {
            products = productRepository.findAllByActivatedTrue();
        }

        List<ProductDto> productDtoList = transferData(products);
        Pageable pageable = PageRequest.of(pageNo, 4);
        return toPage(productDtoList, pageable);

    }

    public List<Product> findCurrentProducts() {
        Optional<List<Product>> optionalProducts= productRepository.findCurrentProducts();
        return optionalProducts.orElse(new ArrayList<Product>());
    }

    public List<Product> findAllByCategoryId(Long id) {
        return productRepository.findAllByCategoryId(id);
    }

    public Page<ProductDto> findAllByActivated(long id,int pageNo) {
        List<Product> products=productRepository.findAllByActivatedTrue(id);
        List<ProductDto>productDtoList = transferData(products);
        Pageable pageable = PageRequest.of(pageNo, 4);
        Page<ProductDto> dtoPage = toPage(productDtoList, pageable);
        return dtoPage;
    }

    public Product findBYId(long id) {
        return productRepository.findById(id);
    }

    public List<Object[]> getProductsStatsBetweenDates(Date startDate, Date endDate) {
        return productRepository.getProductsStatsForConfirmedOrdersBetweenDates(startDate,endDate);
    }

    public List<Object[]> getProductStats() {
        return productRepository.getProductStatsForConfirmedOrders();
    }

    public Long countAllProducts() {
        return productRepository.CountAllProducts();
    }
}
