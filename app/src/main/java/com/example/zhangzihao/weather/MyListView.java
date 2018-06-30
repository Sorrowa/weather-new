package com.example.zhangzihao.weather;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by gao on 2017/12/9.
 */

public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
}
