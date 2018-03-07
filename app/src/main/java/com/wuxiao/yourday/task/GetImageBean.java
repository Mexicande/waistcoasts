package com.wuxiao.yourday.task;

import android.content.Context;

import com.google.gson.Gson;
import com.wuxiao.yourday.App;
import com.wuxiao.yourday.bean.ImagerBean;
import com.wuxiao.yourday.util.Constants;
import com.wuxiao.yourday.util.TinyDB;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Collections;

/**
 * Created by apple on 2017/4/12.
 *   bunder
 *
 */

public class GetImageBean {
    private Context mContext;

    public GetImageBean(Context mContext) {
        this.mContext = mContext;
    }

    public  void execute(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String URL = Constants.Times.URL;
                String nameSpace = Constants.Times.nameSpace;
                String method_Name = "Daohang";
                String SOAP_ACTION = nameSpace + method_Name;
                SoapObject rpc = new SoapObject(nameSpace, method_Name);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.debug = true;
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                try {
                    transport.call(SOAP_ACTION, envelope);
                    SoapObject object = (SoapObject) envelope.bodyIn;
                    String str = object.getProperty("DaohangResult").toString();
                    if (str != null) {
                        Gson gson = new Gson();
                        ImagerBean bean = gson.fromJson(str, ImagerBean.class);
                        Collections.sort(bean.getDaohang());
                        App.setImage(bean);
                        TinyDB db = new TinyDB(mContext);
                        db.remove("ImagerBean");
                        db.putObject("ImagerBean",bean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
