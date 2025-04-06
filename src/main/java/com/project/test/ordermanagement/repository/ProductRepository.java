package com.project.test.ordermanagement.repository;

import com.project.test.ordermanagement.model.Product;
import com.project.test.ordermanagement.repository.base.CRUDBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CRUDBaseRepository<Product, Long> {

}