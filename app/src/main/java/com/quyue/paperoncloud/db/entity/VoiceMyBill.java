package com.quyue.paperoncloud.db.entity;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * 我的听单
 * Created by arter on 2018/6/8.
 */

public class VoiceMyBill extends DataSupport implements BaseEntity {
    private int id;
    private Date addTime;
    private VoiceResource voiceResource;
    private int folderId;

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

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    @Override
    public String toString() {
        return "VoiceMyBill{" +
                "id=" + id +
                ", VoiceResourceId=" + voiceResource.getName() +
                ", addTime=" + addTime +
                ", folderId=" + folderId +
                '}';
    }

    public VoiceResource getVoiceResource() {
        return voiceResource;
    }

    public void setVoiceResource(VoiceResource voiceResource) {
        this.voiceResource = voiceResource;
    }
}
