package demo.config.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import demo.constants.AppConstants.ClusterConstants;

/**
 * Determine component scan packages depends on cluster.type
 */
public class ClusterCondition {

    public static abstract class AbstractClusterCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return getRequiredClusterType().equals(context.getEnvironment().getProperty("cluster.type"));
        }

        abstract String getRequiredClusterType();
    }

    /**
     * Condition of standalone
     */
    public static class StandAloneClusterCondition extends AbstractClusterCondition {

        @Override
        String getRequiredClusterType() {
            return ClusterConstants.TYPE_STANDALONE;
        }
    }

    /**
     * Condition of zookeeper
     */
    public static class ZookeeperClusterCondition extends AbstractClusterCondition {

        @Override
        String getRequiredClusterType() {
            return ClusterConstants.TYPE_ZOOKEEPER;
        }
    }

    public static class RedisClusterCondition extends AbstractClusterCondition {

        @Override
        String getRequiredClusterType() {
            return ClusterConstants.TYPE_REDIS;
        }
    }
}
