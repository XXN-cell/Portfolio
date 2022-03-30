package com.example.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.test.common.database.DatabaseUtils;
import com.example.test.model.Product;

import androidx.appcompat.app.AppCompatActivity;

public class EditProductActivity extends AppCompatActivity {
    Button btn_save;
    EditText name;
    EditText describe;
    EditText old_price;
    EditText new_price;
    Product data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        getSupportActionBar().hide();
        data = (Product) getIntent().getSerializableExtra("data");
        btn_save = findViewById(R.id.btn_save);
        name = findViewById(R.id.name);
        describe = findViewById(R.id.describe);
        old_price = findViewById(R.id.old_price);
        new_price = findViewById(R.id.new_price);

        name.setText(data.getName());
        describe.setText(data.getDescribe());
        old_price.setText(data.getOld_price());
        new_price.setText(data.getNew_price());
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setName(name.getText().toString());
                data.setDescribe(describe.getText().toString());
                data.setNew_price(new_price.getText().toString());
                data.setOld_price(old_price.getText().toString());
                DatabaseUtils.saveProduct(data);
                Toast.makeText(EditProductActivity.this,"编辑成功",Toast.LENGTH_LONG).show();
                EditProductActivity.this.finish();
            }
        });
    }


}
