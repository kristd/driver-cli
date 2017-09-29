package com.app.onemenu;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

public class EndDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private long startDate;
    private long endDate;
    private Fragment mainFragment;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //得到Calendar类实例，用于获取当前时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //返回DatePickerDialog对象
        //因为实现了OnDateSetListener接口，所以第二个参数直接传入this
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day) {
            @Override
            protected void onStop() {}
        };

        datePickerDialog.setTitle("END DATE");
        return datePickerDialog;
    }

    //实现OnDateSetListener接口的onDateSet()方法
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //这样子写就将选择时间的fragment和选择日期的fragment完全绑定在一起
        //使用的时候只需直接调用DatePickerFragment的show()方法
        //即可选择完日期后选择时间
        //TimePickerFragment timePicker = new TimePickerFragment();
        //timePicker.show(getFragmentManager(), "endDatePicker");
        //将用户选择的日期传到TimePickerFragment
        //endDate = year + "年" + (monthOfYear+1) + "月" + dayOfMonth + "日";
        //timePicker.setTime(date);
        //判断activity是否是DataCallBack(这是自己定义的一个接口)的一个实例
        FragmentManager manager = getFragmentManager();
        mainFragment = manager.findFragmentById(R.id.layFrame);

        if(mainFragment instanceof DataCallBack){
            //将activity强转为DataCallBack
            DataCallBack dataCallBack = (DataCallBack) mainFragment;
            //endDate = year + "年" + (monthOfYear+1) + "月" + dayOfMonth + "日";
            //调用activity的getData方法将数据传回activity显示

            Calendar c = Calendar.getInstance();
            c.set(year, monthOfYear, dayOfMonth, 8, 0, 0);
            endDate = c.getTimeInMillis();

            dataCallBack.getData(startDate, endDate);
        }
    }

    public void setStartDate(long start) {
        startDate = start;
    }
}

