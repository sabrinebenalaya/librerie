package com.micro_service_librerie.librerie.Controller;



import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.micro_service_librerie.librerie.Model.Ordder;
import com.micro_service_librerie.librerie.Service.OrderService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;



@RestController
@RequestMapping("/order")
public class OrderController {

@Autowired
private OrderService orderService;

//add a order
@Operation(summary = "Add a Order", description = "Add a Order", tags ="ORDER")
@ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                description="Order is added",
                                content ={@Content(mediaType = "application/json", schema = @Schema(implementation = Ordder.class))}),     
                                @ApiResponse(responseCode ="400", description="incorrect information ",content = @Content)})

@PostMapping("/")
public ResponseEntity<Ordder> addorder(@RequestParam("idUser") @Valid Long idUser){
      
  return new ResponseEntity<Ordder>(orderService.add(idUser),HttpStatus.CREATED);
}     
 


//add a book to the order
@Operation(summary = "Add a Book", description = "Add a Book", tags ="ORDER")
@ApiResponses( value ={@ApiResponse(responseCode ="200", 
                                description="Book added",
                                content ={@Content(mediaType = "application/json", schema = @Schema(implementation = Ordder.class))}),     
                                @ApiResponse(responseCode ="400", description="incorrect information ",content = @Content)})

@PutMapping("/book")
public ResponseEntity<Ordder> putBookInOrder(@RequestParam(required=false, name="id_Order") @Valid Long id_Order,
                                              @RequestParam("idBook") @Valid Long idBook){
      
  return new ResponseEntity<Ordder>(orderService.putBook(id_Order,idBook),HttpStatus.CREATED);
}  



//delete a order
@Operation(summary = "Delete an Order", description = "Delete an Order", tags ="ORDER")
@ApiResponses( value ={@ApiResponse(responseCode ="200",  
                                description="Order is deleted",
                                content ={@Content(mediaType = "application/json", schema = @Schema(implementation = Ordder.class))}),     
                                @ApiResponse(responseCode ="400", description="incorrect information ",content = @Content)})

@DeleteMapping("/delete")
public ResponseEntity<String> delete(@RequestParam ("key") String key,
                                    @RequestParam ("value") String value){
  exceptionfield(key, value);
  return new ResponseEntity<String>(orderService.delete(key, value),HttpStatus.OK);
}  
 


//fetch all the orders
@Operation(summary = "Fetch all the Orders", description = "fetch all the Orders", tags ="ORDER")
@ApiResponses( value ={@ApiResponse(responseCode ="200",  
                                description="Fetch all the orders",
                                content ={@Content(mediaType = "application/json", schema = @Schema(implementation = Ordder.class))})})

@GetMapping("/all")
public ResponseEntity<List<Ordder>> allOrders(){
  return new ResponseEntity<List<Ordder>>(orderService.allOrders(),HttpStatus.OK);
}



// dynamic search
@Operation(summary = "Find order", description = "Find a order", tags ="ORDER")
@ApiResponses( value ={ @ApiResponse (responseCode ="200", 
                                       description="Order found",
                                       content ={@Content(mediaType ="application/json",
                                                          schema =@Schema(implementation = Ordder.class))}),     
                           @ApiResponse(responseCode ="404", description="Order not found",content = @Content)})

@GetMapping("/search") 
public ResponseEntity<List<Ordder>> Search(@RequestParam ("key") String key,
                                                   @RequestParam ("value") String value){
      exceptionfield(key,value);            
      return new ResponseEntity<List<Ordder>>(orderService.searchByOneField(key,value), HttpStatus.OK) ;
} 



//function: the exception field   
public void exceptionfield (String key,String value){
    if (!key.matches("(\\bid_Order\\b)|(\\brefOrder\\b)|(\\bstatusOrder\\b)|(\\bdateOrder\\b)"))
    {throw new IllegalArgumentException("Key must be one of {id_Order,refOrder,statusOrder,dateOrder}");}

    if (key.matches("statusOrder")&&(value.matches(".*[0-9].*")))
    {throw new IllegalArgumentException("Value of this key must be a String");}
 }
}
