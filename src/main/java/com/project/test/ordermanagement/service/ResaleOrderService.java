package com.project.test.ordermanagement.service;

import com.project.test.ordermanagement.api.ResaleOrderApi;
import com.project.test.ordermanagement.exceptions.ResaleOrderException;
import com.project.test.ordermanagement.model.Order;
import com.project.test.ordermanagement.model.OrderProduct;
import com.project.test.ordermanagement.model.Product;
import com.project.test.ordermanagement.model.ResaleOrder;
import com.project.test.ordermanagement.model.dto.OrderDTO;
import com.project.test.ordermanagement.model.dto.OrderItemDTO;
import com.project.test.ordermanagement.model.dto.ResaleOrderDTO;
import com.project.test.ordermanagement.model.enums.OrderStatus;
import com.project.test.ordermanagement.repository.OrderProductRepository;
import com.project.test.ordermanagement.repository.OrderRepository;
import com.project.test.ordermanagement.repository.ResaleOrderRepository;
import com.project.test.ordermanagement.service.base.CRUDBaseServiceImpl;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResaleOrderService extends CRUDBaseServiceImpl<ResaleOrder, Long> {

    private final ResaleOrderApi resaleOrderApi;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public ResaleOrderService(
            ResaleOrderRepository repository,
            ResaleOrderApi resaleOrderApi,
            OrderRepository orderRepository,
            OrderProductRepository orderProductRepository
    ) {
        super(repository);
        this.resaleOrderApi = resaleOrderApi;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Transactional
    public ResaleOrderDTO sendResaleOrders(List<Long> orderIds) {
        ResaleOrderDTO resaleOrderDTO = resaleOrderApi.sendResaleOrders();
        List<Order> orders = orderRepository.findAllById(orderIds);

        validateOrdersQuantity(orders);

        if (resaleOrderDTO.getOrderNumber() != null) {
            updateOrdersToReceived(orders);
            ResaleOrder resaleOrder = saveResaleOrder(resaleOrderDTO.getOrderNumber(), orders);
            resaleOrderDTO.setId(resaleOrder.getId());
        }

        resaleOrderDTO.setOrders(buildOrderDTOs(orders));
        return resaleOrderDTO;
    }

    public ResaleOrderDTO mapResaleOrderToDTO(ResaleOrder resaleOrder) {
        Set<OrderDTO> orderDTOs = resaleOrder.getOrders().stream()
                .map(this::buildOrderDTO)
                .collect(Collectors.toSet());

        return ResaleOrderDTO.builder()
                .id(resaleOrder.getId())
                .orderNumber(resaleOrder.getOrderNumber())
                .orders(orderDTOs)
                .build();
    }

    private void validateOrdersQuantity(List<Order> orders) {
        int totalQuantity = orders.stream()
                .mapToInt(order -> {
                    Set<Long> productIds = order.getProducts().stream()
                            .map(Product::getId)
                            .collect(Collectors.toSet());
                    return orderProductRepository.findByOrderIdAndProductIdsIn(order.getId(), productIds).stream()
                            .mapToInt(OrderProduct::getQuantity)
                            .sum();
                })
                .sum();

        if (totalQuantity < 1000) {
            throw new ResaleOrderException("Orders quantity is less than 1000");
        }
    }

    private void updateOrdersToReceived(List<Order> orders) {
        orders.forEach(order -> order.setStatus(OrderStatus.RECEIVED));
        orderRepository.saveAll(orders);
    }

    private ResaleOrder saveResaleOrder(UUID orderNumber, List<Order> orders) {
        ResaleOrder resaleOrder = ResaleOrder.builder()
                .orderNumber(orderNumber)
                .orderDate(LocalDateTime.now())
                .orders(orders)
                .build();
        return this.save(resaleOrder);
    }

    private Set<OrderDTO> buildOrderDTOs(List<Order> orders) {
        return orders.stream()
                .map(this::buildOrderDTO)
                .collect(Collectors.toSet());
    }

    private OrderDTO buildOrderDTO(Order order) {
        return OrderDTO.builder()
                .orderNumber(order.getId())
                .taxId(order.getClient().getTaxId())
                .client(order.getClient().getName())
                .address(order.getClient().getAddress())
                .date(order.getDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderItems(generateOrderItems(order))
                .build();
    }

    private Set<OrderItemDTO> generateOrderItems(Order order) {
        return order.getProducts().stream()
                .map(product -> {
                    Optional<OrderProduct> optOrderProduct = orderProductRepository.findByOrderIdAndProductId(order.getId(), product.getId());
                    OrderProduct op = optOrderProduct.orElseThrow(() -> new ResaleOrderException("OrderProduct not found"));
                    return OrderItemDTO.builder()
                            .productId(product.getId())
                            .quantity(op.getQuantity())
                            .build();
                })
                .collect(Collectors.toSet());
    }

}
