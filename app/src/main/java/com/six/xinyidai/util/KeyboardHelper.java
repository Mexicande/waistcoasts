package com.six.xinyidai.util;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.six.xinyidai.R;

/**
 * 内部输入法工具类
 * Created by lihuabin on 2016/11/11.
 */
public class KeyboardHelper {
    private Context mContext;
    private Keyboard numberKeyboard;
    private KeyboardView keyboardView;
    private EditText et;
    private KeyboardView.OnKeyboardActionListener listener;

    public KeyboardHelper(Context context, KeyboardView keyboardView, EditText edit) {
        this.mContext = context;
        this.keyboardView = keyboardView;
        this.et = edit;
        numberKeyboard = new Keyboard(context, R.xml.security_keyboard);
        keyboardView.setKeyboard(numberKeyboard);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        listener = new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int primaryCode) {

            }

            @Override
            public void onRelease(int primaryCode) {

            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                Editable editable = et.getText();
                int start = et.getSelectionStart();
                if (primaryCode == Keyboard.KEYCODE_DONE) {// 完成
                    hideKeyboard();
                } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                    if (editable != null && editable.length() > 0) {
                        if (start > 0) {
                            editable.delete(start - 1, start);
                        }
                    }
                } else if (primaryCode == -1008611) {
                    et.setText("");
                } else if (primaryCode == 46) { //小数点
                    if (editable.length() > 9) {
                        Toast.makeText(mContext, mContext.getString(R.string.money_num_to_large), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (editable.toString().contains(".")) {
                        return;
                    } else {
                        editable.insert(start, Character.toString((char) primaryCode));
                    }
                } else {
                    if (editable.length() > 9) {
                        Toast.makeText(mContext, mContext.getString(R.string.money_num_to_large), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String text = editable.toString();
                    if ((!text.contains(".") || (text.length() - 1) - text.indexOf(".") <= 1)) {
                        //小数点后最长2位
                        editable.insert(start, Character.toString((char) primaryCode));
                    }
                }
            }


            @Override
            public void onText(CharSequence text) {

            }

            @Override
            public void swipeLeft() {

            }

            @Override
            public void swipeRight() {

            }

            @Override
            public void swipeDown() {

            }

            @Override
            public void swipeUp() {

            }
        };
        keyboardView.setOnKeyboardActionListener(listener);

    }

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }

    public boolean isKeyboardShow() {
        return keyboardView.getVisibility() == View.VISIBLE;
    }

    public Keyboard getNumKeyboard(Context context) {
        if (numberKeyboard == null) {
            numberKeyboard = new Keyboard(context, R.xml.security_keyboard);
        }
        return numberKeyboard;
    }


}
