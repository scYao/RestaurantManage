package com.example.administrator.restaurantmanage.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.restaurantmanage.R;
import com.example.administrator.restaurantmanage.bean.OrderFinshedBean;
import com.example.administrator.restaurantmanage.bean.OrderFinshedItemBean;
import com.example.administrator.restaurantmanage.utils.BitmapHelper;
import com.example.administrator.restaurantmanage.utils.ChildLiistView;
import com.example.administrator.restaurantmanage.utils.Utility;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/9 0009.
 */

public class OrderFinshedAdapter extends BaseAdapter implements ListAdapter{
    private List<OrderFinshedBean> beanList;
    private List<OrderFinshedItemBean> itemBeanList;
    private LayoutInflater inflater;
    private Context context;
    public OrderFinishedItemAdapter orderFinishedItemAdapter;
    private BitmapUtils bitmapUtils;
    private static final String TAG = "OrderFinshedAdapter";

    public OrderFinshedAdapter(List<OrderFinshedBean> beanList, Context context) {
        this.beanList = beanList;
        this.context = context;
//        this.itemBeanList= itemBeanList;
        inflater = LayoutInflater.from(context);
        bitmapUtils = BitmapHelper.getBitmapUtils(context);
        orderFinishedItemAdapter = new OrderFinishedItemAdapter(context);

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (viewGroup instanceof ListView){

        }
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item_finished_order_father,null);
//            viewHolder.shop_image = (ImageView) view.findViewById(R.id.id_shop_image);
            viewHolder.shop_name = (TextView) view.findViewById(R.id.id_shop_name);
            viewHolder.listView = (ListView) view.findViewById(R.id.id_item_finish_listview);
//            viewHolder.listView = (ChildLiistView) view.findViewById(R.id.id_item_finish_listview);
            viewHolder.order_state = (TextView) view.findViewById(R.id.id_order_state);
            viewHolder.time = (TextView) view.findViewById(R.id.id_time);
            viewHolder.total_fee = (TextView) view.findViewById(R.id.id_total_fee);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        OrderFinshedBean bean = beanList.get(i);
        if (bean != null) {
//            bitmapUtils.display(viewHolder.shop_image, bean.getImagePath());
            viewHolder.shop_name.setText(bean.getShop_name());
            switch (bean.getOrderState()){

                case "1":
                    viewHolder.order_state.setText("未支付");
                    break;
                case "2":
                    viewHolder.order_state.setText("已支付");
                    break;
                case "3":
                    viewHolder.order_state.setText("已完成");
                    break;
            }
            viewHolder.time.setText(bean.getTime());
            viewHolder.total_fee.setText(bean.getTotal_fee());
            Log.e(TAG, "getView: .addAll(bean.getItemBeanList());");

            orderFinishedItemAdapter.addAll(beanList.get(i).getItemBeanList());

            viewHolder.listView.setAdapter(orderFinishedItemAdapter);

            Utility.setListViewHeightBasedOnChildren(viewHolder.listView);
        }

        return view;

    }

    class ViewHolder{
        TextView shop_name;
        TextView order_state;
        ListView listView;
//        ChildLiistView listView;
        TextView time;
        TextView total_fee;
    }
}
