package com.micro_service_librerie.librerie.Service;


import java.util.Date;
import java.util.List;
import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro_service_librerie.librerie.Exception.NotFoundExeption;
import com.micro_service_librerie.librerie.Model.Book;
import com.micro_service_librerie.librerie.Model.Ordder;
import com.micro_service_librerie.librerie.Model.SearchOperation;
import com.micro_service_librerie.librerie.Model.User;
import com.micro_service_librerie.librerie.Repositry.BookRepository;
import com.micro_service_librerie.librerie.Repositry.OrderRepository;
import com.micro_service_librerie.librerie.Repositry.UserRepositry;
import com.micro_service_librerie.librerie.Repositry.Specification.LibrerieSpecification;
import com.micro_service_librerie.librerie.Repositry.Specification.SearchCriteria;


@Service
public class OrderService {

@Autowired
private OrderRepository orderRepository;

@Autowired
private UserRepositry userRepositry;

@Autowired
private BookRepository bookRepository;


//add order
public Ordder add( Long idUser) {
        User user = userRepositry.findById(idUser).get();
        Date date =new Date();
        String ref = "FM-"+idUser+"-"+ new Random().nextInt(1000);
        
        Ordder order = new Ordder();
        order.setRefOrder(ref); 
        order.setStatusOrder("Pending");
        order.setDateOrder(date);
        order.setUser(user);

        return orderRepository.save(order);
}

//delete an order
public String delete(String key, String value) {
    List<Ordder> orders = findOrder(key, value);
    for (int i = 0; i<orders.size();i++){
        if (!orders.get(i).getStatusOrder().matches("Pending")){
            throw new NotFoundExeption("This Order : "+orders.get(i).getRefOrder()+" is not pending, we can't delete it");
        }
    }
    orderRepository.deleteAll(orders);
    return "Order deleted";
}

//add a book to order
public Ordder putBook( Long id_Order,  Long idBook) {
   
    Ordder order = orderRepository.findById(id_Order).orElseThrow(()-> new NotFoundExeption("id order incorrect")) ;
    Book book = bookRepository.findById(idBook).orElseThrow(()-> new NotFoundExeption("id book incorrect")) ;
   
    order.addBook(book);
    return orderRepository.save(order);
}

//Fetch All the orders
public List<Ordder> allOrders() {
    List<Ordder> orders =orderRepository.findAll();
    if (orders.size()==0){
        throw new NotFoundExeption("Orders not found");
    }
    return orders;
}

// function to find an order
private List<Ordder> findOrder(String key, String value){
    LibrerieSpecification<Ordder> spec = new LibrerieSpecification<>();
    spec.add(new SearchCriteria(key, value, SearchOperation.EQUAL));
    List<Ordder> orders = orderRepository.findAll(spec);
    if(orders.size()==0){
        throw new NotFoundExeption("order not found");
    }
    return orders;
}

//search an order
public List<Ordder> searchByOneField(String key, String value) {
    return findOrder(key, value);
}
  
}
