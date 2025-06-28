package cn.zx.cang.ai.order.infrastructure.id;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author kinchou
 */
@Component
public class WorkerIdHolder implements CommandLineRunner {

    @Autowired
    private RedissonClient redissonClient;

    public static long WORKER_ID;

    @Override
    public void run(String... args) throws Exception {
        RAtomicLong atomicLong = redissonClient.getAtomicLong("workerId");
        WORKER_ID = atomicLong.incrementAndGet() % 32;
    }
}
