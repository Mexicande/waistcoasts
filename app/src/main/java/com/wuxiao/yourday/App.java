package com.wuxiao.yourday;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.TypedValue;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.analytics.MobclickAgent;
import com.wuxiao.yourday.bean.ImagerBean;
import com.wuxiao.yourday.bean.Product;
import com.wuxiao.yourday.model.Category;
import com.wuxiao.yourday.util.FormatUtil;
import com.wuxiao.yourday.util.greenDAO.CategoryDao;
import com.wuxiao.yourday.util.greenDAO.DaoMaster;
import com.wuxiao.yourday.util.greenDAO.DaoSession;
import com.wuxiao.yourday.util.greenDAO.RecordDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lihuabin on 2016/10/27.
 */
public class App extends Application {
    public static App app;
    public static String userId;
    public SharedPreferences sp;
    private SharedPreferences.Editor editor;
    public static RequestQueue requestQueue;
    public static RequestQueue getVolleyRequestQueue(){
        return requestQueue;
    }
    private static ImagerBean image;

    public static ImagerBean getImage() {
        return image;
    }

    public static void setImage(ImagerBean image) {
        App.image = image;
    }

    public static Product getProduct() {
        return product;
    }

    public static void setProduct(Product product) {
        App.product = product;
    }

    private static Product product;
    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        sp = this.getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        //友盟
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this,"586a2941c8957603b8000ee8"
                ,channel));

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            FirebaseAuth.getInstance().signOut();
            setIsLogin(false);
        }
        app = (App) getApplicationContext();
        if(isFirstRun()){
            initApp();
        }
    }

    /**
     * 初次运行app事初始化app用
     * 主要是添加分类到数据库
     */
    private void initApp() {
        Category category1 = new Category(1L, "其他", R.mipmap.other, R.color.Red,true,1,"");
        Category category2 = new Category(2L, "吃喝饮食", R.mipmap.food, R.color.colorAccent, true,2,"");
        Category category3 = new Category(3L, "交通出行", R.mipmap.bus, R.color.colorPrimary, true,3,"");
        Category category4 = new Category(4L, "通讯费用", R.mipmap.cellphone, R.color.Brown, true,4,"");
        Category category5 = new Category(5L, "欠债借款", R.mipmap.ghost, R.color.Orange,true,5, "");
        Category category6 = new Category(6L, "人情礼物", R.mipmap.gift, R.color.Purple,true,6, "");
        Category category7 = new Category(7L, "房租水电", R.mipmap.home, R.color.Deep_Orange,true,7, "");
        Category category8 = new Category(8L, "医疗费用", R.mipmap.hospital, R.color.Green,true,8, "");
        Category category9 = new Category(9L, "社交红包", R.mipmap.redpacket, R.color.Red,true,9, "");
        Category category10 = new Category(10L, "逛街购物", R.mipmap.shopping, R.color.Green, true,10,"");
        Category category11 = new Category(11L, "运动健身", R.mipmap.sports, R.color.Yellow,true,11, "");
        Category category12 = new Category(12L, "工资薪水", R.mipmap.income, R.color.Red,true,12, "");
        Category category13 = new Category(13L, "日常用品", R.mipmap.hanger, R.color.Purple,true,13, "");
        Category category14 = new Category(14L, "聚会聚餐", R.mipmap.gathering, R.color.Deep_Orange,true,14, "");
        Category category15 = new Category(15L, "基金理财", R.mipmap.fund, R.color.Lime,true,15, "");
        Category category16 = new Category(16L, "旅行旅游", R.mipmap.travel, R.color.Blue,true,16, "");
        app.getCategoryDao().insertOrReplace(category1);
        app.getCategoryDao().insertOrReplace(category2);
        app.getCategoryDao().insertOrReplace(category3);
        app.getCategoryDao().insertOrReplace(category4);
        app.getCategoryDao().insertOrReplace(category5);
        app.getCategoryDao().insertOrReplace(category6);
        app.getCategoryDao().insertOrReplace(category7);
        app.getCategoryDao().insertOrReplace(category8);
        app.getCategoryDao().insertOrReplace(category9);
        app.getCategoryDao().insertOrReplace(category10);
        app.getCategoryDao().insertOrReplace(category11);
        app.getCategoryDao().insertOrReplace(category12);
        app.getCategoryDao().insertOrReplace(category13);
        app.getCategoryDao().insertOrReplace(category14);
        app.getCategoryDao().insertOrReplace(category15);
        app.getCategoryDao().insertOrReplace(category16);
    }

    public static synchronized App context() {
        return app;
    }




    /**
     * 获取DaoSession
     *
     * @return
     */
    public DaoSession getDaoSession() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context(), "AccountBook.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    public RecordDao getRecordDao() {
        return getDaoSession().getRecordDao();
    }

    public CategoryDao getCategoryDao() {
        return getDaoSession().getCategoryDao();
    }

    /**
     * 自定义查询
     *
     * @param selectWhat 希望返回的内容 如："sum(rowName)"
     * @param tableName  要查的表名
     * @param where      查询条件 如："id = 6"
     * @param andWhere   更多查询条件 如："and name='小明'" 或者"or id > 666"
     * @return
     */
    public Cursor customQuery(String selectWhat, String tableName, String where, String... andWhere) {
        String sql = "SELECT " + selectWhat +
                " FROM " + tableName;
        if(!TextUtils.isEmpty(where)){
            sql+=" WHERE " + where;
        }
        if (andWhere.length > 0) {
            for (String s : andWhere) {
                sql += " " + s;
            }
        }
        Cursor cursor = getDaoSession().getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        return cursor;
    }

    public void setBudget(double budgetNum) {//保存预算
        editor.putString("budgetNum",FormatUtil.getTwoDecimalString(budgetNum));
        editor.apply();
    }

    public String getBudget() {//读取预算
        String num = sp.getString("budgetNum", "0.00");
        return FormatUtil.getTwoDecimalString(num);
    }

    public void setColorful(boolean isColorful) {//保存是否使用多彩列表
        editor.putBoolean("isColorful", isColorful);
        editor.apply();
    }

    public boolean isColorful() {//读取是否使用多彩列表
        return sp.getBoolean("isColorful", false);
    }

    public void setIsLogin(boolean isLogin) {//保存是否已经登录
        editor.putBoolean("isLogin", isLogin);
        editor.apply();
    }

    public boolean isLogin() {//读取是否登录
        return sp.getBoolean("isLogin", false);
    }

    public void setUser(FirebaseUser user) {//保存用户信息
        editor.putString("userDisplayName", user.getDisplayName());
        editor.putString("userEmail", user.getEmail());
        editor.putString("userProviderId", user.getProviderId());
        editor.putString("userUid", user.getUid());
        editor.putBoolean("userIsEmailVerified", user.isEmailVerified());
        if (user.getPhotoUrl()!=null) {
            editor.putString("userPhotoUrl", user.getPhotoUrl().toString());
        }
        editor.apply();
    }

    public FirebaseUser getUser() {//读取用户信息
        return FirebaseAuth.getInstance().getCurrentUser();
    }

