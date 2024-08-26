package com.mashibing.common.enums;

import lombok.Getter;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/8/7 19:12
 */
@Getter
public enum CMPP2DeliverEnums {
    DELIVERED("DELIVRD","Message is delivered to destination"),
    EXPIRED("EXPIRED","Message validity period has expired"),
    DELETED("DELETED","Message has been deleted."),
    UNDELIVERABLE("UNDELIV","Message is undeliverable"),
    ACCEPTED("ACCEPTD"," Message is in accepted state(i.e. has been manually read on behalf of the subscriber by customer service) "),
    UNKNOWN("UNKNOWN","Message is in invalid state"),
    REJECTED("REJECTD","Message is in a rejected state")
    ;
    private String stat;
    private String description ;

    CMPP2DeliverEnums(String stat, String description) {
        this.stat = stat;
        this.description = description;
    }
}
