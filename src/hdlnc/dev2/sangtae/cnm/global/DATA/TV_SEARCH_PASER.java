package hdlnc.dev2.sangtae.cnm.global.DATA;

import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

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

public class TV_SEARCH_PASER {
	// URL
	//final static String XML_URI	=	"http://121.126.13.202:8080/MAMP/allchannel.xml";		// Local testServer
	private String XML_URI;
	// XML Depth
	// ROOT 1
//	private final static String XML_ROOT			=	"TvProgram_Result_Root";
	// deth 2
	private final static String XML_RESULT			=	"resultCode";
	//private final static String XML_CHANNEL			=	"Channel_info";
	private final static String XML_CHANNEL_ITEM	=	"ProgramSearch_Item";
	// deth 3
	private final static String XML_ID				=	"Channel_ID";	
	private final static String XML_NUMBER			=	"Channel_number";	
	private final static String XML_NAME			=	"Channel_name";	
	private final static String XML_INFO			=	"Channel_info";	
	private final static String XML_PROGRAM_ID		=	"Channel_Program_ID";	
	private final static String XML_PROGRAM_Seq		=	"Channel_Program_seq";
	private final static String XML_onAIR_TIME		=	"Channel_Program_Time";	
	private final static String XML_onAIR_TITLE		=	"Channel_Program_Title";
	private final static String XML_onAIR_HD		=	"Channel_Program_HD";
	private final static String XML_onAIR_Grade		=	"Channel_Program_Grade";	
	private final static String XML_NEXT_TIME		=	"Channel_Program_next_Time";	
	private final static String XML_LOGE_IMG		=	"Channel_logo_img";	
	//private final static String XML_CHANNEL_VIEW	=	"Channel_View";	

	private TV_ALL_DATA_LIST mTV_Search_DATA_LIST;

	public TV_SEARCH_PASER(String Location, String Product, String search) {

		mTV_Search_DATA_LIST	=	new TV_ALL_DATA_LIST();
		
		String query;
		try {
			query = URLEncoder.encode(search, "utf-8");
			//XML_Address_Define.Search.getSearchprogram(search, "0", "0", Location, Product);

			//XML_URI	= "http://58.143.243.91/Mobile/searchprogram.asp?LocationInfo="+Location+"&ProductInfo="+Product+ "&Search_String="+query;
			XML_URI	= "http://58.143.243.91/SMApplicationServer/searchprogram.asp?AreaCode="+Location+"&ProductCode="+Product+ "&Search_String="+query;
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
	}

	public TV_SEARCH_PASER(String url) {

		mTV_Search_DATA_LIST	=	new TV_ALL_DATA_LIST();
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
					//System.out.println("Start tag "+xpp.getName());

					if (xpp.getName().equals(XML_RESULT)) {
						eventType = xpp.next();
						mTV_Search_DATA_LIST.setResultCode(xpp.getText());
						/*} else if (xpp.getName().equals(XML_PAGE_NUM)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.setPageNum(xpp.getText());
					} else if (xpp.getName().equals(XML_LOCATION)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.setLocationInfo(xpp.getText());
					} else if (xpp.getName().equals(XML_PRODUCT)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.setProductInfo(xpp.getText());*/
					} else if (xpp.getName().equals(XML_CHANNEL_ITEM)) {
						//eventType = xpp.next();
						mTV_Search_DATA_LIST.add(new TV_ALL_DATA());			// 리스트 추가
						mCurrentCount = mTV_Search_DATA_LIST.size()-1;			// 현재 추가된 리스트의 위치 반환
					} else if (xpp.getName().equals(XML_ID)) {
						eventType = xpp.next();
						mTV_Search_DATA_LIST.get(mCurrentCount).setID(xpp.getText());
					} else if (xpp.getName().equals(XML_PROGRAM_Seq)) {
						eventType = xpp.next();
						mTV_Search_DATA_LIST.get(mCurrentCount).setPROGRAM_nextID(xpp.getText());
					} else if (xpp.getName().equals(XML_NUMBER)) {
						eventType = xpp.next();
						mTV_Search_DATA_LIST.get(mCurrentCount).setNUMBER(xpp.getText());
					} else if (xpp.getName().equals(XML_NAME)) {
						eventType = xpp.next();
						mTV_Search_DATA_LIST.get(mCurrentCount).setNAME(xpp.getText());
					} else if (xpp.getName().equals(XML_INFO)) {
						eventType = xpp.next();
						mTV_Search_DATA_LIST.get(mCurrentCount).setINFO(xpp.getText());
					} else if (xpp.getName().equals(XML_PROGRAM_ID)) {
						eventType = xpp.next();
						mTV_Search_DATA_LIST.get(mCurrentCount).setPROGRAM_onAIR_ID(xpp.getText());
					} else if (xpp.getName().equals(XML_onAIR_TIME)) {
						eventType = xpp.next();
						mTV_Search_DATA_LIST.get(mCurrentCount).setCHANNEL_onAir_TIME(xpp.getText());
					} else if (xpp.getName().equals(XML_onAIR_TITLE)) {
						eventType = xpp.next();
						mTV_Search_DATA_LIST.get(mCurrentCount).setCHANNEL_onAIR_TITLE(xpp.getText());
					} else if (xpp.getName().equals(XML_onAIR_HD)) {
						eventType = xpp.next();
						mTV_Search_DATA_LIST.get(mCurrentCount).setPROGRAM_onAIR_HD(xpp.getText());
					} else if (xpp.getName().equals(XML_NEXT_TIME)) {
						eventType = xpp.next();
						mTV_Search_DATA_LIST.get(mCurrentCount).setCHANNEL_nextTIME(xpp.getText());
					} else if (xpp.getName().equals(XML_onAIR_Grade)) {
						eventType = xpp.next();
						mTV_Search_DATA_LIST.get(mCurrentCount).setCHANNEL_nextTITLE(xpp.getText());
					} else if (xpp.getName().equals(XML_LOGE_IMG)) {
						eventType = xpp.next();
						mTV_Search_DATA_LIST.get(mCurrentCount).setCHANNEL_LOG_IMG(xpp.getText());
						/*} else if (xpp.getName().equals(XML_CHANNEL_VIEW)) {
						eventType = xpp.next();
						mTV_ALL_DATA_LIST.get(mCurrentCount).setCHANNEL_VIEW(xpp.getText());*/
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


	public TV_ALL_DATA_LIST getTV_Search_DATA_LIST() {
		return mTV_Search_DATA_LIST;
	}

}
