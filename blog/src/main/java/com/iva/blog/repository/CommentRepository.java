package com.iva.blog.repository;

import com.iva.blog.models.Article;
import com.iva.blog.models.Commentary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Commentary,Integer> {
    List<Commentary> findByArticleId(int articleId);
    List<Commentary> findAllByArticle(Article article);

}
