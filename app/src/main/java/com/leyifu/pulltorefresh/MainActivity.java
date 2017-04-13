package com.leyifu.pulltorefresh;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.leyifu.pulltorefresh.inter.OnRefreshListeren;
import com.leyifu.pulltorefresh.view.MyPullToRefresh;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PULL_TO_REFRESH = 1;
    public static final int LOAD_MORE_DATA = 2;
    private MyPullToRefresh my_ptr;
    private List<String> dataList;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PULL_TO_REFRESH:
                    String reslut = (String) msg.obj;
                    dataList.add(0,reslut);
                    adapter.notifyDataSetChanged();
                    my_ptr.finishLoad(true);
                    break;
                case LOAD_MORE_DATA:
                    String moreDatas = (String) msg.obj;
                    dataList.add(moreDatas);
                    adapter.notifyDataSetChanged();
                    my_ptr.finishLoad(false);
                    break;
            }
        }
    };
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        my_ptr = ((MyPullToRefresh) findViewById(R.id.my_ptr));

        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, setDatas());
        my_ptr.setAdapter(adapter);
        my_ptr.setOnRefreshListeren(new OnRefreshListeren() {
            @Override
            public void onRefresh() {
                getNetDates();
            }

            @Override
            public void onLoadMore() {
                getMoreDatas();
            }
        });
    }



    private List<String> setDatas() {
        dataList = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            dataList.add("乐易付有限公司 "+i);
        }
        return dataList;
    }

    public void getNetDates() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = "我已获取到数据";
                Message message = Message.obtain();
                message.what=PULL_TO_REFRESH;
                message.obj=result;
                handler.sendMessage(message);
            }
        }).start();

    }
    private void getMoreDatas() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String moreDatas = "我加载到了数据";
                Message message = Message.obtain();
                message.what=LOAD_MORE_DATA;
                message.obj=moreDatas;
                handler.sendMessage(message);
            }
        }).start();
    }
}
