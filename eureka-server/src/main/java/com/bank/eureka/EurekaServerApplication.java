package com.bank.eureka;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
/**
 * 服务注册中心（Eureka Server）
 * 所有微服务启动后都会来这里注册自己的名字和地址。
 * 启动后访问 http://localhost:8761 查看控制台。
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
