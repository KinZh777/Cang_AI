package cn.zx.cang.ai.admin;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author kinchou
 */
@SpringBootApplication(scanBasePackages = {"cn.zx.cang.ai.admin"})
@EnableDubbo
public class NfTurboAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(NfTurboAdminApplication.class, args);
    }

}
