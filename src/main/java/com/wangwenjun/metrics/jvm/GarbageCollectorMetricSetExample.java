package com.wangwenjun.metrics.jvm;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

import java.util.concurrent.TimeUnit;

public class GarbageCollectorMetricSetExample {
    private final static MetricRegistry registry = new MetricRegistry();
    private final static ConsoleReporter reporter = ConsoleReporter
            .forRegistry(registry)
            .build();

    public static void main(String[] args) throws InterruptedException {
        reporter.start(0, 10, TimeUnit.SECONDS);
        final GarbageCollectorMetricSet garbageCollectorMetricSet = new GarbageCollectorMetricSet();
        registry.registerAll(garbageCollectorMetricSet);
        //线程相关的统计集合
        registry.registerAll(new ThreadStatesGaugeSet());
        //类加载相关的统计集合
        registry.registerAll(new ClassLoadingGaugeSet());
        Thread.currentThread().join();
    }
}
