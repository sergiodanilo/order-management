package com.project.test.ordermanagement.service;

import com.project.test.ordermanagement.model.Client;
import com.project.test.ordermanagement.model.Order;
import com.project.test.ordermanagement.model.OrderProduct;
import com.project.test.ordermanagement.model.Product;
import com.project.test.ordermanagement.model.dto.OrderDTO;
import com.project.test.ordermanagement.model.dto.OrderItemDTO;
import com.project.test.ordermanagement.model.enums.OrderStatus;
import com.project.test.ordermanagement.repository.ClientRepository;
import com.project.test.ordermanagement.repository.OrderProductRepository;
import com.project.test.ordermanagement.repository.OrderRepository;
import com.project.test.ordermanagement.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    public Optional<OrderDTO> registerOrder(OrderDTO order) {
        Client client = clientRepository.findByTaxId(order.getTaxId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Set<Long> productIds = order.getOrderItems().stream()
                .map(OrderItemDTO::getProductId).collect(Collectors.toSet());
        Set<Product> products = new HashSet<>(productRepository.findAllById(productIds));
        Map<Long, Product> mapProducts = new HashSet<>(products).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        Order newOrder = Order.builder()
                .date(LocalDateTime.now())
                .products(products)
                .client(client)
                .totalAmount(products.stream()
                        .map(Product::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(newOrder);

        List<OrderProduct> orderProductList = new ArrayList<>();

        order.getOrderItems().forEach(item -> {
            Product product = mapProducts.get(item.getProductId());

            OrderProduct orderProduct = orderProductRepository.findByOrderIdAndProductId(savedOrder.getId(), product.getId());
            orderProduct.setQuantity(item.getQuantity());
            orderProductList.add(orderProduct);
        });

        orderProductRepository.saveAll(orderProductList);

        OrderDTO orderDto = OrderDTO.builder()
                .orderNumber(savedOrder.getId())
                .date(savedOrder.getDate())
                .status(savedOrder.getStatus())
                .totalAmount(savedOrder.getTotalAmount())
                .client(savedOrder.getClient().getName())
                .orderItems(orderProductList.stream()
                        .map(orderProduct ->
                                OrderItemDTO.builder()
                                        .productId(orderProduct.getProduct().getId())
                                        .product(orderProduct.getProduct().getName())
                                        .quantity(orderProduct.getQuantity())
                                        .price(orderProduct.getProduct().getPrice())
                                        .build()
                        )
                        .collect(Collectors.toSet()))
                .build();

        return Optional.of(orderDto);
    }

    public Optional<OrderDTO> findById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(order.getId());
        Set<OrderItemDTO> orderItems = orderProducts.stream()
                .map(orderProduct ->
                        OrderItemDTO.builder()
                                .productId(orderProduct.getProduct().getId())
                                .product(orderProduct.getProduct().getName())
                                .quantity(orderProduct.getQuantity())
                                .price(orderProduct.getProduct().getPrice())
                                .build()
                )
                .collect(Collectors.toSet());

        OrderDTO orderDto = OrderDTO.builder()
                .orderNumber(order.getId())
                .date(order.getDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .client(order.getClient().getName())
                .orderItems(orderItems)
                .build();

        return Optional.of(orderDto);
    }

}
