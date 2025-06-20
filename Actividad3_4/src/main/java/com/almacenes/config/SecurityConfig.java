package com.almacenes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/sedes/**", "/api/clientes/**", "/api/almacenes/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/clientes/**", "/api/almacenes/transaccion").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/clientes/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {});
        
        return http.build();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();
        
        UserDetails cliente = User.builder()
                .username("cliente")
                .password(passwordEncoder().encode("cliente123"))
                .roles("CLIENTE")
                .build();
        
        return new InMemoryUserDetailsManager(admin, cliente);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
