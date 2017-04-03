package net.greensill.flightalert.repository.search;

import net.greensill.flightalert.domain.PermittedCarrier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PermittedCarrier entity.
 */
public interface PermittedCarrierSearchRepository extends ElasticsearchRepository<PermittedCarrier, Long> {
}
