package net.greensill.flightalert.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.greensill.flightalert.domain.FlightRequest;
import net.greensill.flightalert.domain.FlightSlice;
import net.greensill.flightalert.domain.User;
import net.greensill.flightalert.repository.FlightSliceRepository;
import net.greensill.flightalert.repository.UserRepository;
import net.greensill.flightalert.repository.search.FlightSliceSearchRepository;
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
 * REST controller for managing FlightSlice.
 */
@RestController
@RequestMapping("/api")
public class FlightSliceResource {

    private final Logger log = LoggerFactory.getLogger(FlightSliceResource.class);
        
    @Inject
    private FlightSliceRepository flightSliceRepository;
    
    @Inject
    private FlightSliceSearchRepository flightSliceSearchRepository;

    @Inject
    private UserRepository userRepository;
    
    /**
     * POST  /flightSlices -> Create a new flightSlice.
     */
    @RequestMapping(value = "/flightSlices",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlightSlice> createFlightSlice(@Valid @RequestBody FlightSlice flightSlice, Principal principal) throws URISyntaxException {
        log.debug("REST request to save FlightSlice : {}", flightSlice);
        if (flightSlice.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("flightSlice", "idexists", "A new flightSlice cannot already have an ID")).body(null);
        }

        log.debug("principal to update flightSlice : {}", principal.getName());

        Optional<User> existingUser = userRepository.findOneByLogin(principal.getName());
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("flightSlice", "uknownuser", "A new flightSlice can only be updated by a known user")).body(null);
        }
        flightSlice.setUserId(existingUser.get().getId());

        FlightSlice result = flightSliceRepository.save(flightSlice);
        flightSliceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/flightSlices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("flightSlice", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /flightSlices -> Updates an existing flightSlice.
     */
    @RequestMapping(value = "/flightSlices",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlightSlice> updateFlightSlice(@Valid @RequestBody FlightSlice flightSlice,  Principal principal) throws URISyntaxException {
        log.debug("REST request to update FlightSlice : {}", flightSlice);
        if (flightSlice.getId() == null) {
            return createFlightSlice(flightSlice, principal);
        }

        log.debug("principal to update flightSlice : {}", principal.getName());

        Optional<User> existingUser = userRepository.findOneByLogin(principal.getName());
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("flightSlice", "uknownuser", "A new flightSlice can only be updated by a known user")).body(null);
        }
        flightSlice.setUserId(existingUser.get().getId());

        FlightSlice result = flightSliceRepository.save(flightSlice);
        flightSliceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("flightSlice", flightSlice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /flightSlices -> get all the flightSlices.
     */
    @RequestMapping(value = "/flightSlices",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FlightSlice>> getAllFlightSlices(Pageable pageable, Principal principal)
        throws URISyntaxException {
        log.debug("REST request to get a page of FlightSlices");

        log.debug("principal to update FlightRequest : {}", principal.getName());

        Optional<User> existingUser = userRepository.findOneByLogin(principal.getName());
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("flightSlice", "uknownuser", "A new flightSlice can only be updated by a known user")).body(null);
        }

        Page<FlightSlice> page = flightSliceRepository.findAllByUserId( existingUser.get().getId(), pageable );

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/flightSlices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /flightSlices/:id -> get the "id" flightSlice.
     */
    @RequestMapping(value = "/flightSlices/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlightSlice> getFlightSlice(@PathVariable Long id) {
        log.debug("REST request to get FlightSlice : {}", id);
        FlightSlice flightSlice = flightSliceRepository.findOne(id);
        return Optional.ofNullable(flightSlice)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /flightSlices/:id -> delete the "id" flightSlice.
     */
    @RequestMapping(value = "/flightSlices/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFlightSlice(@PathVariable Long id) {
        log.debug("REST request to delete FlightSlice : {}", id);
        flightSliceRepository.delete(id);
        flightSliceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("flightSlice", id.toString())).build();
    }

    /**
     * SEARCH  /_search/flightSlices/:query -> search for the flightSlice corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/flightSlices/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FlightSlice> searchFlightSlices(@PathVariable String query) {
        log.debug("REST request to search FlightSlices for query {}", query);
        return StreamSupport
            .stream(flightSliceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
