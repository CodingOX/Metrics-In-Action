package com.wangwenjun.metrics.metricset;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

public class Application
{
    private final static MetricRegistry REGISTRY = new MetricRegistry();

    private final static JmxReporter REPORTER = JmxReporter.forRegistry(REGISTRY)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) throws InterruptedException
    {
        REPORTER.start();
        BusinessService businessService = new BusinessService();
        REGISTRY.registerAll(businessService);
        businessService.start();
        Thread.currentThread().join();
    }
}