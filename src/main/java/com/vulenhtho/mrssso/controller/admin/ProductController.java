package com.vulenhtho.mrssso.controller.admin;

import com.vulenhtho.mrssso.dto.ProductDTO;
import com.vulenhtho.mrssso.dto.request.IdsRequestDTO;
import com.vulenhtho.mrssso.dto.request.ProductFilterRequestDTO;
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
    private ProductService productService;
    private ProductRepository productRepository;

    @Autowired
    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @PostMapping("/product")
    public ResponseEntity<?> create(@RequestBody ProductDTO productDTO){
        if (productRepository.findByName(productDTO.getName()) != null){
            return ResponseEntity.badRequest().body("Product Name has already existed");
        }
        ProductDTO result = productService.create(productDTO);
        if (result == null){
            return ResponseEntity.badRequest().body("ERROR");
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/product")
    public ResponseEntity<?> update(@RequestBody ProductDTO productDTO){
        Product productExits = productRepository.findByName(productDTO.getName());
        if (productExits != null && !productExits.getId().equals(productDTO.getId())){
            return ResponseEntity.badRequest().body("Product Name has already existed");
        }
        ProductDTO result = productService.update(productDTO);
        if (result == null){
            return ResponseEntity.badRequest().body("ERROR");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id){
        ProductDTO productDTO = productService.findById(id);
        if (productDTO != null){
            return ResponseEntity.ok(productDTO);
        }
        return ResponseEntity.badRequest().body("Not found product with id: " + id);
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductDTO>> getAllWithFilter(
            @RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false) String sort
            , @RequestParam(required = false) String search, @RequestParam(required = false) Long categoryId
            , @RequestParam(required = false) Boolean trend, @RequestParam(required = false) String  discountId
            , @RequestParam(required = false) ProductStatus status, @RequestParam(required = false) Boolean hot){

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
    public ResponseEntity<?> delete(@RequestBody IdsRequestDTO ids){
        if (productService.delete(ids.getIds())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("not found product with ids:" + ids.getIds().toString());
    }
}
