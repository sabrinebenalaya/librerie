package com.micro_service_librerie.librerie.Controller;


import javax.validation.Valid;

import com.micro_service_librerie.librerie.Model.Livre;
import com.micro_service_librerie.librerie.Service.LivreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/book")
public class LivreController {
    
    @Autowired
    private LivreService livreService;

//add a book
    @Operation(summary = "Add a book", description = "Add a book", tags ="POST")
    @ApiResponses( value ={ @ApiResponse (responseCode ="200", 
                                         description="Book added",
                                         content ={@Content(mediaType ="application/json", schema =@Schema(implementation = Livre.class))}),     
                            @ApiResponse(responseCode ="400", description="incorrect information entered",content = @Content),
                            @ApiResponse(responseCode ="404", description="this book already exists",content = @Content)})
    @PostMapping("/")
    public ResponseEntity <MappingJacksonValue> addLivre(@RequestParam ("titre") @Valid String titre,
                                                        @RequestParam ("genre") @Valid String genre,
                                                        @RequestParam ("auteur") @Valid String auteur,
                                                        @RequestParam ("nbrpage") @Valid Integer nbrpage){
               if (auteur.matches(".*[0-9].*")){
                throw new IllegalArgumentException("the field 'auteur' must be a String");
               }
               if (genre.matches(".*[0-9].*")){
                throw new IllegalArgumentException("the field 'genre' must be a String");
               }
              
       return new ResponseEntity<>(livreService.add(titre,auteur,genre,nbrpage), HttpStatus.CREATED);
    }

// find all the books
    @Operation(summary = "Find all", description = "Find all the books", tags ="GET")
    @ApiResponses( value ={ @ApiResponse (responseCode ="200", 
                                         description="Liste des Livre trouvés",
                                         content ={@Content(mediaType ="application/json", schema =@Schema(implementation = Livre.class))}),     
                            @ApiResponse(responseCode ="404", description="Pas de livre trouvé",content = @Content)})
    @GetMapping("/all")
    public ResponseEntity<MappingJacksonValue> listeLivre(){
                  return new ResponseEntity<>(livreService.findAll(),HttpStatus.OK);            
    }

// find a book by title
    @Operation(summary = "Find book by title", description = "Find book by title", tags ="GET")
    @ApiResponses( value ={ @ApiResponse (responseCode ="200", 
                                     description="= Livre trouvé",
                                     content ={@Content(mediaType ="application/json", schema =@Schema(implementation = Livre.class))}),     
                        @ApiResponse(responseCode ="404", description="Pas de livre trouvé",content = @Content)})
    @GetMapping("/titre")
    public ResponseEntity<MappingJacksonValue> findLivreByTitre(@RequestParam("titre") @Valid String titre){
                return new ResponseEntity<>(livreService.findByTitre(titre),HttpStatus.OK);              
    }

//find a book by genre
    @Operation(summary = "Find book by genre", description = "Find book by genre", tags ="GET")
    @ApiResponses( value ={ @ApiResponse (responseCode ="200", 
                                 description="= Livre trouvé",
                                 content ={@Content(mediaType ="application/json", schema =@Schema(implementation = Livre.class))}),     
                    @ApiResponse(responseCode ="404", description="Pas de livre trouvé",content = @Content)})
    @GetMapping("/genre")
    public ResponseEntity<MappingJacksonValue> listeLivreByGenre(@RequestParam("genre")@Valid String genre){
            return new ResponseEntity<>(livreService.findByGenre(genre),HttpStatus.OK);           
    }
   
//find a book by auteur
    @Operation(summary = "Find book by title", description = "Find book by title", tags ="GET")
    @ApiResponses( value ={ @ApiResponse (responseCode ="200", 
                                 description="= Livre trouvé",
                                 content ={@Content(mediaType ="application/json", schema =@Schema(implementation = Livre.class))}),     
                    @ApiResponse(responseCode ="404", description="Pas de livre trouvé",content = @Content)}) 
    @GetMapping( "/auteur")
    public ResponseEntity<MappingJacksonValue> listeLivreByAuteur(@RequestParam("auteur") @Valid String auteur){
            return new ResponseEntity<>(livreService.findByAuteur(auteur),HttpStatus.OK);        
    } 
    
//find a book by nbrpage
    @Operation(summary = "Find book by nbrpage", description = "Find book by number of page", tags ="GET")
    @ApiResponses( value ={ @ApiResponse (responseCode ="200", 
                                 description="= Livre trouvé",
                                 content ={@Content(mediaType ="application/json", schema =@Schema(implementation = Livre.class))}),     
                    @ApiResponse(responseCode ="404", description="Pas de livre trouvé",content = @Content)})
    @GetMapping("/nbrpage")
    public ResponseEntity<MappingJacksonValue> listeLivreByNbr_Page(@RequestParam(value = "nbrpage") @Valid Integer nbr){
            return new ResponseEntity<>(livreService.findByNbrPage(nbr),HttpStatus.OK);    
        }

//find book where nbrpage >= x
    @Operation(summary = "Find book >= nbrpage ", description = "Find book that have a number of page greater than", tags ="GET")
    @ApiResponses( value ={ @ApiResponse (responseCode ="200", 
                                 description="= Livre trouvé",
                                 content ={@Content(mediaType ="application/json", schema =@Schema(implementation = Livre.class))}),     
                    @ApiResponse(responseCode ="404", description="Pas de livre trouvé",content = @Content),
                    @ApiResponse(responseCode ="500", description="Verifier le titre saissi",content = @Content)})
    @GetMapping("/pluspage")
    public ResponseEntity<MappingJacksonValue>listeLivrePlusPage(@RequestParam(value = "pluspage") @Valid Integer nbr){
        return new ResponseEntity<>(livreService.findByNbr_pageGreaterThanEqual(nbr),HttpStatus.OK); 
            }

//find book where nbrpage =< x
    @Operation(summary = "Find book =< nbrpage", description = "FFind book that have a number of page less than", tags ="GET")
    @ApiResponses( value ={ @ApiResponse (responseCode ="200", 
                                 description="= Livre trouvé",
                                 content ={@Content(mediaType ="application/json", schema =@Schema(implementation = Livre.class))}),     
                    @ApiResponse(responseCode ="404", description="Pas de livre trouvé",content = @Content),
                    @ApiResponse(responseCode ="500", description="Verifier le titre saissi",content = @Content)})
    @GetMapping("/moinspage")
    public ResponseEntity<MappingJacksonValue> listeLivreMoinsPage(@RequestParam(value = "moinspage")@Valid Integer nbr){
        return new ResponseEntity<>(livreService.findByNbr_pageLessThanEqual(nbr),HttpStatus.OK);     
        }

//find all the users who have this book
    @Operation(summary = "Find all users", description = "Find all the User that have this book", tags ="GET")
    @ApiResponses( value ={ @ApiResponse (responseCode ="200", 
                                         description="Liste des user trouvés",
                                         content ={@Content(mediaType ="application/json", schema =@Schema(implementation = Livre.class))}),     
                            @ApiResponse(responseCode ="404", description="Pas de user trouvé",content = @Content)})
    @GetMapping("/users")
    public ResponseEntity<MappingJacksonValue> listeUserBylivre(@RequestParam (value="titre") @Valid String titre){
        return new ResponseEntity<>(livreService.findAllUser(titre), HttpStatus.OK) ;  
    } 

}
