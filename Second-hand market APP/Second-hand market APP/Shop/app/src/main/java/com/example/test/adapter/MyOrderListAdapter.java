package com.example.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.model.Order;
import com.example.test.model.Product;

import java.util.ArrayList;

public class MyOrderListAdapter extends BaseAdapter {

    private ArrayList<Order> data = new ArrayList<Order>();
    private LayoutInflater mLayoutInflater;
    public MyOrderListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(ArrayList<Order> list)
    {
        this.data.addAll(list);
    }

    public ArrayList<Order> getList()
    {
        return data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Order getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_my_order_list, null);
            viewHolder = new ViewHolder();
            viewHolder.order_name = (TextView) convertView.findViewById(R.id.order_name);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.describe);
            viewHolder.old_price = (TextView) convertView.findViewById(R.id.old_price);
            viewHolder.new_price = (TextView) convertView.findViewById(R.id.new_price);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Order order = getItem(position);
        Product item = order.getProduct();
        viewHolder.name.setText(item.getName());
        viewHolder.describe.setText(item.getDescribe());
        viewHolder.old_price.setText(item.getOld_price());
        viewHolder.new_price.setText(item.getNew_price());

        viewHolder.order_name.setText(order.getOrder_name());
        viewHolder.time.setText(order.getTime());
        return convertView;
    }

    static class ViewHolder {
        TextView order_name;
        TextView name;
        TextView describe;
        TextView old_price;
        TextView new_price;
        TextView time;
    }
}