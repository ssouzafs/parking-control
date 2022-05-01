package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.IParkingSpotRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService {

    final IParkingSpotRepository parkingSpotRepository;

    /**
     * Contructor of dependency repository
     *
     * @param parkingSpotRepository
     */
    public ParkingSpotService(IParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    public ParkingSpotModel save(ParkingSpotModel model) {
        return this.parkingSpotRepository.save(model);
    }

    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return this.parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
    }

    public boolean existsByNumber(String number) {
        return this.parkingSpotRepository.existsByNumber(number);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return this.parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
    }

    public List<ParkingSpotModel> findAll() {
        return this.parkingSpotRepository.findAll();
    }

    public Optional<ParkingSpotModel> findById(UUID id) {
        return this.parkingSpotRepository.findById(id);
    }

    @Transactional
    public void delete(ParkingSpotModel parkingSpotModel) {
        this.parkingSpotRepository.deleteById(parkingSpotModel.getId());
    }
}
