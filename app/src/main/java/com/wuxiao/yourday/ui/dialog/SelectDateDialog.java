package com.wuxiao.yourday.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.wuxiao.yourday.R;
import com.wuxiao.yourday.util.DateUtil;

import java.util.Date;

/**
 * 选择日期弹窗
 * Created by lihuabin on 2016/11/7.
 */
public class SelectDateDialog extends AppCompatDialogFragment implements OnDateSelectedListener {

    private static TextView textView;
    static DialogInterface.OnClickListener ClickListener;
    static String titleString;
    static Date minimumDate;

    public static SelectDateDialog newInstance(String title) {
        SelectDateDialog frag = new SelectDateDialog();
        minimumDate = null;
        titleString=title;
        return frag;
    }

    public static SelectDateDialog newInstance(String title, DialogInterface.OnClickListener PositiveBtnClickListener) {
        SelectDateDialog frag = new SelectDateDialog();
        titleString=title;
        minimumDate = null;
        ClickListener = PositiveBtnClickListener;
        return frag;
    }

    public static SelectDateDialog newInstance(String title, String minDate, DialogInterface.OnClickListener PositiveBtnClickListener) {
        SelectDateDialog frag = new SelectDateDialog();
        titleString=title;
        minimumDate = DateUtil.getDateFromString(minDate);
        ClickListener = PositiveBtnClickListener;
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //inflate custom layout and get views
        //pass null as parent view because will be in dialog layout
        View view = inflater.inflate(R.layout.dialog_select_date, null);
        textView = (TextView) view.findViewById(R.id.textView);
        MaterialCalendarView widget = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        if(minimumDate!=null)widget.state().edit().setMinimumDate(minimumDate).commit();
        widget.setOnDateChangedListener(this);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(titleString)
                .setView(view)
                .setPositiveButton(android.R.string.ok, ClickListener)
                .create();
        return dialog;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        textView.setText(DateUtil.formatDay(date.getDate()));
    }
    public static String getSelectDate(){
        return textView.getText().toString();
    }
}