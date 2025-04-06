package com.project.test.ordermanagement.repository;

import com.project.test.ordermanagement.model.OrderProduct;
import com.project.test.ordermanagement.repository.base.CRUDBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderProductRepository extends CRUDBaseRepository<OrderProduct, Long> {

    OrderProduct findByOrderIdAndProductId(Long orderId, Long productId);

    @Query("SELECT op FROM OrderProduct op WHERE op.order.id = :orderId AND op.product.id IN (:productIds)")
    List<OrderProduct> findByOrderIdAndProductIdsIn(Long orderId, Set<Long> productIds);

    List<OrderProduct> findByOrderId(Long id);

}