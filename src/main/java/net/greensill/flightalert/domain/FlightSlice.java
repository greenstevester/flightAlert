package net.greensill.flightalert.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import net.greensill.flightalert.domain.enumeration.CabinClass;

/**
 * A FlightSlice.
 */
@Entity
@Table(name = "flight_slice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "flightslice")
public class FlightSlice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 3)
    @Column(name = "origin", length = 3, nullable = false)
    private String origin;

    @NotNull
    @Size(max = 3)
    @Column(name = "destination", length = 3, nullable = false)
    private String destination;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_cabin")
    private CabinClass preferredCabin;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @Min(value = 0)
    @Max(value = 99999)
    @Column(name = "max_stops")
    private Integer maxStops;

    @Min(value = 1)
    @Max(value = 99999)
    @Column(name = "max_connection_duration_in_minutes")
    private Integer maxConnectionDurationInMinutes;

    @Min(value = 1)
    @Max(value = 99999)
    @Column(name = "max_price_in_chf", precision=10, scale=2)
    private BigDecimal maxPriceInCHF;

    @Column(name = "refundable")
    private Boolean refundable;

    @OneToMany(mappedBy = "flightSlice")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PermittedCarrier> permittedCarriers = new HashSet<>();

    @OneToMany(mappedBy = "flightSlice")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Passenger> passengers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "flight_request_id")
    private FlightRequest flightRequest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public CabinClass getPreferredCabin() {
        return preferredCabin;
    }

    public void setPreferredCabin(CabinClass preferredCabin) {
        this.preferredCabin = preferredCabin;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getMaxStops() {
        return maxStops;
    }

    public void setMaxStops(Integer maxStops) {
        this.maxStops = maxStops;
    }

    public Integer getMaxConnectionDurationInMinutes() {
        return maxConnectionDurationInMinutes;
    }

    public void setMaxConnectionDurationInMinutes(Integer maxConnectionDurationInMinutes) {
        this.maxConnectionDurationInMinutes = maxConnectionDurationInMinutes;
    }

    public BigDecimal getMaxPriceInCHF() {
        return maxPriceInCHF;
    }

    public void setMaxPriceInCHF(BigDecimal maxPriceInCHF) {
        this.maxPriceInCHF = maxPriceInCHF;
    }

    public Boolean getRefundable() {
        return refundable;
    }

    public void setRefundable(Boolean refundable) {
        this.refundable = refundable;
    }

    public Set<PermittedCarrier> getPermittedCarriers() {
        return permittedCarriers;
    }

    public void setPermittedCarriers(Set<PermittedCarrier> permittedCarriers) {
        this.permittedCarriers = permittedCarriers;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public FlightRequest getFlightRequest() {
        return flightRequest;
    }

    public void setFlightRequest(FlightRequest flightRequest) {
        this.flightRequest = flightRequest;
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
        FlightSlice flightSlice = (FlightSlice) o;
        if(flightSlice.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, flightSlice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FlightSlice{" +
                "id=" + id +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", preferredCabin=" + preferredCabin +
                ", date=" + date +
                ", maxStops=" + maxStops +
                ", maxConnectionDurationInMinutes=" + maxConnectionDurationInMinutes +
                ", maxPriceInCHF=" + maxPriceInCHF +
                ", refundable=" + refundable +
                ", flightRequest=" + flightRequest +
                ", userId=" + userId +
                '}';
    }

}
