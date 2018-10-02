package com.wangwenjun.metrics.metric.counter;

import com.codahale.metrics.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/***************************************
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class RatioGaugeCounterExample {
    /**
     * 创建 度量注册表
     */
    private static final MetricRegistry REGISTRY = new MetricRegistry();
    /**
     * 创建  报告 <br/>
     * 此处通过父类 ScheduledReporter 更加方便后续替换
     */
    private static final ConsoleReporter REPORTER = ConsoleReporter.forRegistry(REGISTRY)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    /**
     * 自定义，也可以通过 注册表创建
     */
    private final static Counter TOTAL_COUNTER = new Counter();
    private final static Counter SUCCESS_COUNTER = new Counter();

    public static void main(String[] args) {
        REPORTER.start(10, TimeUnit.SECONDS);

        //核心，注册一个 RatioGauge ,其中有2个重要参数。
        REGISTRY.gauge("success-rate", () -> new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(SUCCESS_COUNTER.getCount(), TOTAL_COUNTER.getCount());
            }
        });

        //自己再注册一个关于total的度量
        REGISTRY.counter("total", () -> TOTAL_COUNTER);

        //模拟业务
        for (; ; ) {
            business();
        }
    }

    /**
     * 模拟核心业务
     */
    private static void business() {
        shortSleep();
        //total inc
        TOTAL_COUNTER.inc();
        try {
            int x = 10 / ThreadLocalRandom.current().nextInt(6);
            SUCCESS_COUNTER.inc();
            //success inc
        } catch (Exception e) {
            //System.out.println("ERROR");
        }
    }

    private static void shortSleep() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
