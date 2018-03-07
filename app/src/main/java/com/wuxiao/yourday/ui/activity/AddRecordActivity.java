package com.wuxiao.yourday.ui.activity;

import android.app.Service;
import android.content.DialogInterface;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wuxiao.yourday.R;
import com.wuxiao.yourday.base.BaseActivity;
import com.wuxiao.yourday.base.BaseRecyclerViewAdapter;
import com.wuxiao.yourday.event.RefreshRecordEvent;
import com.wuxiao.yourday.model.Category;
import com.wuxiao.yourday.model.Record;
import com.wuxiao.yourday.ui.adapter.CategoryListItemAdapter;
import com.wuxiao.yourday.ui.dialog.SelectDateDialog;
import com.wuxiao.yourday.util.DateUtil;
import com.wuxiao.yourday.util.KeyboardHelper;
import com.wuxiao.yourday.util.greenDAO.CategoryDao;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * 添加记录
 * Created by lihuabin on 2016/10/27.
 */
public class AddRecordActivity extends BaseActivity {
    private LinearLayout llMoney;
    private TextView categoryName;
    private EditText moneyEt;
    private TextView outOrInTv;
    private TextView createDateTv;
    private TextView remarkTv;
    private RecyclerView categoryList;
    private View popContentView;
    private EditText remarkEt;
    private ImageButton confrimRemark;
    private CategoryListItemAdapter adapter;
    private List<Category> categorydata;
    private KeyboardView keyboardView;

    private KeyboardHelper keyboardHelper;
    private Record record;
    long categoryId = -1;
    private Double money;
    long recordId;
    int outOrIn = Record.OUT;
    private String createDate = DateUtil.getToday();
    private String createTime = DateUtil.getTimeNow();
    private String remark;

    private SelectDateDialog selectDateDialog;
    private PopupWindow popupWindow;
    private InputMethodManager imm;
    private Handler handler = new Handler();//用于异步调起输入法和修改记录时选择传入的分类




    @Override
    public int getLayoutId() {
        return R.layout.activity_add_record;
    }

    @Override
    protected void assignViews() {
        super.assignViews();
        categoryName = (TextView) findViewById(R.id.money_title);
        moneyEt = (EditText) findViewById(R.id.money);
        categoryList = (RecyclerView) findViewById(R.id.category_list);
        llMoney = (LinearLayout) findViewById(R.id.ll_money);
        outOrInTv = (TextView) findViewById(R.id.out_or_in);
        createDateTv = (TextView) findViewById(R.id.create_date);
        remarkTv = (TextView) findViewById(R.id.remark);
        keyboardView = (KeyboardView) findViewById(R.id.keyboard_View);

        popContentView = LayoutInflater.from(mContext).inflate(R.layout.pop_write_remark, null, false);
        remarkEt = (EditText) popContentView.findViewById(R.id.remark_edt);
        confrimRemark = (ImageButton) popContentView.findViewById(R.id.remark_confirm);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        setToolbar(R.string.add_record_activity_title);
        selectDateDialog = SelectDateDialog.newInstance("选择日期", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectDate = SelectDateDialog.getSelectDate();
                if (!TextUtils.isEmpty(selectDate)) {
                    createDate = selectDate;
                    createDateTv.setText(selectDate);
                }
            }
        });

        remarkEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
                return false;
            }
        });
        imm = (InputMethodManager) mContext.getSystemService(Service.INPUT_METHOD_SERVICE);
        popupWindow = new PopupWindow(mContext);
