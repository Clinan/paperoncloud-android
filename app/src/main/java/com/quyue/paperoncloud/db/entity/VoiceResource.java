package com.quyue.paperoncloud.db.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 声音资源类
 * Created by arter on 2018/6/8.
 */

public class VoiceResource extends DataSupport implements BaseEntity, Serializable {

    /**
     * 可以进行对象关系映射的数据类型一共有8种，int、short、long、float、double、boolean、String和Date
     */

    private int id;

    private int resId;

    private String name;

    private String author;

    private int albumResId;

    private int lyricsResId;

    private String lyricsText;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAlbumResId() {
        return albumResId;
    }

    public void setAlbumResId(int albumResId) {
        this.albumResId = albumResId;
    }

    public int getLyricsResId() {
        return lyricsResId;
    }

    public void setLyricsResId(int lyricsResId) {
        this.lyricsResId = lyricsResId;
    }

    public String getLyricsText() {
        return lyricsText;
    }

    public void setLyricsText(String lyricsText) {
        this.lyricsText = lyricsText;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "VoiceResource{" +
                "id=" + id +
                ", resId=" + resId +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", albumResId=" + albumResId +
                ", lyricsResId=" + lyricsResId +
                ", lyricsText='" + lyricsText + '\'' +
                '}';
    }
}
