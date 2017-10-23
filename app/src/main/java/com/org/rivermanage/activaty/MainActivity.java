package com.org.rivermanage.activaty;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.org.rivermanage.R;
import com.org.rivermanage.base.LocationApplication;
import com.org.rivermanage.constant.UrlConst;
import com.org.rivermanage.service.LocationService;
import com.org.rivermanage.service.Utils;
import com.org.rivermanage.utils.JsonResult;
import com.org.rivermanage.utils.UserWaring;
import com.org.rivermanage.utils.Warning;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hdl.com.myhttputils.CommCallback;
import hdl.com.myhttputils.MyHttpUtils;


public class MainActivity extends AppCompatActivity {

    private String loginid="1";

    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationService locService;
    private LinkedList<LocationEntity> locationList = new LinkedList<LocationEntity>(); // 存放历史定位结果的链表，最大存放当前结果的前5次定位结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.bmapView);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));

        locService = ((LocationApplication) getApplication()).locationService;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                            Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                            startActivity(intent);
//                             finish();
                Toast.makeText(getApplicationContext(),"点击了添加",Toast.LENGTH_SHORT).show();

            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShowListActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"点击了信息",Toast.LENGTH_SHORT).show();

            }
        });

        SelectWrning();
        setUserMapCenter();

//        SelectWrningBy();
//        setMarker();
    }

    /**
     * 设置中心点
     */
    private void setUserMapCenter() {

   //学校   117.992811,37.390804

        LatLng cenpt = new LatLng(37.390804, 117.992811);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

    }

    /***
     * 定位结果回调，在此方法中处理定位结果
     */
//    BDAbstractLocationListener listener = new BDAbstractLocationListener() {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            // TODO Auto-generated method stub
//
//            if (location != null && (location.getLocType() == 161 || location.getLocType() == 66)) {
//                Message locMsg = locHander.obtainMessage();
//                Bundle locData;
//                locData = Algorithm(location);
//                if (locData != null) {
//                    locData.putParcelable("loc", location);
//                    locMsg.setData(locData);
//                    locHander.sendMessage(locMsg);
//                }
//            }
//        }
//
//    };

//    private Bundle Algorithm(BDLocation location) {
//        Bundle locData = new Bundle();
//        double curSpeed = 0;
//        if (locationList.isEmpty() || locationList.size() < 2) {
//            LocationEntity temp = new LocationEntity();
//            temp.location = location;
//            temp.time = System.currentTimeMillis();
//            locData.putInt("iscalculate", 0);
//            locationList.add(temp);
//        } else {
//            if (locationList.size() > 5)
//                locationList.removeFirst();
//            double score = 0;
//            for (int i = 0; i < locationList.size(); ++i) {
//                LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
//                        locationList.get(i).location.getLongitude());
//                LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
//                double distance = DistanceUtil.getDistance(lastPoint, curPoint);
//                curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) / 1000;
//                score += curSpeed * Utils.EARTH_WEIGHT[i];
//            }
//            if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
//                location.setLongitude(
//                        (locationList.get(locationList.size() - 1).location.getLongitude() + location.getLongitude())
//                                / 2);
//                location.setLatitude(
//                        (locationList.get(locationList.size() - 1).location.getLatitude() + location.getLatitude())
//                                / 2);
//                locData.putInt("iscalculate", 1);
//            } else {
//                locData.putInt("iscalculate", 0);
//            }
//            LocationEntity newLocation = new LocationEntity();
//            newLocation.location = location;
//            newLocation.time = System.currentTimeMillis();
//            locationList.add(newLocation);
//
//        }
//        return locData;
//    }


//    /***
//     * 接收定位结果消息，并显示在地图上
//     */
//    private Handler locHander = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            // TODO Auto-generated method stub
//            super.handleMessage(msg);
//            try {
//                BDLocation location = msg.getData().getParcelable("loc");
//                int iscal = msg.getData().getInt("iscalculate");
//                if (location != null) {
//                    LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
//                    // 构建Marker图标
//                    BitmapDescriptor bitmap = null;
//                    if (iscal == 0) {
//                        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.huaji); // 非推算结果
//                    } else {
//                        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_focuse_mark); // 推算结果
//                    }
//
//                    // 构建MarkerOption，用于在地图上添加Marker
//                    OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
//                    // 在地图上添加Marker，并显示
//                    mBaiduMap.addOverlay(option);
//                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//                System.out.println(e.getMessage());
//            }
//        }
//
//    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//		WriteLog.getInstance().close();
//        locService.unregisterListener(listener);
        locService.stop();
        mMapView.onDestroy();
    }



    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
//        reset.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if (mBaiduMap != null)
//                    mBaiduMap.clear();
//            }
//        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();

    }

    class LocationEntity {
        BDLocation location;
        long time;
    }


    /**
     * 添加marker
     */
    private void setMarker(double lat, double lon) {
//        Log.v("pcw","setMarker : lat : "+ lat+" lon : " + lon);
        //定义Maker坐标点

        //117.969333,37.400943
        LatLng point = new LatLng(lat, lon);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_openmap_mark);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    private void SelectWrning(){

        //1.请求所有警告点
        new MyHttpUtils()
                .url(UrlConst.SELRCT_WRNING)//请求的url
                .setJavaBean(Warning.class)//设置需要解析成的javabean对象
                .setReadTimeout(60000)//设置读取超时时间,不设置的话默认为30s(30000)
                .setConnTimeout(6000)//设置连接超时时间,不设置的话默认5s(5000)
                .onExecute(new CommCallback<Warning>() {


                               @Override
                               public void onSucess(Warning warning) {
                                for(int  i=0;i<warning.getData().size();i++ ){
                                    setMarker(warning.getData().get(i).getLatitude(),warning.getData().get(i).getLongitude());
                                }
                               }
                               @Override
                               public void onFailed(String msg) {
                                   Toast.makeText(getApplicationContext(),"请求异常:"+msg,Toast.LENGTH_SHORT).show();
                               }
                           });

    }
}
