package cn.zx.cang.ai.collection;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cn.zx.cang.ai.collection")
@MapperScan("cn.zx.cang.ai.collection.infrastructure")
public class CangAICollectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(CangAICollectionApplication.class, args);
    }
}
