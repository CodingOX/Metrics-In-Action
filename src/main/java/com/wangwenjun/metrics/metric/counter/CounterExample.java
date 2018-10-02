package com.wangwenjun.metrics.metric.counter;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/***************************************
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class CounterExample {

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
     * 数据源
     */
    private static final BlockingDeque<Long> SOURCE_QUEUE = new LinkedBlockingDeque<>(1_000);

    public static void main(String[] args) {
        REPORTER.start(10, TimeUnit.SECONDS);

        //创建一个Counter
        Counter counter = REGISTRY.counter("SOURCE_QUEUE-count", Counter::new);

        new Thread(() ->
        {
            for (; ; ) {
                randomSleep();
                SOURCE_QUEUE.add(System.nanoTime());
                //增加
                counter.inc();
            }
        }).start();

        new Thread(() ->
        {
            for (; ; ) {
                randomSleep();
                if (SOURCE_QUEUE.poll() != null) {
                    //减少数值
                    counter.dec();
                }
            }
        }).start();
    }

    /**
     * 随机随眠
     */
    private static void randomSleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(500));
        } catch (InterruptedException e) {
        }
    }
}
