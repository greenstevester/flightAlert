package net.greensill.flightalert.repository;

import net.greensill.flightalert.domain.FlightRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the FlightRequest entity.
 */
public interface FlightRequestRepository extends JpaRepository<FlightRequest,Long> {

    @Query("select c FROM FlightRequest c where c.id=:id and c.userId =:userId")
    FlightRequest findOneByIdUserId( @Param("id") long id , @Param("userId") long userId );

    @Query("select c FROM FlightRequest c where c.userId =:userId")
    List <FlightRequest> findAllByUserId( @Param("userId") long userId );

    @Query("select c FROM FlightRequest c where c.userId =:userId")
    Page<FlightRequest> findAllByUserId( @Param("userId") long userId, Pageable pageable );


}
