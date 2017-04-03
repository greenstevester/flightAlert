package net.greensill.flightalert.repository.search;

import net.greensill.flightalert.domain.FlightRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FlightRequest entity.
 */
public interface FlightRequestSearchRepository extends ElasticsearchRepository<FlightRequest, Long> {
}
