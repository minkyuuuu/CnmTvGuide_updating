package hdlnc.dev2.sangtae.cnm.global.DATA;

import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;

import java.io.IOException;

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

public class GuideImage_DATA_LIST_PASER {
	// URL
	private static String XML_URI;
	// XML Depth
	// ROOT 1
	private final static String XML_ROOT			=	"response";
	private final static String XML_RESULT_CODE		=	"resultCode";
	private final static String XML_ERROR_STRING	=	"errorString";
	// deth 2
	private final static String XML_ITEM				=	"GuideImage_Item";
	// deth 3
	private final static String XML_IMAGE_ID			=	"guideImageID";	
	private final static String XML_GROUP_TYPE			=	"groupType";	
	private final static String XML_SUBJECT				=	"subject";	
	private final static String XML_IMAGE_FILE			=	"imageFile";
	private final static String XML_GUIDE_URL			=	"guideURL";	
	private final static String XML_CONTENCT			=	"content";	
	private final static String XML_REGISTER_DATE		=	"registerDate";	

	private static GuideImage_DATA_LIST mLIST;

	public GuideImage_DATA_LIST_PASER(String aURL) {
		// TODO Auto-generated constructor stub
		mLIST	=	new GuideImage_DATA_LIST();
		XML_URI	= aURL;
	}

	public static Boolean start() {
		Integer	mCurrentCount=0;
		mLIST.clear();
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
						mLIST.setResultCode(xpp.getText());
					} else if (xpp.getName().equals(XML_ERROR_STRING)) {
						eventType = xpp.next();
						mLIST.setErrorString(xpp.getText());
					} else if (xpp.getName().equals(XML_ITEM)) {
						//eventType = xpp.next();
						mLIST.add(new GuideImage_DATA());			// 리스트 추가
						mCurrentCount = mLIST.size()-1;			// 현재 추가된 리스트의 위치 반환
					} else if (xpp.getName().equals(XML_IMAGE_ID)) {
						eventType = xpp.next();
						mLIST.get(mCurrentCount).setGuideImageID(xpp.getText());
					} else if (xpp.getName().equals(XML_GROUP_TYPE)) {
						eventType = xpp.next();
						mLIST.get(mCurrentCount).setGroupType(xpp.getText());
					} else if (xpp.getName().equals(XML_SUBJECT)) {
						eventType = xpp.next();
						mLIST.get(mCurrentCount).setSubject(xpp.getText());
					} else if (xpp.getName().equals(XML_IMAGE_FILE)) {
						eventType = xpp.next();
						mLIST.get(mCurrentCount).setImageFileURL(xpp.getText());
					} else if (xpp.getName().equals(XML_GUIDE_URL)) {
						eventType = xpp.next();
						mLIST.get(mCurrentCount).setGuideURL(xpp.getText());
					} else if (xpp.getName().equals(XML_CONTENCT)) {
						eventType = xpp.next();
						mLIST.get(mCurrentCount).setContent(xpp.getText());
					} else if (xpp.getName().equals(XML_REGISTER_DATE)) {
						eventType = xpp.next();
						mLIST.get(mCurrentCount).setRegisterDate(xpp.getText());
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


	public static GuideImage_DATA_LIST getGuideImage_DATA_LIST() {
		return mLIST;
	}

}
