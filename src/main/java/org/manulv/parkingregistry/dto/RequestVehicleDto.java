package org.manulv.parkingregistry.dto;

import lombok.Data;
import org.manulv.parkingregistry.enums.VehicleTypeEnum;

@Data
public class RequestVehicleDto {

    private String plaque;
    private VehicleTypeEnum type;
    private Boolean applyDiscount;

}
