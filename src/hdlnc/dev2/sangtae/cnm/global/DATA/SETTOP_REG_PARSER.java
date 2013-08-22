package hdlnc.dev2.sangtae.cnm.global.DATA;

import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class SETTOP_REG_PARSER {
	
private final static String DEBUG_TAG = "SETTOP_AUTH_PARSER";
	
	private static String XML_URI;
	// XML Depth
	// ROOT 1
	private final static String XML_ROOT			=	"vodauthcode_root";
	// deth 2
	private final static String XML_RESULT				= "resultCode";
	private final static String XML_RESULT_ERRSTRING	= "errorString";
	private final static String XML_RESULT_CODE			= "authcode";

	private static SETTOP_AUTH_DATA ResultData;
	
	public SETTOP_REG_PARSER(String url) {
		// TODO Auto-generated constructor stub
		XML_URI	= url;
		Log.i(DEBUG_TAG, "Call CreateParser :" + XML_URI);
	}
	
	public static Boolean start() {
		XmlPullParserFactory factory;
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
				xpp.setInput(httpEntity.getContent(), "UTF-8");
			}else {
				return false;
			}
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_DOCUMENT) {
					//System.out.println("Start document");
					ResultData = new SETTOP_AUTH_DATA();
				} else if(eventType == XmlPullParser.END_DOCUMENT) {
					//System.out.println("End document");
				} else if(eventType == XmlPullParser.START_TAG) {
					//System.out.println("Start tag "+xpp.getName());
					if (xpp.getName().equals(XML_RESULT)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							ResultData.setResult(xpp.getText());
					}
					else
					if (xpp.getName().equals(XML_RESULT_CODE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							ResultData.setResultCode(xpp.getText());
					}
					else
					if (xpp.getName().equals(XML_RESULT_ERRSTRING)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							ResultData.setErrString(xpp.getText());
					}
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {	// 파싱 중 발생하는 예외 상황을 받다.
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {				// 통신과 관련된 에외 상황을 받는다.
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	

	public SETTOP_AUTH_DATA getResultData()
	{
		return ResultData;
	}

	
}
