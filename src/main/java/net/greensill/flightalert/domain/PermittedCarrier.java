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

import net.greensill.flightalert.domain.enumeration.CarrierCode;

/**
 * A PermittedCarrier.
 */
@Entity
@Table(name = "permitted_carrier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "permittedcarrier")
public class PermittedCarrier implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "carrier_code", nullable = false)
    private CarrierCode carrierCode;
    
    @ManyToOne
    @JoinColumn(name = "flight_slice_id")
    private FlightSlice flightSlice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CarrierCode getCarrierCode() {
        return carrierCode;
    }
    
    public void setCarrierCode(CarrierCode carrierCode) {
        this.carrierCode = carrierCode;
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
        PermittedCarrier permittedCarrier = (PermittedCarrier) o;
        if(permittedCarrier.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, permittedCarrier.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PermittedCarrier{" +
                "id=" + id +
                ", carrierCode=" + carrierCode +
                ", userId=" + userId +
                '}';
    }
}
