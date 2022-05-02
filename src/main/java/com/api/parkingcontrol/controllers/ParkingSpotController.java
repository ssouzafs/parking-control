package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dto.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    final ParkingSpotService service;

    /**
     * Constructor with dependency inject service
     *
     * @param service
     */
    public ParkingSpotController(ParkingSpotService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto dto) {

        /** Verify if exists number in parking spot */
        if (this.service.existsByNumber(dto.getNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is Already in Use!");
        }

        /** Verify if exists license plate */
        if (this.service.existsByLicensePlateCar(dto.getLicensePlateCar())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is Already in Parking Spot!");
        }

        /** Verify if exists apartment and block registered */
        if (this.service.existsByApartmentAndBlock(dto.getApartment(), dto.getBlock())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Apartment of this block is already in use!!");
        }

        var model = new ParkingSpotModel();
        // Convert dto in model
        BeanUtils.copyProperties(dto, model);
        model.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(model));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotModel>> getParkingSpotModelList(@PageableDefault(page = 0, size = 10,
            sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getParkingSpot(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpotModel> optionalParkingSpotModel = this.service.findById(id);
        if (optionalParkingSpotModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalParkingSpotModel.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpotModel> optionalParkingSpotModel = this.service.findById(id);
        if (optionalParkingSpotModel.isPresent()) {
            this.service.delete(optionalParkingSpotModel.get());
            return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id, @RequestBody @Valid ParkingSpotDto dto) {
        Optional<ParkingSpotModel> optionalParkingSpotModel = this.service.findById(id);

        if (optionalParkingSpotModel.isPresent()) {
            var parkingSpotModel = optionalParkingSpotModel.get();

//            Convert data dto in model
            BeanUtils.copyProperties(dto, parkingSpotModel);

            /** Verify if exists number in parking spot */
            if (this.service.existsByNumber(parkingSpotModel.getId(), parkingSpotModel.getNumber())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is Already in Use!");
            }

            /** Verify if exists license plate */
            if (this.service.existsByLicensePlateCar(parkingSpotModel.getId(), parkingSpotModel.getLicensePlateCar())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is Already in Parking Spot!");
            }

            /** Verify if exists apartment and block registered */
            if (this.service.existsByApartmentAndBlock(parkingSpotModel.getId(), parkingSpotModel.getApartment(), parkingSpotModel.getBlock())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Apartment of this block is already in use!!");
            }

            /** Save if verification is ok */
            return ResponseEntity.status(HttpStatus.OK).body(this.service.save(parkingSpotModel));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found!");
    }
}
