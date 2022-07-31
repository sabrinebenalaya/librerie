package com.micro_service_librerie.librerie.Model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFilter;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Table(name="user")
@JsonFilter("userfilter")
public class User  implements Serializable{
   
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idUser")
    private Long idUser;

    @NotBlank(message ="Please enter your firstname")
    @Column(name = "firstname", length = 40)
    private String firstname;

    @NotBlank(message = "Please enter your lastname")
    @Column(name = "lastname", length = 40)
    private String lastname;
    
    @NotBlank(message = "Please enter your email")
    @Column(name = "email", unique=true)
    @Email
    private String email;

    @NotBlank(message = "Please enter your username")
    @Column(name = "username", length = 40)
    private String username;

    @NotBlank(message = "Please enter your password")
    @Column(name = "password", length = 40)
    private String password;


    @OneToMany(mappedBy="user", 
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL)
    private Set<Ordder> orders;
  
    
}
