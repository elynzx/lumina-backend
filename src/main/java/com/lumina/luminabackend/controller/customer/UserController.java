package com.lumina.luminabackend.controller.customer;


import com.lumina.luminabackend.dto.auth.UserProfileDTO;
import com.lumina.luminabackend.dto.common.ApiResponseDTO;
import com.lumina.luminabackend.dto.user.ChangePasswordDTO;
import com.lumina.luminabackend.dto.user.UpdateUserProfileDTO;
import com.lumina.luminabackend.security.CustomUserPrincipal;
import com.lumina.luminabackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<UserProfileDTO>> getMyProfile(
            @AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        UserProfileDTO profile = userService.getUserProfile(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponseDTO.success("Perfil obtenido correctamente", profile));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponseDTO<UserProfileDTO>> updateMyProfile(
            @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
            @Valid @RequestBody UpdateUserProfileDTO updateDTO) {
        UserProfileDTO updatedProfile = userService.updateUserProfile(
                userPrincipal.getId(),
                updateDTO
        );
        return ResponseEntity.ok(ApiResponseDTO.success("Perfil actualizado correctamente", updatedProfile));
    }

    @PutMapping("/me/change-password")
    public ResponseEntity<ApiResponseDTO<Void>> changePassword(
            @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(userPrincipal.getId(), changePasswordDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Contraseña actualizada correctamente", null));
    }
}