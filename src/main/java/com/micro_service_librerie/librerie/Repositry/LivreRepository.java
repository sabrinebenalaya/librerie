package com.micro_service_librerie.librerie.Repositry;

import java.util.List;
import java.util.Optional;

import com.micro_service_librerie.librerie.Model.Livre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import org.springframework.stereotype.Repository;



@Repository
public interface LivreRepository extends JpaRepository<Livre, Long>, JpaSpecificationExecutor<Livre> {
   
    List<Livre> findByIdLivre(Long id);  

    Optional<Livre> findByTitre(String titre);

    List<Livre> findByAuteur(String auteur);

    List<Livre> findByNbrpage(Integer nbr);

    List<Livre> findByNbrpageGreaterThanEqual(Integer nbr);

    List<Livre> findByNbrpageLessThanEqual(Integer nbr);

    List<Livre> findByTitreAndNbrpage(String titre, Integer nbrpage);

    List<Livre> findByTitreOrNbrpage(String titre, Integer nbrpage);

    List<Livre> findByGenreAndTitreOrNbrpage(String genre, String titre, Integer nbrpage);

    boolean existsByTitre(String titre);

    List<Livre> findByGenre(String genre);
   
}
