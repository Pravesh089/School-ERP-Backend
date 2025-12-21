package com.school.config;

import com.school.model.Role;
import com.school.model.User;
import com.school.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Data Seeder to initialize the database with default data.
 * This runs automatically on application startup.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Checks if the default admin user exists, and creates it if missing.
     */
    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password")); // Default password
            admin.setRole(Role.ROLE_ADMIN);
            admin.setActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            
            userRepository.save(admin);
            System.out.println("Admin user created successfully: username=admin, password=password");
        }
    }
}
