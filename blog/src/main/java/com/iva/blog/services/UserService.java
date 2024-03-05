package com.iva.blog.services;

import com.iva.blog.models.User;
import com.iva.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    public final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findOne(int id) {
        Optional<User> foundPerson = userRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(User person) {
        userRepository.save(person);
    }

    public User findByUsername(String username) {
        Optional<User> foundPerson = userRepository.findByUsername(username);
        return foundPerson.orElse(null);
    }

    public User findById(int userId) {
        Optional<User> foundPerson = userRepository.findById(userId);
        return foundPerson.orElse(null);
    }

    public boolean isOwner(int userId, UserDetails userDetails) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getUsername().equals(userDetails.getUsername());
        }
        return false;
    }

    @Transactional
    public void update(int id, User updatedUser) {
        updatedUser.setId(id);
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
            updatedUser.setPassword(encodedPassword);
        }
        userRepository.save(updatedUser);
    }

    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
