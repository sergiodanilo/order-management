package com.project.test.ordermanagement.repository;

import com.project.test.ordermanagement.model.Resale;
import com.project.test.ordermanagement.repository.base.CRUDBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResaleRepository extends CRUDBaseRepository<Resale, Long> {

}