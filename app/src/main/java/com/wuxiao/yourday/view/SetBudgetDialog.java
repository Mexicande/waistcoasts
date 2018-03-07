package com.wuxiao.yourday.view;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wuxiao.yourday.App;
import com.wuxiao.yourday.R;
import com.wuxiao.yourday.event.RefreshBudgetEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by lihuabin on 2016/12/12.
 */
public class SetBudgetDialog {
    EditText budgetEdt;
    Context mContext;
    App app;
    Handler handler = new Handler();
    AlertDialog dialog;
    InputMethodManager imm;

    public SetBudgetDialog(Context context) {
        //super(context);
        init(context);
    }

    protected SetBudgetDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        //super(context, cancelable, cancelListener);
        init(context);
    }

    protected SetBudgetDialog(@NonNull Context context, @StyleRes int themeResId) {
        //super(context, themeResId);
        init(context);
    }

    void init(Context context) {
        mContext = context;
        app = (App) context.getApplicationContext();
        imm = (InputMethodManager) mContext.getSystemService(Service.INPUT_METHOD_SERVICE);
        View contentView = View.inflate(mContext, R.layout.dialog_set_budget, null);
        budgetEdt = (EditText) contentView.findViewById(R.id.edt_budget);
        budgetEdt.setSingleLine();
        budgetEdt.setHint(app.getBudget() + "");
        Button positiveButton = (Button) contentView.findViewById(R.id.btn_positive);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ssNum = budgetEdt.getText().toString();
                if (!TextUtils.isEmpty(ssNum)) {
                    try {
                        float newbbNum = Float.parseFloat(ssNum);
                        if (newbbNum < 0) {
                            throw new NumberFormatException();//小于0跳转到catch
                        }
                        app.setBudget(newbbNum);
                        EventBus.getDefault().post(new RefreshBudgetEvent());
                        Toast.makeText(mContext, mContext.getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } catch (NumberFormatException e) {
                        Toast.makeText(mContext, mContext.getString(R.string.please_input_the_correct_amount), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, mContext.getString(R.string.please_input_the_correct_amount), Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog = new AlertDialog.Builder(mContext)
                .setTitle(R.string.set_budget)
                .setView(contentView)
                .create();
    }


    public void show() {
        budgetEdt.setText(null);
        budgetEdt.setHint(app.getBudget());
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
