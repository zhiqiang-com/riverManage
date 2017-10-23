package com.org.rivermanage.activaty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.org.rivermanage.R;
import com.org.rivermanage.constant.UrlConst;
import com.org.rivermanage.utils.JsonResult;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    private EditText et_number;
    private EditText et_password;
    private CheckBox chk_rememPwd;
    private CheckBox chk_autoLogin;
//    private EditText et_ip;



    private TextView tv;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv = (TextView) findViewById(R.id.tv_login);
        seekBar = (SeekBar) findViewById(R.id.sb);
        seekBar.setOnSeekBarChangeListener(this);

        et_number = (EditText) findViewById(R.id.et_number);
        et_password = (EditText) findViewById(R.id.et_password);
        chk_rememPwd = (CheckBox) findViewById(R.id.chk_rememPwd);
        chk_autoLogin = (CheckBox) findViewById(R.id.chk_autoLogin);


        chkRemPassAndAutoLogin();
    }

    //检查记住密码与自动登录功能
    private void chkRemPassAndAutoLogin(){
        SharedPreferences sp = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        boolean bRememPwd = sp.getBoolean("rememPwd", false);
        boolean bAutoLogin = sp.getBoolean("autoLogin", false);
        String loginId = sp.getString("loginId", null);
        String password = sp.getString("password", null);

        if(loginId!=null){
            et_number.setText(loginId);
        }
        if(password!=null){
            et_password.setText(password);
        }
        chk_rememPwd.setChecked(bRememPwd);
        chk_autoLogin.setChecked(bAutoLogin);

        if(bAutoLogin){
            doLogin(loginId, password);
        }
    }


    //保存密码与自动登录状态
    private void savePassAndAutoLogin(){
        SharedPreferences sp = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //根据两个复选按钮的状态来保存数据
        if(chk_rememPwd.isChecked()){   //选中了保存密码
            editor.putBoolean("rememPwd", true);
            editor.putString("loginId", et_number.getText().toString().trim());
            editor.putString("password", et_password.getText().toString().trim());
        }else{
            editor.putBoolean("rememPwd", false);
            editor.putString("loginId", "");
            editor.putString("password", "");
        }
        if(chk_autoLogin.isChecked()){   //选中了自动登录
            editor.putBoolean("autoLogin", true);
        }else{
            editor.putBoolean("autoLogin", false);
        }
        editor.commit();
    }

    //调用网络访问进行登录
    private void doLogin(String loginid, String password){
        //1.调用网络进行登录
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user.loginid", loginid);
        params.put("user.passWord", password);

        //url:   parmas：请求时携带的参数信息   responseHandler：是一个匿名内部类接受成功过失败
        String url = UrlConst.LOGIN;
//        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        System.out.println( url);
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //statusCode:状态码    headers：头信息  responseBody：返回的内容，返回的实体
                //判断状态码
                if(statusCode == 200){
                    //获取结果
                    try {
                        String result = new String(responseBody,"utf-8");
                        //Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        JsonResult jsonResult = gson.fromJson(result, JsonResult.class);
                        //Toast.makeText(LoginActivity.this, jsonResult.getMessage(), Toast.LENGTH_LONG).show();
                        //2.判断返回的json数据
                        //2.1若返回json数据success为true的话，调用保存密码与自动登录状态的方法
                        if(jsonResult.isSuccess()){   //2.1成功，则进入主界面
                            savePassAndAutoLogin();
                            Intent intent = new Intent(LoginActivity.this, UploadActivity.class);
                            startActivity(intent);
                        }else{   //2.2失败则显示提示信息
                            new AlertDialog.Builder(LoginActivity.this).setTitle("信息提示")
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setMessage(jsonResult.getMessage())
                                    .setPositiveButton("确定", null)
                                    .setNegativeButton("取消", null)
                                    .create().show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
            }
        });
    }

    /**
     * seekBar进度变化时回调
     *
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getProgress() == seekBar.getMax()) {
            tv.setVisibility(View.VISIBLE);
            tv.setTextColor(Color.WHITE);
            tv.setText("请稍候...");

//            Toast.makeText(this, "执行了", Toast.LENGTH_SHORT).show();

            //1.调用网络访问进行登录
            String loginid = et_number.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            doLogin(loginid, password);
        } else {
            tv.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * seekBar开始触摸时回调
     *
     * @param seekBar
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * seekBar停止触摸时回调
     *
     * @param seekBar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if (seekBar.getProgress() != seekBar.getMax()) {
            seekBar.setProgress(0);
            tv.setVisibility(View.VISIBLE);
            tv.setTextColor(Color.GRAY);
            tv.setText("滑动登录");
        }
    }
}
