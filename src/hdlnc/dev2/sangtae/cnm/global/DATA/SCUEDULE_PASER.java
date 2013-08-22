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

import android.R.string;

public class SCUEDULE_PASER {
	// URL
	//private static String XML_URI	=	XML_Address_Define.Channel.//"http://58.143.243.91/Mobile/Schedule.asp?Channel_ID=";
	// XML Depth
	// ROOT 1
	private final static String XML_ROOT			=	"Detail_Channel_Root";
	// deth 2
	private final static String XML_RESULT			=	"resultCode";
	private final static String XML_COUNT			=	"DateCount";
	private final static String XML_CHANNEL_ITEM	=	"Channel_Item";
	// deth 3
	private final static String XML_Seq				=	"Schedule_seq";
	private final static String XML_HD				=	"Program_HD";
	private final static String XML_Grade			=	"Program_Grade";
	private final static String XML_ID				=	"Program_ID";	
	private final static String XML_TITLE			=	"Program_Title";	
	private final static String XML_CONTENT			=	"Program_Content";	
	private final static String XML_DATE			=	"Broadcasting_Date";	
	private final static String XML_TIME			=	"Program_Broadcasting_Time";		
	private final static String XML_SERIES_ID		=	"SeriesId";
	private final static String XML_PROGRAM_KEY		=	"Program_ProgramKey";
	
	private static SCUEDULE_DATA_LIST mScuedule_DATA_LIST;
	
	public SCUEDULE_PASER() {
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

			//xpp.setInput(new URL(XML_URI+Program_ID).openStream(), null);
			//String temp =XML_Address_Define.Channel.getGetchannelschedule(Program_ID, "");
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, XML_Address_Define.XML_CONNETION_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, XML_Address_Define.XML_CONNETION_TIME_OUT);

			HttpGet httpGet =  new HttpGet(XML_Address_Define.Channel.getGetchannelschedule(Program_ID, ""));
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
					//System.out.println("Start tag "+xpp.getName());
			
					if (xpp.getName().equals(XML_RESULT)) {
						eventType = xpp.next();
						mScuedule_DATA_LIST.setRESULT(xpp.getText());
					} else if (xpp.getName().equals(XML_COUNT)) {
						eventType = xpp.next();
						mScuedule_DATA_LIST.setCount(Integer.parseInt(xpp.getText()));
					} else if (xpp.getName().equals(XML_CHANNEL_ITEM)) {
						//eventType = xpp.next();
						mScuedule_DATA_LIST.add(new SCUEDULE_DATA());			// 리스트 추가
						mCurrentCount = mScuedule_DATA_LIST.size()-1;			// 현재 추가된 리스트의 위치 반환
					} else if (xpp.getName().equals(XML_Seq)) {
						eventType = xpp.next();
						mScuedule_DATA_LIST.get(mCurrentCount).setSeq(xpp.getText());
					} else if (xpp.getName().equals(XML_HD)) {
						eventType = xpp.next();
						if (xpp.getText().equals("YES")) {
							mScuedule_DATA_LIST.get(mCurrentCount).setHD(true);
						} else {
							mScuedule_DATA_LIST.get(mCurrentCount).setHD(false);
						}
					} else if (xpp.getName().equals(XML_Grade)) {
						eventType = xpp.next();
						mScuedule_DATA_LIST.get(mCurrentCount).setGrade(xpp.getText());
					} else if (xpp.getName().equals(XML_ID)) {
						eventType = xpp.next();
						mScuedule_DATA_LIST.get(mCurrentCount).setID(xpp.getText());
					} else if (xpp.getName().equals(XML_TITLE)) {
						eventType = xpp.next();
						mScuedule_DATA_LIST.get(mCurrentCount).setTitle(xpp.getText());
					} else if (xpp.getName().equals(XML_CONTENT)) {
						eventType = xpp.next();
						mScuedule_DATA_LIST.get(mCurrentCount).setContent(xpp.getText());
					} else if (xpp.getName().equals(XML_DATE)) {
						eventType = xpp.next();
						mScuedule_DATA_LIST.get(mCurrentCount).setDate(xpp.getText());
					} else if (xpp.getName().equals(XML_TIME)) {
						eventType = xpp.next();
						mScuedule_DATA_LIST.get(mCurrentCount).setTime(xpp.getText());
					} else if (xpp.getName().equals(XML_SERIES_ID)) {
						eventType = xpp.next();
						mScuedule_DATA_LIST.get(mCurrentCount).setSeriesId(xpp.getText());
					} else if (xpp.getName().equals(XML_PROGRAM_KEY)) {
						eventType = xpp.next();
						mScuedule_DATA_LIST.get(mCurrentCount).setProgramKey(xpp.getText());
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
	
	public static SCUEDULE_DATA_LIST getScuedule_DATA_LIST() {
		return mScuedule_DATA_LIST;
	}
	
}
