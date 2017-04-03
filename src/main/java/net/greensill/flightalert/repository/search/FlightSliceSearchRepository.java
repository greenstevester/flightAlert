package net.greensill.flightalert.repository.search;

import net.greensill.flightalert.domain.FlightSlice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FlightSlice entity.
 */
public interface FlightSliceSearchRepository extends ElasticsearchRepository<FlightSlice, Long> {
}
