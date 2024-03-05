package com.iva.blog.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Название не может быть пустым!")
    @Column(name = "title")
    private String title;

    @Column(name = "content")
    @NotEmpty(message = "Текст не может быть пустым!")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "publish_time")
    private LocalDateTime publishTime;

    @OneToMany
    private List<Commentary> commentaries;
}
