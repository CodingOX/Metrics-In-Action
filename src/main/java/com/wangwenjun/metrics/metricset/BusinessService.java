package com.wangwenjun.metrics.metricset;

import com.codahale.metrics.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * 综合案例
 * @author Wang Wenjun
 */
public class BusinessService extends Thread implements MetricSet {
    private final Map<String, Metric> metrics = new HashMap<>();

    /**
     * Counter统计的是计数信息
     */
    private final Counter totalBusiness = new Counter();
    private final Counter successBusiness = new Counter();
    private final Counter failBusiness = new Counter();
    /**
     * timer统计的是业务时间消耗
     */
    private final Timer timer = new Timer();
    /**
     * 直方图主要统计的情况分布
     */
    private final Histogram volumeHisto = new Histogram(new ExponentiallyDecayingReservoir());

    /**
     * Gauge统计的肯定是一个数值，这个数值可以自己定义，比如是Ratio分数相关的。
     */
    private final Gauge successGauge = new RatioGauge() {
        @Override
        protected Ratio getRatio() {
            return Ratio.of(successBusiness.getCount(), totalBusiness.getCount());
        }
    };

    public BusinessService() {
        metrics.put("cloud-disk-upload-total", totalBusiness);
        metrics.put("cloud-disk-upload-success", successBusiness);
        metrics.put("cloud-disk-upload-failure", failBusiness);
        metrics.put("cloud-disk-upload-frequency", timer);
        metrics.put("cloud-disk-upload-volume", volumeHisto);
        metrics.put("cloud-disk-upload-suc-rate", successGauge);
    }

    @Override
    public void run() {
        while (true) {
            upload(new byte[current().nextInt(10_000)]);
        }
    }

    /**
     * 模拟业务
     *
     * @param buffer
     */
    private void upload(byte[] buffer) {
        totalBusiness.inc();
        Timer.Context timer = this.timer.time();
        try {
            int x = 1 / current().nextInt(10);
            TimeUnit.MILLISECONDS.sleep(200);
            volumeHisto.update(buffer.length);
            successBusiness.inc();
        } catch (Exception e) {
            failBusiness.inc();
        } finally {
            timer.close();
        }

    }

    @Override
    public Map<String, Metric> getMetrics() {
        return metrics;
    }
}
