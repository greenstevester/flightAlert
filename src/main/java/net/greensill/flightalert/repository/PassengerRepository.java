package net.greensill.flightalert.repository;

import net.greensill.flightalert.domain.FlightSlice;
import net.greensill.flightalert.domain.Passenger;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Passenger entity.
 */
public interface PassengerRepository extends JpaRepository<Passenger,Long> {

    @Query("select c FROM Passenger c where c.id=:id and c.userId =:userId")
    Passenger findOneByIdUserId( @Param("id") long id , @Param("userId") long userId );

    @Query("select c FROM Passenger c where c.userId =:userId")
    List <Passenger> findAllByUserId( @Param("userId") long userId );


}
