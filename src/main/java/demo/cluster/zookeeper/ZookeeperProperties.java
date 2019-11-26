package demo.cluster.zookeeper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Zookeeper properties
 */
@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "cluster.zookeeper")
public class ZookeeperProperties {

    private String connectString;
    private int maxRetries;
    private int sleepMsBetweenRetries;
}
