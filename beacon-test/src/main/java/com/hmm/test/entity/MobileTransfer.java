package com.mashibing.test.entity;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/25 21:28
 */

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/25 21:28
 */

public class MobileTransfer {
    //手机号
    private String transferNumber;
    //最终运营商
    private Integer nowIsp;

    public String getTransferNumber() {
        return transferNumber;
    }

    public void setTransferNumber(String transferNumber) {
        this.transferNumber = transferNumber;
    }

    public Integer getNowIsp() {
        return nowIsp;
    }

    public void setNowIsp(Integer nowIsp) {
        this.nowIsp = nowIsp;
    }
}
