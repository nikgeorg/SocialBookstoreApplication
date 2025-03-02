package com.example.socialonlinebookstore.config;

import com.example.socialonlinebookstore.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity

public class WebSecurityConfig {

    /*
     *
     * Authentication configuration
     *
     */

    @Autowired
    private CustomSecuritySuccessHandler customSecuritySuccessHandler;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /*
     * DaoAuthenticationProvider is an AuthenticationProvider implementation that uses
     * a UserDetailsService
     * and PasswordEncoder
     * to authenticate a username and password.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /*
     * Authorization configuration ....
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                (authz) -> authz
                        .requestMatchers("/", "/login", "/register", "/create-profile").permitAll()
                        .requestMatchers("/user/**").hasAnyAuthority("User")
                        .anyRequest().authenticated()
        );

        http.formLogin(fL -> fL.loginPage("/login")
                .defaultSuccessUrl("/profile", true)
                .failureUrl("/login?error=true")
                .successHandler(customSecuritySuccessHandler)
                .usernameParameter("username")
                .passwordParameter("password"));

        http.logout(logOut -> logOut.logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
        );

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }


}