//    public FirebaseUser getUser() {//读取用户信息
//        FirebaseUser user = new FirebaseUser() {
//            @NonNull
//            @Override
//            public String getUid() {
//                return sp.getString("userUid", "null");
//            }
//
//            @NonNull
//            @Override
//            public String getProviderId() {
//                return sp.getString("userProviderId", "null");
//            }
//
//            @Override
//            public boolean isAnonymous() {
//                return isLogin();
//            }
//
//            @Nullable
//            @Override
//            public List<String> getProviders() {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public List<? extends UserInfo> getProviderData() {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public FirebaseUser zzaq(@NonNull List<? extends UserInfo> list) {
//                return null;
//            }
//
//            @Override
//            public FirebaseUser zzcs(boolean b) {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public FirebaseApp zzcou() {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public String getDisplayName() {
//                return sp.getString("userDisplayName", getString(R.string.default_display_name));
//            }
//
//            @Nullable
//            @Override
//            public Uri getPhotoUrl() {
//                return Uri.parse(sp.getString("userPhotoUrl", "null"));
//            }
//
//            @Nullable
//            @Override
//            public String getEmail() {
//                return sp.getString("userEmail", "null");
//            }
//
//            @Override
//            public boolean isEmailVerified() {
//                return sp.getBoolean("userIsEmailVerified", false);
//            }
//
//            @NonNull
//            @Override
//            public GetTokenResponse zzcov() {
//                return null;
//            }
//
//            @Override
//            public void zza(@NonNull GetTokenResponse getTokenResponse) {
//
//            }
//
//            @NonNull
//            @Override
//            public String zzcow() {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public String zzcox() {
//                return null;
//            }
//        };
//        return user;
//    }

    public void setUserNick(String nick) {
        editor.putString("userDisplayName", nick);
        editor.apply();
    }

    public String getUserNick(){
        return sp.getString("userDisplayName",getString(R.string.default_display_name));
    }

    public String getUserMail(){
        return sp.getString("userEmail",getString(R.string.default_mail));
    }

    public void cleanUser() {//清楚用户信息
        editor.remove("userDisplayName");
        editor.remove("userEmail");
        editor.remove("userProviderId");
        editor.remove("userUid");
        editor.remove("userPhotoUrl");
        editor.remove("userIsEmailVerified");
        editor.remove("isLogin");
        editor.apply();
    }

    public boolean isFirstRun(){//判断应用是否第一次运行
        return sp.getBoolean("isFirstRun",true);
    }

    public void setFirstRun(boolean isFirstRun){
        editor.putBoolean("isFirstRun",isFirstRun);
        editor.apply();
    }

    /**
     * dp转px
     * @param dpValue
     * @return
     */
    public static int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context()
                .getResources().getDisplayMetrics());
    }


}
