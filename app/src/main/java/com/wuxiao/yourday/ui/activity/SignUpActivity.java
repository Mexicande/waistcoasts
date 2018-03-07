package com.wuxiao.yourday.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wuxiao.yourday.R;
import com.wuxiao.yourday.base.BaseActivity;
import com.wuxiao.yourday.event.RefreshUserInfoEvent;
import com.wuxiao.yourday.view.LoadingDialogBuilder;
import com.umeng.analytics.MobclickAgent;

/**
 * 注册
 * Created by lihuabin on 2016/11/9.
 */
public class SignUpActivity extends BaseActivity {
    EditText usernameEdt;
    EditText passwordEdt;
    Button nextStepBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email;
    String password;
    AppCompatDialog loadingDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void assignViews() {
        super.assignViews();
        usernameEdt = (EditText) findViewById(R.id.edt_username);
        passwordEdt = (EditText) findViewById(R.id.edt_password);
        nextStepBtn = (Button) findViewById(R.id.btn_next_step);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        setToolbar(R.string.title_activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(loadingDialog.isShowing()){
                    loadingDialog.dismiss();
                }
                if (user != null) {
                    app.setIsLogin(true);
                    app.setUser(user);
                    eventBus.post(new RefreshUserInfoEvent());
                    startActivity(new Intent(mContext,ChangeDisplayName.class));
                    finish();
                } else {
                    app.setIsLogin(false);
                    app.cleanUser();
                }

            }
        };

        loadingDialog = new LoadingDialogBuilder.Builder(mContext).setTip("注册中").setHasTip(true).setCancelable(false).setCanceledOnTouchOutside(false).build();
        nextStepBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_next_step:
                email =usernameEdt.getText().toString();
                password =passwordEdt.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(mContext, "请输入邮箱", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
                } else {
                    signUp();
                }
                break;
            default:
                break;
        }
    }

    public void signUp() {
        loadingDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // TODO: 2016/11/9 注册成功
                        loadingDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, R.string.auth_failed + "\n" + e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        loadingDialog.dismiss();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
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
