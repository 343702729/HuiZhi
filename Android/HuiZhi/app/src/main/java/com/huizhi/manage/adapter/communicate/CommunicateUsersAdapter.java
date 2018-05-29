package com.huizhi.manage.adapter.communicate;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.huizhi.manage.R;
import com.huizhi.manage.adapter.task.LogAdapter;
import com.huizhi.manage.base.BitmapCache;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.CommunicateUserNode;
import com.huizhi.manage.node.LogNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.util.PictureUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2017/12/28.
 */

public class CommunicateUsersAdapter extends BaseAdapter {
    private Context context;
    private List<UserNode> nodes;
    private LayoutInflater layoutInflater;
//    private AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
    private ImageLoader mImageLoader;

    public CommunicateUsersAdapter(Context context, List<UserNode> nodes){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        layoutInflater = LayoutInflater.from(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(queue, new BitmapCache());
    }

    public void updateUsers(List<UserNode> nodes){
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        notifyDataSetChanged();
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
        ViewItem taskItem;
        final UserNode userNode = nodes.get(i);
        if(view==null){
            taskItem = new ViewItem();
            view = layoutInflater.inflate(R.layout.adapter_communicate_users, null);
            taskItem.headIV = view.findViewById(R.id.head_iv);
            taskItem.nameTV = view.findViewById(R.id.name_tv);
            taskItem.roleTV = view.findViewById(R.id.role_tv);
            taskItem.headIV.setTag(userNode.getTeacherId());
            view.setTag(taskItem);
        }else{
            taskItem = (ViewItem)view.getTag();
        }
        LinearLayout titleLL = view.findViewById(R.id.title_ll);
        LinearLayout userLL = view.findViewById(R.id.user_ll);
        if(userNode.getType()==1){
            userLL.setVisibility(View.GONE);
            titleLL.setVisibility(View.VISIBLE);
            TextView titleTV = view.findViewById(R.id.title_tv);
            titleTV.setText(userNode.getTeacherName());
        }else {
            titleLL.setVisibility(View.GONE);
            userLL.setVisibility(View.VISIBLE);
            taskItem.nameTV.setText(userNode.getTeacherName());
            taskItem.roleTV.setText(userNode.getRoleTypeName());
//        taskItem.headIV.setBackgroundResource(R.mipmap.user_icon);

            taskItem.headIV.setDefaultImageResId(R.mipmap.user_icon);
            taskItem.headIV.setErrorImageResId(R.mipmap.user_icon);
            taskItem.headIV.setImageUrl(AsyncFileUpload.getInstance().getFileUrl(userNode.getHeadImgUrl()), mImageLoader);
        }


        return view;
    }

    private class ViewItem{
        NetworkImageView headIV;
        TextView nameTV;
        TextView roleTV;
    }

}
