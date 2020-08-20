package org.jliv3.customer.order_manager.security;

import org.jliv3.customer.order_manager.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .headers(//xss protection
                        headers -> headers
                                .xssProtection(xss -> xss
                                        .block(false)
                                )
                )
                .authorizeRequests(// Restrict access to our application.
                        authorize -> authorize
                                .antMatchers("/css/**", "/img/**", "/js/**").permitAll()
                                .antMatchers("/users", "/trace", "/api/users").hasRole(Role.ADMIN)
                                .anyRequest().authenticated()
                )
                .formLogin()
                .and().exceptionHandling().accessDeniedPage("/403");

    }
}