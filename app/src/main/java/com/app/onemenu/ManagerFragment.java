package com.app.onemenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class ManagerFragment extends Fragment {
    private TextView mTextViewMgrOrderList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ManagerRecyclerViewAdapter adapter;

    private List<OrderItem> orderList = new ArrayList<>();

    private LinearLayoutManager layoutManager;
    private View progressView;
    private Toolbar toolbar;

    private static String TAG = "ManagerFragment";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            if (msg.what == MessageId.GET_DRIVER_LIST_COMPLETED) {
//                adapter = new ManagerRecyclerViewAdapter(orderList, driverList, getActivity());
//                recyclerView.setAdapter(adapter);
//
//                swipeRefreshLayout.setVisibility(View.VISIBLE);
//
//            } else if (msg.what == MessageId.GET_DRIVER_LIST_FAILED) {
//                Toast.makeText(getActivity(), "get driver list failed", Toast.LENGTH_SHORT).show();
//
//            } else if (msg.what == MessageId.GET_ORDER_BY_MGR_COMPLETED) {
//                adapter = new ManagerRecyclerViewAdapter(orderList, driverList, getActivity());
//                recyclerView.setAdapter(adapter);
//
//                swipeRefreshLayout.setVisibility(View.VISIBLE);
//
//            } else if (msg.what == MessageId.GET_ORDER_BY_MGR_FAILED) {
//                Toast.makeText(getActivity(), "get order list failed", Toast.LENGTH_SHORT).show();
//            }

            if (msg.what == MessageId.GET_ORDER_BY_MGR_COMPLETED) {
                recyclerView.getAdapter().notifyDataSetChanged();
                swipeRefreshLayout.setVisibility(View.VISIBLE);

                //Toast.makeText(getActivity(), "get order list success", Toast.LENGTH_SHORT).show();
            } else if (msg.what == MessageId.GET_ORDER_BY_MGR_FAILED) {
                //Toast.makeText(getActivity(), "get order list failed", Toast.LENGTH_SHORT).show();
            }

            showProgress(false);
            swipeRefreshLayout.setRefreshing(false);
        }
    };

//    Callback getDriverListCallback = new Callback() {
//        @Override
//        public void onFailure(Call call, IOException e) {
//            //Log.e(TAG, e.getMessage());
//            e.printStackTrace();
//
//            Message msg = new Message();
//            msg.what =  MessageId.GET_DRIVER_LIST_FAILED;
//            handler.sendMessage(msg);
//        }
//
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            String data = response.body().string();
//            Log.i(TAG, data);
//
//            JSONObject jsonObject = (JSONObject)JSON.parse(data);
//            if (jsonObject!= null && jsonObject.getString("status").equals(ErrorCode.Success)) {
//                initDriverData(jsonObject);
//
//                Message msg = new Message();
//                msg.what =  MessageId.GET_DRIVER_LIST_COMPLETED;
//                handler.sendMessage(msg);
//            } else {
//                Message msg = new Message();
//                msg.what =  MessageId.GET_DRIVER_LIST_FAILED;
//                handler.sendMessage(msg);
//            }
//        }
//    };

    Callback getOrderListCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();

            Message msg = new Message();
            msg.what = MessageId.GET_ORDER_BY_MGR_FAILED;
            handler.sendMessage(msg);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String data = response.body().string();
            Log.i(TAG, data);

            JSONObject jsonObject = (JSONObject)JSON.parse(data);
            if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                initOrderData(jsonObject);

                Message msg = new Message();
                msg.what =  MessageId.GET_ORDER_BY_MGR_COMPLETED;
                handler.sendMessage(msg);
            } else {
                Message msg = new Message();
                msg.what =  MessageId.GET_ORDER_BY_MGR_FAILED;
                handler.sendMessage(msg);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressView = getActivity().findViewById(R.id.manager_get_driver_progress);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_manager);
        toolbar.inflateMenu(R.menu.menu_manager);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                switch (itemId) {
                    case R.id.action_orders:
                        //Get Mgr Order List
                        mTextViewMgrOrderList.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        showProgress(true);

                        HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.GET_ORDER_LIST_MGR, getOrderListCallback, null);

                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {
                                    HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.GET_ORDER_LIST_MGR, getOrderListCallback, null);
                                }
                            });
                        }
                        break;
                    case R.id.action_drivers:
                        //Get Driver List
