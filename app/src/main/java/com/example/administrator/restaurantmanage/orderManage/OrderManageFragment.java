package com.example.administrator.restaurantmanage.orderManage;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.restaurantmanage.R;
import com.example.administrator.restaurantmanage.adapter.OrderManageAdapter;
import com.example.administrator.restaurantmanage.bean.OrderManageBean;
import com.example.administrator.restaurantmanage.utils.GlobalStaticConstant;
import com.example.administrator.restaurantmanage.view.SwipeRefreshView;
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


/**
 * Created by Administrator on 2017/8/1 0001.
 */

public class OrderManageFragment extends Fragment implements AdapterView.OnItemClickListener {
    //控件声明
    private ListView order_listView1, order_listView2;

    private OrderManageAdapter orderManageAdapter1, orderManageAdapter2;
    private List<OrderManageBean> orderManageBeanList = new ArrayList<>();

    private static final String TAG = "OrderManageFragment";

    //viewpager
//    @ViewInject(R.id.id_viewpager)
//    private ViewPager viewPager;
//    private List<View> viewList = new ArrayList<>();
//    private PagerAdapter pagerAdapter;
//
//    @ViewInject(R.id.id_order_no)
//    private TextView order_no;
//    @ViewInject(R.id.id_order_yes)
//    private TextView order_yes;

    @ViewInject(R.id.id_lock)
    private ImageView lock;


    private static int startIndex1 = 0;
    private static int startIndex2 = 0;
    private PullToRefreshListView pullToRefreshListView1;
    private PullToRefreshListView pullToRefreshListView2;

    private static String state ="1";

    @ViewInject(R.id.id_order_listView)
    private ListView listView;
    private PullToRefreshListView pullToRefreshListView;
    private OrderManageAdapter orderManageAdapter;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_manage_viewpager, container, false);
        ViewUtils.inject(getActivity(), view);

        initData("-1", 0);
        initView(view);

//        order_no.setOnClickListener(this);
//        order_yes.setOnClickListener(this);

        return view;

    }


    //通知后台是否有人
    public void releaseTableBackground(String tokenID, String tableID, String url, final String s) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tokenID", tokenID);
            jsonObject.put("tableID", tableID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("data", jsonObject.toString());

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, GlobalStaticConstant.localhost + url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    String status = object.getString("status");

                    if (status.equals("SUCCESS")) {
                        Log.e(TAG, "onSuccess: +++++++++++++++++++" + s);
                        initData(s, 0);
                    } else {

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


    private void initView(View view) {
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View view1 = inflater.inflate(R.layout.order_manage_fragment1, null);
//        View view2 = inflater.inflate(R.layout.order_manage_fragment2, null);
//
//        viewList.add(view1);
//        viewList.add(view2);
//
//        pagerAdapter = new PagerAdapter() {
//
//            @Override
//            public Object instantiateItem(ViewGroup container, int position) {
//                View view = viewList.get(position);
//                container.addView(view);
//                return view;
//            }
//
//            @Override
//            public void destroyItem(ViewGroup container, int position, Object object) {
//                container.removeView(viewList.get(position));
//            }
//
//            @Override
//            public int getCount() {
//                return viewList.size();
//            }
//
//            @Override
//            public boolean isViewFromObject(View view, Object object) {
//                return view == object;
//            }
//        };
//
//        viewPager.setAdapter(pagerAdapter);
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                resetBackground();
//                int current = viewPager.getCurrentItem();
//                switch (current) {
//
//                    case 0:
//                        order_no.setBackgroundColor(getActivity().getResources().getColor(R.color.textDefaultColor));
//                        order_no.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
////                        initData("1");
//                        break;
//
//                    case 1:
//                        order_yes.setBackgroundColor(getActivity().getResources().getColor(R.color.textDefaultColor));
//                        order_yes.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
////                        initData("2");
//                        break;
//                }
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

//        order_listView1 = (ListView) view1.findViewById(R.id.id_order_listView1);

//        pullToRefreshListView1 = (PullToRefreshListView) view1.findViewById(R.id.id_order_listView1);
//        pullToRefreshListView1.setMode(PullToRefreshBase.Mode.BOTH);
//        order_listView1 = pullToRefreshListView1.getRefreshableView();
//        orderManageAdapter1 = new OrderManageAdapter(orderManageBeanList, getActivity(), this, "1");
//        order_listView1.setAdapter(orderManageAdapter1);
//
//
//        pullToRefreshListView1.setOnRefreshListener(refreshListener);
//
//
//        pullToRefreshListView2 = (PullToRefreshListView) view2.findViewById(R.id.id_order_listView2);
//        pullToRefreshListView2.setMode(PullToRefreshBase.Mode.BOTH);
//        order_listView2 = pullToRefreshListView2.getRefreshableView();
//        orderManageAdapter2 = new OrderManageAdapter(orderManageBeanList, getActivity(), this, "2");
//        order_listView2.setAdapter(orderManageAdapter2);
//
//        pullToRefreshListView2.setOnRefreshListener(refreshListener);
//
//        order_listView1.setOnItemClickListener(this);
//        order_listView2.setOnItemClickListener(itemListener2);



        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.id_order_listView);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        listView = pullToRefreshListView.getRefreshableView();
        orderManageAdapter = new OrderManageAdapter(orderManageBeanList, getActivity(), this, "1");
        listView.setAdapter(orderManageAdapter);
        listView.setOnItemClickListener(this);
        pullToRefreshListView.setOnRefreshListener(refreshListener);
    }

    private PullToRefreshBase.OnRefreshListener2 refreshListener = new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
           initData("-1",0);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {

                startIndex1 = startIndex1 + 1;
                initData("-11", startIndex1);

        }
    };


