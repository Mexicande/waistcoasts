package com.wuxiao.yourday.ui.fragment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuxiao.yourday.R;
import com.wuxiao.yourday.base.BaseFragment;
import com.wuxiao.yourday.event.RefreshRecordEvent;
import com.wuxiao.yourday.model.Category;
import com.wuxiao.yourday.model.Record;
import com.wuxiao.yourday.model.StatementListItem;
import com.wuxiao.yourday.ui.adapter.SimpleViewPagerAdapter;
import com.wuxiao.yourday.ui.adapter.StatementListAdapter;
import com.wuxiao.yourday.ui.dialog.SelectDateDialog;
import com.wuxiao.yourday.util.DateUtil;
import com.wuxiao.yourday.util.FormatUtil;
import com.wuxiao.yourday.util.greenDAO.RecordDao;
import com.wuxiao.yourday.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * 报表统计
 * Created by lihuabin on 2016/11/7.
 */
public class StatementFragment extends BaseFragment {
    CustomViewPager viewPager;
    View indicator;
    int screenWidth;
    RecyclerView list;
    TextView dateFromTv;
    TextView dateToTv;
    String dateFrom;
    String dateTo;
    PieChartView pieChartOfOutView;
    PieChartView pieChartOfInView;

    ArrayList<StatementListItem> statementOfOutData;
    ArrayList<StatementListItem> statementOfInData;
    StatementListAdapter adapterOfOut;
    StatementListAdapter adapterOfIn;
    List<Category> categories;//全部分类list
    double sumAllOfOut;//支出总金额
    private double sumAllOfIn;//收入总金额

    @Override
    public int getLayoutId() {
        return R.layout.fragment_statement;
    }

