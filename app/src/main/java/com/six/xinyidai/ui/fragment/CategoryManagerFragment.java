package com.six.xinyidai.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.six.xinyidai.R;
import com.six.xinyidai.base.BaseFragment;
import com.six.xinyidai.base.BaseRecyclerViewAdapter;
import com.six.xinyidai.model.Category;
import com.six.xinyidai.ui.adapter.CategoryListItemAdapter;
import com.six.xinyidai.util.greenDAO.CategoryDao;

import java.util.List;

/**
 * 分类管理页
 * Created by lihuabin on 2016/11/25.
 */
public class CategoryManagerFragment extends BaseFragment {
    RecyclerView categoryList;
    CategoryListItemAdapter adapter;
    Button btnAddCategory;

    @Override
    public int getLayoutId() {
        return R.layout.category_manager_fragment;
    }

    @Override
    protected void assignViews(View view) {
        super.assignViews(view);
        categoryList = (RecyclerView) view.findViewById(R.id.category_list);
        btnAddCategory = (Button) view.findViewById(R.id.add_category);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        btnAddCategory.setOnClickListener(this);
        categoryList.setLayoutManager(new GridLayoutManager(mContext, 5));
        adapter = new CategoryListItemAdapter(mContext, getData());
        categoryList.setAdapter(adapter);
        adapter.setOnItemClickListen(new BaseRecyclerViewAdapter.onItemClickListen() {
            @Override
            public void OnClick(int position, int type) {
//                Intent intent = new Intent(getHoldingActivity(), AddCategoryActivity.class);
//                intent.putExtra("category",adapter.getData().get(position));
//                mContext.startActivity(intent);
                Toast.makeText(mContext, getString(R.string.add_on_new_version_tip, getString(R.string.category_manager_title)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnLongClick(final int position, int type) {
                // TODO: 2016/12/12 长按删除，实际是关闭启用
                Toast.makeText(mContext, getString(R.string.add_on_new_version_tip, getString(R.string.delete_category)), Toast.LENGTH_SHORT).show();

//                AlertDialog dialog= new AlertDialog.Builder(mContext)
//                        .setTitle(R.string.notice)
//                        .setMessage(R.string.delete_record_message)
//                        .setNegativeButton(android.R.string.cancel,null)
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Category c = adapter.getData().get(position);
//                                c.setRemark("false");
//                                app.getCategoryDao().insertOrReplace(c);
//                            }
//                        })
//                        .show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.add_category:
                Toast.makeText(mContext, getString(R.string.add_on_new_version_tip, getString(R.string.category_manager_title)), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }
    }

    private List<Category> getData() {//获取 分类 数据
        CategoryDao categoryDao = app.getCategoryDao();
        List<Category> data = categoryDao.queryBuilder()
                .where(CategoryDao.Properties.IsActivate.eq(true))//只显示被启用的分类
                .orderAsc(CategoryDao.Properties.SerialNumber)//按序号升序排列
                .list();
        return data;
    }

}
