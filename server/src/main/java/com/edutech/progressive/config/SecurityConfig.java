package com.edutech.progressive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.edutech.progressive.jwt.JwtRequestFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtRequestFilter jwtRequestFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter,
                          UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Use our custom UserDetailsService with encoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Stateless + disable CSRF for API
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Return 401 when not authenticated, 403 when authenticated but not authorized
        AuthenticationEntryPoint unauthorizedEntryPoint = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
        AccessDeniedHandler accessDeniedHandler = (request, response, ex) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        };
        http.exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        // Public endpoints
        http.authorizeRequests()
                .antMatchers("/auth/**").permitAll()

                // ===== Role/Authority rules expected by Day 12 tests =====
                // Doctor management: only DOCTOR can create/update/delete doctors
                .antMatchers(HttpMethod.POST, "/doctor/**").hasAuthority("DOCTOR")
                .antMatchers(HttpMethod.PUT,  "/doctor/**").hasAuthority("DOCTOR")
                .antMatchers(HttpMethod.DELETE,"/doctor/**").hasAuthority("DOCTOR")

                // Read endpoints can be open or authenticated; keep conservative:
                .antMatchers(HttpMethod.GET, "/doctor/**").authenticated()

                // Clinic: assume DOCTOR manages clinics (create/update/delete)
                .antMatchers(HttpMethod.POST, "/clinic/**").hasAuthority("DOCTOR")
                .antMatchers(HttpMethod.PUT,  "/clinic/**").hasAuthority("DOCTOR")
                .antMatchers(HttpMethod.DELETE,"/clinic/**").hasAuthority("DOCTOR")
                .antMatchers(HttpMethod.GET, "/clinic/**").authenticated()

                // Patient read/update are authenticated; adjust if Day tests require otherwise
                .antMatchers(HttpMethod.GET, "/patient/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/patient/**").authenticated()
                .antMatchers(HttpMethod.POST, "/patient/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/patient/**").authenticated()

                // Everything else must be authenticated
                .anyRequest().authenticated();

        // Add JWT filter before username/password filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}