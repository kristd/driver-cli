package com.onemenu.driver;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.firebase.iid.FirebaseInstanceId;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends FragmentActivity implements BottomNavigationBar.OnTabSelectedListener {
    private BottomNavigationBar bottomNavigationBar;
    private ArrayList<Fragment> fragments;
    private Fragment lastFragment;

    private static String TAG = "MainActivity";

    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MessageId.REFRESH_ORDER_FAILED) {
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            } else if (msg.what == MessageId.REFRESH_ORDER_COMPLETED) {
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        }
    };

    Callback updateNotifyTokenCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.i(TAG, "update notify token failed");
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String data = response.body().string();
            Log.i(TAG, data);

            JSONObject jsonObject = (JSONObject) JSON.parse(data);
            if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                Log.i(TAG, "update notify token success");
            } else {
                Log.i(TAG, "update notify token failed");
            }
        }
    };

    public class UploadLocationThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    Thread.sleep(1000 * 60);// 线程暂停10秒，单位毫秒
//                    Message message = new Message();
//                    message.what = 1;
//                    handler.sendMessage(message);// 发送消息

                    //Location mLocation = getLocation();
                    Map<String, Object> values = new ConcurrentHashMap();
                    values.put("latitude", "40.7929399");
                    values.put("longitude", "-77.8635751");
                    JSONObject param = JSONObject.parseObject(JSON.toJSONString(values));

                    HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.UPDATE_DRIVER_LOCATION, getLocationCallBack, param);
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BaseApplication.NewInst();
        BaseApplication.setMainActivity(this);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottomNavigation);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        if (User.getInstant().getType() == DriverType.Manager) {
            bottomNavigationBar
                    .addItem(new BottomNavigationItem(R.drawable.ic_orders_unselected_3x, "order").setActiveColorResource(R.color.blue))
                    .addItem(new BottomNavigationItem(R.drawable.ic_schedule_unselected_3x, "report").setActiveColorResource(R.color.blue))
                    .addItem(new BottomNavigationItem(R.drawable.ic_me_unselected_3x, "me").setActiveColorResource(R.color.blue))
                    .addItem(new BottomNavigationItem(R.drawable.ic_manager_unselected_3x, "manager").setActiveColorResource(R.color.blue))
                    .setFirstSelectedPosition(0)
                    .initialise();
        } else {
            bottomNavigationBar
                    .addItem(new BottomNavigationItem(R.drawable.ic_orders_unselected_3x, "order").setActiveColorResource(R.color.blue))
                    .addItem(new BottomNavigationItem(R.drawable.ic_schedule_unselected_3x, "report").setActiveColorResource(R.color.blue))
                    .addItem(new BottomNavigationItem(R.drawable.ic_me_unselected_3x, "me").setActiveColorResource(R.color.blue))
                    .setFirstSelectedPosition(0)
                    .initialise();
        }

        fragments = getFragments();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);

        //update notify token
        //updateNotifyToken();

        new Thread(new UploadLocationThread()).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(OrderFragment.newInstance("order"));
        fragments.add(ReportFragment.newInstance("report", getSupportFragmentManager()));
        fragments.add(InfoFragment.newInstance("info"));
        fragments.add(ManagerFragment.newInstance("manager"));

        return fragments;
    }

    public Fragment getLastFragment() {
        return lastFragment;
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.layFrame, fragments.get(0));
        transaction.commit();
        lastFragment  = fragments.get(0);
    }

    @Override
    public void onTabSelected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);

                ft.remove(lastFragment);
                lastFragment = fragment;

                if (fragment.isAdded()) {
                    ft.replace(R.id.layFrame, fragment);
                } else {
                    ft.add(R.id.layFrame, fragment);
                }
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabUnselected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }

    Callback getLocationCallBack = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.i(TAG, "upload location info success");
        }
    };

    //Get the Location by GPS or WIFI
    private Location getLocation() {
        LocationManager locMan = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        Location location = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        return location;
    }

//    private void updateNotifyToken() {
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        if (refreshedToken == null) {
//            refreshedToken = "";
//        }
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//
//        Map<String, String> values = new ConcurrentHashMap();
//        values.put("notification_token", refreshedToken);
//        JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
//        HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.UPDATE_NOTIFY_TOKEN, updateNotifyTokenCallback, params);
//    }
}
