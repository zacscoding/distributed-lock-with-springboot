package demo.cluster.zookeeper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
public class ZookeeperCluster implements Cluster {

    private final String sharedLockRootPath = "/lock/mutex";
    private final Map<String, InterProcessSemaphoreMutex> mutexMap = new HashMap<>();

    // required
    private final ZookeeperProperties zookeeperProperties;
    private final ClusterProperties clusterProperties;
    private final CuratorFramework curatorFramework;

    @PostConstruct
    private void setUp() {
        logger.info("## Enable zookeeper cluster. {}, {}", clusterProperties, zookeeperProperties);
    }

    @Override
    public String getId() {
        return clusterProperties.getId();
    }

    @Override
    public boolean acquireLock(String taskId, long timeout, TimeUnit timeUnit) {
        final InterProcessSemaphoreMutex mutex = createMutex(taskId);
        Assert.isTrue(timeout >= 0L, "timeout must greater than or equal to 0");

        try {
            if (mutex.acquire(timeout, timeUnit)) {
                mutexMap.put(taskId, mutex);
                return true;
            }

            return false;
        } catch (Exception e) {
            logger.warn("Exception occur while acquiring {} lock.", taskId, e);
            return false;
        }
    }

    @Override
    public void releaseLock(String taskId) {
        InterProcessSemaphoreMutex mutex = mutexMap.remove(taskId);

        if (mutex == null || !mutex.isAcquiredInThisProcess()) {
            logger.warn("this {} didn't acquired {} lock", clusterProperties.getId(), taskId);
            return;
        }

        try {
            mutex.release();
        } catch (Exception e) {
            logger.warn("Exception occur while releasing a {} lock in {}"
                    , taskId, clusterProperties.getId(), e);
        }
    }

    // returns a mutex given taskId
    private InterProcessSemaphoreMutex createMutex(String taskId) {
        final String path = sharedLockRootPath + "/" + taskId;
        return new InterProcessSemaphoreMutex(curatorFramework, path);
    }
}
