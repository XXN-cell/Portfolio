package com.example.test.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.model.Product;

import java.util.ArrayList;

public class ShowProductListAdapter extends BaseAdapter {

    private ArrayList<Product> data = new ArrayList<Product>();
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    public ShowProductListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void addList(ArrayList<Product> list)
    {
        this.data.addAll(list);
    }

    public ArrayList<Product> getList()
    {
        return data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Product getItem(int position) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_show_product_list, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.describe);
            viewHolder.old_price = (TextView) convertView.findViewById(R.id.old_price);
            viewHolder.new_price = (TextView) convertView.findViewById(R.id.new_price);
            viewHolder.icon_add = (ImageView) convertView.findViewById(R.id.icon_add);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Product item = getItem(position);
        viewHolder.name.setText(item.getName());
        viewHolder.describe.setText(item.getDescribe());
        viewHolder.old_price.setText(item.getOld_price());
        viewHolder.new_price.setText(item.getNew_price());
        viewHolder.icon_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("ShopCart");
                intent.putExtra("data",item);
                mContext.sendBroadcast(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView describe;
        TextView old_price;
        TextView new_price;
        ImageView icon_add;
    }
}