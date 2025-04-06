package com.project.test.ordermanagement.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface CRUDBaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

}