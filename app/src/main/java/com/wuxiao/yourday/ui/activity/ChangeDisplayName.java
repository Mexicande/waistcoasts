package com.wuxiao.yourday.ui.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.wuxiao.yourday.R;
import com.wuxiao.yourday.base.BaseActivity;
import com.wuxiao.yourday.event.RefreshUserInfoEvent;
import com.wuxiao.yourday.view.LoadingDialogBuilder;
import com.umeng.analytics.MobclickAgent;

/**
 * 改名字
 * Created by lihuabin on 2016/11/9.
 */
public class ChangeDisplayName extends BaseActivity {
    ImageView userIconImg;
    TextView tipTv;
    EditText nickEdt;
    Button finishBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    AppCompatDialog loadingDialog;
    String newNick;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_displayname;
    }

    @Override
    protected void assignViews() {
        super.assignViews();
        userIconImg = (ImageView) findViewById(R.id.user_icon);
        tipTv = (TextView) findViewById(R.id.tip);
        nickEdt = (EditText) findViewById(R.id.edt_display_name);
        finishBtn = (Button) findViewById(R.id.btn_finish);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        setToolbar(R.string.title_activity_change_nick);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user.getDisplayName() != null) {
            nickEdt.setHint("原昵称:" + app.getUserNick());
        }
        userIconImg.setOnClickListener(this);
        finishBtn.setOnClickListener(this);
        loadingDialog = new LoadingDialogBuilder.Builder(mContext).setCancelable(false).setCanceledOnTouchOutside(false).setHasTip(false).build();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.user_icon:
                Toast.makeText(ChangeDisplayName.this, getString(R.string.add_on_new_version_tip, "修改头像功能"), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_finish:
                loadingDialog.show();
                newNick = nickEdt.getText().toString();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(newNick)
                        //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    loadingDialog.dismiss();
                                    app.setUserNick(newNick);
                                    Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                    eventBus.post(new RefreshUserInfoEvent());
                                    finish();
                                }
                            }
                        });
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
