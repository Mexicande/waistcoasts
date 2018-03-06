package com.six.xinyidai.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lihuabin on 2016/10/12.
 * 倒计时控件
 */
public class CountdownView extends TextView {

    private Context context;
    private int hour = 0;
    private int minute = 0;
    private int second = 60;
    private int sumSecond = 0;
    private boolean isStart = true;
    private boolean canExceeded = false;//用于控制是否可以溢出
    private int exceededTextColor = Color.RED; //用于控制超时后的文本颜色
    private OnTimeOutListen mOnTimeOutListen;

    public CountdownView(Context context) {
        super(context);
        init(context);
    }

    public CountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CountdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CountdownView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    //倒计时实现主要逻辑部分
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isStart) {
                sumSecond--;
            }
            setTextTime(sumSecond);
            if (sumSecond > 0 && isStart) {
                handler.postDelayed(this, 1000);
            } else if (sumSecond <= 0 && isStart && canExceeded) {
                handler.postDelayed(this, 1000);
            }
            if (mOnTimeOutListen != null) {
                if (sumSecond == 0 && isStart) {
                    mOnTimeOutListen.onTimeOut();
                }
                int tempHour = sumSecond / (60 * 60);
                int tempMinute = sumSecond / 60 - (tempHour * 60);
                int tempSecond = sumSecond % 60;
                mOnTimeOutListen.onTimeLapses(tempHour, tempMinute, tempSecond);
            }
        }
    };

    /**
     * 启动倒计时
     */
    public void start() {
        if (!isStart) {
            this.isStart = true;
            handler.post(runnable);
        }
    }

    /**
     * 暂停倒计时
     */
    public void stop() {
        this.isStart = false;
    }

    /**
     * 设置倒计时的初始时间
     *
     * @param hour
     * @param minute
     * @param second
     */
    public void setTime(int hour, int minute, int second) {
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
        if (hour > 0) {
            this.hour = hour;
        }
        if (minute >= 0 && minute < 60) {
            this.minute = minute;
        }
        if (second >= 0 && second < 60) {
            this.second = second;
        }
        sumSecond = hour * 60 * 60 + minute * 60 + second;
        setTextTime(sumSecond);
        handler.postDelayed(runnable, 1000);
    }

    /**
     * 格式化时间 显示
     *
     * @param sumSecond
     */
    private void setTextTime(int sumSecond) {
        int ih = sumSecond / (60 * 60);
        int im = (sumSecond / 60) - (ih * 60);
        int is = sumSecond - ih * 60 * 60 - im * 60;
        if (!canExceeded) {
            if (ih <= 0) {
                ih = 0;
            }
            if (im <= 0) {
                im = 0;
            }
            if (is <= 0) {
                is = 0;
            }
        }
        String h = ih > 9 || ih < -9 ? "" + Math.abs(ih) : "0" + Math.abs(ih);
        String m = im > 9 || im < -9 ? "" + Math.abs(im) : "0" + Math.abs(im);
        String s = is > 9 || is < -9 ? "" + Math.abs(is) : "0" + Math.abs(is);
        setText(h + ":" + m + ":" + s);
        if(sumSecond<0){
            setTextColor(exceededTextColor);
        }
    }

    /**
     * 设置是否可以显示超时
     */
    public void setCanExceeded(boolean canExceeded) {
        this.canExceeded = canExceeded;
    }

    /**
     *设置超时之后文本颜色
     */
    public void setExceededTextColor(int color){
        this.exceededTextColor = color;
    }

    /**
     * 获取超时之后的文本颜色
     */
    public int getExceededTextColor() {
        return exceededTextColor;
    }

    /**
     * 为倒计时设置监听器
     *
     * @param onTimeOutListen
     */
    public void setOnTimeOutListen(OnTimeOutListen onTimeOutListen) {
        if (onTimeOutListen != null) {
            this.mOnTimeOutListen = onTimeOutListen;
        }
    }

    /**
     * 时间变化监听器;
     * 时间为0时触发onTimeOut();
     * 时间变化时触发onTimeLapses(int hour, int minute, int second);
     */
    public interface OnTimeOutListen {
        void onTimeOut();

        void onTimeLapses(int hour, int minute, int second);
    }

    void init(Context context) {
        this.context = context;
    }
}
