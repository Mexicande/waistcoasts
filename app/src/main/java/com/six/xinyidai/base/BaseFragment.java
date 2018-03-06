package com.six.xinyidai.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.six.xinyidai.App;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected Activity mActivity;
    protected Context mContext;
    protected Intent mIntent;
    protected Bundle mBundle;
    protected EventBus eventBus;
    protected App app;
    private View root;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(getLayoutId(), container, false);
        eventBus = EventBus.getDefault();
        app = App.context();
        assignViews(root);
        onViewReady();
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity=(Activity)context;
        mIntent = mActivity.getIntent();
        if (mIntent != null) {
            mBundle = getArguments();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (eventBus != null && eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
    }

    protected void assignViews(View view) {
    }

    protected void onViewReady() {
    }

    public abstract int getLayoutId();

    /**
     * 获取宿主Activity
     * @return
     */
    public Activity getHoldingActivity(){
        return mActivity != null ? mActivity : getActivity();
    }
    @Override
    public void onClick(View view) {

    }


}
