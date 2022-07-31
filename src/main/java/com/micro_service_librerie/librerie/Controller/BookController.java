package com.micro_service_librerie.librerie.Controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.micro_service_librerie.librerie.Exception.FieldNotFound;
import com.micro_service_librerie.librerie.Model.Book;
import com.micro_service_librerie.librerie.Model.SearchOperation;
import com.micro_service_librerie.librerie.Service.BookService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/book")
public class BookController {
  
   @Autowired
   private BookService bookDynamicService;

//add a book
@Operation(summary = "Add a book", description = "Add a book", tags ="BOOK")
@ApiResponses( value ={ @ApiResponse (responseCode ="200", 
                                     description="Book added",
                                     content ={@Content(mediaType ="application/json", schema =@Schema(implementation = Book.class))}),     
                        @ApiResponse(responseCode ="400", description="please check your information",content = @Content)})
@PostMapping("/")
public ResponseEntity <MappingJacksonValue> addBook(@RequestParam ("title") @Valid String title,
                                                    @RequestParam ("gender") @Valid String gender,
                                                    @RequestParam ("author") @Valid String author,
                                                    @RequestParam ("quantity") @Valid Integer quantity,
                                                    @RequestParam ("price") @Valid Integer price){
           if (author.matches(".*[0-9].*")){
            throw new IllegalArgumentException("the field 'author' must be a String");
           }
           if (gender.matches(".*[0-9].*")){
            throw new IllegalArgumentException("the field 'gender' must be a String");
           }
           if (price.toString().matches("[0-9]")){
            throw new IllegalArgumentException("the field 'price' must be a number");
           }
           if (quantity.toString().matches("[0-9]")){
            throw new IllegalArgumentException("the field 'quantity' must be a number");
           }
   return new ResponseEntity<>(bookDynamicService.add(title,author,gender,quantity,price), HttpStatus.CREATED);
}

 

// dynamic delete
@Operation(summary = "Delete Book", description = "Delete Book by field", tags ="BOOK")
@ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                    description="Book deleted",
                                    content ={@Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))}),            
                        @ApiResponse(responseCode ="404", description="Book not found",content = @Content)})
@DeleteMapping("/delete")
public @ResponseBody String delete (@RequestParam ("key") String key,
                                    @RequestParam ("value") String value,
                                    @RequestParam ("SearchOperation") SearchOperation Operation){
         exceptionfield(key,value, Operation);
         return bookDynamicService.deleteByField(key,value, Operation);
}



// dynamic search
@Operation(summary = "Find book", description = "Find a book", tags ="BOOK")
@ApiResponses( value ={ @ApiResponse (responseCode ="200", 
                                       description="Book found",
                                       content ={@Content(mediaType ="application/json",
                                                          schema =@Schema(implementation = Book.class))}),     
                           @ApiResponse(responseCode ="404", description="Book not found",content = @Content)})

@GetMapping("/search") 
public ResponseEntity<MappingJacksonValue> Search(@RequestParam ("key") String key,
                                                   @RequestParam ("value") String value,
                                                   @RequestParam ("SearchOperation") SearchOperation Operation){
      exceptionfield(key,value, Operation);            
      return new ResponseEntity<>(bookDynamicService.searchByOneField(key,value,Operation), HttpStatus.OK) ;
} 


 
// dynamic update
@Operation(summary = "Update Book", description = "Update Book by field", tags ="BOOK")
@ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                       description="Book updated",
                                       content ={@Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))}),            
                           @ApiResponse(responseCode ="404", description="Book not found",content = @Content)} )
@PutMapping("/update")
public @ResponseBody String update (@RequestParam ("key") String key,
                                     @RequestParam ("value") String value,
                                     @RequestParam ("SearchOperation") SearchOperation Operation,
                                     @RequestParam (value = "title", required = false) @Valid String title,
                                     @RequestParam (value = "author", required = false)@Valid String author, 
                                     @RequestParam (value = "gender", required = false)@Valid String gender,
                                     @RequestParam (value="quantity", required = false)@Valid Integer quantity,
                                     @RequestParam (value = "price", required = false)@Valid Integer price){
            
         exceptionfield(key,value, Operation);  
             
         if((title==null)&&(author==null)&&(gender==null)&&(quantity==null)&&(price==null)){
            throw new FieldNotFound("Put field to update");
         }
         return bookDynamicService.updateByField(key,value, Operation,title, author, gender, quantity, price);
}  



// Find all the books
@Operation(summary = "Find all the book", description = "Find all the book book", tags ="BOOK")
@ApiResponses( value ={ @ApiResponse (responseCode ="200", 
                                       description="Book found",
                                       content ={@Content(mediaType ="application/json",
                                                          schema =@Schema(implementation = Book.class))}),     
                           @ApiResponse(responseCode ="404", description="No Book is found",content = @Content)})

@GetMapping("/all") 
public ResponseEntity<MappingJacksonValue> FindAll(){
      return new ResponseEntity<>(bookDynamicService.FindAll(), HttpStatus.OK) ;
} 



//function: the exception field   
   public void exceptionfield (String key,String value, SearchOperation Operation){
      if (!key.matches("(\\bauthor\\b)|(\\bgender\\b)|(\\bquantity\\b)|(\\btitle\\b)|(\\bidBook\\b)|(\\bprice\\b)"))
      {throw new IllegalArgumentException("Key must be one of {idBook,title,author,gender,quantity,price}");}

      if((key.matches("(\\bauthor\\b)|(\\bgender\\b)|(\\btitle\\b)|(\\bidBook\\b)"))&&(!Operation.toString().matches("EQUAL")))
      {throw new IllegalArgumentException("Search operation for this key must be only 'EQUAL'");}

      if (key.matches("(\\bnquantity\\b)|(\\bprice\\b)")&&(!value.matches("\\d+")))
      {throw new IllegalArgumentException("Value of this key must be a number");}

      if (key.matches("(\\bauthor\\b)|(\\bgender\\b)")&&(value.matches(".*[0-9].*")))
      {throw new IllegalArgumentException("Value of this key must be a String");}
   }
}
