package com.wangwenjun.metrics.metric.gauge;

import com.codahale.metrics.*;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/***************************************
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class DerivativeGaugeExample {
    /**
     * 构建缓存
     */
    private final static LoadingCache<String, String> CACHE = CacheBuilder
            .newBuilder().maximumSize(10)
            .expireAfterAccess(5, TimeUnit.SECONDS)
            .recordStats()
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    return key.toUpperCase();
                }
            });

    /**
     * 创建 度量注册表
     */
    private static final MetricRegistry REGISTRY = new MetricRegistry();

    /**
     * 创建  报告 <br/>
     * 此处通过父类 ScheduledReporter 更加方便后续替换
     */
    private static final ScheduledReporter REPORTER = ConsoleReporter.forRegistry(REGISTRY)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) throws InterruptedException {
        REPORTER.start(10, TimeUnit.SECONDS);
        Gauge<CacheStats> cacheGauge = REGISTRY.gauge("CACHE-stats", () -> CACHE::stats);
        REGISTRY.register("missCount", new DerivativeGauge<CacheStats, Long>(cacheGauge) {
            @Override
            protected Long transform(CacheStats stats) {
                return stats.missCount();
            }
        });

        REGISTRY.register("loadExceptionCount", new DerivativeGauge<CacheStats, Long>(cacheGauge) {
            @Override
            protected Long transform(CacheStats stats) {
                return stats.loadExceptionCount();
            }
        });

        while (true) {
            business();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private static void business() {
        CACHE.getUnchecked("alex");
    }
}
