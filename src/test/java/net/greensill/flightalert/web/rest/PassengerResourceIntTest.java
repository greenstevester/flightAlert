package net.greensill.flightalert.web.rest;

import net.greensill.flightalert.Application;
import net.greensill.flightalert.domain.Passenger;
import net.greensill.flightalert.repository.PassengerRepository;
import net.greensill.flightalert.repository.search.PassengerSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import net.greensill.flightalert.domain.enumeration.PassengerType;

/**
 * Test class for the PassengerResource REST controller.
 *
 * @see PassengerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PassengerResourceIntTest {

    
    private static final PassengerType DEFAULT_PASSENGER_TYPE = PassengerType.ADULT;
    private static final PassengerType UPDATED_PASSENGER_TYPE = PassengerType.CHILD;

    @Inject
    private PassengerRepository passengerRepository;

    @Inject
    private PassengerSearchRepository passengerSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPassengerMockMvc;

    private Passenger passenger;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PassengerResource passengerResource = new PassengerResource();
        ReflectionTestUtils.setField(passengerResource, "passengerSearchRepository", passengerSearchRepository);
        ReflectionTestUtils.setField(passengerResource, "passengerRepository", passengerRepository);
        this.restPassengerMockMvc = MockMvcBuilders.standaloneSetup(passengerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        passenger = new Passenger();
        passenger.setPassengerType(DEFAULT_PASSENGER_TYPE);
    }

    @Test
    @Transactional
    public void createPassenger() throws Exception {
        int databaseSizeBeforeCreate = passengerRepository.findAll().size();

        // Create the Passenger

        restPassengerMockMvc.perform(post("/api/passengers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(passenger)))
                .andExpect(status().isCreated());

        // Validate the Passenger in the database
        List<Passenger> passengers = passengerRepository.findAll();
        assertThat(passengers).hasSize(databaseSizeBeforeCreate + 1);
        Passenger testPassenger = passengers.get(passengers.size() - 1);
        assertThat(testPassenger.getPassengerType()).isEqualTo(DEFAULT_PASSENGER_TYPE);
    }

    @Test
    @Transactional
    public void checkPassengerTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setPassengerType(null);

        // Create the Passenger, which fails.

        restPassengerMockMvc.perform(post("/api/passengers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(passenger)))
                .andExpect(status().isBadRequest());

        List<Passenger> passengers = passengerRepository.findAll();
        assertThat(passengers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPassengers() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengers
        restPassengerMockMvc.perform(get("/api/passengers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(passenger.getId().intValue())))
                .andExpect(jsonPath("$.[*].passengerType").value(hasItem(DEFAULT_PASSENGER_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getPassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get the passenger
        restPassengerMockMvc.perform(get("/api/passengers/{id}", passenger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(passenger.getId().intValue()))
            .andExpect(jsonPath("$.passengerType").value(DEFAULT_PASSENGER_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPassenger() throws Exception {
        // Get the passenger
        restPassengerMockMvc.perform(get("/api/passengers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

		int databaseSizeBeforeUpdate = passengerRepository.findAll().size();

        // Update the passenger
        passenger.setPassengerType(UPDATED_PASSENGER_TYPE);

        restPassengerMockMvc.perform(put("/api/passengers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(passenger)))
                .andExpect(status().isOk());

        // Validate the Passenger in the database
        List<Passenger> passengers = passengerRepository.findAll();
        assertThat(passengers).hasSize(databaseSizeBeforeUpdate);
        Passenger testPassenger = passengers.get(passengers.size() - 1);
        assertThat(testPassenger.getPassengerType()).isEqualTo(UPDATED_PASSENGER_TYPE);
    }

    @Test
    @Transactional
    public void deletePassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

		int databaseSizeBeforeDelete = passengerRepository.findAll().size();

        // Get the passenger
        restPassengerMockMvc.perform(delete("/api/passengers/{id}", passenger.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Passenger> passengers = passengerRepository.findAll();
        assertThat(passengers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
