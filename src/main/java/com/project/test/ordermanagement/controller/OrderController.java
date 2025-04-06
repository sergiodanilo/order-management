package com.project.test.ordermanagement.controller;

import com.project.test.ordermanagement.model.dto.OrderDTO;
import com.project.test.ordermanagement.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> registerOrder(@RequestBody OrderDTO order) {
        return orderService.registerOrder(order)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> get(@PathVariable Long orderId) {
        return orderService.findById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}