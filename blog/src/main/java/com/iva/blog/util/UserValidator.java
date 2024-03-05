package com.iva.blog.util;

import com.iva.blog.models.User;
import com.iva.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        // Проверка уникальности имени пользователя
        User existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (existingUser != null) {
            errors.rejectValue("username", "", "Имя уже занято!");
        }

        checkPasswordLength(user.getPassword(), errors);
    }

    private void checkPasswordLength(String password, Errors errors) {
        int minLength = 6;
        int maxLength = 25;
        if (password.length() < minLength || password.length() > maxLength) {
            errors.rejectValue("password", "", "Длина пароля должна быть от " + minLength + " до " + maxLength + " символов.");
        }
    }
}

