package com.example.administrator.restaurantmanage.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.restaurantmanage.R;
import com.example.administrator.restaurantmanage.bean.OrderFinshedBean;
import com.example.administrator.restaurantmanage.bean.OrderFinshedItemBean;
import com.example.administrator.restaurantmanage.orderManage.OrderFinishedActivity;
import com.example.administrator.restaurantmanage.utils.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/9 0009.
 */

public class OrderFinishedItemAdapter extends BaseAdapter {
    private List<OrderFinshedItemBean> beanList;
    private Context context;
    private LayoutInflater inflater;
    private BitmapUtils bitmapUtils;
    public EnsureListener ensureListener;
    private static final String TAG = "OrderFinishedItemAdapte";

    public interface EnsureListener{
        void ensure(String orderID);
    }

    public void setEnsureListener(EnsureListener ensureListener){
        this.ensureListener = ensureListener;
    }

    public OrderFinishedItemAdapter(Context context,List<OrderFinshedItemBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        bitmapUtils = BitmapHelper.getBitmapUtils(context);
//        this.beanList = beanList;
    }

    public OrderFinishedItemAdapter(Context context) {
        this.context = context;

        bitmapUtils = BitmapHelper.getBitmapUtils(context);
//        this.beanList = beanList;
    }

    public void addAll(List<OrderFinshedItemBean> beanList){
        this.beanList = beanList;
        Log.e(TAG, "addAll: aaaaaaaaaaaaaaaaaaa");
        notifyDataSetChanged();
    }

//    public void addAll(){
//        notifyDataSetChanged();
//    }

    public void clearAll(){
        this.beanList.clear();
        notifyDataSetChanged();
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
            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_finished_order,null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.id_image);
            viewHolder.food_name = (TextView) view.findViewById(R.id.id_name);
            viewHolder.price = (TextView) view.findViewById(R.id.id_price);
            viewHolder.count = (TextView) view.findViewById(R.id.id_count);
            viewHolder.order_state = (TextView) view.findViewById(R.id.id_order_state);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final OrderFinshedItemBean bean = beanList.get(i);
        if (bean != null) {
            bitmapUtils.display(viewHolder.imageView, bean.getImagePath());
            viewHolder.food_name.setText(bean.getName());
            viewHolder.price.setText("￥"+bean.getPrice());
            viewHolder.count.setText("×"+ bean.getCount());

            if (bean.getOrderState().equals("1")){
                viewHolder.order_state.setText("未上菜");
                viewHolder.order_state.setBackgroundResource(R.drawable.item_shape);
                viewHolder.order_state.setTextColor(context.getResources().getColor(R.color.textDefaultColor));
                viewHolder.order_state.setClickable(true);

            }else {

                viewHolder.order_state.setText("已上菜");
                viewHolder.order_state.setBackgroundResource(R.drawable.item_select_shape);
                viewHolder.order_state.setTextColor(ContextCompat.getColor(context,R.color.white));
                viewHolder.order_state.setClickable(false);
            }

            Log.e(TAG,i+ "getView: ================="+bean.toString() );


        }
        viewHolder.order_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ensureListener.ensure(bean.getOrderID());
            }
        });

        return view;
    }

    class ViewHolder{
        ImageView imageView;
        TextView food_name;
        TextView price;
        TextView count;
        TextView order_state;
    }
}
