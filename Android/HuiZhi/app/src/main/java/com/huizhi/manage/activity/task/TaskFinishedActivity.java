package com.huizhi.manage.activity.task;

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
import android.os.Environment;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.task.HomeTaskVerifyEditActivity;
import com.huizhi.manage.activity.material.MaterialFilePictureInfoActivity;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.PersonSelDialog;
import com.huizhi.manage.dialog.PictureSelDialog;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.TaskAccessory;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.request.common.FileGetRequest;
import com.huizhi.manage.request.task.TaskPostRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.FileUtil;
import com.huizhi.manage.util.PictureUtil;
import com.huizhi.manage.wiget.view.FileItemView;
import com.huizhi.manage.wiget.view.PictureItemView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2017/12/15.
 */

public class TaskFinishedActivity extends Activity{
    private TaskNode taskNode;
    private LinearLayout picturesLL, filesLL;
    private EditText assignInfoET;
    private List<TaskAccessory> picList = new ArrayList<>(), fileList = new ArrayList<>();
    private UserNode adminNode;
    private TextView personTV;
    private ImageView personIV;
    private String personSelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_finished);
        initDates();
        initViews();
        getUploadToken();
    }

    private void initDates(){
        taskNode = (TaskNode)getIntent().getSerializableExtra("Item");
//        picList = new ArrayList<>();
//        fileList = new ArrayList<>();
//        for(TaskAccessory item:taskNode.getTaskAccessoryLst()){
//            if(1==item.getFileType()){
//                picList.add(item);
//            }else if(2==item.getFileType()){
//                fileList.add(item);
//            }
//        }
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));
        if(taskNode==null)
            return;
        TextView titleTV = findViewById(R.id.title_tv);
        titleTV.setText(taskNode.getTaskTitle());
        TextView createTTV = findViewById(R.id.createtime_tv);
        createTTV.setText(taskNode.getStrCreateTime());
        TextView descriptionTV = findViewById(R.id.description_tv);
        descriptionTV.setText(taskNode.getStrCreateTime());
        personTV = findViewById(R.id.person_tv);
        personIV = findViewById(R.id.person_iv);
        adminNode = UserInfo.getInstance().getUserByTeacherId(taskNode.getCreateTeacherId());
        personSelId = taskNode.getCreateTeacherId();
        Log.i("HuiZhi", "Come into task finished admin");
