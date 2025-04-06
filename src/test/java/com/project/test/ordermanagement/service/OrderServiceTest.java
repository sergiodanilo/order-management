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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Test
    void registerOrder_shouldRegisterAndReturnDTO() {
        String taxId = "123456789";
        Client client = Client.builder().id(1L).name("Test Client").taxId(taxId).build();

        Product product1 = Product.builder().id(1L).name("Product 1").price(BigDecimal.valueOf(50)).build();
        Product product2 = Product.builder().id(2L).name("Product 2").price(BigDecimal.valueOf(100)).build();

        OrderDTO inputOrder = OrderDTO.builder()
                .taxId(taxId)
                .orderItems(Set.of(
                        OrderItemDTO.builder().productId(1L).quantity(1).build(),
                        OrderItemDTO.builder().productId(2L).quantity(2).build()
                ))
                .build();

        Order orderEntity = Order.builder()
                .id(10L)
                .date(LocalDateTime.now())
                .client(client)
                .products(Set.of(product1, product2))
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.valueOf(150))
                .build();

        OrderProduct orderProduct1 = OrderProduct.builder()
                .order(orderEntity)
                .product(product1)
                .quantity(1)
                .build();

        OrderProduct orderProduct2 = OrderProduct.builder()
                .order(orderEntity)
                .product(product2)
                .quantity(2)
                .build();

        when(clientRepository.findByTaxId(taxId)).thenReturn(Optional.of(client));
        when(productRepository.findAllById(anySet())).thenReturn(List.of(product1, product2));
        when(orderRepository.save(any())).thenReturn(orderEntity);
        when(orderProductRepository.findByOrderIdAndProductId(eq(10L), eq(1L)))
                .thenReturn(orderProduct1);
        when(orderProductRepository.findByOrderIdAndProductId(eq(10L), eq(2L)))
                .thenReturn(orderProduct2);

        Optional<OrderDTO> result = orderService.registerOrder(inputOrder);

        assertTrue(result.isPresent());
        OrderDTO dto = result.get();
        assertEquals(10L, dto.getOrderNumber());
        assertEquals("Test Client", dto.getClient());
        assertEquals(2, dto.getOrderItems().size());
    }

    @Test
    void registerOrder_shouldThrowWhenClientNotFound() {
        when(clientRepository.findByTaxId(anyString())).thenReturn(Optional.empty());

        OrderDTO orderDTO = OrderDTO.builder().taxId("000000000").build();

        assertThrows(EntityNotFoundException.class, () -> {
            orderService.registerOrder(orderDTO);
        });
    }

    @Test
    void findById_shouldReturnOrderDTO() {
        Client client = Client.builder().id(1L).name("Test Client").build();
        Product product = Product.builder().id(1L).name("Test Product").price(BigDecimal.TEN).build();

        Order order = Order.builder()
                .id(1L)
                .date(LocalDateTime.now())
                .status(OrderStatus.DELIVERED)
                .totalAmount(BigDecimal.TEN)
                .client(client)
                .products(Set.of(product))
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .order(order)
                .product(product)
                .quantity(2)
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderProductRepository.findByOrderId(1L)).thenReturn(List.of(orderProduct));

        Optional<OrderDTO> result = orderService.findById(1L);

        assertTrue(result.isPresent());
        OrderDTO dto = result.get();
        assertEquals("Test Client", dto.getClient());
        assertEquals(OrderStatus.DELIVERED, dto.getStatus());
        assertEquals(BigDecimal.TEN, dto.getTotalAmount());
        assertEquals(1, dto.getOrderItems().size());
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            orderService.findById(1L);
        });
    }

}
