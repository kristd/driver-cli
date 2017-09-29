package com.app.onemenu;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import android.os.Handler;
import android.os.Message;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class DriverListRecyclerViewAdapter extends RecyclerView.Adapter<DriverListRecyclerViewAdapter.NewsViewHolder> {
    private List<DriverItem> drivers;
    private Context context;
    private boolean isSwitchActive;
    private String orderId;
    private Handler closeHandler;
    private Handler showProgHandler;
    private Handler hideProgHandler;

    private final int ON = 1;
    private final int OFF = 0;
    private int status = 0;

    private static String TAG = "DriverListRecyclerVAda";


    public DriverListRecyclerViewAdapter(List<DriverItem> drivers, Context context, boolean isSwitchActive, String orderId, Handler closeHandler, Handler showProgHandler, Handler hideProgHandler) {
        this.context = context;
        this.drivers = drivers;
        this.isSwitchActive = isSwitchActive;
        this.orderId = orderId;
        this.closeHandler = closeHandler;
        this.showProgHandler = showProgHandler;
        this.hideProgHandler = hideProgHandler;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView driver_cardView;
        TextView driver_name;
        TextView driver_phone;
        Switch driver_status;

        public NewsViewHolder(final View itemView) {
            super(itemView);

            driver_cardView = (CardView) itemView.findViewById(R.id.card_view_driver);
            driver_name = (TextView) itemView.findViewById(R.id.driver_name);
            driver_phone = (TextView) itemView.findViewById(R.id.driver_phone);
            driver_status = (Switch) itemView.findViewById(R.id.driver_status);
        }
    }

    @Override
    public DriverListRecyclerViewAdapter.NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.driver_item, viewGroup, false);

        DriverListRecyclerViewAdapter.NewsViewHolder nvh = new DriverListRecyclerViewAdapter.NewsViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(final DriverListRecyclerViewAdapter.NewsViewHolder viewHolder, int i) {
        final int index = i;

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MessageId.UPDATE_DRIVER_STAT_COMPLETED) {
                    Toast.makeText(context, "update status success", Toast.LENGTH_SHORT).show();
                } else if (msg.what == MessageId.UPDATE_DRIVER_STAT_FAILED) {
                    if (status == OFF) {
                        viewHolder.driver_status.setChecked(false);
                    } else {
                        viewHolder.driver_status.setChecked(true);
                    }
                    Toast.makeText(context, "update status failed", Toast.LENGTH_SHORT).show();
                } else if (msg.what == MessageId.ASSIGN_DRIVER_COMPLETED) {
                    Toast.makeText(context, "assign order success", Toast.LENGTH_SHORT).show();
                } else if (msg.what == MessageId.ASSIGN_DRIVER_FAILED) {
                    Toast.makeText(context, "assign order failed", Toast.LENGTH_SHORT).show();
                }
            }
        };

        final Callback assignOrderCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                Message msg = new Message();
                msg.what = MessageId.ASSIGN_DRIVER_FAILED;
                handler.sendMessage(msg);

                closeHandler.sendMessage(new Message());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.i(TAG, data);

                JSONObject jsonObject = (JSONObject) JSON.parse(data);
                if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                    Log.i(TAG, "assign order success");

                    Message msg = new Message();
                    msg.what = MessageId.ASSIGN_DRIVER_COMPLETED;
                    handler.sendMessage(msg);
                } else {
                    Log.i(TAG, "assign order failed");

                    Message msg = new Message();
                    msg.what = MessageId.ASSIGN_DRIVER_FAILED;
                    handler.sendMessage(msg);
                }

                closeHandler.sendMessage(new Message());
            }
        };

        final Callback updateDriverStatCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                Message msg = new Message();
                msg.what = MessageId.UPDATE_DRIVER_STAT_FAILED;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.i(TAG, data);

                JSONObject jsonObject = (JSONObject) JSON.parse(data);
                if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                    Log.i(TAG, "update stat success");

                    Message msg = new Message();
                    msg.what = MessageId.UPDATE_DRIVER_STAT_COMPLETED;
                    handler.sendMessage(msg);
                } else {
                    Log.e(TAG, "update stat failed");

                    Message msg = new Message();
                    msg.what = MessageId.UPDATE_DRIVER_STAT_FAILED;
                    handler.sendMessage(msg);
                }
            }
        };

        if (drivers.get(i).getStat() == ON) {
            viewHolder.driver_status.setChecked(true);
        } else {
            viewHolder.driver_status.setChecked(false);
        }

        viewHolder.driver_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> values = new ConcurrentHashMap();
                values.put("order_id", orderId);
                values.put("driver_id", drivers.get(index).getId());
                JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
                HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.ASSIGN_ORDER_BY_MGR, assignOrderCallback, params);
            }
        });

        viewHolder.driver_name.setText(drivers.get(i).getName());
        viewHolder.driver_status.setChecked(drivers.get(i).getStat() == ON? true:false);
        viewHolder.driver_phone.setText(drivers.get(i).getPhone());
        viewHolder.driver_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Map<String, Object> values = new ConcurrentHashMap();
                values.put("driver_id", drivers.get(index).getId());
                if (isChecked) {
                    status = OFF;
                    values.put("status", ON);
                } else {
                    status = ON;
                    values.put("status", OFF);
                }

                JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
                HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.UPDATE_DRIVER_STAT_BY_MGR, updateDriverStatCallback, params);
            }
        });

        if (isSwitchActive) {
            //the entrance is dirver, disable cardview click listener
            viewHolder.driver_status.setVisibility(View.VISIBLE);

            viewHolder.driver_cardView.setClickable(false);
            //viewHolder.driver_cardView.setEnabled(false);

        } else {
            //the entrance is order, disable switch click listener
            viewHolder.driver_status.setVisibility(View.GONE);

            viewHolder.driver_cardView.setClickable(true);
            //viewHolder.driver_cardView.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }
}