//        popupWindow = new PopupWindow(popContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        //popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(popContentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setHeight(app.dp2px(56));//不设置6.0以下popupWindow显示不了
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); //防止虚拟软键盘被弹出菜单遮住
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                EditText editText = (EditText) popupWindow.getContentView().findViewById(R.id.remark_edt);
                remark = editText.getText().toString();
                if (!TextUtils.isEmpty(remark)) {
                    remarkTv.setText(remark);
                } else {
                    remarkTv.setText(getString(R.string.write_remark));
                }

            }
        });
        categorydata = getData();
        adapter = getAdapter();
        //categoryList.setLayoutManager(new StaggeredGridLayoutManager(5, LinearLayout.VERTICAL));
        categoryList.setLayoutManager(new GridLayoutManager(this, 5));
        categoryList.setAdapter(adapter);
        if (mBundle != null) {
            record = (Record) mBundle.get("record");
            toolbar.setTitle("修改记录");
            setDataByUpdate();
        }
        outOrInTv.setOnClickListener(this);
        createDateTv.setOnClickListener(this);
        remarkTv.setOnClickListener(this);
        confrimRemark.setOnClickListener(this);

        keyboardHelper = new KeyboardHelper(mContext, keyboardView, moneyEt);
        //moneyEt.setInputType(InputType.TYPE_NULL);
        moneyEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                moneyEt.setSelection(moneyEt.getText().length());
                keyboardHelper.showKeyboard();
                return false;
            }
        });
    }

    private void setDataByUpdate() {//修改时展示传入参数
        recordId = record.getId();
        categoryId = record.getCategoryId();
        money = record.getMoney();
        outOrIn = record.getOutOrIn();
        createDate = record.getCreateDate();
        createTime = record.getCreateTime();
        remark = record.getRemark();

        moneyEt.setText(money + "");
        moneyEt.setSelection(moneyEt.getText().length());
        categoryName.setText(record.getCategorySimple().getCategoryName());
        createDateTv.setText(createDate);
        if (outOrIn == Record.OUT) {
            outOrInTv.setText(getString(R.string.type_out));
        } else if (outOrIn == Record.IN) {
            outOrInTv.setText(getString(R.string.type_in));
        }
        if (remark != null) {
            remarkTv.setText(record.getRemark());
        }
        if (app.isColorful()) {
            llMoney.setBackgroundResource(record.getCategorySimple().getCategoryColor());
        }
        handler.postDelayed(new Runnable() {//需要异步方式，不然会出错
            @Override
            public void run() {
                if (!(categoryId - 1 < 0)) {
                    setItemIsSelected((int) categoryId - 1);
                }
            }
        }, 10);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private CategoryListItemAdapter getAdapter() {
        View footView = LayoutInflater.from(this).inflate(R.layout.category_list_item, null, false);
        ImageView footViewIcon = (ImageView) footView.findViewById(R.id.category_icon);
        TextView footViewTitle = (TextView) footView.findViewById(R.id.category_title);
        footViewIcon.setImageResource(android.R.drawable.ic_menu_add);
        footViewTitle.setText("添加更多");

        CategoryListItemAdapter a = new CategoryListItemAdapter(this, categorydata);
        a.setFooterView(footView);

        a.setOnItemClickListen(new BaseRecyclerViewAdapter.onItemClickListen() {
            @Override
            public void OnClick(int position, int type) {
                if (keyboardHelper.isKeyboardShow()) {
                    keyboardHelper.hideKeyboard();
                }
                if (type == BaseRecyclerViewAdapter.TYPE_FOOTER) {
                    Toast.makeText(AddRecordActivity.this, getString(R.string.add_on_new_version_tip, getString(R.string.custom_category_function)), Toast.LENGTH_SHORT).show();
                } else if (type == BaseRecyclerViewAdapter.TYPE_HEADER) {
                    Toast.makeText(AddRecordActivity.this, "header is onclick", Toast.LENGTH_SHORT).show();
                } else {
                    setItemIsSelected(position);
                }
            }

            @Override
            public void OnLongClick(int position, int type) {
            }
        });
        return a;
    }

    private List<Category> getData() {
        CategoryDao categoryDao = app.getCategoryDao();
        List<Category> data = categoryDao.queryBuilder()
                .where(CategoryDao.Properties.IsActivate.eq(true))//只显示被启用的分类
                .orderAsc(CategoryDao.Properties.SerialNumber)//按序号升序排列
                .list();
        return data;
    }

    public void setItemIsSelected(int pos) {//设置RecyclerView孩子item被选中的显示效果
        categoryId = pos + 1;
        categoryName.setText(categorydata.get(pos).getCategoryName());
        View childView = categoryList.getChildAt(pos);
        TextView tv = (TextView) childView.findViewById(R.id.category_title);
        if (app.isColorful()) {
            childView.setBackgroundResource(categorydata.get(pos).getCategoryColor());
            int bgColor = getResources().getColor(categorydata.get(pos).getCategoryColor());
            llMoney.setBackgroundColor(bgColor);
            //分类标题颜色取反
            int a = 0xFF;//不透明
            int r = 255 - Color.red(bgColor);
            int g = 255 - Color.green(bgColor);
            int b = 255 - Color.blue(bgColor);
            tv.setTextColor(Color.argb(a, r, g, b));
            categoryName.setTextColor(Color.argb(a, r, g, b));
        } else {
            childView.findViewById(R.id.state).setVisibility(View.VISIBLE);
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        for (int i = 0; i < categorydata.size(); i++) {
            if (i != pos) {
                if (adapter.hasFooterView() && i == adapter.getItemCount()) {
                    break;
                }
                View vv = categoryList.getChildAt(i);
                vv.setBackgroundColor(Color.TRANSPARENT);
                vv.findViewById(R.id.state).setVisibility(View.GONE);
                ImageView ivv = (ImageView) vv.findViewById(R.id.category_icon);
                TextView tvv = (TextView) vv.findViewById(R.id.category_title);
                tvv.setTextColor(getResources().getColor(R.color.Grey));

            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (keyboardHelper.isKeyboardShow()) {
            keyboardHelper.hideKeyboard();
        }
        int viewId = view.getId();
        switch (viewId) {
            case R.id.out_or_in:
                switch (outOrIn) {
                    case Record.OUT:
                        outOrInTv.setText(getString(R.string.type_in));
                        outOrIn = Record.IN;
                        break;
                    case Record.IN:
                        outOrInTv.setText(getString(R.string.type_out));
                        outOrIn = Record.OUT;
                        break;
                    default:
                        break;
                }
                break;
            case R.id.create_date:
                selectDateDialog.show(getSupportFragmentManager(), "选择日期");
                break;
            case R.id.remark:
                remarkEt.setText(remark);
                remarkEt.setSelection(remarkEt.getText().length());
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }, 0);
                popupWindow.showAtLocation(this.findViewById(android.R.id.content), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.remark_confirm:
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                break;
            default:
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_record, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (TextUtils.isEmpty(moneyEt.getText())) {
                    Toast.makeText(mContext, "请输入正确的金额", Toast.LENGTH_SHORT).show();
                } else if (categoryId < 0) {
                    Toast.makeText(mContext, "请选择正确分类", Toast.LENGTH_SHORT).show();
                } else {
                    String newMoneyString = moneyEt.getText().toString().trim();
                    if (newMoneyString.endsWith(".")) {
                        newMoneyString = newMoneyString.substring(0, newMoneyString.length() - 1);
                    }
                    double newMoney = Double.parseDouble(newMoneyString);
                    Record r;
                    if (record == null) {
                        r = new Record(null, newMoney, createDate, createTime, outOrIn, categoryId, remark);
                    } else {
                        r = new Record(recordId, newMoney, createDate, createTime, outOrIn, categoryId, remark);
                    }
                    app.getRecordDao().insertOrReplace(r);
                    eventBus.post(new RefreshRecordEvent());
                    finish();
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (keyboardHelper.isKeyboardShow()) {
            keyboardHelper.hideKeyboard();
            return;
        } else if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            return;
        } else {
            super.onBackPressed();
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
