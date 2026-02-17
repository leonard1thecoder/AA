package com.cart.application.controller;

import com.cart.application.dto.*;
import com.cart.application.executor.CartServiceExecutor;
import com.cart.application.service.CartService;
import com.cart.application.service.CheckoutService;

import com.utils.application.ResponseContract;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/cart")

public class CartController extends CartServiceExecutor {

    private final CartService cartService;
    private final CheckoutService checkoutService;

    public CartController(CartService cartService, CheckoutService checkoutService) {
        super(Executors.newVirtualThreadPerTaskExecutor());
        this.cartService = cartService;
        this.checkoutService = checkoutService;
    }

    // ==============================
    // ✅ ADD TO CART
    // ==============================
    @PostMapping("/add")
    public ResponseEntity<List<? extends ResponseContract>> addToCart(
           AddToCartRequest request) {
     var response =   super.executeService(cartService, request
                , "addToCard");

           if(response.getFirst() instanceof CartResponseDTO) {
               return ResponseEntity
                       .status(HttpStatus.CREATED)
                       .body(response);
           }else {
               return ResponseEntity.badRequest().body(response);
           }
    }

    // ==============================
    // ✅ UPDATE CART ITEM
    // ==============================
    @PutMapping("/update")
    public ResponseEntity<List<? extends ResponseContract>> updateCartItem(
            @RequestParam Long userId,
            @RequestParam Long companyProductId,
            @RequestParam Integer quantity) {

        var response =   super.executeService(cartService, AddToCartRequest
                        .builder()
                        .userId(userId)
                        .companyProductId(companyProductId)
                        .quantity(quantity)
                        .build()
                , "addToCard");
       if (response.getFirst() instanceof CartResponseDTO) {
           return ResponseEntity.ok(response);
       }else {
           return ResponseEntity.badRequest().body(response);
       }
    }

    // ==============================
    // ✅ REMOVE ITEM
    // ==============================
    @DeleteMapping("/remove")
    public ResponseEntity<List<? extends ResponseContract>> removeFromCart(
            @RequestParam Long userId,
            @RequestParam Long companyProductId) {
        var response =   super.executeService(cartService, RemoveFromCartRequest
                        .builder()
                        .userId(userId)
                        .companyProductId(companyProductId)
                        .build()
                , "addToCard");


        if (response.getFirst() instanceof CartResponseDTO) {
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ==============================
    // ✅ VIEW CART
    // ==============================
    @GetMapping("/{userId}")
    public ResponseEntity<List<? extends ResponseContract>>  viewCart(
            @PathVariable Long userId) {

        var response =   super.executeService(cartService, ViewCartRequest
                        .builder()
                        .userId(userId)
                        .build()
                , "addToCard");

        if (response.getFirst() instanceof CartResponseDTO) {
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ==============================
    // 🔥 CHECKOUT
    // ==============================
    @PostMapping("/checkout/{userId}")
    public ResponseEntity<List<? extends ResponseContract>> checkout(
            @PathVariable Long userId) {

        var response =   super.executeService(cartService, CheckoutRequest
                        .builder()
                        .userId(userId)
                        .build()
                , "addToCard");

        if (response.getFirst() instanceof CartResponseDTO) {
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
