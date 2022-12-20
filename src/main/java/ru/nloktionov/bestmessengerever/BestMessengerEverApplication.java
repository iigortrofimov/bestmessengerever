package ru.nloktionov.bestmessengerever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.nloktionov.bestmessengerever.config.security.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class BestMessengerEverApplication {

    public static void main(String[] args) {
        SpringApplication.run(BestMessengerEverApplication.class, args);
    }

}
