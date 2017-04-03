package net.greensill.flightalert.service;

import net.greensill.flightalert.domain.FlightRequest;
import net.greensill.flightalert.repository.FlightRequestRepository;
import net.greensill.flightalert.repository.search.FlightRequestSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing FlightRequest.
 */
@Service
@Transactional
public class FlightRequestService {

    private final Logger log = LoggerFactory.getLogger(FlightRequestService.class);
    
    @Inject
    private FlightRequestRepository flightRequestRepository;
    
    @Inject
    private FlightRequestSearchRepository flightRequestSearchRepository;
    
    /**
     * Save a flightRequest.
     * @return the persisted entity
     */
    public FlightRequest save(FlightRequest flightRequest) {
        log.debug("Request to save FlightRequest : {}", flightRequest);
        FlightRequest result = flightRequestRepository.save(flightRequest);
        flightRequestSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the flightRequests.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<FlightRequest> findAllForUser(Long userId, Pageable pageable ) {
        log.debug("Request to get all FlightRequests");
        Page<FlightRequest> result = flightRequestRepository.findAllByUserId(userId, pageable);
        return result;
    }

    /**
     *  get one flightRequest by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public FlightRequest findOne(Long id) {
        log.debug("Request to get FlightRequest : {}", id);
        FlightRequest flightRequest = flightRequestRepository.findOne(id);
        return flightRequest;
    }

    /**
     *  delete the  flightRequest by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete FlightRequest : {}", id);
        flightRequestRepository.delete(id);
        flightRequestSearchRepository.delete(id);
    }

    /**
     * search for the flightRequest corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<FlightRequest> search(String query) {
        
        log.debug("REST request to search FlightRequests for query {}", query);
        return StreamSupport
            .stream(flightRequestSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
