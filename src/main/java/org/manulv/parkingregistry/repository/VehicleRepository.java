package org.manulv.parkingregistry.repository;

import org.manulv.parkingregistry.enums.VehicleTypeEnum;
import org.manulv.parkingregistry.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {

    @Query("{ startDate: { $gte: ?0, $lte: ?1 } }")
    List<Vehicle> getVehiclesInsertedToday(LocalDateTime startOfDay, LocalDateTime endOfDay);


    int getQuantityByType(VehicleTypeEnum type);

}
