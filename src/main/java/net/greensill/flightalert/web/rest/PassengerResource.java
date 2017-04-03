package net.greensill.flightalert.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.greensill.flightalert.domain.Passenger;
import net.greensill.flightalert.domain.User;
import net.greensill.flightalert.repository.PassengerRepository;
import net.greensill.flightalert.repository.UserRepository;
import net.greensill.flightalert.repository.search.PassengerSearchRepository;
import net.greensill.flightalert.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Passenger.
 */
@RestController
@RequestMapping("/api")
public class PassengerResource {

    private final Logger log = LoggerFactory.getLogger(PassengerResource.class);
        
    @Inject
    private PassengerRepository passengerRepository;
    
    @Inject
    private PassengerSearchRepository passengerSearchRepository;

    @Inject
    private UserRepository userRepository;


    /**
     * POST  /passengers -> Create a new passenger.
     */
    @RequestMapping(value = "/passengers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Passenger> createPassenger(@Valid @RequestBody Passenger passenger, Principal principal) throws URISyntaxException {
        log.debug("REST request to save Passenger : {}", passenger);
        if (passenger.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("passenger", "idexists", "A new passenger cannot already have an ID")).body(null);
        }

        log.debug("principal to save passenger : {}", principal.getName());

        Optional<User> existingUser = userRepository.findOneByLogin(principal.getName());
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("passenger", "uknownuser", "A new passenger can only be created by a known user")).body(null);
        }
        passenger.setUserId(existingUser.get().getId());

        Passenger result = passengerRepository.save(passenger);
        passengerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/passengers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("passenger", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /passengers -> Updates an existing passenger.
     */
    @RequestMapping(value = "/passengers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Passenger> updatePassenger(@Valid @RequestBody Passenger passenger, Principal principal) throws URISyntaxException {
        log.debug("REST request to update Passenger : {}", passenger);
        if (passenger.getId() == null) {
            return createPassenger(passenger, principal);
        }

        log.debug("principal to update passenger : {}", principal.getName());

        Optional<User> existingUser = userRepository.findOneByLogin(principal.getName());
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("passenger", "uknownuser", "A new passenger can only be updated by a known user")).body(null);
        }
        passenger.setUserId(existingUser.get().getId());

        Passenger result = passengerRepository.save(passenger);
        passengerSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("passenger", passenger.getId().toString()))
            .body(result);
    }

    /**
     * GET  /passengers -> get all the passengers.
     */
    @RequestMapping(value = "/passengers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Passenger>> getAllPassengers(Principal principal) {
        log.debug("REST request to get all Passengers");

        log.debug("principal to update passenger : {}", principal.getName());

        Optional<User> existingUser = userRepository.findOneByLogin(principal.getName());
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("passenger", "uknownuser", "A new passenger can only be updated by a known user")).body(null);
        }

        List<Passenger> result = passengerRepository.findAllByUserId(existingUser.get().getId());
        return new ResponseEntity(result, HttpStatus.OK);

    }

    /**
     * GET  /passengers/:id -> get the "id" passenger.
     */
    @RequestMapping(value = "/passengers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Passenger> getPassenger(@PathVariable Long id) {
        log.debug("REST request to get Passenger : {}", id);
        Passenger passenger = passengerRepository.findOne(id);
        return Optional.ofNullable(passenger)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /passengers/:id -> delete the "id" passenger.
     */
    @RequestMapping(value = "/passengers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        log.debug("REST request to delete Passenger : {}", id);
        passengerRepository.delete(id);
        passengerSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("passenger", id.toString())).build();
    }

    /**
     * SEARCH  /_search/passengers/:query -> search for the passenger corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/passengers/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Passenger> searchPassengers(@PathVariable String query) {
        log.debug("REST request to search Passengers for query {}", query);
        return StreamSupport
            .stream(passengerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
