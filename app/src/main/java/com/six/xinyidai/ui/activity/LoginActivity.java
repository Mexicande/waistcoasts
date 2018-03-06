package com.six.xinyidai.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.six.xinyidai.R;
import com.six.xinyidai.base.BaseActivity;
import com.six.xinyidai.event.RefreshUserInfoEvent;
import com.six.xinyidai.view.LoadingDialogBuilder;
import com.umeng.analytics.MobclickAgent;

/**
 * 登录
 * Created by lihuabin
 */
public class LoginActivity extends BaseActivity {
    EditText emailEdt;
    EditText passwordEdt;
    Button loginBtn;
    TextView signUp;
    TextView forgetPassword;

    private FirebaseAuth mAuth;//firebase登录用的实例
    private FirebaseAuth.AuthStateListener mAuthListener;//用于响应用户状态变化的相关

    AppCompatDialog loadingDialog;
    String email;
    String password;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void assignViews() {
        super.assignViews();
        emailEdt = (EditText) findViewById(R.id.edt_username);
        passwordEdt = (EditText) findViewById(R.id.edt_password);
        loginBtn = (Button) findViewById(R.id.btn_login);
        signUp = (TextView) findViewById(R.id.txt_sign_up);
        forgetPassword = (TextView) findViewById(R.id.txt_forget_pwd);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        setToolbar(R.string.title_activity_login);
        loginBtn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //if(app.isLogin())return;
                    onLoginSuccess(user);
                    // TODO: 2016/11/8  登录成功 User is signed in
                } else {
                    // TODO: 2016/11/8  登出 User is signed out
                    app.setIsLogin(false);
                    app.cleanUser();
                    // System.out.println("User is signed out");
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                }
            }
        };

        loadingDialog = new LoadingDialogBuilder.Builder(mContext)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setTip("登陆中")
                .setHasTip(true)
                .build();
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setCancelable(false)
                .setTitle(R.string.notice)
                .setMessage(R.string.login_no_for_help)
                .setPositiveButton(R.string.ss_continue,null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_login:
                email = emailEdt.getText().toString();
                password = passwordEdt.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(mContext, "请输入邮箱", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
                } else {
                    login();
                }
                break;
            case R.id.txt_sign_up:
                startActivity(new Intent(mContext, SignUpActivity.class));
                break;
            case R.id.txt_forget_pwd:
                startActivity(new Intent(mContext, ResetPasswordActivity.class));
                break;
            default:
                break;
        }
    }

    private void login() {
        loadingDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        //An internal error has occurred   949978376
                        Toast.makeText(mContext, R.string.auth_failed + "\n" + e.toString(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = authResult.getUser();
                        onLoginSuccess(user);
                        Log.i("signInOnSuccess", user.getEmail());
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

    public void onLoginSuccess(FirebaseUser user){
        if(user==null)return;
        app.setIsLogin(true);
        app.setUser(user);
        eventBus.post(new RefreshUserInfoEvent());
        try {
            if (mBundle != null) {
                Class<?> nextJump = Class.forName(mBundle.getString(NEXT_JUMP));
                startActivity(new Intent(mContext, nextJump));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            finish();
        }

        //System.out.println("User is login in");
        //mAuth.signOut();
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
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

