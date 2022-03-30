package com.example.test.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.example.test.R;
import com.example.test.adapter.ShowProductListAdapter;
import com.example.test.common.database.DatabaseUtils;

import androidx.fragment.app.Fragment;


public class OneFragment extends Fragment {

    private ListView listview;
    private ShowProductListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View mainView = inflater.inflate(R.layout.fragment_one, container, false);
        init(mainView);
        return mainView;
    }

    private void init(View mainView) {
        listview = mainView.findViewById(R.id.listview);
        adapter = new ShowProductListAdapter(getActivity());//数据渲染
        listview.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        adapter.getList().clear();
        adapter.addList(DatabaseUtils.getProductInfoList2());
        adapter.notifyDataSetChanged();
    }
}
