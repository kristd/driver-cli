package com.app.onemenu;

import android.net.Uri;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;


public class HttpApi {
    private static String sServerAddr = "http://34.229.145.34";
    private static String sLogin = "/app/delivery/signin";
    private static String sAutoLogin = "/app/delivery/auto_signin";
    private static String sDriverList = "/app/delivery/manager_get_driver_list";
    private static String sUpdDriverStatByMrg = "/app/delivery/manager_update_driver_work_status";
    private static String sGetOrderListByMgr = "/app/delivery/manager_get_all_delivery_order_list";
    private static String sAssginOrderByMgr = "/app/delivery/manager_assign_order";
    private static String sHistoryOrder = "/app/delivery/get_statistics_list";
    private static String sOrderList = "/app/delivery/get_delivery_order_list";
    private static String sOrderDetail = "/app/delivery/get_delivery_order_detail";
    private static String sAcceptOrder = "/app/delivery/accept_order";
    private static String sPickUpOrder = "/app/delivery/pickup_order";
    private static String sFinishOrder = "/app/delivery/finish_order";
    private static String sUpdDriverStat = "/app/delivery/update_work_status";
    private static String sUpdDriverPhone = "/app/delivery/update_phone_num";
    private static String sUpdDriverName = "/app/delivery/update_name";
    private static String sUploadDriverLocation = "/app/delivery/update_location";
    private static String sGetVerifyCode = "/verification/get_verification_code";
    private static String sCheckVerifyCode = "/verification/verify_code";
    private static String sRefreshOrderCache = "/app/test/loadOrderInCache.do";

    private static String TAG = "HttpApi";

    private static OkHttpClient mInstant = null;

    final public static String POST = "POST";
    final public static String GET = "GET";

    public enum ServerApi {
        LOGIN,
        RELOGIN,
        HISTORY_ORDER_LIST,
        FINISH_ORDER,
        PICKUP_ORDER,
        ACCEPT_ORDER,
        GET_ORDER_DETAIL,
        GET_ORDER_LIST,
        GET_DRIVER_LIST,
        UPDATE_DRIVER_STAT_BY_MGR,
        UPDATE_DRIVER_PHONE,
        UPDATE_DRIVER_NAME,
        UPDATE_DRIVER_STAT,
        UPDATE_DRIVER_AVATAR,
        UPDATE_DRIVER_LOCATION,
        GET_VERIFY_CODE,
        CHECK_VERIFY_CODE,
        ASSIGN_ORDER_BY_MGR,
        GET_ORDER_LIST_MGR,

        REFRESH_ORDER_CACHE,
    }

    static public void init() {
        mInstant = new OkHttpClient();
    }

    static public OkHttpClient getInstant() {
        return mInstant;
    }

    public static void NewRequest(String method, ServerApi api, Callback callback, JSONObject params) {
        switch (method) {
            case POST:
                NewPostRequest(api, callback, params);
                break;
            case GET:
                NewGetRequest(api, callback, params);
                break;
            default:
        }
    }

    private static void NewGetRequest(ServerApi api, Callback callback, JSONObject params) {
        Request request = null;

        switch (api) {
            case REFRESH_ORDER_CACHE:
                request = RefreshOrderCache(params);
                break;
            default:
                break;
        }

        if (request != null) {
            Call call = getInstant().newCall(request);
            call.enqueue(callback);
        } else {
            Log.e(TAG, "Http interface not exist");
        }
    }

    private static void NewPostRequest(ServerApi api, Callback callback, JSONObject params) {
        Request request = null;

        switch (api) {
            case LOGIN:
                request = Login(params);
                break;
            case RELOGIN:
                request = ReLogin(params);
                break;
            case UPDATE_DRIVER_AVATAR:
                request = UpdateAvatar(params);
                break;
            case GET_DRIVER_LIST:
                request = GetDriverList(params);
                break;
            case UPDATE_DRIVER_STAT_BY_MGR:
                request = UpdateDriverStatusByMgr(params);
                break;
            case UPDATE_DRIVER_PHONE:
                request = UpdateDriverPhone(params);
                break;
            case UPDATE_DRIVER_NAME:
                request = UpdateDriverName(params);
                break;
            case UPDATE_DRIVER_STAT:
                request = UpdateDriverStatus(params);
                break;
            case HISTORY_ORDER_LIST:
                request = GetHistoryOrderList(params);
                break;
            case UPDATE_DRIVER_LOCATION:
                request = UpdateDriverLocation(params);
                break;
            case GET_ORDER_LIST:
                request = GetOrderList(params);
                break;
            case GET_ORDER_DETAIL:
                request = GetOrderDetail(params);
                break;
            case PICKUP_ORDER:
                request = PickUpOrder(params);
                break;
            case ACCEPT_ORDER:
                request = AcceptOrder(params);
                break;
            case FINISH_ORDER:
                request = FinishOrder(params);
                break;
            case GET_VERIFY_CODE:
                request = GetVerifyCode(params);
                break;
            case CHECK_VERIFY_CODE:
                request = CheckVerifyCode(params);
                break;
            case ASSIGN_ORDER_BY_MGR:
                request = AssignOrderByMgr(params);
                break;
            case GET_ORDER_LIST_MGR:
                request = GetOrderListByMgr(params);
                break;
            default:
        }

        if (request != null) {
            Call call = getInstant().newCall(request);
            call.enqueue(callback);
        } else {
            Log.e(TAG, "Http interface not exist");
        }
    }

