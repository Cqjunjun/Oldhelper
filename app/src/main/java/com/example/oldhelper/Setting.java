package com.example.oldhelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Setting extends AppCompatActivity {

    @BindView(R.id.left_text)
    EditText leftText;
    @BindView(R.id.right_text)
    EditText rightText;
    @BindView(R.id.forward_text)
    EditText forwardText;
    @BindView(R.id.back_text)
    EditText backText;
    @BindView(R.id.leftforward_text)
    EditText leftforwardText;
    @BindView(R.id.rightforward_text)
    EditText rightforwardText;
    @BindView(R.id.leftback_text)
    EditText leftbackText;
    @BindView(R.id.rightback_text)
    EditText rightbackText;
    @BindView(R.id.machinery_up_text)
    EditText machineryUpText;
    @BindView(R.id.machinery_down_text)
    EditText machineryDownText;
    @BindView(R.id.machinery_stop_text)
    EditText machineryStopText;
    @BindView(R.id.camera_up_text)
    EditText cameraUpText;
    @BindView(R.id.camera_down_text)
    EditText cameraDownText;
    @BindView(R.id.camera_left_text)
    EditText cameraLeftText;
    @BindView(R.id.camera_right_text)
    EditText cameraRightText;
    @BindView(R.id.IP_address_text)
    EditText IPAddressText;
    @BindView(R.id.port_text)
    EditText portText;
    @BindView(R.id.auto_start_text)
    EditText autoStartText;
    @BindView(R.id.auto_stop_text)
    EditText autoStopText;
    @BindView(R.id.video_address_text)
    EditText videoAddressText;
    @BindView(R.id.setting_back_button)
    Button settingBackButton;
    @BindView(R.id.setting_save_button)
    Button settingSaveButton;
    private SharedPreferences car;
    private SendUitl sendUitl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        getAllData();
    }
    private void getAllData() {
        forwardText.setText(SendUitl.OxToString(SendUitl.GO_AHEAD));
        backText.setText(SendUitl.OxToString(SendUitl.GO_BACK));
        leftText.setText(SendUitl.OxToString(SendUitl.TURN_LEFT));
        rightText.setText(SendUitl.OxToString(SendUitl.TURN_RIGHT));
        leftforwardText.setText(SendUitl.OxToString(SendUitl.TURN_LEFT_FORWARD));
        rightforwardText.setText(SendUitl.OxToString(SendUitl.TURN_RIGHT_FORWARD));
        leftbackText.setText(SendUitl.OxToString(SendUitl.TURN_LEFT_BACK));
        rightbackText.setText(SendUitl.OxToString(SendUitl.TURN_RIGHT_BACK));
        machineryUpText.setText(SendUitl.OxToString(SendUitl.MACHINERY_UP));
        machineryDownText.setText(SendUitl.OxToString(SendUitl.MACHINERY_DOWN));
        machineryStopText.setText(SendUitl.OxToString(SendUitl.STOP));
        autoStopText.setText(SendUitl.OxToString(SendUitl.AUTO_STOP));
        autoStartText.setText(SendUitl.OxToString(SendUitl.AUTO_START));
        cameraUpText.setText(SendUitl.OxToString(SendUitl.CAMERA_UP));
        cameraDownText.setText(SendUitl.OxToString(SendUitl.CAMERA_DOWN));
        cameraLeftText.setText(SendUitl.OxToString(SendUitl.CAMERA_LEFT));
        cameraRightText.setText(SendUitl.OxToString(SendUitl.CAMERA_RIGHT));
        portText.setText(SendUitl.PORT+"");
        IPAddressText.setText(SendUitl.IP);
        videoAddressText.setText(SendUitl.VIDEO_PATH);
    }

    @OnClick({R.id.setting_back_button, R.id.setting_save_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_back_button:
                finish();
                break;
            case R.id.setting_save_button:
                SendUitl.setGoAhead(forwardText.getText().toString().trim());
                SendUitl.setGoBack(backText.getText().toString().trim());
                SendUitl.setTurnLeft(leftText.getText().toString().trim());
                SendUitl.setTurnRight(rightText.getText().toString().trim());
                SendUitl.setTurnLeftForward(leftforwardText.getText().toString().trim());
                SendUitl.setTurnRightForward(rightforwardText.getText().toString().trim());
                SendUitl.setTurnLeftBack(leftbackText.getText().toString().trim());
                SendUitl.setTurnRightBack(rightbackText.getText().toString().trim());
                SendUitl.setAutoStart(autoStartText.getText().toString().trim());
                SendUitl.setAutoStop(autoStopText.getText().toString().trim());
                SendUitl.setCameraUp(cameraUpText.getText().toString().trim());
                SendUitl.setCameraDown(cameraDownText.getText().toString().trim());
                SendUitl.setCameraLeft(cameraLeftText.getText().toString().trim());
                SendUitl.setCameraRight(cameraRightText.getText().toString().trim());
                SendUitl.setIP(IPAddressText.getText().toString().trim());
                SendUitl.setPORT(Integer.parseInt(portText.getText().toString().trim()));
                SendUitl.setVideoPath(videoAddressText.getText().toString().trim());
                SendUitl.setSTOP(machineryStopText.getText().toString().trim());
                Toast toast = Toast.makeText(Setting.this,"保存成功！",Toast.LENGTH_SHORT);
                toast.show();
                finish();
                break;
        }
    }
}
