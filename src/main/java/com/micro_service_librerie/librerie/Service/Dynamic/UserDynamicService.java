package com.micro_service_librerie.librerie.Service.Dynamic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.micro_service_librerie.librerie.Exception.NotFoundExeption;
import com.micro_service_librerie.librerie.Model.User;
import com.micro_service_librerie.librerie.Repositry.UserRepositry;
import com.micro_service_librerie.librerie.Repositry.Specification.SearchCriteria;
import com.micro_service_librerie.librerie.Serie.SearchOperation;
import com.micro_service_librerie.librerie.Repositry.Specification.LibrerieSpecification;
import com.micro_service_librerie.librerie.Service.UserService;

@Service
@SuppressWarnings("")
public class UserDynamicService {
   
    @Autowired
    private UserRepositry userRepositry;

    @Autowired
    private UserService userService;
 
//dynamic search
public MappingJacksonValue searchByField(String key, String value) {    
    List<User> users =userRepositry.findAll(find(key, value));
          if (users.size()==0){throw new NotFoundExeption("User Not found with "+ key+ " : "+value);}
    return userService.filter(users) ;
  }

//dynamic delete 
        public String deleteByField(String key, String value)  {
            List<User> user = userRepositry.findAll(find(key, value));
            if (user.size()==0){{throw new NotFoundExeption("User Not found with "+ key+ " : "+value);}}
            userRepositry.deleteAll(userRepositry.findAll(find(key, value)));
            return "User deleted";
        }

//update user by field
        public String updateByField(String key, String value,  String email, String firstname, String lastname) {
            List<User> user = userRepositry.findAll(find(key, value));
            if (user.size()==0){{throw new NotFoundExeption("User Not found with "+ key+ " : "+value);}}
            if ((user.size()>1)&&(email!=null)){throw new IllegalArgumentException("we cant have more than one user with the same email");}
                for(int i = 0; i< user.size(); i++){
                    if (email != null) {user.get(i).setEmail(email); }
                    if (firstname != null) {user.get(i).setFirstname(firstname);} 
                    if (lastname != null) { user.get(i).setLastname(lastname);}
                    userRepositry.save(user.get(i));
                }
            return "user updated";  
        }

///select user by two fields
        public MappingJacksonValue searchByMultiplField(String key, String value,  
                                                        String key1, String value1) {
            Specification<User> specA;
            Specification<User> specB;
            List<User> UserEntity;
            
            if ((value != null) && (key != null)) { specA = find(key, value);}else {specA = null;}
            if ((value1 != null) && (key1 != null)) {specB = find(key1, value1);}else {specB = null;}
            if ((!specA.equals(null))|| (!specB.equals(null)))
                { UserEntity =  userRepositry.findAll(Specification.where(specA).and(specB));}
            else{
                UserEntity = null;
            }

            return userService.filter(UserEntity) ;	
        }

//Find a user
        private LibrerieSpecification<User> find (String key, String value){
        LibrerieSpecification<User> spec = new LibrerieSpecification<>();
        spec.add(new SearchCriteria(key, value, SearchOperation.EQUAL));
        return spec;
    }
}
