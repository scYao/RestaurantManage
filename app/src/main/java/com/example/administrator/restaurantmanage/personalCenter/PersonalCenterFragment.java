package com.example.administrator.restaurantmanage.personalCenter;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.restaurantmanage.R;
import com.example.administrator.restaurantmanage.bluetooth.PrintActivity;
import com.example.administrator.restaurantmanage.bluetooth.PrinterConnectDialog;
import com.gprinter.aidl.GpService;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Administrator on 2017/8/1 0001.
 */

public class PersonalCenterFragment extends Fragment{
    @ViewInject(R.id.id_connect_printer)
    private TextView connect_printer;
    public static final String CONNECT_STATUS = "connect.status";
    private static final String TAG = "PersonalCenterFragment";
    private GpService mGpService;
    private int mPrinterIndex = 0;
    private PrinterServiceConnection conn;
    private static final int requestCode=0;

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mGpService = GpService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mGpService = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_center_fragment, container, false);
        ViewUtils.inject(this,view);
        connection();
        initData();
        return view;
    }

    private void initData() {

    }

    private void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent();
        intent.setAction("com.gprinter.aidl.GpPrintService");
        intent.setPackage(getActivity().getPackageName());
        getActivity().bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.id_connect_printer)
    private void onClick(View v){
        Log.d(TAG, "openPortConfigurationDialog ");
        Intent intent = new Intent(getActivity(),
                PrinterConnectDialog.class);
        boolean[] state = getConnectState();
        intent.putExtra(CONNECT_STATUS, state);
//        startActivityForResult(intent,);
        startActivity(intent);
    }



    public boolean[] getConnectState() {
        boolean[] state = new boolean[GpPrintService.MAX_PRINTER_CNT];
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            state[i] = false;
        }
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            try {
                if (mGpService.getPrinterConnectStatus(i) == GpDevice.STATE_CONNECTED) {
                    state[i] = true;
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return state;
    }
}
