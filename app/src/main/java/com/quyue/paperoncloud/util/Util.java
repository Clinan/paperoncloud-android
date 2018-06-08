package com.quyue.paperoncloud.util;

import com.quyue.paperoncloud.db.entity.BaseEntity;

import java.util.List;

/**
 * Created by arter on 2018/6/8.
 */

public class Util {
    /**
     * 判断id对应的数据是否在list中
     *
     * @param list
     * @param id
     * @return
     */
    public static BaseEntity getEntityFromList(List<? extends BaseEntity> list, int id) {
        for (BaseEntity b : list) {
            if (id == b.getId()) {
                return b;
            }
        }
        return null;
    }

}
