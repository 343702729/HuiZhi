package com.huizhi.manage.activity.user;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.task.TaskFinishedActivity;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.PictureSelDialog;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.request.main.MainRequest;
import com.huizhi.manage.request.user.UserPostRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.FileUtil;
import com.huizhi.manage.util.PictureUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

/**
 * Created by CL on 2018/1/30.
 * 修改头像
 */

public class UserHeadPortraitEdit extends Activity {
    private Uri imageFileUri;
    private ImageView headPorIV;
    private AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_headportrait_edit);
        initViews();
        setHeadPortrait();
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        Button editBtn = findViewById(R.id.edit_btn);
        editBtn.setOnClickListener(editBtnClick);

        headPorIV = findViewById(R.id.head_portrait_iv);
    }

    /**
     * 用户头像加载
     */
    private void setHeadPortrait(){
        String headImg = AsyncFileUpload.getInstance().getFileUrl(UserInfo.getInstance().getUser().getHeadImgUrl());
        try {
            Bitmap bitmap = asyncBitmapLoader.loadBitmap(headPorIV, headImg, new AsyncBitmapLoader.ImageCallBack() {
                @Override
                public void imageLoad(ImageView imageView, Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                }
            });
            if(bitmap!=null){
                headPorIV.setImageBitmap(bitmap);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private View.OnClickListener editBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PictureSelDialog pictureSelDialog = new PictureSelDialog(UserHeadPortraitEdit.this, pictureSelInfo);
            pictureSelDialog.showView(view);
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

                //拍照
//                String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ Constants.TAKE_PICTURE_PATH;
//                File temp = new File(imageFilePath);
//                if(temp.exists())
//                    temp.delete();
//                Uri imageFileUri = Uri.fromFile(temp);//获取文件的Uri
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//跳转到相机Activity
//                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);//告诉相机拍摄完毕输出图片到指定的Uri
//                startActivityForResult(intent, Constants.TAKE_PICTURE);
            }else if(index==2){
                //相册选择
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, Constants.SELECT_PICTURE);
            }

        }
    };

    //图片对应Uri
    private Uri photoUri;
    //图片文件路径
    private String picPath, savePath;
    /**
     * 拍照获取图片
     */
    private void takePictures() {
        //执行拍照前，应该先判断SD卡是否存在
//        String SDState = Environment.getExternalStorageState();
//        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            ContentValues values = new ContentValues();
//            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//            startActivityForResult(intent, Constants.TAKE_PICTURE);
//        } else {
//            Toast.makeText(this, "手机未插入内存卡", Toast.LENGTH_LONG).show();
//        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, Constants.TAKE_PICTURE);

//                String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ Constants.TAKE_PICTURE_PATH;
//                File temp = new File(imageFilePath);
//                if(temp.exists())
//                    temp.delete();
//                Uri imageFileUri = Uri.fromFile(temp);//获取文件的Uri
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//跳转到相机Activity
//                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);//告诉相机拍摄完毕输出图片到指定的Uri
//                startActivityForResult(intent, Constants.TAKE_PICTURE);
    }

    /**
     * 裁剪图片方法实现
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Constants.CROP_SMALL_PICTURE);
    }

    /**
     * 图片裁剪，参数根据自己需要设置
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Log.i("Bar", "Come into startPhotoZoom");
        int dp = 500;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);//输出是X方向的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 400);//输出X方向的像素
        intent.putExtra("outputY", 400);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);//设置为不返回数据
        startActivityForResult(intent, Constants.CROP_SMALL_PICTURE);
    }

    /**
     * 7.0以上版本图片裁剪操作
     *
     * @param imagePath
     */
    private void cropForN(String imagePath) {
        Log.i("Bar", "Come into cropForN");
        Uri cropUri = getImageContentUri(new File(imagePath));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(cropUri, "image/*");
        intent.putExtra("crop", "true");
        //输出是X方向的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, Constants.CROP_SMALL_PICTURE);
    }

    private Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.TAKE_PICTURE){
//            String photoPath = Environment.getExternalStorageDirectory().getAbsolutePath()+Constants.TAKE_PICTURE_PATH;
//            Toast.makeText(this, "The take pic path:" + photoPath, Toast.LENGTH_SHORT).show();
//            cutImage(Uri.fromFile(new File(photoPath)));//Uri.fromFile(new File(picPath))
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
            Log.i("Bar", "The take picPath:" + picPath);
            if (picPath != null ) {//&& (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))
                photoUri = Uri.fromFile(new File(picPath));
                if (Build.VERSION.SDK_INT > 23) {
//                    photoUri = FileProvider.getUriForFile(this, "com.innopro.bamboo.fileprovider", new File(picPath));
                    cropForN(picPath);
                } else {
                    startPhotoZoom(photoUri);
                }
            } else {
                //错误提示
            }
        }else if(requestCode == Constants.SELECT_PICTURE){
            try {
                Uri originalUri = data.getData(); // 获得图片的uri
                if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){
                    String picP = FileUtil.getPath(this, originalUri);
                    Log.i("Task", "The picture path is:" + picP);
                    Bitmap bitmap = BitmapFactory.decodeFile(picP);
                    picPath = Constants.PATH_PIC + String.valueOf(new Date().getTime()) + ".png";
                    Log.i("HuiZhi", "The pic path:" + picPath);
                    PictureUtil.saveBitmapToFile(bitmap, picPath);
//                    Toast.makeText(this, "The select pic path:" + picPath, Toast.LENGTH_SHORT).show();
//                    cutImage(Uri.fromFile(new File(picPath)));
                    cropForN(picPath);
                }else{
                    Toast.makeText(this, "The select pic path:123" , Toast.LENGTH_SHORT).show();
                    cutImage(originalUri);
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }else if(resultCode == Activity.RESULT_OK&&requestCode == Constants.CROP_SMALL_PICTURE){
            Log.i("HuiZhi", "Come into CROP_SMALL_PICTURE");
            if (data != null) {
                Log.i("HuiZhi", "Come into crop picture");
                setImageToView();
            }
        }
    }

    private void setImageToView(){
            try{
                Bitmap bitmap = BitmapFactory.decodeFile(picPath);
//                Bitmap bitmap= BitmapFactory.decodeStream(new FileInputStream(picPath));
//                Bitmap bitmap = extras.getParcelable("data");
                //这里图片是方形的，可以用一个工具类处理成圆形（很多头像都是圆形，这种工具类网上很多不再详述）
                headPorIV.setImageBitmap(bitmap);//显示图片
                savePath = Constants.PATH_PIC + "." + String.valueOf(new Date().getTime()) + ".png";
                Log.i("HuiZhi", "The pic path:" + savePath);
                PictureUtil.saveBitmapToFile(bitmap, savePath);
                AsyncFileUpload.getInstance().upload(this, savePath, picInfoUpdate);
            }catch (Exception e){
                e.printStackTrace();
            }

    }

    private BaseInfoUpdate picInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            String key = (String)object;
            Log.i("HuiZhi", "Come into the crop pic key:" + key);
            UserInfo.getInstance().getUser().setHeadImgUrl(key);
            UserPostRequest userPostRequest = new UserPostRequest();
            userPostRequest.postUserHeadPortrait(UserInfo.getInstance().getUser().getTeacherId(), key, handler);
            Log.i("HuiZhi", "Come into pic path:" + savePath);
            File file = new File(savePath);
            if(file.exists()){
                Log.i("HuiZhi", "Come into delete file");
                file.delete();
            }
            File pfile = new File(picPath);
            if(pfile.exists())
                pfile.delete();
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    getUsersInfo();
                    break;
            }
        }
    };

    private void getUsersInfo(){
        MainRequest mainRequest = new MainRequest();
        mainRequest.getTeamUsers(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), null);
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
