package com.huizhi.manage.wiget.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.node.TaskMouldNode;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.util.DipPxUtil;
import com.huizhi.manage.util.PictureUtil;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by CL on 2018/1/16.
 */

public class MouldStaffItemView extends LinearLayout {
    private Activity context;
    private boolean isInfo;

    public MouldStaffItemView(Activity context){
        super(context);
        this.context = context;
        initViews();
    }

    public MouldStaffItemView(Activity context, boolean isInfo){
        super(context);
        this.context = context;
        this.isInfo = isInfo;
        initViews();
    }

    public MouldStaffItemView(Activity context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_task_mould_staff, this);
    }

    public void setDateViews(String teacherid, List<TaskMouldNode> nodes){
        UserNode user = UserInfo.getInstance().getUserByTeacherId(teacherid);
        TextView nameTV = findViewById(R.id.tec_name_tv);
        ImageView personIV = findViewById(R.id.user_icon_iv);
        if(user!=null){
            nameTV.setText(user.getTeacherName());
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
        }
        if(nodes==null)
            return;
        LinearLayout itemsLL = findViewById(R.id.staff_items_ll);
        MouldTaskItemView itemView = null;
        for(TaskMouldNode node:nodes){
            itemView = new MouldTaskItemView(context, isInfo);
            itemView.setDates(node);
            itemsLL.addView(itemView);
            itemView = null;
        }
    }
}
