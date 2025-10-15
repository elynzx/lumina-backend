package com.lumina.luminabackend.entity.reservation;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationFurnitureId implements Serializable {
    private Integer reservationId;
    private Integer furnitureId;
}