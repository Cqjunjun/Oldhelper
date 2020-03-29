package com.example.oldhelper;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.remote_control_button)
    Button remoteControlButton;
    @BindView(R.id.auto_control_button)
    Button autoControlButton;
    @BindView(R.id.setting_button)
    Button settingButton;
    @BindView(R.id.abuout_button)
    Button abuoutButton;
    @BindView(R.id.test)
    ImageView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Glide.with(this).load(R.drawable.oldman).into(test);
    }

    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//隐藏状态栏但不隐藏状态栏字体
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏，并且不显示字体
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏文字颜色为暗色

        }
    }

    @OnClick({R.id.remote_control_button, R.id.auto_control_button, R.id.setting_button, R.id.abuout_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.remote_control_button:
                Intent remote_intent = new Intent(this,RemoteControl.class);
                startActivity(remote_intent);
                break;
            case R.id.auto_control_button:
                Intent auto_intent = new Intent(this,AutoControl.class);
                startActivity(auto_intent);
                break;
            case R.id.setting_button:
                Intent setting_intent = new Intent(this,Setting.class);
                startActivity(setting_intent);
                break;
            case R.id.abuout_button:
                Intent about_intent = new Intent(this,About.class);
                startActivity(about_intent);
                break;
        }
    }
}
