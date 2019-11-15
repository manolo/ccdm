package com.vaadin.ccdm.spring;

import javax.servlet.http.HttpServletRequest;

import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;

/**
 * The Spring Boot configuration for authentication part of the Vaadin Connect
 * based application. Specifies the way user details should be fetched when
 * authentication takes place.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("{noop}user")
                .roles("USER").and().withUser("admin")
                .password("{noop}admin").roles("ADMIN", "USER");
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    /**
     * Require login to access internal pages and configure login form.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Flow and connect already handles csrf.
        http.csrf().disable();
        // Allow all flow internal requests.
        http.authorizeRequests()
                .requestMatchers(SecurityConfig::isFrameworkInternalRequest)
                .permitAll();
        // using default spring login form
        http.formLogin();
    }

    /**
     * Allows access to static resources, bypassing Spring security.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                // Vaadin Flow static resources
                "/VAADIN/**");
    }

    /**
     * Tests if the request is an internal framework request. The test consists
     * of checking if the request parameter is present and if its value is
     * consistent with any of the request types know.
     *
     * @param request
     *            {@link HttpServletRequest}
     * @return true if is an internal framework request. False otherwise.
     */
    private static boolean isFrameworkInternalRequest(
            HttpServletRequest request) {
        final String parameterValue = request
                .getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
                && Stream.of(ServletHelper.RequestType.values()).anyMatch(
                        r -> r.getIdentifier().equals(parameterValue));
    }
}
