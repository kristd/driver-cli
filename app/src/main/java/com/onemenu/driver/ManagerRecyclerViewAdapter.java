package com.onemenu.driver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;


public class ManagerRecyclerViewAdapter extends RecyclerView.Adapter<ManagerRecyclerViewAdapter.NewsViewHolder> {
    private List<OrderItem> orders;
    private Context context;

    private static String TAG = "ManagerRecyclerViewAda";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            if (msg.what == MessageId.UPDATE_DRIVER_STAT_COMPLETED) {
//                Toast.makeText(context, "update status success", Toast.LENGTH_SHORT).show();
//            } else if (msg.what == MessageId.UPDATE_DRIVER_STAT_FAILED) {
//                Toast.makeText(context, "update status failed", Toast.LENGTH_SHORT).show();
//            } else if (msg.what == MessageId.ASSIGN_DRIVER_COMPLETED) {
//                Toast.makeText(context, "assing driver success", Toast.LENGTH_SHORT).show();
//            } else if (msg.what == MessageId.ASSIGN_DRIVER_FAILED) {
//                Toast.makeText(context, "assing driver failed", Toast.LENGTH_SHORT).show();
//            }

            if (msg.what == MessageId.ASSIGN_DRIVER_COMPLETED) {
                //Toast.makeText(context, "assing driver success", Toast.LENGTH_SHORT).show();
            } else if (msg.what == MessageId.ASSIGN_DRIVER_FAILED) {
                Toast.makeText(context, "Assing driver failed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public ManagerRecyclerViewAdapter(List<OrderItem> orders, Context context) {
        this.context = context;
        this.orders = orders;
    }

//    Callback updateDriverStatCallback = new Callback() {
//        @Override
//        public void onFailure(Call call, IOException e) {
//            e.printStackTrace();
//
//            Message msg = new Message();
//            msg.what = MessageId.UPDATE_DRIVER_STAT_FAILED;
//            handler.sendMessage(msg);
//        }
//
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            String data = response.body().string();
//            Log.i(TAG, data);
//
//            JSONObject jsonObject = (JSONObject) JSON.parse(data);
//            if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
//                Log.i(TAG, "update stat success");
//
//                Message msg = new Message();
//                msg.what = MessageId.UPDATE_DRIVER_STAT_COMPLETED;
//                handler.sendMessage(msg);
//            } else {
//                Log.e(TAG, "update stat failed");
//
//                Message msg = new Message();
//                msg.what = MessageId.UPDATE_DRIVER_STAT_FAILED;
//                handler.sendMessage(msg);
//            }
//        }
//    };

    Callback assignDriverCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();

            Message msg = new Message();
            msg.what = MessageId.ASSIGN_DRIVER_FAILED;
            handler.sendMessage(msg);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String data = response.body().string();
            Log.i(TAG, data);

            JSONObject jsonObject = (JSONObject) JSON.parse(data);
            if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                Log.i(TAG, "assign driver success");

                Message msg = new Message();
                msg.what = MessageId.ASSIGN_DRIVER_COMPLETED;
                handler.sendMessage(msg);
            } else {
                Log.e(TAG, "assign driver failed");

                Message msg = new Message();
                msg.what = MessageId.ASSIGN_DRIVER_FAILED;
                handler.sendMessage(msg);
            }
        }
    };

    //自定义ViewHolder类
    static class NewsViewHolder extends RecyclerView.ViewHolder {
//        CardView driver_cardView;
//        TextView driver_name;
//        TextView driver_phone;
//        Switch driver_status;

        CardView mgr_order_cardView;
        TextView manager_last5code;
        TextView manager_rest_name;
        TextView manger_rest_address;
        TextView manager_driver_name;
        TextView manager_driver_phone;
        ImageView manager_imagebtn;
        Button manager_operator_btn;

        public NewsViewHolder(final View itemView) {
            super(itemView);

//            driver_cardView = (CardView) itemView.findViewById(R.id.card_view_driver);
//            driver_name = (TextView) itemView.findViewById(R.id.driver_name);
//            driver_phone = (TextView) itemView.findViewById(R.id.driver_phone);
//            driver_status = (Switch) itemView.findViewById(R.id.driver_status);

            mgr_order_cardView = (CardView) itemView.findViewById(R.id.card_view_mgr_order);
            manager_last5code = (TextView) itemView.findViewById(R.id.manager_last5code);
            manager_rest_name = (TextView) itemView.findViewById(R.id.manager_rest_name);
            manger_rest_address = (TextView) itemView.findViewById(R.id.manger_rest_address);
            manager_driver_name = (TextView) itemView.findViewById(R.id.manager_driver_name);
            manager_driver_phone = (TextView) itemView.findViewById(R.id.manager_driver_phone);
            manager_imagebtn = (ImageView) itemView.findViewById(R.id.manager_imagebtn);
            manager_operator_btn = (Button) itemView.findViewById(R.id.manager_operator_btn);
        }
    }

    @Override
    public ManagerRecyclerViewAdapter.NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View v = null;
