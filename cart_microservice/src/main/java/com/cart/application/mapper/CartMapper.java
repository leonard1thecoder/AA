package com.cart.application.mapper;

import com.cart.application.dto.CartItemResponseDTO;
import com.cart.application.dto.CartResponseDTO;
import com.cart.application.entities.Cart;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public CartResponseDTO toDTO(Cart cart) {

        List<CartItemResponseDTO> itemDTOs = cart.getItems().stream()
                .map(item -> CartItemResponseDTO.builder()
                        .companyProductId(item.getCompanyProducts().getCompanyProductId())
                        .productName(item.getCompanyProducts()
                                .getProductList().getProductName())
                        .liters(item.getCompanyProducts()
                                .getProductsLiters().getProductLiters())
                        .unitPrice(item.getPriceSnapshot())
                        .quantity(item.getQuantity())
                        .totalPrice(item.getPriceSnapshot() * item.getQuantity())
                        .build())
                .toList();

        double total = itemDTOs.stream()
                .mapToDouble(CartItemResponseDTO::getTotalPrice)
                .sum();

        return CartResponseDTO.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getId())
                .items(itemDTOs)
                .grandTotal(total)
                .build();
    }
}

