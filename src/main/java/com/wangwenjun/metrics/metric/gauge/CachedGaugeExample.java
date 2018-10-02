package com.wangwenjun.metrics.metric.gauge;

import com.codahale.metrics.CachedGauge;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/***************************************
 * 用法：对于部分应用，对某个指标，如果频繁采用，会对性能产生较大影响，所以可以通过 CachedGauge 采用。
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class CachedGaugeExample {
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

        REGISTRY.gauge("cached-db-size", () -> new CachedGauge<Long>(30, TimeUnit.SECONDS) {
            @Override
            protected Long loadValue() {
                return queryFromDB();
            }
        });

        System.out.println("============" + new Date());
        Thread.currentThread().join();
    }

    /**
     * 模拟查询数据库的耗时。
     *
     * @return 耗时
     */
    private static long queryFromDB() {
        System.out.println("====queryFromDB=====");
        return System.currentTimeMillis();
    }
}