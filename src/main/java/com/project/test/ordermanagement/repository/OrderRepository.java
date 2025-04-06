package com.project.test.ordermanagement.repository;

import com.project.test.ordermanagement.model.Order;
import com.project.test.ordermanagement.repository.base.CRUDBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CRUDBaseRepository<Order, Long> {

}