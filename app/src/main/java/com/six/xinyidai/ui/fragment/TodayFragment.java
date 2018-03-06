package com.six.xinyidai.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.six.xinyidai.R;
import com.six.xinyidai.base.BaseFragment;
import com.six.xinyidai.event.RefreshBudgetEvent;
import com.six.xinyidai.event.RefreshRecordEvent;
import com.six.xinyidai.model.Record;
import com.six.xinyidai.ui.activity.AddRecordActivity;
import com.six.xinyidai.ui.adapter.RecordListItemAdapter;
import com.six.xinyidai.util.DateUtil;
import com.six.xinyidai.util.FormatUtil;
import com.six.xinyidai.util.greenDAO.RecordDao;
import com.six.xinyidai.view.SetBudgetDialog;

import java.util.List;

/**
 * 今日
 * Created by lihuabin on 2016/10/20.
 */
public class TodayFragment extends BaseFragment {
    TextView budgetBalance;
    TextView budgetBalanceNum;
    SetBudgetDialog budgetDialog;//设置预算弹窗
    String bbNum;//设置的预算
    TextView inSumTv;
    TextView outSumTv;
    RecyclerView list;
    TextView todaySum;
    TextView textData;
    FloatingActionButton fab;
    RecordListItemAdapter adapter;
    int i = 0;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_today;
    }

    @Override
    protected void assignViews(View view) {
        super.assignViews(view);
        budgetBalance = (TextView) view.findViewById(R.id.budget_balance);
        budgetBalanceNum = (TextView) view.findViewById(R.id.budget_balance_num);
        inSumTv = (TextView) view.findViewById(R.id.text_in_sum);
        outSumTv = (TextView) view.findViewById(R.id.text_out_sum);
        list = (RecyclerView) view.findViewById(R.id.list);
        todaySum = (TextView) view.findViewById(R.id.today_sum);
        textData = (TextView) view.findViewById(R.id.text_date);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        eventBus.register(this);
        textData.setText(DateUtil.getToday());
        setTopBarNum();
        fab.setOnClickListener(this);
        budgetBalance.setOnClickListener(this);
        budgetBalanceNum.setOnClickListener(this);
        final List<Record> data = getData();
        adapter = new RecordListItemAdapter(mContext, data);
        list.setAdapter(adapter);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(mContext));
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!list.canScrollVertically(-1)) {//滑倒顶部则返回false
                    fab.setAlpha(1f);
                } else if (!list.canScrollVertically(1)) {//滑倒低部则返回false{
                    fab.setAlpha(0.3f);
                } else {
                    fab.setAlpha(1f);
                }
            }
        });
        budgetDialog= new SetBudgetDialog(mContext);
    }

    private void setTopBarNum() {
        double inSumNum;//本月收入金额
        double outSumNum;//本月支出金额
        double finalNum;//本月预算余额
        String whereOfIn = "\"OUT_OR_IN\" =" + Record.IN;
        String whereOfOut = "\"OUT_OR_IN\" =" + Record.OUT;
        String andWhere = "and \"create_date\" between \"" + DateUtil.getThisMonthFirstDay() + "\" and \"" + DateUtil.getToday() + "\"";
        Cursor cursor = app.customQuery("sum(\"money\")", RecordDao.TABLENAME, whereOfIn, andWhere);
        inSumNum = cursor.getDouble(0);
        inSumTv.setText(FormatUtil.getTwoDecimalString(inSumNum));
        cursor.close();
        Cursor cursor2 = app.customQuery("sum(\"money\")", RecordDao.TABLENAME, whereOfOut, andWhere);
        outSumNum = cursor2.getDouble(0);
        outSumTv.setText(FormatUtil.getTwoDecimalString(outSumNum));
        cursor.close();
        Cursor cursor3 = app.customQuery("sum(\"money\")", RecordDao.TABLENAME, whereOfIn, "and \"create_date\" =\"" + DateUtil.getToday() + "\"");
        Cursor cursor4 = app.customQuery("sum(\"money\")", RecordDao.TABLENAME, whereOfOut, "and \"create_date\" =\"" + DateUtil.getToday() + "\"");
        double todayBalance = cursor3.getDouble(0) - cursor4.getDouble(0);
        if (todayBalance < 0) {
            todaySum.setTextColor(getResources().getColor(R.color.outTextColor));
            todayBalance = 0 - todayBalance;
        }
        todaySum.setText(FormatUtil.getTwoDecimalString(todayBalance));
        bbNum = app.getBudget();
        finalNum = Double.parseDouble(bbNum) + inSumNum - outSumNum;
        if (finalNum <= 0) {
            budgetBalanceNum.setTextColor(getResources().getColor(R.color.outTextColor));
        }

        budgetBalanceNum.setText(FormatUtil.getTwoDecimalString(finalNum));
    }

    private List<Record> getData() {
        RecordDao recordDao = app.getRecordDao();
        List<Record> data = recordDao.queryBuilder().where(RecordDao.Properties.CreateDate.eq(DateUtil.getToday())).orderDesc(RecordDao.Properties.CreateTime).list();
        return data;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.budget_balance://预算
            case R.id.budget_balance_num:
                budgetDialog.show();
                break;
            case R.id.fab://添加记录
                startActivity(new Intent(mContext, AddRecordActivity.class));
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(RefreshRecordEvent event) {
        setTopBarNum();
        adapter.setData(getData());
    }

    public void onEventMainThread(RefreshBudgetEvent event) {
        setTopBarNum();
    }
}