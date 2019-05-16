package com.genge.demo.async;

import java.util.HashMap;
import java.util.Map;

/**
 * 发生的事件相关的数据打包进model
 * @author Genge
 */
public class EventModel {
    private EventType eventType;
    /**
     * 谁触发的
     */
    private int actorId;
    /**
     * 触发的类型和id
     */
    private int entityType;
    private int entityId;
    /**
     * 触发的拥有者
     */
    private int entityOwnerId;
    /**
     * 保存触发的额外消息
     */
    private Map<String,String> exts = new HashMap<>();

    /**
     * 返回this的方法，主要用于在后面方便链式调用
     * @param key
     * @param value
     * @return
     */
    public EventModel set(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public String get(String key) {
        return exts.get(key);
    }


    public EventModel(EventType eventType) {
        this.eventType = eventType;
    }
    public EventModel() {

    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
}
