package com.ttalkak.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

@Configuration
public class AWSConfig {
    @Value("${aws.access-key}")
    private String accessKey;

    @Value("${aws.secret-key}")
    private String secretKey;

//    @Value("${aws.region}")
//    private String region;

    @Bean
    public StaticCredentialsProvider awsCredentialsProvider() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return StaticCredentialsProvider.create(credentials);
    }

    @Bean
    public BedrockRuntimeClient bedrockClient(StaticCredentialsProvider provider) {
        return BedrockRuntimeClient.builder()
                .region(Region.AP_NORTHEAST_1)
                .credentialsProvider(provider)
                .build();
    }

}
