package com.project.test.ordermanagement.controller;

import com.project.test.ordermanagement.controller.base.CRUDBaseController;
import com.project.test.ordermanagement.model.Client;
import com.project.test.ordermanagement.service.ClientService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientController extends CRUDBaseController<Client, Long> {

    public ClientController(ClientService service) {
        super(service);
    }

}