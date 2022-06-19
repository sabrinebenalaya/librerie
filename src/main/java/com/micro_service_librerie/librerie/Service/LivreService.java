package com.micro_service_librerie.librerie.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.micro_service_librerie.librerie.Exception.AlreadyExistsException;
import com.micro_service_librerie.librerie.Exception.NotFoundExeption;
import com.micro_service_librerie.librerie.Model.Livre;
import com.micro_service_librerie.librerie.Model.User;
import com.micro_service_librerie.librerie.Repositry.LivreRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class LivreService {
    
    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private UserService userService;

//add a book    
    public MappingJacksonValue add(String titre, String auteur, String genre, Integer nbrpage) throws AlreadyExistsException{  
        Livre livre = new Livre();
         livre.setTitre(titre);
         livre.setAuteur(auteur);
         livre.setGenre(genre);
         livre.setNbrpage(nbrpage);
        if (livreRepository.existsByTitre(titre)) {
            throw new AlreadyExistsException("This book already exists");
        }
        livreRepository.save(livre);
        return filter(livre);
    }

//affichier la liste des livres
    public MappingJacksonValue findAll() {
        List<Livre> livres =livreRepository.findAll();
        if (livres.size()==0)
        {throw new NotFoundExeption("Aucun livre trouvé");}
        return filter(livres);
    }

//trouver livre par son titre
    public MappingJacksonValue findByTitre(String titre) {
    Livre livre = livreRepository.findByTitre(titre).orElseThrow(()-> new NotFoundExeption("livre introuvable"));
    return filter(livre);
    }

//trouver livre par son genre
    public MappingJacksonValue findByGenre(String genre) {
        List<Livre> livres =livreRepository.findByGenre(genre);
        if (livres.size()==0)
        { throw new NotFoundExeption("aucun livre trouvé avec ce genre");}
        return filter(livres);
    }

//trouver livre par son auteur
    public MappingJacksonValue findByAuteur(String auteur){
        List<Livre> livres = livreRepository.findByAuteur(auteur);
         if(livres.size()==0)
         { throw new NotFoundExeption("aucun livre trouvé avec ce nom d'auteur");}     
        return filter(livres);
    }
     
//trouver livre par son nombre de page
    public MappingJacksonValue findByNbrPage(Integer nbr) {
        List<Livre> livres =livreRepository.findByNbrpage(nbr);
        if (livres.size()==0){ throw new NotFoundExeption("aucun livre trouvé avec ce nombre de page");}
        return filter(livres);
    }

//trouver les livres qui ont un nombre de page superieur à un nombre defini
    public MappingJacksonValue findByNbr_pageGreaterThanEqual(Integer nbr) {
        List<Livre> livres =livreRepository.findByNbrpageGreaterThanEqual(nbr);
        if (livres.size()==0){ throw new NotFoundExeption("aucun livre trouvé avec ce nombre de page");}
        return filter(livres);
    }

//trouver les livres qui ont un nombre de page inférieur à un nombre defini
    public MappingJacksonValue findByNbr_pageLessThanEqual(Integer nbr) {
        List<Livre> livres =livreRepository.findByNbrpageLessThanEqual(nbr);
        if (livres.size()==0){ throw new NotFoundExeption("aucun livre trouvé avec ce nombre de page");}
        return filter(livres);
    }

//affichier tous les users qui ont le meme livre     
    public MappingJacksonValue findAllUser(String titre) {
        Livre livre = livreRepository.findByTitre(titre).orElseThrow(()-> new NotFoundExeption("Aucun livre trouver avec le titre "+titre+"."));
        Set<User> users = livre.getUsers();
        if (users.size()== 0){
            throw new NotFoundExeption("le livre "+titre+" n'est pris par aucun utilisateur");
        }
        return userService.filter(users);
}

public MappingJacksonValue filter(Object livres){

    Set<String> fields = new HashSet<String>();

    fields.add("idLivre");
    fields.add("titre");
    fields.add("auteur");
    fields.add("genre");
    fields.add("nbrpage");           

    FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("livreFilter", SimpleBeanPropertyFilter.filterOutAllExcept(fields));

    MappingJacksonValue mapper = new MappingJacksonValue(livres);

    mapper.setFilters(filterProvider);

    return mapper;
}

}
