package com.project.test.ordermanagement.repository;

import com.project.test.ordermanagement.model.Client;
import com.project.test.ordermanagement.model.Order;
import com.project.test.ordermanagement.model.OrderProduct;
import com.project.test.ordermanagement.model.Product;
import com.project.test.ordermanagement.model.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class OrderProductRepositoryTest {

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Order savedOrder;
    private Product productA;
    private Product productB;

    @BeforeEach
    void setUp() {
        Client client = Client.builder()
                .name("ACME Corp.")
                .taxId("123456789")
                .email("email@server.com")
                .phone("123456789")
                .address("123 Street Name")
                .build();

        clientRepository.save(client);

        productA = productRepository.save(Product.builder()
                .name("Product A")
                .price(BigDecimal.valueOf(100))
                .measureUnit("Un")
                .sku("1")
                .build());

        productB = productRepository.save(Product.builder()
                .name("Product B")
                .price(BigDecimal.valueOf(200))
                .measureUnit("Un")
                .sku("2")
                .build());

        savedOrder = orderRepository.save(Order.builder()
                .date(LocalDateTime.now())
                .client(client)
                .products(Set.of(productA, productB))
                .totalAmount(BigDecimal.valueOf(300))
                .status(OrderStatus.PENDING)
                .build());

        Optional<OrderProduct> optOrderProduct1 = orderProductRepository.findByOrderIdAndProductId(savedOrder.getId(), productA.getId());
        OrderProduct orderProduct1 = optOrderProduct1.orElseThrow(() -> new RuntimeException("OrderProduct not found"));
        orderProduct1.setQuantity(10);
        orderProductRepository.save(orderProduct1);

        Optional<OrderProduct> optOrderProduct2 = orderProductRepository.findByOrderIdAndProductId(savedOrder.getId(), productB.getId());
        OrderProduct orderProduct2 = optOrderProduct2.orElseThrow(() -> new RuntimeException("OrderProduct not found"));
        orderProduct2.setQuantity(10);
        orderProductRepository.save(orderProduct2);
    }

    @Test
    void shouldFindOrderProductByOrderIdAndProductId() {
        Optional<OrderProduct> result = orderProductRepository.findByOrderIdAndProductId(savedOrder.getId(), productA.getId());

        assertThat(result).isNotNull();
        assertThat(result.get().getQuantity()).isEqualTo(10);
        assertThat(result.get().getProduct().getName()).isEqualTo("Product A");
    }

    @Test
    void shouldFindByOrderIdAndProductIdsIn() {
        Set<Long> productIds = Set.of(productA.getId(), productB.getId());

        List<OrderProduct> results = orderProductRepository.findByOrderIdAndProductIdsIn(savedOrder.getId(), productIds);

        assertThat(results).hasSize(2);
        assertThat(results).extracting(op -> op.getProduct().getId())
                .containsExactlyInAnyOrder(productA.getId(), productB.getId());
    }

    @Test
    void shouldFindAllByOrderId() {
        List<OrderProduct> results = orderProductRepository.findByOrderId(savedOrder.getId());

        assertThat(results).hasSize(2);
    }

}
