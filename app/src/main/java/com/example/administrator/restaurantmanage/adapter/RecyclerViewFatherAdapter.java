package com.example.administrator.restaurantmanage.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.restaurantmanage.R;
import com.example.administrator.restaurantmanage.bean.OrderFinshedBean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/11 0011.
 */

public class RecyclerViewFatherAdapter extends RecyclerView.Adapter<RecyclerViewFatherAdapter.ViewHolder>{
    private List<OrderFinshedBean> beanList;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private RecyclerViewItemAdapter adapter;

    public RecyclerViewFatherAdapter(List<OrderFinshedBean> beanList,Context context) {
        this.beanList = beanList;
        this.context = context;
        adapter = new RecyclerViewItemAdapter(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_finished_order_father,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final OrderFinshedBean bean = beanList.get(position);
        if (bean != null) {
//            bitmapUtils.display(viewHolder.shop_image, bean.getImagePath());
            holder.shop_name.setText(bean.getShop_name());
            switch (bean.getOrderState()){

                case "1":
                    holder.order_state.setText("未支付");
                    break;
                case "2":
                    holder.order_state.setText("已支付");
                    break;
                case "3":
                    holder.order_state.setText("已完成");
                    break;
            }

            holder.time.setText(bean.getTime());
            holder.total_fee.setText(bean.getTotal_fee());

            layoutManager = new LinearLayoutManager(context);
            holder.listView.setLayoutManager(layoutManager);
            holder.listView.setAdapter(adapter);
            final OrderFinshedBean bean1 = beanList.get(position);
            adapter.addData(bean1.getItemBeanList());
//            adapter.addData(bean.getItemBeanList());

        }

    }

    @Override
    public int getItemCount() {
        return beanList==null? 0: beanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView shop_name;
        TextView order_state;
        RecyclerView listView;
        TextView time;
        TextView total_fee;
        public ViewHolder(View itemView) {
            super(itemView);
            shop_name = (TextView) itemView.findViewById(R.id.id_shop_name);
            listView = (RecyclerView) itemView.findViewById(R.id.id_item_finish_listview);
            order_state = (TextView) itemView.findViewById(R.id.id_order_state);
            time = (TextView) itemView.findViewById(R.id.id_time);
            total_fee = (TextView) itemView.findViewById(R.id.id_total_fee);
        }
    }
}
