package com.org.rivermanage.activaty;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.org.rivermanage.R;
import com.org.rivermanage.constant.UrlConst;
import com.org.rivermanage.utils.TimeUtils;
import org.apache.http.Header;
import java.io.File;



/**
 * Created by zhiqiang.com on 2017/10/19.
 */




public class test extends Activity {
    private EditText et_file;
    private Button btn_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_upload);


        //获取控件
        et_file = (EditText) findViewById(R.id.et_upload);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(new Button_OClick());
    }
    public class Button_OClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                uploadFile(et_file.getText().toString(), UrlConst.FILE_UPLOAD);
            } catch (Exception e) {
                e.printStackTrace();
                e.printStackTrace();

            }

        }

        public void uploadFile(String path, String url) throws Exception {
            File file = new File(path);


            if (file.exists() && file.length() > 0) {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();


                String  time = TimeUtils.getCurrentTime()+".mp4";
                System.out.println(time);




                params.put("warning.file",time);

                params.put("warning.zhaopian", file);

                // 上传文件
                client.post(url, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                  Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // TODO Auto-generated method stub
                        super.onRetry(retryNo);
                        // 返回重试次数
                    }

                });
            } else {
            Toast.makeText(getApplicationContext(), "文件不存在", Toast.LENGTH_LONG).show();
            }
        }

    }
}
