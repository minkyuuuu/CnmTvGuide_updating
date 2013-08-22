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

public class ADULT_AUTH_PARSER {
	
	private final static String DEBUG_TAG = "ADULT_AUTH_PARSER";
	
	private static String XML_URI;
	private static String Name;
	private static String IdenKey;
	// XML Depth
	// ROOT 1
	private final static String XML_ROOT			=	"VODAuth_Root";
	// deth 2
	//private final static String XML_RESULT			=	"response";
	private final static String XML_RESULT_CODE			= "resultCode";
	private final static String XML_RESULT_ERRSTRING	= "errString";

	private static ADULT_AUTH_DATA ResultData;
	
	/*
	public ADULT_AUTH_PARSER(String url, String name, String idekey) {
		// TODO Auto-generated constructor stub
		XML_URI	= url;
		Name= name;
		IdenKey = idekey;
		Log.i(DEBUG_TAG, "Call CreateParser :" + url);
	}
	*/
	
	public ADULT_AUTH_PARSER(String url) {
		// TODO Auto-generated constructor stub
		XML_URI	= url;
		ResultData = new ADULT_AUTH_DATA();
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
				//xpp.setInput(new URL(XML_URI).openStream(), "UTF-8");
			}else {
				return false;
			}
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_DOCUMENT) {
					//System.out.println("Start document");
				} else if(eventType == XmlPullParser.END_DOCUMENT) {
					//System.out.println("End document");
				} else if(eventType == XmlPullParser.START_TAG) {
					//System.out.println("Start tag "+xpp.getName());
					if (xpp.getName().equals(XML_RESULT_CODE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							ResultData.setResult(xpp.getText());
						Log.d(DEBUG_TAG, xpp.getText());
					}
					else if (xpp.getName().equals(XML_RESULT_ERRSTRING)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							ResultData.setResultCode(xpp.getText());
						Log.d(DEBUG_TAG, xpp.getText());
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
	
	
	private static String PostData(String ... params) throws ClientProtocolException, IOException {  
        // TODO Auto-generated method stub  
        HttpPost request = mHttpPost(params) ;  
        
        HttpClient client = new DefaultHttpClient();  
        HttpParams param = client.getParams();
        HttpConnectionParams.setConnectionTimeout(param, XML_Address_Define.XML_CONNETION_TIME_OUT);
        HttpConnectionParams.setSoTimeout(param, XML_Address_Define.XML_CONNETION_TIME_OUT);
        ResponseHandler<String> reshandler = new BasicResponseHandler() ;  
        String result = client.execute( request, reshandler ) ;  
        return result;  
    }  
    
    private static HttpPost mHttpPost(String ... params)
    {
    	
    		HttpPost request = new HttpPost(params[0]) ;
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>() ;  
        
    		nameValue.add( new BasicNameValuePair( "UserInfo=", params[1] ) ) ;
//        nameValue.add( new BasicNameValuePair( "password", params[2] ) ) ;  
        request.setEntity( makeEntity(nameValue) ) ;  
        
        return request ;  
    		
    }
    
    private static HttpEntity makeEntity(Vector<NameValuePair> nameValue) {
		// TODO Auto-generated method stub
		HttpEntity result = null ;  
        try {  
            result = new UrlEncodedFormEntity( nameValue ) ;  
        } catch (UnsupportedEncodingException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return result ;  
	}

	public ADULT_AUTH_DATA getResultData()
	{
		return ResultData;
	}
	
}
