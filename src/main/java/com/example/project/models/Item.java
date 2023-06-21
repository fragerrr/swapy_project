package com.example.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    @NotBlank(message = "Please enter the name of item")
    @Size(min = 2, message = "Name should contain at least 2 characters")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "Please enter the description")
    @Size(min = 2, message = "Description should be more than 2")
    private String description;

    @Column(name = "price")
    @NotNull(message = "You should enter price of item")
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Client owner;
}
