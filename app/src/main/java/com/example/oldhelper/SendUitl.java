package com.example.oldhelper;
/*
junjun
*/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;

/**
 * 指令发送工具类
 *
 * @author
 * @date
 * @time
 */
public class SendUitl {
    //指令初始化
    public static byte[] STOP = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF};
    public static byte[] GO_AHEAD = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0xFF};
    public static byte[] GO_BACK = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0xFF};
    public static byte[] TURN_LEFT = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0xFF};
    public static byte[] TURN_RIGHT = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0xFF};
    public static byte[] TURN_LEFT_FORWARD = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0xFF};
    public static byte[] TURN_LEFT_BACK = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0xFF};
    public static byte[] TURN_RIGHT_FORWARD = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0xFF};
    public static byte[] TURN_RIGHT_BACK = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0xFF};
    public static byte[] MACHINERY_UP = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0xFF};
    public static byte[] MACHINERY_DOWN = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0xFF};
    public static byte[] AUTO_START = new byte[]{(byte) 0xFF, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0xFF};
    public static byte[] AUTO_STOP = new byte[]{(byte) 0xFF, (byte) 0x04, (byte) 0x01, (byte) 0x00, (byte) 0xFF};
    public static byte[] CAMERA_UP = new byte[]{(byte) 0xFF, (byte) 0x04, (byte) 0x02, (byte) 0x00, (byte) 0xFF};
    public static byte[] CAMERA_DOWN = new byte[]{(byte) 0xFF, (byte) 0x32, (byte) 0x00, (byte) 0x00, (byte) 0xFF};
    public static byte[] CAMERA_LEFT= new byte[]{(byte) 0xFF, (byte) 0x33, (byte) 0x00, (byte) 0x00, (byte) 0xFF};
    public static byte[] CAMERA_RIGHT= new byte[]{(byte) 0xFF, (byte) 0x13, (byte) 0x01, (byte) 0x00, (byte) 0xFF};
    public static byte[] PATROL_TRACKING_MODE = new byte[]{(byte) 0xFF, (byte) 0x13, (byte) 0x02, (byte) 0x00, (byte) 0xFF};
    public static byte[] INFRARED_OBSTACLE_AVOIDANCE_MODE= new byte[]{(byte) 0xFF, (byte) 0x13, (byte) 0x03, (byte) 0x00, (byte) 0xFF};
    public static byte[] ULTRASONIC_OBSTACLE_AVOIDANCE_MODE= new byte[]{(byte) 0xFF, (byte) 0x13, (byte) 0x04, (byte) 0x00, (byte) 0xFF};
    public static byte[] MANUAL_MODE= new byte[]{(byte) 0xFF, (byte) 0x13, (byte) 0x05, (byte) 0x00, (byte) 0xFF};
    public static byte[] SPEED_GEAR= new byte[]{(byte) 0xFF, (byte) 0x02, (byte) 0x03, (byte) 0x00, (byte) 0xFF};

    //默认IP
    public static String IP = "192.168.8.1";
    //默认端口
    public static int PORT = 2001;
    //默认视频地址
    public static String VIDEO_PATH = "http://192.168.8.1:8083/?action=stream";
    private static SharedPreferences car;
    //本地存储文件
    //private SharedPreferences car;
    //环境上下文
    private Context context;
    //链接对象
    private Socket client = null;
    private OutputStream outputStream = null;
    //消息接受handler
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if ("true".equals(msg.obj)) {
                try {
                    outputStream = client.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, "连接成功!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public SendUitl() {
    }

    /**
     * 功能描述 工具类构造方法，初始化工具类,需传环境上下文
     *
     * @param context
     * @return
     * @author
     * @date
     */
    public SendUitl(final Context context) {
        this.context = context;
        InitInstruction(context);
        //如果不存在连接对象则进行连接，否则不重新连接
        if (client == null) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        client = new Socket(IP, PORT);
                        Message msg = new Message();
                        msg.obj = "true";
                        handler.sendMessage(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public void closeClient(){
        try {
            if (client!=null)
                client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description 初始化指令
     * @Auther
     * @Date
     * @Param [context]
     * @return void
     */
    public void InitInstruction(Context context){
        //获取本地存储的用户自定义指令和设置,如果有则替换默认指令和设置
        car = context.getSharedPreferences("Car", 0);
        if (!car.getString("STOP", "").equals("")) {
            STOP = StringToOx(car.getString("STOP", ""));
        }
        if (!car.getString("GO_AHEAD", "").equals("")) {
            GO_AHEAD = StringToOx(car.getString("GO_AHEAD", ""));
        }
        if (!car.getString("BACK", "").equals("")) {
            GO_BACK = StringToOx(car.getString("BACK", ""));
        }
        if (!car.getString("TURN_LEFT", "").equals("")) {
            TURN_LEFT = StringToOx(car.getString("TURN_LEFT", ""));
        }
        if (!car.getString("TURN_RIGHT", "").equals("")) {
            TURN_RIGHT = StringToOx(car.getString("TURN_RIGHT", ""));
        }
        if (!car.getString("TURN_LEFT_FORWARD", "").equals("")) {
            TURN_LEFT_FORWARD = StringToOx(car.getString("TURN_LEFT_FORWARD", ""));
        }
        if (!car.getString("TURN_LEFT_BACK", "").equals("")) {
            TURN_LEFT_BACK = StringToOx(car.getString("TURN_LEFT_BACK", ""));
        }
        if (!car.getString("TURN_RIGHT_FORWARD", "").equals("")) {
            TURN_RIGHT_FORWARD = StringToOx(car.getString("TURN_RIGHT_FORWARD", ""));
        }
        if (!car.getString("TURN_RIGHT_BACK", "").equals("")) {
            TURN_RIGHT_BACK = StringToOx(car.getString("TURN_RIGHT_BACK", ""));
        }
        if (!car.getString("MACHINERY_UP", "").equals("")) {
            MACHINERY_UP = StringToOx(car.getString("MACHINERY_UP", ""));
        }
        if (!car.getString("MACHINERY_DOWN", "").equals("")) {
            MACHINERY_DOWN = StringToOx(car.getString("MACHINERY_DOWN", ""));
        }
        if (!car.getString("IP", "").equals("")) {
            IP = car.getString("IP", "");
        }
        if (car.getInt("PORT", 0) != 0) {
            PORT = car.getInt("PORT", 0);
        }
        if (!car.getString("VIDEO_PATH", "").equals("")) {
            VIDEO_PATH = car.getString("VIDEO_PATH", "");
        }
        if(!car.getString("AUTO_START","").equals("")){
            AUTO_START = StringToOx(car.getString("AUTO_START",""));
        }
        if(!car.getString("AUTO_STOP","").equals("")){
            AUTO_STOP = StringToOx(car.getString("AUTO_STOP",""));
        }
        if(!car.getString("CAMERA_UP","").equals("")){
            CAMERA_UP = StringToOx(car.getString("CAMERA_UP",""));
        }
        if(!car.getString("CAMERA_DOWN","").equals("")){
            CAMERA_DOWN = StringToOx(car.getString("CAMERA_DOWN",""));
        }
        if(!car.getString("CAMERA_LEFT","").equals("")){
            CAMERA_LEFT = StringToOx(car.getString("CAMERA_LEFT",""));
        }
        if(!car.getString("CAMERA_RIGHT","").equals("")){
            CAMERA_RIGHT = StringToOx(car.getString("CAMERA_RIGHT",""));
        }
    }

    /**
     * 功能描述 获得当前链接ip
     *
     * @param
     * @return java.lang.String
     * @author
     * @date
     */
    public String getIP() {
        return IP;
    }

    /**
     * 功能描述 设置连接ip
     *
     * @param IP
     * @return void
     * @author
     * @date
     */
    public static void setIP(String IP) {
        SendUitl.IP = IP;
        SharedPreferences.Editor edit = car.edit();
        edit.putString("IP", IP);
        edit.commit();
    }

    /**
     * 功能描述 获得当前连接端口号
     *
     * @param
     * @return int
     * @author
     * @date
     */
    public int getPORT() {
        return PORT;
    }

    /**
     * 功能描述 设置连接端口号
     *
     * @param PORT
     * @return void
     * @author
     * @date 2019/11/1
     */
    public static void setPORT(int PORT) {
        SendUitl.PORT = PORT;
        SharedPreferences.Editor edit = car.edit();
        edit.putInt("PORT", PORT);
        edit.commit();
    }

    /**
     * 功能描述 获得视频地址
     * @author
     * @date
     * @param
     * @return java.lang.String
     */
    public String getVideoPath() {
        return VIDEO_PATH;
    }

    /**
     * 功能描述 设置视频地址
     * @author
     * @date
     * @param videoPath
     * @return void
     */
    public static void setVideoPath(String videoPath) {
        VIDEO_PATH = videoPath;
        SharedPreferences.Editor edit = car.edit();
        edit.putString("VIDEO_PATH", videoPath);
        edit.commit();
    }

    /**
     * 功能描述 发送指令
     *
     * @param instruction
     * @return void
     * @author li
     * @date 2019/11/1
     */
    public void sendInstruction(final byte[] instruction) {
        new Thread(){
            @Override
            public void run() {
                try {
                    if (outputStream != null) {
                        outputStream.write(instruction);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 功能描述 修改指令值
     *
     * @param STOP
     * @return void
     * @author li
     * @date 2019/11/1
     */
    public static void setSTOP(String STOP) {
        SendUitl.STOP = StringToOx(STOP);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("STOP", STOP);
        edit.commit();
    }

    public static void setGoAhead(String goAhead) {
        SendUitl.GO_AHEAD = StringToOx(goAhead);
        Log.i("111",OxToString(SendUitl.GO_AHEAD));
        SharedPreferences.Editor edit = car.edit();
        edit.putString("GO_AHEAD", goAhead);
        edit.commit();
    }

    public static void setGoBack(String goBack) {
        SendUitl.GO_BACK = StringToOx(goBack);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("GO_BACK", goBack);
        edit.commit();
    }

    public static void setTurnLeft(String turnLeft) {
        SendUitl.TURN_LEFT = StringToOx(turnLeft);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("TURN_LEFT", turnLeft);
        edit.commit();
    }

    public static void setTurnRight(String turnRight) {
        SendUitl.TURN_RIGHT = StringToOx(turnRight);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("TURN_RIGHT", turnRight);
        edit.commit();
    }

    public static void setTurnLeftForward(String turnLeftForward) {
        SendUitl.TURN_LEFT_FORWARD = StringToOx(turnLeftForward);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("TURN_LEFT_FORWARD", turnLeftForward);
        edit.commit();
    }

    public static void setTurnLeftBack(String turnLeftBack) {
        SendUitl.TURN_LEFT_BACK = StringToOx(turnLeftBack);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("TURN_LEFT_BACK", turnLeftBack);
        edit.commit();
    }

    public static void setTurnRightForward(String turnRightForward) {
        SendUitl.TURN_RIGHT_FORWARD = StringToOx(turnRightForward);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("TURN_RIGHT_FORWARD", turnRightForward);
        edit.commit();
    }

    public static void setTurnRightBack(String turnRightBack) {
        SendUitl.TURN_RIGHT_BACK = StringToOx(turnRightBack);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("TURN_RIGHT_BACK", turnRightBack);
        edit.commit();
    }
    public static void setMachineryUp(String machineryUp){
        SendUitl.MACHINERY_UP = StringToOx(machineryUp);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("MACHINERY_UP",machineryUp);
        edit.commit();
    }
    public static void setMachineryDown(String machineryDown){
        SendUitl.MACHINERY_DOWN = StringToOx(machineryDown);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("MACHINERY_DOWN",machineryDown);
        edit.commit();
    }
    public static void setAutoStart(String autoStart){
        SendUitl.AUTO_START = StringToOx(autoStart);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("AUTO_START",autoStart);
        edit.commit();
    }
    public static void setAutoStop(String autoStop){
        SendUitl.AUTO_STOP = StringToOx(autoStop);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("AUTO_STOP",autoStop);
        edit.commit();
    }
    public static void setCameraUp(String cameraUp){
        SendUitl.CAMERA_UP = StringToOx(cameraUp);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("CAMERA_UP",cameraUp);
        edit.commit();
    }
    public static void setCameraDown(String cameraDown){
        SendUitl.CAMERA_DOWN = StringToOx(cameraDown);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("CAMERA_DOWN",cameraDown);
        edit.commit();
    }
    public static void setCameraLeft(String cameraLeft){
        SendUitl.CAMERA_LEFT = StringToOx(cameraLeft);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("CAMERA_LEFT",cameraLeft);
        edit.commit();
    }
    public static void setCameraRight(String cameraRight){
        SendUitl.CAMERA_RIGHT = StringToOx(cameraRight);
        SharedPreferences.Editor edit = car.edit();
        edit.putString("CAMERA_RIGHT",cameraRight);
        edit.commit();
    }
    public static void setLeftSpeedGear(int speedGear) {
        String sg= Integer.toHexString(speedGear);
        String s=OxToString(SPEED_GEAR);
        if(sg.length()==1){
            sg = "0"+sg;
        }
        SPEED_GEAR =StringToOx(s.substring(0,6)+sg+s.substring(8));
    }



    /**
     * 功能描述 十六进制字符串转换成十六进制字节数组
     *
     * @param str
     * @return byte[]
     * @author
     * @date
     */
    public static byte[] StringToOx(String str) {
        int len = str.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4)
                                 + Character.digit(str.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * 功能描述 十六进制转成对应字符串
     *
     * @param data
     * @return java.lang.String
     * @author
     * @date
     */
    public static String OxToString(byte[] data) {
        String result = "";
        for (int i = 0; i < data.length; i++) {
            if (Integer.toHexString(data[i]).length() == 1) {
                result += "0" + Integer.toHexString(data[i] & 0XFF);
            } else {
                result += Integer.toHexString(data[i] & 0XFF);
            }
        }
        return result.toUpperCase();
    }

}
