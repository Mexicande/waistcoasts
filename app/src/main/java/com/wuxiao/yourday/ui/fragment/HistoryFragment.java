package com.wuxiao.yourday.ui.fragment;

import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.wuxiao.yourday.R;
import com.wuxiao.yourday.base.BaseFragment;
import com.wuxiao.yourday.event.RefreshRecordEvent;
import com.wuxiao.yourday.model.Record;
import com.wuxiao.yourday.ui.adapter.RecordListItemAdapter;
import com.wuxiao.yourday.util.DateUtil;
import com.wuxiao.yourday.util.greenDAO.RecordDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 历史记录
 * Created by lihuabin on 2016/11/7.
 */
public class HistoryFragment extends BaseFragment implements OnDateSelectedListener {
    MaterialCalendarView calendarView;
    RecyclerView list;
    RecordListItemAdapter adapter;
    List<Record> data;
    ArrayList<String> hasRecordDayList;
    DayViewDecorator hasRecordDecor;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    protected void assignViews(View view) {
        super.assignViews(view);
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendar);
        list = (RecyclerView) view.findViewById(R.id.record_list);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        eventBus.register(this);
        hasRecordDayList = new ArrayList<>();

        calendarView.setOnDateChangedListener(this);
        hasRecordDecor = new DayViewDecorator() {//日历上标记存在记录的时间
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return hasRecordDayList.contains(DateUtil.formatDay(day.getDate()));
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.addSpan(new DotSpan(mContext.getResources().getColor(R.color.colorAccent)));
                view.addSpan(new StyleSpan(Typeface.BOLD));
                view.addSpan(new RelativeSizeSpan(1.4f));
            }
        };
        calendarView.addDecorator(hasRecordDecor);
        calendarView.setSelectedDate(Calendar.getInstance().getTime());
        initHasRecordDay();
        //calendarView.refreshDrawableState();
        list.setLayoutManager(new LinearLayoutManager(mContext));
        data = getData(DateUtil.getToday());
        adapter = new RecordListItemAdapter(mContext, data);
        list.setAdapter(adapter);

    }

    private void initHasRecordDay() {//初始画有记录日子的数组
        hasRecordDayList.removeAll(hasRecordDayList);
        Cursor c = app.customQuery("distinct CREATE_DATE", RecordDao.TABLENAME, null);
        if (c.getCount() > 0) {
            hasRecordDayList.add(c.getString(0));//执行上一行后c游标已经在第一行
            while (c.moveToNext()) {
                hasRecordDayList.add(c.getString(0));
            }
        }
    }

    public void goToday() {
        calendarView.setCurrentDate(Calendar.getInstance());
        calendarView.setSelectedDate(Calendar.getInstance().getTime());
        adapter.setData(getData(DateUtil.getToday()));
    }

    private List<Record> getData(String whichDay) {
        RecordDao recordDao = app.getRecordDao();
        List<Record> data = recordDao.queryBuilder().where(RecordDao.Properties.CreateDate.eq(whichDay)).orderDesc(RecordDao.Properties.CreateTime).list();
        return data;
    }

    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
        data = getData(DateUtil.formatDay(date.getDate()));
        adapter.setData(data);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getHoldingActivity().getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_today:
                Toast.makeText(mContext, "go today2", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(RefreshRecordEvent event) {
        initHasRecordDay();
        calendarView.removeDecorators();
        calendarView.addDecorator(hasRecordDecor);
        data = getData(DateUtil.formatDay(calendarView.getSelectedDate().getDate()));
        adapter.setData(data);
    }
}
