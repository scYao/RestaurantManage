package com.example.administrator.restaurantmanage;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.restaurantmanage.orderManage.OrderManageFragment;
import com.example.administrator.restaurantmanage.personalCenter.PersonalCenterFragment;
import com.example.administrator.restaurantmanage.utils.GlobalStaticConstant;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener {
    //控件声明
    @ViewInject(R.id.id_radioGroup)
    private RadioGroup radioGroup;
    @ViewInject(R.id.id_order_manage)
    private RadioButton order_manage;
    @ViewInject(R.id.id_personal_center)
    private RadioButton personal_center;

    private OrderManageFragment orderManageFragment;
    private PersonalCenterFragment personalCenterFragment;
    private static final String TAG = "MainActivity";

    //socket
    private Socket socket;

    private static final int NotificationId = 1;

    ///设置底部选择图片大小
    int top = 4;
    int width = 40;
    int height = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        radioGroup.setOnCheckedChangeListener(this);
        order_manage.setChecked(true);

        //设置底部选择图片大小
        Drawable drawable1 = getResources().getDrawable(R.mipmap.order_select);
        drawable1.setBounds(0, top, width, height);
        order_manage.setCompoundDrawables(null, drawable1, null, null);

        Drawable drawable2 = getResources().getDrawable(R.mipmap.personal_unselect);
        drawable2.setBounds(0, top, width, height);
        personal_center.setCompoundDrawables(null, drawable2, null, null);


        try {
            socket = IO.socket("http://192.168.40.150:3000/");
            socket.connect();
            attemptSend();
            socket.on("new_order", listener);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private void attemptSend() {
        String message = "Hello";

        socket.emit("new_order", message);
    }

    private Emitter.Listener listener = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: " + args[0]);
                    //调用orderManageFragment的方法
                    orderManageFragment.initData("1",0);
                    try {
                        JSONObject data = new JSONObject(args[0].toString());
                        Log.e(TAG, "run111: " + data);
//                        String test = data.getString("tableID");
                        sendNotification(data.toString());



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    //发出通知
    private void sendNotification(String test) {
        //获取PendingIntent
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //获取NotificationManager实例
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                //设置小图标
                .setSmallIcon(R.mipmap.notification)
                //设置通知标题
                .setContentTitle("新订单")
                //设置通知内容
                .setContentText("您有新的订单，请及时处理" + test)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notificationManager.notify(NotificationId, builder.build());

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);



        switch (i) {

            case R.id.id_order_manage:
                if (orderManageFragment == null) {
                    orderManageFragment = new OrderManageFragment();
                    transaction.add(R.id.fragment_container, orderManageFragment);
                } else {
                    transaction.show(orderManageFragment);
                }

                //设置底部选择图片大小
                Drawable drawable1 = getResources().getDrawable(R.mipmap.order_select);
                drawable1.setBounds(0, top, width, height);
                order_manage.setCompoundDrawables(null, drawable1, null, null);

                Drawable drawable2 = getResources().getDrawable(R.mipmap.personal_unselect);
                drawable2.setBounds(0, top, width, height);
                personal_center.setCompoundDrawables(null, drawable2, null, null);

                break;

            case R.id.id_personal_center:
                if (personalCenterFragment == null) {
                    personalCenterFragment = new PersonalCenterFragment();
                    transaction.add(R.id.fragment_container, personalCenterFragment);
                } else {
                    transaction.show(personalCenterFragment);
                }

                //设置底部选择图片大小
                Drawable drawable3 = getResources().getDrawable(R.mipmap.order_unselect);
                drawable3.setBounds(0, top, width, height);
                order_manage.setCompoundDrawables(null, drawable3, null, null);

                Drawable drawable4 = getResources().getDrawable(R.mipmap.personal_select);
                drawable4.setBounds(0, top, width, height);
                personal_center.setCompoundDrawables(null, drawable4, null, null);

                break;

        }
        transaction.commit();
    }

    //隐藏所有fragment
    private void hideAllFragment(FragmentTransaction transaction) {
        if (orderManageFragment != null) {
            transaction.hide(orderManageFragment);
        }

        if (personalCenterFragment != null) {
            transaction.hide(personalCenterFragment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        socket.off("new_order", listener);
    }


}
