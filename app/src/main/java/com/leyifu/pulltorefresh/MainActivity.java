package com.leyifu.pulltorefresh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.leyifu.pulltorefresh.view.MyPullToRefresh;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyPullToRefresh my_ptr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        my_ptr = ((MyPullToRefresh) findViewById(R.id.my_ptr));
        my_ptr.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,setDatas()));
    }

    private List<String> setDatas() {
        List<String> dataList = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            dataList.add("乐易付有限公司 "+i);
        }
        return dataList;
    }
}
