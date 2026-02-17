package com.cart.application.service;

import com.cart.application.dto.AddToCartRequest;
import com.cart.application.dto.CartResponseDTO;
import com.cart.application.dto.GetUserCartRequest;
import com.cart.application.dto.RemoveFromCartRequest;
import com.cart.application.entities.Cart;
import com.cart.application.entities.CartItem;
import com.cart.application.exceptions.CartNotAvailableException;
import com.cart.application.exceptions.ProductNotInCartException;
import com.cart.application.mapper.CartMapper;
import com.cart.application.repositories.CartItemRepository;
import com.cart.application.repositories.CartRepository;
import com.product.application.entities.CompanyProducts;
import com.product.application.exceptions.ProductNotAvailableException;
import com.product.application.exceptions.QuantityNotEnoughException;
import com.product.application.repositories.CompanyProductsRepository;
import com.users.application.entities.Users;
import com.users.application.repository.UsersRepository;
import com.utils.application.RequestContract;
import com.utils.application.ResponseContract;
import com.utils.application.ServiceContract;
import com.utils.application.globalExceptions.IncorrectRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.utils.application.ExceptionHandler.throwExceptionAndReport;

@Service
@RequiredArgsConstructor
@Transactional

public class CartService implements ServiceContract {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UsersRepository usersRepository;
    private final CompanyProductsRepository companyProductsRepository;
    private final CartMapper cartMapper;

    
    public List<CartResponseDTO> addToCart(RequestContract requestContract) {

        if(requestContract instanceof AddToCartRequest request) {
            Users user = getUser(request.getUserId());
            CompanyProducts companyProduct = getCompanyProduct(request.getCompanyProductId());

            validateStock(companyProduct, request.getQuantity());

            Cart cart = getOrCreateCart(user);

            CartItem cartItem = cartItemRepository
                    .findByCartAndCompanyProducts(cart, companyProduct)
                    .orElse(null);

            double finalPrice = calculateFinalPrice(companyProduct);

            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            } else {
                cartItem = CartItem.builder()
                        .cart(cart)
                        .companyProducts(companyProduct)
                        .quantity(request.getQuantity())
                        .priceSnapshot(finalPrice)
                        .build();
                cart.getItems().add(cartItem);
            }

            cartItemRepository.save(cartItem);

            return cartMapper.toDTO(cart);
        }else{
            var errorMessage = "Request sent is not correct to submit AddToCartRequest";
            var resolveIssue = "Use correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);


        }
    }

    
    public List<CartResponseDTO> updateCartItem(RequestContract requestContract) {
        if(requestContract instanceof AddToCartRequest request) {
            Cart cart = getCart(request.getUserId());

            CartItem cartItem = cart.getItems().stream()
                    .filter(item ->
                            item.getCompanyProducts()
                                    .getCompanyProductId()
                                    .equals(request.getCompanyProductId()))
                    .findFirst()
                    .orElseThrow(() -> {
                        var errorMessage = "Product is not in cart";
                        var resolveIssue = "Please check the product is in cart";
                        return throwExceptionAndReport(new ProductNotInCartException(errorMessage), errorMessage, resolveIssue);
                    });

            if (request.getCompanyProductId() <= 0) {
                cartItemRepository.delete(cartItem);
                cart.getItems().remove(cartItem);
            } else {
                validateStock(cartItem.getCompanyProducts(), request.getQuantity());
                cartItem.setQuantity(request.getQuantity());
            }

            cartRepository.save(cart);

            return cartMapper.toDTO(cart);
        }else{
            var errorMessage = "Request sent is not correct to submit AddToCartRequest";
            var resolveIssue = "Use correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);

        }
    }

    
    public List <CartResponseDTO> removeFromCart(RequestContract requestContract) {

        if(requestContract instanceof RemoveFromCartRequest request) {
            Cart cart = getCart(request.getUserId());

            CartItem cartItem = cart.getItems().stream()
                    .filter(item ->
                            item.getCompanyProducts()
                                    .getCompanyProductId()
                                    .equals(request.getCompanyProductId()))
                    .findFirst()
                    .orElseThrow(() ->
                    { var errorMessage = "Product to purchase not available";
                        var resolveIssue = "Please wait for company to add product to purchase";
                        return throwExceptionAndReport(new ProductNotAvailableException(errorMessage), errorMessage, resolveIssue);
                    });

            cartItemRepository.delete(cartItem);
            cart.getItems().remove(cartItem);
            return new ArrayList<>();
        }else{
            var errorMessage = "Request sent is not correct to submit RemoveFromCartRequest";
            var resolveIssue = "Use correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);


        }
    }

    private List <CartResponseDTO> getUserCart(RequestContract requestContract) {

        if(requestContract instanceof GetUserCartRequest request) {
            Cart cart = getCart(request.getUserId());
            return cartMapper.toDTO(cart);
        }else{
            var errorMessage = "Request sent is not correct to submit RemoveFromCartRequest";
            var resolveIssue = "Use correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);

        }
    }


    private Users getUser(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private CompanyProducts getCompanyProduct(Long id) {
        return companyProductsRepository.findById(id)
                .orElseThrow(() -> {
                    var errorMessage = "Product to purchase not available";
                    var resolveIssue = "Please wait for company to add product to purchase";
                    return throwExceptionAndReport(new ProductNotAvailableException(errorMessage), errorMessage, resolveIssue);

                });
    }

    private Cart getCart(Long userId) {
        return cartRepository.findCartWithItems(userId)
                .orElseThrow(() -> {
                    var errorMessage = "Cart  not available";
                    var resolveIssue = "Please add product to cart to be available";
                    return throwExceptionAndReport(new CartNotAvailableException(errorMessage), errorMessage, resolveIssue);
                });
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
            var errorMessage = "Product to purchase not available";
            var resolveIssue = "Please wait for company to add product to purchase";
            throw throwExceptionAndReport(new ProductNotAvailableException(errorMessage), errorMessage, resolveIssue);
        }

        if (cp.getProductQuantity() == 0) {
            cp.setCompanyProductStatus((byte)0);
        }

        if (cp.getProductQuantity() < quantity) {
            var errorMessage = "Company product quantity is less than requested  product quantity ";
            var resolveIssue = "Please purchase the remaining quantity or wait for retail company to add product";
            throw throwExceptionAndReport(new QuantityNotEnoughException(errorMessage), errorMessage, resolveIssue);
        }
    }

    private double calculateFinalPrice(CompanyProducts cp) {

        double price = cp.getProductPrice();

        if (cp.getLateNightPriceStatus() == 1) {
            price += cp.getLateNightPriceIncrease();
        }

        if (cp.getReturnableBottleStatus() == 1) {
            price += cp.getReturnableBottlePrice();
        }

        return price;
    }

    @Override
    public List<? extends ResponseContract> call(String serviceRunner, RequestContract request) {
        return switch (serviceRunner) {
            case "addToCart" -> this.addToCart(request);
            case "removeFromCart" -> this.removeFromCart(request);
            case "updateCartItem" -> this.updateCartItem(request);
            case "getUserCart" -> this.getUserCart(request);

            default -> throw new IllegalStateException("Unexpected value: " + serviceRunner);
        };

    }
}
