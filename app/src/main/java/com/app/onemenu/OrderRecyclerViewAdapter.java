package com.app.onemenu;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Layout;
import android.text.method.NumberKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.NewsViewHolder> {
    private List<OrderItem> orders;
    private Context context;
    private Handler refreshHandler;
    private Handler showProgHandler;
    private Handler hideProgHandler;

    private static String TAG = "OrderRecyclerViewAda";


    public OrderRecyclerViewAdapter(List<OrderItem> orders, Context context, Handler refreshHandler, Handler showProgHandler, Handler hideProgHandler) {
        this.orders = orders;
        this.context = context;
        this.refreshHandler = refreshHandler;
        this.showProgHandler = showProgHandler;
        this.hideProgHandler = hideProgHandler;
    }

    //自定义ViewHolder类
    static class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView last5code;
        TextView restName;
        TextView addrLabel;
        TextView address;
        TextView custom_info;
        TextView custName;
        TextView custPhone;
        Button opreratr_button;

        public NewsViewHolder(final View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_order);
            last5code = (TextView) itemView.findViewById(R.id.order_last5code);
            restName = (TextView) itemView.findViewById(R.id.order_rest_name);
            addrLabel = (TextView) itemView.findViewById(R.id.order_address_label);
            address = (TextView) itemView.findViewById(R.id.order_address);
            custom_info = (TextView) itemView.findViewById(R.id.order_customer_info_lable);
            custName = (TextView) itemView.findViewById(R.id.order_customer_name);
            custPhone = (TextView) itemView.findViewById(R.id.order_customer_phone);
            opreratr_button = (Button) itemView.findViewById(R.id.order_operator_btn);
        }
    }

    @Override
    public OrderRecyclerViewAdapter.NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_item, viewGroup, false);
        OrderRecyclerViewAdapter.NewsViewHolder nvh = new OrderRecyclerViewAdapter.NewsViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(final OrderRecyclerViewAdapter.NewsViewHolder viewHolder, final int i) {
        final int index = i;

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MessageId.ACCEPT_ORDER_COMPLETED) {
                    viewHolder.custom_info.setVisibility(View.VISIBLE);
                    viewHolder.custName.setVisibility(View.VISIBLE);
                    viewHolder.custPhone.setVisibility(View.VISIBLE);
                    viewHolder.opreratr_button.setText("PICK UP");

                    refreshHandler.sendMessage(new Message());

                    Toast.makeText(context, "accept order success", Toast.LENGTH_SHORT).show();
                } else if (msg.what == MessageId.ACCEPT_ORDER_FAILED) {
                    Toast.makeText(context, "accept order failed", Toast.LENGTH_SHORT).show();
                } else if (msg.what == MessageId.PICKUP_ORDER_COMPLETED) {
                    viewHolder.custom_info.setVisibility(View.VISIBLE);
                    viewHolder.custName.setVisibility(View.VISIBLE);
                    viewHolder.custPhone.setVisibility(View.VISIBLE);
                    viewHolder.opreratr_button.setText("FINISH");

                    refreshHandler.sendMessage(new Message());

                    Toast.makeText(context, "pick up order success", Toast.LENGTH_SHORT).show();
                } else if (msg.what == MessageId.PICKUP_ORDER_FAILED) {
                    Toast.makeText(context, "pick up order failed", Toast.LENGTH_SHORT).show();
                } else if (msg.what == MessageId.FINISH_ORDER_COMPLETED) {
                    viewHolder.custom_info.setVisibility(View.VISIBLE);
                    viewHolder.custName.setVisibility(View.VISIBLE);
                    viewHolder.custPhone.setVisibility(View.VISIBLE);

                    refreshHandler.sendMessage(new Message());

                    double tips_fee = msg.getData().getDouble("tips_fee");
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("ORDER FINISHED")
                            .setMessage("ORDER TIPS:   $" + String.valueOf(tips_fee))
                            .setPositiveButton("OK", null)
                            .create();
                    dialog.show();

                    viewHolder.opreratr_button.setText("FINISHED");
                    viewHolder.opreratr_button.setClickable(false);
                    Toast.makeText(context, "finish order success", Toast.LENGTH_SHORT).show();
                } else if (msg.what == MessageId.FINISH_ORDER_FAILED) {
                    Toast.makeText(context, "finish order failed", Toast.LENGTH_SHORT).show();
                }

                hideProgHandler.sendMessage(new Message());
            }
        };

        final Callback acceptOrderCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Log.e(TAG, e.getMessage());
                e.printStackTrace();

                Message msg = new Message();
                msg.what = MessageId.ACCEPT_ORDER_FAILED;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.i(TAG, data);

                JSONObject jsonObject = (JSONObject) JSON.parse(data);
                if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                    Message msg = new Message();
                    msg.what = MessageId.ACCEPT_ORDER_COMPLETED;
                    handler.sendMessage(msg);

                } else {
                    Message msg = new Message();
                    msg.what = MessageId.ACCEPT_ORDER_FAILED;
                    handler.sendMessage(msg);
                }
            }
        };

        final Callback pickUpOrderCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Log.e(TAG, e.getMessage());
                e.printStackTrace();

                Message msg = new Message();
                msg.what = MessageId.PICKUP_ORDER_FAILED;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.i(TAG, "PickUpOrderCallback DATA=" + data);

                JSONObject jsonObject = (JSONObject) JSON.parse(data);
                if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {

                    Message msg = new Message();
                    msg.what = MessageId.PICKUP_ORDER_COMPLETED;
                    handler.sendMessage(msg);


                } else {
                    Message msg = new Message();
                    msg.what = MessageId.PICKUP_ORDER_FAILED;
                    handler.sendMessage(msg);
                }
            }
        };

        final Callback finishOrderCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                Message msg = new Message();
                msg.what = MessageId.FINISH_ORDER_FAILED;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.i(TAG, data);

                JSONObject jsonObject = (JSONObject) JSON.parse(data);
                if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                    double tips_fee = jsonObject.getJSONObject("data").getJSONObject("order").getDoubleValue("tips_fee");

                    Message msg = new Message();
                    msg.what = MessageId.FINISH_ORDER_COMPLETED;
                    Bundle bundle = msg.getData();
                    bundle.putDouble("tips_fee", tips_fee);
                    handler.sendMessage(msg);


                } else {
                    Message msg = new Message();
                    msg.what = MessageId.FINISH_ORDER_FAILED;
                    handler.sendMessage(msg);
                }
            }
        };

        viewHolder.last5code.setText(orders.get(index).getLast5Code().substring(0,3));
        viewHolder.restName.setText(orders.get(index).getRestaurant().getName());
        viewHolder.address.setText(orders.get(index).getCustomerLocation().getAddress());

        if (orders.get(index).getCustomer() != null) {
            viewHolder.custName.setText(orders.get(index).getCustomer().getName());
            viewHolder.custPhone.setText(orders.get(index).getCustomer().getPhone());

            if (orders.get(index).getStatus() == OrderStatus.RestaurantPendingOneMenuDelivery) {
                viewHolder.custom_info.setVisibility(View.GONE);
                viewHolder.custName.setVisibility(View.GONE);
                viewHolder.custPhone.setVisibility(View.GONE);
            } else {
                viewHolder.custom_info.setVisibility(View.VISIBLE);
                viewHolder.custName.setVisibility(View.VISIBLE);
                viewHolder.custPhone.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.custom_info.setVisibility(View.GONE);
            viewHolder.custName.setVisibility(View.GONE);
            viewHolder.custPhone.setVisibility(View.GONE);
        }

        viewHolder.custPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orders.get(index).getCustomer() != null && orders.get(index).getCustomer().getPhone() != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orders.get(index).getCustomer().getPhone()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

        if (orders.get(index).getStatus() == OrderStatus.RestaurantPendingOneMenuDelivery) {
            viewHolder.opreratr_button.setText("ACCEPT");
        } else if (orders.get(index).getStatus() == OrderStatus.PendingForDriverAccept) {
            viewHolder.opreratr_button.setText("PICK UP");
        } else if (orders.get(index).getStatus() == OrderStatus.DriverAccepted) {
            viewHolder.opreratr_button.setText("FINISH");
        } else {
            viewHolder.opreratr_button.setText("FINISHED");
        }

        viewHolder.opreratr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.opreratr_button.getText().toString().equals("ACCEPT")) {
                    Map<String, String> values = new ConcurrentHashMap();
                    values.put("order_id", orders.get(index).getId());
                    JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
                    showProgHandler.sendMessage(new Message());
                    HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.ACCEPT_ORDER, acceptOrderCallback, params);
                } else if (viewHolder.opreratr_button.getText().toString().equals("PICK UP")) {
                    final EditText last2code_edit = new EditText(context);

                    //last2code_edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    //last2code_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    last2code_edit.setGravity(Gravity.CENTER);
                    last2code_edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                    last2code_edit.setKeyListener(new NumberKeyListener() {
                        @Override
                        protected char[] getAcceptedChars() {
                            char[] chars = new char[]{'0','1','2','3','4','5','6','7','8','9'};
                            return chars;
                        }

                        @Override
                        public int getInputType() {
                            return 3;
                        }
                    });

                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("LAST 2 CODE")
                            .setView(last2code_edit)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String last2code = last2code_edit.getText().toString();
                                    Map<String, Object> values = new ConcurrentHashMap();
                                    values.put("last2code", last2code);
                                    values.put("order_id", orders.get(index).getId());
                                    JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
                                    showProgHandler.sendMessage(new Message());
                                    HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.PICKUP_ORDER, pickUpOrderCallback, params);
                                }
                            })
                            .setNegativeButton("CANCEL" ,  null)
                            .create();
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    dialog.show();
                } else if (viewHolder.opreratr_button.getText().toString().equals("FINISH")) {
                    Map<String, String> values = new ConcurrentHashMap();
                    values.put("order_id", orders.get(index).getId());
                    JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
                    showProgHandler.sendMessage(new Message());
                    HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.FINISH_ORDER, finishOrderCallback, params);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
