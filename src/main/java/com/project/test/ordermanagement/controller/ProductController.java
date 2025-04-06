package com.project.test.ordermanagement.controller;

import com.project.test.ordermanagement.controller.base.CRUDBaseController;
import com.project.test.ordermanagement.model.Product;
import com.project.test.ordermanagement.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController extends CRUDBaseController<Product, Long> {

    public ProductController(ProductService service) {
        super(service);
    }

}