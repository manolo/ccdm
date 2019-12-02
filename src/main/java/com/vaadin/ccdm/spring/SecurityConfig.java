package com.vaadin.ccdm.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * The Spring Boot configuration for authentication part of the Vaadin Connect
 * based application. Specifies the way user details should be fetched when
 * authentication takes place.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      // Vaadin already handles csrf.
      http.csrf().disable();
      // Use default spring login form
      http.formLogin();
    }

    @Override
    public void configure(WebSecurity web) {
      // Access to static resources, bypassing Spring security.
      web.ignoring().antMatchers("/VAADIN/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      // Configure users and roles in memory
      auth.inMemoryAuthentication()
          .withUser("user").password("{noop}user").roles("USER")
          .and()
          .withUser("admin").password("{noop}admin").roles("ADMIN", "USER");
    }
}
