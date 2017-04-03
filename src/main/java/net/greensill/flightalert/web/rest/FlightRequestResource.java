package net.greensill.flightalert.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.greensill.flightalert.domain.FlightRequest;
import net.greensill.flightalert.domain.User;
import net.greensill.flightalert.repository.UserRepository;
import net.greensill.flightalert.service.FlightRequestService;
import net.greensill.flightalert.web.rest.util.HeaderUtil;
import net.greensill.flightalert.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing FlightRequest.
 */
@RestController
@RequestMapping("/api")
public class FlightRequestResource {

    private final Logger log = LoggerFactory.getLogger(FlightRequestResource.class);
        
    @Inject
    private FlightRequestService flightRequestService;

    @Inject
    private UserRepository userRepository;


    /**
     * POST  /flightRequests -> Create a new flightRequest.
     */
    @RequestMapping(value = "/flightRequests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlightRequest> createFlightRequest(@Valid @RequestBody FlightRequest flightRequest, Principal principal) throws URISyntaxException {
        log.debug("REST request to save FlightRequest : {}", flightRequest);
        if (flightRequest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("flightRequest", "idexists", "A new flightRequest cannot already have an ID")).body(null);
        }
        log.debug("principal to save FlightRequest : {}", principal.getName());

        Optional<User> existingUser = userRepository.findOneByLogin(principal.getName());
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("flightRequest", "uknownuser", "A new flightRequest can only be created by a known user")).body(null);
        }
        flightRequest.setUserId(existingUser.get().getId());

        FlightRequest result = flightRequestService.save(flightRequest);
        return ResponseEntity.created(new URI("/api/flightRequests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("flightRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /flightRequests -> Updates an existing flightRequest.
     */
    @RequestMapping(value = "/flightRequests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlightRequest> updateFlightRequest(@Valid @RequestBody FlightRequest flightRequest,  Principal principal) throws URISyntaxException {
        log.debug("REST request to update FlightRequest : {}", flightRequest);
        if (flightRequest.getId() == null) {
            return createFlightRequest(flightRequest, principal);
        }
        log.debug("principal to update FlightRequest : {}", principal.getName());

        Optional<User> existingUser = userRepository.findOneByLogin(principal.getName());
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("flightRequest", "uknownuser", "A new flightRequest can only be updated by a known user")).body(null);
        }
        flightRequest.setUserId(existingUser.get().getId());

        FlightRequest result = flightRequestService.save(flightRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("flightRequest", flightRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /flightRequests -> get all the flightRequests.
     */
    @RequestMapping(value = "/flightRequests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FlightRequest>> getAllFlightRequests(Pageable pageable, Principal principal)
        throws URISyntaxException {
        log.debug("REST request to get a page of FlightRequests");

        log.debug("principal to update FlightRequest : {}", principal.getName());

        Optional<User> existingUser = userRepository.findOneByLogin(principal.getName());
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("flightRequest", "uknownuser", "A new flightRequest can only be updated by a known user")).body(null);
        }


        Page<FlightRequest> page = flightRequestService.findAllForUser( existingUser.get().getId(), pageable );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/flightRequests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /flightRequests/:id -> get the "id" flightRequest.
     */
    @RequestMapping(value = "/flightRequests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlightRequest> getFlightRequest(@PathVariable Long id) {
        log.debug("REST request to get FlightRequest : {}", id);
        FlightRequest flightRequest = flightRequestService.findOne(id);
        return Optional.ofNullable(flightRequest)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /flightRequests/:id -> delete the "id" flightRequest.
     */
    @RequestMapping(value = "/flightRequests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFlightRequest(@PathVariable Long id) {
        log.debug("REST request to delete FlightRequest : {}", id);
        flightRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("flightRequest", id.toString())).build();
    }

    /**
     * SEARCH  /_search/flightRequests/:query -> search for the flightRequest corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/flightRequests/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FlightRequest> searchFlightRequests(@PathVariable String query) {
        log.debug("Request to search FlightRequests for query {}", query);
        return flightRequestService.search(query);
    }
}
