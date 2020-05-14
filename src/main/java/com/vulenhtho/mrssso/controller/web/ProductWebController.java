package com.vulenhtho.mrssso.controller.web;


import com.vulenhtho.mrssso.dto.request.ItemDTO;
import com.vulenhtho.mrssso.dto.request.ProductFilterRequestDTO;
import com.vulenhtho.mrssso.dto.response.*;
import com.vulenhtho.mrssso.repository.ProductRepository;
import com.vulenhtho.mrssso.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/web")
public class ProductWebController {
    private final ProductService productService;

    @Autowired
    public ProductWebController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        ProductWebResponseDTO productDTO = productService.findForWebById(id);
        if (productDTO != null) {
            return ResponseEntity.ok(productDTO);
        }
        return ResponseEntity.badRequest().body("Not found product with id: " + id);
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductWebResponseDTO>> getAllWithFilter(
            @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "5") Integer size
            , @RequestParam(required = false) String sort, @RequestParam(required = false) String search
            , @RequestParam(required = false) Long categoryId, @RequestParam(required = false) Boolean trend
            , @RequestParam(required = false) Boolean hot) {

        ProductFilterRequestDTO filterRequestDTO = new ProductFilterRequestDTO(sort, null, search, categoryId, hot, trend, null, page, size);
        Page<ProductWebResponseDTO> productDTOS = productService.getAllWithFilterForWeb(filterRequestDTO);
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/products/window-view")
    public ResponseEntity<ListProductPageResponse> getWindowViewWithFilter(
            @RequestParam(required = false, defaultValue = "0") Integer page
            , @RequestParam(required = false, defaultValue = "5") Integer size
            , @RequestParam(required = false) String sort, @RequestParam(required = false) String search
            , @RequestParam(required = false) Long subCategoryId, @RequestParam(required = false) Boolean trend
            , @RequestParam(required = false) Boolean hot) {

        ProductFilterRequestDTO filterRequestDTO = new ProductFilterRequestDTO(sort, null, search, subCategoryId, hot, trend, null, page, size);
        ListProductPageResponse productDTOS = productService.getWindowViewByFilterForWeb(filterRequestDTO);
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/welcome-page")
    public ResponseEntity<WebHomeResponse> getWelcomePage() {
        return ResponseEntity.ok(productService.getDataForWebHomePage());
    }

    @GetMapping("/header")
    public ResponseEntity<PageHeaderDTO> getHeader() {
        return ResponseEntity.ok(productService.getHeaderResponse());
    }

    @PostMapping("/products/itemInCart")
    public ResponseEntity<ItemsForCartAndHeader> getItemDetailForCart(@RequestBody List<ItemDTO> items) {
        ItemsForCartAndHeader result = productService.getItemShowInCart(items);
        return ResponseEntity.ok(result);
    }
}
