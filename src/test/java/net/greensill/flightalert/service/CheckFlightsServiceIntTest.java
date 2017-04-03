package net.greensill.flightalert.service;

import net.greensill.flightalert.Application;
import net.greensill.flightalert.domain.User;
import net.greensill.flightalert.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by stevengreensill on 9/2/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
@ActiveProfiles("dev")
public class CheckFlightsServiceIntTest {

    @Inject
    private CheckFlightsService checkFlightsService;

    @Test
    public void testCheckFlightsService() {
        checkFlightsService.checkFlights();
    }


}
