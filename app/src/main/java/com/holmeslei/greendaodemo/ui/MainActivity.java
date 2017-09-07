package com.holmeslei.greendaodemo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.holmeslei.greendaodemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_gd: //GreenDao3实现
                startActivity(new Intent(this, GreenDaoActivity.class));
                break;
            case R.id.btn_gd_rx: //GreenDao3+RxJava2实现
                startActivity(new Intent(this, RxDaoActivity.class));
                break;
        }
    }
}
