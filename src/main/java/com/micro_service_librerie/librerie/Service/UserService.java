package com.micro_service_librerie.librerie.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.micro_service_librerie.librerie.Exception.AlreadyExistsException;
import com.micro_service_librerie.librerie.Exception.NotFoundExeption;
import com.micro_service_librerie.librerie.Model.Livre;
import com.micro_service_librerie.librerie.Model.User;
import com.micro_service_librerie.librerie.Repositry.LivreRepository;
import com.micro_service_librerie.librerie.Repositry.UserRepositry;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor

public class UserService {

    @Autowired
    private UserRepositry userRepositry;

    @Autowired
    private LivreRepository livreRepository;
   
    @Autowired
    @Lazy private LivreService livreService;

//add a User
    public MappingJacksonValue add(String firtstname, String lastname, String email) throws AlreadyExistsException{
        User user = new User();
        user.setFirstname(firtstname);
        user.setLastname(lastname);       
        user.setEmail(email);
        if (userRepositry.existsByEmail(email)){
            throw new AlreadyExistsException("User already exists");
        }
        return filter(userRepositry.save(user));
    }

//put a book in a user table
    public User addLivretoUser(Long idUser, Long idLivre) {
        User user = userRepositry.findById(idUser).orElseThrow(()-> new NotFoundExeption("id user incorrect")) ;
        Livre livre = livreRepository.findById(idLivre).orElseThrow(()-> new NotFoundExeption("id livre incorrect")) ;
        Set<Livre> livres = user.getLivres();
        livres.forEach(l->{
            if  (l.getIdLivre()==idLivre){
                throw new AlreadyExistsException("This user already have this book");
            }
        }
       );
        user.addLivre(livre);
        return userRepositry.save(user);     
    }

//afficher tous les Users
    public  MappingJacksonValue findAll() { 
        List<User> users = userRepositry.findAll();
        if (users.size()==0)
        {throw new NotFoundExeption("aucun user trouvé");}
        else{
            return filter(users);
        }
        
    }

//trouver un User par un identifiant
    public MappingJacksonValue findById(Long id) {
        User user = userRepositry.findById(id).orElseThrow(()-> new NotFoundExeption("identifiant incorrect")) ;
        return filter(user);
    }

//affichier un User par son non et prenom
public MappingJacksonValue findByName(String firstname, String lastname) {
    List<User> users = userRepositry.findByFirstnameAndLastname(firstname, lastname);
        if (users.size()==0){
            throw new NotFoundExeption("aucun user trouvé");
        }else{
            return filter(users);
        } 
 }

//trouver un User par son email
    public MappingJacksonValue findByEmail(String email) {
        User user = userRepositry.findByEmail(email).orElseThrow(()-> new NotFoundExeption("email introuvable")) ; 
        return filter(user);             
    }
    
//trouver la liste des livre par un user 
public MappingJacksonValue findAllLivreByUser(Long idUser) {
    User user = userRepositry.findById(idUser).orElseThrow(()-> new NotFoundExeption("User introuvable")) ;
    Set<Livre> livres = user.getLivres();
    if (livres.size()==0){ throw  new NotFoundExeption("aucun livre n'a été trouvé pour cet utilisateur");} 
        
    return livreService.filter(livres);
}   

//Make a filter for the user table that don't show the list of books
public MappingJacksonValue filter(Object user){

    Set<String> fields = new HashSet<String>();

    fields.add("idUser");
    fields.add("firstname");
    fields.add("lastname");
    fields.add("email");

    FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("userfilter", SimpleBeanPropertyFilter.filterOutAllExcept(fields));

    MappingJacksonValue mapper = new MappingJacksonValue(user);

    mapper.setFilters(filterProvider);

    return mapper;
}

}



