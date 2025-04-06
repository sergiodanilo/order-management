package com.project.test.ordermanagement.controller;

import com.project.test.ordermanagement.model.ResaleOrder;
import com.project.test.ordermanagement.model.dto.ResaleOrderDTO;
import com.project.test.ordermanagement.service.ResaleOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/resale-orders")
@RequiredArgsConstructor
public class ResaleOrderController {

    private final ResaleOrderService resaleOrderService;

    @PostMapping
    public ResponseEntity<ResaleOrderDTO> sendResaleOrders(@RequestBody List<Long> orderIds) {
        return ResponseEntity.ok(resaleOrderService.sendResaleOrders(orderIds));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResaleOrderDTO> get(@PathVariable Long id) {
        Optional<ResaleOrder> opt = resaleOrderService.findById(id);
        return opt.map(resaleOrder -> ResponseEntity.ok(resaleOrderService.mapResaleOrderToDTO(resaleOrder)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
