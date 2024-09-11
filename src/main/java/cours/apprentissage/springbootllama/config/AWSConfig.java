package cours.apprentissage.springbootllama.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

@Configuration
public class AWSConfig {
    @Value("${spring.ai.bedrock.aws.access-key}")
    private String accessKey;
    @Value("${spring.ai.bedrock.aws.secret-key}")
    private String secretKey;
    @Value("${spring.ai.bedrock.aws.region}")
    private String region;

    public AwsCredentialsProvider awsCredentialsProvider() {
        AwsCredentialsProvider awsCredentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
        return awsCredentialsProvider;
    }

    @Bean
    public BedrockRuntimeClient bedrockClient() {

        return BedrockRuntimeClient.builder().region(Region.of(region)).credentialsProvider(awsCredentialsProvider()).build();
    }

    @Bean
    public BedrockRuntimeAsyncClient bedrockAsyncClient() {

        return BedrockRuntimeAsyncClient.builder().region(Region.of(region)).credentialsProvider(awsCredentialsProvider()).build();
    }
}
