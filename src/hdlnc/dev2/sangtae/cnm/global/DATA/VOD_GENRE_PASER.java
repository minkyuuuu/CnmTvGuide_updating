package hdlnc.dev2.sangtae.cnm.global.DATA;

/*
 * XML ���Ǽ� ǥ����� �������� Ŭ���� �̸��� �����Ͽ����� ���ظ� �������� �ΰ������� �ʿ�
 * GENRE_ALL �� VOD �޴��� �帣�� �� ����Ʈ�� �ǹ�  
 */

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


public class VOD_GENRE_PASER {
	// URL
//	private final static String XML_URI	=	"http://121.88.252.2/Mobile/VOD_Genre.aspx";		// Local testServer
	private static String XML_URI="";

	// XML Depth
	// ROOT 1
	private final static String XML_ROOT			=	"Genre_Root";
	// depth 2
	private final static String XML_RESULT_CODE		=	"resultCode";
	private final static String XML_GENRE_ITEM		=	"genre_item";
	// depth 3
	private final static String XML_ID				=	"GenreId";	
	private final static String XML_TITLE			=	"Genre_Title";	
	private final static String XML_MORE			=	"Genre_More";	

	private static VOD_GENRE_LIST mVod_Genre_List;

	public VOD_GENRE_PASER(String url) {
		// TODO Auto-generated constructor stub
		mVod_Genre_List	=	new VOD_GENRE_LIST();
		XML_URI = url;
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

					if (xpp.getName().equals(XML_RESULT_CODE)) {
						eventType = xpp.next();
						mVod_Genre_List.setRESULT(xpp.getText());
					} else if (xpp.getName().equals(XML_GENRE_ITEM)) {
//						eventType = xpp.next();
						mVod_Genre_List.add(new VOD_GENRE());			// ����Ʈ �߰�
						mCurrentCount = mVod_Genre_List.size()-1;			// ���� �߰��� ����Ʈ�� ��ġ ��ȯ
					} else if (xpp.getName().equals(XML_ID)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mVod_Genre_List.get(mCurrentCount).setID(xpp.getText());
					} else if (xpp.getName().equals(XML_TITLE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mVod_Genre_List.get(mCurrentCount).setTitle(xpp.getText());
						eventType = xpp.next();
					} else if (xpp.getName().equals(XML_MORE)) {
						eventType = xpp.next();
						if(xpp.getText() != null)
						mVod_Genre_List.get(mCurrentCount).setMore(xpp.getText());
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

	public static VOD_GENRE_LIST getArrayList() {
		return mVod_Genre_List;
	}
}
