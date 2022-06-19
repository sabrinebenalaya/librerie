package com.micro_service_librerie.librerie.Controller;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import com.micro_service_librerie.librerie.Exception.NotFoundExeption;
import com.micro_service_librerie.librerie.Model.User;
import com.micro_service_librerie.librerie.Service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;



@RestController
@RequestMapping("/user")
public class UserController {
   
    @Autowired
    private UserService userService;

//add a User
    @Operation(summary = "Add a User", description = "Add a User", tags ="POST")
    @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                        description="User added",
                                        content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),     
                                        @ApiResponse(responseCode ="400", description="incorrect information ",content = @Content),
                                        @ApiResponse(responseCode ="404", description="this user is already exists",content = @Content)})

    @PostMapping("/")                        
    public  ResponseEntity<MappingJacksonValue> addUser( @RequestParam("firstname") @Valid String firstname,
                                                         @RequestParam("lastname") @Valid String lastname,
                                                         @RequestParam("email") @Valid String email){    
         
         Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(email);
         if (!m.find()){throw new IllegalArgumentException(email+" must be a syntactically correct email address");}
         if (lastname.matches(".*[0-9].*")){throw new IllegalArgumentException(lastname+" must be a String");}
         if (firstname.matches(".*[0-9].*")){throw new IllegalArgumentException(firstname+" must be a String");}
                                                                                                                                                   
         return new ResponseEntity<>(userService.add(firstname, lastname, email), HttpStatus.CREATED);      
    }

//add a book to a books-list in a user table
    @Operation(summary = "Add a book to a User", description = "Add book to a User", tags ="PUT")
    @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                        description="Livre ajouté",
                                        content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
                                        @ApiResponse(responseCode ="404", description="les identifiants sont innexistantes",content = @Content),
                            } )
    @PutMapping("/livre")
    public  ResponseEntity<User> addLivretoUser(@RequestParam("iduser") Long iduser, @RequestParam("idlivre") Long idlivre)throws NotFoundExeption{
       
        return ResponseEntity.accepted().body(userService.addLivretoUser(iduser, idlivre));    
    }

//find all users
    @Operation(summary = "Get all Users", description = "Affichier tous le sutilisateurs", tags ="GET")
    @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                        description="User trouvé",
                                        content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
                            @ApiResponse(responseCode ="404", description="Aucun User trouvé",content = @Content) } ) 
    @GetMapping("/users")
    public ResponseEntity<MappingJacksonValue>  findAllUser(){
       return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

//find User by  ID
    @Operation(summary = "Get User By ID", description = "Find a User by his ID", tags ="GET")
    @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                        description="User trouvé",
                                        content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
                                        
                            @ApiResponse(responseCode ="404", description="User non trouvé",content = @Content),
                            } )                          
    @GetMapping("/")
    public ResponseEntity<MappingJacksonValue> findUserById(@RequestParam Long id) throws NotFoundException {
        return ResponseEntity.ok(userService.findById(id));  
    }   

//find a user by  name
    @Operation(summary = "Get User By Name", description = "Find a User by his Name", tags ="GET")
    @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                        description="User trouvé",
                                        content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
                                        
                            @ApiResponse(responseCode ="404", description="User non trouvé",content = @Content),
                            } )
    @GetMapping("/username")
    public  ResponseEntity<MappingJacksonValue> findUser(@RequestParam("Firstname") @Valid String firstname, @RequestParam("Lastname") @Valid String lastname){
        return new ResponseEntity<>(userService.findByName(firstname, lastname), HttpStatus.OK);
    }

//find a user by email
    @Operation(summary = "Get User By Email", description = "Find a User by his EMAIL", tags ="GET")
    @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                        description="User trouvé",
                                        content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
                                        
                            @ApiResponse(responseCode ="404", description="User non trouvé",content = @Content),
                            } )
    @GetMapping("/email")
    public  ResponseEntity<MappingJacksonValue> findUserByemail(@RequestParam("Email") @Valid String email){
        return new ResponseEntity<>(userService.findByEmail(email), HttpStatus.OK);       
    }   

//find all a boks by user
    @Operation(summary = "Get list off Book By User", description = "Find all the books by one User", tags ="GET")
    @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                        description="Livres trouvés",
                                        content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
                                        
                            @ApiResponse(responseCode ="404", description="Livres non trouvé",content = @Content),
                            } )
    @GetMapping("/livres")
    public  ResponseEntity<MappingJacksonValue> findAllLivreByUser(@RequestParam("idUser") Long idUser){
        return new ResponseEntity<>(userService.findAllLivreByUser(idUser), HttpStatus.OK);
    }  
    
}
