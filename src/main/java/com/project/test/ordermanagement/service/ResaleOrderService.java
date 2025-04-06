package com.project.test.ordermanagement.service;

import com.project.test.ordermanagement.api.ResaleOrderApi;
import com.project.test.ordermanagement.exceptions.ResaleOrderException;
import com.project.test.ordermanagement.model.Order;
import com.project.test.ordermanagement.model.OrderProduct;
import com.project.test.ordermanagement.model.Product;
import com.project.test.ordermanagement.model.dto.ResaleOrderDTO;
import com.project.test.ordermanagement.model.enums.OrderStatus;
import com.project.test.ordermanagement.repository.OrderProductRepository;
import com.project.test.ordermanagement.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResaleOrderService {

    private final ResaleOrderApi resaleOrderApi;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public ResaleOrderDTO sendResaleOrders(List<Long> orderIds) {
        ResaleOrderDTO resaleOrder = resaleOrderApi.sendResaleOrders();

        List<Order> orders = orderRepository.findAllById(orderIds);
        validateOrdersQuantity(orders);

        if (resaleOrder.getOrderNumber() != null) {
            orders.forEach(order -> order.setStatus(OrderStatus.DELIVERED));
            orderRepository.saveAll(orders);
        }

        resaleOrder.setOrders(orders);
        return resaleOrder;
    }

    private void validateOrdersQuantity(List<Order> orders) {
        int sum = 0;
        for (Order order : orders) {
            Set<Long> productIds = order.getProducts().stream().map(Product::getId).collect(Collectors.toSet());
            List<OrderProduct> orderProducts = orderProductRepository.findByOrderIdAndProductIdsIn(order.getId(), productIds);
            sum += orderProducts.stream().mapToInt(OrderProduct::getQuantity).sum();
        }
        if (sum >= 1000) throw new ResaleOrderException("Orders quantity is less than 1000");
    }

}