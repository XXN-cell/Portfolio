package com.example.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.test.adapter.MyOrderListAdapter;
import com.example.test.common.database.DatabaseUtils;
import com.example.test.model.Order;

import androidx.appcompat.app.AppCompatActivity;

public class MyOrderActivity extends AppCompatActivity {


    private ListView listview;
    MyOrderListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list);
        getSupportActionBar().hide();
        listview = findViewById(R.id.listview);
        adapter = new MyOrderListAdapter(this);
        listview.setAdapter(adapter);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int pos, long id) {
                new AlertDialog.Builder(MyOrderActivity.this).setTitle("确认删除此订单吗")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Order order = adapter.getItem(pos);
                                long result = DatabaseUtils.deleteOrder(order.get_id());
                                if(result>0){
                                    Toast.makeText(MyOrderActivity.this,"删除成功!",Toast.LENGTH_SHORT).show();
                                }
                                adapter.getList().remove(pos);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .create().show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.getList().clear();
        adapter.getList().addAll(DatabaseUtils.getOrderList());
        adapter.notifyDataSetChanged();
    }
}
