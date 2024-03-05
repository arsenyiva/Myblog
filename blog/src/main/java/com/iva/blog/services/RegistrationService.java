package com.iva.blog.services;

import com.iva.blog.models.User;
import com.iva.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public RegistrationService(UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void register(User user) {
        String verificationCode = generateVerificationCode();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setBanned(false);
        user.setVerified(false);
        user.setVerificationCode(verificationCode);
        String subject = "Подтверждение регистрации";
        String text = "Для завершения регистрации введите следующий код: " + verificationCode;
        emailService.sendEmail(user.getEmail(), subject, text);
        userRepository.save(user);
    }

    @Transactional
    public void sendMessageForResetPassword(User user) {
        String verificationCode = generateVerificationCode();
        user.setVerificationCode(verificationCode);
        String subject = "Сброс пароля";
        String text = "Для сброса пароля введите следующий код: " + verificationCode;
        emailService.sendEmail(user.getEmail(), subject, text);
        userRepository.save(user);
    }

    @Transactional
    public void setNewPassword(int id, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVerificationCode(null);
        userRepository.save(user);
    }


    @Transactional
    public void verify(User user) {
        user.setVerified(true);
        user.setVerificationCode(null);
        userRepository.save(user);
    }

    private String generateVerificationCode() {
        return UUID.randomUUID().toString();
    }

    public User getUserById(int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
    }

    public User getByEmail(String emailFromForm) {
        Optional<User> userOptional = userRepository.findByEmail(emailFromForm);
        return userOptional.orElse(null);
    }
}
