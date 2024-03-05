package com.iva.blog.controllers;

import com.iva.blog.models.Article;
import com.iva.blog.models.Commentary;
import com.iva.blog.models.User;
import com.iva.blog.services.ArticleService;
import com.iva.blog.services.CommentService;
import com.iva.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;
    private final CommentService commentService;

    @Autowired
    public ArticleController(ArticleService articleService,
                             UserService userService,
                             CommentService commentService) {
        this.articleService = articleService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/currentUserPage/add")
    public String showAddArticleForm(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUsername = userDetails.getUsername();
            User currentUser = userService.findByUsername(currentUsername);
            if (currentUser != null) {
                model.addAttribute("user", currentUser);
                model.addAttribute("article", new Article());
                return "registeredUser/addArticle";
            }
        }
        return "redirect:/auth/login";
    }


    @PostMapping("/currentUserPage/add")
    public String addArticle(@ModelAttribute Article article, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUsername = userDetails.getUsername();
            User currentUser = userService.findByUsername(currentUsername);
            if (currentUser != null) {
                article.setUser(currentUser);
                article.setPublishTime(LocalDateTime.now());
                articleService.save(article);
                return "redirect:/currentUserPage";
            }
        }
        return "redirect:/auth/login";
    }

    @GetMapping("user/{userId}/article/{articleId}")
    public String showArticle(@PathVariable("articleId") int id,
                              @PathVariable("userId") int userId,
                              Model model, Principal principal) {
        List<Commentary> comments = commentService.findByArticleId(id);
        Article article = articleService.findOne(id);
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        if (principal != null) {
            String currentUsername = principal.getName();
            if (currentUsername.equals(article.getUser().getUsername())) {
                model.addAttribute("isOwner", true);
            }
        }
        return "allUsers/articlePage";
    }


    @GetMapping("user/{userId}/article/{articleId}/update")
    public String showArticleUpdateForm(@PathVariable("articleId") int articleId,
                                        @PathVariable("userId") int userId,
                                        Model model,
                                        Principal principal) {
        String currentUsername = principal.getName();
        Article article = articleService.findOne(articleId);
        if (article != null && article.getUser().getId() == userId && article.getUser().getUsername().equals(currentUsername)) {
            model.addAttribute("article", article);
            return "registeredUser/editArticle";
        } else {
            return "redirect:/mainPage";
        }
    }

    @PostMapping("/article/{articleId}/update")
    public String updateArticle(@PathVariable("articleId") int articleId,
                                @ModelAttribute Article updatedArticle) {
        articleService.updateArticle(articleId, updatedArticle);
        return "redirect:/currentUserPage";
    }
}

