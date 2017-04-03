package net.greensill.flightalert.web.rest;

import net.greensill.flightalert.Application;
import net.greensill.flightalert.domain.PermittedCarrier;
import net.greensill.flightalert.repository.PermittedCarrierRepository;
import net.greensill.flightalert.repository.search.PermittedCarrierSearchRepository;

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

import net.greensill.flightalert.domain.enumeration.CarrierCode;

/**
 * Test class for the PermittedCarrierResource REST controller.
 *
 * @see PermittedCarrierResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PermittedCarrierResourceIntTest {

    
    private static final CarrierCode DEFAULT_CARRIER_CODE = CarrierCode.LX;
    private static final CarrierCode UPDATED_CARRIER_CODE = CarrierCode.LH;

    @Inject
    private PermittedCarrierRepository permittedCarrierRepository;

    @Inject
    private PermittedCarrierSearchRepository permittedCarrierSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPermittedCarrierMockMvc;

    private PermittedCarrier permittedCarrier;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PermittedCarrierResource permittedCarrierResource = new PermittedCarrierResource();
        ReflectionTestUtils.setField(permittedCarrierResource, "permittedCarrierSearchRepository", permittedCarrierSearchRepository);
        ReflectionTestUtils.setField(permittedCarrierResource, "permittedCarrierRepository", permittedCarrierRepository);
        this.restPermittedCarrierMockMvc = MockMvcBuilders.standaloneSetup(permittedCarrierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        permittedCarrier = new PermittedCarrier();
        permittedCarrier.setCarrierCode(DEFAULT_CARRIER_CODE);
    }

    @Test
    @Transactional
    public void createPermittedCarrier() throws Exception {
        int databaseSizeBeforeCreate = permittedCarrierRepository.findAll().size();

        // Create the PermittedCarrier

        restPermittedCarrierMockMvc.perform(post("/api/permittedCarriers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(permittedCarrier)))
                .andExpect(status().isCreated());

        // Validate the PermittedCarrier in the database
        List<PermittedCarrier> permittedCarriers = permittedCarrierRepository.findAll();
        assertThat(permittedCarriers).hasSize(databaseSizeBeforeCreate + 1);
        PermittedCarrier testPermittedCarrier = permittedCarriers.get(permittedCarriers.size() - 1);
        assertThat(testPermittedCarrier.getCarrierCode()).isEqualTo(DEFAULT_CARRIER_CODE);
    }

    @Test
    @Transactional
    public void checkCarrierCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = permittedCarrierRepository.findAll().size();
        // set the field null
        permittedCarrier.setCarrierCode(null);

        // Create the PermittedCarrier, which fails.

        restPermittedCarrierMockMvc.perform(post("/api/permittedCarriers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(permittedCarrier)))
                .andExpect(status().isBadRequest());

        List<PermittedCarrier> permittedCarriers = permittedCarrierRepository.findAll();
        assertThat(permittedCarriers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPermittedCarriers() throws Exception {
        // Initialize the database
        permittedCarrierRepository.saveAndFlush(permittedCarrier);

        // Get all the permittedCarriers
        restPermittedCarrierMockMvc.perform(get("/api/permittedCarriers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(permittedCarrier.getId().intValue())))
                .andExpect(jsonPath("$.[*].carrierCode").value(hasItem(DEFAULT_CARRIER_CODE.toString())));
    }

    @Test
    @Transactional
    public void getPermittedCarrier() throws Exception {
        // Initialize the database
        permittedCarrierRepository.saveAndFlush(permittedCarrier);

        // Get the permittedCarrier
        restPermittedCarrierMockMvc.perform(get("/api/permittedCarriers/{id}", permittedCarrier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(permittedCarrier.getId().intValue()))
            .andExpect(jsonPath("$.carrierCode").value(DEFAULT_CARRIER_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPermittedCarrier() throws Exception {
        // Get the permittedCarrier
        restPermittedCarrierMockMvc.perform(get("/api/permittedCarriers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePermittedCarrier() throws Exception {
        // Initialize the database
        permittedCarrierRepository.saveAndFlush(permittedCarrier);

		int databaseSizeBeforeUpdate = permittedCarrierRepository.findAll().size();

        // Update the permittedCarrier
        permittedCarrier.setCarrierCode(UPDATED_CARRIER_CODE);

        restPermittedCarrierMockMvc.perform(put("/api/permittedCarriers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(permittedCarrier)))
                .andExpect(status().isOk());

        // Validate the PermittedCarrier in the database
        List<PermittedCarrier> permittedCarriers = permittedCarrierRepository.findAll();
        assertThat(permittedCarriers).hasSize(databaseSizeBeforeUpdate);
        PermittedCarrier testPermittedCarrier = permittedCarriers.get(permittedCarriers.size() - 1);
        assertThat(testPermittedCarrier.getCarrierCode()).isEqualTo(UPDATED_CARRIER_CODE);
    }

    @Test
    @Transactional
    public void deletePermittedCarrier() throws Exception {
        // Initialize the database
        permittedCarrierRepository.saveAndFlush(permittedCarrier);

		int databaseSizeBeforeDelete = permittedCarrierRepository.findAll().size();

        // Get the permittedCarrier
        restPermittedCarrierMockMvc.perform(delete("/api/permittedCarriers/{id}", permittedCarrier.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PermittedCarrier> permittedCarriers = permittedCarrierRepository.findAll();
        assertThat(permittedCarriers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
