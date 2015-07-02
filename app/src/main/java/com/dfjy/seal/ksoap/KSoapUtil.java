package com.dfjy.seal.ksoap;

import org.kobjects.base64.Base64;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 *  Project：TestKsoap
 *  User: dongxf(dongxf@orient-it.com)
 *  Date: 2015-06-24
 *  Time: 13:35
 */
public class KSoapUtil {
    private static final String SERVER_URL = "http://192.168.1.29:30001/?wsdl";
    public static final String SERVICE_NAMESPACE = "http://localhost/ws.wsdl";
    private static HeaderProperty mSessionHeader = null;
    private static HeaderProperty mUserIdHeader = null;


    /**
     * @return
     */
    public static List<String> getLoginUserInfo() {
        String methodName = "getTableList";
        final HttpTransportSE httpSE = new HttpTransportSE(SERVER_URL);
        httpSE.debug=true;
        try {
            //httpSE.debug = true;
            SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE, methodName);
            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER12);
            LinkedList<HeaderProperty> reqHeaders = new LinkedList<HeaderProperty>();
            //if (mSessionHeader != null && mUserIdHeader != null) {
            //reqHeaders.add(new HeaderProperty("userid", mUserIdHeader.getValue()));
            reqHeaders.add(new HeaderProperty("userid", "ee"));
            //reqHeaders.add(new HeaderProperty("sessionid", mSessionHeader.getValue()));
            reqHeaders.add(new HeaderProperty("sessionid", "12"));
            ArrayList<String> desList = new ArrayList<String>();
            ArrayList<String> rowsList = new ArrayList<String>();
            //}
//            Element[] header = new Element[1];
//            header[0] = new Element().createElement(null, "wsHeader");
//            Element userid = new Element().createElement(null, "userid");
//            Element sessionid = new Element().createElement(null, "sessionid");
//            userid.addChild(Node.TEXT, "gly");
//            sessionid.addChild(Node.TEXT, "123333");
//            header[0].addChild(Node.ELEMENT, userid);
//            header[0].addChild(Node.ELEMENT, sessionid);
            StringBuffer buff = new StringBuffer();
            buff.append("<?xml version=\"1.0\" encoding=\"gb2312\" ?>");
            buff.append("<USERINFO>");
            buff.append("<USER_ID>gly</USER_ID>");
            buff.append("<USER_PWD>888888</USER_PWD>");
            buff.append("<APP_ID>1</APP_ID>");
            buff.append("</USERINFO>");
            soapObject.addProperty("itype", 110);
            soapObject.addProperty("isubtype", 1);
            String strByte = Base64.encode(buff.toString().getBytes("gb2312"));
            soapObject.addProperty("pdata", strByte);
            envelope.bodyOut = soapObject;
           // envelope.headerOut = header;
            new MarshalBase64().register(envelope);
            List<HeaderProperty> respHeaders = httpSE.call(null, envelope, reqHeaders);
            for (HeaderProperty hp : respHeaders) {
                if (hp != null && hp.getKey() != null && hp.getKey().equalsIgnoreCase("userid")) {
                    mUserIdHeader = hp;
                }
                if (hp != null && hp.getKey() != null && hp.getKey().equalsIgnoreCase("sessionid")) {
                    mSessionHeader = hp;
                }
            }
            if (envelope.getResponse() != null) {
                Object Response =httpSE.responseDump;
                SoapObject resultBodyIn = (SoapObject) envelope.bodyIn;
                int result = Integer.parseInt(String.valueOf(resultBodyIn.getProperty("iresult")));
                if (result == 0) {
                    SoapObject resultPtablist = (SoapObject) resultBodyIn.getProperty("ptableList");
                    int countTabListCont = resultPtablist.getPropertyCount();
                    SoapObject subObject;
                    for(int i=0;i<countTabListCont;i++){
                        subObject = (SoapObject) resultPtablist.getProperty(i);
                        if (subObject.hasProperty("pfield")){
                            desList.add(subObject.toString());
                        }
                        if(subObject.hasProperty("cols")){
                            rowsList.add(subObject.toString());
                        }
                    }
                    ServiceDataConver.rowsDataConver(desList, rowsList);
                    Element[] headerIn = envelope.headerIn;

                }

            }
        } catch (SoapFault e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
