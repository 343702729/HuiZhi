package com.huizhi.manage.activity.home.task;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.task.TaskFinishedActivity;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.ContentEntryDialog;
import com.huizhi.manage.dialog.LoadingProgress;
import com.huizhi.manage.dialog.PersonMultSelDialog;
import com.huizhi.manage.dialog.PersonSelDialog;
import com.huizhi.manage.dialog.PictureSelDialog;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.TaskAccessory;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.request.home.HomeTaskPostRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.FileUtil;
import com.huizhi.manage.util.PictureUtil;
import com.huizhi.manage.wiget.SwitchView;
import com.huizhi.manage.wiget.datepicker.CustomDatePicker;
import com.huizhi.manage.wiget.view.PictureItemView;
import com.huizhi.manage.wiget.view.SubTaskItemView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by CL on 2018/1/9.
 * 创建自定义任务
 */

public class HomeTaskCustomFoundActivity extends Activity {
    private Button priHBtn, priMbtn, priLBtn;
    private FrameLayout limtTimeFL;
    private SwitchView switchBtn;
    private TextView limtTimeTV;
    private int priority = 3;
    private boolean isLimitTime = false;
    private FrameLayout titleFL, descriptionFL;
    private TextView titleTV, descriptionTV, personTV, personCTV;
    private ImageView personIV, personCIV;
    private String personSelId = "", personCSelId = "";
    private List<TaskNode> subTasks = new ArrayList<>();
    private LinearLayout subTaskLL;
    private LinearLayout personCCLL;
    private String uniqueId;
    private LoadingProgress loadingProgress;

