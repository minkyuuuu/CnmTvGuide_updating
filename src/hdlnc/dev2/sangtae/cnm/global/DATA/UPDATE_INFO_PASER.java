package hdlnc.dev2.sangtae.cnm.global.DATA;

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

public class UPDATE_INFO_PASER {
	// URL
	private static String XML_URI	=	"http://58.143.243.91/Mobile/UpdateInfo.asp";
	// XML Depth
	// ROOT 1
	private final static String XML_ROOT			=	"Table_UpdateInfo_Root";
	// deth 2
	private final static String XML_RESULT			=	"result";
	private final static String XML_CHANNEL_ITEM	=	"Table_UpdateInfo_Item";
	// deth 3
	private final static String XML_ID				=	"Table_ID";	
	private final static String XML_NAME			=	"Table_Name";	
	private final static String XML_DATE			=	"UpdateDate";	
	
	private static SCUEDULE_DATA_LIST mScuedule_DATA_LIST;
	
	public UPDATE_INFO_PASER() {
		// TODO Auto-generated constructor stub
		mScuedule_DATA_LIST	=	new SCUEDULE_DATA_LIST();
	}
	
	public static Boolean start(String Program_ID) {
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

			HttpGet httpGet =  new HttpGet(XML_URI+Program_ID);
			HttpResponse httpResponse = null;
			httpResponse = httpclient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			
			if (httpEntity != null) {
				xpp.setInput(httpEntity.getContent(), null);
				//xpp.setInput(new URL(XML_URI+Program_ID).openStream(), null);
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
			
					if (xpp.getName().equals(XML_RESULT)) {
						eventType = xpp.next();
						mScuedule_DATA_LIST.setRESULT(xpp.getText());
					} else if (xpp.getName().equals(XML_CHANNEL_ITEM)) {
						//eventType = xpp.next();
						mScuedule_DATA_LIST.add(new SCUEDULE_DATA());			// 리스트 추가
						mCurrentCount = mScuedule_DATA_LIST.size()-1;			// 현재 추가된 리스트의 위치 반환
					} else if (xpp.getName().equals(XML_ID)) {
						eventType = xpp.next();
						mScuedule_DATA_LIST.get(mCurrentCount).setID(xpp.getText());
					} else if (xpp.getName().equals(XML_DATE)) {
						eventType = xpp.next();
						mScuedule_DATA_LIST.get(mCurrentCount).setDate(xpp.getText());
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
	
	public static SCUEDULE_DATA_LIST getScuedule_DATA_LIST() {
		return mScuedule_DATA_LIST;
	}
	
}
