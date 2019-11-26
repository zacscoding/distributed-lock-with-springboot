package demo.cluster;

import java.util.concurrent.TimeUnit;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
public interface Cluster {

    /**
     * Returns a cluster id
     */
    String getId();

    /**
     * Acquire a lock given taskId with time limit
     *
     * @return true if acquire lock, otherwise false.
     */
    boolean acquireLock(String taskId, long timeout, TimeUnit timeUnit);

    /**
     * Release a lock given taskId
     */
    void releaseLock(String taskId);
}
