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

    /**
     * This method will enable fetching session from
     * request header with key named X-Auth-Token instead of traditional cookie based method which will
     * contain unique session id required for authorization of user
     * <br/>
     *
     * For more details visit - <a href="https://docs.spring.io/spring-session/docs/current/api/org/springframework/session/web/http/HttpSessionIdResolver.html">Official doc 1</a>
     *                        - <a href="https://docs.spring.io/spring-session/docs/current/api/org/springframework/session/web/http/HeaderHttpSessionIdResolver.html">Official doc 2</a>
     *
     */
    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return  HeaderHttpSessionIdResolver.xAuthToken();
    }

}