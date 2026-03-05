package org.intech.vehiclerental.configurations;

import jakarta.servlet.http.HttpServletResponse;
import org.intech.vehiclerental.services.CustomAccountDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    /**
     * "/api/vehicle/getall",
     * "/uploads/vehicles/**" are permitted for temporary basis only for testing purposes
     * Above endpoints will be removed from permitAll in future
     *
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {

                    CorsConfiguration config = new CorsConfiguration();

                    config.setAllowCredentials(true);
                    config.setAllowedOrigins(List.of("http://localhost:5173"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
                    config.setExposedHeaders(List.of("X-Auth-Token"));

                    return config;
                })) // enable CORS inside Spring Security
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/createaccount",
                                "/api/vehicle/detail/**",
                                "/uploads/vehicles/**"
                                ).permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll() // allow preflight
                        .anyRequest().authenticated() // Secure all other requests
                ).sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .sessionFixation().migrateSession()
                ).logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                );

        return http.build();
    }

    /**
     * Defines the hierarchical relationship between application roles.
     *
     * <p>
     * Role hierarchy allows higher-level roles to automatically inherit
     * the permissions of lower-level roles without explicitly assigning
     * multiple roles to a user.
     * </p>
     *
     * <p>
     * In this configuration:
     * <ul>
     *     <li>ROLE_COMPANY inherits ROLE_ADMIN</li>
     *     <li>ROLE_ADMIN inherits ROLE_INDIVIDUAL</li>
     * </ul>
     * </p>
     *
     * <p>
     * This means:
     * <ul>
     *     <li>A user with ROLE_COMPANY automatically has ROLE_ADMIN and ROLE_INDIVIDUAL privileges.</li>
     *     <li>A user with ROLE_ADMIN automatically has ROLE_INDIVIDUAL privileges.</li>
     * </ul>
     * </p>
     *
     * <p>
     * Internally, Spring Security expands the granted authorities at runtime
     * using this hierarchy before performing authorization checks.
     * </p>
     *
     * @return configured RoleHierarchy instance
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("""
            ROLE_COMPANY > ROLE_ADMIN
            ROLE_ADMIN > ROLE_INDIVIDUAL
        """);
    }


    /**
     * Configures the expression handler used for method-level security.
     *
     * <p>
     * Spring Security uses a MethodSecurityExpressionHandler to evaluate
     * security annotations such as:
     * </p>
     *
     * <ul>
     *     <li>@PreAuthorize</li>
     *     <li>@PostAuthorize</li>
     *     <li>@Secured</li>
     * </ul>
     *
     * <p>
     * This bean integrates the RoleHierarchy into method-level security
     * so that role inheritance is considered when evaluating expressions
     * like hasRole('ADMIN').
     * </p>
     *
     * <p>
     * Without this configuration, the role hierarchy would not be applied
     * to method-level security checks.
     * </p>
     *
     * @param roleHierarchy the configured role hierarchy
     * @return configured MethodSecurityExpressionHandler
     */
    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
            RoleHierarchy roleHierarchy) {

        DefaultMethodSecurityExpressionHandler handler =
                new DefaultMethodSecurityExpressionHandler();

        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }


    /**
     * Exposes the AuthenticationManager as a Spring Bean.
     *
     * <p>
     * AuthenticationManager is the core Spring Security component responsible
     * for processing authentication requests.
     * </p>
     *
     * <p>
     * It delegates authentication to configured AuthenticationProviders
     * (e.g., DaoAuthenticationProvider).
     * </p>
     *
     * <p>
     * In this application, it is used in the login endpoint to authenticate
     * a user based on email and password.
     * </p>
     *
     * @param configuration Spring's authentication configuration
     * @return configured AuthenticationManager
     * @throws Exception if authentication manager cannot be built
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    /**
     * Configures the DaoAuthenticationProvider.
     *
     * <p>
     * DaoAuthenticationProvider is responsible for authenticating users
     * using a UserDetailsService and a PasswordEncoder.
     * </p>
     *
     * <p>
     * Authentication process:
     * <ol>
     *     <li>Load user from database via CustomAccountDetailsService</li>
     *     <li>Compare provided password with stored password using PasswordEncoder</li>
     *     <li>If valid, return authenticated Authentication object</li>
     * </ol>
     * </p>
     *
     * <p>
     * This provider is automatically used by AuthenticationManager.
     * </p>
     *
     * @param userDetailsService custom user loader
     * @param passwordEncoder password encoder used for verification
     * @return configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomAccountDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "argon2";

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("bcrypt", new BCryptPasswordEncoder());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

}