//        ImageView personArrowIV = findViewById(R.id.person_arrow_iv);
        LinearLayout personSelLL = findViewById(R.id.person_sel_ll);
        personSelLL.setOnClickListener(personSelClick);
        if(adminNode!=null){
            Log.i("HuiZhi", "Come into task finished admin node");
            personTV.setText(adminNode.getTeacherName());
            AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
            try{
//                asyncBitmapLoader.showPicByVolleyRequest(TaskFinishedActivity.this, AsyncFileUpload.getInstance().getFileUrl(adminNode.getHeadImgUrl()), personIV);
                Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(personIV, AsyncFileUpload.getInstance().getFileUrl(adminNode.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
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
        }
        Button picSelBtn = findViewById(R.id.pic_upload_btn);
        picSelBtn.setOnClickListener(uploadSelClick);
        Button fileSelBtn = findViewById(R.id.file_upload_btn);
        fileSelBtn.setOnClickListener(uploadSelClick);
        picturesLL = findViewById(R.id.pictures_ll);
        filesLL = findViewById(R.id.files_ll);
        addAccessories();

        assignInfoET = findViewById(R.id.assigninfo_et);
        Button submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(submitBtnClick);
    }

    private void getUploadToken(){
        FileGetRequest getRequest = new FileGetRequest();
        getRequest.getFileUploadToken(null);
    }

    /**
     * 图片&附件加载
     */
    private void addAccessories(){
        if(picList!=null){
            for(TaskAccessory item:picList)
                addPicture(item, picList);
        }

        if(fileList!=null){
            for(TaskAccessory item:fileList)
                addFile(item);
        }
    }

    private View.OnClickListener uploadSelClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.pic_upload_btn){
                PictureSelDialog picSelDialog = new PictureSelDialog(TaskFinishedActivity.this, pictureSelInfo);
                picSelDialog.showView(view);
            }else if(view.getId()==R.id.file_upload_btn){
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, Constants.SELECT_FILE);
            }
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

    private void addNewPicture(String path, String key){
        String filename = path.substring(path.lastIndexOf("/") + 1);
        TaskAccessory taskAccessory = new TaskAccessory();
        taskAccessory.setFileLocalUrl(path);
        taskAccessory.setLocal(true);
        taskAccessory.setFileUrl(key);
        taskAccessory.setFileName(filename);
        picList.add(taskAccessory);
        addPicture(taskAccessory, picList);
    }

    private void addPicture(TaskAccessory accessory, List<TaskAccessory> picList){
        PictureItemView picItemV = new PictureItemView(this);
        picItemV.setDatas(accessory, picList, picItemDeleteUpdate);
        picturesLL.addView(picItemV);
    }

    private void addNewFile(String path, String key){
        String filename = path.substring(path.lastIndexOf("/") + 1);
        TaskAccessory taskAccessory = new TaskAccessory();
        taskAccessory.setFileLocalUrl(path);
        taskAccessory.setLocal(true);
        taskAccessory.setFileUrl(key);
        taskAccessory.setFileName(filename);
        fileList.add(taskAccessory);
        addFile(taskAccessory);
    }

    private void addFile(TaskAccessory accessory){
        FileItemView fileItemV = new FileItemView(this);
        fileItemV.setDatas(accessory, fileList, fileItemDeleteUpdate);
        filesLL.addView(fileItemV);
    }

    /**
     * 审核人
     */
    private View.OnClickListener personSelClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PersonSelDialog perSelDialog = new PersonSelDialog(TaskFinishedActivity.this, personSelId, true, personsInfoUpdate);
            perSelDialog.showView(view);
        }

        private BaseInfoUpdate personsInfoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object!=null){
                    personSelId = (String)object;
                    UserNode user = UserInfo.getInstance().getUserByTeacherId(personSelId);
                    personTV.setText(user.getTeacherName());
                    personIV.setImageBitmap(null);
                    personIV.setBackgroundResource(R.mipmap.user_icon);
                    try {
                        AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
//                        asyncBitmapLoader.showPicByVolleyRequest(HomeTaskVerifyEditActivity.this, URLData.getUrlFile(UserInfo.getInstance().getUser().getHeadImgUrl()), personIV);
                        Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(personIV, AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
                            @Override
                            public void imageLoad(ImageView imageView, Bitmap bitmap) {
//                                imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                                Log.i("HuiZhi", "The pic:" + bitmap);
                                imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                            }
                        });
                        Log.i("HuiZhi", "The 1 pic:" + bitmap);
                        if(bitmap!=null){
                            personIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
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

    private View.OnClickListener submitBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String taskId = taskNode.getTaskId();
            String userId = UserInfo.getInstance().getUser().getTeacherId();
            String nextApproveUI = personSelId;
            String assignReason = assignInfoET.getText().toString();
//            if(3!=taskNode.getTaskStatus()&&TextUtils.isEmpty(assignReason)){
//                Toast.makeText(TaskFinishedActivity.this, "备注信息不能为空", Toast.LENGTH_LONG).show();
//                return;
//            }
            TaskPostRequest postRequest = new TaskPostRequest();
            List<TaskAccessory> accessories = new ArrayList<>();
            accessories.addAll(picList);
            accessories.addAll(fileList);
            postRequest.postTaskFinish(taskId, userId, nextApproveUI, assignReason, getAccessoryJS(), handler);
        }

        private String getAccessoryJS(){
            JSONArray jsonAr = new JSONArray();
            JSONObject jsonOb = null;
            try{
                for (TaskAccessory node:picList){
                    jsonOb = createJsonOb(node, 1);
                    if(jsonOb==null)
                        continue;
                    jsonAr.put(jsonOb);
                    jsonOb = null;
                }
                for (TaskAccessory node:fileList){
                    jsonOb = createJsonOb(node, 2);
                    if(jsonOb==null)
                        continue;
                    jsonAr.put(jsonOb);
                    jsonOb = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return jsonAr.toString();
        }

        private JSONObject createJsonOb(TaskAccessory node, int filetype){
            if(node==null)
                return null;
            JSONObject jsonOb = new JSONObject();
            try {
                jsonOb.put("AccessoryType", "1");
                jsonOb.put("FileType", String.valueOf(filetype));
                jsonOb.put("FileUrl", node.getFileUrl());
                jsonOb.put("FileSize", node.getFileSize());
                jsonOb.put("FileName", node.getFileName());

            }catch (Exception e){

            }
            return jsonOb;
        }
    };
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        Toast.makeText(TaskFinishedActivity.this, message, Toast.LENGTH_LONG).show();
                        finish();
                    }
                    break;
                case Constants.MSG_FAILURE:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        Toast.makeText(TaskFinishedActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.TAKE_PICTURE){
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
                    String picPath = FileUtil.getPath(TaskFinishedActivity.this, photoUri);
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
                    String picPath = FileUtil.getPath(TaskFinishedActivity.this, originalUri);
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
        }else if(requestCode == Constants.SELECT_FILE){
            try{
                if(data==null)
                    return;
                Uri uri = data.getData();
                if(uri==null)
                    return;
                String filePath = "";
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                    filePath = FileUtil.getPath(this, uri);
                } else {//4.4一下系统调用方法
                    filePath = FileUtil.getRealFilePath(this, uri);
                }
                if(!TextUtils.isEmpty(filePath))
                    AsyncFileUpload.getInstance().upload(this, filePath, new FileInfoUpdate(filePath));
            }catch (Exception e){
                e.printStackTrace();
            }

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

    private class FileInfoUpdate implements BaseInfoUpdate{
        private String filepath;

        FileInfoUpdate(String filepath){
            this.filepath = filepath;
        }

        @Override
        public void update(Object object) {
            if(object==null)
                return;
            addNewFile(filepath, (String)object);
        }
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

    private BaseInfoUpdate fileItemDeleteUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            updateFiles();
        }
    };

    private void updateFiles(){
        filesLL.removeAllViews();
        for(TaskAccessory item:fileList){
            addFile(item);
        }
    }

    //图片对应Uri
    private Uri photoUri;
    //图片文件路径
    private String picPath;
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
