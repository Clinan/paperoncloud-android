package com.quyue.paperoncloud.db.entity;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * 历史听单
 * Created by arter on 2018/6/8.
 */

public class VoiceHistory extends DataSupport implements BaseEntity {

    private int id;
    private VoiceResource voiceResource;
    private Date addTime;

    @Override
    public String getName() {
        return voiceResource.getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "VoiceHistory{" +
                "id=" + id +
                "," + voiceResource.getName() +
                ", addTime=" + addTime +
                '}';
    }

    public VoiceResource getVoiceResource() {
        return voiceResource;
    }

    public void setVoiceResource(VoiceResource voiceResource) {
        this.voiceResource = voiceResource;
    }
}
