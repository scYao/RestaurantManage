package com.example.administrator.restaurantmanage.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.restaurantmanage.R;
import com.example.administrator.restaurantmanage.bean.OrderManageBean;
import com.example.administrator.restaurantmanage.orderManage.OrderManageFragment;
import com.example.administrator.restaurantmanage.utils.BitmapHelper;
import com.example.administrator.restaurantmanage.utils.GlobalStaticConstant;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1 0001.
 */

public class OrderManageAdapter extends BaseAdapter {

    private List<OrderManageBean> beanList;
    private LayoutInflater inflater;
    private Context context;
    private BitmapUtils bitmapUtils;
    private static final String TAG = "OrderManageAdapter";
    private OrderManageFragment fragment;
    private String orderState;

    public interface ResetListenr{
        void reLock(int positon);
    }

    private ResetListenr resetListenr;

    public void setResetListener(ResetListenr resetListenr){
        this.resetListenr = resetListenr;
    }

    public OrderManageAdapter(List<OrderManageBean> beanList, Context context, OrderManageFragment fragment, String orderState) {
        this.beanList = beanList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        bitmapUtils = BitmapHelper.getBitmapUtils(context);
        this.fragment = fragment;
        this.orderState = orderState;
    }

    @Override
    public int getCount() {
        return beanList.size();
    }

    @Override
    public Object getItem(int i) {
        return beanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item_order_manage, null);

            viewHolder.linearLayout = (LinearLayout) view.findViewById(R.id.id_item_layout);
            viewHolder.number = (TextView) view.findViewById(R.id.id_number);
            viewHolder.table_number = (TextView) view.findViewById(R.id.id_table_number);
            viewHolder.state = (TextView) view.findViewById(R.id.id_state);
            viewHolder.lock = (ImageView) view.findViewById(R.id.id_lock);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final OrderManageBean orderManageBean = beanList.get(i);
        if (orderManageBean != null) {
            viewHolder.number.setText(orderManageBean.getNumber());

            if (Integer.parseInt(orderManageBean.getState()) == 1) {
                viewHolder.state.setText("无人");
                viewHolder.linearLayout.setBackgroundResource(R.drawable.item_shape);
                viewHolder.lock.setImageResource(R.mipmap.unlock);
                viewHolder.lock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment.releaseTableBackground(GlobalStaticConstant.tokenID,orderManageBean.getTableID(),"lock_table_background/",orderState);
                    }
                });
            } else {
                viewHolder.state.setText("有人");
                viewHolder.linearLayout.setBackgroundResource(R.drawable.item_select_shape);
                viewHolder.lock.setImageResource(R.mipmap.lock);
                viewHolder.lock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment.releaseTableBackground(GlobalStaticConstant.tokenID,orderManageBean.getTableID(),"release_table_background/",orderState);
                    }
                });
            }



            viewHolder.table_number.setText(orderManageBean.getTable_number());

        }
        return view;
    }

    class ViewHolder {

        LinearLayout linearLayout;
        TextView number;
        TextView table_number;
        TextView state;
        ImageView lock;

    }
}
