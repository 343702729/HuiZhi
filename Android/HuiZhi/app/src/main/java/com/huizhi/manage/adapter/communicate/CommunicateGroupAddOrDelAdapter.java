package com.huizhi.manage.adapter.communicate;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.huizhi.manage.R;
import com.huizhi.manage.base.BitmapCache;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.util.PictureUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/2/8.
 */

public class CommunicateGroupAddOrDelAdapter extends BaseAdapter {
    private Context context;
    private List<UserNode> nodes;
    private List<String> selIds;
    private LayoutInflater layoutInflater;
//    private AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
    private List<String> personSelList = new ArrayList<>();
    private ImageLoader mImageLoader;

    public CommunicateGroupAddOrDelAdapter(Context context, List<UserNode> nodes, List<String> selIds){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        this.selIds = selIds;
        layoutInflater = LayoutInflater.from(context);

        RequestQueue queue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(queue, new BitmapCache());
    }

    public void updateViewsData(List<UserNode> nodes){
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        notifyDataSetChanged();
    }

    public List<String> getPersonSelList(){
        return personSelList;
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
        final UserNode userNode = nodes.get(i);
        if(view==null){
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.adapter_communicate_group_sel, null);
            viewHolder.selLL = view.findViewById(R.id.person_ll);
            viewHolder.selIV = view.findViewById(R.id.person_ck_iv);
            viewHolder.selCB = view.findViewById(R.id.person_cb);
            viewHolder.iconIV = view.findViewById(R.id.person_iv);
            viewHolder.nameTV = view.findViewById(R.id.person_tv);
            viewHolder.iconIV.setTag(userNode.getHeadImgUrl());
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
        }
        if(personSelList.contains(userNode.getTeacherId()))
            viewHolder.selIV.setBackgroundResource(R.mipmap.check_fc);
        else
            viewHolder.selIV.setBackgroundResource(R.mipmap.check_bg);
        if(selIds!=null&&selIds.contains(userNode.getTeacherId())){

            viewHolder.selIV.setBackgroundResource(R.mipmap.check_fc_sel);
            viewHolder.selLL.setOnClickListener(null);
        }else {
            viewHolder.selLL.setOnClickListener(new PersonItemClick(userNode, viewHolder.selIV));
        }

        viewHolder.nameTV.setText(userNode.getTeacherName());
//        viewHolder.selCB.setOnCheckedChangeListener(new PersonItemSelChange(userNode));
//        viewHolder.iconIV.setBackgroundResource(R.mipmap.user_icon);

        viewHolder.iconIV.setDefaultImageResId(R.mipmap.user_icon);
        viewHolder.iconIV.setErrorImageResId(R.mipmap.user_icon);
        viewHolder.iconIV.setImageUrl(AsyncFileUpload.getInstance().getFileUrl(userNode.getHeadImgUrl()), mImageLoader);


        /**
        try {
//            asyncBitmapLoader.showPicByVolleyRequest(context, AsyncFileUpload.getInstance().getFileUrl(userNode.getHeadImgUrl()), viewHolder.iconIV);

            Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(viewHolder.iconIV, AsyncFileUpload.getInstance().getFileUrl(userNode.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
                @Override
                public void imageLoad(ImageView imageView, Bitmap bitmap) {
                    if(imageView.getTag()!=null&&imageView.getTag().equals(userNode.getHeadImgUrl()))
                        imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                }
            });
            if(bitmap!=null){
                if(viewHolder.iconIV.getTag()!=null&&viewHolder.iconIV.getTag().equals(userNode.getHeadImgUrl()))
                    viewHolder.iconIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
         */
        return view;
    }

    private class ViewHolder {
        LinearLayout selLL;
        ImageView selIV;
        CheckBox selCB;
        NetworkImageView iconIV;
        TextView nameTV;
    }

    private class PersonItemClick implements View.OnClickListener{
        private UserNode user;
        private ImageView checkIV;

        public PersonItemClick(UserNode user, ImageView checkIV){
            this.user = user;
            this.checkIV = checkIV;
        }

        @Override
        public void onClick(View view) {
            if(personSelList.contains(user.getTeacherId())){
                personSelList.remove(user.getTeacherId());
                checkIV.setBackgroundResource(R.mipmap.check_bg);
            }else{
                personSelList.add(user.getTeacherId());
                checkIV.setBackgroundResource(R.mipmap.check_fc);
            }
        }
    }

    private class PersonItemSelChange implements CompoundButton.OnCheckedChangeListener{
        private UserNode user;

        public PersonItemSelChange(UserNode user){
            this.user = user;
        }
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean flage) {
            Log.i("HuiZhi", "The name:" + user.getTeacherName() + "  ischecked:" + flage);
            if(flage){
                personSelList.add(user.getTeacherId());
            }else {
                personSelList.remove(user.getTeacherId());
            }
        }
    }


}
