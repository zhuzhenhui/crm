package cn.wolfcode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "cn.wolfcode.mapper")
public class CrmApp {

    public static void main(String[] args) {
        SpringApplication.run(CrmApp.class, args);
    }
}
