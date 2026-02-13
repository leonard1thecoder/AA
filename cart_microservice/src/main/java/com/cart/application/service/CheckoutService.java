package com.cart.application.service;

import com.cart.application.dto.CheckoutResponseDTO;
import com.cart.application.entities.Cart;
import com.cart.application.entities.CartItem;
import com.cart.application.entities.OrderItems;
import com.cart.application.entities.Orders;
import com.cart.application.repositories.CartRepository;
import com.cart.application.repositories.OrderItemsRepository;
import com.cart.application.repositories.OrdersRepository;
import com.product.application.entities.CompanyProducts;
import com.product.application.repositories.CompanyProductsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private static final Logger logger =
            LoggerFactory.getLogger(CheckoutService.class);

    private final CartRepository cartRepository;
    private final CompanyProductsRepository companyProductsRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;

    @Transactional
    public CheckoutResponseDTO checkout(Long userId) {

        // 🔹 Fetch cart with items
        Cart cart = cartRepository.findCartWithItems(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double totalAmount = 0;

        // 🔹 Create Order first
        Orders order = Orders.builder()
                .user(cart.getUser())
                .orderDate(LocalDateTime.now().toString())
                .status("COMPLETED")
                .build();

        ordersRepository.save(order);

        // 🔹 Process each cart item
        for (CartItem cartItem : cart.getItems()) {

            Long companyProductId =
                    cartItem.getCompanyProducts().getCompanyProductId();

            // 🔥 LOCK THE ROW
            CompanyProducts companyProduct =
                    companyProductsRepository
                            .findByIdForUpdate(companyProductId)
                            .orElseThrow(() ->
                                    new RuntimeException("Product not found"));

            // 🔹 Validate availability
            if (companyProduct.getCompanyProductStatus() != 1) {
                throw new RuntimeException("Product unavailable");
            }

            if (companyProduct.getProductQuantity() <
                    cartItem.getQuantity()) {

                throw new RuntimeException(
                        "Insufficient stock for product ID: "
                                + companyProductId);
            }

            // 🔥 Reduce stock safely
            companyProduct.setProductQuantity(
                    companyProduct.getProductQuantity()
                            - cartItem.getQuantity());

            // 🔹 Create OrderItem
            OrderItems orderItem = OrderItems.builder()
                    .order(order)
                    .companyProducts(companyProduct)
                    .quantity(cartItem.getQuantity())
                    .priceAtPurchase(cartItem.getPriceSnapshot())
                    .build();

            orderItemsRepository.save(orderItem);

            totalAmount += cartItem.getPriceSnapshot()
                    * cartItem.getQuantity();

            logger.info("Stock reduced for companyProduct {}",
                    companyProductId);
        }

        // 🔹 Update order total
        order.setTotalAmount(totalAmount);
        ordersRepository.save(order);

        // 🔥 Clear cart after success
        cart.getItems().clear();
        cartRepository.save(cart);

        logger.info("Checkout completed for user {}", userId);

        return CheckoutResponseDTO.builder()
                .orderId(order.getOrderId())
                .totalAmount(totalAmount)
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .build();
    }
}
