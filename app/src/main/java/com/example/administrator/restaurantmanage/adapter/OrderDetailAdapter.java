package com.example.administrator.restaurantmanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.restaurantmanage.R;
import com.example.administrator.restaurantmanage.bean.OrderDetailBean;
import com.example.administrator.restaurantmanage.utils.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1 0001.
 */

public class OrderDetailAdapter extends BaseAdapter{

    private List<OrderDetailBean> beanList;
    private LayoutInflater inflater;
    private Context context;
    private BitmapUtils bitmapUtils;

    public OrderDetailAdapter(List<OrderDetailBean> beanList, Context context) {
        this.beanList = beanList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        bitmapUtils = BitmapHelper.getBitmapUtils(context);
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
        ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item_order_detail, null);
            viewHolder.image = (ImageView) view.findViewById(R.id.id_image);
            viewHolder.description = (TextView) view.findViewById(R.id.id_description);
            viewHolder.price = (TextView) view.findViewById(R.id.id_price);
            viewHolder.count = (TextView) view.findViewById(R.id.id_count);

            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        OrderDetailBean orderManageBean = beanList.get(i);
        if (orderManageBean != null){
            bitmapUtils.display(viewHolder.image,orderManageBean.getImgPath());
            viewHolder.description.setText(orderManageBean.getDescription());
            viewHolder.price.setText("×"+orderManageBean.getPrice());
            viewHolder.count.setText("￥"+orderManageBean.getSelectedCount());
        }
        return view;
    }

    class ViewHolder{
        ImageView image;
        TextView description;
        TextView price;
        TextView count;
    }
}
