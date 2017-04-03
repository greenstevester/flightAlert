package net.greensill.flightalert.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.config.java.ServiceConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile(Constants.SPRING_PROFILE_CLOUD)
public class CloudDatabaseConfiguration extends AbstractCloudConfig {

    private final Logger log = LoggerFactory.getLogger(CloudDatabaseConfiguration.class);


    @Bean(destroyMethod = "close")
    public DataSource dataSource(JHipsterProperties jHipsterProperties, CacheManager cacheManager) {


        log.info("------------------ CLOUD dataSource START -------------------------");

        DataSource ds = null;

        ServiceConnectionFactory serviceConnectionFactory = connectionFactory();
        ds = serviceConnectionFactory.dataSource();

        log.info(Constants.LINE_SEPARATOR);

        if (ds instanceof org.apache.tomcat.jdbc.pool.DataSource) {
            org.apache.tomcat.jdbc.pool.DataSource pooledDs = (org.apache.tomcat.jdbc.pool.DataSource) ds;
            pooledDs.setMaxActive(jHipsterProperties.getDatasource().getMaximumPoolSize());
            log.info("maximumPoolSize: {}", pooledDs.getMaxActive());
        }

        log.info("------------------ CLOUD dataSource END -------------------------");
        return ds;

    }
}
