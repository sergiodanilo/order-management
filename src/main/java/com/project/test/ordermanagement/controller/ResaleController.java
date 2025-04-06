package com.project.test.ordermanagement.controller;

import com.project.test.ordermanagement.controller.base.CRUDBaseController;
import com.project.test.ordermanagement.model.Resale;
import com.project.test.ordermanagement.service.ResaleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resales")
public class ResaleController extends CRUDBaseController<Resale, Long> {

    public ResaleController(ResaleService service) {
        super(service);
    }

}