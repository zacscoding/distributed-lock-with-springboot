package demo;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisConsoleTests {

    Logger logger = LoggerFactory.getLogger(RedisConsoleTests.class);

    @BeforeEach
    public void setUp() {
        LogLevelTestUtil.setWarn();
    }

    @Test
    public void runTests() throws Exception {
        final String taskId = "task1";

        for (int i = 1; i <= 2; i++) {
            final String id = i == 1 ? "AAA" : "BBB";

            Thread task = new Thread(() -> {
                final Config config = new Config();
                config.useSingleServer()
                      .setAddress("redis://192.168.79.130:6379");
                final RedissonClient redisson = Redisson.create(config);
                final RLock lock = redisson.getLock(taskId);
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        final Random random = new Random();

                        // wait
                        int sleepSeconds = random.nextInt(10) + 1;
                        TimeUnit.SECONDS.sleep(sleepSeconds);

                        // failed to acquire lock
                        logger.warn("[{}] Try to acquire {} lock", id, taskId);

                        if (!lock.tryLock(5L, -1, TimeUnit.SECONDS)) {
                            logger.warn("[{}] failed to acquire {} lock", id, taskId);
                            continue;
                        }

                        try {
                            // do task
                            sleepSeconds = random.nextInt(10) + 1;
                            logger.warn(">>>>>>>>>> [{}] acquire {} lock. start to do task during {} seconds."
                                    , id, taskId, sleepSeconds);
                            TimeUnit.SECONDS.sleep(sleepSeconds);
                        } finally {
                            // release lock
                            logger.warn("<<<<<<<<<< [{}] release {} lock. {} - {}"
                                    , id, taskId, lock.isLocked(), lock.isHeldByCurrentThread());
                            lock.unlock();
                        }
                    }
                } catch (Exception e) {
                    logger.warn("Exception occur while doing task[{}]", taskId, e);
                    redisson.shutdown();
                }
            });

            task.setDaemon(true);
            task.start();
        }

        TimeUnit.MINUTES.sleep(5L);
    }
}
