package com.project.test.ordermanagement.controller;

import com.project.test.ordermanagement.model.dto.ResaleOrderDTO;
import com.project.test.ordermanagement.service.ResaleOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/resale-orders")
@RequiredArgsConstructor
public class ResaleOrderController {

    private final ResaleOrderService resaleOrderService;

    @PostMapping
    public ResponseEntity<ResaleOrderDTO> sendResaleOrders(@RequestBody List<Long> orderIds) {
        return ResponseEntity.ok(resaleOrderService.sendResaleOrders(orderIds));
    }

}
