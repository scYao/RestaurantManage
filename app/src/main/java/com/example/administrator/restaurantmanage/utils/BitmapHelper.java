package com.example.administrator.restaurantmanage.utils;

import android.content.Context;



import com.example.administrator.restaurantmanage.R;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by Administrator on 2015/7/7.
 */
public class BitmapHelper {

    private static BitmapUtils bitmapUtils;

    private BitmapHelper() {
    }

    public static BitmapUtils getBitmapUtils(Context appContext) {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(appContext);
            bitmapUtils.configDefaultLoadingImage(R.mipmap.loading_bitmap);
            bitmapUtils.configDefaultLoadFailedImage(R.mipmap.load_bitmap_failed);
        }
        return bitmapUtils;
    }
}
