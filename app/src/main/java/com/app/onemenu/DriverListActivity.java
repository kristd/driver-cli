package com.app.onemenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DriverListActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private View progressView;
    private DriverListRecyclerViewAdapter adapter;
    private List<DriverItem> driverList = new ArrayList<>();
    private Context context = this;
    private boolean isSwithActive = true;
    private String orderId = "";

    private static String TAG = "DriverListActivity";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MessageId.GET_DRIVER_LIST_COMPLETED) {
                recyclerView.getAdapter().notifyDataSetChanged();

                swipeRefreshLayout.setVisibility(View.VISIBLE);
                Toast.makeText(context, "get driver list success", Toast.LENGTH_SHORT).show();
            } else if (msg.what == MessageId.GET_DRIVER_LIST_FAILED) {
                Toast.makeText(context, "get driver list failed", Toast.LENGTH_SHORT).show();
            }

            showProgress(false);
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private Handler closeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            if (msg.what == MessageId.CLOSE_DRIVER_LIST) {
//                finish();
//            }

            finish();
        }
    };

    private Handler showProgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            swipeRefreshLayout.setVisibility(View.GONE);
            showProgress(true);
        }
    };

    private Handler hideProgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            showProgress(false);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    };

    Callback getDriverListCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();

            Message msg = new Message();
            msg.what =  MessageId.GET_DRIVER_LIST_FAILED;
            handler.sendMessage(msg);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String data = response.body().string();
            Log.i(TAG, data);

            JSONObject jsonObject = (JSONObject) JSON.parse(data);
            if (jsonObject!= null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                initDriverData(jsonObject);

                Message msg = new Message();
                msg.what =  MessageId.GET_DRIVER_LIST_COMPLETED;
                handler.sendMessage(msg);
            } else {
                Message msg = new Message();
                msg.what =  MessageId.GET_DRIVER_LIST_FAILED;
                handler.sendMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list);

        Intent intent = getIntent();
        isSwithActive = intent.getBooleanExtra("isSwithActive", true);
        orderId = intent.getStringExtra("orderId");

        progressView = findViewById(R.id.driverlist_progress);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_driverlist);
        swipeRefreshLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.GET_DRIVER_LIST, getDriverListCallback, null);
            }
        });

        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_driverlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DriverListRecyclerViewAdapter(driverList, this, isSwithActive, orderId, closeHandler, showProgHandler, hideProgHandler);
        recyclerView.setAdapter(adapter);

        HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.GET_DRIVER_LIST, getDriverListCallback, null);
        showProgress(true);
    }

    private void showProgress(final boolean show) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                progressView.animate().setDuration(shortAnimTime)
                        .alpha(show ? 1 : 0)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                            }
                        });
            } else {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initDriverData(JSONObject jsonObject) {
        driverList.clear();
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("driver_list");

        for(int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            DriverItem item = new DriverItem(
                    "",
                    object.getString("id"),
                    object.getString("name"),
                    object.getString("phone_num"),
                    "",
                    "",
                    object.getIntValue("work_status"));

            driverList.add(item);
        }
    }
}
