package com.micro_service_librerie.librerie.Controller.Dynamic;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.micro_service_librerie.librerie.Model.Livre;
import com.micro_service_librerie.librerie.Serie.SearchOperation;
import com.micro_service_librerie.librerie.Service.Dynamic.LivreDynamicService;


import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/DynamicBook")
public class LivreDynamicController {
  
   @Autowired
   private LivreDynamicService livreDynamicService;
     
// dynamic search
   @Operation(summary = "Dynamic Search", description = "Dynamic Search", tags ="DynamicGET")
   @ApiResponses( value ={ @ApiResponse (responseCode ="200", 
                                       description="Book found",
                                       content ={@Content(mediaType ="application/json",
                                                          schema =@Schema(implementation = Livre.class))}),     
                           @ApiResponse(responseCode ="404", description="Book not found",content = @Content)})

   @GetMapping("/search") 
   public ResponseEntity<MappingJacksonValue> Search(@RequestParam ("key") String key,
                                                   @RequestParam ("value") String value,
                                                   @RequestParam ("SearchOperation") SearchOperation Operation){
      exceptionfield(key,value, Operation);            
      return new ResponseEntity<>(livreDynamicService.searchByOneField(key,value,Operation), HttpStatus.OK) ;
   } 

// dynamic delete
   @Operation(summary = "Delete Book", description = "Delete Book by field", tags ="DynamicDelete")
   @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                       description="Book deleted",
                                       content ={@Content(mediaType = "application/json", schema = @Schema(implementation = Livre.class))}),            
                           @ApiResponse(responseCode ="404", description="Book not found",content = @Content)})
   @DeleteMapping("/delete")
   public @ResponseBody String delete (@RequestParam ("key") String key,
                                    @RequestParam ("value") String value,
                                    @RequestParam ("SearchOperation") SearchOperation Operation){
            exceptionfield(key,value, Operation);
           return livreDynamicService.deleteByField(key,value, Operation);
   }
 
   
  
// dynamic update
   @Operation(summary = "Update Book", description = "Update Book by field", tags ="DynamicUpdate")
   @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                       description="Book updated",
                                       content ={@Content(mediaType = "application/json", schema = @Schema(implementation = Livre.class))}),            
                           @ApiResponse(responseCode ="404", description="Book not found",content = @Content)} )
   @PutMapping("/update")
   public @ResponseBody String update (@RequestParam ("key") String key,
                                     @RequestParam ("value") String value,
                                     @RequestParam ("SearchOperation") SearchOperation Operation,
                                     @RequestParam (value = "titre", required = false) @Valid String titre,
                                     @RequestParam (value = "auteur", required = false)@Valid String auteur, 
                                     @RequestParam (value = "genre", required = false)@Valid String genre,
                                     @RequestParam (value="nbrpage", required = false)@Valid Integer nbrpage){
            
            exceptionfield(key,value, Operation);
            if (key.matches("(\\bauteur\\b)|(\\bgenre\\b)")&&(value.matches(".*[0-9].*")))
            {
               throw new IllegalArgumentException("the value of "+key+" must be a String");
            } 
        
         return livreDynamicService.updateByField(key,value, Operation,titre, auteur, genre, nbrpage);
   }  

//function: the exception field   
   public void exceptionfield (String key,String value, SearchOperation Operation){
      if (!key.matches("(\\bauteur\\b)|(\\bgenre\\b)|(\\bnbrpage\\b)|(\\btitre\\b)|(\\bidLivre\\b)"))
      {throw new IllegalArgumentException("Key must be one of {idLivre,titre,auteur,genre,nbrpage}");}

      if((key.matches("(\\bauteur\\b)|(\\bgenre\\b)|(\\btitre\\b)|(\\bidLivre\\b)"))&&(!Operation.toString().matches("EQUAL")))
      {throw new IllegalArgumentException("Search operation must be only 'EQUAL'");}

      if (key.matches("nbrpage")&&(!value.matches("\\d+")))
      {throw new IllegalArgumentException("Value of nbrpage must be a number");}
   }
}
