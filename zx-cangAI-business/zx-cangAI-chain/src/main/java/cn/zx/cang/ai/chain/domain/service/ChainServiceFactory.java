package cn.zx.cang.ai.chain.domain.service;

import cn.zx.cang.ai.api.chain.constant.ChainType;
import cn.zx.cang.ai.base.utils.BeanNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChainServiceFactory {

    @Autowired
    private final Map<String, ChainService> chainServiceMap = new ConcurrentHashMap<>();

    public ChainService get(ChainType chainType) {
        String beanName = BeanNameUtils.getBeanName(chainType.name(), "ChainService");

        //组装出beanName，并从map中获取对应的bean
        ChainService service = chainServiceMap.get(beanName);

        if (service != null) {
            return service;
        } else {
            throw new UnsupportedOperationException(
                    "No ChainService Found With chainType : " + chainType);
        }
    }

}
