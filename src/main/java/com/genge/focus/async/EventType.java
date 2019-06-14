package com.genge.focus.async;

/**
 * 当时发生了什么事件
 * @author Genge
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    SCORE(4);

    private int value;
    EventType(int value) {
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
