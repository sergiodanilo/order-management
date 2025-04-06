package com.project.test.ordermanagement.service;

import com.project.test.ordermanagement.model.Product;
import com.project.test.ordermanagement.repository.ProductRepository;
import com.project.test.ordermanagement.service.base.CRUDBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends CRUDBaseServiceImpl<Product, Long> {

    public ProductService(ProductRepository repository) {
        super(repository);
    }

}