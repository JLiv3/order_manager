package org.jliv3.customer.order_manager.security;

import org.jliv3.customer.order_manager.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

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
//                .requiresChannel(//Redirect to HTTPS
//                        channel -> channel.anyRequest().requiresSecure()
//                )
                .headers().disable()
                .authorizeRequests(// Restrict access to our application.
                        authorize -> authorize
                                .antMatchers("/css/**", "/img/**", "/js/**").permitAll()
                                .antMatchers(HttpMethod.PUT, "/api/orders").hasAnyRole(Role.getRoles())
                                .antMatchers(HttpMethod.DELETE, "/api/orders").hasRole(Role.ADMIN)
                                .antMatchers(HttpMethod.PUT, "/api/orders/toggleChecked").hasRole(Role.ADMIN)
                                .antMatchers("/users", "/trace", "/api/users").hasRole(Role.ADMIN)
                                .anyRequest().authenticated()
                )
                .formLogin()
                .and().exceptionHandling().accessDeniedPage("/403");

    }

    @Bean//for session destroyed handler
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}