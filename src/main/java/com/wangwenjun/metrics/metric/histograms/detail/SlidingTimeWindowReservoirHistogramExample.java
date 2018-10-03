package com.wangwenjun.metrics.metric.histograms.detail;

import com.codahale.metrics.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


/***************************************
 * 最适合的是最近一段时间的数据分布图
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class SlidingTimeWindowReservoirHistogramExample {
    private final static MetricRegistry registry = new MetricRegistry();
    private final static ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    private final static Histogram histogram = new Histogram(new SlidingTimeWindowReservoir(30, TimeUnit.SECONDS));

    public static void main(String[] args) {
        reporter.start(10, TimeUnit.SECONDS);
        registry.register("SlidingTimeWindowReservoir-Histogram", histogram);
        while (true) {
            doSearch();
            randomSleep();
        }
    }

    private static void doSearch() {
        histogram.update(ThreadLocalRandom.current().nextInt(10));
    }

    private static void randomSleep() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
