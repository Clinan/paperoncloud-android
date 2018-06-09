package com.quyue.paperoncloud.db.data;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.quyue.paperoncloud.R;
import com.quyue.paperoncloud.db.entity.BaseEntity;
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

    private static final String TAG = "DataBaseConstants";
    public static List<VoiceResource> voiceResourceList = new ArrayList<>();

    public static List<VoiceCategory> voiceCategoryList = new ArrayList<>();

    public static List<VoiceCollection> voiceCollectionList = new ArrayList<>();

    public static List<VoiceHistory> voiceHistoryList = new ArrayList<>();

    public static List<VoiceMyBill> voiceMyBillList = new ArrayList<>();

    public static List<VoiceMyBillFolder> voiceMyBillFolderList = new ArrayList<>();

    public static void selectAllTableData() {
        voiceResourceList = DataSupport.findAll(VoiceResource.class, true);
        voiceCategoryList = DataSupport.findAll(VoiceCategory.class, true);
        voiceCollectionList = DataSupport.findAll(VoiceCollection.class, true);
        voiceHistoryList = DataSupport.order("id desc").find(VoiceHistory.class, true);
        voiceMyBillList = DataSupport.findAll(VoiceMyBill.class, true);
        voiceMyBillFolderList = DataSupport.findAll(VoiceMyBillFolder.class, true);

    }

    private static void printLists() {
        //打印到控制台
        Log.d(TAG, listToString(voiceResourceList));
        Log.d(TAG, listToString(voiceCategoryList));
        Log.d(TAG, listToString(voiceCollectionList));
//        Log.d(TAG, listToString(voiceHistoryList));
//        Log.d(TAG, listToString(voiceMyBillList));
        Log.d(TAG, listToString(voiceMyBillFolderList));
    }

    private static String listToString(List<? extends BaseEntity> list) {
        String result = "";
        for (BaseEntity baseEntity : list) {
            if (baseEntity != null) {
                result = result + baseEntity.toString() + ",\n";
            }
        }
        return result + "\n\n";
    }

    /**
     * 初始化数据库数据
     */
    public static Thread initDataBaseData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //创建数据库
                SQLiteDatabase db = Connector.getDatabase();
                selectAllTableData();
                //初始化
                initVoiceCategory();
                initVoiceBillFolder();
                selectAllTableData();
                printLists();
            }
        });
        thread.start();
        return thread;
    }

    /**
     * 初始化声音分类
     */
    public static void initVoiceCategory() {
        String[] tabs = {"童话", "唐诗", "养成好习惯", "英语"};
        for (int i = 0; i < tabs.length; i++) {
            boolean isExist = false;
            for (BaseEntity baseEntity : voiceCategoryList) {
                if (baseEntity.getName().equals(tabs[i])) {
                    isExist = true;
                }
            }
            if (!isExist) {
                switch (i) {
                    //童话
                    case 0: {
                        VoiceCategory category = new VoiceCategory();
                        category.setId(i);
                        String[] storyList = {"白雪公主", "卖火柴的小女孩", "灰姑娘", "丑小鸭", "皇帝的新装", "拇指姑娘"};
                        String[] storyauthorList = {"安徒生", "安徒生", "安徒生", "安徒生", "安徒生", "安徒生"};
                        int[] storyPicResList = {R.raw.pic_snow_white, R.raw.pic_little_girl_selling_matches, R.raw.pic_cinderella, R.raw.pic_ugly_duckling, R.raw.pic_emperor_new_clothes, R.raw.pic_thumbelina};
                        int[] storyVoiceResList = {R.raw.v_snow_white, R.raw.v_little_girl_selling_matches, R.raw.v_cinderella, R.raw.v_ugly_duckling, R.raw.v_emperor_new_clothes, R.raw.v_thumbelina};
                        int[] storyLycResList = {R.raw.lyc_snow_white, R.raw.lyc_little_girl_selling_matches, R.raw.lyc_cinderella, R.raw.lyc_ugly_duckling, R.raw.lyc_emperor_new_clothes, R.raw.lyc_thumbelina};
                        category.setVoiceResourceList(initVoiceResource(storyList, storyauthorList, storyPicResList, storyVoiceResList, storyLycResList));
                        category.setName(tabs[i]);
                        category.save();
                    }
                    break;
                    //唐诗
                    case 1: {
                        VoiceCategory category = new VoiceCategory();
                        category.setId(i);
                        String[] storyList = {"春晓", "游子吟", "咏柳", "咏鹅", "寻隐者不遇", "悯农"};
                        String[] storyauthorList = {"孟浩然", "孟郊", "贺知章", "骆宾王", "贾岛", "李绅"};
                        int[] storyPicResList = {
                                R.raw.p_pic_chunxiao,
                                R.raw.p_pic_youziyin,
                                R.raw.p_pic_yongliu,
                                R.raw.p_pic_yonge,
                                R.raw.p_pic_xunyinzhebuyu,
                                R.raw.p_pic_minnong
                        };
                        int[] storyLycResList = {
                                R.raw.p_lyc_chunxiao,
                                R.raw.p_lyc_youziyin,
                                R.raw.p_lyc_yongliu,
                                R.raw.p_lyc_yonge,
                                R.raw.p_lyc_xunyinzhebuyu,
                                R.raw.p_lyc_minnong
                        };
                        int[] storyVoiceResList = {
                                R.raw.p_v_chunxiao,
                                R.raw.p_v_youziyin,
                                R.raw.p_v_yongliu,
                                R.raw.p_v_yonge,
                                R.raw.p_v_xunyinzhebuyu,
                                R.raw.p_v_minnong
                        };
                        category.setVoiceResourceList(initVoiceResource(storyList, storyauthorList, storyPicResList, storyVoiceResList, storyLycResList));
                        category.setName(tabs[i]);
                        category.save();
                    }
                    break;
                    //养成好习惯
                    case 2: {

                        VoiceCategory category = new VoiceCategory();
                        category.setId(i);
                        String[] storyList = {"吃饭不挑食", "健康歌", "刷牙歌", "洗手歌", "学习歌", "自己的衣服自己穿"};
                        String[] storyauthorList = {"佚名", "佚名", "佚名", "佚名", "佚名", "佚名"};
                        int[] storyPicResList = {
                                R.raw.h_pic_chifanbutiaoshi,
                                R.raw.h_pic_jianshenge,
                                R.raw.h_pic_shuayage,
                                R.raw.h_pic_xishouge,
                                R.raw.h_pic_xuexige,
                                R.raw.h_pic_zijideyifuzijichuan,
                        };
                        int[] storyLycResList = {
                                R.raw.h_lyc_chifanbutiaoshi,
                                R.raw.h_lyc_jianshenge,
                                R.raw.h_lyc_shuayage,
                                R.raw.h_lyc_xishouge,
                                R.raw.h_lyc_xuexige,
                                R.raw.h_lyc_zijideyifuzijichuan,
                        };
                        int[] storyVoiceResList = {
                                R.raw.h_v_chifanbutiaoshi,
                                R.raw.h_v_jianshenge,
                                R.raw.h_v_shuayage,
                                R.raw.h_v_xishouge,
                                R.raw.h_v_xuexige,
                                R.raw.h_v_zijideyifuzijichuan,
                        };
                        category.setVoiceResourceList(initVoiceResource(storyList, storyauthorList, storyPicResList, storyVoiceResList, storyLycResList));
                        category.setName(tabs[i]);
                        category.save();
                    }
                    break;
                    //英语
                    case 3: {
                        VoiceCategory category = new VoiceCategory();
                        category.setId(i);
                        String[] storyList = {"家庭成员称呼", "礼貌问候语", "数字歌", "天气", "月份", "字母歌"};
                        String[] storyauthorList = {"佚名", "佚名", "佚名", "佚名", "佚名", "佚名"};
                        int[] storyPicResList = {
                                R.raw.e_pic_jiatingchengyuanchenghu,
                                R.raw.e_pic_limaowenhouyu,
                                R.raw.e_pic_shuzige,
                                R.raw.e_pic_tianqi,
                                R.raw.e_pic_yuefen,
                                R.raw.e_pic_zimuge,
                        };
                        int[] storyLycResList = {
                                R.raw.e_lyc_jiatingchengyuanchenghu,
                                R.raw.e_lyc_limaowenhouyu,
                                R.raw.e_lyc_shuzige,
                                R.raw.e_lyc_tianqi,
                                R.raw.e_lyc_yuefen,
                                R.raw.e_lyc_zimuge,
                        };
                        int[] storyVoiceResList = {
                                R.raw.e_v_jiatingchengyuanchenghu,
                                R.raw.e_v_limaowenhouyu,
                                R.raw.e_v_shuzige,
                                R.raw.e_v_tianqi,
                                R.raw.e_v_yuefen,
                                R.raw.e_v_zimuge,
                        };
                        category.setVoiceResourceList(initVoiceResource(storyList, storyauthorList, storyPicResList, storyVoiceResList, storyLycResList));
                        category.setName(tabs[i]);
                        category.save();
                    }
                    break;
                }

            }
        }
    }

    /**
     * 初始化声音资源
     */
    private static List<VoiceResource> initVoiceResource(String[] storyList, String[] storyauthorList, int[] storyPicResList, int[] storyVoiceResList, int[] storyLycResList) {

        List<VoiceResource> list = new ArrayList<>();

        for (int i = 0; i < storyList.length; i++) {
            boolean isExist = false;
            for (BaseEntity baseEntity : voiceResourceList) {
                if (baseEntity.getName().equals(storyList[i])) {
                    isExist = true;
                }
            }
            if (!isExist) {
                VoiceResource voiceResource = new VoiceResource();
                voiceResource.setId(i);
                voiceResource.setAlbumResId(storyPicResList[i]);
                voiceResource.setAuthor(storyauthorList[i]);
                voiceResource.setLyricsResId(storyLycResList[i]);
                voiceResource.setName(storyList[i]);
                voiceResource.setResId(storyVoiceResList[i]);
                voiceResource.save();
                list.add(voiceResource);
            }
        }
        return list.size() <= 0 ? null : list;
    }

    /**
     * 初始化我的听单文件夹，添加默认文件夹
     */
    public static void initVoiceBillFolder() {
        boolean isExist = false;
        for (BaseEntity baseEntity : voiceMyBillFolderList) {
            if (baseEntity.getName().equals("默认歌单")) {
                isExist = true;
            }
        }
        if (!isExist) {
            VoiceMyBillFolder voiceMyBillFolder = new VoiceMyBillFolder();
            voiceMyBillFolder.setFolderName("默认歌单");
            voiceMyBillFolder.setAddTime(new Date());
            voiceMyBillFolder.setId(0);
            voiceMyBillFolder.save();
        }
    }

    /**
     * 添加到新增听单中
     *
     * @param folderName
     * @param voiceResource
     * @return
     */
    public static boolean insert2VoiceMyBillWithNewFolder(String folderName, VoiceResource voiceResource) {

        if (voiceResource == null) {
            return false;
        }
        for (VoiceMyBillFolder folder : voiceMyBillFolderList) {
            if (folder.getFolderName().equals(folderName)) {
                return false;
            }
        }

        //新增一个文件夹
        final VoiceMyBillFolder voiceMyBillFolder = new VoiceMyBillFolder();
        voiceMyBillFolder.setAddTime(new Date());
        voiceMyBillFolder.setFolderName(folderName);

        //新增一个听单记录
        final VoiceMyBill voiceMyBill = new VoiceMyBill();
        voiceMyBill.setAddTime(new Date());
        voiceMyBill.setVoiceResource(voiceResource);
        //将记录放入到List中，用于放入到文件夹中
        List<VoiceMyBill> voiceMyBills = new ArrayList<>();
        voiceMyBills.add(voiceMyBill);

        voiceMyBillFolder.setVoiceMyBillList(voiceMyBills);
        //开一个线程做数据库IO
        new Thread(new Runnable() {
            @Override
            public void run() {
                voiceMyBill.save();
                voiceMyBillFolder.save();
                selectAllTableData();
            }
        }).start();
        return true;
    }

    /**
     * 添加到一个听单中
     *
     * @param folderId
     * @param voiceResource
     * @return
     */
    public static boolean insert2VoiceMyBill(int folderId, VoiceResource voiceResource) {
        VoiceMyBillFolder voiceMyBillFolder = null;
        // 先从 voiceMyBillFolderList 查找到对应的对象，得到对象
        for (VoiceMyBillFolder f : voiceMyBillFolderList) {
            if (f.getId() == folderId) {
                voiceMyBillFolder = f;
            }
        }
        if (voiceMyBillFolder == null) {
            return false;
        } else {
            //新增一个听单记录
            final VoiceMyBill voiceMyBill = new VoiceMyBill();
            voiceMyBill.setAddTime(new Date());
            voiceMyBill.setVoiceResource(voiceResource);
            //将记录放入到List中，用于放入到文件夹中
            List<VoiceMyBill> voiceMyBills = new ArrayList<>();
            voiceMyBills.add(voiceMyBill);

            voiceMyBillFolder.setVoiceMyBillList(voiceMyBills);
            //开一个线程做数据库IO
            final VoiceMyBillFolder finalVoiceMyBillFolder = voiceMyBillFolder;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    voiceMyBill.save();
                    finalVoiceMyBillFolder.save();
                    selectAllTableData();
                }
            }).start();
            return true;
        }
    }

    //添加到历史记录中
    public static boolean insert2VoiceHistoryBill(VoiceResource voiceResource) {
        if (voiceResource == null) {
            return false;
        } else {
            for (VoiceHistory v:voiceHistoryList) {
                if (v.getVoiceResource().getId()==voiceResource.getId()){
                    return false;
                }
            }
            final VoiceHistory voiceHistory = new VoiceHistory();
            voiceHistory.setAddTime(new Date());
            voiceHistory.setVoiceResource(voiceResource);
            //开一个线程做数据库IO
            new Thread(new Runnable() {
                @Override
                public void run() {
                    voiceHistory.save();
                    selectAllTableData();
                }
            }).start();
            return true;
        }
    }


    public static boolean insertOrDel2VoiceMyCollection(VoiceResource voiceResource){
        if (voiceResource == null) {
            return false;
        } else {
            for (VoiceCollection v:voiceCollectionList) {
                if (v.getVoiceResource().getId()==voiceResource.getId()){
                    v.setVoiceResource(null);
                    v.update(v.getId());
                    selectAllTableData();
                    return false;
                }
            }
            final VoiceCollection voiceCollection = new VoiceCollection();
            voiceCollection.setAddTime(new Date());
            voiceCollection.setVoiceResource(voiceResource);
            //开一个线程做数据库IO
            new Thread(new Runnable() {
                @Override
                public void run() {
            voiceCollection.save();
            selectAllTableData();
        }
            }).start();
            return true;
        }
    }
}
