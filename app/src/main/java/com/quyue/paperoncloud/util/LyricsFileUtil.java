package com.quyue.paperoncloud.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by arter on 2018/6/8.
 */

public class LyricsFileUtil {

    public static String getResTxtFileString(Context context, int resId) throws IOException {
        InputStream is = context.getResources().openRawResource(resId);
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
