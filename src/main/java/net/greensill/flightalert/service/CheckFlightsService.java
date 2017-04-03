package net.greensill.flightalert.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.qpxExpress.QPXExpress;
import com.google.api.services.qpxExpress.QPXExpressRequestInitializer;
import com.google.api.services.qpxExpress.model.*;
import net.greensill.flightalert.domain.*;
import net.greensill.flightalert.domain.enumeration.PassengerType;
import net.greensill.flightalert.repository.FlightRequestRepository;
import net.greensill.flightalert.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by stevengreensill on 9/2/16.
 */
@Service
@Transactional
public class CheckFlightsService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private static final String APPLICATION_NAME = "MyFlightApplication";

    private static final String API_KEY = "YOU NEED TO GET ONE FROM GOOGLE";

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Inject
    private UserRepository userRepository;

    @Inject
    private FlightRequestRepository flightRequestRepository;

    @Inject
    private MailService mailService;


    @PostConstruct
    public void init() throws IOException {
        log.info("CheckFlightsService started");
    }

    /**
     *
     *
     * Check flights
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * <p>
     * Cron expression is represented by six fields:
     * second, minute, hour, day of month, month, day(s) of week
     * <p>
     *
     *
     *  "0 0 * * * *" = the top of every hour of every day.
     *  "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
     *  "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
     *  "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
     *  "0 0 0 25 12 ?" = every Christmas Day at midnight
     *
     *  <p>
     *  (*) means match any
     *  a / followed by * means every X
     *
     *        *   *    *    *    *    *   (year optional)
     *        ┬   ┬    ┬    ┬    ┬    ┬
     *        │   │    │    │    │    │
     *        │   │    │    │    │    │
     *        │   │    │    │    │    └───── day of week (0 - 7) (0 or 7 is Sun, or use names)
     *        │   │    │    │    └────────── month (1 - 12)
     *        │   │    │    └─────────────── day of month (1 - 31)
     *        │   │    └──────────────────── hour (0 - 23)
     *        │   └───────────────────────── min (0 - 59)
     *        └─────────────────────────      seconds
     *
     */

     //@Scheduled(cron = "0 0 1 * * ?")  // every day at 1am
    @Scheduled(cron = "0 0 0/12 * * ?")  // every 12hrs, i.e. 2x per day

     //@Scheduled(cron = "0 0/1 * 1/1 * ?") // every minute for testing
     // @Scheduled(cron = "0 0 0/4 * * ?")  // every 4hrs

    public void checkFlights() {

        ZonedDateTime now = ZonedDateTime.now();

        // Get the transport
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get all flight requests that are still valid
        // for each flight request, find slices, passengers, permitted carriers.
        // send the request to google.
        // send an email.

        List<FlightRequest> flightRequests = flightRequestRepository.findAll();

        log.info("==============================");
        for (FlightRequest flightRequest: flightRequests) {

            if (!flightRequest.getLegs().isEmpty()) {

                log.info("creating flightRequest:[{}]", flightRequest);

                // if request is valid, create a new request via a RequestBuilder
                TripOptionsRequest request= CreateTripOptionsRequest(flightRequest);

                try {

                    TripsSearchRequest parameters = new TripsSearchRequest();
                    parameters.setRequest(request);
                    QPXExpress qpXExpress= new QPXExpress.Builder(httpTransport, JSON_FACTORY, null).setApplicationName(APPLICATION_NAME)
                        .setGoogleClientRequestInitializer(new QPXExpressRequestInitializer(API_KEY)).build();

                    TripsSearchResponse list= qpXExpress.trips().search(parameters).execute();
                    List<TripOption> tripResults=list.getTrips().getTripOption();

                    // if successful, create the email and send it out
                    Long userId = flightRequest.getUserId();
                    Optional<User> existingUser = userRepository.findOneById(userId);
                    if (existingUser.isPresent()) {
                        String emailSubject= getEmailSubject(flightRequest);
                        mailService.sendFlightRequestResultEmail(existingUser.get(), tripResults, emailSubject );
                    }

                } catch ( IOException e ) {
                    log.error(e.getMessage());
                    mailService.sendEmail("steve@greensill.net", "Flight request response error", e.getMessage(), false, false);

                }

            }

        }
        log.info("==============================");

    }

    private String getEmailSubject(FlightRequest flightRequest) {

        StringBuilder stringBuilder = new StringBuilder();

        Set<FlightSlice> flightSlices = flightRequest.getLegs();
        for (FlightSlice flightSlice: flightSlices) {

            if (!stringBuilder.toString().isEmpty()) {
                stringBuilder.append(" ... ");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = flightSlice.getDate().format(formatter);

            stringBuilder.append(flightSlice.getOrigin()).append("-to-")
                    .append(flightSlice.getDestination())
                    .append(" [").append(flightSlice.getPreferredCabin().name()).append("]")
                    .append(" ").append(formattedDate);


        }

        return stringBuilder.toString();

    }


    private TripOptionsRequest CreateTripOptionsRequest(FlightRequest flightRequest) {

        TripOptionsRequest request= new TripOptionsRequest();
        request.setSolutions(10);
        request.setPassengers(getPassengerCounts(flightRequest));
        request.setSlice(getSlices(flightRequest));
        return request;

    }

    private List<SliceInput> getSlices(FlightRequest flightRequest) {

        List<SliceInput> returnSlices = new ArrayList<SliceInput>();

        Set<FlightSlice> flightSlices = flightRequest.getLegs();
        for (FlightSlice flightSlice: flightSlices) {
            log.info("flightSlice:[{}]", flightSlice);

            SliceInput slice = new SliceInput();
            slice.setOrigin(flightSlice.getOrigin());
            slice.setDestination(flightSlice.getDestination());
            slice.setPreferredCabin(flightSlice.getPreferredCabin().name());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = flightSlice.getDate().format(formatter);
            slice.setDate(formattedDate);
            List<String> permittedCarriers = new ArrayList<String>();
            Set<PermittedCarrier> permittedCarriersSet = flightSlice.getPermittedCarriers();
            if (permittedCarriersSet != null && !permittedCarriersSet.isEmpty()) {
                permittedCarriers.addAll(permittedCarriersSet.stream()
                    .map(permittedCarrier -> permittedCarrier.getCarrierCode().name())
                    .collect(Collectors.toList()));
                slice.setPermittedCarrier(permittedCarriers);
            }


            // always exclude these carriers
            List<String> prohibitedCarriers = new ArrayList<String>();
            prohibitedCarriers.add("AI");
            prohibitedCarriers.add("AF");
            prohibitedCarriers.add("CA");
            prohibitedCarriers.add("CI");
            slice.setProhibitedCarrier(prohibitedCarriers);

            returnSlices.add(slice);

        }

        return returnSlices;


    }

    private PassengerCounts getPassengerCounts(FlightRequest flightRequest) {

        PassengerCounts passengerCounts= new PassengerCounts();

        Set<FlightSlice> flightSlices = flightRequest.getLegs();

        // assuming symetrical onward and return trip for passengers, so only count passengers on one slice
        HashMap<PassengerType,Integer> passengerCountMap = new HashMap<>();
        if (!flightSlices.isEmpty()) {
            FlightSlice flightSlice = flightSlices.iterator().next();
            Set<Passenger> passengerSet = flightSlice.getPassengers();
            for (Passenger passenger : passengerSet) {
                log.info("passenger:[{}]", passenger);
                Integer oldValue = passengerCountMap.get(passenger.getPassengerType()) == null ? 0 : passengerCountMap.get(passenger.getPassengerType());
                passengerCountMap.put(passenger.getPassengerType(), oldValue+1);
            }

            for (PassengerType passengerType : passengerCountMap.keySet()) {
                if (passengerType.equals(PassengerType.ADULT)) {
                    int value = passengerCountMap.get(passengerType);
                    log.info("passenger count for type:[{}] = [{}]", passengerType, value );
                    passengerCounts.setAdultCount(value);
                } else if (passengerType.equals(PassengerType.CHILD)) {
                    int value = passengerCountMap.get(passengerType);
                    log.info("passenger count for type:[{}] = [{}]", passengerType, value );
                    passengerCounts.setChildCount(value);
                } else if (passengerType.equals(PassengerType.SENIOR)) {
                    int value = passengerCountMap.get(passengerType);
                    log.info("passenger count for type:[{}] = [{}]", passengerType, value);
                    passengerCounts.setSeniorCount(value);
                }
            }
        }

        return passengerCounts;

    }


}
