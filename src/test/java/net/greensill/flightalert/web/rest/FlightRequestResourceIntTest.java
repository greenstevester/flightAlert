package net.greensill.flightalert.web.rest;

import net.greensill.flightalert.Application;
import net.greensill.flightalert.domain.FlightRequest;
import net.greensill.flightalert.repository.FlightRequestRepository;
import net.greensill.flightalert.service.FlightRequestService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FlightRequestResource REST controller.
 *
 * @see FlightRequestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FlightRequestResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_VALID_FROM_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_VALID_FROM_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_VALID_FROM_DATE_STR = dateTimeFormatter.format(DEFAULT_VALID_FROM_DATE);

    private static final ZonedDateTime DEFAULT_VALID_TO_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_VALID_TO_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_VALID_TO_DATE_STR = dateTimeFormatter.format(DEFAULT_VALID_TO_DATE);

    @Inject
    private FlightRequestRepository flightRequestRepository;

    @Inject
    private FlightRequestService flightRequestService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFlightRequestMockMvc;

    private FlightRequest flightRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FlightRequestResource flightRequestResource = new FlightRequestResource();
        ReflectionTestUtils.setField(flightRequestResource, "flightRequestService", flightRequestService);
        this.restFlightRequestMockMvc = MockMvcBuilders.standaloneSetup(flightRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        flightRequest = new FlightRequest();
        flightRequest.setValidFromDate(DEFAULT_VALID_FROM_DATE);
        flightRequest.setValidToDate(DEFAULT_VALID_TO_DATE);
    }

    @Test
    @Transactional
    public void createFlightRequest() throws Exception {
        int databaseSizeBeforeCreate = flightRequestRepository.findAll().size();

        // Create the FlightRequest

        restFlightRequestMockMvc.perform(post("/api/flightRequests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flightRequest)))
                .andExpect(status().isCreated());

        // Validate the FlightRequest in the database
        List<FlightRequest> flightRequests = flightRequestRepository.findAll();
        assertThat(flightRequests).hasSize(databaseSizeBeforeCreate + 1);
        FlightRequest testFlightRequest = flightRequests.get(flightRequests.size() - 1);
        assertThat(testFlightRequest.getValidFromDate()).isEqualTo(DEFAULT_VALID_FROM_DATE);
        assertThat(testFlightRequest.getValidToDate()).isEqualTo(DEFAULT_VALID_TO_DATE);
    }

    @Test
    @Transactional
    public void checkValidFromDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightRequestRepository.findAll().size();
        // set the field null
        flightRequest.setValidFromDate(null);

        // Create the FlightRequest, which fails.

        restFlightRequestMockMvc.perform(post("/api/flightRequests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flightRequest)))
                .andExpect(status().isBadRequest());

        List<FlightRequest> flightRequests = flightRequestRepository.findAll();
        assertThat(flightRequests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValidToDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightRequestRepository.findAll().size();
        // set the field null
        flightRequest.setValidToDate(null);

        // Create the FlightRequest, which fails.

        restFlightRequestMockMvc.perform(post("/api/flightRequests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flightRequest)))
                .andExpect(status().isBadRequest());

        List<FlightRequest> flightRequests = flightRequestRepository.findAll();
        assertThat(flightRequests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFlightRequests() throws Exception {
        // Initialize the database
        flightRequestRepository.saveAndFlush(flightRequest);

        // Get all the flightRequests
        restFlightRequestMockMvc.perform(get("/api/flightRequests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(flightRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].validFromDate").value(hasItem(DEFAULT_VALID_FROM_DATE_STR)))
                .andExpect(jsonPath("$.[*].validToDate").value(hasItem(DEFAULT_VALID_TO_DATE_STR)));
    }

    @Test
    @Transactional
    public void getFlightRequest() throws Exception {
        // Initialize the database
        flightRequestRepository.saveAndFlush(flightRequest);

        // Get the flightRequest
        restFlightRequestMockMvc.perform(get("/api/flightRequests/{id}", flightRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(flightRequest.getId().intValue()))
            .andExpect(jsonPath("$.validFromDate").value(DEFAULT_VALID_FROM_DATE_STR))
            .andExpect(jsonPath("$.validToDate").value(DEFAULT_VALID_TO_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingFlightRequest() throws Exception {
        // Get the flightRequest
        restFlightRequestMockMvc.perform(get("/api/flightRequests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlightRequest() throws Exception {
        // Initialize the database
        flightRequestRepository.saveAndFlush(flightRequest);

		int databaseSizeBeforeUpdate = flightRequestRepository.findAll().size();

        // Update the flightRequest
        flightRequest.setValidFromDate(UPDATED_VALID_FROM_DATE);
        flightRequest.setValidToDate(UPDATED_VALID_TO_DATE);

        restFlightRequestMockMvc.perform(put("/api/flightRequests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flightRequest)))
                .andExpect(status().isOk());

        // Validate the FlightRequest in the database
        List<FlightRequest> flightRequests = flightRequestRepository.findAll();
        assertThat(flightRequests).hasSize(databaseSizeBeforeUpdate);
        FlightRequest testFlightRequest = flightRequests.get(flightRequests.size() - 1);
        assertThat(testFlightRequest.getValidFromDate()).isEqualTo(UPDATED_VALID_FROM_DATE);
        assertThat(testFlightRequest.getValidToDate()).isEqualTo(UPDATED_VALID_TO_DATE);
    }

    @Test
    @Transactional
    public void deleteFlightRequest() throws Exception {
        // Initialize the database
        flightRequestRepository.saveAndFlush(flightRequest);

		int databaseSizeBeforeDelete = flightRequestRepository.findAll().size();

        // Get the flightRequest
        restFlightRequestMockMvc.perform(delete("/api/flightRequests/{id}", flightRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FlightRequest> flightRequests = flightRequestRepository.findAll();
        assertThat(flightRequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
