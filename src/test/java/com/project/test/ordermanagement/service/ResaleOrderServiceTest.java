package com.project.test.ordermanagement.service;

import com.project.test.ordermanagement.api.ResaleOrderApi;
import com.project.test.ordermanagement.exceptions.ResaleOrderException;
import com.project.test.ordermanagement.model.*;
import com.project.test.ordermanagement.model.dto.ResaleOrderDTO;
import com.project.test.ordermanagement.model.enums.OrderStatus;
import com.project.test.ordermanagement.repository.OrderProductRepository;
import com.project.test.ordermanagement.repository.OrderRepository;
import com.project.test.ordermanagement.repository.ResaleOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResaleOrderServiceTest {

    @Mock
    private ResaleOrderApi resaleOrderApi;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderProductRepository orderProductRepository;
    @Mock
    private ResaleOrderRepository resaleOrderRepository;

    @InjectMocks
    private ResaleOrderService resaleOrderService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        resaleOrderService = new ResaleOrderService(
                resaleOrderRepository,
                resaleOrderApi,
                orderRepository,
                orderProductRepository
        );
    }

    @Test
    void testSendResaleOrders_success() {
        List<Long> orderIds = List.of(1L, 2L);
        UUID orderNumber = UUID.randomUUID();

        Product product1 = Product.builder().id(10L).name("Product A").price(BigDecimal.TEN).build();
        Product product2 = Product.builder().id(20L).name("Product B").price(BigDecimal.TEN).build();

        Client client1 = Client.builder().id(1L).name("Client A").taxId("123456789").build();
        Client client2 = Client.builder().id(2L).name("Client B").taxId("987654321").build();

        Order order1 = Order.builder()
                .id(1L)
                .client(client1)
                .products(Set.of(product1))
                .date(LocalDateTime.now())
                .totalAmount(BigDecimal.TEN)
                .status(OrderStatus.PENDING)
                .build();

        Order order2 = Order.builder()
                .id(2L)
                .client(client2)
                .products(Set.of(product2))
                .date(LocalDateTime.now())
                .totalAmount(BigDecimal.TEN)
                .status(OrderStatus.PENDING)
                .build();

        OrderProduct orderProduct1 = OrderProduct.builder()
                .order(order1).product(product1).quantity(600).build();

        OrderProduct orderProduct2 = OrderProduct.builder()
                .order(order2).product(product2).quantity(500).build();

        ResaleOrderDTO resaleOrderDTO = ResaleOrderDTO.builder()
                .id(1L)
                .orderNumber(orderNumber)
                .build();

        when(resaleOrderApi.sendResaleOrders()).thenReturn(resaleOrderDTO);
        when(orderRepository.findAllById(orderIds)).thenReturn(List.of(order1, order2));
        when(orderProductRepository.findByOrderIdAndProductId(1L, 10L)).thenReturn(orderProduct1);
        when(orderProductRepository.findByOrderIdAndProductId(2L, 20L)).thenReturn(orderProduct2);
        when(orderProductRepository.findByOrderIdAndProductIdsIn(eq(1L), anySet())).thenReturn(List.of(orderProduct1));
        when(orderProductRepository.findByOrderIdAndProductIdsIn(eq(2L), anySet())).thenReturn(List.of(orderProduct2));

        ResaleOrderDTO result = resaleOrderService.sendResaleOrders(orderIds);

        assertNotNull(result);
        assertEquals(orderNumber, result.getOrderNumber());
        assertEquals(2, result.getOrders().size());
        verify(orderRepository).saveAll(anyList());
        verify(resaleOrderRepository).save(any(ResaleOrder.class));
    }

    @Test
    void testSendResaleOrders_throwsResaleOrderException_whenQuantityTooLow() {
        List<Long> orderIds = List.of(1L);

        Product product = Product.builder().id(10L).name("Product A").price(BigDecimal.TEN).build();
        Client client = Client.builder().id(1L).name("Client A").taxId("123456789").build();

        Order order = Order.builder()
                .id(1L)
                .client(client)
                .products(Set.of(product))
                .date(LocalDateTime.now())
                .totalAmount(BigDecimal.TEN)
                .status(OrderStatus.PENDING)
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .order(order).product(product).quantity(200).build();

        ResaleOrderDTO resaleOrderDTO = ResaleOrderDTO.builder()
                .orderNumber(UUID.randomUUID())
                .build();

        when(resaleOrderApi.sendResaleOrders()).thenReturn(resaleOrderDTO);
        when(orderRepository.findAllById(orderIds)).thenReturn(List.of(order));
        when(orderProductRepository.findByOrderIdAndProductIdsIn(eq(1L), anySet())).thenReturn(List.of(orderProduct));

        assertThrows(ResaleOrderException.class, () -> resaleOrderService.sendResaleOrders(orderIds));
        verify(orderRepository, never()).saveAll(anyList());
        verify(resaleOrderRepository, never()).save(any(ResaleOrder.class));
    }

}
