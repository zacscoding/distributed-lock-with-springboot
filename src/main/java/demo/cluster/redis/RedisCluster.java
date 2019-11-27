package demo.cluster.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import demo.cluster.Cluster;
import demo.config.properties.ClusterProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCluster implements Cluster {

    private final Map<String, RLock> locksMap = new HashMap<>();

    // required
    private final RedisClusterProperties properties;
    private final ClusterProperties clusterProperties;
    private final RedissonClient redissonClient;

    @PostConstruct
    private void setUp() {
        logger.info("## Enable redis cluster. {}, {}", clusterProperties, properties);
    }

    @Override
    public String getId() {
        return clusterProperties.getId();
    }

    @Override
    public boolean acquireLock(String taskId, long timeout, TimeUnit timeUnit) {
        RLock rLock = locksMap.get(taskId);

        if (rLock == null) {
            rLock = redissonClient.getLock(taskId);
            locksMap.put(taskId, rLock);
        }

        try {
            return rLock.tryLock(timeout, -1, timeUnit);
        } catch (Exception e) {
            logger.warn("Exception occur while acquiring {} lock.", taskId, e);
            return false;
        }
    }

    @Override
    public void releaseLock(String taskId) {
        RLock rLock = locksMap.get(taskId);

        if (rLock == null) {
            return;
        }

        if (!rLock.isLocked() || !rLock.isHeldByCurrentThread()) {
            logger.warn("current thread doesn't hold a {} lock", taskId);
            return;
        }

        rLock.unlock();
    }
}