//                        mTextViewDriverList.setVisibility(View.GONE);
//                        showProgress(true);
//
//                        HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.GET_DRIVER_LIST, getDriverListCallback, null);
//
//                        if (swipeRefreshLayout != null) {
//                            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                                @Override
//                                public void onRefresh() {
//                                    HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.GET_DRIVER_LIST, getDriverListCallback, null);
//                                }
//                            });
//                        }

                        Intent intent = new Intent(getActivity(), DriverListActivity.class);
                        intent.putExtra("isSwithActive", true);
                        intent.putExtra("orderId", "");
                        startActivity(intent);
                        break;
                    default:
                        break;
                }

                return true;
            }
        });

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView_manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ManagerRecyclerViewAdapter(orderList, getActivity());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_manager);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);

        mTextViewMgrOrderList = (TextView) getActivity().findViewById(R.id.tv_driverList);
        mTextViewMgrOrderList.setVisibility(View.GONE);
        mTextViewMgrOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewMgrOrderList.setVisibility(View.GONE);
                showProgress(true);
                swipeRefreshLayout.setVisibility(View.VISIBLE);

                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.GET_ORDER_LIST_MGR, getOrderListCallback, null);
                    }
                });

                HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.GET_ORDER_LIST_MGR, getOrderListCallback, null);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        swipeRefreshLayout.setVisibility(View.GONE);
        HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.GET_ORDER_LIST_MGR, getOrderListCallback, null);
        showProgress(true);
    }

    public static ManagerFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        ManagerFragment fragment = new ManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    private void initDriverData(JSONObject jsonObject) {
//        orderList.clear();
//        driverList.clear();
//        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("driver_list");
//
//        for(int i = 0; i < jsonArray.size(); i++) {
//            JSONObject object = jsonArray.getJSONObject(i);
//
//            DriverItem item = new DriverItem(
//                    "",
//                    object.getString("id"),
//                    object.getString("name"),
//                    object.getString("phone_num"),
//                    "",
//                    "",
//                    object.getIntValue("work_status"));
//
//            driverList.add(item);
//        }
//    }

    private void initOrderData(JSONObject jsonObject) {
        orderList.clear();
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("order_list");

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            JSONObject driverObj = object.getJSONObject("driver");
            DriverItem driver = null;
            if (driverObj != null) {
                driver = new DriverItem(
                    "",
                    object.getJSONObject("driver").getString("id"),
                    object.getJSONObject("driver").getString("name"),
                    object.getJSONObject("driver").getString("phone_num"),
                    object.getJSONObject("driver").getString("latitude"),
                    object.getJSONObject("driver").getString("longitude"),
                    1
                );
            }

            MyLocation cLocation = new MyLocation(
                object.getJSONObject("destination").getString("id"),
                object.getJSONObject("destination").getString("address"),
                object.getJSONObject("destination").getString("latitude"),
                object.getJSONObject("destination").getString("longitude")
            );

            MyLocation rLocation = new MyLocation(
                object.getJSONObject("restaurant").getJSONObject("location").getString("id"),
                object.getJSONObject("restaurant").getJSONObject("location").getString("address"),
                object.getJSONObject("restaurant").getJSONObject("location").getString("latitude"),
                object.getJSONObject("restaurant").getJSONObject("location").getString("longitude")
            );

            Restaurant r = new Restaurant(
                object.getJSONObject("restaurant").getString("id"),
                rLocation,
                object.getJSONObject("restaurant").getString("name"),
                object.getJSONObject("restaurant").getString("phone_num")
            );

            OrderItem orderItem = new OrderItem(
                    object.getIntValue("already_in_time"),
                    object.getLongValue("create_time"),
                    null,
                    cLocation,
                    object.getString("id"),
                    object.getString("last5code"),
                    object.getString("code"),
                    r,
                    driver,
                    object.getIntValue("status")
            );
            orderList.add(orderItem);
        }

        Log.i(TAG, "orderList.size() = " + orderList.size());
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
}
