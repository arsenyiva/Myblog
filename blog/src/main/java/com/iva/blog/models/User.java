package com.iva.blog.models;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Email(message = "Email should be valid")
    @Column(name = "email", unique = true)
    @NotNull
    private String email;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "verification_сode")
    private String verificationCode;

    @Size(min = 2, max = 100, message = "Длина должна быть больше 2х и меньше 100 символов")
    @Column(name = "name", unique = true)
    private String username;

    @Min(value = 1900, message = "Год должен быть больше 1900")
    @Max(value = 2023, message = "Год должен быть меньше 2023")
    @Column(name = "year_of_birth")
    private int yearOfBirth;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "user")
    private List<Article> articles;

    @OneToMany(mappedBy = "user")
    private List<Commentary> commentaries;

    @Column(name = "Banned")
    private Boolean banned;

}
