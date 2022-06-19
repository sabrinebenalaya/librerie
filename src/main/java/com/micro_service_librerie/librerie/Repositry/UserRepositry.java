package com.micro_service_librerie.librerie.Repositry;

import java.util.List;
import java.util.Optional;

import com.micro_service_librerie.librerie.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositry extends JpaRepository<User,Long>, JpaSpecificationExecutor<User>{


    Optional<User> findByIdUser(Long idUser);

    List<User> findByFirstnameAndLastname(String firstname, String lastname);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}
