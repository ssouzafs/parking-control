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

    /**
     * Verify if exists license Plates Car
     * @param licensePlateCar
     * @return
     */
    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return this.parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
    }

    /**
     * check if the license plate already exists in other records, based on the id informed.
     * @param id
     * @param licensePlateCar
     * @return
     */
    public boolean existsByLicensePlateCar(UUID id, String licensePlateCar) {
        return this.parkingSpotRepository.countByLicensePlateCar(id, licensePlateCar) > 0;
    }

    public boolean existsByNumber(String number) {
        return this.parkingSpotRepository.existsByNumber(number);
    }

    /**
     * check if the number already exists in other records, based on the id informed.
     * @param id
     * @param number
     * @return
     */
    public boolean existsByNumber(UUID id, String number) {
        return this.parkingSpotRepository.countByNumber(id, number) > 0;
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return this.parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
    }

    /**
     * check if the apartment and block already exists in other records, based on the id informed.
     * @param id
     * @param apartment
     * @param block
     * @return
     */
    public boolean existsByApartmentAndBlock(UUID id, String apartment, String block) {
        return this.parkingSpotRepository.countByApartmentAndBlock(id, apartment, block) > 0;
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
