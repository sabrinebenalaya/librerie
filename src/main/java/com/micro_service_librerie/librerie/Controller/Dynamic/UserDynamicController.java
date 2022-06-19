package com.micro_service_librerie.librerie.Controller.Dynamic;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.micro_service_librerie.librerie.Model.User;
import com.micro_service_librerie.librerie.Serie.SearchOperation;
import com.micro_service_librerie.librerie.Service.Dynamic.UserDynamicService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/DynamicUser")
public class UserDynamicController {

    @Autowired
    private UserDynamicService userDynamicService;


// Dynamic search
        @Operation(summary = "Get User by field", description = "Get User by field", tags ="DynamicGET")
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
        
// dynamic delete
        @Operation(summary = "Delete User", description = "Delete User by field", tags ="DynamicDelete")
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
        
        
// dynamic update
        @Operation(summary = "Update User", description = "Update User by field", tags ="DynamicUpdate")
        @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                             description="User updated",
                                             content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
                                 @ApiResponse(responseCode ="404", description="User not found",content = @Content)} )
        @PutMapping("/update")
        public ResponseEntity<String> update(@RequestParam ("key") String key,@RequestParam ("value") String value,
                                         @RequestParam (value = "email", required = false) String email,
                                         @RequestParam (value="firstname", required = false) String firstname,
                                         @RequestParam (value="lastname", required = false) String lastname){
              
                exceptionfield(key,value); 

                Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(email);
                if (!m.find()){
                        throw new IllegalArgumentException("the value of email must be a syntactically correct email address");
                }
               return new ResponseEntity<>(userDynamicService.updateByField(key,value,email,firstname,lastname), HttpStatus.OK) ;
        }
        

// dynamic searche(Multi)
        @Operation(summary = "Get User ", description = "Get User by two field", tags ="DynamicGET")
        @ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                            description="User found",
                                            content ={@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
                                @ApiResponse(responseCode ="404", description="User not found",content = @Content)} )
        @GetMapping("/multiple/search")
        public ResponseEntity<MappingJacksonValue> MultiSearch( @RequestParam (value="key1", required = false) String key,
                                                                @RequestParam ("SearchOperation") SearchOperation OperationA,
                                                                @RequestParam (value="value1", required = false) String value,
                                                                @RequestParam (value="key1", required = false) String key1,
                                                                @RequestParam ("SearchOperation1") SearchOperation OperationB,
                                                                @RequestParam (value="value1", required = false) String value1){
            return new ResponseEntity<>(userDynamicService.searchByMultiplField(key,value, key1, value1),HttpStatus.OK);
        }

        
        public void exceptionfield (String key,String value){
                if (!key.matches("(\\bfirstname\\b)|(\\blastname\\b)|(\\bemail\\b)|(\\bidUser\\b)"))
                {throw new IllegalArgumentException("Key must be one of {iduser,firstname,lastname,email}");}
                if (key.matches("idUser")&&(!value.matches("\\d+"))){
                  throw new IllegalArgumentException("the value of idUser must be a number");
                }
                if (key.matches("firstname")&&(value.matches("\\d+"))){
                        throw new IllegalArgumentException("the value of firstname must be a string");
                }
                if (key.matches("lastname")&&(value.matches("\\d+"))){
                        throw new IllegalArgumentException("the value of lastname must be a string");
                }
                Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(value);
                if ((key.matches("email"))&&(!m.find())){
                        throw new IllegalArgumentException("the value of email must be a syntactically correct email address");
                }
             }
    
}
