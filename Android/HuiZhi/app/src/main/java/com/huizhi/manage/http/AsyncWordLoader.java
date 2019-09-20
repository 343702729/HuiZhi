package com.huizhi.manage.http;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.base.ThreadPoolDo;
import com.huizhi.manage.data.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by CL on 2018/1/21.
 */

public class AsyncWordLoader {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    public String loadWord(Activity activity, String wordurl, BaseInfoUpdate infoUpdate){
        verifyStoragePermissions(activity);
        Log.i("HuiZhi", "The word url:" + wordurl);
        String wordName;
        if(!wordurl.contains("?"))
            wordName = wordurl.substring(wordurl.lastIndexOf("/") + 1);
        else
            wordName = wordurl.substring(wordurl.lastIndexOf("/") + 1, wordurl.indexOf("?"));
        File cacheDir = new File(Constants.PATH_WORD);
        File[] cacheFiles = cacheDir.listFiles();
        int i = 0;
        if(null!=cacheFiles){
            for(; i<cacheFiles.length; i++){
                if(wordName.equals(cacheFiles[i].getName())){
                    break;
                }
            }
            if(i < cacheFiles.length){
                return Constants.PATH_WORD + wordName;
            }
        }
        ThreadPoolDo.getInstance().executeThread(new LoadWordThread(wordurl, wordName, Constants.PATH_WORD, infoUpdate));
        return null;
    }

    public void downloadWord(String wordurl, BaseInfoUpdate infoUpdate){
        Log.i("HuiZhi", "Come into download word");
//        String wordName = wordurl.substring(wordurl.lastIndexOf("/") + 1);
//        String wordName = wordurl.substring(wordurl.lastIndexOf("/") + 1, wordurl.indexOf("?"));
        String wordName;
        if(!wordurl.contains("?"))
            wordName = wordurl.substring(wordurl.lastIndexOf("/") + 1);
        else
            wordName = wordurl.substring(wordurl.lastIndexOf("/") + 1, wordurl.indexOf("?"));
        Log.i("HuiZhi", "Come into download word wordName:" + wordName);
        File cacheDir = new File(Constants.PATH_WORD);
        File[] cacheFiles = cacheDir.listFiles();
        int i = 0;
        if(null!=cacheFiles){
            for(; i<cacheFiles.length; i++){
                if(wordName.equals(cacheFiles[i].getName())){
                    break;
                }
            }
            if(i < cacheFiles.length){
                String oldPath =  Constants.PATH_WORD + wordName;
                String newPath = Constants.PATH_DOWNLOAD + wordName;
                copyFile(oldPath, newPath);
                infoUpdate.update(newPath);
                return;
            }
        }
        ThreadPoolDo.getInstance().executeThread(new LoadWordThread(wordurl, wordName, Constants.PATH_DOWNLOAD, infoUpdate));
    }

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    private class LoadWordThread extends Thread{
        private String url;
        private String fileName;
        private BaseInfoUpdate infoUpdate;
        private String folderUrl;

        public LoadWordThread(String url, String fileName, String folderUrl, BaseInfoUpdate infoUpdate){
            this.url = url;
            this.fileName = fileName;
            this.infoUpdate = infoUpdate;
            this.folderUrl = folderUrl;
        }

        @Override
        public void run() {
            super.run();
            OutputStream os = null;
            InputStream is = null;
            try {
                is = HttpConnect.getStreamFromURL(url);
                File f = new File(folderUrl);
                if(!f.exists()){
                    f.mkdirs();
                }
                //创建文件
                File file = new File(folderUrl+fileName);
                //判断是否存在文件
                if(!file.exists()){
                    //创建新文件
                    file.createNewFile();
                }else{
                    file.delete();
                    file.createNewFile();
                }
                os = new FileOutputStream(file);
                byte buffer[] = new byte[1024];
                int len =0 ;
                while( (len = is.read(buffer))!= -1){
                    os.write(buffer,0,len);
                }
                os.flush();
                if(infoUpdate!=null)
                    infoUpdate.update(folderUrl+fileName);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try{
                    os.close();
                    is.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     * @param activity
     */
    public void verifyStoragePermissions(Activity activity) {
// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
