package com.micro_service_librerie.librerie.Service;



import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.micro_service_librerie.librerie.Exception.AlreadyExistsException;
import com.micro_service_librerie.librerie.Exception.NotFoundExeption;
import com.micro_service_librerie.librerie.Model.Book;
import com.micro_service_librerie.librerie.Model.SearchOperation;
import com.micro_service_librerie.librerie.Repositry.BookRepository;
import com.micro_service_librerie.librerie.Repositry.Specification.SearchCriteria;
import com.micro_service_librerie.librerie.Repositry.Specification.LibrerieSpecification;

@Service
@SuppressWarnings("")

public class BookService {
    
    @Autowired
    private BookRepository bookRepository;


//add a book    
    public MappingJacksonValue add(String title, String author, String gender, Integer quantity, Integer price) {  
        Book book = new Book();
        book.setAuthor(author);
        book.setGender(gender);
        book.setTitle(title);
        book.setQuantity(quantity);
        book.setPrice(price);
        if (bookRepository.existsByTitle(title)) {
            throw new AlreadyExistsException("This book already exists");
        }
        bookRepository.save(book);
        return filter(book);
    }

//Delete Book 
    public String deleteByField(String key, String value, SearchOperation operation) {
        List<Book> books = findBook(key, value, operation);
        bookRepository.deleteAll(books);  
        return books.size()+" books deleted";
    }  

//search book by field
    public MappingJacksonValue searchByOneField(String key, String value, SearchOperation operation) { 
                    return filter(findBook(key, value, operation));
    }

//update Book by field
    public String updateByField(String key, String value, SearchOperation operation,
                               String title, String author, String gender, Integer quantity, Integer price) {
        List<Book> book = findBook(key, value, operation);
        int nbr = book.size();
        int i=0;
        do{
            if (book.get(i).getTitle().equalsIgnoreCase(title)){
                throw new AlreadyExistsException("This title of book is already exists");
            } 
            i++;
        }while((i== nbr)|| (book.get(i-1 ).getTitle().equalsIgnoreCase(title)));
      
        if ((nbr>1)&& title!=null){
            throw new AlreadyExistsException("we can't have more than one book with the same title");
        }
      
            for(int j = 0; j< nbr; j++){
                   if (title != null) {book.get(j).setTitle(title); }

                   if (author != null){  book.get(j).setAuthor(author);} 

                   if (gender != null){  book.get(j).setGender(gender);} 

                   if (quantity != null) { book.get(j).setQuantity(quantity);}

                   if (price != null) { book.get(j).setPrice(price);}

                   bookRepository.save(book.get(i));          
            }
         return nbr+" book updated";  
    }

//Find all the books
    public MappingJacksonValue FindAll() {
        List<Book> books = bookRepository.findAll();
        if (books.size()==0){
            throw new NotFoundExeption("No Book is found");
        }
        return filter(books);
    }

// function to find a book
    private List<Book> findBook(String key, String value, SearchOperation operation){
        LibrerieSpecification<Book> spec = new LibrerieSpecification<>();
        spec.add(new SearchCriteria(key, value, operation));
        
        List<Book> books = bookRepository.findAll(spec);
        if(books.size()==0){
            throw new NotFoundExeption("Book not found");
        }
        return books;
    }

//filter
public MappingJacksonValue filter(Object books){

    Set<String> fields = new HashSet<String>();

    fields.add("idBook");
    fields.add("title");
    fields.add("author");
    fields.add("gender");
    fields.add("quantity");           
    fields.add("price");           

    FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("BookFilter", SimpleBeanPropertyFilter.filterOutAllExcept(fields));

    MappingJacksonValue mapper = new MappingJacksonValue(books);

    mapper.setFilters(filterProvider);

    return mapper;
}


}
