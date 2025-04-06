package com.project.test.ordermanagement.repository;

import com.project.test.ordermanagement.model.Client;
import com.project.test.ordermanagement.repository.base.CRUDBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends CRUDBaseRepository<Client, Long> {

    Optional<Client> findByTaxId(String taxId);

}