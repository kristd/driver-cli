package com.onemenu.driver;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

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

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken == null) {
            refreshedToken = "";
        }
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        Map<String, String> values = new ConcurrentHashMap();
        values.put("notification_token", token);
        JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));

        HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.UPDATE_NOTIFY_TOKEN, updateNotifyTokenCallback, params);
    }
}
