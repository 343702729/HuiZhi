package com.huizhi.manage.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.base.ThreadPoolDo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class AsyncBitmapLoader {
	 /** 
     * 内存图片软引用缓冲 
     */  
    private HashMap<String, SoftReference<Bitmap>> imageCache = null;  

      
    public AsyncBitmapLoader() {
        imageCache = new HashMap<String, SoftReference<Bitmap>>();  
    }

    public Bitmap loadBitmap(final ImageView imageView, final String imageURL, final ImageCallBack imageCallBack) throws Exception{
        if(TextUtils.isEmpty(imageURL))
            return null;
        Log.i("HuiZhi", "The image url:" + imageURL);
        //在内存缓存中，则返回Bitmap对象  
        if(imageCache.containsKey(imageURL)){
            SoftReference<Bitmap> reference = imageCache.get(imageURL);  
            Bitmap bitmap = reference.get();  
            if(bitmap != null){
                return bitmap;  
            }  
        }else{
            /** 
             * 加上一个对本地缓存的查找 
             */  
            String bitmapName = imageURL.substring(imageURL.lastIndexOf("/") + 1);  
            File cacheDir = new File(Constants.PATH_PIC);
            File[] cacheFiles = cacheDir.listFiles();  
            int i = 0;  
            if(null!=cacheFiles){
                for(; i<cacheFiles.length; i++){
                    if(bitmapName.equals(cacheFiles[i].getName())){
                        break;
                    }
                }

                if(i < cacheFiles.length){
                    return BitmapFactory.decodeFile(Constants.PATH_PIC + bitmapName);
                }
            }
        }

        final Handler handler = new Handler()  
        {  
            /* (non-Javadoc) 
             * @see android.os.Handler#handleMessage(android.os.Message) 
             */  
            @Override  
            public void handleMessage(Message msg)  
            {  
                // TODO Auto-generated method stub  
                imageCallBack.imageLoad(imageView, (Bitmap)msg.obj);  
            }  
        };  
          
        //如果不在内存缓存中，也不在本地（被jvm回收掉），则开启线程下载图片  
        ThreadPoolDo.getInstance().executeThread(new LoadPicThread(imageURL, handler, true));
        return null;
    }

    public Bitmap loadBitmapFromServer(final ImageView imageView, final String imageURL, final ImageCallBack imageCallBack) throws Exception{
        Log.i("HuiZhi", "The image url:" + imageURL);
        //在内存缓存中，则返回Bitmap对象
        if(imageCache.containsKey(imageURL)){
            SoftReference<Bitmap> reference = imageCache.get(imageURL);
            Bitmap bitmap = reference.get();
            if(bitmap != null){
                return bitmap;
            }
        }
        final Handler handler = new Handler(){
            /* (non-Javadoc)
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
            @Override
            public void handleMessage(Message msg){
                // TODO Auto-generated method stub
                imageCallBack.imageLoad(imageView, (Bitmap)msg.obj);
            }
        };
        ThreadPoolDo.getInstance().executeThread(new LoadPicThread(imageURL, handler, false));
        return null;
    }

    private class LoadPicThread extends Thread{
        private String imageURL;
        private Handler handler;
        private boolean isSave;

        public LoadPicThread(String imageURL,  Handler handler, boolean isSave){
            this.imageURL = imageURL;
            this.handler = handler;
            this.isSave = isSave;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            InputStream bitmapIs = HttpConnect.getStreamFromURL(imageURL);
            if(bitmapIs == null){
//                Log.i("Error", "img get null");
                return ;
            }
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(bitmapIs);
                imageCache.put(imageURL, new SoftReference<Bitmap>(bitmap));
//                bitmapIs.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            Message msg = handler.obtainMessage(0, bitmap);
            handler.sendMessage(msg);

            if(isSave)
                saveLocal(bitmap);

        }

        private void saveLocal(Bitmap bitmap){
            if(bitmap==null)
                return;
            File dir = new File(Constants.PATH_PIC);
            if(!dir.exists()){
                dir.mkdirs();
            }

            File bitmapFile = new File(Constants.PATH_PIC +
                    imageURL.substring(imageURL.lastIndexOf("/") + 1));
            if(!bitmapFile.exists()){
                try{
                    bitmapFile.createNewFile();
                }
                catch (IOException e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            FileOutputStream fos;
            try{
                fos = new FileOutputStream(bitmapFile);
                bitmap.compress(Bitmap.CompressFormat.PNG,
                        100, fos);
                fos.close();
            }catch (FileNotFoundException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }catch (IOException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public interface ImageCallBack {
        public void imageLoad(ImageView imageView, Bitmap bitmap);  
    }

    /**
     * @param  context   上下文对象
     * @param  url       网络图片地址
     * @param  singleImg  需要显示的imageview
     */
    public void showPicByVolleyRequest(Context context, String url, final ImageView singleImg){
        RequestQueue mQueue = Volley.newRequestQueue(context);
        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        // TODO Auto-generated method stub
                        singleImg.setImageBitmap(bitmap);
//                        singleImg.setBackground(new BitmapDrawable(bitmap));
                    }
                }, 1080, 720, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        });
        mQueue.add(imageRequest);
    }

    public void showFitPicByVolleyRequest(Context context, String url, final ImageView singleImg){
        try {
            RequestQueue mQueue = Volley.newRequestQueue(context);
            ImageRequest imageRequest = new ImageRequest(url,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            // TODO Auto-generated method stub
                            singleImg.setImageBitmap(bitmap);
                        }
                    }, 4800, 2400, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    e.printStackTrace();
                }
            });
            mQueue.add(imageRequest);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
