package com.example.pruebatecnica;

import javax.validation.Validator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
public class PruebaTecnicaApplication {

  public static void main(String[] args) {
    SpringApplication.run(PruebaTecnicaApplication.class, args);
  }

  @Bean
  public Validator validator() {
    return new LocalValidatorFactoryBean();
  }

}
