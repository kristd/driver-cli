package com.app.onemenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ReportFragment extends Fragment implements DataCallBack {
    private static FragmentManager mfragmentMrg;
    private TextView mTextViewReportDates;
    private TextView date_period;
    private TextView total_tips;
    private TextView total_orders;

    private View progressView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ReportRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<ReportItem> reportList = new ArrayList<>();
    private StartDatePickerFragment startDatePickerFragment;
    private Summary summary;

    private static String TAG = "ReportFragment";
    private ReportTimeStamp timeStamp;

    private class ReportTimeStamp {
        private long mStart;
        private long mEnd;

        public ReportTimeStamp(long start, long end) {
            this.mStart = start;
            this.mEnd = end;
        }

        public void setEnd(long mEnd) {
            this.mEnd = mEnd;
        }

        public void setStart(long mStart) {
            this.mStart = mStart;
        }

        public long getEnd() {
            return mEnd;
        }

        public long getStart() {
            return mStart;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MessageId.GET_REPORT_LIST_COMPLETED) {
                date_period = (TextView) getActivity().findViewById(R.id.report_date_period);
                date_period.setText("           Date Period     " + summary.getTimePeriod());

                total_orders = (TextView) getActivity().findViewById(R.id.report_total_order);
                total_orders.setText("           Total Orders    " + summary.getTotalCount());

                total_tips = (TextView) getActivity().findViewById(R.id.report_total_tips);
                total_tips.setText("           Total Tips        " + summary.getTotalTips());


                swipeRefreshLayout.setVisibility(View.VISIBLE);
                recyclerView.getAdapter().notifyDataSetChanged();

                //Toast.makeText(getActivity(), "get report list success", Toast.LENGTH_SHORT).show();
            } else if (msg.what == MessageId.GET_REPORT_LIST_FAILED) {
                //Toast.makeText(getActivity(), "get report list failed", Toast.LENGTH_SHORT).show();
            }

            showProgress(false);
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    Callback getRepotListCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String data = response.body().string();
            Log.i(TAG, data);

            JSONObject jsonObject = (JSONObject) JSON.parse(data);
            if (jsonObject != null && jsonObject.getString("status").equals(ErrorCode.Success)) {
                Log.i(TAG, "get history reports success");

                initReportData(jsonObject);
                Message msg = new Message();
                msg.what = MessageId.GET_REPORT_LIST_COMPLETED;
                handler.sendMessage(msg);
            } else {
                Log.e(TAG, "get history reports failed");
                Message msg = new Message();
                msg.what = MessageId.GET_REPORT_LIST_FAILED;
                handler.sendMessage(msg);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        timeStamp = new ReportTimeStamp(0, 0);
        progressView = getActivity().findViewById(R.id.report_get_report_progress);
        startDatePickerFragment = new StartDatePickerFragment();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView_report);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReportRecyclerViewAdapter(reportList, getActivity());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_report);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startDatePickerFragment.show(mfragmentMrg, "startDatePicker");
                showProgress(true);
            }
        });

        mTextViewReportDates = (TextView) getActivity().findViewById(R.id.tv_reportDates);
        mTextViewReportDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewReportDates.setVisibility(View.GONE);
                showProgress(true);
                swipeRefreshLayout.setVisibility(View.VISIBLE);

                //第一个参数为FragmentManager对象, 第二个为调用该方法的fragment的标签
                startDatePickerFragment.show(mfragmentMrg, "startDatePicker");

                //Fragment f = mfragmentMrg.findFragmentById(R.id.layFrame);
                //Log.i("mainFragment", ">>> " + f);

//                swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_report);
//                swipeRefreshLayout.setVisibility(View.VISIBLE);
//                swipeRefreshLayout.setColorSchemeResources(R.color.blue);
//                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//                        //request report data
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                });
//
//                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//                recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView_report);
//                recyclerView.setVisibility(View.VISIBLE);
//
//                initReportData();
//                adapter = new ReportRecyclerViewAdapter(reportList, getActivity());
//
//                recyclerView.setHasFixedSize(true);
//                recyclerView.setLayoutManager(layoutManager);
//                recyclerView.setAdapter(adapter);
            }
        });
    }

    public static ReportFragment newInstance(String content, FragmentManager fragmentManager) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        ReportFragment fragment = new ReportFragment();
        fragment.setArguments(args);
        mfragmentMrg = fragmentManager;
        return fragment;
    }

    //获取选择的时间回调
    @Override
    public void getData(long start, long end) {
        //initReportData(null);
        //adapter = new ReportRecyclerViewAdapter(reportList, getActivity());
        //recyclerView.setAdapter(adapter);

        timeStamp.setStart(start);
        timeStamp.setEnd(end);

        Map<String, Long> values = new ConcurrentHashMap<>();
        values.put("start_timestamp", start);
        values.put("end_timestamp", end);
        JSONObject params = JSONObject.parseObject(JSON.toJSONString(values));
        HttpApi.NewRequest(HttpApi.POST, HttpApi.ServerApi.HISTORY_ORDER_LIST, getRepotListCallback, params);
    }

    private void initReportData(JSONObject jsonObject) {
        //reportList = new ArrayList<>();
        reportList.clear();

        try {
            JSONObject sum = jsonObject.getJSONObject("data").getJSONObject("summary");
            summary = new Summary(sum.getIntValue("order_total_count"), sum.getDoubleValue("total_tips_fee"), sum.getString("time_period"));
        } catch (Exception ex) {
            summary = new Summary(0, 0.00, "1900/01/01-2017/01/01");
        }

        JSONArray objectArray = jsonObject.getJSONObject("data").getJSONArray("report_list");
        for (int i = 0; i < objectArray.size(); i++) {
            JSONObject object = objectArray.getJSONObject(i);

            ReportItem item = new ReportItem(
                    object.getString("order_id"),
                    object.getString("order_code"),
                    object.getDoubleValue("tips_fee"),
                    object.getIntValue("tips_type"),
                    object.getString("date"),
                    object.getIntValue("used_time"));

            reportList.add(item);
        }
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
