package com.micro_service_librerie.librerie.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.micro_service_librerie.librerie.Exception.AlreadyExistsException;
import com.micro_service_librerie.librerie.Exception.NotFoundExeption;
import com.micro_service_librerie.librerie.Model.SearchOperation;
import com.micro_service_librerie.librerie.Model.User;
import com.micro_service_librerie.librerie.Repositry.UserRepositry;
import com.micro_service_librerie.librerie.Repositry.Specification.SearchCriteria;
import com.micro_service_librerie.librerie.Repositry.Specification.LibrerieSpecification;

@Service
@SuppressWarnings("")
public class UserService {
   
    @Autowired
    private UserRepositry userRepositry;

//add a User
public MappingJacksonValue add(String firtstname, String lastname, String email, String username, String password) throws AlreadyExistsException{
    User user = new User();
    user.setFirstname(firtstname);
    user.setLastname(lastname);       
    user.setEmail(email);
    user.setUsername(username);
    user.setPassword(password);

    if (userRepositry.existsByEmail(email)){
        throw new AlreadyExistsException("User already exists");
    }

    if (userRepositry.existsByUsername(username)){
        throw new AlreadyExistsException("This username is already taken");
    }
    return filter(userRepositry.save(user));
}

//afficher tous les Users
public  MappingJacksonValue findAll() { 
    List<User> users = userRepositry.findAll();
    if (users.size()==0)
        {throw new NotFoundExeption("Users not found");}
    else{
        return filter(users);
    }
    
}

//dynamic delete 
public String deleteByField(String key, String value)  {
    List<User> user = userRepositry.findAll(find(key, value));
    if (user.size()==0){{throw new NotFoundExeption("User Not found with "+ key+ " : "+value);}}
    userRepositry.deleteAll(userRepositry.findAll(find(key, value)));
    return "User deleted";
}

//dynamic search
public MappingJacksonValue searchByField(String key, String value) {    
    List<User> users =userRepositry.findAll(Specification.where(find(key, value)));
        if (users.size()==0){throw new NotFoundExeption("User Not found with "+ key+ " = "+value);}
        return filter(users) ;
  }

//update user by field
public String updateByField(String key, String value,  String email, String firstname, String lastname,  String username, String password) {
    List<User> user = userRepositry.findAll(find(key, value));
    
    if (user.size()==0){throw new NotFoundExeption("User Not found with "+ key+ " : "+value);}
  
    if((firstname==null)&&(lastname==null)&&(username==null)&&(password==null)&&(email==null)) {
        throw new IllegalArgumentException("You mut enter a data to be update");
    }

    if (email!=null){
        Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(email);
        if (!m.find()){
            throw new IllegalArgumentException("the value of email must be a syntactically correct email address");
        } 
    }
    if (userRepositry.existsByEmail(email)){throw new IllegalArgumentException("this email is already existe");}

    if ((username!=null)&&(userRepositry.existsByUsername(username))){throw new IllegalArgumentException("this username is already existe");}

    if ((firstname!=null)&&(firstname.matches(".*[0-9].*"))){
        throw new IllegalArgumentException("the value of firstname must be a string");
    }

    if ((lastname!=null)&&(lastname.matches(".*[0-9].*"))){
            throw new IllegalArgumentException("the value of lastname must be a string");
    }

    for(int i = 0; i< user.size(); i++){
        if (email != null) {user.get(i).setEmail(email); }
        if (firstname != null) {user.get(i).setFirstname(firstname);} 
        if (lastname != null) { user.get(i).setLastname(lastname);}
        if (password != null) { user.get(i).setPassword(password);}
        if (username != null) { user.get(i).setUsername(username);}
        userRepositry.save(user.get(i));
    }
    return "user updated";  
}

///select user by two fields
public MappingJacksonValue searchByMultiplField(String keyA, String valueA,  
                                                String keyB, String valueB) {
    Specification<User> specA;
    Specification<User> specB;
    List<User> UserEntity;
            
    if ((valueA != null) && (keyA != null)) { specA = find(keyA, valueA);}else {specA = null;}
    if ((valueB != null) && (keyB != null)) {specB = find(keyB, valueB);}else {specB = null;}

    if ((specA != null)|| (specB != null))
        {            
            UserEntity =  userRepositry.findAll(Specification.where(specA).and(specB));   
        }
    else{
            UserEntity = null; 
        }

    if (UserEntity.size()==0){throw new NotFoundExeption("No user is found");}

    return filter(UserEntity) ;	
}

//Find a user
        private LibrerieSpecification<User> find (String key, String value){
        LibrerieSpecification<User> spec = new LibrerieSpecification<>();
        spec.add(new SearchCriteria(key, value, SearchOperation.EQUAL));
        return spec;
    }

//Make a filter for the user table that don't show the list of books
public MappingJacksonValue filter(Object user){

    Set<String> fields = new HashSet<String>();

    fields.add("idUser");
    fields.add("firstname");
    fields.add("lastname");
    fields.add("email");
    fields.add("username");

    FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("userfilter", SimpleBeanPropertyFilter.filterOutAllExcept(fields));

    MappingJacksonValue mapper = new MappingJacksonValue(user);

    mapper.setFilters(filterProvider);

    return mapper;
}

}
