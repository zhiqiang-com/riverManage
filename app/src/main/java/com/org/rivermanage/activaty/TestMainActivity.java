package com.org.rivermanage.activaty;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.org.rivermanage.R;
import com.org.rivermanage.utils.FullyGridLayoutManager;
import com.org.rivermanage.utils.GridImageAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class TestMainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private List<LocalMedia> selectList = new ArrayList<>();

    private List<LocalMedia> selectList2 = new ArrayList<>();


    private int maxSelectNum = 1;
    private RecyclerView  recyclerView ;
    private RecyclerView  recyclerView2 ;
    private GridImageAdapter adapter;
    private GridImageAdapter adapter2;
    private int themeId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);

        themeId = R.style.picture_default_style;
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView2 = (RecyclerView) findViewById(R.id.recycler2);

        FullyGridLayoutManager manager = new FullyGridLayoutManager(TestMainActivity.this, 1, GridLayoutManager.VERTICAL, false);
        FullyGridLayoutManager manager2 = new FullyGridLayoutManager(TestMainActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);//
        recyclerView2.setLayoutManager(manager2);


        adapter = new GridImageAdapter(TestMainActivity.this, onAddPicClickListener);
        adapter2 = new GridImageAdapter(TestMainActivity.this, onAddPicClickListener2);
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
                            PictureSelector.create(TestMainActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(TestMainActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(TestMainActivity.this).externalPictureAudio(media.getPath());
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
                            PictureSelector.create(TestMainActivity.this).externalPicturePreview(position, selectList2);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(TestMainActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(TestMainActivity.this).externalPictureAudio(media.getPath());
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
                    PictureFileUtils.deleteCacheDirFile(TestMainActivity.this);
                } else {
                    Toast.makeText(TestMainActivity.this,
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

    }
//拉取照片
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
                PictureSelector.create(TestMainActivity.this)
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
        PictureSelector.create(TestMainActivity.this)
                .openGallery( PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(1)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(false)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
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

}
