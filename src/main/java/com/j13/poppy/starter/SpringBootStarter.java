package com.j13.poppy.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;


@EnableAutoConfiguration
@ImportResource({"classpath:applicationContext.xml"})
@PropertySource({"classpath:application.properties"})
@SpringBootApplication
public class SpringBootStarter {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStarter.class, args);
    }

}
