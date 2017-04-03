package net.greensill.flightalert.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.inject.Inject;

/**
 * Created by stevengreensill on 28/3/16.
 */
@Configuration
@Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
public class MailConfiguration {

    @Inject
    Environment environment;

    @Bean
    public JavaMailSender mailSender() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(environment.getProperty("spring.mail.host"));
        javaMailSender.setPort(Integer.valueOf(environment.getProperty("spring.mail.port")));
        javaMailSender.setUsername(environment.getProperty("spring.mail.username"));
        javaMailSender.setPassword(environment.getProperty("spring.mail.password"));

        return javaMailSender;
    }

}
