package com.leyifu.pulltorefresh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.leyifu.pulltorefresh.R;

/**
 * Created by hahaha on 2017/4/11 0011.
 */
public class MyPullToRefresh extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = "MyPullToRefresh";
    private View head;
    private int downY;
    private int measuredHeight;
    private boolean isRecord;

    public MyPullToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    //初始化头脚
    private void init() {
        initHead();
        initFooter();
        setListener();
    }

    private void setListener() {
        setOnScrollListener(this);
    }

    private void initHead() {
        head = View.inflate(getContext(), R.layout.head, null);
        head.measure(0, 0);
        measuredHeight = head.getMeasuredHeight();
        head.setPadding(0, -measuredHeight, 0, 0);
        Log.e(TAG, "initHead: " + measuredHeight);
        addHeaderView(head);
    }

    private void initFooter() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0 && !isRecord) {
                    downY = (int) ev.getY();
                    isRecord = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY =  (int) ev.getY();
                int dY = moveY - downY;
                Log.e(TAG, "onTouchEvent: dY" + dY + " downY=" + downY + " moveY=" + moveY);
                int topY = (int) (-measuredHeight + dY / 3.0);
                if (isRecord && dY > 0) {
                    head.setPadding(0, topY, 0, 0);
//                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private int firstVisibleItem;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;

    }
}
