package com.org.rivermanage.activaty;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.org.rivermanage.R;
import com.org.rivermanage.base.LocationApplication;
import com.org.rivermanage.constant.UrlConst;
import com.org.rivermanage.service.LocationService;
import com.org.rivermanage.utils.AudioRecoderUtils;
import com.org.rivermanage.utils.FullyGridLayoutManager;
import com.org.rivermanage.utils.GridImageAdapter;
import com.org.rivermanage.utils.PopupWindowFactory;
import com.org.rivermanage.utils.TimeUtils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;

import java.io.File;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class UploadActivity extends AppCompatActivity {
    static final int VOICE_REQUEST_CODE = 66;

    private List<LocalMedia> selectList = new ArrayList<>();

    private List<LocalMedia> selectList2 = new ArrayList<>();


    private int maxSelectNum = 1;
    private RecyclerView recyclerView ;
    private RecyclerView  recyclerView2 ;
    private GridImageAdapter adapter;
    private GridImageAdapter adapter2;
    private int themeId;




    private Button mButton;
    private Button upload ;
    private ImageView mImageView;
    private TextView mTextView;
    private AudioRecoderUtils mAudioRecoderUtils;
    private Context context;
    private PopupWindowFactory mPop;
    private RelativeLayout rl;
        private LocationService locationService;
        private TextView LocationResult;
        private Button Refresh;
        private static int RESULT_LOAD_IMAGE = 1;

        private static String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        upload = (Button) findViewById(R.id.upload_Btn);
        upload.setOnClickListener( new upload_OnClick( ));



        themeId = R.style.picture_default_style;
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView2 = (RecyclerView) findViewById(R.id.recycler2);

        FullyGridLayoutManager manager = new FullyGridLayoutManager(UploadActivity.this, 1, GridLayoutManager.VERTICAL, false);
        FullyGridLayoutManager manager2 = new FullyGridLayoutManager(UploadActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);//
        recyclerView2.setLayoutManager(manager2);

        adapter = new GridImageAdapter(UploadActivity.this, onAddPicClickListener);
        adapter2 = new GridImageAdapter(UploadActivity.this, onAddPicClickListener2);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        adapter2.setList(selectList2);
        adapter2.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        recyclerView2.setAdapter(adapter2);


        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(UploadActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(UploadActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(UploadActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
        adapter2.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList2.size() > 0) {
                    LocalMedia media = selectList2.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(UploadActivity.this).externalPicturePreview(position, selectList2);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(UploadActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(UploadActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });

        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(UploadActivity.this);
                } else {
                    Toast.makeText(UploadActivity.this,
                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });


















        context = this;

        rl = (RelativeLayout) findViewById(R.id.r1);

        mButton = (Button) findViewById(R.id.button);

        //PopupWindow的布局文件
        final View view = View.inflate(this, R.layout.layout_microphone, null);
        mPop = new PopupWindowFactory(this,view);
        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);
        mAudioRecoderUtils = new AudioRecoderUtils();
        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {
            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }
            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                Toast.makeText(UploadActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
               mTextView.setText(TimeUtils.long2String(0));
            }
        });
        //6.0以上需要权限申请
        requestPermissions();
        LocationResult = (TextView) findViewById(R.id.tv_location);

        Refresh = (Button) findViewById(R.id.btn_R);
        LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        //支持滑动
    }

    //拉取照片
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(UploadActivity.this)
                    .openGallery( PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(false)// 是否可预览视频
                    .enablePreviewAudio(false) // 是否可播放音频
                    .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    .isCamera(true)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .enableCrop(false)// 是否裁剪
                    .compress(true)// 是否压缩
                    .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                    .selectionMedia(selectList)// 是否传入已选图片
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }

    };


    //    拉取视频
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener2 = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(UploadActivity.this)
                    .openGallery( PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
//                    .previewImage(false)// 是否可预览图片
                    .previewVideo(true)// 是否可预览视频
//                    .enablePreviewAudio(false) // 是否可播放音频
                    .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
            .isCamera(true)// 是否显示拍照按钮
//                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
//                    .enableCrop(false)// 是否裁剪
                    .compress(true)// 是否压缩
                    .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
//                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                    .selectionMedia(selectList2)// 是否传入已选图片
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }

    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.print(requestCode);

        if (resultCode == RESULT_OK) {
            List<LocalMedia> file = PictureSelector.obtainMultipleResult(data);
            String s =file.get(0).getPictureType();
            if(s.substring(0,s.lastIndexOf("/")).equals("image")){
                selectList.clear();
                selectList=file;
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
            }else {
                selectList2.clear();
                selectList2=file;
                adapter2.setList(selectList2);
                adapter2.notifyDataSetChanged();
            }
        }
    }

    private void requestPermissions() {
        //判断是否开启摄像头权限
        if ((ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                ) {
            StartListener();

            //判断是否开启语音权限
        } else {
            //请求获取摄像头权限
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, VOICE_REQUEST_CODE);
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == VOICE_REQUEST_CODE) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED) ) {
                StartListener();
            } else {
                Toast.makeText(context, "已拒绝权限！", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void StartListener(){
        //Button的touch监听
        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mPop.showAtLocation(rl, Gravity.CENTER, 0, 0);
                        mButton.setText("松开保存");
                        mAudioRecoderUtils.startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
//                        mAudioRecoderUtils.cancelRecord();    //取消录音（不保存录音文件）
                        mPop.dismiss();
                        mButton.setText("按住说话");
                        break;
                }
                return true;
            }
        });
    }




    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str) {
        final String s = str;
        try {
            if (LocationResult != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocationResult.post(new Runnable() {
                            @Override
                            public void run() {
                                LocationResult.setText(s);
                            }
                        });

                    }
                }).start();
            }
            //LocationResult.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // -----------location config ------------
        locationService = ((LocationApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK

        Refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    locationService.stop();
                    locationService.start();// 定位SDK
                   Toast.makeText(UploadActivity.this,"刷新成功！",Toast.LENGTH_SHORT).show();

            }
        });
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);

                sb.append("\n纬度 : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("   经度 : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\n位置 : ");// 地址信息
                sb.append(location.getAddrStr());

                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\n状态 : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果

                    sb.append("\n状态 : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\n状态 : ");
                    sb.append("离线定位成功");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\n状态 : ");
                    sb.append("服务端网络定位失败");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\n状态 : ");
                    sb.append("请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\n状态 : ");
                    sb.append("无法获取有效定位依据导致定位失败");
                }
                logMsg(sb.toString());

            }
        }

    };


    public class upload_OnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            try {
                uploadFile( UrlConst.FILE_UPLOAD);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void uploadFile( String url) throws Exception {
            File file1;
            File file2;
            File file3;

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();

            if(selectList.size()>0){
                file1=  new File(selectList.get(0).getCompressPath());
                params.put("warning.zhaopian", file1);
                params.put("warning.file",TimeUtils.getCurrentTime()+".jpg");
            }

            if(selectList2.size()>0){
                file2=  new File(selectList2.get(0).getPath());
                params.put("warning.shipin", file2);
                params.put("warning.video",TimeUtils.getCurrentTime()+".mp4");
            }

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
        }

    }

}
