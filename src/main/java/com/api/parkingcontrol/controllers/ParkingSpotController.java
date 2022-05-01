package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dto.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    final ParkingSpotService parkingSpotService;

    /**
     * Constructor with dependency inject service
     *
     * @param parkingSpotService
     */
    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto dto) {

        /** Verify if exists number in parking spot */
        if (this.parkingSpotService.existsByNumber(dto.getNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is Already in Use!");
        }

        /** Verify if exists license plate */
        if (this.parkingSpotService.existsByLicensePlateCar(dto.getLicensePlateCar())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is Already in Parking Spot!");
        }

        /** Verify if exists apartment and block registered */
        if (this.parkingSpotService.existsByApartmentAndBlock(dto.getApartment(), dto.getBlock())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Apartment and Block is Already in Parking Spot!!");
        }

        var model = new ParkingSpotModel();
        // Convert dto in model
        BeanUtils.copyProperties(dto, model);
        model.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(this.parkingSpotService.save(model));
    }

    @GetMapping
    public ResponseEntity<List<ParkingSpotModel>> getParkingSpotModelList() {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkingSpotService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getParkingSpot(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpotModel> optionalParkingSpotModel = this.parkingSpotService.findById(id);
        if (optionalParkingSpotModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalParkingSpotModel.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpotModel> optionalParkingSpotModel = this.parkingSpotService.findById(id);
        if (optionalParkingSpotModel.isPresent()) {
            this.parkingSpotService.delete(optionalParkingSpotModel.get());
            return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id, @RequestBody @Valid ParkingSpotDto dto) {
        Optional<ParkingSpotModel> optionalParkingSpotModel = this.parkingSpotService.findById(id);
        if (optionalParkingSpotModel.isPresent()) {
            var parkingSpotModel = optionalParkingSpotModel.get();
            parkingSpotModel.setLicensePlateCar(dto.getLicensePlateCar());
            parkingSpotModel.setNumber(dto.getNumber());
            parkingSpotModel.setModelCar(dto.getModelCar());
            parkingSpotModel.setBrandCar(dto.getBrandCar());
            parkingSpotModel.setColorCar(dto.getColorCar());
            parkingSpotModel.setResponsibleName(dto.getResponsibleName());
            parkingSpotModel.setApartment(dto.getApartment());
            parkingSpotModel.setBlock(dto.getBlock());

            return ResponseEntity.status(HttpStatus.OK).body(this.parkingSpotService.save(parkingSpotModel));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found!");
    }
}
