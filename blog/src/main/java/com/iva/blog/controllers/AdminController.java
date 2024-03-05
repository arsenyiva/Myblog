package com.iva.blog.controllers;

import com.iva.blog.models.User;
import com.iva.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String adminPage(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/adminPage";
    }

    @PostMapping("/changeRole")
    public String changeUserRole(@RequestParam("role") String role,
                                 @RequestParam("userId") int userId) {
        User existingUser = userService.findById(userId);
        if (existingUser != null) {
            existingUser.setRole(role);
            userService.save(existingUser);
        }
        return "redirect:/admin";
    }

    @PostMapping("/banOrUnbanUser")
    public String banOrUnbanUser(@RequestParam("Banned") Boolean isBanned,
                                 @RequestParam("userId") int userId) {
        User existingUser = userService.findById(userId);
        if (existingUser != null) {
            existingUser.setBanned(isBanned);
            userService.save(existingUser);
        }
        return "redirect:/admin";
    }
}
