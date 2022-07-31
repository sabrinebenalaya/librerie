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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
@JsonFilter("BookFilter")
public class Book implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idBook")
    private Long idBook;

    @Size(min = 3)
    @NotBlank(message = "Please enter the title of the book")
    @Column(name = "title", unique=true, length = 100)
    private String title;

   
    @NotBlank
    @Size(min = 3)
    @NotBlank(message = "Please enter the author of the book")
    @Column(name = "author",  length = 50)
    private String author;

    @NotBlank(message = "Please enter the gender of the book")
    @Size(min = 3)
    @Column(name = "gender",  length = 50)
    private String gender;

    @Min(50)
    @Column(name = "quantity")
    private Integer quantity;

    @Min(10)
    @NotNull
    @Column(name = "price")
    private Integer price;

    @ManyToMany(fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
                mappedBy = "books")
    @JsonIgnore
    @EqualsAndHashCode.Exclude 
    @ToString.Exclude 
    private Set<Ordder> order = new HashSet<>();
}
