package com.api.parkingcontrol.repositories;

import com.api.parkingcontrol.models.ParkingSpotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IParkingSpotRepository  extends JpaRepository<ParkingSpotModel, UUID> {

    boolean existsByLicensePlateCar(String licensePlateCar);

    @Query("SELECT COUNT(ps.id) FROM ParkingSpotModel ps WHERE ps.licensePlateCar = :licensePlateCar AND ps.id <> :id")
    Long countByLicensePlateCar(@Param("id") UUID id, @Param("licensePlateCar") String licensePlateCar);

    boolean existsByNumber(String number);

    @Query("SELECT COUNT(ps.id) FROM ParkingSpotModel ps WHERE ps.number = :number AND ps.id <> :id")
    Long countByNumber(@Param("id") UUID id, @Param("number") String number);
    
    boolean existsByApartmentAndBlock(String apartment, String block);

    @Query("SELECT COUNT(ps.id) FROM ParkingSpotModel ps WHERE ps.apartment = :apartment AND ps.block = :block AND ps.id <> :id")
    Long countByApartmentAndBlock(@Param("id") UUID id, @Param("apartment") String apartment, @Param("block") String block);
}
