package com.huizhi.manage.activity.communicate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.communicate.CommunicateUsersAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.ContentEntryDialog;
import com.huizhi.manage.dialog.GroupChatSelDialog;
import com.huizhi.manage.dialog.SchoolSelDialog;
import com.huizhi.manage.node.SchoolNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.character.CharacterSearchUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by CL on 2018/2/5.
 * 通讯录
 */

public class CommunicateUsersActivity extends Activity {
    private ListView listView;
    private CommunicateUsersAdapter usersAdapter;
    private List<String> persons;
    private EditText searchET;

    private List<UserNode> talkUsers;
    private String schoolId;
    private SchoolNode schoolNode;
    private TextView schoolTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_communicate);
        initViews();
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        talkUsers = UserInfo.getInstance().getTalkUsers();

        schoolTV = findViewById(R.id.school_tv);

//        Button qlBtn = findViewById(R.id.ql_btn);
//        qlBtn.setOnClickListener(grupChatClick);

        LinearLayout switchLL = findViewById(R.id.switch_ll);
        switchLL.setOnClickListener(switchSchoolBtnClick);
        LinearLayout qunlLL = findViewById(R.id.qunl_ll);
        qunlLL.setOnClickListener(grupChatClick);

        searchET = findViewById(R.id.search_et);
        searchET.addTextChangedListener(userTextWatcher);

        listView = findViewById(R.id.listview);
        listView.setOnItemClickListener(listItemClick);
        usersAdapter = new CommunicateUsersAdapter(this, talkUsers);
        listView.setAdapter(usersAdapter);

//        Log.i("HuiZhi", "The talk users size:" + UserInfo.getInstance().getTalkUsers().size());
    }

    private View.OnClickListener grupChatClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            GroupChatSelDialog selDialog = new GroupChatSelDialog(CommunicateUsersActivity.this, talkUsers, personSelInfo);
            selDialog.showView(view);
        }
    };

    private View.OnClickListener switchSchoolBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SchoolSelDialog schoolSelDialog = new SchoolSelDialog(CommunicateUsersActivity.this, schoolId, 130, infoUpdate);
            schoolSelDialog.showView(view);
        }

        BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object==null)
                    return;

                schoolNode = (SchoolNode)object;
                schoolId = schoolNode.getSchoolId();
                UserInfo.getInstance().setSwitchSchool(schoolNode);
                schoolTV.setText(schoolNode.getSchoolName());
            }
        };
    };

    private BaseInfoUpdate personSelInfo = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            persons = (List<String>)object;
            ContentEntryDialog entryDialog = new ContentEntryDialog(CommunicateUsersActivity.this, titleInfoUpdate);
            entryDialog.showView(listView, "群聊标题");
        }
    };

    private BaseInfoUpdate titleInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            String title = (String)object;
            Log.i("HuiZhi", "The persons size:" + persons.size());
            RongIM.getInstance().createDiscussionChat(CommunicateUsersActivity.this, persons,  title, new RongIMClient.CreateDiscussionCallback(){
                @Override
                public void onSuccess(String s) {
                    Log.i("HuiZhi", "Come into group chat success:" + s);
                    finish();;
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    };

    private TextWatcher userTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(talkUsers==null||talkUsers.size()==0)
                return;
            String str = searchET.getText().toString();
            Log.i("HuiZhi", "The user str:" + str);
            if(TextUtils.isEmpty(str)){
                usersAdapter = new CommunicateUsersAdapter(CommunicateUsersActivity.this, talkUsers);
                listView.setAdapter(usersAdapter);
                return;
            }

            List<UserNode> otherU = new ArrayList<>();
            for(UserNode user:talkUsers){
                if(user.getType()!=1){
                    otherU.add(user);
                }
            }
            List<UserNode> users = CharacterSearchUtil.searchUsers(str, otherU);
            for(UserNode node:users){
                Log.i("HuiZhi", "The name:" + node.getTeacherName() + "  img:" + node.getHeadImgUrl());
            }
//            usersAdapter.updateUsers(users);
            usersAdapter = new CommunicateUsersAdapter(CommunicateUsersActivity.this, users);
            listView.setAdapter(usersAdapter);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private AdapterView.OnItemClickListener listItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            Intent intent = new Intent();
//            intent.setClass(activity, CommunicateActivity.class);
//            activity.startActivity(intent);
            UserNode userNode = (UserNode)usersAdapter.getItem(i);
            RongIM.getInstance().startPrivateChat(CommunicateUsersActivity.this, userNode.getTeacherId(), userNode.getTeacherName());
        }
    };

}
