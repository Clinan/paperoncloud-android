package com.quyue.paperoncloud.db.data;

import android.database.sqlite.SQLiteDatabase;

import com.quyue.paperoncloud.R;
import com.quyue.paperoncloud.db.entity.VoiceCategory;
import com.quyue.paperoncloud.db.entity.VoiceCollection;
import com.quyue.paperoncloud.db.entity.VoiceHistory;
import com.quyue.paperoncloud.db.entity.VoiceMyBill;
import com.quyue.paperoncloud.db.entity.VoiceMyBillFolder;
import com.quyue.paperoncloud.db.entity.VoiceResource;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据库数据常量类
 * 在欢迎界面读取数据，并经数据保存到数组中，避免后续多次读取数据造成主线程堵塞
 * 当对数据进行增删操作时，重新查询数据库，并将数据保存到对应的List里面
 * <p>
 * 包含一些用于数据库数据初始化的方法。如果首次使用应用，会在欢迎界面初始化该数据库，并将数据导入到数据库
 * 注意：里面的方法都开启了一个线程进行操作，无需担心主线程堵塞问题
 * Created by clan on 2018/6/8.
 */

public class DataBaseConstants {

    public static List<VoiceResource> voiceResourceList = new ArrayList<>();

    public static List<VoiceCategory> voiceCategoryList = new ArrayList<>();

    public static List<VoiceCollection> voiceCollectionList = new ArrayList<>();

    public static List<VoiceHistory> voiceHistoryList = new ArrayList<>();

    public static List<VoiceMyBill> voiceMyBillList = new ArrayList<>();

    public static List<VoiceMyBillFolder> voiceMyBillFolderList = new ArrayList<>();

    public static void deleteDataBaseAllData() {
        DataSupport.deleteAll(VoiceResource.class);
        DataSupport.deleteAll(VoiceCategory.class);
    }

    /**
     * 初始化数据库数据
     */
    public static void initDataBaseData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建数据库
                SQLiteDatabase db = Connector.getDatabase();

                //初始化
                initVoiceCategory();
                initVoiceResource();
                initVoiceBillFolder();
            }
        }).start();
    }

    /**
     * 初始化声音分类
     */
    public static void initVoiceCategory() {
        String[] tabs = {"童话", "科教", "自然", "安全"};
        for (int i = 0; i < tabs.length; i++) {
            VoiceCategory category = new VoiceCategory();
            category.setId(i);
            category.setName(tabs[i]);
            category.save();
        }
    }

    /**
     * 初始化声音资源
     */
    public static void initVoiceResource() {
        final String[] storyList = {"白雪公主", "卖火柴的小女孩", "灰姑娘", "丑小鸭", "皇帝的新装", "拇指姑娘"};
        final String[] storyauthorList = {"安徒生", "安徒生", "安徒生", "安徒生", "安徒生", "安徒生"};
        final int[] storyPicResList = {R.raw.pic_snow_white, R.raw.pic_little_girl_selling_matches, R.raw.pic_cinderella, R.raw.pic_ugly_duckling, R.raw.pic_emperor_new_clothes, R.raw.pic_thumbelina};
        final int[] storyVoiceResList = {R.raw.v_snow_white, R.raw.v_little_girl_selling_matches, R.raw.v_cinderella, R.raw.v_ugly_duckling, R.raw.v_emperor_new_clothes, R.raw.v_thumbelina};
        final int[] storyLycResList = {R.raw.lyc_snow_white, R.raw.lyc_little_girl_selling_matches, R.raw.lyc_cinderella, R.raw.lyc_ugly_duckling, R.raw.lyc_emperor_new_clothes, R.raw.lyc_thumbelina};

        for (int i = 0; i < storyList.length; i++) {
            VoiceResource voiceResource = new VoiceResource();
            voiceResource.setId(i);
            voiceResource.setAlbumResId(storyPicResList[i]);
            voiceResource.setAuthor(storyauthorList[i]);
            voiceResource.setLyricsResId(storyLycResList[i]);
            voiceResource.setName(storyList[i]);
            voiceResource.setResId(storyVoiceResList[i]);
            voiceResource.save();
        }
    }

    /**
     * 初始化我的收藏文件夹，添加默认文件夹
     */
    public static void initVoiceBillFolder() {
        VoiceMyBillFolder voiceMyBillFolder = new VoiceMyBillFolder();
        voiceMyBillFolder.setFolderName("默认歌单");
        voiceMyBillFolder.setAddTime(new Date());
        voiceMyBillFolder.setId(0);
        voiceMyBillFolder.save();
    }

}
