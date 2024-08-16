package org.manulv.parkingregistry.controller;

import org.manulv.parkingregistry.dto.RequestVehicleDto;
import org.manulv.parkingregistry.model.Vehicle;
import org.manulv.parkingregistry.services.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/parking")
public class RegistryController {

    private final RegistryService service;

    public RegistryController(RegistryService service) {
        this.service = service;
    }

    @PostMapping(value = "/registry")
    public ResponseEntity<Vehicle> registerVehicle(@RequestBody RequestVehicleDto vehicle) {
        return ResponseEntity.ok(service.publishVehicle(vehicle));
    }

    @GetMapping(value = "/get-vehicles")
    public ResponseEntity<List<Vehicle>> getVehicles() {
        return ResponseEntity.ok(service.getVehicleList());
    }
    @GetMapping(value = "/earnings")
    public ResponseEntity<Double> getEarnings() {
        return ResponseEntity.ok(service.getEarnings());
    }
    @PutMapping(value = "/check-out/{id}")
    public ResponseEntity<Vehicle> checkOutVehicle(@PathVariable String id) {
        return ResponseEntity.ok(service.checkOutVehicle(id));
    }

    @PutMapping(value = "/update-vehicle/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable String id, @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(service.updateVehicle(id, vehicle));
    }

    public ResponseEntity<Boolean> deleteVehicle(@PathVariable String id) {
        return ResponseEntity.ok(service.deleteVehicle(id));
    }

}
