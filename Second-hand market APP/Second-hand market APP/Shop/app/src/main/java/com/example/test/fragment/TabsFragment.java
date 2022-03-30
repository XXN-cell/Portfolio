package com.example.test.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class TabsFragment extends Fragment {

    LinearLayout tab_onebg;
    ImageView tab_one_iv;
    TextView tab_one_tv;
    LinearLayout tab_twobg;
    ImageView tab_two_iv;
    TextView tab_two_tv;

    LinearLayout tab_threebg;
    ImageView tab_three_iv;
    TextView tab_three_tv;

    //标准
    Fragment tab_one_fragment;
    //项目
    Fragment tab_two_fragment;
    //项目
    Fragment tab_three_fragment;
    private String currentPage = "";
    private Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        activity = getActivity();
        View mainView = inflater.inflate(R.layout.fragment_toolbar, container, false);
        init(mainView);
        return mainView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }


    void init(View mainView) {//设置点击按钮 对应点击事件
        tab_one_iv =  (ImageView) mainView.findViewById(R.id.tab_one_iv);
        tab_two_iv =  (ImageView) mainView.findViewById(R.id.tab_two_iv);
        tab_three_iv =  (ImageView) mainView.findViewById(R.id.tab_three_iv);

        tab_one_tv =  (TextView) mainView.findViewById(R.id.tab_one_tv);
        tab_two_tv =  (TextView) mainView.findViewById(R.id.tab_two_tv);
        tab_three_tv =  (TextView) mainView.findViewById(R.id.tab_three_tv);

        this.tab_onebg = (LinearLayout) mainView.findViewById(R.id.tab_onebg);
        this.tab_onebg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnTabSelected("tab_one");
            }
        });

        this.tab_twobg = (LinearLayout) mainView.findViewById(R.id.tab_twobg);
        this.tab_twobg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnTabSelected("tab_two");
            }
        });


        this.tab_threebg = (LinearLayout) mainView.findViewById(R.id.tab_threebg);
        this.tab_threebg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnTabSelected("tab_three");
            }
        });
        OnTabSelected("tab_one");
    }

    public void OnTabSelected(String tabName) {
        if(tabName.equals(currentPage)){
            return ;
        }
        FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
        tab_one_fragment =  getFragmentManager().findFragmentByTag("tab_one");
        tab_two_fragment =  getFragmentManager().findFragmentByTag("tab_two");
        tab_three_fragment =  getFragmentManager().findFragmentByTag("tab_three");
        if (tabName == "tab_one") {
            //activity.setTitle(getResources().getString(R.string.tab_one_msg));
            currentPage = "tab_one";
            if (null == tab_one_fragment)
            {
                tab_one_fragment = new OneFragment();
                localFragmentTransaction.add(R.id.fragment_container, tab_one_fragment, "tab_one");
            }else{
                localFragmentTransaction.show(tab_one_fragment);
            }
            if(tab_two_fragment!=null){
                localFragmentTransaction.hide(tab_two_fragment);
            }

            if(tab_three_fragment!=null){
                localFragmentTransaction.hide(tab_three_fragment);
            }
            this.tab_one_iv.setImageResource(R.mipmap.tab_one_pressed);
            this.tab_one_tv.setTextColor(getResources().getColor(R.color.system_text_color_22));
            this.tab_two_iv.setImageResource(R.mipmap.tab_two_normal);
            this.tab_two_tv.setTextColor(getResources().getColor(R.color.system_text_color_11));
            this.tab_three_iv.setImageResource(R.mipmap.tab_three_normal);
            this.tab_three_tv.setTextColor(getResources().getColor(R.color.system_text_color_11));
        }
        else if (tabName == "tab_two") {
            //activity.setTitle(getResources().getString(R.string.tab_two_msg));
            currentPage = "tab_two";
            if (null == tab_two_fragment)
            {
                tab_two_fragment = new TwoFragment();
                localFragmentTransaction.add(R.id.fragment_container, tab_two_fragment, "tab_two");
            }else{
                localFragmentTransaction.show(tab_two_fragment);
            }

            if(tab_one_fragment!=null){
                localFragmentTransaction.hide(tab_one_fragment);
            }
            if(tab_three_fragment!=null){
                localFragmentTransaction.hide(tab_three_fragment);
            }
            this.tab_one_iv.setImageResource(R.mipmap.tab_one_normal);
            this.tab_one_tv.setTextColor(getResources().getColor(R.color.system_text_color_11));
            this.tab_two_iv.setImageResource(R.mipmap.tab_two_pressed);
            this.tab_two_tv.setTextColor(getResources().getColor(R.color.system_text_color_22));

            this.tab_three_iv.setImageResource(R.mipmap.tab_three_normal);
            this.tab_three_tv.setTextColor(getResources().getColor(R.color.system_text_color_11));
        }
        else if (tabName == "tab_three") {
            //activity.setTitle(getResources().getString(R.string.tab_two_msg));
            currentPage = "tab_three";
            if (null == tab_three_fragment)
            {
                tab_three_fragment = new ThreeFragment();
                localFragmentTransaction.add(R.id.fragment_container, tab_three_fragment, "tab_three");
            }else{
                localFragmentTransaction.show(tab_three_fragment);
            }

            if(tab_one_fragment!=null){
                localFragmentTransaction.hide(tab_one_fragment);
            }
            if(tab_two_fragment!=null){
                localFragmentTransaction.hide(tab_two_fragment);
            }
            this.tab_one_iv.setImageResource(R.mipmap.tab_one_normal);
            this.tab_one_tv.setTextColor(getResources().getColor(R.color.system_text_color_11));
            this.tab_two_iv.setImageResource(R.mipmap.tab_two_normal);
            this.tab_two_tv.setTextColor(getResources().getColor(R.color.system_text_color_11));

            this.tab_three_iv.setImageResource(R.mipmap.tab_three_pressed);
            this.tab_three_tv.setTextColor(getResources().getColor(R.color.system_text_color_22));
        }
        localFragmentTransaction.commit();
    }
}