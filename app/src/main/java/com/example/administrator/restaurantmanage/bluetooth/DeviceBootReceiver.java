package com.example.administrator.restaurantmanage.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gprinter.service.GpPrintService;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class DeviceBootReceiver extends BroadcastReceiver {
    /**开机广播**/
    static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BOOT_COMPLETED)) {
            Intent i = new Intent(context, GpPrintService.class);
            context.startService(i);
            Log.i("DeviceBootReceiver", "GpPrintService.start");
        }
    }
}
