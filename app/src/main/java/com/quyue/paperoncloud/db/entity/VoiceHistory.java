package com.quyue.paperoncloud.db.entity;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * 历史听单
 * Created by arter on 2018/6/8.
 */

public class VoiceHistory extends DataSupport {

    private int id;
    private int VoiceResourceId;
    private Date addTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVoiceResourceId() {
        return VoiceResourceId;
    }

    public void setVoiceResourceId(int voiceResourceId) {
        VoiceResourceId = voiceResourceId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
