package com.example.oldhelper;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kongqw.rockerlibrary.view.RockerView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RemoteControl extends AppCompatActivity {

    @BindView(R.id.video)
    WebView video;
    @BindView(R.id.speedcontrol)
    NumberPicker speedcontrol;
    @BindView(R.id.move_rockerView)
    RockerView moveRockerView;
    @BindView(R.id.machinery_stop_button)
    Button machineryStopButton;
    @BindView(R.id.machinery_up_button)
    Button machineryUpButton;
    @BindView(R.id.machinery_down_button)
    Button machineryDownButton;
    @BindView(R.id.camera_rockerView)
    RockerView cameraRockerView;

    private SendUitl sendUitl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control);
        ButterKnife.bind(this);
        sendUitl = new SendUitl(RemoteControl.this);
        setCameraRockerView();
        videoError();
        setMoveRockerView();
        setSpeedControl();
        setStatusBar();
    }

    @OnClick({R.id.machinery_stop_button, R.id.machinery_up_button, R.id.machinery_down_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.machinery_stop_button:
                sendUitl.sendInstruction(SendUitl.STOP);
                break;
            case R.id.machinery_up_button:
                sendUitl.sendInstruction(SendUitl.MACHINERY_UP);
                break;
            case R.id.machinery_down_button:
                sendUitl.sendInstruction(SendUitl.MACHINERY_DOWN);
                break;
        }
    }
    //视频获取，并在视频失效时重连
    private void videoError() {
        video.loadUrl(SendUitl.VIDEO_PATH);
        video.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(getApplicationContext(),"连接失败！",Toast.LENGTH_SHORT).show();
                view.loadUrl("about:blank");
                sendUitl = new SendUitl(RemoteControl.this);
            }
        });
    }
    //调速计参数改变
    private void setSpeedControl(){
        speedcontrol.setMinValue(0);
        speedcontrol.setMaxValue(2);
        speedcontrol.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                com.example.oldhelper.SendUitl.setLeftSpeedGear(newVal);
                sendUitl.sendInstruction(com.example.oldhelper.SendUitl.SPEED_GEAR);
            }
        });
    }
    //位移摇杆
    private void setMoveRockerView(){
        moveRockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
        // 监听摇动方向
        moveRockerView.setOnShakeListener(RockerView.DirectionMode.DIRECTION_8, new RockerView.OnShakeListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void direction(RockerView.Direction direction) {
                switch (direction){
                    case DIRECTION_CENTER:
                        break;
                    case DIRECTION_UP:
                        sendUitl.sendInstruction(SendUitl.GO_AHEAD);
                        break;
                    case DIRECTION_DOWN:
                        sendUitl.sendInstruction(SendUitl.GO_BACK);
                        break;
                    case DIRECTION_LEFT:
                        sendUitl.sendInstruction(SendUitl.TURN_LEFT);
                        break;
                    case DIRECTION_RIGHT:
                        sendUitl.sendInstruction(SendUitl.TURN_RIGHT);
                        break;
                    case DIRECTION_UP_LEFT:
                        sendUitl.sendInstruction(SendUitl.TURN_LEFT_FORWARD);
                        break;
                    case DIRECTION_UP_RIGHT:
                        sendUitl.sendInstruction(SendUitl.TURN_RIGHT_FORWARD);
                        break;
                    case DIRECTION_DOWN_RIGHT:
                        sendUitl.sendInstruction(SendUitl.TURN_RIGHT_FORWARD);
                        break;
                    case DIRECTION_DOWN_LEFT:
                        sendUitl.sendInstruction(SendUitl.TURN_RIGHT_BACK);
                        break;
                }
            }

            @Override
            public void onFinish() {
            }
        });
    }
    //云台摇杆
    private void setCameraRockerView(){
        cameraRockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
        cameraRockerView.setOnShakeListener(RockerView.DirectionMode.DIRECTION_4_ROTATE_45, new RockerView.OnShakeListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void direction(RockerView.Direction direction) {
                switch (direction) {
                    case DIRECTION_CENTER:
                        break;
                    case DIRECTION_LEFT:
                        sendUitl.sendInstruction(SendUitl.CAMERA_LEFT);
                        break;
                    case DIRECTION_RIGHT:
                        sendUitl.sendInstruction(SendUitl.CAMERA_RIGHT);
                        break;
                    case DIRECTION_UP:
                        sendUitl.sendInstruction(SendUitl.CAMERA_UP);
                        break;
                    case DIRECTION_DOWN:
                        sendUitl.sendInstruction(SendUitl.CAMERA_DOWN);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onFinish() {

            }
        });
    }
    //隐藏状态栏
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//隐藏状态栏但不隐藏状态栏字体
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏，并且不显示字体
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏文字颜色为暗色

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendUitl.closeClient();
    }
}
