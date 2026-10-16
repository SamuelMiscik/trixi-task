package cz.miscik.trixi_task_spring.cacheConfig;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Hazelcast caching.
 * Defines the Hazelcast instance and cache maps used by the application.
 */
@Configuration
public class HazelcastConfig {

    /**
     * Creates Hazelcast configuration with caches for villages
     * and village parts.
     *
     * @return Hazelcast configuration
     */
    @Bean
    public Config hazelcastConfig() {
        return new Config()
                .setInstanceName("trixi-hazelcast")

                // Cache map for obce
                .addMapConfig(
                        new MapConfig()
                                .setName("obce")
                                .setTimeToLiveSeconds(300) // TTL=5 minutes
                )

                // Cache map for allObce
                .addMapConfig(
                        new MapConfig()
                                .setName("allObce")
                                .setTimeToLiveSeconds(300)
                )

                // Cache map for castiObce
                .addMapConfig(
                        new MapConfig()
                                .setName("castiObce")
                                .setTimeToLiveSeconds(300)
                )

                // Cache map for allCastiObce
                .addMapConfig(
                        new MapConfig()
                                .setName("allCastiObce")
                                .setTimeToLiveSeconds(300)
                );
    }
}