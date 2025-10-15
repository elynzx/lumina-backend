package com.lumina.luminabackend.dto.auth;
import lombok.*;

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
    private String roleName;

}