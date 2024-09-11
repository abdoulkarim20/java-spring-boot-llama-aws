package cours.apprentissage.springbootllama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootLlamaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLlamaApplication.class, args);
        System.setProperty("software.amazon.awssdk.logging", "debug");
    }

}
