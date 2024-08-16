package org.manulv.parkingregistry.model;

import lombok.Data;
import org.manulv.parkingregistry.enums.VehicleTypeEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "entry-log")
public class Vehicle {

    @Id
    private String id;
    private String plaque;
    private Boolean applyDiscount;
    private VehicleTypeEnum type;
    private Short assignedPlace;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double totalToPay;


}
