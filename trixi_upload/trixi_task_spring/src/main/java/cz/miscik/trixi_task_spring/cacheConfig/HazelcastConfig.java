package cz.miscik.trixi_task_spring.cacheConfig;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
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
    public Config hazelcastConfiguration() {
        return new Config()
                .setInstanceName("trixi-hazelcast")

                // Cache map for obce
                .addMapConfig(
                        new MapConfig()
                                .setName("obce")
                                .setMaxIdleSeconds(300)// I almost used setTimeToLiveSeconds(300) BUT that was not what i wanted because i wanted it to be cached based on how frequent the use is and not every 5 mins recaching it even if its used very much
                )

                // Cache map for allObce
                .addMapConfig(
                        new MapConfig()
                                .setName("allObce")
                                .setMaxIdleSeconds(300)
                )

                // Cache map for castiObce
                .addMapConfig(
                        new MapConfig()
                                .setName("castiObce")
                                .setMaxIdleSeconds(300)
                )

                // Cache map for allCastiObce
                .addMapConfig(
                        new MapConfig()
                                .setName("allCastiObce")
                                .setMaxIdleSeconds(300)
                );
    }

    /**
     * Creates the actual running Hazelcast instance from the configuration above.
     *
     * <p>The Config object only describes how Hazelcast should behave
     * (instance name, cache maps, max-idle settings, etc.). This bean starts
     * the real in-memory Hazelcast node that will store cached data.</p>
     *
     * @param hazelcastConfiguration Hazelcast settings defined above
     * @return running Hazelcast instance
     */
    @Bean
    public HazelcastInstance hazelcastInstance(Config hazelcastConfiguration) {
        return Hazelcast.newHazelcastInstance(hazelcastConfiguration);
    }

    /**
     * Connects Spring's cache abstraction with the running Hazelcast instance.
     *
     * <p>Spring annotations such as {@code @Cacheable}, {@code @CachePut}
     * and {@code @CacheEvict} use this CacheManager to access Hazelcast caches.</p>
     *
     * @param hazelcastInstance running Hazelcast instance
     * @return Spring cache manager backed by Hazelcast
     */
    @Bean
    public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
        return new HazelcastCacheManager(hazelcastInstance);
    }
}