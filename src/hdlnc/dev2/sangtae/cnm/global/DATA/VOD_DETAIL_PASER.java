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

public class VOD_DETAIL_PASER {
	// URL
	private static String XML_URI="";

	// XML Depth
	// ROOT 1
//	private final static String XML_ROOT = "Root";
	// deth 2
	private final static String XML_RESULT_CODE = "resultCode";
	private final static String XML_VOD_ITEM = "Tag_Item";
	//private final static String XML_VOD_ITEM = "Tag_Item";
	// deth 3
	private final static String XML_VOD_ID = "VOD_ID";
	private final static String XML_VOD_CATEGORY = "VOD_CATEGORY";
	private final static String XML_VOD_TAG = "VOD_TAG";
	private final static String XML_VOD_TITLE = "VOD_Title";
	private final static String XML_VOD_POSTER_URL = "VOD_IMG";
	private final static String XML_VOD_VIDEO_URL = "VOD_Video_Path";
	private final static String XML_VOD_DIRECTOR = "VOD_Director";
	private final static String XML_VOD_ACTOR = "VOD_Actor";
	private final static String XML_VOD_GRADE = "VOD_Grade";
	private final static String XML_VOD_HD = "VOD_HD";
	private final static String XML_VOD_PRICE = "VOD_Price";
	private final static String XML_VOD_RTIME = "VOD_Duration";
	private final static String XML_VOD_CONTENTS = "VOD_Contents";
	private final static String XML_VOD_MORE = "VOD_More";

	private static VOD_PREVIEW_LIST mArrayList;

	public static Boolean start(String url) {
		Integer	mCurrentCount=0;
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
				if(eventType == XmlPullParser.START_DOCUMENT) {
					//System.out.println("Start document");
					mArrayList	=	new VOD_PREVIEW_LIST();
					
				} else if(eventType == XmlPullParser.END_DOCUMENT) {
					//System.out.println("End document");
				} else if(eventType == XmlPullParser.START_TAG) {
					//System.out.println("Start tag "+xpp.getName());

					if (xpp.getName().equals(XML_RESULT_CODE)) {
						eventType = xpp.next();
						mArrayList.setRESULT(xpp.getText());
						Log.d("VOD_DETAIL_PASER", "Result: " + xpp.getText());
					} else if (xpp.getName().equals(XML_VOD_ITEM)) {
//						eventType = xpp.next();
						mArrayList.add(new VOD_INFO());			// 리스트 추가
						mCurrentCount = mArrayList.size()-1;			// 현재 추가된 리스트의 위치 반환
					} else if (xpp.getName().equals(XML_VOD_ID)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mArrayList.get(mCurrentCount).setID(xpp.getText().trim());
					} else if (xpp.getName().equals(XML_VOD_TITLE)) {
						eventType = xpp.next();
						if(xpp.getText() != null) {
							mArrayList.get(mCurrentCount).setTitle(xpp.getText());
							Log.d("VOD_DETAIL_PASER", "Title: " + xpp.getText());
						}
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_VOD_MORE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mArrayList.get(mCurrentCount).setMore(xpp.getText().trim());
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_VOD_ACTOR)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mArrayList.get(mCurrentCount).setActor(xpp.getText());
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_VOD_CONTENTS)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mArrayList.get(mCurrentCount).setContents(xpp.getText());
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_VOD_DIRECTOR)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mArrayList.get(mCurrentCount).setDirector(xpp.getText());
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_VOD_GRADE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mArrayList.get(mCurrentCount).setGrade(xpp.getText().trim());
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_VOD_POSTER_URL)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mArrayList.get(mCurrentCount).setPosterUrl(xpp.getText().trim());
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_VOD_RTIME)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mArrayList.get(mCurrentCount).setRunningTime(xpp.getText());
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_VOD_VIDEO_URL)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mArrayList.get(mCurrentCount).setVideoUrl(xpp.getText().trim());
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_VOD_TAG)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mArrayList.get(mCurrentCount).setTag(xpp.getText().trim());
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_VOD_HD)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mArrayList.get(mCurrentCount).setHD(xpp.getText().trim());
						eventType = xpp.next();
					}else if (xpp.getName().equals(XML_VOD_PRICE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mArrayList.get(mCurrentCount).setPrice(xpp.getText());
						eventType = xpp.next();
					}else if (xpp.getName().equals(XML_VOD_CATEGORY)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mArrayList.get(mCurrentCount).setCATEGORY(xpp.getText());
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

	public static VOD_PREVIEW_LIST getArrayList() {
		return mArrayList;
	}
}
