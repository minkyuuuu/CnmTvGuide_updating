package hdlnc.dev2.sangtae.cnm.global.DATA;

import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

//import android.util.Log;

public class CHECK_RESPONSE_PASER {
	// URL
	private static String XML_URI	=	"";

	// XML Depth
	// ROOT 1
	private final static String XML_ROOT = "response";
	// deth 2
	private final static String XML_VERSION = "version";
	private final static String XML_TRANSACTION_ID = "transactionId";
	private final static String XML_RESULT_CODE = "resultCode";
	private final static String XML_ERROR_STRING = "errorString";
	//private final static String XML_MAC_ADDRESS = "MacAddress";
	private final static String XML_SO_ID 		= "soId";
	private final static String XML_IP_ADRRESS = "IpAddress";
	private final static String XML_SETTOPBOX_KIND = "SetTopBoxKind";
	
	private static RESPONSE_DATA mArrayList;

	public static Boolean start(String url) {
		XmlPullParserFactory factory;
		XML_URI = url;
		try {
			factory = XmlPullParserFactory.newInstance();
			
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, XML_Address_Define.XML_CONNETION_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, XML_Address_Define.XML_CONNETION_TIME_OUT);

			HttpGet httpGet =  new HttpGet(XML_URI);
			HttpResponse httpResponse = null;
			httpResponse = httpclient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			
			if (httpEntity != null) {
				xpp.setInput(httpEntity.getContent(), null);
			}else {
				return false;
			}
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (xpp.getName() != null) {
					if (xpp.getName().equals(XML_ROOT)) {
						if (mArrayList == null) 
							mArrayList	=	new RESPONSE_DATA();
					} else if (xpp.getName().equals(XML_VERSION)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mArrayList.setVersion(xpp.getText());
					} else if (xpp.getName().equals(XML_TRANSACTION_ID)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mArrayList.setTransactionId(xpp.getText());
					} else if (xpp.getName().equals(XML_RESULT_CODE) || xpp.getName().equals("Result")
								 || xpp.getName().equals("result")) {

						eventType = xpp.next();
						if(xpp.getText() != null)
							mArrayList.setResultCode(xpp.getText());
					} else if (xpp.getName().equals(XML_ERROR_STRING)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mArrayList.setMacAddress(xpp.getText());
					} else if (xpp.getName().equals(XML_SO_ID)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mArrayList.setSoId(xpp.getText());
					} else if (xpp.getName().equals(XML_IP_ADRRESS)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mArrayList.setIpAddress(xpp.getText());
					} else if (xpp.getName().equals(XML_SETTOPBOX_KIND)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mArrayList.setSetTopBoxKind(xpp.getText());
					}
					eventType = xpp.next();
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {	// 파싱 중 발생하는 예외 상황을 받다.

			e.printStackTrace();
		} catch (IOException e) {				// 통신과 관련된 에외 상황을 받는다.

			e.printStackTrace();
		}
		
		return true;
	}

	public static RESPONSE_DATA getArrayList() {
		return mArrayList;
	}
}