    @Override
    protected void assignViews(View view) {
        super.assignViews(view);
        viewPager = (CustomViewPager) view.findViewById(R.id.viewPager);
        list = (RecyclerView) view.findViewById(R.id.list);
        dateFromTv = (TextView) view.findViewById(R.id.date_from);
        dateToTv = (TextView) view.findViewById(R.id.date_to);
        indicator = view.findViewById(R.id.indicator);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        eventBus.register(this);
        dateFromTv.setText(DateUtil.getThisMonthFirstDay());
        dateToTv.setText(DateUtil.getToday());
        categories = app.getCategoryDao().queryBuilder().list();

        dateFrom = dateFromTv.getText().toString();
        dateTo = dateToTv.getText().toString();
        initIndicator();
        pieChartOfOutView = createPieChartView();
        pieChartOfInView = createPieChartView();
        adapterOfOut = new StatementListAdapter(mContext);
        adapterOfIn = new StatementListAdapter(mContext);
        refresh();

        dateFromTv.setOnClickListener(this);
        dateToTv.setOnClickListener(this);
        list.setLayoutManager(new LinearLayoutManager(mContext));
        list.setAdapter(adapterOfOut);

        List<View> viewList = new ArrayList<>();
        viewList.add(pieChartOfOutView);
        viewList.add(pieChartOfInView);

        viewPager.setAdapter(new SimpleViewPagerAdapter(mContext, viewList));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) indicator.getLayoutParams();

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0) {
                    return;
                }
                lp.leftMargin = (int) (positionOffset * (screenWidth / 2));
                indicator.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                System.out.println(position);
                if (position == 0) {
                    list.setAdapter(adapterOfOut);
                } else {
                    list.setAdapter(adapterOfIn);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.date_from:
                SelectDateDialog.newInstance("选择开始日期", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectDate = SelectDateDialog.getSelectDate();
                        if (!TextUtils.isEmpty(selectDate)) {
                            dateFrom = selectDate;
                        }
                        dateFromTv.setText(dateFrom);
                    }
                }).show(getChildFragmentManager(), "选择开始日期");
                break;
            case R.id.date_to:
                SelectDateDialog.newInstance("选择结束日期", dateFrom, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectDate = SelectDateDialog.getSelectDate();
                        if (!TextUtils.isEmpty(selectDate)) {
                            dateTo = selectDate;
                        }
                        dateToTv.setText(dateTo);
                        refresh();
                    }
                }).show(getChildFragmentManager(), "选择结束日期");
                break;
            default:
                break;
        }
    }

    void initIndicator() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) indicator.getLayoutParams();
        lp.width = screenWidth / 2;
        indicator.setLayoutParams(lp);
    }


    public PieChartView createPieChartView() {//创建饼图
        PieChartView pieChartView = new PieChartView(mContext);
        PieChartData pieChartdata = pieChartView.getPieChartData();
        pieChartdata.setHasLabels(false);//显示Labels
        pieChartdata.setValueLabelTextSize(14);
        //pieChartdata.setValueLabelsTextColor(Color.parseColor("#90FFFFFF"));lable字体颜色
        pieChartdata.setHasLabelsOnlyForSelected(true);//点击显示Labels
        pieChartdata.setHasLabelsOutside(true);//Labels是否显示在饼图外面
        pieChartdata.setHasCenterCircle(true);//是否是环形显示
        //pieChartdata.setValues(initOutPieChartValue());//填充数据
        pieChartdata.setCenterCircleColor(Color.WHITE);//设置环形中间的颜色
        pieChartdata.setCenterCircleScale(0.5f);//设置环形的大小级别
        //pieChartdata.setCenterText1(sumAllOfOut+"");//环形中间的文字1
        pieChartdata.setCenterText1Color(Color.BLACK);//文字颜色
        pieChartdata.setCenterText1FontSize(14);//文字大小
        //pieChartdata.setCenterText2("总支出");
        pieChartdata.setCenterText2Color(Color.BLACK);
        pieChartdata.setCenterText2FontSize(18);
        /**这里也可以自定义你的字体   Roboto-Italic.ttf这个就是你的字体库*/
//      Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Italic.ttf");
//      data.setCenterText1Typeface(tf);
        pieChartView.setPieChartData(pieChartdata);
        pieChartView.setValueSelectionEnabled(true);//选择饼图某一块变大
        pieChartView.setAlpha(0.9f);//设置透明度
        pieChartView.setCircleFillRatio(0.8f);//设置饼图大小
        pieChartView.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int arcIndex, SliceValue value) {
                list.scrollToPosition(arcIndex);
            }

            @Override
            public void onValueDeselected() {

            }
        });
        return pieChartView;
    }

    private void initOutPieChartValue() {//初始支出饼图
        PieChartData pieChartdata = pieChartOfOutView.getPieChartData();
        pieChartdata.setCenterText1(FormatUtil.getTwoDecimalString(sumAllOfOut));
        pieChartdata.setCenterText2("总支出");
        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < statementOfOutData.size(); i++) {
            StatementListItem item = statementOfOutData.get(i);
            String percentageNum = FormatUtil.getTwoDecimalString(item.getPercentage() * 100) + "%";
            SliceValue sliceValue = new SliceValue((float) item.getMoneyOfCategory(), getResources().getColor(item.getCategoryColor()));
            sliceValue.setLabel(item.getCategoryName() + "\r\n" + percentageNum);
            values.add(sliceValue);
        }
        pieChartdata.setValues(values);
        pieChartOfOutView.setPieChartData(pieChartdata);
    }

    private void initInPieChartValue() {//初始支出饼图
        PieChartData pieChartdata = pieChartOfInView.getPieChartData();
        pieChartdata.setCenterText1(FormatUtil.getTwoDecimalString(sumAllOfIn));
        pieChartdata.setCenterText2("总收入");
        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < statementOfInData.size(); i++) {
            StatementListItem item = statementOfInData.get(i);
            String percentageNum = FormatUtil.getTwoDecimalString(item.getPercentage() * 100) + "%";
            SliceValue sliceValue = new SliceValue((float) item.getMoneyOfCategory(), getResources().getColor(item.getCategoryColor()));
            sliceValue.setLabel(item.getCategoryName() + "\r\n" + percentageNum);
            values.add(sliceValue);
        }
        pieChartdata.setValues(values);
        pieChartOfInView.setPieChartData(pieChartdata);
        //return values;
    }

    private ArrayList<StatementListItem> getStatementOfOutData() {//获取支出数据
        ArrayList<StatementListItem> data = new ArrayList<>();
        String where = "\"create_date\" between \"" + dateFrom + "\" and \"" + dateTo + "\"";
        String andWhere = "and \"OUT_OR_IN\" = " + Record.OUT;
        Cursor cursorAll = app.customQuery("SUM(\"money\")", RecordDao.TABLENAME, where, andWhere);
        sumAllOfOut = cursorAll.getDouble(0);
        cursorAll.close();
        for (Category c : categories) {
            Cursor cursor = app.customQuery("SUM(\"money\")", RecordDao.TABLENAME, "\"CATEGORY_ID\" = " + c.getCategoryId() + " AND " + where, andWhere);
            //statementData.put(c.getCategoryName(),cursor.getDouble(0));
            double sumCategory = cursor.getDouble(0);
            if (sumCategory == 0) {//当前分类金额为0时不显示
                continue;
            }
            StatementListItem item = new StatementListItem(c.getCategoryName(), c.getCategoryColor(), cursor.getDouble(0), (float) (sumCategory / sumAllOfOut));
            data.add(item);
            cursor.close();
        }
        return data;
    }

    private ArrayList<StatementListItem> getStatementOfInData() {
        ArrayList<StatementListItem> data = new ArrayList<>();
        String where = "\"create_date\" between \"" + dateFrom + "\" and \"" + dateTo + "\"";
        String andWhere = "and \"OUT_OR_IN\" = " + Record.IN;
        Cursor cursorAll = app.customQuery("SUM(\"money\")", RecordDao.TABLENAME, where, andWhere);
        sumAllOfIn = cursorAll.getDouble(0);
        cursorAll.close();
        for (Category c : categories) {
            Cursor cursor = app.customQuery("SUM(\"money\")", RecordDao.TABLENAME, "\"CATEGORY_ID\" = " + c.getCategoryId() + " AND " + where, andWhere);
            //statementData.put(c.getCategoryName(),cursor.getDouble(0));
            double sumCategory = cursor.getDouble(0);
            if (sumCategory == 0) {//当前分类金额为0时不显示
                continue;
            }
            StatementListItem item = new StatementListItem(c.getCategoryName(), c.getCategoryColor(), cursor.getDouble(0), (float) (sumCategory / sumAllOfIn));
            data.add(item);
            cursor.close();
        }
        return data;
    }

    public void refresh() {
        statementOfOutData = getStatementOfOutData();
        adapterOfOut.setData(statementOfOutData);
        initOutPieChartValue();
        statementOfInData = getStatementOfInData();
        adapterOfIn.setData(statementOfInData);
        initInPieChartValue();
        if (viewPager.getCurrentItem() == 0) {
            list.setAdapter(adapterOfOut);
        } else {
            list.setAdapter(adapterOfIn);
        }

    }

    public void onEventMainThread(RefreshRecordEvent event) {
        refresh();
    }

}
