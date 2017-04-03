package net.greensill.flightalert.repository.search;

import net.greensill.flightalert.domain.Passenger;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Passenger entity.
 */
public interface PassengerSearchRepository extends ElasticsearchRepository<Passenger, Long> {
}
