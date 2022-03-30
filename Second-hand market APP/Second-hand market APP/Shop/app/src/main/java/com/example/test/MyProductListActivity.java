package com.example.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.adapter.MyProductListAdapter;
import com.example.test.common.database.DatabaseUtils;
import com.example.test.model.Product;

public class MyProductListActivity extends AppCompatActivity {


    private ListView listview;
    MyProductListAdapter adapter;
    private ImageView add_product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_list);
        getSupportActionBar().hide();
        listview = findViewById(R.id.listview);
        add_product = findViewById(R.id.add_product);
        adapter = new MyProductListAdapter(this);
        listview.setAdapter(adapter);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int pos, long id) {
                new AlertDialog.Builder(MyProductListActivity.this).setTitle("确认删除此商品信息吗")
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
                                Product user = adapter.getItem(pos);
                                long result = DatabaseUtils.deleteProduct(user.get_id());
                                if(result>0){
                                    Toast.makeText(MyProductListActivity.this,"删除成功!",Toast.LENGTH_SHORT).show();
                                }
                                adapter.getList().remove(pos);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .create().show();
                return true;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                Product user = adapter.getItem(pos);
                Intent intent = new Intent(MyProductListActivity.this, EditProductActivity.class);
                intent.putExtra("data",user);
                startActivity(intent);
            }
        });
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProductListActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.getList().clear();
        adapter.getList().addAll(DatabaseUtils.getProductInfoList());
        adapter.notifyDataSetChanged();
    }
}
