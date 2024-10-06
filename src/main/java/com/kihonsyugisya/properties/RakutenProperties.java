package com.kihonsyugisya.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class RakutenProperties {
    @Value("${rakuten.api.applicationId}")
    private String applicationId;
    
    @Value("${rakuten.api.affiliateId}")
    private String affiliateId;

    @Value("${rakuten.api.apiUrl}")
    private String apiUrl;
}
