package com.example.test;

import android.content.Intent;
import android.os.Bundle;

import com.example.test.common.database.DatabaseUtils;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddProductActivity extends AppCompatActivity {

    Button btn_save;
    EditText name;
    EditText describe;
    EditText old_price;
    EditText new_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().hide();
        btn_save = findViewById(R.id.btn_save);
        name = findViewById(R.id.name);
        describe = findViewById(R.id.describe);
        old_price = findViewById(R.id.old_price);
        new_price = findViewById(R.id.new_price);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseUtils.saveProduct(name.getText().toString(),describe.getText().toString(),old_price.getText().toString(),new_price.getText().toString());
                Toast.makeText(AddProductActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                AddProductActivity.this.finish();
            }
        });
    }


}
