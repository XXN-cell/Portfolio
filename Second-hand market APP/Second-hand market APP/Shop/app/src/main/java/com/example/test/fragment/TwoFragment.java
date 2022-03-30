package com.example.test.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.TestContext;
import com.example.test.adapter.ShopCartAdapter;
import com.example.test.adapter.ShowProductListAdapter;
import com.example.test.common.database.DatabaseUtils;
import com.example.test.model.Product;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.fragment.app.Fragment;


public class TwoFragment extends Fragment {

    private MyBroadcastReceiver myBroadcastReceiver;
    private Button btn_pay;
    private ListView listview;
    private ShopCartAdapter adapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View mainView = inflater.inflate(R.layout.fragment_two, container, false);
        init(mainView);
        return mainView;
    }

    private void init(View mainView) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ShopCart");
        myBroadcastReceiver = new MyBroadcastReceiver();
        getActivity().registerReceiver(myBroadcastReceiver, intentFilter);

        btn_pay = mainView.findViewById(R.id.btn_pay);
        listview = mainView.findViewById(R.id.listview);
        adapter = new ShopCartAdapter(getActivity());
        listview.setAdapter(adapter);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Product> data = adapter.getList();
                Gson gson = new Gson();
                for(int i=0;i<data.size();i++){
                    DatabaseUtils.saveOrder(TestContext.getInstance().getCurrentUser().getUser_name(),gson.toJson(data.get(i)),sdf.format(new Date()));
                }
                adapter.getList().clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"支付成功,请到我的订单中查看!",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Product product = (Product) intent.getSerializableExtra("data");
            adapter.getList().add(product);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(myBroadcastReceiver);
    }
}