//    private void resetBackground() {
//
//        order_no.setBackgroundColor(getActivity().getResources().getColor(R.color.backgroundColor));
//        order_yes.setBackgroundColor(getActivity().getResources().getColor(R.color.backgroundColor));
//
//        order_no.setTextColor(getActivity().getResources().getColor(R.color.textDefaultColor));
//        order_yes.setTextColor(getActivity().getResources().getColor(R.color.textDefaultColor));
//    }

    public void initData(final String orderState, int startIndex) {
        Log.e(TAG, "initData: ======================================================================");

        if (startIndex == 0) {
            orderManageBeanList.clear();
        }



        JSONObject object = new JSONObject();
//        Log.e(TAG, "initData: "+tokenID);
        try {
            object.put("tokenID", GlobalStaticConstant.tokenID);
            object.put("orderState", orderState);//1未付款,2已付款
            object.put("startIndex", startIndex);
            object.put("pageCount", GlobalStaticConstant.pageCount);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("data", object.toString());

        Log.e(TAG, "initData:参数 b " + object);

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST,
                GlobalStaticConstant.localhost + "get_table_state_list_background/",
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        JSONObject result = null;
                        try {
                            result = new JSONObject(responseInfo.result);
                            String status = result.getString("status");

                            if (status.equals("SUCCESS")) {
                                // Toast or alert error info
                                JSONObject data = result.getJSONObject("data");

                                JSONArray dataList = data.getJSONArray("dataList");

//                                Log.e(TAG, "onSuccess: "+dataList.length());
                                Log.e(TAG, "onSuccess:========================" + dataList);
                                for (int i = 0; i < dataList.length(); i++) {

                                    OrderManageBean bean = new OrderManageBean();
                                    JSONObject o = dataList.getJSONObject(i);


//

                                    bean.setNumber("" + i);
                                    bean.setTable_number(o.getString("tableName"));
                                    bean.setState(o.getString("state"));
                                    bean.setGroupID(o.getString("groupID"));
                                    bean.setTableID(o.getString("tableID"));
//                                    bean.setOrderID(o.getString("orderID"));
                                    bean.setOrderType("1");
                                    bean.setOrderState(orderState);
//                                    bean.set

                                    orderManageBeanList.add(bean);
                                }

                                orderManageAdapter.notifyDataSetChanged();
                                pullToRefreshListView.onRefreshComplete();

//                                //隐藏或显示加载
//                                if (orderState.equals("1")) {
//
//                                    orderManageAdapter1.notifyDataSetChanged();
//                                    Log.e(TAG, "onSuccess: djkslfdjh的科技发达了健康");
//                                    pullToRefreshListView1.onRefreshComplete();
//                                } else {
//
//                                    Log.e(TAG, "onSuccess: 策略是浪费了 ");
//                                    orderManageAdapter2.notifyDataSetChanged();
//                                    pullToRefreshListView1.onRefreshComplete();
//
//                                }
                                // 加载完数据设置为不刷新状态，将下拉进度收起来
//                                if (swipeRefreshView1.isRefreshing()) {
//                                    swipeRefreshView1.setRefreshing(false);
//                                } else {
//                                    swipeRefreshView1.setLoading(false);
//                                }


                            } else {
                                String data = result.getString("data");
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


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        OrderManageBean bean = (OrderManageBean) adapterView.getAdapter().getItem(i);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("orderState", bean.getOrderState());
        bundle.putString("orderType", bean.getOrderType());
        intent.putExtras(bundle);
        intent.setClass(getActivity(), OrderFinishedActivity.class);

        startActivity(intent);
    }

    private AdapterView.OnItemClickListener itemListener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            OrderManageBean bean = (OrderManageBean) adapterView.getAdapter().getItem(i);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("groupID", bean.getGroupID());
            intent.putExtras(bundle);
            intent.setClass(getActivity(), OrderDetailActivity.class);

            startActivity(intent);
        }
    };

//    @Override
//    public void onClick(View view) {
//        resetBackground();
//        switch (view.getId()) {
//            case R.id.id_order_no:
//                viewPager.setCurrentItem(0);
//                order_no.setBackgroundColor(getActivity().getResources().getColor(R.color.textDefaultColor));
//                order_no.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
//                initData("1", 0);
//                state = "1";
//                break;
//
//            case R.id.id_order_yes:
//                order_yes.setBackgroundColor(getActivity().getResources().getColor(R.color.textDefaultColor));
//                order_yes.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
//                initData("2", 0);
//                state = "2";
//                break;
//        }
//
//    }
}
