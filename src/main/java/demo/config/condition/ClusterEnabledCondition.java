package demo.config.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import demo.constants.AppConstants.ClusterConstants;

/**
 * Determine component scan packages depends on cluster.type
 */
public class ClusterEnabledCondition {

    public static abstract class AbstractClusterEnabledCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return getRequiredClusterType().equals(context.getEnvironment().getProperty("cluster.type"));
        }

        abstract String getRequiredClusterType();
    }

    /**
     * Condition of standalone
     */
    public static class StandAloneEnabledCondition extends AbstractClusterEnabledCondition {

        @Override
        String getRequiredClusterType() {
            return ClusterConstants.TYPE_STANDALONE;
        }
    }

    /**
     * Condition of zookeeper
     */
    public static class ZookeeperEnabledCondition extends AbstractClusterEnabledCondition {

        @Override
        String getRequiredClusterType() {
            return ClusterConstants.TYPE_ZOOKEEPER;
        }
    }
}
