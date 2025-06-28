package cn.zx.cang.ai.order.infrastructure.config;

import cn.zx.cang.ai.api.collection.service.CollectionFacadeService;
import cn.zx.cang.ai.api.goods.service.GoodsFacadeService;
import cn.zx.cang.ai.api.pay.service.PayFacadeService;
import cn.zx.cang.ai.api.user.service.UserFacadeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kinchou
 */
@Configuration
public class OrderDubboConfiguration {

    @DubboReference(version = "1.0.0")
    private CollectionFacadeService collectionFacadeService;

    @DubboReference(version = "1.0.0")
    private UserFacadeService userFacadeService;

    @DubboReference(version = "1.0.0")
    private PayFacadeService payFacadeService;

    @DubboReference(version = "1.0.0")
    private GoodsFacadeService goodsFacadeService;

    @Bean
    @ConditionalOnMissingBean(name = "collectionFacadeService")
    public CollectionFacadeService collectionFacadeService() {
        return this.collectionFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "userFacadeService")
    public UserFacadeService userFacadeService() {
        return this.userFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "payFacadeService")
    public PayFacadeService payFacadeService() {
        return this.payFacadeService;
    }
    
    @Bean
    @ConditionalOnMissingBean(name = "goodsFacadeService")
    public GoodsFacadeService goodsFacadeService() {
        return this.goodsFacadeService;
    }
}
