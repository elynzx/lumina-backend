package com.lumina.luminabackend.config;

import com.lumina.luminabackend.entity.user.Role;
import com.lumina.luminabackend.entity.user.User;
import com.lumina.luminabackend.repository.user.RoleRepository;
import com.lumina.luminabackend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitialAdminSetup implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        String adminEmail = "admin@lumina.com";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            Role adminRole = roleRepository.findByRoleName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));

            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("Principal")
                    .dni("00000000")
                    .phone("999999999")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin123"))
                    .role(adminRole)
                    .registrationDate(LocalDateTime.now())
                    .build();

            userRepository.save(admin);
            System.out.println("Usuario ADMIN creado: " + adminEmail + " / admin123");
        } else {
            System.out.println("El usuario ADMIN ya existe, no se creó otro.");
        }
    }
}
