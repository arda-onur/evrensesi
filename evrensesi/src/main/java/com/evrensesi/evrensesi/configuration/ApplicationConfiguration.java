package com.evrensesi.evrensesi.configuration;

import com.evrensesi.evrensesi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration implements WebMvcConfigurer {
 private final UserRepository userRepository;

     @Bean
    public UserDetailsService userDetailsService() {
     return username -> userRepository.findUserByUsername(username)
             .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
   @Bean
   public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
       DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
       provider.setPasswordEncoder(passwordEncoder);
       return provider;
   }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
         return configuration.getAuthenticationManager();

    }
}
