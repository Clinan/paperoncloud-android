package com.quyue.paperoncloud.db.entity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 我的听单文件夹
 * Created by arter on 2018/6/8.
 */

public class VoiceMyBillFolder extends DataSupport implements BaseEntity{
    private int id;
    private String folderName;
    private Date addTime;

    //和VoiceMyBill是一对多关系
    private List<VoiceMyBill> voiceMyBillList = new ArrayList<>();

    @Override
    public String getName() {
        return folderName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public List<VoiceMyBill> getVoiceMyBillList() {
        return voiceMyBillList;
    }

    public void setVoiceMyBillList(List<VoiceMyBill> voiceMyBillList) {
        this.voiceMyBillList = voiceMyBillList;
    }

    @Override
    public String toString() {
        String voiceMyBillListStr = "";
        for (VoiceMyBill v : voiceMyBillList) {
            voiceMyBillListStr = voiceMyBillListStr + v.toString() + ",";
        }
        return "VoiceMyBillFolder{" +
                "id=" + id +
                ", folderName='" + folderName + '\'' +
                ", addTime=" + addTime +
                ", voiceMyBillList=" + voiceMyBillListStr +
                '}';
    }
}
