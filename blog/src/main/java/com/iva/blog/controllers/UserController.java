package com.iva.blog.controllers;

import com.iva.blog.models.Article;
import com.iva.blog.models.User;
import com.iva.blog.services.ArticleService;
import com.iva.blog.services.UserService;
import com.iva.blog.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
public class UserController {
    private final UserService userService;
    private final ArticleService articleService;
    private final UserValidator userValidator;
    private final LocaleResolver localeResolver;

    @Autowired
    public UserController(UserService userService, ArticleService articleService,
                          UserValidator userValidator, LocaleResolver localeResolver) {
        this.userService = userService;
        this.articleService = articleService;
        this.userValidator = userValidator;
        this.localeResolver = localeResolver;
    }

    @GetMapping("/mainPage")
    public String mainPage(Model model, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        LocaleContextHolder.setLocale(locale);
        List<Article> articles = articleService.findAll();
        model.addAttribute("articles", articles);
        return "allUsers/mainPage";
    }


    @GetMapping("/user/{userId}")
    public String getUserPage(@PathVariable("userId") int userId, Model model,
                              Authentication authentication) {
        User user = userService.findOne(userId);
        model.addAttribute("user", user);

        boolean isOwner = userService.isOwner(userId, (UserDetails) authentication.getPrincipal());
        model.addAttribute("isOwner", isOwner);

        return "allUsers/userPage";
    }

    @GetMapping("/currentUserPage/update")
    public String showUpdateForm(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUsername = userDetails.getUsername();
            User currentUser = userService.findByUsername(currentUsername);
            if (currentUser != null) {
                model.addAttribute("user", currentUser);
                return "registeredUser/editUserData";
            }
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/currentUserPage")
    public String getCurrentUserPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUsername = userDetails.getUsername();
            User currentUser = userService.findByUsername(currentUsername);
            if (currentUser != null) {
                model.addAttribute("user", currentUser);
                return "registeredUser/authenticatedUserPage";
            }
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/currentUserPage/update")
    public String updateUser(@ModelAttribute("user") @Valid User updatedUser,
                             BindingResult bindingResult,
                             Authentication authentication) {
        userValidator.validate(updatedUser, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registeredUser/editUserData";
        }
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUsername = userDetails.getUsername();
            User currentUser = userService.findByUsername(currentUsername);
            if (currentUser != null) {
                currentUser.setEmail(updatedUser.getEmail());
                currentUser.setUsername(updatedUser.getUsername());
                currentUser.setPassword(updatedUser.getPassword());
                currentUser.setYearOfBirth(updatedUser.getYearOfBirth());
                userService.update(currentUser.getId(), currentUser);
                return "redirect:/currentUserPage";
            }
        }
        return "redirect:/auth/login";
    }
}




