package org.simtp.example.starter.library;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(value = "org.simtp.exmaple.config.enabled", havingValue = "true", matchIfMissing = true)
public class ExampleConfiguration {

    // Create your Bean definitions here
    @Bean
    public MyBean myBean() {
        return new MyBean();
    }
}
