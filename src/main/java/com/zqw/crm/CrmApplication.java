package com.zqw.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zqw.crm.mapper")
public class CrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
        System.out.println("\n=======================================================");
        System.out.println("   CRM Backend Application Started Successfully!");
        System.out.println("   Swagger Docs: http://localhost:8080/doc.html");
        System.out.println("=======================================================\n");
    }
}
