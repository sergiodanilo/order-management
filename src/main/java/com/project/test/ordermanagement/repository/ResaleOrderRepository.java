package com.project.test.ordermanagement.repository;

import com.project.test.ordermanagement.model.ResaleOrder;
import com.project.test.ordermanagement.repository.base.CRUDBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResaleOrderRepository extends CRUDBaseRepository<ResaleOrder, Long> {

}