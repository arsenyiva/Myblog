package com.iva.blog.controllers;

import com.iva.blog.models.User;
import com.iva.blog.services.RegistrationService;
import com.iva.blog.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserValidator userValidator;
    private final RegistrationService registrationService;


    @Autowired
    public AuthController(UserValidator userValidator, RegistrationService registrationService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult,
                                      Model model) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        registrationService.register(user);
        int id = user.getId();
        model.addAttribute("id", id);
        return "redirect:/auth/verify-registration/" + id;
    }

    @GetMapping("/verify-registration/{userId}")
    public String verifyRegistrationPage(@PathVariable int userId, Model model) {
        model.addAttribute("userId", userId);
        return "auth/verify-registration";
    }

    @PostMapping("/verify-registration/{userId}")
    public String verifyRegistration(@PathVariable int userId,
                                     @RequestParam("verificationCode")
                                     String verificationCodeFromForm) {
        User user = registrationService.getUserById(userId);
        if (verificationCodeFromForm.equals(user.getVerificationCode())) {
            registrationService.verify(user);
            return "redirect:/auth/login";
        }
        return "redirect:/auth/registration";
    }

    @GetMapping("/forget-password")
    public String forgetPasswordPage() {
        return "auth/forget-password";
    }

    @PostMapping("/forget-password")
    public String forgetPassword(@RequestParam("email") String emailFromForm,
                                 RedirectAttributes redirectAttributes) {
        User user = registrationService.getByEmail(emailFromForm);
        if (user == null) {
            redirectAttributes.addAttribute("error", "true");
            return "redirect:/auth/forget-password";
        }
        registrationService.sendMessageForResetPassword(user);
        return "redirect:/auth/new-password/" + user.getId();
    }

    @GetMapping("/new-password/{userId}")
    public String newPasswordPage(@PathVariable int userId, Model model) {
        model.addAttribute("userId", userId);
        return "auth/new-password";
    }

    @PostMapping("/new-password/{userId}")
    public String setNewPassword(@PathVariable int userId,
                                 @RequestParam("verificationCode") String verificationCodeFromForm,
                                 @RequestParam("password") String newPassword,
                                 RedirectAttributes redirectAttributes) {
        User user = registrationService.getUserById(userId);
        boolean verificationCodeValid = user.getVerificationCode().equals(verificationCodeFromForm);
        boolean newPasswordLengthValid = newPassword.length() >= 5 && newPassword.length() <= 25;

        if (!verificationCodeValid) {
            redirectAttributes.addAttribute("verificationCodeError", "true");
        }
        if (!newPasswordLengthValid) {
            redirectAttributes.addAttribute("passwordLengthError", "true");
        }
        if (!verificationCodeValid || !newPasswordLengthValid) {
            return "redirect:/auth/new-password/" + user.getId();
        }

        registrationService.setNewPassword(userId, newPassword);
        return "auth/login";
    }

}