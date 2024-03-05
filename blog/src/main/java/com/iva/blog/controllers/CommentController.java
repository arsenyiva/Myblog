package com.iva.blog.controllers;

import com.iva.blog.models.Article;
import com.iva.blog.models.Commentary;
import com.iva.blog.models.User;
import com.iva.blog.services.ArticleService;
import com.iva.blog.services.CommentService;
import com.iva.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;


@Controller
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final ArticleService articleService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService, ArticleService articleService) {
        this.commentService = commentService;
        this.userService = userService;
        this.articleService = articleService;
    }

    @GetMapping("/user/article/{articleId}/comment")
    public String showAddCommentForm(@PathVariable("articleId") int articleId,
                                     Model model, Principal principal) {
        model.addAttribute("articleId", articleId);
        String username = principal.getName();
        User currentUser = userService.findByUsername(username);
        if (currentUser != null) {
            model.addAttribute("userId", currentUser.getId());
        }
        model.addAttribute("comment", new Commentary());
        return "registeredUser/addComment";
    }

    @PostMapping("/user/article/{articleId}/comment")
    public String addComment(@PathVariable int articleId,
                             @ModelAttribute Commentary commentary,
                             Principal principal) {
        String username = principal.getName();
        User currentUser = userService.findByUsername(username);
        if (currentUser == null) {
            return "redirect:/login";
        }
        Article article = articleService.findOne(articleId);
        commentary.setArticle(article);
        commentary.setUser(currentUser);
        commentary.setPublishTime(LocalDateTime.now());
        commentService.save(commentary);
        return "redirect:/user/" + currentUser.getId() + "/article/" + articleId;
    }
}