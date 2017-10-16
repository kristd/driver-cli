package com.onemenu.driver;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

public class SplashActivity extends AppCompatActivity {
    private static String TAG = "SplashActivity";
//    private UserReLoginTask mReAuthTask = null;
//    private View mProgressView;
//    SharedPreferences loginPref;
//    SharedPreferences.Editor loginEditor;

//    public class UserReLoginTask extends AsyncTask<Void, Void, Boolean> {
//        private final String mToken;
//
//        UserReLoginTask(String token) {
//            mToken = token;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            try {
//                Response response = HttpApi.getInstant().newCall(HttpApi.ReLogin(mToken, "")).execute();
//                String data = response.body().string();
//                Log.i(TAG, data);
//
//                JSONObject object = (JSONObject) JSON.parse(data);
//                if (object != null && object.getString("status").equals(ErrorCode.Success)) {
//                    initUser(object.getJSONObject("data"));
//                    return true;
//                } else {
//                    return false;
//                }
//            } catch (Exception ioEx) {
//                Log.e(TAG, ioEx.getMessage());
//                return false;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mReAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                Log.i(TAG, "LOGIN SUCCESS");
//                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(intent);
//
//                finish();
//            } else {
//                Log.i(TAG, "LOGIN FAILED");
//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(intent);
//
//                finish();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mReAuthTask = null;
//            showProgress(false);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },1000*3);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

//    private void showProgress(final boolean show) {
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                mProgressView.animate().setDuration(shortAnimTime).alpha(
//                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                    }
//                });
//            } else {
//                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

//    private void initUser(JSONObject params) {
//        JSONObject driver = params.getJSONObject("driver");
//        String token = params.getString("login_token");
//
//        Avatar avatar;
//        try {
//            avatar = new Avatar(driver.getJSONObject("avatar").getString("url"), driver.getJSONObject("avatar").getIntValue("width"), driver.getJSONObject("avatar").getIntValue("height"));
//        } catch (Exception ex) {
//            Log.e(TAG, "no avatar available");
//            avatar = new Avatar("", 0, 0);
//        }
//
//        User.init(driver.getString("phone_num"),
//                "",
//                token,
//                driver.getIntValue("work_status"),
//                driver.getString("name"),
//                driver.getString("id"),
//                avatar,
//                driver.getIntValue("driver_type"));
//
//        loginEditor.putString("url", avatar.getUrl());
//        loginEditor.putInt("width", avatar.getWidth());
//        loginEditor.putInt("height", avatar.getHeight());
//
//        loginEditor.putString("token", token);
//        loginEditor.putString("name", User.getInstant().getName());
//        loginEditor.putString("phone_num", User.getInstant().getPhone());
//        loginEditor.putString("id", User.getInstant().getId());
//        loginEditor.putInt("driver_type", User.getInstant().getType());
//        // 提交数据修改
//        loginEditor.commit();
//    }
}