//
//        if (i == ITEM_TYPE.ITEM_TYPE_DRIVER.ordinal()) {
//            v = LayoutInflater.from(context).inflate(R.layout.driver_item, viewGroup, false);
//        } else if (i == ITEM_TYPE.ITEM_TYPE_ORDER.ordinal()) {
//            v = LayoutInflater.from(context).inflate(R.layout.mgr_order_item, viewGroup, false);
//        }

        View v = LayoutInflater.from(context).inflate(R.layout.mgr_order_item, viewGroup, false);

        NewsViewHolder nvh = new NewsViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(ManagerRecyclerViewAdapter.NewsViewHolder viewHolder, int i) {
        final int index = i;

//        if (item_type == ITEM_TYPE.ITEM_TYPE_DRIVER) {
//            if (drivers.get(i).getStat() == ON) {
//                viewHolder.driver_status.setChecked(true);
//            } else {
//                viewHolder.driver_status.setChecked(false);
//            }
//            viewHolder.driver_name.setText(drivers.get(i).getName());
//            viewHolder.driver_status.setChecked(drivers.get(i).getStat() == ON? true:false);
//            viewHolder.driver_phone.setText(drivers.get(i).getPhone());
//            viewHolder.driver_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                    Map<String, Object> values = new ConcurrentHashMap();
//                    values.put("driver_id", drivers.get(index).getId());
//                    if (isChecked) {
//                        values.put("status", ON);
//                    } else {
//                        values.put("status", OFF);
//                    }
//
//                    JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
//                    HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.UPDATE_DRIVER_STAT_BY_MGR, updateDriverStatCallback, params);
//                }
//            });
//        } else {
//            viewHolder.manager_operator_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String orderId = orders.get(index).getId();
//                    String driverId = orders.get(index).getDriver().getId();
//
//                    Map<String, Object> values = new ConcurrentHashMap();
//                    values.put("order_id", orderId);
//                    values.put("driver_id", driverId);
//                    JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
//
//                    HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.ASSIGN_ORDER_BY_MGR, assignDriverCallback, params);
//                }
//            });
//
//            viewHolder.manager_imagebtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+orders.get(index).getDriver().getPhone()));
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }
//            });
//
//            viewHolder.manager_last5code.setText(orders.get(index).getLast5Code());
//            viewHolder.manager_rest_name.setText(orders.get(index).getRestaurant().getName());
//            viewHolder.manger_rest_address.setText(orders.get(index).getRestaurant().getLocation().getAddress());
//            if (orders.get(index).getDriver() != null) {
//                viewHolder.manager_driver_name.setText(orders.get(index).getDriver().getName());
//                viewHolder.manager_driver_phone.setText(orders.get(index).getDriver().getPhone());
//            } else {
//                viewHolder.manager_driver_name.setText("order not assign");
//                viewHolder.manager_driver_phone.setVisibility(View.GONE);
//                viewHolder.manager_imagebtn.setVisibility(View.GONE);
//            }
//        }

        viewHolder.manager_operator_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String orderId = orders.get(index).getId();
//                String driverId = orders.get(index).getDriver().getId();
//
//                Map<String, Object> values = new ConcurrentHashMap();
//                values.put("order_id", orderId);
//                values.put("driver_id", driverId);
//                JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
//
//                HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.ASSIGN_ORDER_BY_MGR, assignDriverCallback, params);

                Intent intent = new Intent(context, DriverListActivity.class);
                intent.putExtra("isSwithActive", false);
                intent.putExtra("orderId", orders.get(index).getId());
                context.startActivity(intent);
            }
        });

        viewHolder.manager_imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+orders.get(index).getDriver().getPhone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        viewHolder.manager_last5code.setText(orders.get(index).getLast5Code());
        viewHolder.manager_rest_name.setText(orders.get(index).getRestaurant().getName());
        viewHolder.manger_rest_address.setText(orders.get(index).getRestaurant().getLocation().getAddress());
        if (orders.get(index).getDriver() != null) {
            viewHolder.manager_driver_name.setText(orders.get(index).getDriver().getName());
            viewHolder.manager_driver_phone.setText(orders.get(index).getDriver().getPhone());
            if (orders.get(index).getDriver().getPhone() == null || orders.get(index).getDriver().getPhone().equals("")) {
                viewHolder.manager_imagebtn.setVisibility(View.GONE);
            }
        } else {
            viewHolder.manager_driver_name.setText("order not assign");
            viewHolder.manager_driver_phone.setVisibility(View.GONE);
            viewHolder.manager_imagebtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (orders.size() == 0) {
//            return ITEM_TYPE.ITEM_TYPE_DRIVER.ordinal();
//        } else {
//            return ITEM_TYPE.ITEM_TYPE_ORDER.ordinal();
//        }
//    }
}
