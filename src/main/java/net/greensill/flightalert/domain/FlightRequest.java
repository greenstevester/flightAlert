package net.greensill.flightalert.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

/**
 * A FlightRequest.
 */
@Entity
@Table(name = "flight_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "flightrequest")
public class FlightRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "valid_from_date", nullable = false)
    private ZonedDateTime validFromDate;

    @NotNull
    @Column(name = "valid_to_date", nullable = false)
    private ZonedDateTime validToDate;

    @OneToMany(mappedBy = "flightRequest")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FlightSlice> legs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getValidFromDate() {
        return validFromDate;
    }

    public void setValidFromDate(ZonedDateTime validFromDate) {
        this.validFromDate = validFromDate;
    }

    public ZonedDateTime getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(ZonedDateTime validToDate) {
        this.validToDate = validToDate;
    }

    public Set<FlightSlice> getLegs() {

        SortedSet<FlightSlice>  sortedLegs = new TreeSet<FlightSlice>(new Comparator<FlightSlice>(){
            public int compare(FlightSlice fs1, FlightSlice fs2) {
                Long id1 = ((FlightSlice) fs1).getId();
                Long id2 = ((FlightSlice) fs2).getId();
                if (id1>id2)
                    return 1;
                if (id2>id1)
                    return -1;
                return 0;
            }
        });

        sortedLegs.addAll(legs);
        return sortedLegs;

    }

    public void setLegs(Set<FlightSlice> flightSlices) {
        this.legs = flightSlices;
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
        FlightRequest flightRequest = (FlightRequest) o;
        if(flightRequest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, flightRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FlightRequest{" +
                "id=" + id +
                ", validFromDate=" + validFromDate +
                ", validToDate=" + validToDate +
                ", userId=" + userId +
                '}';
    }
}
