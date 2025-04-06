package com.project.test.ordermanagement.service;

import com.project.test.ordermanagement.model.Client;
import com.project.test.ordermanagement.repository.ClientRepository;
import com.project.test.ordermanagement.service.base.CRUDBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ClientService extends CRUDBaseServiceImpl<Client, Long> {

    public ClientService(ClientRepository repository) {
        super(repository);
    }

}