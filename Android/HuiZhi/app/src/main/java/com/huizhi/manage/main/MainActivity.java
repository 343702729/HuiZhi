package com.huizhi.manage.main;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.fragment.MaterialFragment;
import com.huizhi.manage.fragment.NewHomeFragment;
import com.huizhi.manage.fragment.NewMessageFragment;
import com.huizhi.manage.fragment.NewUserFragment;
import com.huizhi.manage.node.VersionNode;
import com.huizhi.manage.request.common.FileGetRequest;
import com.huizhi.manage.request.home.HomeUserGetRequest;
import com.huizhi.manage.request.main.MainRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.NavigationBarUtil;
import com.huizhi.manage.util.RongUtil;
import com.huizhi.manage.version.VersionUtil;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;

public class MainActivity extends FragmentActivity {
    private FragmentManager fragmentManager;
    private Fragment homeFragment, taskFragment, materialFragment, communicateFragment, communicateListFragment, messageFragment, userFragment;
    private View homeV, taskV, materialV, communicateV, userV;
    private int currentIndex = -1;
    private boolean isExit = false;
    private int index = 0;
    private boolean isChat = false;
    private Fragment currentFG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ((MainApplication)getApplication()).setRongInit(UserInfo.getInstance().getUser().getAppKey());

        getDatas();

//        communicateIMConnect();
        initDates();
        initViews();
        fragmentManager = getFragmentManager();
        setTabSelection(index);
        getUsersInfo();
        getUploadToken();
        verifyStoragePermissions(this);
//        addRongListener();
//        addUnreadCountObserver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFileData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeUnreadCountObserver();
    }

    private void initDates(){
        index = getIntent().getIntExtra("Index", 0);
//        if(index==3){
//            isChat = getIntent().getBooleanExtra("IsChat", false);
//            String userid = getIntent().getStringExtra("Date");
//            RongIM.getInstance().startPrivateChat(this, userid, userid);
//        }
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
//        StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
        homeV = (LinearLayout)findViewById(R.id.home_btn);
        homeV.setOnClickListener(itemBtnClick);
        taskV = (LinearLayout)findViewById(R.id.task_btn);
        taskV.setOnClickListener(itemBtnClick);
        materialV = (LinearLayout)findViewById(R.id.material_btn);
        materialV.setOnClickListener(itemBtnClick);
        communicateV = (LinearLayout)findViewById(R.id.communicate_btn);
        communicateV.setOnClickListener(itemBtnClick);
        userV = (LinearLayout)findViewById(R.id.user_btn);
        userV.setOnClickListener(itemBtnClick);
    }

    public void getDatas(){
        MainRequest mainRequest = new MainRequest();
        mainRequest.getVersion(((MainApplication)getApplication()).localVersion, handler);
    }

    public void getFileData(){
        HomeUserGetRequest getRequest = new HomeUserGetRequest();
        getRequest.getFileNoReadCound(UserInfo.getInstance().getUser().getTeacherId(), handler);
    }

    private void setTabSelection(int index){
        if(index==currentIndex)
            return;
        currentIndex = index;
        setSelectionIcon(index);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (index){
            case 0:
                NavigationBarUtil.MIUISetStatusBarLightMode(getWindow(), false);
                if(homeFragment==null){
                    homeFragment = new NewHomeFragment();
                    transaction.add(R.id.content, homeFragment);
                }else{
                    transaction.show(homeFragment);
                    homeFragment.onResume();
                }
                currentFG = homeFragment;
                break;
            case 1:
                NavigationBarUtil.MIUISetStatusBarLightMode(getWindow(), false);
                if(messageFragment==null){
//                    messageFragment = new NewMessageFragment();
                    messageFragment = new MaterialFragment();
                    transaction.add(R.id.content, messageFragment);
                }else{
                    transaction.show(messageFragment);
                    messageFragment.onResume();
                }
                currentFG = messageFragment;
                break;
            /**
            case 2:
                NavigationBarUtil.MIUISetStatusBarLightMode(getWindow(), false);
                if(materialFragment==null){
                    materialFragment = new MaterialFragment();
                    transaction.add(R.id.content, materialFragment);
                }else{
                    transaction.show(materialFragment);
                    materialFragment.onResume();
                }
                break;

            case 3:
                NavigationBarUtil.MIUISetStatusBarLightMode(getWindow(), false);

                if(communicateListFragment==null){
                    communicateListFragment = new CommunicateListFragment();
                    transaction.add(R.id.content, communicateListFragment);
                }else{
                    transaction.show(communicateListFragment);
                    communicateListFragment.onResume();
                }
                break;
                 */
            case 2:
                NavigationBarUtil.MIUISetStatusBarLightMode(getWindow(), true);
                if(userFragment==null){
//                    userFragment = new UserFragment();
                    userFragment = new NewUserFragment();
                    transaction.add(R.id.content, userFragment);
                }else{
                    transaction.show(userFragment);
                    userFragment.onResume();
                }
                currentFG = userFragment;
                break;
        }
        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态 test
     * @param transaction
     */
    private void hideFragment(FragmentTransaction transaction){
        if(homeFragment!=null)
            transaction.hide(homeFragment);
        if(messageFragment!=null)
            transaction.hide(messageFragment);
//        if(materialFragment!=null)
//            transaction.hide(materialFragment);
//        if(communicateListFragment !=null)
//            transaction.hide(communicateListFragment);
        if(userFragment!=null)
            transaction.hide(userFragment);
    }

    private void setSelectionIcon(int index){
//        int[] ivs = {R.id.home_home_iv, R.id.home_task_iv, R.id.home_material_iv, R.id.home_communicate_iv, R.id.home_user_iv};
//        int[] tvs = {R.id.home_home_tv, R.id.home_task_tv, R.id.home_material_tv, R.id.home_communicate_tv, R.id.home_user_tv};
//        int[] bgs = {R.mipmap.home_bg, R.mipmap.task_bg, R.mipmap.material_bg, R.mipmap.communicate_bg, R.mipmap.user_bg};
//        int[] fcs = {R.mipmap.home_bg_fc, R.mipmap.task_bg_fc, R.mipmap.material_bg_fc, R.mipmap.communicate_bg_fc, R.mipmap.user_bg_fc};

        int[] ivs = {R.id.home_home_iv, R.id.home_task_iv, R.id.home_user_iv};
        int[] tvs = {R.id.home_home_tv, R.id.home_task_tv, R.id.home_user_tv};
        int[] bgs = {R.mipmap.home_bg, R.mipmap.icon_message_bg, R.mipmap.icon_user_bg};
        int[] fcs = {R.mipmap.home_bg_fc, R.mipmap.icon_message_fc, R.mipmap.icon_user_fc};
        ImageView imageView;
        TextView textView;
        for(int i=0; i<3; i++){
            imageView = findViewById(ivs[i]);
            textView = findViewById(tvs[i]);
            if(index==i){
                imageView.setBackgroundResource(fcs[i]);
                textView.setTextColor(getResources().getColor(R.color.app_foot_item_fc));
            }else{
                imageView.setBackgroundResource(bgs[i]);
                textView.setTextColor(getResources().getColor(R.color.app_foot_item));
            }
        }
    }

    private View.OnClickListener itemBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.home_btn:
                    setTabSelection(0);
                    break;
                case R.id.task_btn:
                    setTabSelection(1);
                    break;
                case R.id.material_btn:
                    setTabSelection(2);
                    break;
                case R.id.communicate_btn:
                    setTabSelection(3);
                    break;
                case R.id.user_btn:
                    setTabSelection(2);
                    break;
            }

        }
    };

    private void getUsersInfo(){
        MainRequest mainRequest = new MainRequest();
        mainRequest.getTeamUsers(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), null);
        mainRequest.getTalkUsers(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), null);
    }

    private void getUploadToken(){
        FileGetRequest getRequest = new FileGetRequest();
        getRequest.getFileUploadToken(null);
    }

    private void addUnreadCountObserver(){
        RongUtil.unReadCountAddListener(messageObserver);
    }

    private void removeUnreadCountObserver(){
        RongUtil.unReadCountRemoveListener(messageObserver);
    }

    private IUnReadMessageObserver messageObserver = new IUnReadMessageObserver() {
        @Override
        public void onCountChanged(int i) {
            Log.i("HuiZhi", "The unread message i:" + i);
            Message msg = new Message();
            msg.what = 2;
            msg.obj = i;
            handler.sendMessage(msg);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Main", "Come into activity result");
        if(currentFG!=null)
            currentFG.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
//        return super.onKeyDown(keyCode, event);
    }

    public void exit(){
        Log.i("HuiZhi", "Come into exit");
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(1, 2000);
        } else {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
//            System.exit(0);
            UserInfo.getInstance().setLogin(false);
            finish();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                isExit = false;
            }else if(msg.what==2){
                if(msg.obj==null)
                    return;
                int count = (int)msg.obj;
                setCommunicateCountStatus(count);
            }else if(msg.what==3){
                communicateIMConnect();
            }else if(msg.what== Constants.MSG_SUCCESS){
                VersionNode versionNode = (VersionNode)msg.obj;
                checkVersion(versionNode);
            }else if(msg.what == Constants.MSG_SUCCESS_ONE){
                ImageView fileIV = findViewById(R.id.material_count_iv);
                String count = (String)msg.obj;
                if("0".equals(count))
                    fileIV.setVisibility(View.INVISIBLE);
                else
                    fileIV.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * IMKit SDK调用第二步
     *
     * 建立与服务器的连接
     *
     */
    private void communicateIMConnect(){
        Log.i("HuiZhi", "Come into communicateIMConnect:" + UserInfo.getInstance().getUser().getChatToken());
        RongIM.connect(UserInfo.getInstance().getUser().getChatToken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //Connect Token 失效的状态处理，需要重新获取 Token
                Toast.makeText(MainActivity.this, "IM token 失效", Toast.LENGTH_SHORT).show();
                Log.i("HuiZhi", "——onTokenIncorrect—-");
            }

            @Override
            //连接成功
            public void onSuccess(String userId) {
                Log.i("HuiZhi", "——onSuccess—-" + userId);
            }

            @Override
            //连接失败  errorCode错误码
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.i("HuiZhi", "——onError—-" + errorCode);
                handler.sendMessageDelayed(handler.obtainMessage(3), 1000 * 3);
            }
        });
    }

    private void setCommunicateCountStatus(int count){
        ImageView flageIV = findViewById(R.id.communicate_count_iv);
        if(count==0){
            flageIV.setVisibility(View.GONE);
        }else {
            flageIV.setVisibility(View.VISIBLE);
        }
    }

    private void addRongListener(){
        RongIM.setConnectionStatusListener(connectionStatusListener);
    }

    private RongIMClient.ConnectionStatusListener connectionStatusListener = new RongIMClient.ConnectionStatusListener(){
        @Override
        public void onChanged(ConnectionStatus connectionStatus) {
            switch (connectionStatus) {

                case CONNECTED://连接成功。
                    Log.i("HuiZhi", "Come into rong CONNECTED");
                    break;
                case DISCONNECTED://断开连接。
                    Log.i("HuiZhi", "Come into rong DISCONNECTED");
                    break;
                case CONNECTING://连接中。
                    Log.i("HuiZhi", "Come into rong CONNECTING");
                    break;
                case NETWORK_UNAVAILABLE://网络不可用。
                    Log.i("HuiZhi", "Come into rong NETWORK_UNAVAILABLE");
                    break;
                case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                    Log.i("HuiZhi", "Come into rong KICKED_OFFLINE_BY_OTHER_CLIENT");
                    break;
            }

        }
    };

    /**
     * 版本更新检测
     */
    private void checkVersion(VersionNode node){
        if(node==null)
            return;
        VersionUtil versionUtil = new VersionUtil();
        boolean flage = versionUtil.checkVersion(this, node);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
        }
    }

}
