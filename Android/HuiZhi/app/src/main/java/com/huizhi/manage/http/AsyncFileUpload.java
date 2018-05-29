package com.huizhi.manage.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.huizhi.manage.base.BaseInfoUpdate;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by CL on 2018/1/22.
 */

public class AsyncFileUpload {
    private static AsyncFileUpload mAsyncFileUpload;
    private Configuration config;
    private UploadManager uploadManager;
    private String token;
    private String domain = "http://p2y642yty.bkt.clouddn.com";

    private AsyncFileUpload(){
        init();
    }

    public static AsyncFileUpload getInstance(){
        if(mAsyncFileUpload==null)
            mAsyncFileUpload = new AsyncFileUpload();
        return mAsyncFileUpload;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getFileUrl(String fileurl){
        if(TextUtils.isEmpty(fileurl))
            return null;
        if(fileurl.startsWith("http"))
            return fileurl;
        if(fileurl.startsWith("/"))
            return domain + fileurl;
        return domain + "/" + fileurl; //domain
    }

    /**
     * 初始化上传参数
     */
    private void init(){

        Recorder recorder = new Recorder(){
            @Override
            public void set(String s, byte[] bytes) {
            }
            @Override
            public byte[] get(String s) {
                return new byte[0];
            }
            @Override
            public void del(String s) {
            }
        };

//        String[] strs = {"http://up.qiniu.com"};
        //上传配置
//        FixedZone zone = new FixedZone(strs);
        config = new Configuration.Builder()
                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认 256K
                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认 512K
                .connectTimeout(10) // 链接超时。默认 10秒
                .responseTimeout(60) // 服务器响应超时。默认 60秒
                .recorder(recorder)  // recorder 分片上传时，已上传片记录器。默认 null
                .recorder(recorder, null)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 FixedZone.zone0
                .build();
        // 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
        uploadManager = new UploadManager(config);
    }

    /***
     * 表单上传
     */
    public void upload(final Context context, String filePath, final BaseInfoUpdate infoUpdate){
        if(TextUtils.isEmpty(token))
            return;
        // 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
        //data = <File对象、或 文件路径、或 字节数组>
        String data = filePath;    //FILE_SAVEPATH_UPLOAD路径下的 upload.jpg
        String key = createFileName(filePath);  //在七牛上显示的名字
        String name = filePath.substring(filePath.lastIndexOf("/") + 1);
//        String token = "";                                    //上传token
        uploadManager.put(data, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //  res 包含hash、key等信息，具体字段取决于上传策略的设置。
                        Log.i("HuiZhi", "The key: " + key + ",\r\n " + info + ",\r\n " + res);
                        Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
                        if(infoUpdate!=null){
                            infoUpdate.update(key);
                        }
                    }
                }, new UploadOptions(null, null, false, new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                        Log.i("HuiZhi", "The key:" + key + "  percent:" + percent);
                    }
                }, null));
    }

    private String createFileName(String filePath){
        String type = getFileType(filePath);
        return String.valueOf(new Date().getTime()) + "." + getFileType(filePath);
    }

    /***
     * 获取文件类型
     *
     * @param path 文件路径
     * @return 文件的格式
     */
    private String getFileType(String path) {
        String str = "";
        if (TextUtils.isEmpty(path)) {
            return str;
        }
        int i = path.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }
        str = path.substring(i + 1);
        return str;
    }
}
