package demo.task;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import demo.cluster.Cluster;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class Tasks {

    private final String[] taskIds = {
            "task01", "task02", "task01"
    };

    private final Cluster cluster;

    @PostConstruct
    private void setUp() {
        for (final String taskId : taskIds) {
            Thread task = new Thread(() -> {
                try {
                    final String id = cluster.getId() + "-" + Thread.currentThread().getName();
                    logger.info("Start {}", id);

                    while (!Thread.currentThread().isInterrupted()) {
                        final Random random = new Random();

                        // wait
                        int sleepSeconds = random.nextInt(10) + 1;
                        TimeUnit.SECONDS.sleep(sleepSeconds);

                        logger.info("[{}] try to acquire {} lock.", id, taskId);
                        // failed to acquire lock
                        if (!cluster.acquireLock(taskId, 5L, TimeUnit.SECONDS)) {
                            logger.info("[{}] failed to acquire {} lock", id, taskId);
                            continue;
                        }

                        try {
                            // do task
                            sleepSeconds = random.nextInt(10) + 1;
                            logger.info(">>>>>>>>>> [{}] acquire {} lock. start to do task during {} seconds."
                                    , id, taskId, sleepSeconds);
                            TimeUnit.SECONDS.sleep(sleepSeconds);
                        } finally {
                            // release lock
                            logger.info("<<<<<<<<<< [{}] release {} lock.", id, taskId);
                            cluster.releaseLock(taskId);
                        }
                    }
                } catch (Exception e) {
                    logger.warn("Exception occur while doing task[{}]", taskId, e);
                }
            });
            task.setDaemon(true);
            task.start();
        }
    }
}
