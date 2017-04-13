package com.leyifu.pulltorefresh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.leyifu.pulltorefresh.R;
import com.leyifu.pulltorefresh.inter.OnRefreshListeren;

import java.text.SimpleDateFormat;

/**
 * Created by hahaha on 2017/4/11 0011.
 */
public class MyPullToRefresh extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = "MyPullToRefresh";
    private View head;
    private int downY;
    private int measuredHeight;
    private boolean isRecord;
    private ImageView iv_arrow;
    private ProgressBar pg_bar;
    private TextView tv_state;
    private TextView tv_time;
    private int topY;
    private ProgressBar pg_bar_footer;
    private TextView tv_footer_state;
    private View footer;
    private int footerHeight;

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
        iv_arrow = ((ImageView) head.findViewById(R.id.iv_arrow));
        pg_bar = ((ProgressBar) head.findViewById(R.id.pg_bar));
        tv_state = ((TextView) head.findViewById(R.id.tv_state));
        tv_time = (TextView) head.findViewById(R.id.tv_time);
        head.measure(0, 0);
        measuredHeight = head.getMeasuredHeight();
        head.setPadding(0, -measuredHeight, 0, 0);
        Log.e(TAG, "initHead: " + measuredHeight);
        addHeaderView(head);

        finishLoad(true);
    }


    private void initFooter() {
        footer = View.inflate(getContext(), R.layout.footer, null);
        pg_bar_footer = ((ProgressBar) footer.findViewById(R.id.pg_bar_footer));
        tv_footer_state = ((TextView) footer.findViewById(R.id.tv_footer_state));
        footer.measure(0, 0);
        footerHeight = footer.getMeasuredHeight();
        Log.e(TAG, "initHead: " + footerHeight);
        footer.setPadding(0, -footerHeight, 0, 0);
        addFooterView(footer);
    }

    private PullState state;

    private enum PullState {
        PULL_TO_REFRESH, REFRESH_REFRESH, REFRESHING
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
                if (firstVisibleItem == 0 && !isRecord) {
                    downY = (int) ev.getY();
                    isRecord = true;
                }
                int moveY = (int) ev.getY();
                int dY = moveY - downY;
                Log.e(TAG, "onTouchEvent: dY" + dY + " downY=" + downY + " moveY=" + moveY);
                topY = (int) (-measuredHeight + dY / 3.0);
                if (isRecord && dY > 0) {
                    head.setPadding(0, topY, 0, 0);
                    if (topY > 0 && state != PullState.REFRESH_REFRESH) {
                        state = PullState.REFRESH_REFRESH;
//                        iv_arrow.setVisibility(INVISIBLE);
//                        pg_bar.setVisibility(VISIBLE);
                        tv_state.setText("释放刷新");
                    } else if (topY < 0 && state != PullState.PULL_TO_REFRESH) {
                        state = PullState.PULL_TO_REFRESH;
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                isRecord = false;
                if (state == PullState.REFRESH_REFRESH) {
                    state = PullState.REFRESHING;
                    head.setPadding(0, 0, 0, 0);
                    iv_arrow.setVisibility(INVISIBLE);
                    pg_bar.setVisibility(VISIBLE);
                    tv_state.setText("正在刷新");
                    if (listeren != null) {
                        listeren.onRefresh();
                    }
                } else if (state == PullState.PULL_TO_REFRESH) {
                    head.setPadding(0, -measuredHeight, 0, 0);
                    tv_state.setText("下拉刷新");
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private boolean isButton;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            Log.e(TAG, "onScrollStateChanged: SCROLL_STATE_IDLE 停止");
        }
        if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            Log.e(TAG, "onScrollStateChanged: SCROLL_STATE_TOUCH_SCROLL 动");
        }
        if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
            Log.e(TAG, "onScrollStateChanged: SCROLL_STATE_FLING 猛动");
        }
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            if (getLastVisiblePosition() == getCount() - 1) {
                if (!isButton) {
                    isButton=true;
                    footer.setPadding(0, 0, 0, 0);
                    setSelection(getCount());
                    if (listeren!=null){
                        listeren.onLoadMore();
                    }
                }
            }
        }
    }

    private int firstVisibleItem;
    private OnRefreshListeren listeren;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;

    }

    public void setOnRefreshListeren(OnRefreshListeren listeren) {
        this.listeren = listeren;
    }

    public void finishLoad(boolean isPullToRefresh) {
        if (isPullToRefresh) {
            iv_arrow.setVisibility(VISIBLE);
            pg_bar.setVisibility(INVISIBLE);
            head.setPadding(0, -measuredHeight, 0, 0);
            tv_state.setText("下拉刷新");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String time = simpleDateFormat.format(System.currentTimeMillis());
            tv_time.setText("最后刷新时间" + time);
            //初始状态为下拉刷新
            state = PullState.PULL_TO_REFRESH;
        } else {
            footer.setPadding(0,-footerHeight,0,0);
            isButton=false;
        }
    }
}
