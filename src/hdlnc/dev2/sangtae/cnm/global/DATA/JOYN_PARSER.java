package hdlnc.dev2.sangtae.cnm.global.DATA;


import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;

import java.io.IOException;
import java.net.URL;

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


import android.util.Log;

public class JOYN_PARSER {
	
	CNMApplication App;
	// URL
	//final static String XML_URI	=	"http://121.126.13.202:8080/MAMP/allchannel.xml";		// Local testServer
	private static String XML_URI;
	// XML Depth
	// ROOT 1
	private final static String XML_ROOT			=	"JoyList_Root";
	// deth 2
	private final static String XML_RESULT_CODE			=	"resultCode";
	private final static String XML_JOY_LIST_ITEM	=	"JoyList_Item";
	// deth 3
	private final static String XML_ID				=	"Joy_ID";	
	private final static String XML_TITLE			=	"Joy_Title";  
	private final static String XML_SUB_TITLE			=	"Joy_SubTitle";  
	private final static String XML_CONTENT			=	"Joy_Content";	
	private final static String XML_THUMBNAIL_IMG	=	"Joy_Thumbnail_Img";	

	private static JOYN_DATA_LIST mJOYN_DATA_LIST;

	public JOYN_PARSER(String url) {
		// TODO Auto-generated constructor stub
		XML_URI	= url;
	}

	public static Boolean start() {
		Integer	mCurrentCount=0;
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
				xpp.setInput(httpEntity.getContent(), null);
			}else {
				return false;
			}
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_DOCUMENT) {
					//System.out.println("Start document");
					mJOYN_DATA_LIST	=	new JOYN_DATA_LIST();
				} else if(eventType == XmlPullParser.END_DOCUMENT) {
					//System.out.println("End document");
				} else if(eventType == XmlPullParser.START_TAG) {
					//System.out.println("Start tag "+xpp.getName());

					if (xpp.getName().equals(XML_RESULT_CODE)) {
						eventType = xpp.next();
						mJOYN_DATA_LIST.setRESULT(xpp.getText());
					} else if (xpp.getName().equals(XML_JOY_LIST_ITEM)) {
						//eventType = xpp.next();
						mJOYN_DATA_LIST.add(new JOYN_DATA());
						mCurrentCount = mJOYN_DATA_LIST.size()-1;
					} else if (xpp.getName().equals(XML_ID)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mJOYN_DATA_LIST.get(mCurrentCount).setID(xpp.getText());
					} else if (xpp.getName().equals(XML_TITLE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mJOYN_DATA_LIST.get(mCurrentCount).setTITLE(xpp.getText());
					} else if (xpp.getName().equals(XML_SUB_TITLE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mJOYN_DATA_LIST.get(mCurrentCount).setSUBTITLE(xpp.getText());
					} else if (xpp.getName().equals(XML_CONTENT)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mJOYN_DATA_LIST.get(mCurrentCount).setCONTENT(xpp.getText());
					} else if (xpp.getName().equals(XML_THUMBNAIL_IMG)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mJOYN_DATA_LIST.get(mCurrentCount).setTHUMBNAIL_IMG(xpp.getText());
						eventType = xpp.next();
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


	public static JOYN_DATA_LIST getJOYN_DATA_LIST() {
		return mJOYN_DATA_LIST;
	}

}
