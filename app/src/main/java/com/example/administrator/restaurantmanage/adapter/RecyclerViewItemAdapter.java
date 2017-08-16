package com.example.administrator.restaurantmanage.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.restaurantmanage.R;
import com.example.administrator.restaurantmanage.bean.OrderFinshedItemBean;
import com.example.administrator.restaurantmanage.utils.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/11 0011.
 */

public class RecyclerViewItemAdapter extends RecyclerView.Adapter<RecyclerViewItemAdapter.ViewHolder> {

    private List<OrderFinshedItemBean> beanList;
    private BitmapUtils bitmapUtils;
    private Context context;

    public RecyclerViewItemAdapter(Context context) {
        this.context = context;
        bitmapUtils = BitmapHelper.getBitmapUtils(context);
    }

    public void addData(List<OrderFinshedItemBean> beanList) {
        this.beanList = beanList;
//        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_finished_order, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OrderFinshedItemBean bean = beanList.get(position);
        if (bean != null) {
            bitmapUtils.display(holder.imageView, bean.getImagePath());
            holder.food_name.setText(bean.getName());
            holder.price.setText("￥" + bean.getPrice());
            holder.count.setText("×" + bean.getCount());

            if (bean.getOrderState().equals("1")) {
                holder.order_state.setText("未上菜");
                holder.order_state.setBackgroundResource(R.drawable.item_shape);
                holder.order_state.setTextColor(context.getResources().getColor(R.color.textDefaultColor));
                holder.order_state.setClickable(true);

            } else {

                holder.order_state.setText("已上菜");
                holder.order_state.setBackgroundResource(R.drawable.item_select_shape);
                holder.order_state.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.order_state.setClickable(false);
            }

        }

    }

    @Override
    public int getItemCount() {
        return beanList == null ? 0 : beanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView food_name;
        TextView price;
        TextView count;
        TextView order_state;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.id_image);
            food_name = (TextView) itemView.findViewById(R.id.id_name);
            price = (TextView) itemView.findViewById(R.id.id_price);
            count = (TextView) itemView.findViewById(R.id.id_count);
            order_state = (TextView) itemView.findViewById(R.id.id_order_state);

        }
    }
}
