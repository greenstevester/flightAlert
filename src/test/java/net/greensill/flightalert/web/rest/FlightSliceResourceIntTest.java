package net.greensill.flightalert.web.rest;

import net.greensill.flightalert.Application;
import net.greensill.flightalert.domain.FlightSlice;
import net.greensill.flightalert.repository.FlightSliceRepository;
import net.greensill.flightalert.repository.search.FlightSliceSearchRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import net.greensill.flightalert.domain.enumeration.CabinClass;

/**
 * Test class for the FlightSliceResource REST controller.
 *
 * @see FlightSliceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FlightSliceResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_ORIGIN = "AAAAA";
    private static final String UPDATED_ORIGIN = "BBBBB";
    private static final String DEFAULT_DESTINATION = "AAAAA";
    private static final String UPDATED_DESTINATION = "BBBBB";
    
    private static final CabinClass DEFAULT_PREFERRED_CABIN = CabinClass.COACH;
    private static final CabinClass UPDATED_PREFERRED_CABIN = CabinClass.PREMIUM_COACH;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    private static final Integer DEFAULT_MAX_STOPS = 1;
    private static final Integer UPDATED_MAX_STOPS = 2;

    private static final Integer DEFAULT_MAX_CONNECTION_DURATION_IN_MINUTES = 1;
    private static final Integer UPDATED_MAX_CONNECTION_DURATION_IN_MINUTES = 2;

    private static final BigDecimal DEFAULT_MAX_PRICE_IN_CHF = new BigDecimal(1);
    private static final BigDecimal UPDATED_MAX_PRICE_IN_CHF = new BigDecimal(2);

    private static final Boolean DEFAULT_REFUNDABLE = false;
    private static final Boolean UPDATED_REFUNDABLE = true;

    @Inject
    private FlightSliceRepository flightSliceRepository;

    @Inject
    private FlightSliceSearchRepository flightSliceSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFlightSliceMockMvc;

    private FlightSlice flightSlice;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FlightSliceResource flightSliceResource = new FlightSliceResource();
        ReflectionTestUtils.setField(flightSliceResource, "flightSliceSearchRepository", flightSliceSearchRepository);
        ReflectionTestUtils.setField(flightSliceResource, "flightSliceRepository", flightSliceRepository);
        this.restFlightSliceMockMvc = MockMvcBuilders.standaloneSetup(flightSliceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        flightSlice = new FlightSlice();
        flightSlice.setOrigin(DEFAULT_ORIGIN);
        flightSlice.setDestination(DEFAULT_DESTINATION);
        flightSlice.setPreferredCabin(DEFAULT_PREFERRED_CABIN);
        flightSlice.setDate(DEFAULT_DATE);
        flightSlice.setMaxStops(DEFAULT_MAX_STOPS);
        flightSlice.setMaxConnectionDurationInMinutes(DEFAULT_MAX_CONNECTION_DURATION_IN_MINUTES);
        flightSlice.setMaxPriceInCHF(DEFAULT_MAX_PRICE_IN_CHF);
        flightSlice.setRefundable(DEFAULT_REFUNDABLE);
    }

    @Test
    @Transactional
    public void createFlightSlice() throws Exception {
        int databaseSizeBeforeCreate = flightSliceRepository.findAll().size();

        // Create the FlightSlice

        restFlightSliceMockMvc.perform(post("/api/flightSlices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flightSlice)))
                .andExpect(status().isCreated());

        // Validate the FlightSlice in the database
        List<FlightSlice> flightSlices = flightSliceRepository.findAll();
        assertThat(flightSlices).hasSize(databaseSizeBeforeCreate + 1);
        FlightSlice testFlightSlice = flightSlices.get(flightSlices.size() - 1);
        assertThat(testFlightSlice.getOrigin()).isEqualTo(DEFAULT_ORIGIN);
        assertThat(testFlightSlice.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testFlightSlice.getPreferredCabin()).isEqualTo(DEFAULT_PREFERRED_CABIN);
        assertThat(testFlightSlice.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testFlightSlice.getMaxStops()).isEqualTo(DEFAULT_MAX_STOPS);
        assertThat(testFlightSlice.getMaxConnectionDurationInMinutes()).isEqualTo(DEFAULT_MAX_CONNECTION_DURATION_IN_MINUTES);
        assertThat(testFlightSlice.getMaxPriceInCHF()).isEqualTo(DEFAULT_MAX_PRICE_IN_CHF);
        assertThat(testFlightSlice.getRefundable()).isEqualTo(DEFAULT_REFUNDABLE);
    }

    @Test
    @Transactional
    public void checkOriginIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightSliceRepository.findAll().size();
        // set the field null
        flightSlice.setOrigin(null);

        // Create the FlightSlice, which fails.

        restFlightSliceMockMvc.perform(post("/api/flightSlices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flightSlice)))
                .andExpect(status().isBadRequest());

        List<FlightSlice> flightSlices = flightSliceRepository.findAll();
        assertThat(flightSlices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDestinationIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightSliceRepository.findAll().size();
        // set the field null
        flightSlice.setDestination(null);

        // Create the FlightSlice, which fails.

        restFlightSliceMockMvc.perform(post("/api/flightSlices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flightSlice)))
                .andExpect(status().isBadRequest());

        List<FlightSlice> flightSlices = flightSliceRepository.findAll();
        assertThat(flightSlices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightSliceRepository.findAll().size();
        // set the field null
        flightSlice.setDate(null);

        // Create the FlightSlice, which fails.

        restFlightSliceMockMvc.perform(post("/api/flightSlices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flightSlice)))
                .andExpect(status().isBadRequest());

        List<FlightSlice> flightSlices = flightSliceRepository.findAll();
        assertThat(flightSlices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFlightSlices() throws Exception {
        // Initialize the database
        flightSliceRepository.saveAndFlush(flightSlice);

        // Get all the flightSlices
        restFlightSliceMockMvc.perform(get("/api/flightSlices?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(flightSlice.getId().intValue())))
                .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN.toString())))
                .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION.toString())))
                .andExpect(jsonPath("$.[*].preferredCabin").value(hasItem(DEFAULT_PREFERRED_CABIN.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].maxStops").value(hasItem(DEFAULT_MAX_STOPS)))
                .andExpect(jsonPath("$.[*].maxConnectionDurationInMinutes").value(hasItem(DEFAULT_MAX_CONNECTION_DURATION_IN_MINUTES)))
                .andExpect(jsonPath("$.[*].maxPriceInCHF").value(hasItem(DEFAULT_MAX_PRICE_IN_CHF.intValue())))
                .andExpect(jsonPath("$.[*].refundable").value(hasItem(DEFAULT_REFUNDABLE.booleanValue())));
    }

    @Test
    @Transactional
    public void getFlightSlice() throws Exception {
        // Initialize the database
        flightSliceRepository.saveAndFlush(flightSlice);

        // Get the flightSlice
        restFlightSliceMockMvc.perform(get("/api/flightSlices/{id}", flightSlice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(flightSlice.getId().intValue()))
            .andExpect(jsonPath("$.origin").value(DEFAULT_ORIGIN.toString()))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION.toString()))
            .andExpect(jsonPath("$.preferredCabin").value(DEFAULT_PREFERRED_CABIN.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.maxStops").value(DEFAULT_MAX_STOPS))
            .andExpect(jsonPath("$.maxConnectionDurationInMinutes").value(DEFAULT_MAX_CONNECTION_DURATION_IN_MINUTES))
            .andExpect(jsonPath("$.maxPriceInCHF").value(DEFAULT_MAX_PRICE_IN_CHF.intValue()))
            .andExpect(jsonPath("$.refundable").value(DEFAULT_REFUNDABLE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFlightSlice() throws Exception {
        // Get the flightSlice
        restFlightSliceMockMvc.perform(get("/api/flightSlices/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlightSlice() throws Exception {
        // Initialize the database
        flightSliceRepository.saveAndFlush(flightSlice);

		int databaseSizeBeforeUpdate = flightSliceRepository.findAll().size();

        // Update the flightSlice
        flightSlice.setOrigin(UPDATED_ORIGIN);
        flightSlice.setDestination(UPDATED_DESTINATION);
        flightSlice.setPreferredCabin(UPDATED_PREFERRED_CABIN);
        flightSlice.setDate(UPDATED_DATE);
        flightSlice.setMaxStops(UPDATED_MAX_STOPS);
        flightSlice.setMaxConnectionDurationInMinutes(UPDATED_MAX_CONNECTION_DURATION_IN_MINUTES);
        flightSlice.setMaxPriceInCHF(UPDATED_MAX_PRICE_IN_CHF);
        flightSlice.setRefundable(UPDATED_REFUNDABLE);

        restFlightSliceMockMvc.perform(put("/api/flightSlices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flightSlice)))
                .andExpect(status().isOk());

        // Validate the FlightSlice in the database
        List<FlightSlice> flightSlices = flightSliceRepository.findAll();
        assertThat(flightSlices).hasSize(databaseSizeBeforeUpdate);
        FlightSlice testFlightSlice = flightSlices.get(flightSlices.size() - 1);
        assertThat(testFlightSlice.getOrigin()).isEqualTo(UPDATED_ORIGIN);
        assertThat(testFlightSlice.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testFlightSlice.getPreferredCabin()).isEqualTo(UPDATED_PREFERRED_CABIN);
        assertThat(testFlightSlice.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testFlightSlice.getMaxStops()).isEqualTo(UPDATED_MAX_STOPS);
        assertThat(testFlightSlice.getMaxConnectionDurationInMinutes()).isEqualTo(UPDATED_MAX_CONNECTION_DURATION_IN_MINUTES);
        assertThat(testFlightSlice.getMaxPriceInCHF()).isEqualTo(UPDATED_MAX_PRICE_IN_CHF);
        assertThat(testFlightSlice.getRefundable()).isEqualTo(UPDATED_REFUNDABLE);
    }

    @Test
    @Transactional
    public void deleteFlightSlice() throws Exception {
        // Initialize the database
        flightSliceRepository.saveAndFlush(flightSlice);

		int databaseSizeBeforeDelete = flightSliceRepository.findAll().size();

        // Get the flightSlice
        restFlightSliceMockMvc.perform(delete("/api/flightSlices/{id}", flightSlice.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FlightSlice> flightSlices = flightSliceRepository.findAll();
        assertThat(flightSlices).hasSize(databaseSizeBeforeDelete - 1);
    }
}
