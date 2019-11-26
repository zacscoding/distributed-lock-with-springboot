package demo.config.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import demo.constants.AppConstants.ClusterConstants;

/**
 * Determine component scan packages depends on cluster.type
 */
public class ClusterEnabledCondition {

    public static class StandAloneEnabledCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return ClusterConstants.STANDALONE_TYPE.equals(
                    context.getEnvironment().getProperty("cluster.type"));
        }
    }
}
