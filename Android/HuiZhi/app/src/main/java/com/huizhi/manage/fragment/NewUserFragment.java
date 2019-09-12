package com.huizhi.manage.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huizhi.manage.R;

public class NewUserFragment extends Fragment {
    private View messageLayout;
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_new_user, container, false);
        activity = getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
