package com.lumina.luminabackend.dto.auth;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String dni;
    private String roleName;
    private LocalDateTime registrationDate;

}