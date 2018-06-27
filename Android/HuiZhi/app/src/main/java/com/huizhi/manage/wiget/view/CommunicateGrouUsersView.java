package com.huizhi.manage.wiget.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.communicate.CommunicateListActivity;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.GroupChatAddOrDelDialog;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.request.main.MainRequest;
import com.huizhi.manage.util.PictureUtil;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by CL on 2018/3/4.
 */

public class CommunicateGrouUsersView extends LinearLayout {
    private Context context;
    private String targetId;
    private ImageView iv1, iv2, iv3, iv4, iv5;
    private TextView tv1, tv2, tv3, tv4, tv5;
    private List<String> ids;
    private AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
    private BaseInfoUpdate userInfoUpdate;
    private boolean isCreate = false;

    public CommunicateGrouUsersView(Activity context, String targetId, List<String> ids, boolean isCreate, BaseInfoUpdate userInfoUpdate){
        super(context);
        this.context = context;
        this.targetId = targetId;
        this.ids = ids;
        this.isCreate = isCreate;
        this.userInfoUpdate = userInfoUpdate;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_communicate_group_users, this);
        iv1 = findViewById(R.id.user_iv1);
        iv2 = findViewById(R.id.user_iv2);
        iv3 = findViewById(R.id.user_iv3);
        iv4 = findViewById(R.id.user_iv4);
        iv5 = findViewById(R.id.user_iv5);
        tv1 = findViewById(R.id.user_tv1);
        tv2 = findViewById(R.id.user_tv2);
        tv3 = findViewById(R.id.user_tv3);
        tv4 = findViewById(R.id.user_tv4);
        tv5 = findViewById(R.id.user_tv5);
    }

    public void setDates(String userid1, String userid2, String userid3, String userid4, String userid5, int index){
        Log.i("HuiZhi", "The cguv index:" + index + " userid1:" + userid1 + " userid2:" + userid2 + " userid3:" + userid3 + " userid4:" + userid4 + " userid5:" + userid5);
        if(!TextUtils.isEmpty(userid1)){
            setUserItem(userid1, iv1, tv1);
            iv1.setVisibility(VISIBLE);
            tv1.setVisibility(VISIBLE);
        }else {
            setAddDeletePerView(index);
            return;
        }

        if(!TextUtils.isEmpty(userid2)){
            setUserItem(userid2, iv2, tv2);
            iv2.setVisibility(VISIBLE);
            tv2.setVisibility(VISIBLE);
        }else {
            setAddDeletePerView(index);
            return;
        }

        if(!TextUtils.isEmpty(userid3)){
            setUserItem(userid3, iv3, tv3);
            iv3.setVisibility(VISIBLE);
            tv3.setVisibility(VISIBLE);
        }else {
            setAddDeletePerView(index);
            return;
        }

        if(!TextUtils.isEmpty(userid4)){
            setUserItem(userid4, iv4, tv4);
            iv4.setVisibility(VISIBLE);
            tv4.setVisibility(VISIBLE);
        }else {
            setAddDeletePerView(index);
            return;
        }

        if(!TextUtils.isEmpty(userid5)){
            setUserItem(userid5, iv5, tv5);
            iv5.setVisibility(VISIBLE);
            tv5.setVisibility(VISIBLE);
        }else {
            setAddDeletePerView(index);
            return;
        }
    }

    private void setUserItem(String userid, ImageView iv, TextView tv){
        if(TextUtils.isEmpty(userid))
            return;
        MainRequest mainRequest = new MainRequest();
        mainRequest.getTalkItemUser(userid, new UserInfoUpdate(userid, iv, tv));

        /**
        UserNode userNode = UserInfo.getInstance().getUserByTeacherId(userid);
        tv.setText(userNode.getTeacherName());
        try {
            Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(iv, AsyncFileUpload.getInstance().getFileUrl(userNode.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
                @Override
                public void imageLoad(ImageView imageView, Bitmap bitmap) {
                    imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                }
            });
            if(bitmap!=null){
                iv.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
         */
    }

    private void setAddDeletePerView(int index){
        if(index==0){
            iv1.setBackgroundResource(R.mipmap.icon_btn_deleteperson);
            iv1.setOnClickListener(deleteBtnClick);
            iv1.setVisibility(VISIBLE);
        }else if(index==1){
            iv1.setBackgroundResource(R.mipmap.icon_btn_addperson);
            iv2.setBackgroundResource(R.mipmap.icon_btn_deleteperson);
            iv1.setOnClickListener(addBtnClick);
            iv2.setOnClickListener(deleteBtnClick);
            iv1.setVisibility(VISIBLE);
            if(isCreate)
                iv2.setVisibility(VISIBLE);
        }else if(index==2){
            iv2.setBackgroundResource(R.mipmap.icon_btn_addperson);
            iv3.setBackgroundResource(R.mipmap.icon_btn_deleteperson);
            iv2.setOnClickListener(addBtnClick);
            iv3.setOnClickListener(deleteBtnClick);
            iv2.setVisibility(VISIBLE);
            if(isCreate)
                iv3.setVisibility(VISIBLE);
        }else if(index==3){
            iv3.setBackgroundResource(R.mipmap.icon_btn_addperson);
            iv4.setBackgroundResource(R.mipmap.icon_btn_deleteperson);
            iv3.setOnClickListener(addBtnClick);
            iv4.setOnClickListener(deleteBtnClick);
            iv3.setVisibility(VISIBLE);
            if(isCreate)
                iv4.setVisibility(VISIBLE);
        }else if(index==4){
            iv4.setBackgroundResource(R.mipmap.icon_btn_addperson);
            iv5.setBackgroundResource(R.mipmap.icon_btn_deleteperson);
            iv4.setOnClickListener(addBtnClick);
            iv5.setOnClickListener(deleteBtnClick);
            iv4.setVisibility(VISIBLE);
            if(isCreate)
                iv5.setVisibility(VISIBLE);
        }else if(index==5){
            iv5.setBackgroundResource(R.mipmap.icon_btn_addperson);
            iv5.setOnClickListener(addBtnClick);
            iv5.setVisibility(VISIBLE);
        }
    }

    private OnClickListener addBtnClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("HuiZhi", "Come into add btn click:" + ids);
            GroupChatAddOrDelDialog addOrDelDialog = new GroupChatAddOrDelDialog(context, infoUpdate);
            addOrDelDialog.showView(view, ids, true);
        }

        BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object==null)
                    return;
                final List<String> perSels = (List<String>)object;
                if(perSels==null)
                    return;
                Log.i("HuiZhi", "The add persels size:" + perSels.size());
                RongIM.getInstance().addMemberToDiscussion(targetId, perSels, new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        if(ids!=null)
                            ids.addAll(perSels);
                        userInfoUpdate.update(true);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }
        };
    };

    private OnClickListener deleteBtnClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("HuiZhi", "Come into delete btn click");
            GroupChatAddOrDelDialog addOrDelDialog = new GroupChatAddOrDelDialog(context, infoUpdate);
            addOrDelDialog.showView(view, ids, false);
        }

        int index = 0;
        BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object==null)
                    return;
                final List<String> perSels = (List<String>)object;
                if(perSels==null)
                    return;
                Log.i("HuiZhi", "The remove persels size:" + perSels.size());
                index = 0;
                for(final String perSelId:perSels){
                    Log.i("HuiZhi", "The remove id:" + perSelId);
                    RongIM.getInstance().removeMemberFromDiscussion(targetId, perSelId, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            index++;
                            if(ids!=null)
                                ids.remove(perSelId);
                            if(index==perSels.size()){
                                userInfoUpdate.update(true);
                            }
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            Log.i("HuiZhi", "come into remvoe error:" + errorCode.getValue() +"  message:" + errorCode.getMessage());
                        }
                    });
                }


            }
        };
    };

    private class UserInfoUpdate implements BaseInfoUpdate {
        private String targetid;
        private ImageView imageView;
        private TextView textView;
        private UserNode userNode;

        public UserInfoUpdate(String targetid, ImageView imageView, TextView textView) {
            this.targetid = targetid;
            this.imageView = imageView;
            this.textView = textView;
        }

        @Override
        public void update(Object object) {
            if(object==null)
                return;
            userNode = (UserNode)object;
            handler.sendEmptyMessage(1);
        }

        private Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    if(userNode==null)
                        return;
//                    UserNode userNode = UserInfo.getInstance().getUserByTeacherId(targetid);
                    textView.setText(userNode.getTeacherName());
                    try {
                        Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(imageView, AsyncFileUpload.getInstance().getFileUrl(userNode.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
                            @Override
                            public void imageLoad(ImageView imageView, Bitmap bitmap) {
                                imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                            }
                        });
                        if(bitmap!=null){
                            imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
    }


}
