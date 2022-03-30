package com.example.test;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.test.common.database.DatabaseUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText email;
    private EditText password;
    private EditText password_again;

    private Button email_resetting_button;
    private Button email_register_button;
    private Button email_cancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password_again = findViewById(R.id.password_again);
        email_resetting_button = findViewById(R.id.email_resetting_button);
        email_register_button = findViewById(R.id.email_register_button);
        email_cancel = findViewById(R.id.email_cancel);


        email_resetting_button.setOnClickListener(this);
        email_register_button.setOnClickListener(this);
        email_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.email_resetting_button:
                //输入内容重置
                email.setText("");
                password.setText("");
                password_again.setText("");
                break;
            case R.id.email_register_button:
                if(TextUtils.isEmpty(email.getText().toString())){
                    Toast.makeText(this,"用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password.getText().toString())){
                    Toast.makeText(this,"密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password_again.getText().toString())){
                    Toast.makeText(this,"确认密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!password.getText().toString().equals(password_again.getText().toString())){
                    Toast.makeText(this,"2次输入密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //注册的数据保存在数据库中
                        long index = DatabaseUtils.saveUser(email.getText().toString(),password.getText().toString());
                        if(index == -1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this,"注册失败!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else if(index == -2){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this,"用户名已经存在!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this,"注册成功,请登录!", Toast.LENGTH_SHORT).show();
                                    RegisterActivity.this.finish();
                                }
                            });
                        }

                    }
                });
                thread.start();

                break;
            case R.id.email_cancel:
                this.finish();
                break;
        }
    }
}