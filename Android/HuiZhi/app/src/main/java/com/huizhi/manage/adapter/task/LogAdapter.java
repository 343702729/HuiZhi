package com.huizhi.manage.adapter.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.TaskAssignNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.util.PictureUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2017/12/16.
 */

public class LogAdapter extends BaseAdapter {
    private Context context;
    private List<TaskAssignNode> nodes;
    private LayoutInflater layoutInflater;
    private AsyncBitmapLoader asyncBitmapLoader;

    public LogAdapter(Context context, List<TaskAssignNode> nodes){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        layoutInflater = LayoutInflater.from(context);
        asyncBitmapLoader = new AsyncBitmapLoader();
    }

    @Override
    public int getCount() {
        return nodes.size();
    }

    @Override
    public Object getItem(int i) {
        return nodes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        TaskAssignNode assignNode = nodes.get(i);
        if(view==null){
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.adapter_task_log, null);
            viewHolder.portraitIV = view.findViewById(R.id.user_icon_iv);
            viewHolder.descriptionTV = view.findViewById(R.id.description_tv);
            viewHolder.timeTV = view.findViewById(R.id.createtime_tv);
            viewHolder.remarksTV = view.findViewById(R.id.remarks_tv);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.descriptionTV.setText(assignNode.getMessage());
        viewHolder.timeTV.setText(assignNode.getStrCreateTime());
        viewHolder.remarksTV.setText(assignNode.getAssignReason());
        try {
            viewHolder.portraitIV.setImageBitmap(null);
            viewHolder.portraitIV.setBackgroundResource(R.mipmap.user_icon);
//            asyncBitmapLoader.showPicByVolleyRequest(context, AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl()), viewHolder.portraitIV);
            Bitmap bitmap=asyncBitmapLoader.loadBitmapFromServer(viewHolder.portraitIV, AsyncFileUpload.getInstance().getFileUrl(assignNode.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {

                @Override
                public void imageLoad(ImageView imageView, Bitmap bitmap) {
                    // TODO Auto-generated method stub
                    imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                }
            });
            if(bitmap!=null)
                viewHolder.portraitIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    private class ViewHolder {
        public ImageView portraitIV;
        public TextView descriptionTV;
        public TextView timeTV;
        public TextView remarksTV;
    }
}
