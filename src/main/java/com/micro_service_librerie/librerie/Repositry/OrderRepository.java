package com.micro_service_librerie.librerie.Repositry;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.micro_service_librerie.librerie.Model.Ordder;

@Repository
public interface OrderRepository extends JpaRepository<Ordder, Long>, JpaSpecificationExecutor<Ordder>{

  
    
}
