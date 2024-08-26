package com.mashibing.common.util;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/12 16:25
 */

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/12 16:25
 */

import com.mashibing.common.enums.ExceptionEnums;
import com.mashibing.common.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 雪花算法生成全局唯一ID
 * 64个bit为的long类型的值
 * 第一位：占1个bit位，就是0.
 * 第二位：占41个bit位，代表时间戳
 * 第三位：占5个bit位，代表机器id
 * 第四位：占5个bit位，服务id
 * 第五位：占12个bit位，序列，自增的数值
 * @author hmm
 * @description
 */

@Component
public class SnowFlakeUtil {

    /**
     * 41个bit位存储时间戳，从0开始计算，最多可以存储69.7年。
     * 那么如果默认使用，从1970年到现在，最多可以用到2039年左右。
     * 按照从2022-11-11号开始计算，存储41个bit为，这样最多可以使用到2092年不到~~
     */
    private long timeStart = 1668096000000L;

    /**
     * 机器id
     */
    @Value("${snowflake.machineId:0}")
    private long machineId;

    /**
     * 服务id
     */
    @Value("${snowflake.serviceId:0}")
    private long serviceId;

    /**
     * 序列
     */
    private long sequence;


    /**
     * 机器id占用的bit位数
     */
    private long machineIdBits = 5L;

    /**
     * 服务id占用的bit位数
     */
    private long serviceIdBits = 5L;

    /**
     * 序列占用的bit位数
     */
    private long sequenceBits = 12L;

    /**
     * 计算出机器id的最大值
     */
    private long maxMachineId = -1 ^ (-1 << machineIdBits);

    /**
     * 计算出服务id的最大值
     */
    private long maxServiceId = -1 ^ (-1 << serviceIdBits);

    @PostConstruct
    public void init(){
        if(machineId > maxMachineId || serviceId > maxServiceId){
            System.out.println("机器ID或服务ID超过最大范围值！！");
            throw new ApiException(ExceptionEnums.SNOWFLAKE_OUT_OF_RANGE);
        }
    }

    /**
     * 服务id需要位移的位数
     */
    private long serviceIdShift = sequenceBits;

    /**
     * 机器id需要位移的位数
     */
    private long machineIdShift = sequenceBits + serviceIdBits;

    /**
     * 时间戳需要位移的位数
     */
    private long timestampShift = sequenceBits + serviceIdBits + machineIdBits;

    /**
     *  序列的最大值
     */
    private long maxSequenceId = -1 ^ (-1 << sequenceBits);

    /**
     * 记录最近一次获取id的时间
     */
    private long lastTimestamp = -1;

    /**
     *  获取系统时间毫秒值
     * @return
     */
    private long timeGen(){
        return System.currentTimeMillis();
    }


    public synchronized long nextId(){
        //1、 拿到当前系统时间的毫秒值
        long timestamp = timeGen();
        // 避免时间回拨造成出现重复的id
        if(timestamp < lastTimestamp){
            // 说明出现了时间回拨
            System.out.println("当前服务出现时间回拨！！！");
            throw new ApiException(ExceptionEnums.SNOWFLAKE_TIME_BACK);
        }

        //2、 判断当前生成id的时间和上一次生成的时间
        if(timestamp == lastTimestamp){
            // 同一毫秒值生成id
            sequence = (sequence + 1) & maxSequenceId;
            // 0000 10100000 :sequence
            // 1111 11111111 :maxSequenceId
            if(sequence == 0){
                // 进到这个if，说明已经超出了sequence序列的最大取值范围
                // 需要等到下一个毫秒再做回来生成具体的值
                timestamp = timeGen();
                while(timestamp <= lastTimestamp){
                    // 时间还没动。
                    timestamp = timeGen();
                }
            }
        }else{
            // 另一个时间点生成id
            sequence = 0;
        }
        //3、重新给lastTimestamp复制
        lastTimestamp = timestamp;

        //4、计算id，将几位值拼接起来。  41bit位的时间，5位的机器，5位的服务 ，12位的序列
        return  ((timestamp - timeStart) << timestampShift) |
                (machineId << machineIdShift) |
                (serviceId << serviceIdShift) |
                sequence &
                        Long.MAX_VALUE;
    }
}