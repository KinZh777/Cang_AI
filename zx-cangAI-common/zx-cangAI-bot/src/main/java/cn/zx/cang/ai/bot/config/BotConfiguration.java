package cn.zx.cang.ai.bot.config;

import cn.zx.cang.ai.bot.BotService;
import cn.zx.cang.ai.bot.CozeServiceImpl;
import cn.zx.cang.ai.bot.token.BotTokenAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BotProperties.class)
public class BotConfiguration {

    @Autowired
    private BotProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = BotProperties.PREFIX, name = "enabled", havingValue = "true")
    public BotService cozeService(){
        CozeServiceImpl cozeService = new CozeServiceImpl();
        cozeService.setClientID(properties.getClientId());
        cozeService.setPublicKey(properties.getPublicKey());
        return cozeService;
    }

    @Bean
    public BotTokenAspect botTokenAspect(){
        return new BotTokenAspect();
    }
}
