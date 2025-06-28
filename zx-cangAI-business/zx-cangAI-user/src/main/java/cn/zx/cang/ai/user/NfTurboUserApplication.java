package cn.zx.cang.ai.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author kinchou
 */
@SpringBootApplication(scanBasePackages = "cn.zx.cang.ai.user")
@EnableDubbo
public class NfTurboUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(NfTurboUserApplication.class, args);
    }

}
