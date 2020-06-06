package com.vulenhtho.mrssso.controller.admin;

import com.vulenhtho.mrssso.dto.ProductColorSizeDTO;
import com.vulenhtho.mrssso.dto.ProductDTO;
import com.vulenhtho.mrssso.dto.ProductDetailDTO;
import com.vulenhtho.mrssso.dto.request.IdsRequestDTO;
import com.vulenhtho.mrssso.dto.request.ProductFilterRequestDTO;
import com.vulenhtho.mrssso.dto.response.InfoToCreateProductDTO;
import com.vulenhtho.mrssso.entity.Product;
import com.vulenhtho.mrssso.entity.enumeration.ProductStatus;
import com.vulenhtho.mrssso.repository.ProductRepository;
import com.vulenhtho.mrssso.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @PostMapping("/product")
    public ResponseEntity<?> create(@RequestBody ProductDTO productDTO) {
        if (productRepository.findByName(productDTO.getName()) != null) {
            return ResponseEntity.badRequest().body("Product Name has already existed");
        }
        Product product = productService.create(productDTO);

        return ResponseEntity.ok(product.getId());
    }

    @PutMapping("/product")
    public ResponseEntity<?> update(@RequestBody ProductDetailDTO productDetailDTO) {
        Product productExits = productRepository.findByName(productDetailDTO.getProductDTO().getName());
        if (productExits != null && !productExits.getId().equals(productDetailDTO.getProductDTO().getId())) {
            return ResponseEntity.badRequest().body("Product Name has already existed");
        }
        productService.update(productDetailDTO);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/product/addProductColorSize")
    public ResponseEntity<?> addProductColorSize(@RequestBody ProductColorSizeDTO productColorSizeDTO) {
        try {
            productService.addProductColorSize(productColorSizeDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        ProductDetailDTO productDTO = productService.getProductDetailByAdmin(id);
        if (productDTO != null) {
            return ResponseEntity.ok(productDTO);
        }
        return ResponseEntity.badRequest().body("Not found product with id: " + id);
    }

    @GetMapping("/product/infoToCreate")
    public ResponseEntity<InfoToCreateProductDTO> getInfoToCreateProduct() {
        return ResponseEntity.ok(productService.getInfoToCreateProductDTO());
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductDTO>> getAllWithFilter(
            @RequestParam(required = false, defaultValue = "0") Integer page
            , @RequestParam(required = false, defaultValue = "5") Integer size, @RequestParam(required = false) String sort
            , @RequestParam(required = false) String search, @RequestParam(required = false) Long categoryId
            , @RequestParam(required = false) Boolean trend, @RequestParam(required = false) String discountId
            , @RequestParam(required = false) ProductStatus status, @RequestParam(required = false) Boolean hot) {

        ProductFilterRequestDTO filterRequestDTO = new ProductFilterRequestDTO(sort, status, search, categoryId, hot, trend, discountId, page, size);
        Page<ProductDTO> productDTOS = productService.getAllWihFilter(filterRequestDTO);
        return ResponseEntity.ok(productDTOS);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if (productService.delete(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("not found product with ids:" +id.toString());
    }

    @DeleteMapping("/products")
    public ResponseEntity<?> delete(@RequestBody IdsRequestDTO idsRequestDTO) {
        if (productService.delete(idsRequestDTO.getIds())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("not found product with ids:" + idsRequestDTO.getIds().toString());
    }
}
