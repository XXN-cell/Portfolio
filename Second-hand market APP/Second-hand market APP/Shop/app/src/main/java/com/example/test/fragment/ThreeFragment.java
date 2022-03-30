package com.example.test.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test.MyOrderActivity;
import com.example.test.MyProductListActivity;
import com.example.test.R;
import com.example.test.TestContext;
import com.example.test.common.database.DatabaseUtils;

import androidx.fragment.app.Fragment;

public class ThreeFragment extends Fragment {
    private Button button1=null;
    private Button button2=null;
    private Button button3=null;
    private Button my_order;
    private Button my_product;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View mainView = inflater.inflate(R.layout.fragment_three, container, false);
        init(mainView);
        initData();
        return mainView;
    }

    private void init(View mainView) {
        my_order=(Button)mainView.findViewById(R.id.my_order);
        my_product=(Button)mainView.findViewById(R.id.my_product);
        button1=(Button)mainView.findViewById(R.id.btn1);
        button2=(Button)mainView.findViewById(R.id.btn2);
        button3=(Button)mainView.findViewById(R.id.btn3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(getActivity());
                new AlertDialog.Builder(getActivity()).setTitle("请输入密码")
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //按下确定键后的事件
                                DatabaseUtils.updateUser(et.getText().toString(),TestContext.getInstance().getCurrentUser().get_id());
                                Toast.makeText(getActivity(),"密码修改成功", Toast.LENGTH_LONG).show();
                                getActivity().finish();
                            }
                        }).setNegativeButton("取消",null).show();

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseUtils.deleteUser(TestContext.getInstance().getCurrentUser().get_id());
                Toast.makeText(getActivity(),"账号注销成功", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void initData() {

        my_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(intent);
            }
        });

        my_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyProductListActivity.class);
                startActivity(intent);
            }
        });
    }

}
