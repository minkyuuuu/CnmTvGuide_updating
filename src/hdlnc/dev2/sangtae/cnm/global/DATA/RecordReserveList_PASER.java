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

// 예약 녹화 목록
public class RecordReserveList_PASER {
	// URL
	private static String XML_URI;
	// XML Depth
	// ROOT 1
	private final static String XML_ROOT			=	"response";
	private final static String XML_RESULT_CODE		=	"resultCode";
	private final static String XML_ERROR_STRING	=	"errorString";
	// deth 2
	private final static String XML_RESERVE_ITEM	=	"Reserve_Item";
	// deth 3
	private final static String XML_SERIES_ID			=	"SeriesId";	
	private final static String XML_SCHEDULE_ID			=	"ScheduleId";	
	private final static String XML_CHANNEL_ID			=	"ChannelId";
	private final static String XML_CHANNEL_NO			=	"ChannelNo";	
	private final static String XML_CHANNEL_NAME		=	"ChannelName";	
	private final static String XML_PROGRAM_GRADE		=	"Program_Grade";	
	private final static String XML_CHANNEL_LOGO_IMG	=	"Channel_logo_img";	
	private final static String XML_PROGRAM_NAME		=	"ProgramName";	
	private final static String XML_RECORD_START_TIME	=	"RecordStartTime";	
	private final static String XML_RECORD_END_TIME		=	"RecordEndTime";	
	private final static String XML_RECORD_HD			=	"RecordHD";	
	private final static String XML_RECORD_ID			=	"RecordId";	
	private final static String XML_RECORDING_TYPE		=	"RecordingType";	
	private final static String XML_RECORD_PROTECTION	=	"RecordProtection";
	private final static String XML_RECORD_PLAY_TYPE	=	"RecordPaytype";
	private final static String XML_RECORD_STATUS		=	"RecordStatus";

	private static RECORD_DATA_LIST mRecord_LIST;

	public RecordReserveList_PASER(String aURL) {
		// TODO Auto-generated constructor stub
		mRecord_LIST	=	new RECORD_DATA_LIST();
		XML_URI	= aURL;
	}

	public Boolean start() {
		Integer	mCurrentCount=0;
		mRecord_LIST.clear();
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();

			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			//Log.e("Sangtae", XML_URI);
			
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
					//System.out.println("Start tag "+xpp.getName());

					if (xpp.getName().equals(XML_RESULT_CODE)) {
						eventType = xpp.next();
						mRecord_LIST.setResultCode(xpp.getText());
					} else if (xpp.getName().equals(XML_ERROR_STRING)) {
						eventType = xpp.next();
						mRecord_LIST.setErrorString(xpp.getText());
					} else if (xpp.getName().equals(XML_RESERVE_ITEM)) {
						//eventType = xpp.next();
						mRecord_LIST.add(new RECORD_DATA());			// 리스트 추가
						mCurrentCount = mRecord_LIST.size()-1;			// 현재 추가된 리스트의 위치 반환
					} else if (xpp.getName().equals(XML_RECORDING_TYPE)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setRecordingType(xpp.getText());
					} else if (xpp.getName().equals(XML_SERIES_ID)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setSeriesId(xpp.getText());
					} else if (xpp.getName().equals(XML_SCHEDULE_ID)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setScheduleId(xpp.getText());
					} else if (xpp.getName().equals(XML_CHANNEL_ID)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setChannelId(xpp.getText());
					} else if (xpp.getName().equals(XML_CHANNEL_NO)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setChannelNo(xpp.getText());
					} else if (xpp.getName().equals(XML_CHANNEL_NAME)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setChannelName(xpp.getText());
					} else if (xpp.getName().equals(XML_PROGRAM_GRADE)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setProgram_Grade(xpp.getText());
					} else if (xpp.getName().equals(XML_CHANNEL_LOGO_IMG)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setChannel_logo_img(xpp.getText());
					} else if (xpp.getName().equals(XML_PROGRAM_NAME)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setProgramName(xpp.getText());
					} else if (xpp.getName().equals(XML_RECORD_START_TIME)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setRecordStartTime(xpp.getText());
					} else if (xpp.getName().equals(XML_RECORD_END_TIME)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setRecordEndTime(xpp.getText());
					} else if (xpp.getName().equals(XML_RECORD_HD)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setRecordHD(xpp.getText());
					} else if (xpp.getName().equals(XML_RECORD_ID)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setRecordId(xpp.getText());
					} else if (xpp.getName().equals(XML_RECORDING_TYPE)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setRecordingType(xpp.getText());
					} else if (xpp.getName().equals(XML_RECORD_PROTECTION)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setRecordProtection(xpp.getText());
					} else if (xpp.getName().equals(XML_RECORD_PLAY_TYPE)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setRecordPaytype(xpp.getText());
					} else if (xpp.getName().equals(XML_RECORD_STATUS)) {
						eventType = xpp.next();
						mRecord_LIST.get(mCurrentCount).setRecordStatus(xpp.getText());
						eventType = xpp.next();
					}
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {	// 파싱 중 발생하는 예외 상황을 받다.
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {				// 통신과 관련된 에외 상황을 받는다.
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}


		return true;
	}


	public RECORD_DATA_LIST getRecord_LIST() {
		return mRecord_LIST;
	}

}
