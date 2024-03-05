package com.iva.blog.repository;

import com.iva.blog.models.Article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findByUserId(int userId);

    List<Article> findAllByUserId(int userId);
}
