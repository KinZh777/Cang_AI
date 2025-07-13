package cn.zx.cang.ai.bot.token;

import cn.hutool.core.lang.Assert;
import cn.zx.cang.ai.bot.BotService;
import com.coze.openapi.client.auth.OAuthToken;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static cn.zx.cang.ai.bot.constant.CozeConstant.COZE_TOKEN_PREFIX;

@Aspect
@Component
@Order(Integer.MIN_VALUE)
public class BotTokenAspect {



    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private BotService botService;

    @Before("@annotation(cn.zx.cang.ai.bot.token.BotToken)")
    public void before(JoinPoint joinPoint) throws Exception {

    }
}
