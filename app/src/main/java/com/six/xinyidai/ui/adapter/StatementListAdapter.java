package com.six.xinyidai.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.six.xinyidai.App;
import com.six.xinyidai.R;
import com.six.xinyidai.base.BaseRecyclerViewAdapter;
import com.six.xinyidai.model.StatementListItem;
import com.six.xinyidai.util.FormatUtil;

import java.util.List;

/**
 * Created by lihuabin on 2016/11/18.
 */
public class StatementListAdapter extends BaseRecyclerViewAdapter<StatementListItem> {
    TextView categoryName;
    TextView moneyOfCategory;
    TextView percentage;

    public StatementListAdapter(Context mContext) {
        super(mContext, R.layout.statement_list_item);
    }

    public StatementListAdapter(Context mContext, List<StatementListItem> data) {
        super(mContext, R.layout.statement_list_item, data);
    }

    @Override
    protected void fillData(View itemView, StatementListItem model) {
        categoryName = (TextView) itemView.findViewById(R.id.category_title);
        moneyOfCategory = (TextView) itemView.findViewById(R.id.money_of_category);
        percentage = (TextView) itemView.findViewById(R.id.percentage);
        categoryName.setText(model.getCategoryName());
        moneyOfCategory.setText(FormatUtil.getTwoDecimalString(model.getMoneyOfCategory()));
        String percentageNum=FormatUtil.getTwoDecimalString(model.getPercentage() * 100);
        percentage.setText(percentageNum + "%");
        if (App.context().isColorful()) {
            setTextColor(model.getCategoryColor());
        }
    }

    private void setTextColor(int categoryColor) {
        categoryName.setTextColor(mContext.getResources().getColor(categoryColor));
        moneyOfCategory.setTextColor(mContext.getResources().getColor(categoryColor));
        percentage.setTextColor(mContext.getResources().getColor(categoryColor));
    }
}
