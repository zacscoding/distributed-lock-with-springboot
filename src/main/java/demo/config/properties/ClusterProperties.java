package demo.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Common cluster properties
 */
@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "cluster")
public class ClusterProperties {

    private String id;
}
