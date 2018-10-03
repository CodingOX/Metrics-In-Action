package com.wangwenjun.metrics.metric.meter;

import com.codahale.metrics.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/***************************************
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class RatioGaugeExample {
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

    private final static Meter TOTAL_METER = new Meter();
    private final static Meter SUCCESS_METER = new Meter();

    public static void main(String[] args) {
        REPORTER.start(10, TimeUnit.SECONDS);
        REGISTRY.gauge("success-rate", () -> new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(SUCCESS_METER.getCount(), TOTAL_METER.getCount());
            }
        });

        REGISTRY.register("total-meter",TOTAL_METER);
        REGISTRY.register("success-meter",SUCCESS_METER);

        for (; ; ) {
            business();
        }
    }

    private static void business() {
        //total inc
        shortSleep();
        TOTAL_METER.mark();
        try {
            int x = 10 / ThreadLocalRandom.current().nextInt(6);
            SUCCESS_METER.mark();
            //success inc
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

    private static void shortSleep() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(6));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
