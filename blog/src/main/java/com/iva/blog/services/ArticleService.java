package com.iva.blog.services;

import com.iva.blog.models.Article;
import com.iva.blog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findOne(int id) {
        Optional<Article> foundBook = articleRepository.findById(id);
        return foundBook.orElse(null);
    }

    public Article findByName(int id) {
        Optional<Article> foundBook = articleRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Article article) {
        articleRepository.save(article);
    }

    @Transactional
    public void updateArticle(int articleId, Article updatedArticle) {
        Optional<Article> existingArticleOptional = articleRepository.findById(articleId);
        if (existingArticleOptional.isPresent()) {
            Article existingArticle = existingArticleOptional.get();
            existingArticle.setTitle(updatedArticle.getTitle());
            existingArticle.setContent(updatedArticle.getContent());
            articleRepository.save(existingArticle);
        } else {
            throw new IllegalArgumentException("Article with id " + articleId + " not found");
        }
    }


    @Transactional
    public void delete(int id) {
        articleRepository.deleteById(id);
    }

    public List<Article> findAllByUserId(int userId) {
        return articleRepository.findAllByUserId(userId);
    }
}
