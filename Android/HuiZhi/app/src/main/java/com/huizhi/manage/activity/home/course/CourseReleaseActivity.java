package com.huizhi.manage.activity.home.course;

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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.task.TaskFinishedActivity;
import com.huizhi.manage.adapter.home.CoursePictureAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.PictureSelDialog;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.node.PictureNode;
import com.huizhi.manage.node.StudentNode;
import com.huizhi.manage.request.home.HomeCourseGetRequest;
import com.huizhi.manage.request.home.HomeCoursePostRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.FileUtil;
import com.huizhi.manage.util.PictureUtil;
import com.huizhi.manage.wiget.MyGridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CourseReleaseActivity extends Activity {
    private String lessonNum;
    private String stuNum;
    private MyGridView gridView;
    private TextView signSTV;
    private Button signBtn;
    private EditText commentET;
    private CheckBox publishCB;
    private CoursePictureAdapter pictureAdapter;
    private StudentNode studentNode;
    private List<PictureNode> picNodes = new ArrayList<>();
    private boolean isClass = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_course_release);
        initDatas();
        initViews();
        getDatas();
    }

    private void initDatas(){
        isClass = getIntent().getBooleanExtra("IsClass", false);
        if(!isClass) {
            lessonNum = getIntent().getStringExtra("LessonNum");
            stuNum = getIntent().getStringExtra("StuNum");
        }else {
            lessonNum = getIntent().getStringExtra("LessonNum");
        }
    }

    private void initViews() {
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        LinearLayout topLL = findViewById(R.id.top_ll);
        if(isClass)
            topLL.setVisibility(View.GONE);

        signSTV = findViewById(R.id.sign_status_tv);
        signBtn = findViewById(R.id.sign_btn);

        commentET = findViewById(R.id.comment_et);

        publishCB = findViewById(R.id.publish_cb);


        gridView = findViewById(R.id.gridview);
        pictureAdapter = new CoursePictureAdapter(this, picNodes);
        gridView.setAdapter(pictureAdapter);

        LinearLayout submitLL = findViewById(R.id.submit_ll);
        submitLL.setOnClickListener(submitLLClick);

        Button saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(saveBtnClick);
    }

    private void getDatas(){
        HomeCourseGetRequest getRequest = new HomeCourseGetRequest();
//        if(!isClass)
            getRequest.getCourseStuInfo(lessonNum, stuNum, handler);
    }

    private View.OnClickListener submitLLClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!isClass){
                if(studentNode.getStuStatus()!=1){
                    Toast.makeText(CourseReleaseActivity.this, "请先给学员签到，再上传作品", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if(picNodes.size()>=4){
                Toast.makeText(CourseReleaseActivity.this, "最多上传4张", Toast.LENGTH_SHORT).show();
                return;
            }
            PictureSelDialog picSelDialog = new PictureSelDialog(CourseReleaseActivity.this, pictureSelInfo);
            picSelDialog.showView(view);

        }
    };

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

            }else if(index==2){
                if (Build.VERSION.SDK_INT < 23) {
                    //相册选择
                    Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, Constants.SELECT_PICTURE);

                } else {
                    permissionForS();
                }

            }

        }
    };

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

    private void permissionForS() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.SELECT_PICTURE);
        } else {
            //相册选择
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, Constants.SELECT_PICTURE);
        }
    }

    private View.OnClickListener saveBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("HuiZhi", "The pic size is:" + picNodes.size());
            boolean isPublish = publishCB.isChecked();
            String comment = commentET.getText().toString();
            int status = 0;

            if(isPublish){
                if(picNodes.size()==0){
                    Toast.makeText(CourseReleaseActivity.this, "请上传图片", Toast.LENGTH_LONG).show();
                    return;
                }
            }else {
                if(picNodes.size()==0&&TextUtils.isEmpty(comment)){
                    Toast.makeText(CourseReleaseActivity.this, "图片和评语不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            if(isPublish)
                status = 1;



//            if(TextUtils.isEmpty(comment)){
//                Toast.makeText(CourseReleaseActivity.this, "评语不能为空", Toast.LENGTH_LONG).show();
//                return;
//            }

            int isStuWork = 0;

            HomeCoursePostRequest postRequest = new HomeCoursePostRequest();
            if(isClass){
                isStuWork = 0;
                postRequest.postPublish(isStuWork, UserInfo.getInstance().getUser().getTeacherName(), lessonNum, stuNum, getPicsJson(), "", comment, status, "", handler);
            }else {
                isStuWork = 1;
                postRequest.postPublish(isStuWork, UserInfo.getInstance().getUser().getTeacherName(), lessonNum, stuNum, getPicsJson(), "", comment, status, "", handler);
            }

        }

        private String getPicsJson(){
            if(picNodes==null||picNodes.size()==0)
                return "";
            JSONArray jsonAr = new JSONArray();
            try {
                for (PictureNode picNode:picNodes){
                    JSONObject jsonOb = new JSONObject();
                    jsonOb.put("ImageUrl", picNode.getUrl());
                    jsonOb.put("ImageSize", "1");
                    jsonOb.put("FileSize", "1");
                    jsonAr.put(jsonOb);
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            String pics = jsonAr.toString();
            Log.i("HuiZhi", "The json pics is:" + pics);
            return pics;
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    studentNode = (StudentNode)msg.obj;
                    setViewsData(studentNode);
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    signSTV.setText("已签到");
                    signSTV.setTextColor(getResources().getColor(R.color.app_green));
                    signBtn.setVisibility(View.GONE);
                    if(studentNode!=null) {
                        studentNode.setStuStatus(1);
                        studentNode.setStrStuStatus("已签到");
                    }
                    break;
                case Constants.MSG_SUCCESS_TWO:
                    String successMsg = (String)msg.obj;
                    Toast.makeText(CourseReleaseActivity.this, successMsg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(Constants.RESULT_CODE, intent);
                    finish();
                    break;
                case Constants.MSG_FAILURE:
                    String failMsg = (String)msg.obj;
                    Toast.makeText(CourseReleaseActivity.this, failMsg, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void setViewsData(StudentNode node){
        if(node==null)
            return;
        ImageView userIV = findViewById(R.id.user_iv);
        try {
            AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
            Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(userIV, node.getFullHeadImgUrl(), new AsyncBitmapLoader.ImageCallBack() {
                @Override
                public void imageLoad(ImageView imageView, Bitmap bitmap) {
                    imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                }
            });
            if(bitmap!=null){
                userIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TextView nameTV = findViewById(R.id.name_tv);
        nameTV.setText(node.getStuName());
        signBtn.setOnClickListener(new SignBtnClick(node.getStuNum()));
        signSTV.setText(node.getStrStuStatus());
        if(node.getStuStatus()==-1){
            signSTV.setTextColor(getResources().getColor(R.color.red));
        }else if(node.getStuStatus()==0){
            signSTV.setTextColor(getResources().getColor(R.color.dark_gray));
            signBtn.setVisibility(View.VISIBLE);
        }else if(node.getStuStatus()==1){
            signSTV.setTextColor(getResources().getColor(R.color.app_green));
        }
        if(node.getPictures()!=null){
            picNodes.addAll(node.getPictures());
            pictureAdapter.updateViewsData(picNodes);
        }
        commentET.setText(node.getComment());
        publishCB.setChecked(node.isPublished());
    }

    private class SignBtnClick implements View.OnClickListener{
        private String stuNum;

        public SignBtnClick(String stuNum){
            this.stuNum = stuNum;
        }

        @Override
        public void onClick(View view) {
            HomeCoursePostRequest postRequest = new HomeCoursePostRequest();
            postRequest.postStudentsSignInfo(lessonNum, stuNum, handler);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.TAKE_PICTURE&&resultCode == Activity.RESULT_OK){
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
                    String picPath = FileUtil.getPath(CourseReleaseActivity.this, photoUri);
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
                    String picPath = FileUtil.getPath(CourseReleaseActivity.this, originalUri);
//                    Toast.makeText(this, "选择本地图片：" + picPath, Toast.LENGTH_LONG).show();
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

    private class PicInfoUpdate implements BaseInfoUpdate {
        private String picpath;

        PicInfoUpdate(String picpath){
            this.picpath = picpath;
        }

        @Override
        public void update(Object object) {
            if(object==null)
                return;
//            Toast.makeText(CourseReleaseActivity.this, "加载图片：" + picpath, Toast.LENGTH_LONG).show();
            addNewPicture(picpath, (String)object);
        }
    };

    private void addNewPicture(String picPath, String picUrl){
//        Toast.makeText(CourseReleaseActivity.this, "加载网络图片：" + picUrl, Toast.LENGTH_LONG).show();
        if(TextUtils.isEmpty(picPath)||TextUtils.isEmpty(picUrl))
            return;
        PictureNode node = new PictureNode();
        node.setPath(picPath);
        node.setUrl(picUrl);
        node.setThumbImageUrl190(picUrl);
        node.setServer(true);
        picNodes.add(node);
        pictureAdapter.updateViewsData(picNodes);
//        pictureAdapter = new CoursePictureAdapter(this, picNodes);
//        gridView.setAdapter(pictureAdapter);
    }

    private class ItemNode {
        String ImageUrl;
        String ImageSize;
        String FileSize;

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String imageUrl) {
            ImageUrl = imageUrl;
        }

        public String getImageSize() {
            return ImageSize;
        }

        public void setImageSize(String imageSize) {
            ImageSize = imageSize;
        }

        public String getFileSize() {
            return FileSize;
        }

        public void setFileSize(String fileSize) {
            FileSize = fileSize;
        }
    }
}
