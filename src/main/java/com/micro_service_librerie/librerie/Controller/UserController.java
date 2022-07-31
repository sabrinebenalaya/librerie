package com.micro_service_librerie.librerie.Controller;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import com.micro_service_librerie.librerie.Model.User;
import com.micro_service_librerie.librerie.Service.UserService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor 
@NoArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userDynamicService;

//add a User
        @Operation(summary = "Add a User", description = "Add a User", tags ="USER")
        @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                        description="User is added",
                                        content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),     
                                        @ApiResponse(responseCode ="400", description="incorrect information ",content = @Content),
                                        @ApiResponse(responseCode ="404", description="this user is already exists",content = @Content)})

        @PostMapping("/")                        
        public  ResponseEntity<MappingJacksonValue> addUser( @RequestParam("firstname") @Valid String firstname,
                                                        @RequestParam("lastname") @Valid String lastname,
                                                        @RequestParam("email") @Valid String email,
                                                        @RequestParam("username") @Valid String username,
                                                        @RequestParam("password") @Valid String password){    
        
        Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(email);
        if (!m.find()){throw new IllegalArgumentException(email+" must be a syntactically correct email address");}
        if (lastname.matches(".*[0-9].*")){throw new IllegalArgumentException(lastname+" must be a String");}
        if (firstname.matches(".*[0-9].*")){throw new IllegalArgumentException(firstname+" must be a String");}
                                                                                                                                                
        return new ResponseEntity<>(userDynamicService.add(firstname, lastname, email, username, password), HttpStatus.CREATED);      
        }



//find all users
        @Operation(summary = "Get all Users", description = "Find all the users", tags ="USER")
        @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                                description="Users found",
                                                content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
                                @ApiResponse(responseCode ="404", description="No User is found",content = @Content) } ) 
        @GetMapping("/all")
        public ResponseEntity<MappingJacksonValue>  findAllUser(){
        return new ResponseEntity<>(userDynamicService.findAll(), HttpStatus.OK);
        }


// dynamic delete
        @Operation(summary = "Delete User", description = "Delete User by field", tags ="USER")
        @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                        description="User deleted",
                                        content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
                                @ApiResponse(responseCode ="404", description="User not found",content = @Content)} )
        @DeleteMapping("/delete")
        public ResponseEntity<String> delete(@RequestParam ("key") String key,
                                        @RequestParam ("value") String value){
        exceptionfield(key,value); 
        return new ResponseEntity<>(userDynamicService.deleteByField(key,value), HttpStatus.OK);
        }



// Dynamic search
        @Operation(summary = "Get User by field", description = "Get User by field", tags ="USER")
        @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                            description="User found",
                                            content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),            
                                @ApiResponse(responseCode ="404", description="User not found",content = @Content) } )
        @GetMapping("/search")
        public ResponseEntity<MappingJacksonValue> Search(@RequestParam ("key") @Valid String key,
                                                          @RequestParam ("value") @Valid String value){
            exceptionfield(key,value); 
            return new ResponseEntity<>(userDynamicService.searchByField(key,value), HttpStatus.OK);
           
        }
        

               
// dynamic update
        @Operation(summary = "Update User", description = "Update User by field", tags ="USER")
        @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                             description="User updated",
                                             content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
                                 @ApiResponse(responseCode ="404", description="User not found",content = @Content)} )
        @PutMapping("/update")
        public ResponseEntity<String> update(@RequestParam ("key") String key,@RequestParam ("value") String value,
                                         @RequestParam (value = "email", required = false) String email,
                                         @RequestParam (value="firstname", required = false) String firstname,
                                         @RequestParam (value="lastname", required = false) String lastname,
                                         @RequestParam (value="username", required = false) String username,
                                         @RequestParam (value="password", required = false) String password){
              
                exceptionfield(key,value); 
                return new ResponseEntity<>(userDynamicService.updateByField(key,value,email,firstname,lastname, username, password), HttpStatus.OK) ;
        }
        

// dynamic searche(Multi)
        @Operation(summary = "Get User ", description = "Get User by two field", tags ="USER")
        @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                            description="User found",
                                            content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
                                @ApiResponse(responseCode ="404", description="User not found",content = @Content)} )
        @GetMapping("/multiple_search")
        public ResponseEntity<MappingJacksonValue> SearchByTwoField(    @RequestParam (value="keyA", required = false) String keyA,
                                                                        @RequestParam (value="valueA", required = false) String valueA,
                                                                        @RequestParam (value="keyB", required = false) String keyB,
                                                                        @RequestParam (value="valueB", required = false) String valueB){
                if ((keyA == null)&& (valueA==null)&&(keyB == null)&& (valueB ==null))
                        {throw new IllegalArgumentException("please enter at least one key and one value");  }

                if ((keyA != null)&& (valueA==null)){throw new IllegalArgumentException("valueA must not be emplty");  }
                if ((keyB != null)&& (valueB==null)){throw new IllegalArgumentException("valueB must not be emplty");  }
                if ((keyA == null)&& (valueA!=null)){throw new IllegalArgumentException("keyA must not be emplty");  }
                if ((keyB == null)&& (valueB!=null)){throw new IllegalArgumentException("keyB must not be emplty");  }

                if ((keyA != null) &&( valueA != null)){exceptionfield(keyA,valueA);} 
                if ((keyB != null) &&( valueB != null)){exceptionfield(keyB,valueB);} 

                return new ResponseEntity<>(userDynamicService.searchByMultiplField(keyA, valueA, keyB, valueB),HttpStatus.OK);
        }



//function that verify the key and the value       
        public void exceptionfield (String key,String value){
                if (!key.matches("(\\bfirstname\\b)|(\\blastname\\b)|(\\bemail\\b)|(\\bidUser\\b)|(\\busername\\b)"))
                {throw new IllegalArgumentException("Key must be one of {idUser,firstname,lastname,email,username}");}
                if (key.matches("idUser")&&(!value.matches("\\d+"))){
                  throw new IllegalArgumentException("the value of idUser must be a number");
                }
                if (key.matches("firstname")&&(value.matches(".*[0-9].*"))){
                        throw new IllegalArgumentException("the value of firstname must be a string");
                }
                if (key.matches("lastname")&&(value.matches(".*[0-9].*"))){
                        throw new IllegalArgumentException("the value of lastname must be a string");
                }
                Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(value);
                if ((key.matches("email"))&&(!m.find())){
                        throw new IllegalArgumentException("the value of email must be a syntactically correct email address");
                }
             }
    
}
