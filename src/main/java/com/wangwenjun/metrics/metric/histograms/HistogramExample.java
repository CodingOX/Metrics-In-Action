package com.wangwenjun.metrics.metric.histograms;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/***************************************
 *
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class HistogramExample {
    private final static MetricRegistry registry = new MetricRegistry();
    private final static ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    private final static Histogram histogram = registry.histogram("search-result");

    public static void main(String[] args) {
        reporter.start(10, TimeUnit.SECONDS);

        while (true) {
            doSearch();
            randomSleep();
        }
    }

    /**
     * 模拟数据检索<br>
     * 数据应该是获取到了多少个数字，结果表明的是 在这段时间内，最大的搜索量，最小的搜索量等。
     */
    private static void doSearch() {
        histogram.update(ThreadLocalRandom.current().nextInt(10));
    }

    private static void randomSleep() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}