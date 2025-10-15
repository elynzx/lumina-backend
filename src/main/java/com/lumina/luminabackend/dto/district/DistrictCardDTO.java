package com.lumina.luminabackend.dto.district;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictCardDTO {
    private Integer districtId;
    private String districtName;
    private String photoUrl;
    private Integer venueCount;
}