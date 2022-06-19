package com.micro_service_librerie.librerie.Service.Dynamic;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.micro_service_librerie.librerie.Exception.AlreadyExistsException;
import com.micro_service_librerie.librerie.Exception.FieldNotFound;
import com.micro_service_librerie.librerie.Exception.NotFoundExeption;
import com.micro_service_librerie.librerie.Model.Livre;
import com.micro_service_librerie.librerie.Repositry.LivreRepository;
import com.micro_service_librerie.librerie.Repositry.Specification.SearchCriteria;

import com.micro_service_librerie.librerie.Serie.SearchOperation;
import com.micro_service_librerie.librerie.Repositry.Specification.LibrerieSpecification;
import com.micro_service_librerie.librerie.Service.LivreService;

@Service
@SuppressWarnings("")
public class LivreDynamicService {
    
    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private LivreService livreService;

//search book by field
    public MappingJacksonValue searchByOneField(String key, String value, SearchOperation operation) { 
                    return livreService.filter(findBook(key, value, operation));
    }

//Delete Book 
    public String deleteByField(String key, String value, SearchOperation operation) {
        livreRepository.deleteAll(findBook(key, value, operation));
        return "book deleted";
    }

//update Book by field
    public String updateByField(String key, String value, SearchOperation operation,
                             String titre, String auteur, String genre, Integer nbrpage) {
         List<Livre> book = findBook(key, value, operation);
        int i=0;
        do{
            if (book.get(i).getTitre().equalsIgnoreCase(titre)){
                throw new AlreadyExistsException("This title of book is already exists");
            } 
            i++;
        }while((i< book.size()-1)|| (book.get(i).getTitre().equalsIgnoreCase(titre)));
      
        if ((book.size()>1)&& titre!=null)
        {throw new AlreadyExistsException("we can't have more than one book with the same title");}
      

        if((titre==null)||(auteur==null)||(genre==null)||(nbrpage==null)){
            throw new FieldNotFound("Put field to update");
        }

            for(int j = 0; j< book.size(); j++){
                   if (titre != null) {book.get(j).setTitre(titre); }

                   if (auteur != null){  book.get(j).setAuteur(auteur);} 

                   if (genre != null){  book.get(j).setGenre(genre);} 

                   if (nbrpage != null) { book.get(j).setNbrpage(nbrpage);}

                   livreRepository.save(book.get(i));
                     
            }
         return "book updated";  
    }

//private function
    private LibrerieSpecification<Livre> find (String key, String value, SearchOperation operation) {
        LibrerieSpecification<Livre> spec = new LibrerieSpecification<>();
        spec.add(new SearchCriteria(key, value, operation));
        return spec;
    }
    private List<Livre> findBook(String key, String value, SearchOperation operation){
        List<Livre> livres = livreRepository.findAll(find(key, value,operation));
        if(livres.size()==0){
            throw new NotFoundExeption("Book not found");
        }
        return livres;
    }
}
