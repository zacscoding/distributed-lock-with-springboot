package demo.config;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import demo.cluster.redis.RedisClusterProperties;
import demo.cluster.zookeeper.ZookeeperProperties;
import demo.config.condition.ClusterCondition.RedisClusterCondition;
import demo.config.condition.ClusterCondition.StandAloneClusterCondition;
import demo.config.condition.ClusterCondition.ZookeeperClusterCondition;
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
                , ClusterConstants.TYPE_REDIS
        );

        final String clusterType = environment.getProperty("cluster.type");

        if (clusterTypes.indexOf(clusterType) < 0) {
            throw new UnsupportedOperationException("Not supported cluster type " + clusterType);
        }
    }

    @Configuration
    @Conditional(value = StandAloneClusterCondition.class)
    @ComponentScan("demo.cluster.standalone") // enable standalone package
    public static class StandAloneConfiguration {
    }

    @Configuration
    @Conditional(value = ZookeeperClusterCondition.class)
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

    @Configuration
    @Conditional(value = RedisClusterCondition.class)
    @ComponentScan("demo.cluster.redis") // enable zookeeper package
    public static class RedisConfiguration {

        @Autowired
        private RedisClusterProperties redisClusterProperties;

        @Bean(destroyMethod = "shutdown")
        public RedissonClient redissonClient() {
            String[] nodes = redisClusterProperties.getAddress().split(",");

            if (nodes == null || nodes.length == 0) {
                throw new RuntimeException("Invalid redis address : " + redisClusterProperties.getAddress());
            }

            Config redissonConfig = new Config();

            if (nodes.length == 1) {
                redissonConfig.useSingleServer().setAddress(nodes[0]);
            } else {
                redissonConfig.useClusterServers()
                              .addNodeAddress(nodes);
            }

            return Redisson.create(redissonConfig);
        }
    }
}
