package com.huizhi.manage.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huizhi.manage.R;
import com.huizhi.manage.wiget.view.ItemMessageView;

public class NewMessageFragment extends Fragment {
    private View messageLayout;
    private Activity activity;
    private LinearLayout messagesLL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_new_message, container, false);
        activity = getActivity();
        initViews();
        return messageLayout;
    }

    private void initViews(){
        messagesLL = messageLayout.findViewById(R.id.messages_ll);
        addMessages();
    }

    private void addMessages(){
        messagesLL.addView(new ItemMessageView(activity, 1));
        messagesLL.addView(new ItemMessageView(activity, 1));
        messagesLL.addView(new ItemMessageView(activity, 1));
        messagesLL.addView(new ItemMessageView(activity, 1));
        messagesLL.addView(new ItemMessageView(activity, 1));
        messagesLL.addView(new ItemMessageView(activity, 1));
        messagesLL.addView(new ItemMessageView(activity, 1));
        messagesLL.addView(new ItemMessageView(activity, 1));
        messagesLL.addView(new ItemMessageView(activity, 1));
    }
}
