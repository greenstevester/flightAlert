package net.greensill.flightalert.config;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Created by stevengreensill on 28/3/16.
 */
@Configuration
@Profile(Constants.SPRING_PROFILE_CLOUD)
public class CloudMailConfiguration extends AbstractCloudConfig {
    @Bean
    public JavaMailSender mailSender() {
        return connectionFactory().service(JavaMailSender.class);
    }
}
