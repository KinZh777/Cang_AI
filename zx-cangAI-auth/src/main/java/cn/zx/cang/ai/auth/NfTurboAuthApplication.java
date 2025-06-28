package cn.zx.cang.ai.auth;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author kinchou
 */
@SpringBootApplication(scanBasePackages = {"cn.zx.cang.ai.auth"})
@EnableDubbo
public class NfTurboAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(NfTurboAuthApplication.class, args);
    }

}
