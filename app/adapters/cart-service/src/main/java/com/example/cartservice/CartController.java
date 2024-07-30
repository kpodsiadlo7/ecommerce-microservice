package com.example.cartservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Validated
@RestController
@RequestMapping("/mycart")
@RequiredArgsConstructor
public class CartController {

    private final CartService productService;
    private final CartMapper cartMapper;

    @PostMapping("/add-product/{cartId}")
    ResponseEntity<CartRecord> addProductToCart(@RequestParam Long productId,
                                                @RequestHeader("PublicUserId") String userId,
                                                @RequestParam(required = false) Integer quantity) throws IOException {
        var response = cartMapper.toRecord(productService.addProductToCart(productId, userId, quantity));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/clear")
    ResponseEntity<CartRecord> clearMyCart(@RequestHeader("PublicUserId") String userId) {
        var response = cartMapper.toRecord(productService.clearMyCart(userId));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping
    ResponseEntity<CartRecord> getCart(@RequestHeader("PublicUserId") String userId) {
        var response = cartMapper.toRecord(productService.getMyCart(userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
