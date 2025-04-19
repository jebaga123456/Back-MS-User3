package com.exp.cl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties
@Data
public class JwtProperties {
    
	@Value("${springbootwebfluxjjwt.password.encoder.secret}")
	private String secret;

	@Value("${springbootwebfluxjjwt.password.encoder.iteration}")
	private int iteration;

	@Value("${springbootwebfluxjjwt.password.encoder.keylength}")
	private int keylength;
}