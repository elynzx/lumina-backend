package com.lumina.luminabackend.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Holds configuration values for JWT tokens: secret key and expiration time.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /** Secret key used to sign and verify JWT tokens. */
    private String secret;

    /** Token expiration time in milliseconds. */
    private long expiration;
}