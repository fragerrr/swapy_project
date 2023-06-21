package com.example.project.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @OneToOne(mappedBy = "user")
    private Client client;

    @OneToOne(mappedBy = "user")
    private Admin admin;

    public Users(Integer id, String password, String role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }



    @PreRemove
    private void removeDoctor(){
        if(this.admin != null){
            this.admin.setUser(null);
        }
        if(this.client != null){
            this.client.setUser(null);
        }


    }
}
