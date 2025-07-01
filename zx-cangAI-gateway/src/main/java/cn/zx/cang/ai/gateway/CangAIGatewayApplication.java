package cn.zx.cang.ai.gateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author kinchou
 * @since 2025/1/20 16:09
 */
@SpringBootApplication(scanBasePackages = "cn.zx.cang.ai.gateway")
public class CangAIGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(CangAIGatewayApplication.class, args);
    }
}
