package com.huizhi.manage.activity.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.ContentEntryDialog;
import com.huizhi.manage.dialog.JudgeDialog;
import com.huizhi.manage.node.AttendanceInfoNode;
import com.huizhi.manage.request.home.HomeUserGetRequest;
import com.huizhi.manage.request.home.HomeUserPostRequest;
import com.huizhi.manage.util.AppUtil;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import java.util.List;

/**
 * Created by CL on 2017/12/28.
 * 考勤
 */

public class HomeAttendanceActivity extends MapActivity {
    private TencentLocationManager locationManager;
    private MapView mapView;
    private TencentMap tencentMap;
    private  TencentLocationRequest request;
    private ImageButton sbBtn, xbBtn, yxbBtn;
    private LocationListener locationListener;
    private TextView isInRegionTV;
    private String lateReason, earlyReason;
    private static String netMac;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_attendance);
        getWifiName(this);
        initViews();
        getDates(handler);

    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        isInRegionTV = findViewById(R.id.isRegion_tv);

        sbBtn = findViewById(R.id.sb_dk_btn);
        xbBtn = findViewById(R.id.xb_dk_btn);
        yxbBtn = findViewById(R.id.yxb_btn);
        sbBtn.setOnClickListener(itemBtnClick);
        xbBtn.setOnClickListener(itemBtnClick);

        mapView = (MapView)findViewById(R.id.mapview);
        tencentMap = mapView.getMap();
