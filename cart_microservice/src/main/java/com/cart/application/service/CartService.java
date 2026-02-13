package com.cart.application.service;

import com.cart.application.dto.CartResponseDTO;
import com.cart.application.entities.Cart;
import com.cart.application.entities.CartItem;
import com.cart.application.mapper.CartMapper;
import com.cart.application.repositories.CartItemRepository;
import com.cart.application.repositories.CartRepository;
import com.product.application.entities.CompanyProducts;
import com.product.application.repositories.CompanyProductsRepository;
import com.users.application.entities.Users;
import com.users.application.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
@Transactional

public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UsersRepository usersRepository;
    private final CompanyProductsRepository companyProductsRepository;
    private final CartMapper cartMapper;

    
    public CartResponseDTO addToCart(Long userId,
                                     Long companyProductId,
                                     Integer quantity) {

        Users user = getUser(userId);
        CompanyProducts companyProduct = getCompanyProduct(companyProductId);

        validateStock(companyProduct, quantity);

        Cart cart = getOrCreateCart(user);

        CartItem cartItem = cartItemRepository
                .findByCartAndCompanyProducts(cart, companyProduct)
                .orElse(null);

        double finalPrice = calculateFinalPrice(companyProduct);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .companyProducts(companyProduct)
                    .quantity(quantity)
                    .priceSnapshot(finalPrice)
                    .build();
            cart.getItems().add(cartItem);
        }

        cartItemRepository.save(cartItem);

        return cartMapper.toDTO(cart);
    }

    
    public CartResponseDTO updateCartItem(Long userId,
                                          Long companyProductId,
                                          Integer quantity) {

        Cart cart = getCart(userId);

        CartItem cartItem = cart.getItems().stream()
                .filter(item ->
                        item.getCompanyProducts()
                                .getCompanyProductId()
                                .equals(companyProductId))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Product not in cart"));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
            cart.getItems().remove(cartItem);
        } else {
            validateStock(cartItem.getCompanyProducts(), quantity);
            cartItem.setQuantity(quantity);
        }

        cartRepository.save(cart);

        return cartMapper.toDTO(cart);
    }

    
    public void removeFromCart(Long userId,
                               Long companyProductId) {

        Cart cart = getCart(userId);

        CartItem cartItem = cart.getItems().stream()
                .filter(item ->
                        item.getCompanyProducts()
                                .getCompanyProductId()
                                .equals(companyProductId))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Product not in cart"));

        cartItemRepository.delete(cartItem);
        cart.getItems().remove(cartItem);
    }

    
    @Transactional
    public CartResponseDTO getUserCart(Long userId) {

        Cart cart = getCart(userId);

        return cartMapper.toDTO(cart);
    }


    private Users getUser(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private CompanyProducts getCompanyProduct(Long id) {
        return companyProductsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private Cart getCart(Long userId) {
        return cartRepository.findCartWithItems(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    private Cart getOrCreateCart(Users user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .createdAt(LocalDateTime.now().toString())
                                .build()));
    }

    private void validateStock(CompanyProducts cp,
                               Integer quantity) {

        if (cp.getCompanyProductStatus() != 1) {
            throw new RuntimeException("Product unavailable");
        }

        if (cp.getProductQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
    }

    private double calculateFinalPrice(CompanyProducts cp) {

        double price = cp.getProductPrice();

        if (cp.getLateNightPriceStatus() == 1) {
            price += cp.getLateNightPriceIncrease();
        }

        if (cp.getReturnableBottlePrice() != null) {
            price += cp.getReturnableBottlePrice();
        }

        return price;
    }
}
