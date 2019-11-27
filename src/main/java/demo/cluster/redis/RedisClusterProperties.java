package demo.cluster.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "cluster.redis")
public class RedisClusterProperties {

    private String address;
    private int idleConnectionTimeout;
}
