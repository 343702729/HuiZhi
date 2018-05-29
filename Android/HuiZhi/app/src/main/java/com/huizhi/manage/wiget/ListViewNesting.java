package com.huizhi.manage.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by CL on 2018/2/10.
 */

public class ListViewNesting extends ListView {

    public ListViewNesting(Context context) {
        super(context);
    }

    public  ListViewNesting(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public  ListViewNesting(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
