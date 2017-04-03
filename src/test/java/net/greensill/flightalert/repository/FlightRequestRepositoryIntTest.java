package net.greensill.flightalert.repository;

import net.greensill.flightalert.Application;
import net.greensill.flightalert.domain.FlightRequest;
import net.greensill.flightalert.domain.FlightSlice;
import org.assertj.core.api.StrictAssertions;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Test class for the UserResource REST controller.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
@ActiveProfiles("dev")
public class FlightRequestRepositoryIntTest {

    private final Logger log = LoggerFactory.getLogger( FlightRequestRepositoryIntTest.class );

    @Inject
    private FlightRequestRepository flightRequestRepository;

    @Test
    public void findAllForUser() {

        ZonedDateTime now = ZonedDateTime.now();
        List<FlightRequest> flightRequests = flightRequestRepository.findAllByUserId(3);

        log.info("==============================");
        for (FlightRequest flightRequest: flightRequests) {
            log.info("flightRequest:[{}]", flightRequest);
            for (FlightSlice flightSlice: flightRequest.getLegs()) {
                log.info("flightSlice:[{}]", flightSlice);
            }

            Set<FlightSlice> sliceSet = flightRequest.getLegs();
            FlightSlice[] slices = sliceSet.toArray(new FlightSlice[sliceSet.size()]);
            boolean inOrder = true;
            for(int i = 0 ; i < slices.length -1; i++) {
                if(slices[i].getId() >= slices[i+1].getId()) {
                    inOrder = false;
                    break;
                }
            }

            StrictAssertions.assertThat(inOrder);

        }
        log.info("==============================");

        assertThat(flightRequests).isNotEmpty();

        Pageable pageable = new PageRequest(0,10).first();
        Page<FlightRequest> flightRequestPage = flightRequestRepository.findAllByUserId(3, pageable);


        assertThat(flightRequestPage).isNotEmpty();

    }

    @Test
    public void findOneForUser() {

        FlightRequest flightRequest = flightRequestRepository.findOneByIdUserId(1, 3);
        log.info("==============================");
        log.info("flightRequest:[{}]", flightRequest);
        log.info("==============================");
        StrictAssertions.assertThat(flightRequest).isNotNull();


    }


}
