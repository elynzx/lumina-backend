package com.lumina.luminabackend.dto.district;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistrictDTO {
    private Integer districtId;
    private String districtName;
}