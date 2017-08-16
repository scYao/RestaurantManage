package com.example.administrator.restaurantmanage.orderManage;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.restaurantmanage.R;
import com.example.administrator.restaurantmanage.adapter.OrderDetailAdapter;
import com.example.administrator.restaurantmanage.bean.OrderDetailBean;
import com.example.administrator.restaurantmanage.bluetooth.PrintActivity;
import com.example.administrator.restaurantmanage.utils.GlobalStaticConstant;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends Activity {
//    private OrderManageBean bean;
    //标题
    @ViewInject(R.id.id_back)
    private ImageView back;
    @ViewInject(R.id.id_title)
    private TextView title;

    @ViewInject(R.id.id_order_listView)
    private ListView order_listView;
    private List<OrderDetailBean> beanList = new ArrayList<>();
    private OrderDetailAdapter adapter;
    private static final String TAG = "OrderDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ViewUtils.inject(this);
//        bean = (OrderManageBean) this.getIntent().getExtras().getSerializable("bean");
        Bundle bundle = this.getIntent().getExtras();
//        String orderState = bundle.getString("orderState");
        String groupID = bundle.getString("groupID");

        title.setText("订单列表");
        //返回事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getCountableOrderListByGroupIDBackground(groupID);
        initView();
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(OrderDetailActivity.this, PrintActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initView() {
        adapter = new OrderDetailAdapter(beanList,OrderDetailActivity.this);
        order_listView.setAdapter(adapter);
    }

    private void getCountableOrderListByGroupIDBackground(String groupID) {
        //获取tokenID
        SharedPreferences preferences = getSharedPreferences("token", Context.MODE_PRIVATE);
        String tokenID = preferences.getString("tokenID", "-1");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tokenID",tokenID);
            jsonObject.put("startIndex",0);
            jsonObject.put("pageCount", GlobalStaticConstant.pageCount);
//            jsonObject.put("orderState",orderState);
            jsonObject.put("groupID",groupID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("data", jsonObject.toString());

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, GlobalStaticConstant.localhost + "get_countable_order_list_by_group_id_background/", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                try {
                    JSONObject result = new JSONObject(responseInfo.result);
                    String status = result.getString("status");
                    Log.e(TAG, "onSuccess: ssssssssssssssssssssssssssssssssss"+result);
                    if(status.equals("SUCCESS")){
                        JSONObject data = result.getJSONObject("data");
                        JSONArray dataList = data.getJSONArray("dataList");

                        for (int i = 0; i <dataList.length() ; i++) {
                            OrderDetailBean bean = new OrderDetailBean();
                            JSONObject object = dataList.getJSONObject(i);
                            Log.e(TAG, "onSuccess: "+object.getString("description"));
                            bean.setDescription(object.getString("description"));
                            bean.setImgPath(object.getString("imgPath"));
                            bean.setPrice(object.getString("price"));
                            bean.setSelectedCount(object.getString("selectedCount"));
                            beanList.add(bean);
                        }
                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(OrderDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e(TAG, "onFailure: " + e.getExceptionCode() + ":" + s);
            }
        });

    }
}
