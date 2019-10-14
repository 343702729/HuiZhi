package com.huizhi.manage.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huizhi.manage.R;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.MessageListNode;
import com.huizhi.manage.request.message.MessageRequest;
import com.huizhi.manage.wiget.view.ItemMessageView;

import java.util.List;

public class NewMessageFragment extends Fragment {
    private View messageLayout;
    private Activity activity;
    private LinearLayout messagesLL;
    private int page = 1;
    private int limit = 20;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_new_message, container, false);
        activity = getActivity();
        initViews();
        getDatas();
        return messageLayout;
    }

    private void initViews(){
        messagesLL = messageLayout.findViewById(R.id.messages_ll);
    }

    private void getDatas(){
        MessageRequest request = new MessageRequest();
//        request.getMessageList(UserInfo.getInstance().getUser().getTeacherId(), String.valueOf(page), String.valueOf(limit), handler);
        request.getMessageList("D578E213-0657-4A7A-955B-7B657D5596C6", String.valueOf(page), String.valueOf(limit), handler);
    }

    private void addMessages(List<MessageListNode.MessageItemNode> items){
        if(items==null)
            return;
        for (MessageListNode.MessageItemNode item:items){
            messagesLL.addView(new ItemMessageView(activity, item));
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    MessageListNode node = (MessageListNode)msg.obj;
                    addMessages(node.getItemList());
                    break;
            }
        }
    };
}
