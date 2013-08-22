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

public class CHECK_SETTOPSTATUS_PASER {
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
	private final static String XML_MAC_ADDRESS = "MacAddress";
	private final static String XML_RESULT		= "result";
	private final static String XML_STATE = "state";
	private final static String XML_ERR_CODE = "errcode";
	private final static String XML_TOTAL_SIZE	= "totalsize";
	private final static String XML_USAGE_SIZE = "usagesize";
	private final static String XML_RECORDING_CHANNEL1 = "recordingchannel1";
	private final static String XML_RECORDING_CHANNEL2 		= "recordingchannel2";
	private final static String XML_WATCHING_CHANNEL = "watchingchannel";
	private final static String XML_PIP_CHANNEL = "pipchannel";

	private static SettopStatus_DATA mData;

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
						if (mData == null) 
							mData	=	new SettopStatus_DATA();
					} else if (xpp.getName().equals(XML_VERSION)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mData.setVersion(xpp.getText());
					} else if (xpp.getName().equals(XML_TRANSACTION_ID)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mData.setTransactionId(xpp.getText());
					} else if (xpp.getName().equals(XML_RESULT_CODE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mData.setResultCode(xpp.getText());
					} else if (xpp.getName().equals(XML_ERROR_STRING)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mData.setErrorString(xpp.getText());
					} else if (xpp.getName().equals(XML_MAC_ADDRESS)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mData.setMacAddress(xpp.getText());
					} else if (xpp.getName().equals(XML_RESULT)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mData.setResult(xpp.getText());
					} else if (xpp.getName().equals(XML_STATE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mData.setState(xpp.getText());
					} else if (xpp.getName().equals(XML_ERR_CODE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mData.setErrcode(xpp.getText());
					} else if (xpp.getName().equals(XML_TOTAL_SIZE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mData.setTotalsize(xpp.getText());
					} else if (xpp.getName().equals(XML_USAGE_SIZE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mData.setUsagesize(xpp.getText());
					} else if (xpp.getName().equals(XML_RECORDING_CHANNEL1)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mData.setRecordingchannel1(xpp.getText());
					} else if (xpp.getName().equals(XML_RECORDING_CHANNEL2)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mData.setRecordingchannel2(xpp.getText());
					} else if (xpp.getName().equals(XML_WATCHING_CHANNEL)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mData.setWatchingchannel(xpp.getText());
					} else if (xpp.getName().equals(XML_PIP_CHANNEL)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mData.setPipchannel(xpp.getText());
					}
					eventType = xpp.next();
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {	// 파싱 중 발생하는 예외 상황을 받다.
			e.printStackTrace();
			return false;
		} catch (IOException e) {				// 통신과 관련된 에외 상황을 받는다.
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static SettopStatus_DATA getData() {
		return mData;
	}
}
