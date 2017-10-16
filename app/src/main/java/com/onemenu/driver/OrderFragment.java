package com.onemenu.driver;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.Callback;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    private TextView mTextViewOrderList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private OrderRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<OrderItem> orderList = new ArrayList<>();
    //private View progressView;
    private MyProgessView pView = new MyProgessView();

    private static String TAG = "OrderFragment";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MessageId.GET_ORDER_LIST_COMPLETED) {
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.getAdapter().notifyItemRangeChanged(0, recyclerView.getAdapter().getItemCount());
                swipeRefreshLayout.setVisibility(View.VISIBLE);

                //Toast.makeText(getActivity(), "get order list success", Toast.LENGTH_SHORT).show();
            } else if (msg.what == MessageId.DRIVER_OFFLINE) {
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.getAdapter().notifyItemRangeChanged(0, recyclerView.getAdapter().getItemCount());

                Toast.makeText(getActivity(), "Driver is offline", Toast.LENGTH_SHORT).show();
            } else if (msg.what == MessageId.GET_ORDER_LIST_FAILED) {
                //Toast.makeText(getActivity(), "Get order list failed", Toast.LENGTH_SHORT).show();
            }

            pView.showProgress(getResources(), false);
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    };

    private Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            swipeRefreshLayout.setVisibility(View.GONE);
            HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.GET_ORDER_LIST, getOrderListCallback, null);
            pView.showProgress(getResources(), true);
        }
    };

    private Handler showProgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            swipeRefreshLayout.setVisibility(View.GONE);
            pView.showProgress(getResources(), true);
        }
    };

    private Handler hideProgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            pView.showProgress(getResources(), false);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    };

    Callback getOrderListCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //Log.e(TAG, e.getMessage());
            e.printStackTrace();

            Message msg = new Message();
            msg.what =  MessageId.GET_ORDER_LIST_FAILED;
            handler.sendMessage(msg);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String data = response.body().string();
            Log.i(TAG, data);

            JSONObject jsonObject = (JSONObject) JSON.parse(data);
            if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                initOrderData(jsonObject);

                Message msg = new Message();
                msg.what =  MessageId.GET_ORDER_LIST_COMPLETED;
                handler.sendMessage(msg);
            } else if (jsonObject.getString("status").equals(ErrorCode.Offline)) {
                Message msg = new Message();
                msg.what =  MessageId.DRIVER_OFFLINE;
                handler.sendMessage(msg);
            } else {
                Message msg = new Message();
                msg.what =  MessageId.GET_ORDER_LIST_FAILED;
                handler.sendMessage(msg);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        pView.VIEW = getActivity().findViewById(R.id.order_get_order_progress);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView_order);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OrderRecyclerViewAdapter(orderList, getActivity(), refreshHandler, showProgHandler, hideProgHandler);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_order);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.GET_ORDER_LIST, getOrderListCallback, null);
            }
        });

        mTextViewOrderList = (TextView) getActivity().findViewById(R.id.tv_orderList);
        mTextViewOrderList.setVisibility(View.GONE);
        mTextViewOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewOrderList.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);

                HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.GET_ORDER_LIST, getOrderListCallback, null);
                pView.showProgress(getResources(), true);
            }
        });
    }

    public static OrderFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        swipeRefreshLayout.setVisibility(View.GONE);
        HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.GET_ORDER_LIST, getOrderListCallback, null);
        pView.showProgress(getResources(), true);
    }

    private void initOrderData(JSONObject params) {
        orderList.clear();

        JSONObject data = params.getJSONObject("data");
        if (data == null) {
            return;
        }

        JSONArray orderListArray = data.getJSONArray("order_list");
        if (orderListArray != null) {
            for (int i = 0; i < orderListArray.size(); i++) {
                JSONObject object = orderListArray.getJSONObject(i);

                JSONObject customerObj = object.getJSONObject("customer");
                Customer c = new Customer(customerObj.getString("id"), customerObj.getString("name"), customerObj.getString("phone_num"));

                JSONObject customerLoc = object.getJSONObject("destination");
                MyLocation cLocation = new MyLocation(customerLoc.getString("id"), customerLoc.getString("address"), customerLoc.getString("latitude"), customerLoc.getString("longitude"));

                JSONObject restLoc = object.getJSONObject("restaurant").getJSONObject("location");
                MyLocation rLocation = new MyLocation(restLoc.getString("id"), restLoc.getString("address"), restLoc.getString("latitude"), restLoc.getString("longitude"));

                JSONObject restObj = object.getJSONObject("restaurant");
                Restaurant r = new Restaurant(restObj.getString("id"), rLocation, restObj.getString("name"), restObj.getString("phone_num"));

                OrderItem item = new OrderItem(
                        object.getIntValue("already_in_time"),
                        object.getLongValue("create_time"),
                        c,
                        cLocation,
                        object.getString("id"),
                        object.getString("last5code"),
                        object.getString("code"),
                        r,
                        null,
                        object.getIntValue("status"));      //13
                orderList.add(item);
            }
        }

        JSONArray otherOrderArray = data.getJSONArray("other_available_order_list");
        if(otherOrderArray != null) {
            for (int i = 0; i < otherOrderArray.size(); i++) {
                JSONObject object = otherOrderArray.getJSONObject(i);

                JSONObject customerLoc = object.getJSONObject("destination");
                MyLocation cLocation = new MyLocation(customerLoc.getString("id"), customerLoc.getString("address"), customerLoc.getString("latitude"), customerLoc.getString("longitude"));

                JSONObject restLoc = object.getJSONObject("restaurant").getJSONObject("location");
                MyLocation rLocation = new MyLocation(restLoc.getString("id"), restLoc.getString("address"), restLoc.getString("latitude"), restLoc.getString("longitude"));

                JSONObject restObj = object.getJSONObject("restaurant");
                Restaurant r = new Restaurant(restObj.getString("id"), rLocation, restObj.getString("name"), restObj.getString("phone_num"));

                OrderItem item = new OrderItem(
                        object.getIntValue("already_in_time"),
                        object.getLongValue("create_time"),
                        null,
                        cLocation,
                        object.getString("id"),
                        object.getString("last5code"),
                        "",
                        r,
                        null,
                        object.getIntValue("status"));      //14
                orderList.add(item);
            }
        }
        //orderList.add(new OrderItem(12, 12312333, new Customer("", "", ""), new MyLocation("", "", "", ""), "122321232", "12345", "as2333", new Restaurant("", new MyLocation("", "", "", ""), "", ""), 13));
    }

//    private void showProgress(final boolean show) {
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                progressView.animate().setDuration(shortAnimTime)
//                        .alpha(show ? 1 : 0)
//                        .setListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                            }
//                        });
//            } else {
//                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public void refreshOrderList() {
        refreshHandler.sendMessage(new Message());
        Log.i(TAG, "RefreshOrderList Done");
    }
}
