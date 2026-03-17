package org.intech.vehiclerental.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import java.util.concurrent.ConcurrentHashMap;


@Configuration
//@EnableSpringHttpSession
@EnableJdbcHttpSession
public class SessionConfig {

//    @Bean
//    public MapSessionRepository sessionRepository() {
//        return new MapSessionRepository(new ConcurrentHashMap<>());
//    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return  HeaderHttpSessionIdResolver.xAuthToken();
    }

}