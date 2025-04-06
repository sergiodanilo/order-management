package com.project.test.ordermanagement.service;

import com.project.test.ordermanagement.model.Resale;
import com.project.test.ordermanagement.repository.ResaleRepository;
import com.project.test.ordermanagement.service.base.CRUDBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ResaleService extends CRUDBaseServiceImpl<Resale, Long> {

    public ResaleService(ResaleRepository repository) {
        super(repository);
    }

}
