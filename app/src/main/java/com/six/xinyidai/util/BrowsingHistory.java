package com.six.xinyidai.util;



import com.six.xinyidai.App;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by tantan on 2017/7/14.
 */

public class BrowsingHistory {

    public  void execute(final String prdId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject js1=new JSONObject();
                final JSONObject js2=new JSONObject();
                try {
                    js1.put("userId", App.userId);
                    js1.put("prdId",prdId);
                    js1.put("channel",Constants.Times.channel);
                    js1.put("channel2",Constants.Times.channel1);
                    js1.put("page","");
                    js2.put("Record",js1);
                } catch (JSONException e) {

                }
                String URL = Constants.Times.URL;
                String nameSpace = Constants.Times.nameSpace;
                String method_Name = "SetRecord";
                String SOAP_ACTION = nameSpace + method_Name;
                SoapObject rpc = new SoapObject(nameSpace, method_Name);
                rpc.addProperty("strJson", js2.toString());
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.debug = true;
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                try {
                    transport.call(SOAP_ACTION, envelope);
                    SoapObject object = (SoapObject) envelope.bodyIn;
                    String str = object.getProperty("SetRecordResult").toString();
                } catch (Exception e) {

                }
            }
        }).start();
    }
}
