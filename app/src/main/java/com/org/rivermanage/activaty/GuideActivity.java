package com.org.rivermanage.activaty;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.rivermanage.R;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    //存放引导页
    private List<View> layoutData = new ArrayList<>();
    private PagerAdapter adapter;
    //第三个引导页的跳过按钮
    private TextView skip;

    //三个引导页
    private View view1;
    private View view2;
    private View view3;

    //引导页的三个点
    private ImageView icon1;
    private ImageView icon2;
    private ImageView icon3;

    ImageView[] icons = new ImageView[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.guide);

        initWieget();
        initEvent();

    }

    /**
     * 为控件添加事件
     */
    private void initEvent() {

        viewPager.addOnPageChangeListener(this);

    }

    /**
     * 初始化所有控件
     */
    private void initWieget() {

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.guide_item1, null);
        view2 = inflater.inflate(R.layout.guide_item2, null);
        view3 = inflater.inflate(R.layout.guide_item3, null);

        //初始化带跳过按钮的第三个布局，并且设置好事件监听
        initView3();

        layoutData.add(view1);
        layoutData.add(view2);
        layoutData.add(view3);

        //设置viewpager的适配器
        adapter = new PagerAdapter() {

            @Override
            public int getCount() {
                return layoutData.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                View view = layoutData.get(position);
                container.addView(view);
                return view;

            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

                container.removeView(layoutData.get(position));

            }

        };

        viewPager.setAdapter(adapter);

        icon1 = (ImageView) findViewById(R.id.icon_1);
        icon2 = (ImageView) findViewById(R.id.icon_2);
        icon3 = (ImageView) findViewById(R.id.icon_3);

        icons[0] = icon1;
        icons[1] = icon2;
        icons[2] = icon3;

    }

    /**
     * 初始化带跳过按钮的布局
     */
    private void initView3() {

        skip = (TextView) view3.findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = getIntent();
                boolean isWelcomeIn = intent1.getBooleanExtra("welcome", false);
                if (isWelcomeIn) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    SharedPreferences preferences = getSharedPreferences(DataUtil.sharedPreferenceFile, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(DataUtil.isFirstInKey, false);
                    editor.commit();
                }
                finish();
            }
        });

    }

    /**
     * viewpager滚动时
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 当页面被选中的时候
     * @param position
     */
    @Override
    public void onPageSelected(int position) {

        clearDots();
        icons[position].setImageResource(R.drawable.adware_style_selected);

    }



    /**
     * viewpager状态被改变的时候
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 将所有点都设置为未选中状态
     */
    private void clearDots() {

        icon1.setImageResource(R.drawable.adware_style_default);
        icon2.setImageResource(R.drawable.adware_style_default);
        icon3.setImageResource(R.drawable.adware_style_default);

    }

}
