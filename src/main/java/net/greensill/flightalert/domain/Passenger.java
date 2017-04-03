package net.greensill.flightalert.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import net.greensill.flightalert.domain.enumeration.PassengerType;

/**
 * A Passenger.
 */
@Entity
@Table(name = "passenger")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "passenger")
public class Passenger implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "passenger_type", nullable = false)
    private PassengerType passengerType;
    
    @ManyToOne
    @JoinColumn(name = "flight_slice_id")
    private FlightSlice flightSlice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PassengerType getPassengerType() {
        return passengerType;
    }
    
    public void setPassengerType(PassengerType passengerType) {
        this.passengerType = passengerType;
    }

    public FlightSlice getFlightSlice() {
        return flightSlice;
    }

    public void setFlightSlice(FlightSlice flightSlice) {
        this.flightSlice = flightSlice;
    }

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Passenger passenger = (Passenger) o;
        if(passenger.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, passenger.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", passengerType=" + passengerType +
                ", userId=" + userId +
                '}';
    }
}
