package com.kihonsyugisya.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class TwitterProperties {
    @Value("${twitter.api.key}")
    private String apiKey;

    @Value("${twitter.api.secret.key}")
    private String apiSecretKey;

    @Value("${twitter.access.token}")
    private String accessToken;

    @Value("${twitter.access.token.secret}")
    private String accessTokenSecret;

}
