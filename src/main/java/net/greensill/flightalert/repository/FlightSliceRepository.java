package net.greensill.flightalert.repository;

import net.greensill.flightalert.domain.FlightSlice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the FlightSlice entity.
 */
public interface FlightSliceRepository extends JpaRepository<FlightSlice,Long> {

    @Query("select c FROM FlightSlice c where c.id=:id and c.userId =:userId")
    FlightSlice findOneByIdUserId( @Param("id") long id , @Param("userId") long userId );

    @Query("select c FROM FlightSlice c where c.userId =:userId")
    List <FlightSlice> findAllByUserId( @Param("userId") long userId );

    @Query("select c FROM FlightSlice c where c.userId =:userId")
    Page<FlightSlice> findAllByUserId( @Param("userId") long userId, Pageable pageable );

}
