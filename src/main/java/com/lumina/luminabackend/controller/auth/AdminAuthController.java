package com.lumina.luminabackend.controller.auth;

import com.lumina.luminabackend.dto.auth.AdminLoginRequestDTO;
import com.lumina.luminabackend.dto.auth.AuthResponseDTO;
import com.lumina.luminabackend.dto.common.ApiResponseDTO;
import com.lumina.luminabackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> adminLogin(
            @Valid @RequestBody AdminLoginRequestDTO request) {
        AuthResponseDTO response = authService.adminLogin(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Acceso administrativo permitido", response));
    }
}