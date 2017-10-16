package com.onemenu.driver;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class InfoFragment extends Fragment {
    private TextView info_phone;
    private TextView info_name;
    private Switch info_stat;
    private String verifyCode = "";
    private String nPhone = "";
    private final String TAG = "InfoFragment";

    private int ON = 1;
    private int OFF = 0;

    Handler handlerMain = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MessageId.PHONE_CHANGED_COMPLETED) {
                info_phone.setText(nPhone);
                //Toast.makeText(getActivity(), "Changed Phone Success", Toast.LENGTH_SHORT).show();
            } else if (msg.what == MessageId.PHONE_CHANGED_FAILED) {
                //Toast.makeText(getActivity(), "Changed Phone Failed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        info_phone = (TextView) getActivity().findViewById(R.id.info_phone);
        info_phone.setText(User.getInstant().getPhone());
        info_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText info_edit_phone = new EditText(getActivity());
                final EditText info_edit_vcode = new EditText(getActivity());

                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == MessageId.PHONE_CHANGED_COMPLETED) {
                            info_phone.setText(info_edit_phone.getText().toString());
                        } else if (msg.what == MessageId.PHONE_CHANGED_FAILED) {
                            //Toast.makeText(getActivity(), "Changed Phone Failed", Toast.LENGTH_SHORT).show();
                        } else if (msg.what == MessageId.GET_VERIFY_CODE_COMPLETED) {
                            //Toast.makeText(getActivity(), "Get Verify Code Success", Toast.LENGTH_SHORT).show();
                        } else if (msg.what == MessageId.GET_VERIFY_CODE_FAILED) {
                            //Toast.makeText(getActivity(), "Get Verify Code Failed", Toast.LENGTH_SHORT).show();
                        } else if (msg.what == MessageId.CHECK_VERIFY_CODE_FAILED) {
                            //Toast.makeText(getActivity(), "Verify Code Check Failed", Toast.LENGTH_SHORT).show();
                        } else if (msg.what == MessageId.CHECK_VERIFY_CODE_COMPLETED) {
                            AlertDialog dialog_newPhone = new AlertDialog.Builder(getActivity())
                                .setTitle("PHONE NUMBER")
                                .setView(info_edit_phone)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String newPhone = info_edit_phone.getText().toString();

                                        final okhttp3.Callback updateDriverPhoneCallback = new okhttp3.Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                //Log.e(TAG, e.getMessage());
                                                e.printStackTrace();

                                                Message msg = new Message();
                                                msg.what = MessageId.PHONE_CHANGED_FAILED;
                                                handlerMain.sendMessage(msg);
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String data = response.body().string();
                                                Log.i(TAG, data);

                                                JSONObject jsonObject = (JSONObject) JSON.parse(data);
                                                if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                                                    Log.i(TAG, "update user phone success");

                                                    Message msg = new Message();
                                                    msg.what = MessageId.PHONE_CHANGED_COMPLETED;
                                                    handlerMain.sendMessage(msg);
                                                } else {
                                                    Log.e(TAG, "update user phone failed");

                                                    Message msg = new Message();
                                                    msg.what = MessageId.PHONE_CHANGED_FAILED;
                                                    handlerMain.sendMessage(msg);
                                                }
                                            }
                                        };

                                        Map<String, Object> values = new ConcurrentHashMap();
                                        values.put("phone_num", newPhone);
                                        values.put("verification_code", verifyCode);
                                        JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
                                        HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.UPDATE_DRIVER_PHONE, updateDriverPhoneCallback, params);
                                    }
                                })
                                .setNegativeButton("CANCEL" ,  null)
                                .create();
                            dialog_newPhone.show();
                        }
                    }
                };

                final Callback getVerifyCodeCallback = new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message msg = new Message();
                        msg.what = MessageId.GET_VERIFY_CODE_FAILED;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = response.body().string();
                        Log.i(TAG, data);

                        JSONObject jsonObject = (JSONObject) JSON.parse(data);
                        if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                            Message msg = new Message();
                            msg.what = MessageId.GET_VERIFY_CODE_COMPLETED;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = MessageId.GET_VERIFY_CODE_FAILED;
                            handler.sendMessage(msg);
                        }
                    }
                };

                final Callback checkVerifyCodeCallback = new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message msg = new Message();
                        msg.what = MessageId.CHECK_VERIFY_CODE_FAILED;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = response.body().string();
                        Log.i(TAG, data);

                        JSONObject jsonObject = (JSONObject) JSON.parse(data);
                        if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                            Message msg = new Message();
                            msg.what = MessageId.CHECK_VERIFY_CODE_FAILED;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = MessageId.CHECK_VERIFY_CODE_COMPLETED;
                            handler.sendMessage(msg);
                        }
                    }
                };

                Map<String, String> values = new ConcurrentHashMap<>();
                values.put("phone_num", User.getInstant().getPhone());
                JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
                HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.GET_VERIFY_CODE, getVerifyCodeCallback, params);

                AlertDialog dialog_verifyCode = new AlertDialog.Builder(getActivity())
                        .setTitle("VERIFY CODE")
                        .setView(info_edit_vcode)
                        .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String vCode = info_edit_vcode.getText().toString();
                                verifyCode = vCode;

                                Map<String, String> values = new ConcurrentHashMap<>();
                                values.put("phone_num", User.getInstant().getPhone());
                                values.put("verification_code", vCode);
                                JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
                                HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.CHECK_VERIFY_CODE, checkVerifyCodeCallback, params);
                            }
                        })
                        .setNegativeButton("CANCEL", null)
                        .create();
                dialog_verifyCode.show();
            }
        });

        info_name = (TextView) getActivity().findViewById(R.id.info_username);
        info_name.setText(User.getInstant().getName());
        info_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText info_edit_name = new EditText(getActivity());
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == MessageId.NAME_CHANGED_COMPLETED) {
                            info_name.setText(info_edit_name.getText().toString());
                        } else if (msg.what == MessageId.NAME_CHANGED_FAILE) {
                            //Toast.makeText(getActivity(), "Changed Name Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                final Callback updateDriverNameCallback = new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //Log.e(TAG, e.getMessage());
                        e.printStackTrace();

                        Message msg = new Message();
                        msg.what = MessageId.NAME_CHANGED_FAILE;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = response.body().string();
                        Log.i(TAG, data);

                        JSONObject jsonObject = (JSONObject) JSON.parse(data);
                        if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                            Log.i(TAG, "update user name success");

                            Message msg = new Message();
                            msg.what = MessageId.NAME_CHANGED_COMPLETED;
                            handler.sendMessage(msg);
                        } else {
                            Log.e(TAG, "update user name failed");

                            Message msg = new Message();
                            msg.what = MessageId.NAME_CHANGED_FAILE;
                            handler.sendMessage(msg);
                        }
                    }
                };

                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("NAME")
                        .setView(info_edit_name)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String newName = info_edit_name.getText().toString();

                                Map<String, Object> values = new ConcurrentHashMap();
                                values.put("name", newName);
                                JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
                                HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.UPDATE_DRIVER_NAME, updateDriverNameCallback, params);
                            }
                        })
                        .setNegativeButton("CANCEL" ,  null )
                        .create();
                dialog.show();
            }
        });

        info_stat = (Switch) getActivity().findViewById(R.id.info_status);
        if (User.getInstant().getStatus() == ON) {
            info_stat.setChecked(true);
        } else {
            info_stat.setChecked(false);
        }
        info_stat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Map<String, Object> values = new ConcurrentHashMap();

                Callback updateDriverStatCallback = new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        //Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = response.body().string();
                        Log.i(TAG, data);

                        JSONObject jsonObject = (JSONObject) JSON.parse(data);
                        if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                            Log.i(TAG, "update user status success");
                        } else {
                            Log.e(TAG, "update user status failed");
                        }
                    }
                };

                if (isChecked) {
                    values.put("status", ON);
                } else {
                    values.put("status", OFF);
                }

                JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
                HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.UPDATE_DRIVER_STAT, updateDriverStatCallback, params);
            }
        });
    }

    public static InfoFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