    private LinearLayout picturesLL;
    //图片对应Uri
    private Uri photoUri;
    //图片文件路径
    private String picPath;
    private List<TaskAccessory> picList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_task_custom_found);
        initDates();
        initViews();
    }

    private void initDates(){
        UUID uuid = UUID.randomUUID();
        uniqueId = uuid.toString().toUpperCase();
        Log.i("Task", "The task unique id:" + uniqueId);
    }

    private void initViews() {
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        titleTV = findViewById(R.id.title_tv);
        titleFL = findViewById(R.id.title_ll);
        titleFL.setOnClickListener(titleFLClick);

        descriptionTV = findViewById(R.id.description_tv);
        descriptionFL = findViewById(R.id.description_fl);
        descriptionFL.setOnClickListener(descriptionFLClick);

        personTV = findViewById(R.id.person_tv);
        personIV = findViewById(R.id.person_iv);

        LinearLayout personSelLL = findViewById(R.id.person_sel_ll);
        personSelLL.setOnClickListener(personSelClick);

        personCTV = findViewById(R.id.person_c_tv);
        personCIV = findViewById(R.id.person_c_iv);

        personCCLL = findViewById(R.id.person_c_ll);
        LinearLayout personCSelLL = findViewById(R.id.person_c_sel_ll);
        personCSelLL.setOnClickListener(personCSelClick);

        priHBtn = (Button)findViewById(R.id.pro_h_btn);
        priHBtn.setOnClickListener(priorityBtnClick);
        priMbtn = (Button)findViewById(R.id.pro_m_btn);
        priMbtn.setOnClickListener(priorityBtnClick);
        priLBtn = (Button)findViewById(R.id.pro_l_btn);
        priLBtn.setOnClickListener(priorityBtnClick);
        switchBtn = findViewById(R.id.switch_btn);
        switchBtn.setOnStateChangedListener(timeSwitchChange);
        limtTimeFL = findViewById(R.id.timelimit_fl);
        limtTimeFL.setOnClickListener(timeLimitClick);
        limtTimeTV = findViewById(R.id.limttime_tv);

        ImageView addPicIV = findViewById(R.id.addpic_iv);
        addPicIV.setOnClickListener(addPicClick);
        picturesLL = findViewById(R.id.pictures_ll);

        subTaskLL = findViewById(R.id.sub_task_ll);

        ImageButton subTaskAddBtn = findViewById(R.id.add_subtask_btn);
        subTaskAddBtn.setOnClickListener(addSubTaskClick);

        Button submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(submitBtnClick);
    }

    private View.OnClickListener priorityBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.pro_h_btn:
                    priorityBtnSel(3);
                    break;
                case R.id.pro_m_btn:
                    priorityBtnSel(2);
                    break;
                case R.id.pro_l_btn:
                    priorityBtnSel(1);
                    break;
            }
        }
    };

    /**
     * 任务标题
     */
    private View.OnClickListener titleFLClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("Task", "title tv click");
            ContentEntryDialog ceDialog = new ContentEntryDialog(HomeTaskCustomFoundActivity.this,  infoUpdate);
            ceDialog.showView(view, titleTV.getText().toString(), "任务标题");
        }

        private BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object==null)
                    return;
                titleTV.setText((String)object);
            }
        };
    };

    /**
     * 任务描述
     */
    private View.OnClickListener descriptionFLClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("Task", "title tv click");
            ContentEntryDialog ceDialog = new ContentEntryDialog(HomeTaskCustomFoundActivity.this,  infoUpdate);
            ceDialog.showView(view, descriptionTV.getText().toString(), "任务描述");
        }

        private BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object==null)
                    return;
                descriptionTV.setText((String)object);
            }
        };
    };

    /**
     * 处理人
     */
    private View.OnClickListener personSelClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PersonSelDialog perSelDialog = new PersonSelDialog(HomeTaskCustomFoundActivity.this, personSelId, false, true, personsInfoUpdate);
            perSelDialog.showView(view);
        }

        private BaseInfoUpdate personsInfoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object!=null){
                    personSelId = (String)object;
                    personIV.setImageBitmap(null);
                    personIV.setBackgroundResource(R.mipmap.user_icon);
                    if("1".equals(personSelId)){
                        Log.i("HuiZhi", "Come into person select all");
                        personTV.setText("所有人");
                        personCCLL.setVisibility(View.GONE);
                    }else {
                        UserNode user = UserInfo.getInstance().getUserByTeacherId(personSelId);
                        personTV.setText(user.getTeacherName());

                        try {
                            AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
                            Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(personIV, AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
                                @Override
                                public void imageLoad(ImageView imageView, Bitmap bitmap) {
                                    imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                                }
                            });
                            if(bitmap!=null){
                                personIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        personCCLL.setVisibility(View.VISIBLE);
                    }

                    personTV.setVisibility(View.VISIBLE);
                    personIV.setVisibility(View.VISIBLE);
                }else{
                    personSelId = null;
                    personTV.setVisibility(View.GONE);
                    personIV.setVisibility(View.GONE);
                }
            }
        };
    };

    /**
     * 抄送人
     */
    private View.OnClickListener personCSelClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PersonMultSelDialog multSelDialog = new PersonMultSelDialog(HomeTaskCustomFoundActivity.this, personCSelId, infoUpdate);
            multSelDialog.showView(view);
        }

        private BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object==null)
                    personCSelId = "";
                else
                    personCSelId = (String)object;
                Log.i("HuiZhi", "Come into personCSelId:" + personCSelId);
                if(TextUtils.isEmpty(personCSelId)){
                    personCTV.setVisibility(View.GONE);
                    personCIV.setVisibility(View.GONE);
                }else {
                    personCIV.setImageBitmap(null);
                    personCIV.setBackgroundResource(R.mipmap.user_icon);
                    String[] perids = personCSelId.split(",");
                    UserNode user = UserInfo.getInstance().getUserByTeacherId(perids[0]);
                    if(perids.length>1){
                        personCTV.setText(user.getTeacherName() + "等" + perids.length + "人");
                    }else {
                        personCTV.setText(user.getTeacherName());
                    }

                    try {
                        AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
                        Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(personCIV, AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
                            @Override
                            public void imageLoad(ImageView imageView, Bitmap bitmap) {
                                imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                            }
                        });
                        if(bitmap!=null){
                            personCIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    personCTV.setVisibility(View.VISIBLE);
                    personCIV.setVisibility(View.VISIBLE);
                }

            }
        };
    };

    private SwitchView.OnStateChangedListener timeSwitchChange = new SwitchView.OnStateChangedListener() {
        @Override
        public void toggleToOn() {
            switchBtn.toggleSwitch(true);
            limtTimeTV.setText("");
            isLimitTime = true;
            limtTimeFL.setVisibility(View.VISIBLE);
        }

        @Override
        public void toggleToOff() {
            switchBtn.toggleSwitch(false);
            limtTimeFL.setVisibility(View.GONE);
            limtTimeTV.setText("");
            isLimitTime = false;
        }
    };

    private View.OnClickListener timeLimitClick = new View.OnClickListener() {
        private CustomDatePicker customDatePicker, customDatePicker1;

        @Override
        public void onClick(View view) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            String now = sdf.format(new Date());
            initDatePicker();
            customDatePicker.show(now);
        }

        private void initDatePicker() {
            customDatePicker = new CustomDatePicker(HomeTaskCustomFoundActivity.this, new CustomDatePicker.ResultHandler() {
                @Override
                public void handle(String time) { // 回调接口，获得选中的时间
                    limtTimeTV.setText(time);
                }
            }, "2017-01-01 00:00", "2037-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
            customDatePicker.showSpecificTime(true); // 显示时和分
            customDatePicker.setIsLoop(true); // 允许循环滚动
        }
    };

    private void priorityBtnSel(int index){
        priority = index;
        //setBackground(context.getResources().getDrawable(R.drawable.frame_daily_do));
        priHBtn.setBackground(getResources().getDrawable(R.drawable.frame_light_gray));
        priMbtn.setBackground(getResources().getDrawable(R.drawable.frame_light_gray));
        priLBtn.setBackground(getResources().getDrawable(R.drawable.frame_light_gray));
        priHBtn.setTextColor(getResources().getColor(R.color.gray));
        priMbtn.setTextColor(getResources().getColor(R.color.gray));
        priLBtn.setTextColor(getResources().getColor(R.color.gray));
        if(index==3){
            priHBtn.setBackground(getResources().getDrawable(R.drawable.frame_bg_red));
            priHBtn.setTextColor(getResources().getColor(R.color.white));
        }else if(index==2){
            priMbtn.setBackground(getResources().getDrawable(R.drawable.frame_bg_red));
            priMbtn.setTextColor(getResources().getColor(R.color.white));
        }else if(index==1){
            priLBtn.setBackground(getResources().getDrawable(R.drawable.frame_bg_red));
            priLBtn.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private View.OnClickListener addSubTaskClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(HomeTaskCustomFoundActivity.this, HomeTaskCustomSubFoundActivity.class);
            intent.putExtra("ParentId", uniqueId);
            startActivityForResult(intent, Constants.REQUEST_CODE);
        }
    };

    private View.OnClickListener addPicClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PictureSelDialog picSelDialog = new PictureSelDialog(HomeTaskCustomFoundActivity.this, pictureSelInfo);
            picSelDialog.showView(view);
        }

        private BaseInfoUpdate pictureSelInfo = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object==null)
                    return;
                int index = (Integer)object;
                if(index==1){
                    //小于6.0版本直接操作
                    if (Build.VERSION.SDK_INT < 23) {
                        takePictures();
                    } else {
                        //6.0以后权限处理
                        permissionForM();
                    }

                    //拍照
//                    String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+Constants.TAKE_PICTURE_PATH;
//                    File temp = new File(imageFilePath);
//                    if(temp.exists())
//                        temp.delete();
//                    Uri imageFileUri = Uri.fromFile(temp);//获取文件的Uri
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//跳转到相机Activity
//                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);//告诉相机拍摄完毕输出图片到指定的Uri
//                    startActivityForResult(intent, Constants.TAKE_PICTURE);
                }else if(index==2){
                    //相册选择
                    Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, Constants.SELECT_PICTURE);
                }

            }
        };
    };

    private View.OnClickListener submitBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String createTechId = UserInfo.getInstance().getUser().getTeacherId();
            String title = titleTV.getText().toString();
            if(TextUtils.isEmpty(title)){
                Toast.makeText(HomeTaskCustomFoundActivity.this, "任务标题不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            String description = descriptionTV.getText().toString();
//            if(TextUtils.isEmpty(description)){
//                Toast.makeText(HomeTaskCustomFoundActivity.this, "任务描述不能为空", Toast.LENGTH_LONG).show();
//                return;
//            }
            if(TextUtils.isEmpty(personSelId)){
                Toast.makeText(HomeTaskCustomFoundActivity.this, "请选择处理人", Toast.LENGTH_LONG).show();
                return;
            }
            int isTimeLimit = 0;
            String planEndTime = "";
            if(isLimitTime) {
                isTimeLimit = 1;
                planEndTime = limtTimeTV.getText().toString();
            };

            String assignReason = "";

            TaskNode taskN = new TaskNode();
            taskN.setTaskId(uniqueId);
            taskN.setTaskTitle(title);
            taskN.setTaskDescription(description);
            taskN.setTimeLimit(isLimitTime);
            taskN.setStrPlanEndTime(planEndTime);
            taskN.setPriority(priority);
            if("1".equals(personSelId)){
                taskN.setAtAll(personSelId);
                taskN.setProcessingTeacherId("");
            }else {
                taskN.setAtAll("0");
                taskN.setProcessingTeacherId(personSelId);
            }
            taskN.setCcTeacherId(personCSelId);
            taskN.setTaskMainAccessoryLst(picList);
            List<TaskNode> items = new ArrayList<>();
            items.add(taskN);
            items.addAll(subTasks);
            loadingProgress = new LoadingProgress(HomeTaskCustomFoundActivity.this, null);
            loadingProgress.showView(view);
            HomeTaskPostRequest postRequest = new HomeTaskPostRequest();
            postRequest.postCustomTask(createTechId, UserInfo.getInstance().getUser().getSchoolId(), getTasksJS(items), handler);
//            postRequest.postCustomTask(uniqueId, "", createTechId, title, description, isTimeLimit, planEndTime, priority, personSelId, assignReason, handler);
        }

        private String getTasksJS(List<TaskNode> nodes){
            JSONArray jsonAr = new JSONArray();
            JSONObject jsonOb = null;
            try{
                for (TaskNode node:nodes){
                    jsonOb = new JSONObject();
                    jsonOb.put("TaskId", node.getTaskId());
                    jsonOb.put("ParentTaskId", node.getParentId());
                    jsonOb.put("TaskTitle", node.getTaskTitle());
                    jsonOb.put("TaskDescription", node.getTaskDescription());
                    if(node.isTimeLimit())
                        jsonOb.put("IsTimeLimit", 1);
                    else
                        jsonOb.put("IsTimeLimit", 0);
                    jsonOb.put("PlanEndTime", node.getStrPlanEndTime());
                    jsonOb.put("Priority", node.getPriority());
                    jsonOb.put("ProcessingTeacherId", node.getProcessingTeacherId());
                    jsonOb.put("CCTeacherId", node.getCcTeacherId());
                    jsonOb.put("AssignReason", "");
                    jsonOb.put("AtAll", node.getAtAll());
                    jsonOb.put("AccessoryList", getAccessoryListStr(node.getTaskMainAccessoryLst()));
                    jsonAr.put(jsonOb);
                    jsonOb = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return jsonAr.toString();
        }

        private JSONArray getAccessoryListStr(List<TaskAccessory> accessories){
            if(accessories==null)
                return null;
            JSONArray jsonAr = new JSONArray();
            JSONObject jsonOb = null;
            try{
                for (TaskAccessory node:accessories){
                    jsonOb = new JSONObject();
                    jsonOb.put("AccessoryId", "");
                    jsonOb.put("AccessoryType", node.getAccessoryType());
                    jsonOb.put("FileType", node.getFileType());
                    jsonOb.put("FileUrl", node.getFileUrl());
                    jsonOb.put("FileSize", node.getFileSize());
                    jsonOb.put("FileName", node.getFileName());
                    jsonAr.put(jsonOb);
                    jsonOb = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return jsonAr;
        }

    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(loadingProgress!=null)
                loadingProgress.closeView();
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        Toast.makeText(HomeTaskCustomFoundActivity.this, message, Toast.LENGTH_LONG).show();
                        if(UserInfo.getInstance().getTaskCreatInfo()!=null){
                            UserInfo.getInstance().getTaskCreatInfo().update(true);
                        }
                        finish();
                    }
                    break;
                case Constants.MSG_FAILURE:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        Toast.makeText(HomeTaskCustomFoundActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.REQUEST_CODE && resultCode==Constants.RESULT_CODE){
            Log.i("Task", "come into task custom result");
            TaskNode taskNode = (TaskNode)data.getSerializableExtra("Item");
            if(taskNode!=null){
                Log.i("Task", "The task title is:" + taskNode.getTaskTitle());
                updateTaskNode(taskNode);
                addSubTaskViews();
            }
        }else if(requestCode == Constants.TAKE_PICTURE){
            String[] pojo = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                cursor.moveToFirst();
                picPath = cursor.getString(columnIndex);
                if (Build.VERSION.SDK_INT < 14) {
                    cursor.close();
                }
            }
            Log.i("HuiZhi", "The take picPath:" + picPath);
            if (picPath != null ) {//&& (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))
                photoUri = Uri.fromFile(new File(picPath));
                if (Build.VERSION.SDK_INT > 23) {
//                    photoUri = FileProvider.getUriForFile(this, "com.innopro.bamboo.fileprovider", new File(picPath));
                    AsyncFileUpload.getInstance().upload(this, picPath, new PicInfoUpdate(picPath));
                } else {
                    String picPath = FileUtil.getPath(HomeTaskCustomFoundActivity.this, photoUri);
                    AsyncFileUpload.getInstance().upload(this, picPath, new PicInfoUpdate(picPath));
                }
            } else {
                //错误提示
            }
//            photoBt = BitmapFactory.decodeFile(photoPath);
//            System.out.println("take bt:" + photoBt);
//            photoBtn.setImageBitmap(photoBt);
        }else if(requestCode == Constants.SELECT_PICTURE){
            // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
            System.out.println("come into search ar selectphoto");
            ContentResolver resolver = getContentResolver();
            try {
                Uri originalUri = data.getData(); // 获得图片的uri
                System.out.println("The uri is:" + originalUri);
                if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){
                    String picPath = FileUtil.getPath(HomeTaskCustomFoundActivity.this, originalUri);
                    Log.i("Task", "The picture path is:" + picPath);
//                    System.out.println("The pic path:" + picPath);
                    AsyncFileUpload.getInstance().upload(this, picPath, new PicInfoUpdate(picPath));

                }else{
//                    photoBt = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片
//                    System.out.println("select bt:" + photoBt);
//                    photoBtn.setImageBitmap(photoBt);
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

    }

    private void updateTaskNode(TaskNode node){
        if(node==null)
            return;
        String uniqueId = node.getTaskId();
        if(TextUtils.isEmpty(uniqueId))
            return;
        for(TaskNode itemN:subTasks){
            if(uniqueId.equals(itemN.getTaskId())){
                int index = subTasks.indexOf(itemN);
                subTasks.remove(itemN);
                subTasks.add(index, node);
                return;
            }
        }
        subTasks.add(node);
    }

    private void addSubTaskViews(){
        Log.i("Task", "The sub task size is:" + subTasks.size());
        //subTaskLL
        subTaskLL.removeAllViews();
        for(TaskNode node:subTasks){
            SubTaskItemView subTaskV = new SubTaskItemView(this);
            subTaskV.setDates(node, uniqueId);
            subTaskLL.addView(subTaskV);
        }
    }

    private class PicInfoUpdate implements BaseInfoUpdate {
        private String picpath;

        PicInfoUpdate(String picpath){
            this.picpath = picpath;
        }

        @Override
        public void update(Object object) {
            if(object==null)
                return;
            addNewPicture(picpath, (String)object);
        }
    };

    private void addNewPicture(String path, String key){
        String filename = path.substring(path.lastIndexOf("/") + 1);
        TaskAccessory taskAccessory = new TaskAccessory();
        taskAccessory.setFileLocalUrl(path);
        taskAccessory.setLocal(true);
        taskAccessory.setFileUrl(key);
        taskAccessory.setAccessoryType(1);
        taskAccessory.setFileType(1);
        taskAccessory.setFileName(filename);
        picList.add(taskAccessory);
        addPicture(taskAccessory, picList);
    }

    private void addPicture(TaskAccessory accessory, List<TaskAccessory> picList){
        PictureItemView picItemV = new PictureItemView(this);
        picItemV.setDatas(accessory, picList, picItemDeleteUpdate);
        picturesLL.addView(picItemV);
    }

    private BaseInfoUpdate picItemDeleteUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            updatePics();
        }
    };

    private void updatePics(){
        picturesLL.removeAllViews();
        Log.i("HuiZhi", "The pic list:" + picList.size());
        for(TaskAccessory item:picList){
            addPicture(item, picList);
        }
    }

    /**
     * 拍照获取图片
     */
    private void takePictures() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, Constants.TAKE_PICTURE);
    }

    /**
     * 安卓6.0以上版本权限处理
     */
    private void permissionForM() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.TAKE_PICTURE);
        } else {
            takePictures();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == Constants.TAKE_PICTURE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePictures();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
