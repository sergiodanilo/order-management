package com.project.test.ordermanagement.controller;

import com.project.test.ordermanagement.model.Client;
import com.project.test.ordermanagement.model.Product;
import com.project.test.ordermanagement.model.Resale;
import com.project.test.ordermanagement.model.dto.OrderDTO;
import com.project.test.ordermanagement.model.dto.OrderItemDTO;
import com.project.test.ordermanagement.service.ClientService;
import com.project.test.ordermanagement.service.OrderService;
import com.project.test.ordermanagement.service.ProductService;
import com.project.test.ordermanagement.service.ResaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/startup")
@RequiredArgsConstructor
public class StartupController {

    private final ResaleService resaleService;
    private final ClientService clientService;
    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<Void> initializeData() {
        Client client = Client.builder()
                .name("João da Silva")
                .email("joao" + new Random().nextInt(1000) + "@gmail.com")
                .phone("11999990000")
                .address("Rua Exemplo, 123")
                .taxId(UUID.randomUUID().toString().substring(0, 13))
                .build();
        clientService.save(client);

        Resale resale = Resale.builder()
                .businessName("Comercial Exemplo LTDA")
                .contractName("João da Silva")
                .deliveryAddress("Rua da Entrega, 456")
                .email("contato@exemplo.com")
                .phone("11999887766")
                .taxId(UUID.randomUUID().toString().substring(0, 13))
                .tradeName("Comercial Exemplo")
                .build();
        resaleService.save(resale);

        for (int i = 0; i < 10; i++) {
            Product product = Product.builder()
                    .name("Produto " + (i + 1))
                    .description("Descrição do Produto " + (i + 1))
                    .price(BigDecimal.valueOf(new Random().nextDouble(10, 100)))
                    .sku("SKU-" + UUID.randomUUID())
                    .measureUnit("UN")
                    .build();
            productService.save(product);
        }

        Set<OrderItemDTO> orderItems = new HashSet<>();

        for (int i = 0; i < 5; i++) {
            OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                    .productId((long) i + 1)
                    .quantity(200)
                    .build();

            orderItems.add(orderItemDTO);
        }

        OrderDTO orderDTO = OrderDTO.builder()
                .taxId(client.getTaxId())
                .orderItems(orderItems)
                .date(LocalDateTime.now())
                .build();

        orderService.registerOrder(orderDTO);

        return ResponseEntity.noContent().build();
    }

}
