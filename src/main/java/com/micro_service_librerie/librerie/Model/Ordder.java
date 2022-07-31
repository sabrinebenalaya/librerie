package com.micro_service_librerie.librerie.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "Ordder")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ordder implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_Order")
    private Long id_Order;

    @Column(name="refOrder")
    @NotBlank(message = "please put the reference of the order")
    private String refOrder;

    @Column(name="statusOrder")
    @NotBlank(message = "please put the statut of the order")
    private String statusOrder;

    @Column(name="dateOrder")
    private Date dateOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idUser", nullable=false)
    @JsonIgnore
    private User user;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL
  )
    @JoinTable( name = "Order_Book",
                joinColumns = { @JoinColumn(name = "id_Order") },
                inverseJoinColumns = { @JoinColumn(name = "idBook") })
    @JsonIgnore
    private Set<Book> books = new HashSet<>();  
    
    public void addBook(Book book) {
        books.add(book);
    }
}
