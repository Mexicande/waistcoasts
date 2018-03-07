package com.wuxiao.yourday.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.itheima.roundedimageview.RoundedImageView;
import com.jaeger.library.StatusBarUtil;
import com.wuxiao.yourday.R;
import com.wuxiao.yourday.bean.Product;
import com.wuxiao.yourday.bean.UserRecord;
import com.wuxiao.yourday.util.SPUtils;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductActivity extends AppCompatActivity {

    @Bind(R.id.title_name)
    TextView titleName;
    @Bind(R.id.head)
    RoundedImageView head;
    @Bind(R.id.product_name)
    TextView productName;
    @Bind(R.id.summary)
    TextView summary;
    @Bind(R.id.top1)
    RelativeLayout top1;
    @Bind(R.id.view1)
    View view1;
    @Bind(R.id.limit)
    TextView limit;
    @Bind(R.id.lines)
    TextView lines;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.time_limit)
    TextView timeLimit;
    @Bind(R.id.cost)
    TextView cost;
    @Bind(R.id.rate)
    TextView rate;
    @Bind(R.id.view2)
    LinearLayout view2;
    @Bind(R.id.view3)
    View view3;
    @Bind(R.id.view4)
    TextView view4;
    @Bind(R.id.view5)
    View view5;
    @Bind(R.id.tv_demand1)
    TextView tvDemand1;
    @Bind(R.id.tv_demand2)
    TextView tvDemand2;
    @Bind(R.id.view6)
    View view6;
    @Bind(R.id.view7)
    TextView view7;
    @Bind(R.id.view8)
    View view8;
    @Bind(R.id.tv_tips1)
    TextView tvTips1;
    @Bind(R.id.tv_tips2)
    TextView tvTips2;
    @Bind(R.id.view9)
    View view9;
    @Bind(R.id.down_money)
    TextView downMoney;
    @Bind(R.id.speed)
    TextView speed;
    @Bind(R.id.difficulty_limet)
    TextView difficultyLimet;
    @Bind(R.id.difficulty)
    TextView difficulty;
    @Bind(R.id.view10)
    LinearLayout view10;
    @Bind(R.id.view11)
    View view11;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.apply)
    Button apply;


    private static String str = "http://www.shoujiweidai.com";
    private String url;
    private  Product.PrdListBean product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        StatusBarUtil.setColor(this,0,0);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.Blue));
        ButterKnife.bind(this);
        getDate();
    }

    private void getDate() {
        titleName.setText("产品详情");
        ivBack.setVisibility(View.VISIBLE);
        product = (Product.PrdListBean) getIntent().getSerializableExtra("product");
        Log.e("product", product.toString() + "-----------");
        if (product != null) {
            Glide.with(this).load(str + product.getLogo()).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(head);
            lines.setText(product.getLines());
            timeLimit.setText(product.getTimeLimit());

            if (product.getRate().length() > 10) {
                rate.setText("0.09%/14天");
            } else {
                rate.setText(product.getRate());
            }
            difficulty.setText(product.getDifficulty());
            tvDemand1.setText("1、" + product.getDemand1());
            tvDemand2.setText("2、" + product.getDemand2());
            tvTips1.setText("1、" + product.getTips1());
            tvTips2.setText("2、" + product.getTips2());
            speed.setText(product.getSpeed());
            productName.setText(product.getName());
            url = product.getLink();
            summary.setText(product.getSummary());
        }

    }

    @OnClick({R.id.iv_back, R.id.apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.apply:
                //诸葛
                setZhuge();
                sendProduct();
                startActivity(new Intent(this, HtmlActivity.class).putExtra("html", url));
                break;
        }
    }

    private void setZhuge() {
        //定义与事件相关的属性信息
        JSONObject eventObject = new JSONObject();
        String userId= (String) SPUtils.get(this,"uid","");
        try {
            eventObject.put("APP名称", "现金超人");
            eventObject.put("用户ID", userId);
            eventObject.put("产品名称", product.getName());
            eventObject.put("渠道", "信和");
            eventObject.put("开发者", "谭谈");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //记录事件
        ZhugeSDK.getInstance().track(getApplicationContext(), "统计",
                eventObject);
    }

    private void sendProduct() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String URL = "http://www.shoujiweidai.com/Service/WSForAPP3.asmx";
                String nameSpace = "http://chachaxy.com/";
                String method_Name = "SetRecord";
                String SOAP_ACTION = nameSpace + method_Name;
                SoapObject rpc = new SoapObject(nameSpace, method_Name);
                UserRecord userRecord = new UserRecord();
                UserRecord.Record record1 = new UserRecord.Record();

                if (product != null) {
                    record1.setPrdId(product.getUid());
                    record1.setChannel("Android-现金超人");
                    record1.setUserId((String) SPUtils.get(ProductActivity.this, "id", "1"));
                    userRecord.setRecord(record1);
                    Gson gson = new Gson();
                    String json = gson.toJson(userRecord);
                    Log.e("----", json);
                    rpc.addProperty("strJson", json);
                }
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.debug = true;
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                try {
                    transport.call(SOAP_ACTION, envelope);
                    SoapObject object = (SoapObject) envelope.bodyIn;
                    final String str = object.getProperty("SetRecordResult").toString();
                    Log.e("----", str);

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }).start();
    }


}
