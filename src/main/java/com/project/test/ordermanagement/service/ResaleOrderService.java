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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResaleOrderService extends CRUDBaseServiceImpl<ResaleOrder, Long> {

    private final ResaleOrderApi resaleOrderApi;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public ResaleOrderService(ResaleOrderRepository repository,
                              ResaleOrderApi resaleOrderApi,
                              OrderRepository orderRepository,
                              OrderProductRepository orderProductRepository) {
        super(repository);
        this.resaleOrderApi = resaleOrderApi;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    public ResaleOrderDTO sendResaleOrders(List<Long> orderIds) {
        ResaleOrderDTO resaleOrderDTO = resaleOrderApi.sendResaleOrders();

        List<Order> orders = orderRepository.findAllById(orderIds);
        validateOrdersQuantity(orders);

        if (resaleOrderDTO.getOrderNumber() != null) {
            orders.forEach(order -> order.setStatus(OrderStatus.RECEIVED));
            orderRepository.saveAll(orders);

            ResaleOrder resaleOrder = ResaleOrder.builder()
                    .id(resaleOrderDTO.getId())
                    .orderNumber(resaleOrderDTO.getOrderNumber())
                    .orderDate(LocalDateTime.now())
                    .orders(orders)
                    .build();

            this.save(resaleOrder);
        }

        Set<OrderDTO> orderDTOs = orders.stream().map(order ->
                OrderDTO.builder()
                        .orderNumber(order.getId())
                        .taxId(order.getClient().getTaxId())
                        .orderItems(generateOrderItems(order))
                        .date(order.getDate())
                        .totalAmount(order.getTotalAmount())
                        .status(order.getStatus())
                        .client(order.getClient().getName())
                        .build()
        ).collect(Collectors.toSet());

        resaleOrderDTO.setOrders(orderDTOs);
        return resaleOrderDTO;
    }

    public ResaleOrderDTO mapResaleOrderToDTO(ResaleOrder resaleOrder) {
        return ResaleOrderDTO.builder()
                .id(resaleOrder.getId())
                .orderNumber(resaleOrder.getOrderNumber())
                .orders(resaleOrder.getOrders().stream()
                        .map(order -> OrderDTO.builder()
                                .orderNumber(order.getId())
                                .status(order.getStatus())
                                .date(order.getDate())
                                .totalAmount(order.getTotalAmount())
                                .client(order.getClient().getName())
                                .address(order.getClient().getAddress())
                                .orderItems(generateOrderItems(order))
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }


    private Set<OrderItemDTO> generateOrderItems(Order order) {
        return order.getProducts().stream()
                .map(product -> OrderItemDTO.builder()
                        .productId(product.getId())
                        .quantity(orderProductRepository.findByOrderIdAndProductId(order.getId(), product.getId()).getQuantity())
                        .build())
                .collect(Collectors.toSet());
    }

    private void validateOrdersQuantity(List<Order> orders) {
        int sum = 0;
        for (Order order : orders) {
            Set<Long> productIds = order.getProducts().stream().map(Product::getId).collect(Collectors.toSet());
            List<OrderProduct> orderProducts = orderProductRepository.findByOrderIdAndProductIdsIn(order.getId(), productIds);
            sum += orderProducts.stream().mapToInt(OrderProduct::getQuantity).sum();
        }
        if (sum < 1000) throw new ResaleOrderException("Orders quantity is less than 1000");
    }

}