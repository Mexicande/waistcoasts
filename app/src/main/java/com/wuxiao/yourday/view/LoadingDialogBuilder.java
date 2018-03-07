package com.wuxiao.yourday.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wuxiao.yourday.R;

/**
 * Created by lihuabin on 2016/11/9.
 */
public class LoadingDialogBuilder {
    static Context mContext;
    static boolean mCancelable;
    static boolean mCanceledOnTouchOutside;
    static DialogInterface.OnCancelListener mOnCancelListener;
    static boolean mHasTip;
    static AppCompatDialog mDialog;
    static String mTip = "加载中";


    public LoadingDialogBuilder(Builder builder) {
        mContext = builder.context;
        mCancelable = builder.cancelable;
        mCanceledOnTouchOutside = builder.canceledOnTouchOutside;
        mOnCancelListener = builder.onCancelListener;
        mHasTip = builder.hasTip;
        mTip = builder.tip;
    }

    public static AppCompatDialog createDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null, false);
        TextView tipTv = (TextView) v.findViewById(R.id.loading_text);
        if (mHasTip) {
            tipTv.setVisibility(View.VISIBLE);
            tipTv.setText(mTip);
        } else {
            tipTv.setVisibility(View.GONE);
        }
        mDialog = new AppCompatDialog(mContext);
        mDialog.setCancelable(mCancelable);
        mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
        mDialog.setOnCancelListener(mOnCancelListener);
        mDialog.setContentView(v);
        return mDialog;
    }

    public static void showDialog() {
        if (mDialog == null) {
            mDialog = createDialog();
        }
        mDialog.show();
    }

    public static void hideDialog() {
        if (mDialog == null) {
            return;
        }
        if (mDialog.isShowing()) mDialog.dismiss();
    }

    public static class Builder {
        Context context;
        boolean cancelable =false;
        boolean canceledOnTouchOutside =false;
        boolean darkWhenDialogShow;
        DialogInterface.OnCancelListener onCancelListener;
        boolean hasTip =true;
        int dialogStyle;
        String tip;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder setDarkWhenDialogShow(boolean darkWhenDialogShow) {
            this.darkWhenDialogShow = darkWhenDialogShow;
            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.onCancelListener = onCancelListener;
            return this;
        }

        public Builder setHasTip(boolean hasTip) {
            this.hasTip = hasTip;
            return this;
        }

        public Builder setDialogStyle(int dialogStyle) {
            this.dialogStyle = dialogStyle;
            return this;
        }

        public Builder setTip(String tip) {
            this.tip = tip;
            return this;
        }

        public AppCompatDialog build() {
            return new LoadingDialogBuilder(this).createDialog();
        }

    }

}
