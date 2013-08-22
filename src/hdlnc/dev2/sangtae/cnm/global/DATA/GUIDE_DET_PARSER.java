package hdlnc.dev2.sangtae.cnm.global.DATA;

import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

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

public class GUIDE_DET_PARSER {
	// URL
	//final static String XML_URI	=	"http://121.126.13.202:8080/MAMP/allchannel.xml";		// Local testServer
	private static String XML_URI;
	// XML Depth
	// ROOT 1
	private final static String XML_GUIDE_ITEM_ROOT			=	"UseGuideItem_Root";
	// deth 2
	private final static String XML_GUIDE_RESULT			=	"resultCode";
	private final static String XML_GUIDE_ITEM				=	"Guide_Item";
	// deth 3
	private final static String XML_GUIDE_ID				=	"guideId";
	private final static String XML_GUIDE_TITLE				=	"guide_title";
	private final static String XML_GUIDE_CONTENT			=	"guide_Content";  

	private static GUIDE_DET_DATA mGUIDE_DET_DATA;
	
	public GUIDE_DET_PARSER() {
		// TODO Auto-generated constructor stub
		//mGUIDE_DATA_LIST	=	new GUIDE_DATA_LIST();
		//XML_GUIDE_URI	= "http://58.143.243.91/mobile/Guide.aspx";
	}
	
	public GUIDE_DET_PARSER(String aID) {
		// TODO Auto-generated constructor stub
		XML_URI	=  XML_Address_Define.Service.getGetserviceguideinfo(aID);//"http://58.143.243.91/mobile/Guide_Detail.asp?guideID="+aID;
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
					mGUIDE_DET_DATA = new GUIDE_DET_DATA();
				} else if(eventType == XmlPullParser.END_DOCUMENT) {
					//System.out.println("End document");
				} else if(eventType == XmlPullParser.START_TAG) {
					//System.out.println("Start tag "+xpp.getName());

					if (xpp.getName().equals(XML_GUIDE_RESULT)) {
						eventType = xpp.next();
						mGUIDE_DET_DATA.setRESULT(xpp.getText());
					} else if (xpp.getName().equals(XML_GUIDE_ITEM)) {
						eventType = xpp.next();
						//mGUIDE_DATA_LIST.add(new GUIDE_DATA());
						//mCurrentCount = mGUIDE_DATA_LIST.size()-1;
					} else if (xpp.getName().equals(XML_GUIDE_ID)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mGUIDE_DET_DATA.setID(xpp.getText());
					} else if (xpp.getName().equals(XML_GUIDE_TITLE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mGUIDE_DET_DATA.setTitle(xpp.getText());
					} else if (xpp.getName().equals(XML_GUIDE_CONTENT)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
							mGUIDE_DET_DATA.setCONTENT(xpp.getText());
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
	
	public GUIDE_DET_DATA getGUIDE_DET_DATA(String aID) {
		return mGUIDE_DET_DATA;
	}

}
