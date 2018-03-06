package com.six.xinyidai.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.six.xinyidai.R;
import com.six.xinyidai.base.BaseActivity;
import com.six.xinyidai.event.RefreshRecordEvent;
import com.six.xinyidai.model.Category;
import com.six.xinyidai.model.Record;
import com.umeng.analytics.MobclickAgent;

/**
 * 记录详情
 * Created by lihuabin on 2016/10/27.
 */
public class RecordDetailsActivity extends BaseActivity {
    private TextView moneyTv;
    private TextView categoryTv;
    private ImageView categoryIcon;
    private TextView outOrInTv;
    private TextView createDateTv;
    private TextView createTimeTv;
    private TextView remarkTv;
    private boolean isColorful;

    @Override
    public int getLayoutId() {
        return R.layout.activity_record_details;
    }

    @Override
    protected void assignViews() {
        super.assignViews();
        moneyTv = (TextView) findViewById(R.id.money);
        categoryTv = (TextView) findViewById(R.id.category_text);
        categoryIcon = (ImageView) findViewById(R.id.category_icon);
        outOrInTv = (TextView) findViewById(R.id.out_or_in);
        createDateTv = (TextView) findViewById(R.id.create_date);
        createTimeTv = (TextView) findViewById(R.id.create_time);
        remarkTv = (TextView) findViewById(R.id.remark);
    }

    Record record;
    Category category;

    @Override
    protected void onViewReady() {
        super.onViewReady();
        setToolbar(R.string.record_details_activity_title);
        isColorful = app.isColorful();
        record = (Record) getIntent().getExtras().get("record");
        //record.getCategory() 会报错 Record.93throw new DaoException("Entity is detached from DAO context");
        //category = (Category) getIntent().getExtras().get("category");
        category = record.getCategorySimple();
        moneyTv.setText(record.getMoney() + "");
        categoryTv.setText(category.getCategoryName());
        if (isColorful) {
            categoryTv.setTextColor(getResources().getColor(category.getCategoryColor()));
        }
        categoryIcon.setImageResource(category.getCategoryIconRId());
        createDateTv.setText(record.getCreateDate());
        createTimeTv.setText(record.getCreateTime().substring(0, 5));
        if (record.getOutOrIn() == Record.IN) {
            outOrInTv.setText("收入");
            if (isColorful) {
                outOrInTv.setTextColor(getResources().getColor(R.color.inTextColor));
                moneyTv.setTextColor(getResources().getColor(R.color.inTextColor));
            }
        } else if (record.getOutOrIn() == Record.OUT) {
            outOrInTv.setText("支出");
            if (isColorful) {
                outOrInTv.setTextColor(getResources().getColor(R.color.outTextColor));
                moneyTv.setTextColor(getResources().getColor(R.color.outTextColor));
            }
        }
        if (record.getRemark() != null) {
            remarkTv.setText(record.getRemark());
        } else {
            remarkTv.setText("未填写");
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.record_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                Intent i = new Intent(this, AddRecordActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("record", record);
                i.putExtras(b);
                startActivity(i);
                finish();
                break;
            case R.id.delete:
                AlertDialog dialog= new AlertDialog.Builder(mContext)
                        .setTitle(getString(R.string.delete))
                        .setMessage(getString(R.string.delete_record_message))
                        .setNegativeButton(android.R.string.cancel,null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                app.getRecordDao().delete(record);
                                eventBus.post(new RefreshRecordEvent());
                                finish();
                            }
                        })
                        .show();

                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
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
