package com.micro_service_librerie.librerie.Model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "livre")
@JsonFilter("livreFilter")
public class Livre implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idLivre")
    private Long idLivre;

    @NotBlank
    @Size(min = 3)
    @Column(name = "titre", unique=true, length = 100)
    private String titre;

   
    @NotBlank
    @Size(min = 3)
    @Column(name = "auteur",  length = 50)
    private String auteur;

    @NotBlank
    @Size(min = 3)
    @Column(name = "genre",  length = 50)
    private String genre;

    @Min(50)
    @Column(name = "nbrpage")
    private Integer nbrpage;

    @ManyToMany(fetch = FetchType.LAZY,
                cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
                },
                mappedBy = "livres")
    @JsonIgnore
    @EqualsAndHashCode.Exclude 
    @ToString.Exclude 
    private Set<User> users = new HashSet<>();
}
