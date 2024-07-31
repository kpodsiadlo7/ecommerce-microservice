package com.example.cartservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Validated
@RestController
@RequestMapping("/mycart")
@RequiredArgsConstructor
class CartController {

    private final CartService productService;
    private final CartMapper cartMapper;

    @PostMapping("/add-product")
    ResponseEntity<CartRecord> addProductToCart(@RequestParam Long productId,
                                                @RequestParam(required = false) Integer quantity) throws IOException {
        var response = cartMapper.toRecord(productService.addProductToCart(productId, quantity));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/clear")
    ResponseEntity<CartRecord> clearMyCart() throws IOException, TimeoutException {
        var response = cartMapper.toRecord(productService.clearMyCart());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping
    ResponseEntity<CartRecord> getCart() {
        var response = cartMapper.toRecord(productService.getMyCart());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/order/cart")
    ResponseEntity<CartRecord> fetchCart() {
        var response = cartMapper.toRecord(productService.fetchCart());
        return ResponseEntity.ok(response);
    }
}
