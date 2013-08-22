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

import android.util.Log;

public class SERVICE_NOTICE_PARSER {

	private final static String DEBUG_TAG = "SERVICE_NOTICE_PARSER";
	
// URL
//	private final static String XML_URI	=	"http://mooneyes.textcube.com/26/attach/vod_test_4.xml";		// Local testServer
	private String XML_URI	=	"";

	// XML Depth
	// ROOT 1
//	private final static String XML_ROOT = "Root";
	// deth 2
	private final static String XML_RESULT = "result";
	private final static String XML_NOTICE_ITEM = "Notice_Item";
	// deth 3
	private final static String XML_NOTICE_ID = "noticeId";
	private final static String XML_NOTICE_TITLE = "notice_Title";
	private final static String XML_NOTICE_LOCATION = "notice_Location";
	private final static String XML_NOTICE_STARTDATE = "notice_StartDate";
	private final static String XML_NOTICE_ENDDATE = "notice_EndDate";
	private final static String XML_NOTICE_CONTENT = "notice_Content";

	private SERVICE_NOTICE_LIST mArrayList;

	public SERVICE_NOTICE_PARSER(String url) {
		mArrayList	=	new SERVICE_NOTICE_LIST();
		
		XML_URI = url;
	}

	public Boolean start() {
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
				} else if(eventType == XmlPullParser.END_DOCUMENT) {
					//System.out.println("End document");
				} else if(eventType == XmlPullParser.START_TAG) {
					Log.d(DEBUG_TAG, "Start tag : "+xpp.getName());
					//System.out.println("Start tag "+xpp.getName());

					if (xpp.getName().equals(XML_RESULT)) {
						eventType = xpp.next();
						mArrayList.setRESULT(xpp.getText());
					} else if (xpp.getName().equals(XML_NOTICE_ITEM)) {
						eventType = xpp.next();
						mArrayList.add(new SERVICE_NOTICE_INFO());			// 리스트 추가
						mCurrentCount = mArrayList.size()-1;			// 현재 추가된 리스트의 위치 반환
					} else if (xpp.getName().equals(XML_NOTICE_ID)) {
						eventType = xpp.next();
						Log.d(DEBUG_TAG, "getText : "+xpp.getText());
						mArrayList.get(mCurrentCount).setID(xpp.getText().trim());
					} else if (xpp.getName().equals(XML_NOTICE_TITLE)) {
						eventType = xpp.next();
						Log.d(DEBUG_TAG, "getText : "+xpp.getText());
						mArrayList.get(mCurrentCount).setTitle(xpp.getText());
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_NOTICE_CONTENT)) {
						eventType = xpp.next();
						Log.d(DEBUG_TAG, "getText : "+xpp.getText());
						mArrayList.get(mCurrentCount).setContent(xpp.getText());
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_NOTICE_STARTDATE)) {
						eventType = xpp.next();
						Log.d(DEBUG_TAG, "getText : "+xpp.getText());
						mArrayList.get(mCurrentCount).setStartDate(xpp.getText().trim());
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_NOTICE_ENDDATE)) {
						eventType = xpp.next();
						Log.d(DEBUG_TAG, "getText : "+xpp.getText());
						mArrayList.get(mCurrentCount).setEndDate(xpp.getText().trim());
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_NOTICE_LOCATION)) {
						eventType = xpp.next();
						Log.d(DEBUG_TAG, "getText : "+xpp.getText());
						mArrayList.get(mCurrentCount).setLocation(xpp.getText().trim());
						eventType = xpp.next();
					}
					
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

	public  SERVICE_NOTICE_LIST getArrayList() {
		return mArrayList;
	}
}
