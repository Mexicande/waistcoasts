package com.wuxiao.yourday.ui.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.wuxiao.yourday.R;
import com.wuxiao.yourday.base.BaseActivity;
import com.wuxiao.yourday.view.LoadingDialogBuilder;
import com.umeng.analytics.MobclickAgent;

/**
 * 重置密码
 * Created by lihuabin on 2016/11/10.
 */
public class ResetPasswordActivity extends BaseActivity {
    EditText mailEdt;
    Button sendMailBtn;
    String mail;
    FirebaseAuth mAuth;
    AppCompatDialog loadingDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void assignViews() {
        super.assignViews();
        mailEdt = (EditText) findViewById(R.id.edt_username);
        sendMailBtn = (Button) findViewById(R.id.btn_send_mail);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        setToolbar(R.string.reset_password);
        mAuth = FirebaseAuth.getInstance();
        sendMailBtn.setOnClickListener(this);
        loadingDialog = new LoadingDialogBuilder.Builder(mContext).setHasTip(false).build();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_send_mail:
                mail = mailEdt.getText().toString();
                if (TextUtils.isEmpty(mail)) {
                    Toast.makeText(mContext, getString(R.string.please_input_mail), Toast.LENGTH_SHORT).show();
                } else {
                    loadingDialog.show();
                    mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            loadingDialog.dismiss();
                            Toast.makeText(ResetPasswordActivity.this, "重置密码邮件已发送至 "+mail, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingDialog.dismiss();
                            Toast.makeText(ResetPasswordActivity.this, "重置密码邮件发送失败，邮箱 "+mail, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                break;
            default:
                break;
        }
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
