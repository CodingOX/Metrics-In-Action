package com.wangwenjun.metrics.metric.histograms;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/***************************************
 *
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class HistogramExample {
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

    /**
     * 直方图的信息，将每次耗时记录到直方图中<br>
     * 采用的是默认的直方图分布
     */
    private final static Histogram HISTOGRAM = REGISTRY.histogram("search-result");

    /**
     * 核心业务
     *
     * @param args
     */
    public static void main(String[] args) {
        REPORTER.start(10, TimeUnit.SECONDS);

        while (true) {
            doSearch();
        }
    }

    /**
     * 模拟数据检索<br>
     * 数据应该是获取到了多少个数字，结果表明的是 在这段时间内，最大的搜索量，最小的搜索量等。
     */
    private static void doSearch() {
        //记录当次的耗时信息，
        HISTOGRAM.update(doBusiness());
    }


    /**
     * 模拟业务耗时
     *
     * @return 耗时
     */
    private static int doBusiness() {
        int coast = randomSleep();
        return coast;
    }

    private static int randomSleep() {
        try {
            int sleep = ThreadLocalRandom.current().nextInt(5);
            TimeUnit.SECONDS.sleep(sleep);
            return sleep;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }
}