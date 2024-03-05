package com.iva.blog.services;

import com.iva.blog.models.Article;
import com.iva.blog.models.Commentary;
import com.iva.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void save(Commentary commentary) {
        commentRepository.save(commentary);
    }

    public List<Commentary> findByArticleId(int articleId) {
        return commentRepository.findByArticleId(articleId);
    }

    public List<Commentary> findAllByArticle(Article article) {
        return commentRepository.findAllByArticle(article);
    }
}