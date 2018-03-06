package com.six.xinyidai.task;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.six.xinyidai.bean.Product;
import com.six.xinyidai.util.Constants;
import com.six.xinyidai.util.TinyDB;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Collections;

/**
 * Created by apple on 2017/4/20.
 */

public class GetProduct3 {

    public GetProduct3(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;

    public  void execute(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String URL = Constants.Times.URL;
                String nameSpace =  Constants.Times.nameSpace;
                String method_Name = "GetProduct";
                String SOAP_ACTION = nameSpace + method_Name;
                SoapObject rpc = new SoapObject(nameSpace, method_Name);
                rpc.addProperty("sAppName", Constants.Times.sAppName);
                rpc.addProperty("sPage", "34");
                rpc.addProperty("channel", "3");
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.debug = true;
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                try {
                    transport.call(SOAP_ACTION, envelope);
                    SoapObject object = (SoapObject) envelope.bodyIn;
                    String str = object.getProperty("GetProductResult").toString();
                    if (!TextUtils.isEmpty(str)&&!str.startsWith("1")&&!str.startsWith("2")) {
                        Gson gson = new Gson();
                        Product product = gson.fromJson(str, Product.class);
                        Collections.sort(product.getPrdList());
                        TinyDB db = new TinyDB(mContext);
                        db.remove("Product3");
                        db.putObject("Product3",product);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
