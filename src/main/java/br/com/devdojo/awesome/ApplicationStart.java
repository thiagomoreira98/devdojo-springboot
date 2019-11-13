package br.com.devdojo.awesome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// @EnableAutoConfiguration // configura as dependencias
// @ComponentScan // Scannear os components, repositories, services
// @Configuration //  passa as config do xml para objetos JAVA
@SpringBootApplication // mesma coisa que importar os 3 de cima
public class ApplicationStart {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStart.class, args);
    }
}
