package net.greensill.flightalert.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.greensill.flightalert.domain.Passenger;
import net.greensill.flightalert.domain.PermittedCarrier;
import net.greensill.flightalert.domain.User;
import net.greensill.flightalert.repository.PermittedCarrierRepository;
import net.greensill.flightalert.repository.UserRepository;
import net.greensill.flightalert.repository.search.PermittedCarrierSearchRepository;
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
 * REST controller for managing PermittedCarrier.
 */
@RestController
@RequestMapping("/api")
public class PermittedCarrierResource {

    private final Logger log = LoggerFactory.getLogger(PermittedCarrierResource.class);
        
    @Inject
    private PermittedCarrierRepository permittedCarrierRepository;
    
    @Inject
    private PermittedCarrierSearchRepository permittedCarrierSearchRepository;

    @Inject
    private UserRepository userRepository;
    
    /**
     * POST  /permittedCarriers -> Create a new permittedCarrier.
     */
    @RequestMapping(value = "/permittedCarriers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PermittedCarrier> createPermittedCarrier(@Valid @RequestBody PermittedCarrier permittedCarrier, Principal principal) throws URISyntaxException {
        log.debug("REST request to save PermittedCarrier : {}", permittedCarrier);
        if (permittedCarrier.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("permittedCarrier", "idexists", "A new permittedCarrier cannot already have an ID")).body(null);
        }

        log.debug("principal to save PermittedCarrier : {}", principal.getName());

        Optional<User> existingUser = userRepository.findOneByLogin(principal.getName());
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("permittedCarrier", "uknownuser", "A new permittedCarrier can only be created by a known user")).body(null);
        }
        permittedCarrier.setUserId(existingUser.get().getId());

        PermittedCarrier result = permittedCarrierRepository.save(permittedCarrier);
        permittedCarrierSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/permittedCarriers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("permittedCarrier", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /permittedCarriers -> Updates an existing permittedCarrier.
     */
    @RequestMapping(value = "/permittedCarriers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PermittedCarrier> updatePermittedCarrier(@Valid @RequestBody PermittedCarrier permittedCarrier, Principal principal) throws URISyntaxException {
        log.debug("REST request to update PermittedCarrier : {}", permittedCarrier);
        if (permittedCarrier.getId() == null) {
            return createPermittedCarrier(permittedCarrier, principal);
        }

        log.debug("principal to update permittedCarrier : {}", principal.getName());

        Optional<User> existingUser = userRepository.findOneByLogin(principal.getName());
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("permittedCarrier", "uknownuser", "A new permittedCarrier can only be updated by a known user")).body(null);
        }
        permittedCarrier.setUserId(existingUser.get().getId());

        PermittedCarrier result = permittedCarrierRepository.save(permittedCarrier);
        permittedCarrierSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("permittedCarrier", permittedCarrier.getId().toString()))
            .body(result);
    }

    /**
     * GET  /permittedCarriers -> get all the permittedCarriers.
     */
    @RequestMapping(value = "/permittedCarriers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PermittedCarrier>> getAllPermittedCarriers(Principal principal) {
        log.debug("REST request to get all PermittedCarriers");

        log.debug("principal to update permittedCarrier : {}", principal.getName());

        Optional<User> existingUser = userRepository.findOneByLogin(principal.getName());
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("permittedCarrier", "uknownuser", "A new permittedCarrier can only be updated by a known user")).body(null);
        }

        List<PermittedCarrier> result = permittedCarrierRepository.findAllByUserId(existingUser.get().getId());
        return new ResponseEntity(result, HttpStatus.OK);

    }

    /**
     * GET  /permittedCarriers/:id -> get the "id" permittedCarrier.
     */
    @RequestMapping(value = "/permittedCarriers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PermittedCarrier> getPermittedCarrier(@PathVariable Long id) {
        log.debug("REST request to get PermittedCarrier : {}", id);
        PermittedCarrier permittedCarrier = permittedCarrierRepository.findOne(id);
        return Optional.ofNullable(permittedCarrier)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /permittedCarriers/:id -> delete the "id" permittedCarrier.
     */
    @RequestMapping(value = "/permittedCarriers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePermittedCarrier(@PathVariable Long id) {
        log.debug("REST request to delete PermittedCarrier : {}", id);
        permittedCarrierRepository.delete(id);
        permittedCarrierSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("permittedCarrier", id.toString())).build();
    }

    /**
     * SEARCH  /_search/permittedCarriers/:query -> search for the permittedCarrier corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/permittedCarriers/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PermittedCarrier> searchPermittedCarriers(@PathVariable String query) {
        log.debug("REST request to search PermittedCarriers for query {}", query);
        return StreamSupport
            .stream(permittedCarrierSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
