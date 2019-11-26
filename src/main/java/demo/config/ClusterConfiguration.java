package demo.config;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import demo.config.condition.ClusterEnabledCondition.StandAloneEnabledCondition;
import demo.constants.AppConstants.ClusterConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ClusterConfiguration {

    @Autowired
    private final Environment environment;

    @PostConstruct
    public void setUp() {
        List<String> clusterTypes = Arrays.asList(
                ClusterConstants.STANDALONE_TYPE
        );

        final String clusterType = environment.getProperty("cluster.type");

        if (clusterTypes.indexOf(clusterType) < 0) {
            throw new UnsupportedOperationException("Not supported cluster type " + clusterType);
        }
    }

    @Configuration
    @Conditional(value = StandAloneEnabledCondition.class)
    @ComponentScan("demo.cluster.standalone") // enable standalone package
    public static class StandAloneConfiguration {
    }
}
