package com.cart.application.controller;

import com.cart.application.dto.CartResponseDTO;
import com.cart.application.dto.CheckoutResponseDTO;
import com.cart.application.service.CartService;
import com.cart.application.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CheckoutService checkoutService;

    // ==============================
    // ✅ ADD TO CART
    // ==============================
    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addToCart(
            @RequestParam Long userId,
            @RequestParam Long companyProductId,
            @RequestParam Integer quantity) {

        CartResponseDTO response =
                cartService.addToCart(userId, companyProductId, quantity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ==============================
    // ✅ UPDATE CART ITEM
    // ==============================
    @PutMapping("/update")
    public ResponseEntity<CartResponseDTO> updateCartItem(
            @RequestParam Long userId,
            @RequestParam Long companyProductId,
            @RequestParam Integer quantity) {

        CartResponseDTO response =
                cartService.updateCartItem(userId, companyProductId, quantity);

        return ResponseEntity.ok(response);
    }

    // ==============================
    // ✅ REMOVE ITEM
    // ==============================
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(
            @RequestParam Long userId,
            @RequestParam Long companyProductId) {

        cartService.removeFromCart(userId, companyProductId);

        return ResponseEntity.ok("Item removed from cart successfully");
    }

    // ==============================
    // ✅ VIEW CART
    // ==============================
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> viewCart(
            @PathVariable Long userId) {

        CartResponseDTO response =
                cartService.getUserCart(userId);

        return ResponseEntity.ok(response);
    }

    // ==============================
    // 🔥 CHECKOUT
    // ==============================
    @PostMapping("/checkout/{userId}")
    public ResponseEntity<CheckoutResponseDTO> checkout(
            @PathVariable Long userId) {

        CheckoutResponseDTO response =
                checkoutService.checkout(userId);

        return ResponseEntity.ok(response);
    }
}
