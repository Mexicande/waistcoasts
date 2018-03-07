package com.wuxiao.yourday.ui.fragment;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.wuxiao.yourday.MainActivity;
import com.wuxiao.yourday.R;
import com.wuxiao.yourday.base.BaseFragment;
import com.wuxiao.yourday.event.MainSetCurrentEvent;
import com.wuxiao.yourday.event.RefreshBudgetEvent;
import com.wuxiao.yourday.event.RefreshRecordEvent;
import com.wuxiao.yourday.event.RefreshUserInfoEvent;
import com.wuxiao.yourday.view.SetBudgetDialog;

/**
 * 用户中心
 * Created by lihuabin on 2016/11/10.
 */
public class UserCentreFragment extends BaseFragment {
    TextView recordSumTv;
    TextView categorySumTv;
    TextView recordNumTitleTv;
    TextView categoryNumTitleTv;
    LinearLayout llSetBudget;
    TextView budgetBalanceNum;//预算金额
    Button signOutBtn;
    Button deleteAll;

    AlertDialog deleteConfirmDialog;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_centre;
    }

    @Override
    protected void assignViews(View view) {
        super.assignViews(view);
        recordSumTv = (TextView) view.findViewById(R.id.record_sum);
        categorySumTv = (TextView) view.findViewById(R.id.category_sum);
        signOutBtn = (Button) view.findViewById(R.id.btn_sign_out);
        deleteAll = (Button) view.findViewById(R.id.delete_all);
        recordNumTitleTv = (TextView) view.findViewById(R.id.text_record_num);
        categoryNumTitleTv = (TextView) view.findViewById(R.id.text_category_num);
        llSetBudget= (LinearLayout) view.findViewById(R.id.set_budget);
        budgetBalanceNum = (TextView) view.findViewById(R.id.budget_balance_num);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        eventBus.register(this);
        setRecordSum();
        setCategorySum();
        updateUserInfo();
        budgetBalanceNum.setText(app.getBudget());
        signOutBtn.setOnClickListener(this);
        deleteAll.setOnClickListener(this);
        recordSumTv.setOnClickListener(this);
        categorySumTv.setOnClickListener(this);
        recordNumTitleTv.setOnClickListener(this);
        categoryNumTitleTv.setOnClickListener(this);
        llSetBudget.setOnClickListener(this);
        deleteConfirmDialog = new AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.notice))
                .setMessage(getString(R.string.delete_all_record_tip))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        app.getRecordDao().deleteAll();
                        Toast.makeText(mContext,"清空成功",Toast.LENGTH_SHORT).show();
                        eventBus.post(new RefreshRecordEvent());
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private void updateUserInfo() {
        if (!app.isLogin()) {
            signOutBtn.setVisibility(View.GONE);
        }
        // TODO: 2016/11/10  头像
    }

    private void setCategorySum() {
        categorySumTv.setText(app.getCategoryDao().count() + "");
    }

    private void setRecordSum() {
        recordSumTv.setText(app.getRecordDao().count() + "");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_sign_out:
                FirebaseAuth.getInstance().signOut();
                app.cleanUser();
                eventBus.post(new RefreshUserInfoEvent());
                Toast.makeText(mContext, "注销成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.user_icon:

            case R.id.delete_all:
                deleteConfirmDialog.show();
                break;
            case R.id.record_sum:
            case R.id.text_record_num:
                eventBus.post(new MainSetCurrentEvent(MainActivity.HISTORY,4));
                break;
            case R.id.category_sum:
            case R.id.text_category_num:
                Toast.makeText(mContext,getString(R.string.add_on_new_version_tip,getString(R.string.category_manager_title)),Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_budget://设置预算
                new SetBudgetDialog(mContext).show();
                break;
            default:
                break;
        }
    }


    public void onEventMainThread(RefreshUserInfoEvent event) {
        updateUserInfo();
    }

    public void onEventMainThread(RefreshRecordEvent event) {
        setRecordSum();
        setCategorySum();
    }
    public void onEventMainThread(RefreshBudgetEvent event) {
        budgetBalanceNum.setText(app.getBudget());
    }
}
