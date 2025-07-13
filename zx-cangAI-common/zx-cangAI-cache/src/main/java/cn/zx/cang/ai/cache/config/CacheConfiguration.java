package cn.zx.cang.ai.cache.config;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置
 *
 * @author kinchou
 */
@Configuration
@EnableMethodCache(basePackages = "cn.zx.cang.ai")
public class CacheConfiguration {
}
