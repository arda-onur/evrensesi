package com.arda.evrensesi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class PasswordEncryption {
    @Bean
    public PasswordEncoder passwordEncoder(){
       return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
