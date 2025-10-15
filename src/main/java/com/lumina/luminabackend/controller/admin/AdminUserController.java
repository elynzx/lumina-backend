package com.lumina.luminabackend.controller.admin;

import com.lumina.luminabackend.dto.auth.UserProfileDTO;
import com.lumina.luminabackend.dto.common.ApiResponseDTO;
import com.lumina.luminabackend.dto.user.CreateAdminDTO;
import com.lumina.luminabackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @PostMapping("/admins")
    public ResponseEntity<ApiResponseDTO<UserProfileDTO>> createAdmin(
            @Valid @RequestBody CreateAdminDTO createDTO) {
        UserProfileDTO newAdmin = userService.createAdmin(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Administrador creado exitosamente", newAdmin));
    }

    @GetMapping("/clients")
    public ResponseEntity<ApiResponseDTO<List<UserProfileDTO>>> getAllClients() {
        List<UserProfileDTO> clients = userService.getAllClients();
        return ResponseEntity.ok(ApiResponseDTO.success("Lista de clientes obtenida", clients));
    }

    @GetMapping("/clients/search")
    public ResponseEntity<ApiResponseDTO<List<UserProfileDTO>>> searchClients(
            @RequestParam String keyword) {
        List<UserProfileDTO> clients = userService.searchClients(keyword);
        return ResponseEntity.ok(ApiResponseDTO.success("Búsqueda de clientes completada", clients));
    }
}