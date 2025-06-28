package cn.zx.cang.ai.ratelimiting;


import cn.zx.cang.ai.limiter.SlidingWindowRateLimiter;
import cn.zx.cang.ai.limiter.configuration.RateLimiterConfiguration;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author kinchou
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RateLimiterTestConfiguration.class})
@ActiveProfiles("test")
public class SlidingWindowRateLimiterTest {

    @Autowired
    SlidingWindowRateLimiter slidingWindowRateLimiter;

    @Test
    @Ignore
    public void tryAcquire1() {
        Boolean result = slidingWindowRateLimiter.tryAcquire("testLock997", 3, 10);
        Assert.assertTrue(result);
        result = slidingWindowRateLimiter.tryAcquire("testLock997", 3, 10);
        Assert.assertTrue(result);
        result = slidingWindowRateLimiter.tryAcquire("testLock997", 3, 10);
        Assert.assertTrue(result);
        result = slidingWindowRateLimiter.tryAcquire("testLock997", 3, 10);
        Assert.assertFalse(result);

        try {
            Thread.currentThread().sleep(10000);
        }catch (Exception e){

        }
        result = slidingWindowRateLimiter.tryAcquire("testLock997", 3, 10);
        Assert.assertTrue(result);

    }

    @Test
    @Ignore
    public void tryAcquire() {
        Boolean result = slidingWindowRateLimiter.tryAcquire("testLock", 1, 5);
        Assert.assertTrue(result);
        Boolean result1 = slidingWindowRateLimiter.tryAcquire("testLock", 1, 3);
        Assert.assertFalse(result1);
        try {
            Thread.currentThread().sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Boolean result2 = slidingWindowRateLimiter.tryAcquire("testLock", 1, 1);
        Assert.assertFalse(result2);

        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Boolean result3 = slidingWindowRateLimiter.tryAcquire("testLock", 1, 1);
        Assert.assertTrue(result3);
    }
}