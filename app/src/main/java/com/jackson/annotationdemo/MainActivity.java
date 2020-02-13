package com.jackson.annotationdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jackson.bindviewannotation.BindView;
import com.jackson.bindviewapi.ViewBinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_bind)
    public Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewBinder.bind(this);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"注解测试",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
