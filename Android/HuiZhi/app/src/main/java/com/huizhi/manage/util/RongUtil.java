package com.huizhi.manage.util;

import android.os.Handler;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;

/**
 * Created by CL on 2018/3/4.
 */

public class RongUtil {

    public static void unReadCountAddListener(final IUnReadMessageObserver mCountListener){
        final Conversation.ConversationType[] conversationTypes = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.DISCUSSION,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE};

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().addUnReadMessageCountChangedObserver(mCountListener, conversationTypes);
            }
        }, 500);

    }

    public static void unReadCountRemoveListener(final IUnReadMessageObserver mCountListener){
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(mCountListener);
    }
}
