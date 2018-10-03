package com.wangwenjun.metrics.healthcheck;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;

import java.util.concurrent.TimeUnit;

/**
 * 包含多个健康检查的demo
 */
public class HealthCheckSetExample {
    public static void main(String[] args) throws InterruptedException {

        //创建注册器
        MetricRegistry registry = new MetricRegistry();
        //注册器通过控制台的形式输出
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();

        //创建健康检查注册器，并向其中注册健康监测的单元项
        //可以创建多个健康检查
        HealthCheckRegistry hcRegistry = new HealthCheckRegistry();
        hcRegistry.register("restful-hc", new RESTfulServiceHealthCheck());
        hcRegistry.register("thread-dead-lock-hc", new ThreadDeadlockHealthCheck());
        //gauge返回的应该是一个值
        registry.gauge("restful-hc", () -> hcRegistry::runHealthChecks);

        reporter.start(10, TimeUnit.SECONDS);
        Thread.currentThread().join();
    }
}
