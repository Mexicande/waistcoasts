package com.wuxiao.yourday.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuxiao.yourday.App;
import com.wuxiao.yourday.R;
import com.wuxiao.yourday.ui.activity.RecordDetailsActivity;
import com.wuxiao.yourday.base.BaseRecyclerViewAdapter;
import com.wuxiao.yourday.model.Record;
import com.wuxiao.yourday.util.FormatUtil;

import java.util.List;

/**
 * 记录列表适配器
 * Created by lihuabin on 2016/10/26.
 */
public class RecordListItemAdapter extends BaseRecyclerViewAdapter<Record> {

    public RecordListItemAdapter(Context mContext) {
        super(mContext, R.layout.record_list_item);
    }

    public RecordListItemAdapter(Context mContext, List<Record> data) {
        super(mContext, R.layout.record_list_item, data);
    }


    @Override
    protected void fillData(View itemView, final Record model) {
        boolean isColorful = App.context().isColorful();
        ImageView iconImg = (ImageView) itemView.findViewById(R.id.category_icon);
        TextView remarkTv = (TextView) itemView.findViewById(R.id.remark);
        TextView moneyTv = (TextView) itemView.findViewById(R.id.money);
        int imgRid = model.getCategory().getCategoryIconRId();
        String remark = model.getRemark();
        if (remark == null) {
            remark = model.getCategory().getCategoryName();
        }
        double money = model.getMoney();
        int ourOrIn = model.getOutOrIn();
        iconImg.setImageResource(imgRid);
        remarkTv.setText(remark);
        if (isColorful) {
            itemView.setBackgroundColor(mContext.getResources().getColor(model.getCategory().getCategoryColor()));
            switch (ourOrIn) {
                case Record.OUT:
                    moneyTv.setTextColor(mContext.getResources().getColor(R.color.outTextColor));
                    break;
                case Record.IN:
                    moneyTv.setTextColor(mContext.getResources().getColor(R.color.inTextColor));
                    break;
                default:
                    break;
            }
        } else {
            if (ourOrIn == Record.OUT) {
                money = 0 - money;
            }
        }
        moneyTv.setText(FormatUtil.getTwoDecimalString(money));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, RecordDetailsActivity.class);
                i.putExtra("record", model);
                mContext.startActivity(i);
            }
        });
    }
}
