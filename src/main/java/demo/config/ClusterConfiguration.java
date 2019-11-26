package demo.config;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import demo.cluster.zookeeper.ZookeeperProperties;
import demo.config.condition.ClusterEnabledCondition.StandAloneEnabledCondition;
import demo.config.condition.ClusterEnabledCondition.ZookeeperEnabledCondition;
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
                ClusterConstants.TYPE_STANDALONE
                , ClusterConstants.TYPE_ZOOKEEPER
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

    @Configuration
    @Conditional(value = ZookeeperEnabledCondition.class)
    @ComponentScan("demo.cluster.zookeeper") // enable zookeeper package
    public static class ZookeeperConfiguration {

        @Autowired
        private ZookeeperProperties zookeeperProperties;

        @Bean
        public CuratorFramework curatorFramework() {
            RetryPolicy retryPolicy = new RetryNTimes(
                    zookeeperProperties.getMaxRetries(),
                    zookeeperProperties.getSleepMsBetweenRetries());

            CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperProperties.getConnectString(),
                                                                        retryPolicy);

            client.start();
            return client;
        }
    }
}
