package com.example.administrator.restaurantmanage.orderManage;

import android.app.Activity;
import android.content.Intent;
import android.net.http.LoggingEventHandler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.restaurantmanage.R;
import com.example.administrator.restaurantmanage.adapter.ExpandableListAdapter;
import com.example.administrator.restaurantmanage.adapter.OrderFinishedItemAdapter;
import com.example.administrator.restaurantmanage.adapter.OrderFinshedAdapter;
import com.example.administrator.restaurantmanage.adapter.RecyclerViewFatherAdapter;
import com.example.administrator.restaurantmanage.bean.OrderFinshedBean;
import com.example.administrator.restaurantmanage.bean.OrderFinshedItemBean;
import com.example.administrator.restaurantmanage.bluetooth.PrintActivity;
import com.example.administrator.restaurantmanage.utils.GlobalStaticConstant;
import com.example.administrator.restaurantmanage.utils.Utility;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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

public class OrderFinishedActivity extends Activity {
    private String orderState;
    private String orderType;
    private static final String TAG = "OrderFinishedActivity";
    private static int startIndex = 0;
    private List<OrderFinshedBean> beanList = new ArrayList<>();
    private OrderFinshedAdapter orderFinshedAdapter;
    private OrderFinishedItemAdapter orderFinishedItemAdapter;


//    @ViewInject(R.id.id_order_listView_finished)
//    private ListView listView;
//    private PullToRefreshListView pullToRefreshListView;

    //    @ViewInject(R.id.id_back)
    private ImageView back;
    //    @ViewInject(R.id.id_title)
    private TextView title;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewFatherAdapter recyclerViewFatherAdapter;

    private ExpandableListAdapter expandableListAdapter;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        setContentView(R.layout.activity_order_finished);

        title = (TextView) findViewById(R.id.id_title);
        back = (ImageView) findViewById(R.id.id_back);

        title.setText("菜单列表");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        orderType = this.getIntent().getExtras().getString("orderType");
        orderState = this.getIntent().getExtras().getString("orderState");


        initView();
        getData();


        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderFinishedActivity.this, PrintActivity.class);
                startActivity(intent);
            }
        });


    }

    private void initView() {
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListAdapter = new ExpandableListAdapter(beanList, this);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

        expandableListAdapter.setEnsureOrderListener(new ExpandableListAdapter.EnsureOrderListener() {
            @Override
            public void ensure(String orderID) {
                completeOrderBackground(orderID);
            }
        });

//        recyclerView  = (RecyclerView) findViewById(R.id.id_order_listView_finished);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        recyclerViewFatherAdapter = new RecyclerViewFatherAdapter(beanList,OrderFinishedActivity.this);
//        recyclerView.setAdapter(recyclerViewFatherAdapter);


//        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.id_order_listView_finished);
////        listView = (ListView) findViewById(R.id.id_order_listView_finished);
//        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
//
//        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                getData();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//            }
//        });
//        listView = pullToRefreshListView.getRefreshableView();
//
//        orderFinshedAdapter = new OrderFinshedAdapter(beanList,OrderFinishedActivity.this);
//        listView.setAdapter(orderFinshedAdapter);

//        orderFinishedItemAdapter = orderFinshedAdapter.orderFinishedItemAdapter;
//        orderFinishedItemAdapter.setEnsureListener(new OrderFinishedItemAdapter.EnsureListener() {
//            @Override
//            public void ensure(String orderID) {
//                completeOrderBackground(orderID);
//            }
//        });

    }

    //完成订单
    private void completeOrderBackground(String orderID) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tokenID", GlobalStaticConstant.tokenID);
            jsonObject.put("orderID", orderID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "completeOrderBackground: " + jsonObject);

        RequestParams params = new RequestParams();
        params.addBodyParameter("data", jsonObject.toString());
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, GlobalStaticConstant.localhost + "complete_order_background/", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e(TAG, "onSuccess: " + responseInfo.result);
                getData();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e(TAG, "onFailure: " + e + s);

            }
        });
    }

    private void getData() {
        if (startIndex == 0) {
            beanList.clear();

        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tokenID", GlobalStaticConstant.tokenID);
            jsonObject.put("orderState", "-1");
            jsonObject.put("orderType", orderType);
            jsonObject.put("startIndex", startIndex);
            jsonObject.put("pageCount", GlobalStaticConstant.pageCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "getData: " + jsonObject);
        RequestParams params = new RequestParams();
        params.addBodyParameter("data", jsonObject.toString());
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, GlobalStaticConstant.localhost + "get_group_list_background/", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject result = new JSONObject(responseInfo.result);
                    String status = result.getString("status");
                    if (status.equals("SUCCESS")) {
                        JSONObject data = result.getJSONObject("data");
                        JSONArray dataList = data.getJSONArray("dataList");

                        Log.e(TAG, "onSuccess: ===============" + dataList.length());

                        for (int i = 0; i < dataList.length(); i++) {
                            List<OrderFinshedItemBean> itemBeanLists = new ArrayList<>();

                            JSONObject object = dataList.getJSONObject(i);
                            OrderFinshedBean bean = new OrderFinshedBean();
                            bean.setShop_name(object.getString("ticketNum"));
                            bean.setOrderState(object.getString("orderState"));
                            bean.setTime(object.getString("createTime"));
                            bean.setTotal_fee("合计:￥" + object.getString("totalFee"));

                            JSONArray merchandiseList = object.getJSONArray("merchandiseList");

                            bean.setItemBeanList(getItemData(itemBeanLists, merchandiseList));

                            beanList.add(bean);
                        }

                        Log.e(TAG, "onSuccess: " + beanList);

                        for (int i = 0; i < beanList.size(); i++) {
                            expandableListView.expandGroup(i);
                        }

                        expandableListAdapter.notifyDataSetChanged();

//                        recyclerViewFatherAdapter.notifyDataSetChanged();
//                        recyclerViewFatherAdapter.notifyDataSetChanged();


//                        pullToRefreshListView.onRefreshComplete();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e(TAG, "onFailure: " + e + s);

            }
        });
    }

    public List<OrderFinshedItemBean> getItemData(List<OrderFinshedItemBean> beanList, JSONArray merchandiseList) {

        for (int j = 0; j < merchandiseList.length(); j++) {
            try {
                JSONObject item = merchandiseList.getJSONObject(j);
                OrderFinshedItemBean itemBean = new OrderFinshedItemBean();

                itemBean.setImagePath(item.getString("path"));
                itemBean.setName(item.getString("merchandiseName"));
                itemBean.setPrice(item.getString("price"));
                itemBean.setCount(item.getString("count"));
                itemBean.setOrderID(item.getString("orderID"));
                itemBean.setOrderState(item.getString("serviceState"));

                Log.e(TAG, "getItemData: "+ item.getString("serviceState"));

                beanList.add(itemBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return beanList;

    }


}
