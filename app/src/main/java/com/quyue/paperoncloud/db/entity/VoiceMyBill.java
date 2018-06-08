package com.quyue.paperoncloud.db.entity;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * 我的听单
 * Created by arter on 2018/6/8.
 */

public class VoiceMyBill extends DataSupport {
    private int id;
    private int VoiceResourceId;
    private Date addTime;
    private int folderId;

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

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }
}
