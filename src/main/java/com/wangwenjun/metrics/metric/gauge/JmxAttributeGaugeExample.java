package com.wangwenjun.metrics.metric.gauge;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.JmxAttributeGauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.concurrent.TimeUnit;

/***************************************
 * 获取JMX中的信息
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class JmxAttributeGaugeExample {

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


    public static void main(String[] args) throws MalformedObjectNameException, InterruptedException {
        REPORTER.start(10, TimeUnit.SECONDS);

        REGISTRY.register(MetricRegistry.name(JmxAttributeGaugeExample.class, "HeapMemory"),
                new JmxAttributeGauge(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage"));

        REGISTRY.register(MetricRegistry.name(JmxAttributeGaugeExample.class, "NonHeapMemoryUsage"),
                new JmxAttributeGauge(new ObjectName("java.lang:type=Memory"), "NonHeapMemoryUsage"));

        Thread.currentThread().join();
    }
}