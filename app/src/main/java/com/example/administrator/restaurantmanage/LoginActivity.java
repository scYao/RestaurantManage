package com.example.administrator.restaurantmanage;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.restaurantmanage.utils.GlobalStaticConstant;
import com.example.administrator.restaurantmanage.utils.Md5Encrypt;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity implements View.OnClickListener {

    @ViewInject(R.id.id_login)
    private Button login;
    @ViewInject(R.id.id_username)
    private EditText usrname;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String username ="1235";
        String password = "123456";
        JSONObject object = new JSONObject();
        try {
            object.put("adminName", username);
            object.put("password", Md5Encrypt.getInstance().encrypt(password));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestParams params = new RequestParams();
        params.addBodyParameter("data", object.toString());

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, GlobalStaticConstant.localhost + "admin_login/", params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject result = new JSONObject(responseInfo.result);
                            String status = result.getString("status");
                            if (status.equals("SUCCESS")){
                                String data = result.getString("data");
                                saveTokenID(data);
                                GlobalStaticConstant.tokenID = data;
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this,MainActivity.class);
                                startActivity(intent);

                            }else {
                                Toast.makeText(LoginActivity.this, "登录失败"+result, Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onSuccess: 登录失败"+result );
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });
    }

    //保存tokenID
    public void saveTokenID(String token){
        SharedPreferences preferences = getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("tokenID", token);
        editor.commit();
    }
}
