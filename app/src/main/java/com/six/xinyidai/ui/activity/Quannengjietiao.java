package com.six.xinyidai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.six.xinyidai.R;
import com.six.xinyidai.bean.Message;
import com.six.xinyidai.bean.MessageBean;
import com.six.xinyidai.bean.User;
import com.six.xinyidai.util.CaptchaTimeCount;
import com.six.xinyidai.util.Constants;
import com.six.xinyidai.util.SPUtils;
import com.umeng.analytics.MobclickAgent;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class Quannengjietiao extends AppCompatActivity {

    @Bind(R.id.etPhone)
    EditText etPhone;
    @Bind(R.id.etCode)
    EditText etCode;
    @Bind(R.id.bt_getCode)
    Button btGetCode;


    private CaptchaTimeCount captchaTimeCount;

    private String MessageCode = null;
    public static void launch(Context context) {
        context.startActivity(new Intent(context, Quannengjietiao.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        StatusBarUtil.setColor(this,0,0);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.status));
        ButterKnife.bind(this);
        captchaTimeCount = new CaptchaTimeCount(Constants.Times.MILLIS_IN_TOTAL, Constants.Times.COUNT_DOWN_INTERVAL, btGetCode, this);

    }

    @OnClick({R.id.bt_getCode, R.id.bt_Login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_getCode:
                if (!etPhone.getText().toString().isEmpty() && etPhone.length() == 11) {
                    getCodeMessage();
                } else {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_Login:
                if (!etPhone.getText().toString().isEmpty() && !etCode.getText().toString().isEmpty() && etCode.getText().toString().equals(MessageCode)) {
                    setLogin();
                } else {
                    Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    /**
     * 获取短信验证码
     */
    private void getCodeMessage() {
        Log.e("main", "---0000");
        captchaTimeCount.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String URL = "http://www.shoujiweidai.com/Service/WSForAPP3.asmx";
                String nameSpace = "http://chachaxy.com/";
                String method_Name = "QuickLgnMsg";
                String SOAP_ACTION = nameSpace + method_Name;
                SoapObject rpc = new SoapObject(nameSpace, method_Name);
                rpc.addProperty("strCellNumber", etPhone.getText().toString());
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.debug = true;
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                try {
                    transport.call(SOAP_ACTION, envelope);
                    SoapObject object = (SoapObject) envelope.bodyIn;
                    String str = object.getProperty("QuickLgnMsgResult").toString();
                    if (str != null) {
                        Log.e("main", "---" + str.toString());
                        if (str.length() > 1) {
                            String[] split = str.split(",");
                            Message bean = new Message(split[0], split[1]);
                            if ("0".equals(bean.getMeaage())) {
                                MessageCode = bean.getCode();
                            }
                        } else {
                            Quannengjietiao.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toasty.error(Quannengjietiao.this, "网络错误,发送失败", Toast.LENGTH_SHORT, true).show();

                                }
                            });
                        }
                    } else {
                        Quannengjietiao.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Quannengjietiao.this, "网络异常,请稍后再试", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }).start();

    }
    /**
     * 登陆
     */
    private KProgressHUD hud;
    private void setLogin() {
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("登录中...")
                .setCancellable(true)
                .show();
        Log.e("main", "---0000");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String URL = "http://www.shoujiweidai.com/Service/WSForAPP3.asmx";
                String nameSpace = "http://chachaxy.com/";
                String method_Name = "QuickLgn";
                String SOAP_ACTION = nameSpace + method_Name;
                SoapObject rpc = new SoapObject(nameSpace, method_Name);

                MessageBean message = new MessageBean();
                MessageBean.GerenBean gerenBean = new MessageBean.GerenBean();
                gerenBean.setUsername(etPhone.getText().toString());
                gerenBean.setChannel("Android-现金助手-xinhe");
                gerenBean.setPassword("");
                gerenBean.setQudao("");
                gerenBean.setUserid("");
                message.setGeren(gerenBean);
                Gson gson = new Gson();
                String json = gson.toJson(message);
                rpc.addProperty("strJson", json);

                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.debug = true;
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                try {
                    transport.call(SOAP_ACTION, envelope);
                    SoapObject object = (SoapObject) envelope.bodyIn;
                    final String str = object.getProperty("QuickLgnResult").toString();
                    if (str != null) {
                        Log.e("main", "---" + str.toString());

                        if(!str.startsWith("1")){
                            Quannengjietiao.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SPUtils.put(Quannengjietiao.this,"flag","Ok");
                                    Gson gson1=new Gson();
                                    User user = gson1.fromJson(str, User.class);
                                    SPUtils.put(Quannengjietiao.this, "id", user.getGeren().getUid());
                                    Log.e("---", "id---" + user.toString());
                                    HomeActivity.launch(Quannengjietiao.this);
                                    finish();
                                }
                            });
                        }else {
                            Quannengjietiao.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Quannengjietiao.this, "网络错误", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }


                    } else {
                        Quannengjietiao.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Quannengjietiao.this, "网络异常,请稍后再试", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    hud.dismiss();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }).start();
    }
    private long mLastBackTime = 0;

    @Override
    public void onBackPressed() {
        // finish while click back key 2 times during 1s.
        if ((System.currentTimeMillis() - mLastBackTime) < 1000) {
            finish();
        } else {
            mLastBackTime = System.currentTimeMillis();
            Toast.makeText(this, "请再确认一次", Toast.LENGTH_SHORT).show();
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
