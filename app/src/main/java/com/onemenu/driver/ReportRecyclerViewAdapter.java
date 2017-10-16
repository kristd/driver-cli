package com.onemenu.driver;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ReportRecyclerViewAdapter extends RecyclerView.Adapter<ReportRecyclerViewAdapter.NewsViewHolder> {
    private List<ReportItem> reports;
    private Context context;

    public ReportRecyclerViewAdapter(List<ReportItem> reports, Context context) {
        this.reports = reports;
        this.context = context;
    }

    //自定义ViewHolder类
    static class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView orderId;
        TextView orderCode;
        TextView tipsFee;
        TextView tipsType;
        TextView date;
        TextView usedTime;

        public NewsViewHolder(final View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_report);
            orderId = (TextView) itemView.findViewById(R.id.order_id_report);
            orderCode = (TextView) itemView.findViewById(R.id.order_code_report);
            tipsFee = (TextView) itemView.findViewById(R.id.tips_free_report);
            tipsType = (TextView) itemView.findViewById(R.id.tips_type_report);
            date = (TextView) itemView.findViewById(R.id.deal_date_report);
            usedTime = (TextView) itemView.findViewById(R.id.used_time_report);
        }
    }

    @Override
    public ReportRecyclerViewAdapter.NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.report_item, viewGroup, false);
        ReportRecyclerViewAdapter.NewsViewHolder nvh = new ReportRecyclerViewAdapter.NewsViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(ReportRecyclerViewAdapter.NewsViewHolder viewHolder, int i) {
        final int index = i;

        viewHolder.orderId.setText(reports.get(index).getOrderId());
        String orderCode = reports.get(index).getOrderCode();
        viewHolder.orderCode.setText("Code              " + orderCode.substring(orderCode.length()-5, orderCode.length()-2) + "-" + orderCode.substring(orderCode.length()-2, orderCode.length()));
        viewHolder.tipsFee.setText("Tips Free       $" + String.valueOf(reports.get(index).getTipsFee()));
        viewHolder.tipsType.setText(String.valueOf(reports.get(index).getTipsType()));
        viewHolder.date.setText("Date               " + reports.get(index).getDate());
        viewHolder.usedTime.setText("Order Used    " + String.valueOf(reports.get(index).getUsedTime()));
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }
}
