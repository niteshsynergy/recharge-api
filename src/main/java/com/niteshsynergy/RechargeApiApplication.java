package com.niteshsynergy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class RechargeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RechargeApiApplication.class, args);
    }

}
