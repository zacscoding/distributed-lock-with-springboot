package demo.cluster.standalone;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import demo.cluster.Cluster;
import demo.config.properties.ClusterProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StandAloneCluster implements Cluster {

    private final StandAloneClusterProperties properties;
    private final ClusterProperties clusterProperties;
    private final ConcurrentHashMap<String, TaskLock> semaphoreMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void setUp() {
        logger.info("## Enable standalone cluster. {}, {}", clusterProperties, properties);
    }

    @Override
    public String getId() {
        return clusterProperties.getId();
    }

    @Override
    public boolean acquireLock(String taskId, long timeout, TimeUnit timeUnit) {
        Assert.isTrue(timeout >= 0L, "timeout must greater than or equal to 0");
        requireNonNull(timeUnit, "timeUnit");

        if (!properties.isAlwaysAcquire()) {
            return false;
        }

        try {
            final TaskLock lock = getOrCreateSemaphore(taskId);

            if (lock.getSemaphore().tryAcquire(timeout, timeUnit)) {
                lock.onAcquired();
                return true;
            }

            return false;
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void releaseLock(String taskId) {
        TaskLock lock = semaphoreMap.remove(taskId);

        if (lock == null) {
            return;
        }

        if (lock.isAcquiredThread()) {
            logger.warn("Try to release with different thread");
        }

        lock.getSemaphore().release();
    }

    private TaskLock getOrCreateSemaphore(String taskId) {
        return semaphoreMap.computeIfAbsent(taskId, (key) -> new TaskLock());
    }

    private static class TaskLock {

        private final Semaphore semaphore;
        private String threadName;
        private Long threadId;
        private long acuiqredTime;

        public TaskLock() {
            this.semaphore = new Semaphore(1);
        }

        public Semaphore getSemaphore() {
            return semaphore;
        }

        public void onAcquired() {
            final Thread acquiredThread = Thread.currentThread();

            this.threadId = acquiredThread.getId();
            this.threadName = acquiredThread.getName();
            this.acuiqredTime = System.currentTimeMillis();
        }

        public boolean isAcquiredThread() {
            final Thread thread = Thread.currentThread();
            return threadName.equals(thread.getName()) && threadId == thread.getId();
        }
    }
}