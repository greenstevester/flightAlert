package net.greensill.flightalert.config;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Properties specific to JHipster.
 *
 * <p>
 *     Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "spring.jpa", ignoreUnknownFields = true)
public class SpringDataJpaProperties extends JpaProperties {

    private JpaProperties.Hibernate hibernate;

    private Map<String, String> properties = new HashMap();

    public SpringDataJpaProperties() {
        super();
        properties = super.getProperties();

        properties.put("hibernate.cache.use_second_level_cache", "true");
        properties.put("hibernate.cache.use_query_cache:", "true");
        properties.put("hibernate.generate_statistics", "false");
        properties.put("hibernate.cache.region.factory_class", "net.greensill.flightalert.config.hazelcast.HazelcastCacheRegionFactory");
        properties.put("hibernate.cache.use_minimal_puts", "true");
        properties.put("hibernate.cache.hazelcast.use_lite_member", "true");
    }

}
