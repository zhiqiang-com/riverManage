package com.org.rivermanage.activaty;


import java.io.UnsupportedEncodingException;
import java.util.List;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.org.rivermanage.R;

import com.org.rivermanage.constant.UrlConst;
import com.org.rivermanage.utils.JsonResult;
import com.org.rivermanage.utils.UserWaring;
import com.org.rivermanage.utils.warningAdapter;

import org.apache.http.Header;

/**
 * Created by zhiqiang.com on 2017/10/23.
 */

public class ShowListActivity extends Activity implements AdapterView.OnItemClickListener {

    ListView lv_upinfor;
    private String logingId;
    List list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        logingId=intent.getStringExtra("logingId");
        lv_upinfor= findViewById(R.id.lv_upinfor);
        showDataByName();

//        lv_upinfor.setOnClickListener(this);



    }

    //查询用户上报信息
    private void showDataByName(){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("loginid", logingId);
        asyncHttpClient.post(UrlConst.SELRCTByLoginId_WRNING, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //statusCode:状态码    headers：头信息  responseBody：返回的内容，返回的实体
                //判断状态码
                if(statusCode == 200){
                    //获取结果
                    try {
                        String result = new String(responseBody,"utf-8");
                        Gson gson = new Gson();
                        JsonResult jsonResult = gson.fromJson(result, JsonResult.class);
                        if(jsonResult.isSuccess()){   //2.1成功，则进入主界面
                            list = gson.fromJson(gson.toJson(jsonResult.getData()), new TypeToken<List<UserWaring>>(){}.getType());
                            lv_upinfor.setAdapter(new warningAdapter(ShowListActivity.this,list));
                        }else{   //2.2失败则显示提示信息
                            Toast.makeText(getApplicationContext(),"数据无效",Toast.LENGTH_SHORT).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"数据转换异常",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),"请求失败！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//        Toast.makeText(this,adapterView+""+view+"",Toast.LENGTH_SHORT).show();

    }

//    /**
//     61      * 实现类，响应按钮点击事件
//     62      */
//    private MyClickListener mListener = new MyClickListener() {
//        @Override
//        public void myOnClick(int position, View v) {
//                        Toast.makeText(
//                                        MainActivity.this,
//                                        "listview的内部的按钮被点击了！，位置是-->" + position + ",内容是-->"
//                                                + contentList.get(position), Toast.LENGTH_SHORT)
//                                .show();
//                   }
//    };

}
