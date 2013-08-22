package hdlnc.dev2.sangtae.cnm.global.DATA;

import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;

import java.io.IOException;
import java.net.URL;
import java.security.PublicKey;

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

public class PRODUCT_INFO_PASER {
	// URL
	private static String XML_URI;		// Local testServer
	//final static String XML_URI	= "http://210.109.98.196:7878/Mobile/Channel.aspx?LocationInfo=0&ProductInfo=1&channel_info=all";	// CMS Address
	// XML Depth
	// ROOT 1
	private final static String XML_ROOT			=	"Product_Info_Root";
	// deth 2
	private final static String XML_RESULT			=	"resultCode";
	private final static String XML_CHANNEL_ITEM	=	"product_item";
	// deth 3
	private final static String XML_ID				=	"productCode";	
	private final static String XML_NAME			=	"productName";	
	private final static String XML_INFO			=	"productInfo";
	private static PRODUCT_INFO_LIST mProduct_INFO_LIST;

	public PRODUCT_INFO_PASER(String aURL) {
		// TODO Auto-generated constructor stub
		mProduct_INFO_LIST	=	new PRODUCT_INFO_LIST();
		XML_URI = aURL;
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
						mProduct_INFO_LIST.setRESULT(xpp.getText());
					} else if (xpp.getName().equals(XML_CHANNEL_ITEM)) {
						eventType = xpp.next();
						mProduct_INFO_LIST.add(new PRODUCT_INFO());			// 리스트 추가
						mCurrentCount = mProduct_INFO_LIST.size()-1;			// 현재 추가된 리스트의 위치 반환
					} else if (xpp.getName().equals(XML_ID)) {
						eventType = xpp.next();
						mProduct_INFO_LIST.get(mCurrentCount).setID(xpp.getText());
					} else if (xpp.getName().equals(XML_NAME)) {
						eventType = xpp.next();
						mProduct_INFO_LIST.get(mCurrentCount).setName(xpp.getText());
					} else if (xpp.getName().equals(XML_INFO)) {
						eventType = xpp.next();
						if (xpp.getText().equals("YES")) {
							mProduct_INFO_LIST.setDefaultIndx(mCurrentCount);//get(mCurrentCount).setName(xpp.getText());
						}
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

	public static PRODUCT_INFO_LIST getProduct_INFO_LIST() {
		return mProduct_INFO_LIST;
	}
}
