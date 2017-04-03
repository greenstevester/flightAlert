package net.greensill.flightalert.repository;

import net.greensill.flightalert.domain.FlightSlice;
import net.greensill.flightalert.domain.PermittedCarrier;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PermittedCarrier entity.
 */
public interface PermittedCarrierRepository extends JpaRepository<PermittedCarrier,Long> {

    @Query("select c FROM PermittedCarrier c where c.id=:id and c.userId =:userId")
    PermittedCarrier findOneByIdUserId( @Param("id") long id , @Param("userId") long userId );

    @Query("select c FROM PermittedCarrier c where c.userId =:userId")
    List <PermittedCarrier> findAllByUserId( @Param("userId") long userId );


}
