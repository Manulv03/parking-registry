package org.manulv.parkingregistry.services;

import org.manulv.parkingregistry.dto.RequestVehicleDto;
import org.manulv.parkingregistry.enums.VehicleTypeEnum;
import org.manulv.parkingregistry.model.Vehicle;
import org.manulv.parkingregistry.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Service
public class RegistryService {
    Logger logger = Logger.getLogger(RegistryService.class.getName());
    private final VehicleRepository vehicleRepository;

    RegistryService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    private static final int MOTORCYCLE_COST = 62;
    private static final int LIGHT_VEHICLE = 120;
    private static final double DISCOUNT = 0.25;

    public Vehicle publishVehicle(RequestVehicleDto requestVehicleDto) {
        var vehicle = new Vehicle();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        vehicle.setPlaque(requestVehicleDto.getPlaque().toUpperCase());
        vehicle.setType(requestVehicleDto.getType());
        vehicle.setApplyDiscount(requestVehicleDto.getApplyDiscount());

        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        List<Vehicle> vehicleList = vehicleRepository.getVehiclesInsertedToday(startOfDay.minusHours(5), endOfDay.minusHours(5));
        int max = 0;
        if (requestVehicleDto.getType() == VehicleTypeEnum.LIGHT) {
            max = 5;
        }
        if (requestVehicleDto.getType() == VehicleTypeEnum.MOTORCYCLE) {
            max = 6;
        }
        vehicle.setAssignedPlace((short) 1);
        vehicle.setStartDate(LocalDateTime.now().minusHours(5));
        vehicle.setEndDate(null);

        return vehicleRepository.save(vehicle);
    }

    public Vehicle checkOutVehicle(String id) {
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        Vehicle vehicle = vehicleList.stream().filter(v -> v.getId().equals(id)).findFirst().orElse(null);
        if (vehicle == null) {
            throw new RuntimeException("Vehicle not found");
        }
        vehicle.setEndDate(LocalDateTime.now());
        vehicle.setTotalToPay(calculateCost(vehicle));
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getVehicleList() {
        try {
            return vehicleRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error can't get vehicle list");
        }
    }

    public Vehicle updateVehicle(String id, Vehicle vehicle) {
        try {
            return vehicleRepository.findById(id).map(v -> {
                v.setPlaque(vehicle.getPlaque());
                v.setType(vehicle.getType());
                v.setApplyDiscount(vehicle.getApplyDiscount());
                v.setStartDate(vehicle.getStartDate());
                v.setEndDate(vehicle.getEndDate());
                v.setTotalToPay(vehicle.getTotalToPay());
                return vehicleRepository.save(v);
            }).orElseThrow(() -> new RuntimeException("Vehicle not found"));

        } catch (Exception e) {
            logger.info("Error can't update vehicle" + e.getMessage());
            throw new RuntimeException("Error can't update vehicle");
        }

    }
    public Double getEarnings() {
        try {
            return calculateEarnings();
        } catch (Exception e) {
            logger.info("Error can't get earnings" + e.getMessage());
            throw new RuntimeException("Error can't get earnings");
        }

    }
    public boolean deleteVehicle(String id) {
        try {
            vehicleRepository.deleteById(id);
            logger.fine("Vehicle deleted successfully");
            return true;
        } catch (Exception e) {
            logger.info("Error can't delete vehicle" + e.getMessage());
            return false;
        }
    }

    private Double calculateEarnings() {
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        return vehicleList.stream().mapToDouble(Vehicle::getTotalToPay).sum();
    }

    public Double calculateCost(Vehicle vehicle) {
        Duration duration = Duration.between(vehicle.getStartDate(), vehicle.getEndDate());
        long durationHours = duration.toHours();
        double costByHour = vehicle.getType().equals(VehicleTypeEnum.MOTORCYCLE) ? MOTORCYCLE_COST : LIGHT_VEHICLE;
        return durationHours * costByHour;
    }


}
