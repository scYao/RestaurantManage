package com.example.administrator.restaurantmanage.bluetooth;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.restaurantmanage.R;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;

import org.apache.commons.lang.ArrayUtils;

import java.util.Vector;

public class PrintActivity extends Activity {
    private GpService mGpService;
    private static final String TAG = "MainActivity";
    public static final String CONNECT_STATUS = "connect.status";
    private int mPrinterIndex = 0;
    private PrinterServiceConnection conn;

    class PrinterServiceConnection implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mGpService = GpService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mGpService = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        connection();
//        sendReceipt();
    }

    private void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent();
        intent.setAction("com.gprinter.aidl.GpPrintService");
        intent.setPackage(getPackageName());
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    public void openPortDialogueClicked(View view) {
        Log.d(TAG, "openPortConfigurationDialog ");
        Intent intent = new Intent(this,
                PrinterConnectDialog.class);
        boolean[] state = getConnectState();
        intent.putExtra(CONNECT_STATUS, state);
        this.startActivity(intent);
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

    //打印测试页
    public void printTestPageClicked(View view) {
        try {
            int rel = mGpService.printeTestPage(mPrinterIndex); //
            Log.i("ServiceConnection", "rel " + rel);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    //打印票据
    public void printReceiptClicked(View view){
        try {
            int type = mGpService.getPrinterCommandType(mPrinterIndex);
            if (type == GpCom.ESC_COMMAND){
                int status = mGpService.queryPrinterStatus(mPrinterIndex,500);
                if (status == GpCom.STATE_NO_ERR){
                    sendReceipt();
                }else {
                    Toast.makeText(this, "打印机错误", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //检测是否需要换行
//    private String checkWarp(String s){
//        if (s.length() >5){
//            String ns = s.substring(0, 5)+ "\n" +s.substring(5);
//            return ns;
//        }else {
//            for (int i = 0; i <(6-s.length()) ; i++) {
//                s =s+ "  ";
//            }
//            return s;
//        }
//    }

    private String checkWarp(String s){
        if (s.length() >5){
            String ns = s.substring(0, 5)+ "\n" +s.substring(5);
            return ns;
        }else {
            return s;
        }
    }

    //打印设置
    void sendReceipt(){
        EscCommand escCommand = new EscCommand();
        escCommand.addPrintAndFeedLines((byte)3);

        escCommand.addSelectInternationalCharacterSet(EscCommand.CHARACTER_SET.CHINA);

        escCommand.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);




        // line 1
        short title =30;
        short count =200;
        short price =300;

        //标题
        escCommand.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
//        escCommand.addSetAbsolutePrintPosition(title);
        escCommand.addText("名称");
        escCommand.addSetAbsolutePrintPosition(count);
        escCommand.addText("数量");
        escCommand.addSetAbsolutePrintPosition(price);
        escCommand.addText("金额");

        escCommand.addPrintAndLineFeed();

        escCommand.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
//        escCommand.addSetAbsolutePrintPosition(title);
        escCommand.addText(checkWarp("黄蒙鸡米饭对对对dl"));
        escCommand.addSetAbsolutePrintPosition(count);
        escCommand.addText("10");
        escCommand.addSetAbsolutePrintPosition(price);
        escCommand.addText("$10");

        escCommand.addPrintAndLineFeed();
         //line2
        escCommand.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
//        escCommand.addSetAbsolutePrintPosition(title);
        escCommand.addText(checkWarp("啤酒鸭多久额外"));
        escCommand.addSetAbsolutePrintPosition(count);
        escCommand.addText("9");
        escCommand.addSetAbsolutePrintPosition(price);
        escCommand.addText("$190");

        escCommand.addPrintAndLineFeed();

        // line3
        escCommand.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
//        escCommand.addSetAbsolutePrintPosition(title);
        escCommand.addText(checkWarp("啤酒鸭你卡了"));
        escCommand.addSetAbsolutePrintPosition(count);
        escCommand.addText("9");
        escCommand.addSetAbsolutePrintPosition(price);
        escCommand.addText("$190");

        escCommand.addPrintAndLineFeed();

        sendData(escCommand);




    }

    private void sendData(EscCommand escCommand) {
        //发送数据
        Vector<Byte> datas = escCommand.getCommand();
        Log.e(TAG, "sendReceipt: "+datas);
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);//数组以字符串输出
        String string = Base64.encodeToString(bytes, Base64.DEFAULT);

        int result;
        try {
            result = mGpService.sendEscCommand(mPrinterIndex, string);
            GpCom.ERROR_CODE error_code = GpCom.ERROR_CODE.values()[result];
            if (error_code != GpCom.ERROR_CODE.SUCCESS){
                Toast.makeText(this, GpCom.getErrorText(error_code), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
