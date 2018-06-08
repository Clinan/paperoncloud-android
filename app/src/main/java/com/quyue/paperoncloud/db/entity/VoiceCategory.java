package com.quyue.paperoncloud.db.entity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arter on 2018/6/8.
 */

public class VoiceCategory extends DataSupport {
    private int id;
    private String name;
    private List<VoiceResource> voiceResourceList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VoiceResource> getVoiceResourceList() {
        return voiceResourceList;
    }

    public void setVoiceResourceList(List<VoiceResource> voiceResourceList) {
        this.voiceResourceList = voiceResourceList;
    }

    @Override
    public String toString() {
        return "VoiceCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", voiceResourceList=" + voiceResourceList +
                '}';
    }
}
