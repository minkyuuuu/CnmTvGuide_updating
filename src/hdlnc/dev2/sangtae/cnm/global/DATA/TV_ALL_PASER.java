package hdlnc.dev2.sangtae.cnm.global.DATA;

import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;

import java.io.IOException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
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

public class TV_ALL_PASER {
	// URL
	//final static String XML_URI	=	"http://121.126.13.202:8080/MAMP/allchannel.xml";		// Local testServer
	private  String XML_URI;
	// XML Depth
	// ROOT 1
	//private final static String XML_ROOT			=	"All_Channel_Root";
	private final static String XML_RESULT_CODE		=	"resultCode";
	private final static String XML_ERROR_STRING	=	"errorString";
	private final static String XML_GENRE_CODE		=	"genreCode ";
	// deth 2
	private final static String XML_CHANNEL_ITEM	=	"All_Channel_Item";
	private final static String XML_GENRE_ITEM		=	"Genre_Channel_Item";
	// deth 3
	private final static String XML_ID				=	"Channel_ID";	
	private final static String XML_NUMBER			=	"Channel_number";	
	private final static String XML_NAME			=	"Channel_name";	
	private final static String XML_INFO			=	"Channel_info";
	private final static String XML_onAIR_HD		=	"Channel_onAir_HD";	
	private final static String XML_PROGRAM_ID		=	"Channel_Program_onAir_ID";	
	private final static String XML_onAIR_TIME		=	"Channel_Program_onAir_Time";	
	private final static String XML_onAIR_TITLE		=	"Channel_Program_onAir_Title";	
	private final static String XML_PROGRAM_nextID	=	"Channel_Program_next_ID";	
	private final static String XML_NEXT_TIME		=	"Channel_Program_next_Time";	
	private final static String XML_NEXT_TITLE		=	"Channel_Program_next_Title";	
	private final static String XML_LOGE_IMG		=	"Channel_logo_img";	
	private final static String XML_CHANNEL_VIEW	=	"Channel_View";
	private final static String XML_CHANNEL_RECORDING	=	"Channel_Recording";

	private  TV_ALL_DATA_LIST mTV_ALL_DATA_LIST;

	public  Boolean start(String aURL) {
		Integer	mCurrentCount=0;
		if (mTV_ALL_DATA_LIST != null)
			mTV_ALL_DATA_LIST.clear();
		XmlPullParserFactory factory;
		
		XML_URI	= aURL;
	
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
					mTV_ALL_DATA_LIST	=	new TV_ALL_DATA_LIST();

				} else if(eventType == XmlPullParser.END_DOCUMENT) {
					//System.out.println("End document");
				} else if(eventType == XmlPullParser.START_TAG) {
					//System.out.println("Start tag "+xpp.getName());

					if (xpp.getName().equals(XML_RESULT_CODE)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.setResultCode(xpp.getText());
					} else if (xpp.getName().equals(XML_ERROR_STRING)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.setErrorString(xpp.getText());
					} else if (xpp.getName().equals(XML_GENRE_CODE)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.setGenreCode(xpp.getText());
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.setGenreCode(xpp.getText());
					} else if (xpp.getName().equals(XML_CHANNEL_ITEM) || xpp.getName().equals(XML_GENRE_ITEM)) {
						//eventType = xpp.next();
						mTV_ALL_DATA_LIST.add(new TV_ALL_DATA());			// 리스트 추가
						mCurrentCount = mTV_ALL_DATA_LIST.size()-1;			// 현재 추가된 리스트의 위치 반환
					} else if (xpp.getName().equals(XML_ID)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setID(xpp.getText());
					} else if (xpp.getName().equals(XML_NUMBER)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setNUMBER(xpp.getText());
					} else if (xpp.getName().equals(XML_NAME)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setNAME(xpp.getText());
					} else if (xpp.getName().equals(XML_INFO)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setINFO(xpp.getText());
					} else if (xpp.getName().equals(XML_onAIR_HD)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setPROGRAM_onAIR_HD(xpp.getText());
					} else if (xpp.getName().equals(XML_PROGRAM_ID)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setPROGRAM_onAIR_ID(xpp.getText());
					} else if (xpp.getName().equals(XML_onAIR_TIME)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setCHANNEL_onAir_TIME(xpp.getText());
					} else if (xpp.getName().equals(XML_onAIR_TITLE)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setCHANNEL_onAIR_TITLE(xpp.getText());
					} else if (xpp.getName().equals(XML_PROGRAM_nextID)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setPROGRAM_nextID(xpp.getText());
					} else if (xpp.getName().equals(XML_NEXT_TIME)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setCHANNEL_nextTIME(xpp.getText());
					} else if (xpp.getName().equals(XML_NEXT_TITLE)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setCHANNEL_nextTITLE(xpp.getText());
					} else if (xpp.getName().equals(XML_LOGE_IMG)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setCHANNEL_LOG_IMG(xpp.getText());
					} else if (xpp.getName().equals(XML_CHANNEL_VIEW)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setCHANNEL_VIEW(xpp.getText());
					} else if (xpp.getName().equals(XML_CHANNEL_RECORDING)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setCHANNEL_RECORDING(xpp.getText());
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


	public  TV_ALL_DATA_LIST getTV_ALL_DATA_LIST() {
		return mTV_ALL_DATA_LIST;
	}

}
