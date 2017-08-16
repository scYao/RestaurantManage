package com.example.administrator.restaurantmanage.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.restaurantmanage.R;
import com.example.administrator.restaurantmanage.bean.OrderFinshedBean;
import com.example.administrator.restaurantmanage.bean.OrderFinshedItemBean;
import com.example.administrator.restaurantmanage.utils.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/14 0014.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private List<OrderFinshedBean> beanList;
    private Context context;
    private LayoutInflater inflater;
    private BitmapUtils bitmapUtils;

    private EnsureOrderListener ensureOrderListener;

    public interface EnsureOrderListener{
        void ensure(String orderID);
    }

    public void setEnsureOrderListener(EnsureOrderListener ensureOrderListener){
        this.ensureOrderListener = ensureOrderListener;
    }

    public ExpandableListAdapter(List<OrderFinshedBean> beanList, Context context) {
        this.beanList = beanList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        bitmapUtils = BitmapHelper.getBitmapUtils(context);
    }

    @Override
    public int getGroupCount() {
        return beanList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return beanList.get(i).getItemBeanList().size();
    }

    @Override
    public Object getGroup(int i) {
        return beanList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return beanList.get(i).getItemBeanList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupHolder groupHolder;
        if(view == null){
            groupHolder = new GroupHolder();
            view = inflater.inflate(R.layout.group,null);
            groupHolder.shop_name = (TextView) view.findViewById(R.id.id_shop_name);
            groupHolder.order_state = (TextView) view.findViewById(R.id.id_order_state);
            view.setTag(groupHolder);
        }else {
            groupHolder= (GroupHolder) view.getTag();
        }

        OrderFinshedBean bean = beanList.get(i);
        if (bean != null) {
            groupHolder.shop_name.setText(bean.getShop_name());
            switch (bean.getOrderState()){

                case "1":
                    groupHolder.order_state.setText("未支付");
                    break;
                case "2":
                    groupHolder.order_state.setText("已支付");
                    break;
                case "3":
                    groupHolder.order_state.setText("已完成");
                    break;
            }
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder childHolder;

        if (view == null){
            childHolder = new ChildHolder();
            view = inflater.inflate(R.layout.child,null);
            childHolder.imageView = (ImageView) view.findViewById(R.id.id_image);
            childHolder.food_name = (TextView) view.findViewById(R.id.id_name);
            childHolder.price = (TextView) view.findViewById(R.id.id_price);
            childHolder.count = (TextView) view.findViewById(R.id.id_count);
            childHolder.order_state = (TextView) view.findViewById(R.id.id_order_state);
            view.setTag(childHolder);
        }else {
            childHolder = (ChildHolder) view.getTag();
        }

        final OrderFinshedItemBean bean = beanList.get(i).getItemBeanList().get(i1);

        if (bean != null){
            bitmapUtils.display(childHolder.imageView, bean.getImagePath());

            childHolder.food_name.setText(bean.getName());
            childHolder.price.setText(bean.getPrice());
            childHolder.count.setText(bean.getPrice());

            if (bean.getOrderState().equals("1")){
                childHolder.order_state.setText("未上菜");
                childHolder.order_state.setBackgroundResource(R.drawable.item_shape);
                childHolder.order_state.setTextColor(context.getResources().getColor(R.color.textDefaultColor));
                childHolder.order_state.setClickable(true);

            }else {

                childHolder.order_state.setText("已上菜");
                childHolder.order_state.setBackgroundResource(R.drawable.item_select_shape);
                childHolder.order_state.setTextColor(ContextCompat.getColor(context,R.color.white));
                childHolder.order_state.setClickable(false);
            }
        }

        childHolder.order_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ensureOrderListener.ensure(bean.getOrderID());
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    class GroupHolder{
        TextView shop_name;
        TextView order_state;
    }

    class ChildHolder{
        ImageView imageView;
        TextView food_name;
        TextView price;
        TextView count;
        TextView order_state;

    }
}
