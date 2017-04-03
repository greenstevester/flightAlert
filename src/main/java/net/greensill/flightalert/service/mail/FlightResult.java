package net.greensill.flightalert.service.mail;

import com.google.api.services.qpxExpress.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by stevengreensill on 9/2/16.
 */
public class FlightResult {

    private final Logger log = LoggerFactory.getLogger(FlightResult.class);

    private String price;
    private String carrier;
    private String duration;
    private String stopDuration;
    private String bookingCode;
    private String flightNumber;

    public FlightResult(TripOption tripResult) {

        price = tripResult.getPricing().get(0).getSaleFareTotal();
        SliceInfo sliceInfo = tripResult.getSlice().get(0);
        duration = sliceInfo.getDuration().toString();
        bookingCode = sliceInfo.getSegment().get(0).getBookingCode();
        flightNumber = sliceInfo.getSegment().get(0).getFlight().getNumber();
        carrier = sliceInfo.getSegment().get(0).getFlight().getCarrier();
        stopDuration = sliceInfo.getSegment().get(0).getConnectionDuration()==null ? "None": sliceInfo.getSegment().get(0).getConnectionDuration().toString();

    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStopDuration() {
        return stopDuration;
    }

    public void setStopDuration(String stopDuration) {
        this.stopDuration = stopDuration;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlightResult that = (FlightResult) o;

        if (!price.equals(that.price)) return false;
        if (!carrier.equals(that.carrier)) return false;
        if (!duration.equals(that.duration)) return false;
        if (!stopDuration.equals(that.stopDuration)) return false;
        if (!bookingCode.equals(that.bookingCode)) return false;
        return flightNumber.equals(that.flightNumber);

    }

    @Override
    public int hashCode() {
        int result = price.hashCode();
        result = 31 * result + carrier.hashCode();
        result = 31 * result + duration.hashCode();
        result = 31 * result + stopDuration.hashCode();
        result = 31 * result + bookingCode.hashCode();
        result = 31 * result + flightNumber.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FlightResult{" +
                "price='" + price + '\'' +
                ", carrier='" + carrier + '\'' +
                ", duration='" + duration + '\'' +
                ", stopDuration='" + stopDuration + '\'' +
                ", bookingCode='" + bookingCode + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                '}';
    }
}
