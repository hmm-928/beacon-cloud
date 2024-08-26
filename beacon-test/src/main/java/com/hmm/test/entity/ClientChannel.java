package com.mashibing.test.entity;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/27 16:38
 */

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/27 16:38
 */

public class ClientChannel {
    private Long clientId;
    private Long channelId;
    private Integer clientChannelWeight;
    private String clientChannelNumber;
    private Integer isAvailable;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Integer getClientChannelWeight() {
        return clientChannelWeight;
    }

    public void setClientChannelWeight(Integer clientChannelWeight) {
        this.clientChannelWeight = clientChannelWeight;
    }

    public String getClientChannelNumber() {
        return clientChannelNumber;
    }

    public void setClientChannelNumber(String clientChannelNumber) {
        this.clientChannelNumber = clientChannelNumber;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }
}
