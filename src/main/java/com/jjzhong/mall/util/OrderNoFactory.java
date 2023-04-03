package com.jjzhong.mall.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 订单号工厂类
 */
public class OrderNoFactory {
    /**
     * 获取时间字符串
     * @return 时间字符串
     */
    private static String getDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        return dateTimeFormatter.format(LocalDateTime.now());
    }

    /**
     * 获取随机数
     * @param seed 随机数种子
     * @return 随机数
     */
    private static int getRandom(Long seed) {
        Random random = new Random(seed);
        return random.nextInt(90000) + 10000;
    }

    /**
     * 获取订单号
     * @return 订单号
     */
    public static String getOrderNo() {
        return getDateTime() + getRandom(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }
}
