package demo.cluster.standalone;

import static java.util.Objects.requireNonNull;

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

        try {
            if (properties.isAlwaysAcquire()) {
                return true;
            }

            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
        }

        return false;
    }

    @Override
    public void releaseLock(String taskId) {
        // do nothing
    }
}