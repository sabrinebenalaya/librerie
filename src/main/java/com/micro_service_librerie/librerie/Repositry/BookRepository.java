package com.micro_service_librerie.librerie.Repositry;

import java.util.List;
import java.util.Optional;

import com.micro_service_librerie.librerie.Model.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
   
    List<Book> findByIdBook(Long idBook);  

    Optional<Book> findByTitle(String title);

    boolean existsByTitle(String title);

   
}