//        tencentMap.setSatelliteEnabled(true);
        tencentMap.setZoom(19);

        request = TencentLocationRequest.create();
        locationManager = TencentLocationManager.getInstance(this);

        locationListener = new LocationListener(loadInfoUpdate);
        int error = locationManager.requestLocationUpdates(request, locationListener);
        Log.e("HuiZhi", "The location error:" + error);
        if(!isLocationEnabled()){
            Toast.makeText(this, "当前应用定位不可用，请先开启", Toast.LENGTH_LONG).show();
        }
    }

    private void setViewDate(AttendanceInfoNode node){
        if(node==null)
            return;
        TextView monthTV = findViewById(R.id.month_tv);
        monthTV.setText(node.getDaysOFMonth());
        TextView yearTV = findViewById(R.id.year_tv);
        yearTV.setText(node.getDaysOFYear());
        String startTime = node.getStartTime();
        String endTime = node.getEndTime();
        Log.i("HuiZhi", "The starttime:" + startTime + "  endtime:" + endTime);
        sbBtn.setVisibility(View.GONE);
        xbBtn.setVisibility(View.GONE);
        yxbBtn.setVisibility(View.GONE);
        if(TextUtils.isEmpty(startTime)&&TextUtils.isEmpty(endTime)){
            sbBtn.setVisibility(View.VISIBLE);
        }else if(!TextUtils.isEmpty(startTime)&&TextUtils.isEmpty(endTime)){
            xbBtn.setVisibility(View.VISIBLE);
        }else{
            yxbBtn.setVisibility(View.VISIBLE);
        }
    }

    private void getDates(Handler handler){
        HomeUserGetRequest getRequest = new HomeUserGetRequest();
        getRequest.getUserAttendanceInfo(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), handler);
    }

    private void removeLocationListener(){
        if(locationListener!=null) {
            locationManager.removeUpdates(locationListener);
            locationListener = null;
        }
    }

    private BaseInfoUpdate loadInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            double[] location = (double[])object;
            HomeUserPostRequest postRequest = new HomeUserPostRequest();
            postRequest.postUserAttendanceIsInRegion(UserInfo.getInstance().getUser().getSchoolId(), location[0], location[1], netMac, handler);
        }
    };

    private View.OnClickListener itemBtnClick = new View.OnClickListener() {
        private HomeUserPostRequest postRequest = new HomeUserPostRequest();

        @Override
        public void onClick(View view) {
            removeLocationListener();
            if(view.getId()==R.id.sb_dk_btn){
                JudgeDialog judgeDialog = new JudgeDialog(HomeAttendanceActivity.this, judgeSbInfoUpdate);
                judgeDialog.showView(view, "是否上班打卡");

//                postRequest.postUserAttendance(UserInfo.getInstance().getUser().getTeacherId(), );
            }else if(view.getId()==R.id.xb_dk_btn){
                JudgeDialog judgeDialog = new JudgeDialog(HomeAttendanceActivity.this, judgeXbInfoUpdate);
                judgeDialog.showView(view, "是否下班打卡");

            }
        }

        private BaseInfoUpdate judgeSbInfoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                getDates(timeSBHandler);
            }
        };

        private BaseInfoUpdate judgeXbInfoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                getDates(timeXBHandler);

            }
        };
    };

    private BaseInfoUpdate sbInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            double[] location = (double[])object;
            Log.i("HuiZhi", "The sb latitude:" + location[0] + "  longitude:" + location[1]);
            HomeUserPostRequest postRequest = new HomeUserPostRequest();
            postRequest.postUserAttendanceIsInRegion(UserInfo.getInstance().getUser().getSchoolId(), location[0], location[1], netMac, new AttendanceHandler(1));
        }
    };

    private BaseInfoUpdate xbInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            double[] location = (double[])object;
            Log.i("HuiZhi", "The xb latitude:" + location[0] + "  longitude:" + location[1]);
            HomeUserPostRequest postRequest = new HomeUserPostRequest();
            postRequest.postUserAttendanceIsInRegion(UserInfo.getInstance().getUser().getSchoolId(), location[0], location[1], netMac, new AttendanceHandler(2));
        }
    };

    private class LocationListener implements TencentLocationListener{
        private BaseInfoUpdate infoUpdate;

        LocationListener(BaseInfoUpdate infoUpdate){
            this.infoUpdate = infoUpdate;
        }

        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int error, String s) {
            if(TencentLocation.ERROR_OK == error){
                //定位成功
                double lat = tencentLocation.getLatitude();
                double lng = tencentLocation.getLongitude();
                tencentMap.setCenter(new LatLng(lat, lng));
                double[] location = {lat, lng};
                System.out.println("The latitude:" + lat + "  longitude:" + lng);
                if(infoUpdate!=null)
                    infoUpdate.update(location);
                removeLocationListener();
            }else {
                Log.e("HuiZhi", "Come into location error:" + s);
            }
        }

        @Override
        public void onStatusUpdate(String name, int status, String desc) {
            Log.i("HuiZhi", "come into location status update name:" + name);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj==null)
                        return;
                    AttendanceInfoNode node = (AttendanceInfoNode)msg.obj;
                    setViewDate(node);
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    if(msg.obj==null)
                        return;
                    AttendanceInfoNode node1 = (AttendanceInfoNode)msg.obj;
                    if(node1.isInRegion())
                        isInRegionTV.setText("已处于考勤范围内");
                    else
                        isInRegionTV.setText("在考勤范围之外");
                    break;
                case Constants.MSG_SUCCESS_TWO:
                    if(msg.obj==null)
                        return;
                    Toast.makeText(HomeAttendanceActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
                    getDates(handler);
                    break;
            }
        }
    };

    private Handler timeSBHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj==null)
                        return;
                    AttendanceInfoNode node = (AttendanceInfoNode)msg.obj;
                    if(node.isClockInLate()){
                        ContentEntryDialog entryDialog = new ContentEntryDialog(HomeAttendanceActivity.this, lateInfoUpdate);
                        entryDialog.showView(isInRegionTV, "迟到原因");
                    }else {
                        locationListener = new LocationListener(sbInfoUpdate);
                        int error = locationManager.requestLocationUpdates(request, locationListener);
                    }
                    break;
            }
        }
    };

    private BaseInfoUpdate lateInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            lateReason = (String)object;
            locationListener = new LocationListener(sbInfoUpdate);
            int error = locationManager.requestLocationUpdates(request, locationListener);
        }
    };

    private Handler timeXBHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj==null)
                        return;
                    AttendanceInfoNode node = (AttendanceInfoNode)msg.obj;
                    if(node.isClockOutEarly()){
                        ContentEntryDialog entryDialog = new ContentEntryDialog(HomeAttendanceActivity.this, earlyInfoUpdate);
                        entryDialog.showView(isInRegionTV, "早退原因");
                    }else {
                        locationListener = new LocationListener(xbInfoUpdate);
                        int error = locationManager.requestLocationUpdates(request, locationListener);
                    }
                    break;
            }
        }
    };

    private BaseInfoUpdate earlyInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            earlyReason = (String)object;
            locationListener = new LocationListener(xbInfoUpdate);
            int error = locationManager.requestLocationUpdates(request, locationListener);
        }
    };

    private class AttendanceHandler extends Handler{
        private int type;

        AttendanceHandler(int type){
            this.type = type;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS_ONE:
                    if(msg.obj==null)
                        return;
                    AttendanceInfoNode node1 = (AttendanceInfoNode)msg.obj;
                    if(node1.isInRegion()) {
                        isInRegionTV.setText("已处于考勤范围内");
                        HomeUserPostRequest postRequest = new HomeUserPostRequest();
                        postRequest.postUserAttendance(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), type, 1, "", lateReason, earlyReason, handler);
                    }else{
                        isInRegionTV.setText("在考勤范围之外");
//                        Toast.makeText(HomeAttendanceActivity.this, "不在考勤范围之内无法提交考勤", Toast.LENGTH_LONG).show();
                        ContentEntryDialog contentEntryDialog = new ContentEntryDialog(HomeAttendanceActivity.this, reasonInfoUpdate);
                        contentEntryDialog.showView(isInRegionTV, "异常考勤范围原因");
                    }
                    break;
            }
        }

        private BaseInfoUpdate reasonInfoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object==null)
                    return;

                HomeUserPostRequest postRequest = new HomeUserPostRequest();
                postRequest.postUserAttendance(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), type, 2, (String)object, lateReason, earlyReason, handler);
            }
        };
    };

    public boolean isLocationEnabled() {
        checkVersion();
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
         } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public void checkVersion(){
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(permissions, 0);
            }
        }
    }


    private static void getWifiName(Context context) {
        String wserviceName = Context.WIFI_SERVICE;
        WifiManager wm = (WifiManager) context.getSystemService(wserviceName);
        List<ScanResult> wifiList = wm.getScanResults();
        for (int i = 0; i < wifiList.size(); i++) {

            ScanResult result = wifiList.get(i);
            Log.d("dada", "bssid=" + result.BSSID + " ssid:" + result.SSID);
        }

        //下面的代码可以获取当当前设备连接到的网络的wifi信息

        WifiManager mWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (mWifi.isWifiEnabled()) {
            WifiInfo wifiInfo = mWifi.getConnectionInfo();
            String netName = wifiInfo.getSSID(); //获取被连接网络的名称
            netMac = wifiInfo.getBSSID(); //获取被连接网络的mac地址
            String localMac = wifiInfo.getMacAddress();// 获得本机的MAC地址
//            Toast.makeText(context, "The netname:" + netName + "   mac:" + netMac, Toast.LENGTH_LONG).show();
            Log.i("HuiZhi", "---netName:" + netName);   //---netName:HUAWEI MediaPad
            Log.i("HuiZhi", "---netMac:" + netMac);     //---netMac:78:f5:fd:ae:b9:97
            Log.i("HuiZhi", "---localMac:" + localMac); //---localMac:BC:76:70:9F:56:BD
        }
    }
}
