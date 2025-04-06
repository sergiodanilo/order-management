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
import jakarta.transaction.Transactional;
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

    @Transactional
    public Optional<OrderDTO> registerOrder(OrderDTO orderDTO) {
        Client client = clientRepository.findByTaxId(orderDTO.getTaxId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Map<Long, Product> productMap = getProductMap(orderDTO);

        Order newOrder = createOrder(client, productMap.values());
        Order savedOrder = orderRepository.save(newOrder);

        List<OrderProduct> orderProducts = buildOrderProducts(orderDTO, savedOrder, productMap);
        orderProductRepository.saveAll(orderProducts);

        return Optional.of(buildOrderDTO(savedOrder, orderProducts));
    }

    public Optional<OrderDTO> findById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(order.getId());

        return Optional.of(buildOrderDTO(order, orderProducts));
    }

    private Map<Long, Product> getProductMap(OrderDTO orderDTO) {
        Set<Long> productIds = orderDTO.getOrderItems().stream()
                .map(OrderItemDTO::getProductId)
                .collect(Collectors.toSet());

        return productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }

    private Order createOrder(Client client, Collection<Product> products) {
        BigDecimal total = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Order.builder()
                .date(LocalDateTime.now())
                .client(client)
                .products(new HashSet<>(products))
                .totalAmount(total)
                .status(OrderStatus.PENDING)
                .build();
    }

    private List<OrderProduct> buildOrderProducts(OrderDTO orderDTO, Order order, Map<Long, Product> productMap) {
        return orderDTO.getOrderItems().stream()
                .map(item -> {
                    Product product = productMap.get(item.getProductId());
                    OrderProduct orderProduct = orderProductRepository
                            .findByOrderIdAndProductId(order.getId(), product.getId());
                    orderProduct.setQuantity(item.getQuantity());
                    return orderProduct;
                })
                .collect(Collectors.toList());
    }

    private OrderDTO buildOrderDTO(Order order, List<OrderProduct> orderProducts) {
        Set<OrderItemDTO> items = orderProducts.stream()
                .map(op -> OrderItemDTO.builder()
                        .productId(op.getProduct().getId())
                        .product(op.getProduct().getName())
                        .quantity(op.getQuantity())
                        .price(op.getProduct().getPrice())
                        .build())
                .collect(Collectors.toSet());

        return OrderDTO.builder()
                .orderNumber(order.getId())
                .date(order.getDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .client(order.getClient().getName())
                .orderItems(items)
                .build();
    }

}