    public static Request Login(String phone, String passwd) {
        RequestBody body = new FormBody.Builder()
                .add("phone_num", phone)
                .add("password", passwd)
                .build();
        return new Request.Builder().url(sServerAddr + sLogin).post(body).build();
    }

    private static Request Login(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("phone_num", params.getString("phone_num"))
                .add("password", params.getString("password"))
                .build();
        return new Request.Builder().url(sServerAddr + sLogin).post(body).build();
    }

    private static Request ReLogin(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("login_token", params.getString("login_token"))
                .build();
        return new Request.Builder().url(sServerAddr + sAutoLogin).post(body).build();
    }

    public static Request ReLogin(String token) {
        RequestBody body = new FormBody.Builder()
                .add("login_token", token)
                .build();
        return new Request.Builder().url(sServerAddr + sAutoLogin).post(body).build();
    }

    private static Request UpdateAvatar(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("base64string", params.getString("base64string"))
                .add("width", Integer.toString(params.getIntValue("width")))
                .add("height", Integer.toString(params.getIntValue("height")))
                .build();
        return new Request.Builder().url(sServerAddr + sAutoLogin).post(body).build();
    }

    private static Request GetDriverList(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .build();
        return new Request.Builder().url(sServerAddr + sDriverList).header("login_token", User.getInstant().getToken()).method("POST", body).build();
    }

    private static Request UpdateDriverStatusByMgr(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("driver_id", params.getString("driver_id"))
                .add("status", Integer.toString(params.getIntValue("status")))
                .build();
        return new Request.Builder().url(sServerAddr + sUpdDriverStatByMrg).header("login_token", User.getInstant().getToken()).post(body).build();
    }

    private static Request UpdateDriverName(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("name", params.getString("name"))
                .build();
        return new Request.Builder().url(sServerAddr + sUpdDriverName).header("login_token", User.getInstant().getToken()).post(body).build();
    }

    private static Request UpdateDriverPhone(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("phone_num", params.getString("phone_num"))
                .add("verification_code", params.getString("verification_code"))
                .build();
        return new Request.Builder().url(sServerAddr + sUpdDriverPhone).header("login_token", User.getInstant().getToken()).post(body).build();
    }

    private static Request UpdateDriverStatus(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("status", Integer.toString(params.getIntValue("status")))
                .build();
        return new Request.Builder().url(sServerAddr + sUpdDriverStat).header("login_token", User.getInstant().getToken()).post(body).build();
    }

    private static Request GetHistoryOrderList(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("start_timestamp", Long.toString(params.getLongValue("start_timestamp")))
                .add("end_timestamp", Long.toString(params.getLongValue("end_timestamp")))
                .build();
        return new Request.Builder().url(sServerAddr + sHistoryOrder).header("login_token", User.getInstant().getToken()).post(body).build();
    }

    private static Request UpdateDriverLocation(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("latitude", params.getString("latitude"))
                .add("longitude", params.getString("longitude"))
                .build();
        return new Request.Builder().url(sServerAddr + sUploadDriverLocation).header("login_token", User.getInstant().getToken()).post(body).build();
    }

    private static Request GetOrderList(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .build();
        return new Request.Builder()
                .url(sServerAddr + sOrderList)
                .header("login_token", User.getInstant().getToken())
                .post(body)
                .build();
    }

    private static Request GetOrderDetail(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("order_id", params.getString("order_id"))
                .build();
        return new Request.Builder()
                .url(sServerAddr + sOrderDetail)
                .header("login_token", User.getInstant().getToken())
                .post(body)
                .build();
    }

    private static Request AcceptOrder(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("order_id", params.getString("order_id"))
                .build();
        return new Request.Builder()
                .url(sServerAddr + sAcceptOrder)
                .header("login_token", User.getInstant().getToken())
                .post(body)
                .build();
    }

    private static Request PickUpOrder(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("order_id", params.getString("order_id"))
                .add("last2code", params.getString("last2code"))
                .build();
        return new Request.Builder()
                .url(sServerAddr + sPickUpOrder)
                .header("login_token", User.getInstant().getToken())
                .post(body)
                .build();
    }

    private static Request FinishOrder(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("order_id", params.getString("order_id"))
                .build();
        return new Request.Builder()
                .url(sServerAddr + sFinishOrder)
                .header("login_token", User.getInstant().getToken())
                .post(body)
                .build();
    }

    private static Request GetVerifyCode(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("phone_num", params.getString("phone_num"))
                .build();
        return new Request.Builder()
                .url(sServerAddr + sGetVerifyCode)
                .post(body)
                .build();
    }

    private static Request CheckVerifyCode(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("phone_num", params.getString("phone_num"))
                .add("verification_code", params.getString("verification_code"))
                .build();
        return new Request.Builder().url(sServerAddr + sCheckVerifyCode).post(body).build();
    }

    private static Request RefreshOrderCache(JSONObject params) {
        return new Request.Builder().url(sServerAddr + sRefreshOrderCache).build();
    }

    private static Request AssignOrderByMgr(JSONObject params) {
        RequestBody body = new FormBody.Builder()
                .add("order_id",params.getString("order_id"))
                .add("driver_id", params.getString("driver_id"))
                .build();
        return new Request.Builder().url(sServerAddr + sAssginOrderByMgr).header("login_token", User.getInstant().getToken()).post(body).build();
    }

    private static Request GetOrderListByMgr(JSONObject params) {
        RequestBody body = new FormBody.Builder().build();
        return new Request.Builder().url(sServerAddr + sGetOrderListByMgr).header("login_token", User.getInstant().getToken()).post(body).build();
    }
}
