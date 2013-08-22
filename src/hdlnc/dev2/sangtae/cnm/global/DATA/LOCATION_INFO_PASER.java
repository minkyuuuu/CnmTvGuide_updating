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

public class LOCATION_INFO_PASER {

	// URL
	private final static String XML_URI	=	XML_Address_Define.Channel.getGetchannelarea();//"http://58.143.243.91/Mobile/LocationInfo.asp";		// Local testServer
	//final static String XML_URI	= "http://210.109.98.196:7878/Mobile/Channel.aspx?LocationInfo=0&ProductInfo=1&channel_info=all";	// CMS Address
	// XML Depth
	// ROOT 1
	private final static String XML_ROOT			=	"Location_Info_Root";
	// deth 2
	private final static String XML_RESULT			=	"resultCode";
	private final static String XML_CHANNEL_ITEM	=	"area_item";
	// deth 3
	private final static String XML_ID				=	"areaCode";	
	private final static String XML_NAME			=	"areaName";
	private final static String XML_NAME2			=	"areaNameDetail";

	private static LOCATION_INFO_LIST mLocation_info;

	public LOCATION_INFO_PASER() {
		// TODO Auto-generated constructor stub
		mLocation_info	=	new LOCATION_INFO_LIST();
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
				} else if(eventType == XmlPullParser.END_DOCUMENT) {
					//System.out.println("End document");
				} else if(eventType == XmlPullParser.START_TAG) {
					//System.out.println("Start tag "+xpp.getName());

					if (xpp.getName().equals(XML_RESULT)) {
						eventType = xpp.next();
						mLocation_info.setRESULT(xpp.getText());
					} else if (xpp.getName().equals(XML_CHANNEL_ITEM)) {
						//eventType = xpp.next();
						mLocation_info.add(new LOCATION_INFO());			// ����Ʈ �߰�
						mCurrentCount = mLocation_info.size()-1;			// ���� �߰��� ����Ʈ�� ��ġ ��ȯ
					} else if (xpp.getName().equals(XML_ID)) {
						eventType = xpp.next();
						mLocation_info.get(mCurrentCount).setID(xpp.getText());
					} else if (xpp.getName().equals(XML_NAME)) {
						eventType = xpp.next();
						mLocation_info.get(mCurrentCount).setName(xpp.getText());
					} else if (xpp.getName().equals(XML_NAME2)) {
						eventType = xpp.next();
						mLocation_info.get(mCurrentCount).setName2(xpp.getText());
						eventType = xpp.next();
					}
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {	// �Ľ� �� �߻��ϴ� ���� ��Ȳ�� �޴�.
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {				// ��Ű� ���õ� ���� ��Ȳ�� �޴´�.
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return true;
	}

	public static LOCATION_INFO_LIST getLocation_info() {
		return mLocation_info;
	}

}
