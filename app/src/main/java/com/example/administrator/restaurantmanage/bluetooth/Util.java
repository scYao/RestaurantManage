package com.example.administrator.restaurantmanage.bluetooth;

import java.io.IOException;

import taobe.tec.jcc.JChineseConvertor;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class Util {
    public static String SimToTra(String simpStr) {
        String traditionalStr = null;
        try {
            JChineseConvertor jChineseConvertor = JChineseConvertor
                    .getInstance();
            traditionalStr = jChineseConvertor.s2t(simpStr);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return traditionalStr;
    }
}
