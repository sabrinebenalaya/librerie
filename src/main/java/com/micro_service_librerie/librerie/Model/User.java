package com.micro_service_librerie.librerie.Model;

import java.io.Serializable;
import java.util.HashSet;
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

    @NotBlank
    @Column(name = "firstname", length = 40)
    private String firstname;

    @NotBlank(message = "veuillez saisir le nom du user")
    @Column(name = "lastname", length = 40)
    private String lastname;
    
    @NotBlank(message = "veuillez saisir l'email du user")
    @Column(name = "email", unique=true)
    @Email
    private String email;

    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable( name = "User_Livre",
                joinColumns = { @JoinColumn(name = "idUser") },
                inverseJoinColumns = { @JoinColumn(name = "idLivre") })
    private Set<Livre> livres = new HashSet<>();

    public void addLivre(Livre livre) {
        livres.add(livre);
    }
    
}
