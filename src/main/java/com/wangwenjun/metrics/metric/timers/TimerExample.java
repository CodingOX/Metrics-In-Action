package com.wangwenjun.metrics.metric.timers;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Timer;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

/***************************************
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class TimerExample {
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

    private final static Timer TIMER = REGISTRY.timer("request", Timer::new);

    /**
     * 启动类
     *
     * @param args
     */
    public static void main(String[] args) {
        REPORTER.start(10, TimeUnit.SECONDS);
        while (true) {
            business();
        }
    }

    /**
     * 模拟业务
     */
    private static void business() {
        //new就是开启的意思
        Timer.Context context = TIMER.time();
        try {
            TimeUnit.SECONDS.sleep(current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //stops就是暂停，在finally中暂停，最为恰当。
            long stop = context.stop();
            System.out.println("=====" + stop);
        }
    }
}
