package cn.zx.cang.ai.limiter;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

/**
 * 滑动窗口限流服务
 *
 * @author kinchou
 */
public class SlidingWindowRateLimiter implements RateLimiter {

    private RedissonClient redissonClient;

    private static final String LIMIT_KEY_PREFIX = "nft:turbo:limit:";

    public SlidingWindowRateLimiter(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public Boolean tryAcquire(String key, int limit, int windowSize) {
        RRateLimiter rRateLimiter = redissonClient.getRateLimiter(LIMIT_KEY_PREFIX + key);
        //判断当前窗口是否存在
        if (!rRateLimiter.isExists()) {
            //如果不存在 针对不同的业务设置限流窗口
            rRateLimiter.trySetRate(RateType.PER_CLIENT, limit, windowSize, RateIntervalUnit.SECONDS);
        }

        return rRateLimiter.tryAcquire();
    }
}
