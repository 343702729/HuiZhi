package com.huizhi.manage.wiget.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class BannerViewFlow extends ViewFlow{
	private ViewPager mPager;
	
	public BannerViewFlow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BannerViewFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void setViewPager(ViewPager viewPager) {
        mPager = viewPager;
    }

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
//		if (mPager != null)
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                case MotionEvent.ACTION_MOVE:
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
            }
		return super.onInterceptTouchEvent(ev);
	}
}
