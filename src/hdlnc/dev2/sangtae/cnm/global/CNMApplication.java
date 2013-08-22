
package hdlnc.dev2.sangtae.cnm.global;

import static hdlnc.dev2.sangtae.cnm.global.TVCH_Alarm_CheckSQLiteOpenHelper.PROGRAM_ALARAM_HD;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_Alarm_CheckSQLiteOpenHelper.PROGRAM_ALARAM_TIME_TEXT;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_Alarm_CheckSQLiteOpenHelper.PROGRAM_ALARM_CHECK;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_Alarm_CheckSQLiteOpenHelper.PROGRAM_CH_GRADE;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_Alarm_CheckSQLiteOpenHelper.PROGRAM_CH_NAME;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_Alarm_CheckSQLiteOpenHelper.PROGRAM_CH_NUMBER;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_Alarm_CheckSQLiteOpenHelper.PROGRAM_CH_URL;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_Alarm_CheckSQLiteOpenHelper.PROGRAM_TITLE;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_Alarm_CheckSQLiteOpenHelper.SCHEULE_SEQ;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_Alarm_CheckSQLiteOpenHelper.TV_ALARM_TABLE;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_CheckSQLiteOpenHelper.CHANNEL_ID;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_CheckSQLiteOpenHelper.MY_CHANNEL;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_CheckSQLiteOpenHelper.MY_CHANNEL_TIME;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_CheckSQLiteOpenHelper.TVCH_CHECK_TABLE;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_PR_CheckSQLiteOpenHelper.PROGRAM_ALARM_TIME;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_PR_CheckSQLiteOpenHelper.PROGRAM_ID;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_PR_CheckSQLiteOpenHelper.PROGRAM_MY_CHANNEL;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_PR_CheckSQLiteOpenHelper.PROGRAM_MY_CHANNEL_TIME;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_PR_CheckSQLiteOpenHelper.TVPR_CHECK_TABLE;
import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.DATA.GUIDE_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.JOYN_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.JOYN_DET_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.RECORD_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.SCUEDULE_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.SERVICE_NOTICE_INFO;
import hdlnc.dev2.sangtae.cnm.global.DATA.SERVICE_NOTICE_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.SERVICE_NOTICE_PARSER;
import hdlnc.dev2.sangtae.cnm.global.DATA.TV_ALL_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_GENRE_DETAIL_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_GENRE_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_NEWMOVIE_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_PREVIEW_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_TVREPLAY_LIST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

interface InitViewSetting {
	//메뉴, TV채널, VOD
	int MENU_VIEW=1, TV_CHANNEL_VIEW=2, VOD_VIEW=3;
}

public class CNMApplication extends Application {
	// 기본 실행 상황 체크

	//final public String CNM_DOMAIN = "http://58.143.243.91/mobile/";
	//final public String DEV_DOMAIN = "http://121.88.252.2/Mobile/";
	//final public String DEV_DOMAIN = "http://58.143.243.91/Mobile/";

	//final public String URL_VOD_TAG_SEARCH = CNM_DOMAIN + "vod_tag.asp?VOD_TAG=";
	//final public String URL_VOD_SEARCH = CNM_DOMAIN + "searchVod.asp?search_string=";
	//final public String URL_VOD_TVREPLAY = CNM_DOMAIN + "vod_tv.asp";
	//final public String URL_VOD_NEWMOVIE = CNM_DOMAIN + "vod_movie.asp";
	//final public String URL_VOD_PREVIEW = CNM_DOMAIN + "vod_trailer.asp";
	//final public String URL_VOD_GENRE = CNM_DOMAIN + "VOD_Genre.asp?Genre_ID=";
	//final public String URL_VOD_GENRE_ID = CNM_DOMAIN + "VOD_Genre_detail.asp?Genre_ID=";
	//final public String URL_VOD_AUTH_VOD = CNM_DOMAIN + "auth_vod.asp?";

	//final public String URL_JOYN_LIST = CNM_DOMAIN + "joyN.asp?";
	//final public String URL_JOYN_DET = CNM_DOMAIN + "joyn_detail.asp?joynID=";

	//final public String URL_ZZIM_INSERT = DEV_DOMAIN + "MyVOD_Insert.asp?MemberID=";
	//final public String URL_ZZIM_DELET = DEV_DOMAIN + "MyVOD_Delete.asp?MemberID=";
	//final public String URL_ZZIM_LIST = DEV_DOMAIN + "myvod_list.asp?MemberID=";
	//final public String URL_ZZIM_EXISTS_CHECK = DEV_DOMAIN + "myvod.asp?MemberID=";
	//final public String URL_ZZIM_REFURBISH = DEV_DOMAIN + "aMyVOD_Delete.asp?MemberID=";

	//final public String URL_SETTING_ADULT_AUTH = CNM_DOMAIN + "auth_adult.asp?userinfo=";
	//final public String URL_SETTING_SETTOP_AUTH = DEV_DOMAIN + "requestAuthCode.asp?";
	//final public String URL_SETTING_SETTOP_REG = DEV_DOMAIN + "auth_vod.asp?";
	//final public String URL_SETTING_SETTOP_REFUBLESH = DEV_DOMAIN + "MyVOD_Delete.asp?MemberID=";

	//final public String URL_SETTOP_REG_CHECK = DEV_DOMAIN + "CheckRegist.asp?memberId=";

	
	private final static String DEBUG_TAG = "CNMApplication";
	
	private static int TVSC_Index;

	public int getTVSC_Index() {
		Log.d("Sangtae", "getTVSC_Index: "+ TVSC_Index);
		return TVSC_Index;
	}

	public void setTVSC_Index(int tVSC_Index) {
		Log.d("Sangtae", "setTVSC_Index: "+ tVSC_Index);
		TVSC_Index = tVSC_Index;
	}
	final public int RowTitleLength  = 16;

	// TV CH 상품 / 지역 환경 변수키
	final private String CNM_SP = "CNM_SharedPreferences";
	private SharedPreferences.Editor cnmEditor;
	private Boolean FirstLoding;
	final private String TVCH_SettigChecked_KEY = "TVCH_SettigChecked_KEY";
	private Boolean TVCH_SettigChecked;
	final private String	FirstLodingKey = "FirstLodingKey";
	private String	ProductID;
	final private String	ProductIDKey = "ProductIDKey";
	private String	ProductName;
	final private String	ProductNameKey = "ProductNameKey";
	private String	LocationID;
	final private String	LocationIDKey = "LocationIDKey";
	private String	LocationName;
	private String	LocationName2;
	final private String	LocationNameKey = "LocationNameKey";
	final private String	SystemUDIDKey = "SystemUDIDKey";
	private String	SystemUDID;
	//초기 화면 설정
	//private InitViewSetting InitViewSettingValue;
	private int InitViewSettingValue;
	final private String    InitViewSettingKey = "InitViewSettingKey";
	//셋탑등록
	private Boolean SettopRegAuth=false;
	private String SettopBoxType=null;
	final private String 	SettopRegAuthKey = "SettopAuthKey";
	final private String 	SettopKindKey = "SettopKindKey";
	//성인인증
	public Boolean AdultAuth=false;
	public Boolean AdultAutoAuth;
	final private String 	AdultAutoAuthKey = "AdultAuthKey";

	//소셜네트워크 키 정의
	//	public final String TwitterConsumerKey = "wPS0JdqFrXxQAJTrnhzGA";
	//	public final String TwitterSecretKey = "Il42o28hulfFakfXeAJ9dRDwZ1qpRNxjLO02uSuY";
	public final String TwitterConsumerKey = "qrgFLK7jONbi0HqRvY60oQ";
	public final String TwitterSecretKey = "utrMLwD8ojcTuhWRWEVk5s8bqJ98wGHosXqr5DUZA";
	public final String FACEBOOK_APPLICATION_ID = "133481023382074";
	public final String FACEBOOK_APPLICATION_KEY = "b61b031fa81faa77aaf1c64a6d79ecf6";
	public final String FACEBOOK_APPLICATION_SECRET = "443818698f82dd9a0ae9a801820d5a1e";


	//외장메모리 경로
	public String ExternalStoregePath;
	public String RootPath = Environment.getRootDirectory().getAbsolutePath();
	public String DataPath = Environment.getDataDirectory().getAbsolutePath();
	public String CachePath = Environment.getDownloadCacheDirectory().getAbsolutePath();

	//네트워크 접속상태 확인 상수
	public final int WIFI_CONNECTED = 0;
	public final int MOBILE_CONNECTED = 1;
	public final int DECONNECTE = 2;

	//private ArrayList<TVCH_CheckList> currentTVCH;			// TV 채널 설정 정보 저장 형식
	private HashMap<String, TVCH_CheckList> TVCh_CheckMap;
	private HashMap<String, TVCH_PR_CheckList> TVPr_CheckMap;
	private SQLiteDatabase TVCH_database;						// TV 채널 설정 정보 저장 데이터베이스
	private SQLiteDatabase TVPR_database;						// TV 채널 설정 정보 저장 데이터베이스
	private TVCH_CheckSQLiteOpenHelper helper;
	private TVCH_PR_CheckSQLiteOpenHelper pr_helper;

	public SQLiteDatabase TV_Alarm_database;						// TV 알람 설정 정보 저장 데이터 베이스
	private TVCH_Alarm_CheckSQLiteOpenHelper alarm_helper;			// 알람 db helper
	private HashMap<String, TVCH_Alarm_CheckList> TVAl_CheckMap;
	private ArrayList<TVCH_Alarm_CheckList> TVAl_CheckArr = new ArrayList();

	// 메인 탭의 라디오 그룹
	private  RadioGroup mChannelTabGroup;
	private  RadioGroup mRemoteTabGroup;
	private  RadioGroup mPVRTabGroup;
	private  int mChannelTabID  = R.id.TVCH_AllCH_TABBtn;
	private  RadioGroup mMainTabGroup;
	// 버튼 효과음
	private CNM_SoundPool mSoundPool;
	private RelativeLayout mTransLayout;
	private ImageButton	mPower;
	private ImageButton	mVolUP;
	private ImageButton	mVolDOWN;
	private Handler GlobalRemotHandl;

	public Handler getGlobalRemotHandl() {
		return GlobalRemotHandl;
	}

	public void setGlobalRemotHandl(Handler globalRemotHandl) {
		GlobalRemotHandl = globalRemotHandl;
	}

	public RelativeLayout getTransLayout() {
		return mTransLayout;
	}

	public void setTransLayout(RelativeLayout mTransLayout) {
		this.mTransLayout = mTransLayout;
	}

	public ImageButton getPower() {
		return mPower;
	}

	public void setPower(ImageButton mPower) {
		this.mPower = mPower;
	}

	public ImageButton getVolUP() {
		return mVolUP;
	}

	public void setVolUP(ImageButton mVolUP) {
		this.mVolUP = mVolUP;
	}

	public ImageButton getVolDOWN() {
		return mVolDOWN;
	}

	public void setVolDOWN(ImageButton mVolDOWN) {
		this.mVolDOWN = mVolDOWN;
	}
	// 공지 목록
	private SERVICE_NOTICE_LIST mServiceNoticeList = null;
	
	public SERVICE_NOTICE_LIST getmServiceNoticeList() {
		return mServiceNoticeList;
	}

	public void setmServiceNoticeList(SERVICE_NOTICE_LIST mServiceNoticeList) {
		this.mServiceNoticeList = mServiceNoticeList;
	}

	public SERVICE_NOTICE_INFO getServiceNoticeInfo(int index) {
		if (mServiceNoticeList == null)
			return null;
		
		for (int i = 0; i < mServiceNoticeList.size(); i++) {
			SERVICE_NOTICE_INFO notice = mServiceNoticeList.get(i);
			Log.d(DEBUG_TAG,"notice.getLocation(): "+notice.getLocation()+"     String.valueOf(index): "+String.valueOf(index));
			
			if (notice.getLocation().contains(String.valueOf(index)))
				return notice;
		}
		return null;
	}
	
	public RadioGroup getMainTabGroup() {
		return mMainTabGroup;
	}

	public void setMainTabGroup(RadioGroup mMainTabGroup) {
		this.mMainTabGroup = mMainTabGroup;
	}

	public RadioGroup getChannelTabGroup() {
		return mChannelTabGroup;
	}
	public int getChannelTabID() {
		return mChannelTabID;
	}
	
	

	public RadioGroup getmRemoteTabGroup() {
		return mRemoteTabGroup;
	}

	public void setmRemoteTabGroup(RadioGroup mRemoteTabGroup) {
		this.mRemoteTabGroup = mRemoteTabGroup;
	}

	public RadioGroup getmPVRTabGroup() {
		return mPVRTabGroup;
	}

	public void setmPVRTabGroup(RadioGroup mPVRTabGroup) {
		this.mPVRTabGroup = mPVRTabGroup;
	}

	public void setChannelTabID(int mChannelTabID) {
		this.mChannelTabID = mChannelTabID;
	}
	public void setChannelTabGroup(RadioGroup mChannelTabGroup) {
		this.mChannelTabGroup = mChannelTabGroup;
	}

	public ArrayList<TVCH_Alarm_CheckList> getTVAl_CheckArr() {
		return TVAl_CheckArr;
	}

	public void setTVAl_CheckArr(ArrayList<TVCH_Alarm_CheckList> tVAl_CheckArr) {
		TVAl_CheckArr = tVAl_CheckArr;
	}

	public HashMap<String, TVCH_Alarm_CheckList> getTVAl_CheckMap() {
		return TVAl_CheckMap;
	}

	public void setTVAl_CheckMap(HashMap<String, TVCH_Alarm_CheckList> tVAl_CheckMap) {
		TVAl_CheckMap = tVAl_CheckMap;
	}

	//파싱 데이터 저장 및 이벤트 오브젝트 
	private	TV_ALL_DATA_LIST mTv_ALL_DATA_LIST;
	private	TV_ALL_DATA_LIST mTv_HD_DATA_LIST;
	private	TV_ALL_DATA_LIST mTv_PAY_DATA_LIST;

	private SCUEDULE_DATA_LIST mTV_SCUEDULE_LIST;
	private RECORD_DATA_LIST mRecord_DATA_LIST;
	private	VOD_GENRE_LIST mVod_GENRE_DATA_LIST; 
	private	VOD_GENRE_DETAIL_LIST mVod_GENRE_DETAIL_DATA_LIST;
	private	VOD_PREVIEW_LIST mVod_PREVIEW_DATA_LIST;
	private	VOD_NEWMOVIE_LIST mVod_NEWMOVIE_DATA_LIST;
	private	VOD_TVREPLAY_LIST mVod_TVREPLAY_DATA_LIST;
	private	GUIDE_DATA_LIST mSetting_GUIDE_DATA_LIST;
	private	JOYN_DATA_LIST mJoyn_DATA_LIST;
	private	JOYN_DET_DATA mJoyn_DATA_DET_LIST;

	//네비게이션 버튼
	private static	NaviBtn_Singleton naviBtn_Singleton;
	private RadioButton MainrbFirst;
	private RadioButton MainrbSecond;
	private RadioButton MainrbThird;
	private RadioButton MainrbFourth;
	private RadioButton MainrbFifth;

	private int goToSetting = Intent_KEY_Define.Set_Mode.MODE.NORMAL;



	public int getGoToSetting() {
		return goToSetting;
	}

	public void setGoToSetting(int goToSetting) {
		this.goToSetting = goToSetting;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		Log.i("hwang", "CNMApplication - onCreate()");

		mSoundPool = new CNM_SoundPool(getApplicationContext());

		loadPreferences();								// 공용 설정 정보 가져오
		mTv_ALL_DATA_LIST	= new TV_ALL_DATA_LIST();
		mTv_HD_DATA_LIST	= new TV_ALL_DATA_LIST();
		mTv_PAY_DATA_LIST	= new TV_ALL_DATA_LIST();
		//		mVod_GENRE_DATA_LIST	= new VOD_GENRE_LIST();
		//		mVod_GENRE_DETAIL_DATA_LIST	= new VOD_GENRE_DETAIL_LIST();
		//		mVod_PREVIEW_DATA_LIST	= new VOD_PREVIEW_LIST();
		//		mVod_NEWMOVIE_DATA_LIST	= new VOD_NEWMOVIE_LIST();
		//		mVod_TVREPLAY_DATA_LIST = new VOD_TVREPLAY_LIST();

		helper = new TVCH_CheckSQLiteOpenHelper(this);
		alarm_helper = new TVCH_Alarm_CheckSQLiteOpenHelper(this);
		pr_helper = new TVCH_PR_CheckSQLiteOpenHelper(this);

		TVCH_database = helper.getWritableDatabase();
		TV_Alarm_database = alarm_helper.getWritableDatabase();
		TVPR_database = pr_helper.getWritableDatabase();

		//		helper.onUpgrade(TVCH_database, 1, 1);
		LoadTVCHCheckList();
		LoadTVPRCheckList();
		LoadTVAlarmList();

		ComponentName cn = new ComponentName(this.getPackageName(), CNM_Alarm_Service.class.getName()); 
		this.startService(new Intent().setComponent(cn));

		Log.d(DEBUG_TAG, "LocationID: "+LocationID+"    ProductID"+ProductID);
		if (LocationID != "" && ProductID != "")
			new ServiceNoticeAsyncTask().execute(XML_Address_Define.Service.getGetserviceNoticeinfo(LocationID, ProductID));//tempApp.getMobileIMIE(getApplicationContext()));
	}
	
	class ServiceNoticeAsyncTask extends AsyncTask<String, Void, SERVICE_NOTICE_LIST>
	{

		private ProgressDialog pro;
		@Override
		protected SERVICE_NOTICE_LIST doInBackground(String... params) {
			

			SERVICE_NOTICE_PARSER paser = new SERVICE_NOTICE_PARSER(params[0]);
			Log.d(DEBUG_TAG,"## URL Request 공지사항: "+params[0]);
			
			for (int i = 0; i < 2; i++) {
				if(paser.start()){
					return paser.getArrayList();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(SERVICE_NOTICE_LIST result) {
			
			setmServiceNoticeList(result);
			
			super.onPostExecute(result);
			
			
		}
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		helper.close();
		alarm_helper.close();
		pr_helper.close();
	}

	public void ButtonBeepPlay() {
		// TODO Auto-generated method stub
		mSoundPool.play();
	}

	public void Parser_data_Reset() {
		mTv_ALL_DATA_LIST.clear();

	}



	public void loadPreferences() {
		// TODO Auto-generated method stub

		SharedPreferences cnmPreferences = getSharedPreferences(CNM_SP, Activity.MODE_WORLD_WRITEABLE);
		cnmEditor	= cnmPreferences.edit();

		FirstLoding = cnmPreferences.getBoolean(FirstLodingKey, false);
		TVCH_SettigChecked = cnmPreferences.getBoolean(TVCH_SettigChecked_KEY, false);
		ProductID	= cnmPreferences.getString(ProductIDKey, "");
		ProductName	= cnmPreferences.getString(ProductNameKey, "");
		LocationID	= cnmPreferences.getString(LocationIDKey, "");
		LocationName= cnmPreferences.getString(LocationNameKey, "");
		SystemUDID	= cnmPreferences.getString(SystemUDIDKey, "");
		InitViewSettingValue = cnmPreferences.getInt(InitViewSettingKey, 1);
		AdultAutoAuth = cnmPreferences.getBoolean(AdultAutoAuthKey, false);
		SettopRegAuth = cnmPreferences.getBoolean(SettopRegAuthKey, false); 
		SettopBoxType = cnmPreferences.getString(SettopKindKey, "미등록");

	}


	public void savePreferences(Boolean Loading) {
		// TODO Auto-generated method stub
		FirstLoding=Loading;
		cnmEditor.putBoolean(FirstLodingKey, Loading);
		cnmEditor.putBoolean(TVCH_SettigChecked_KEY, TVCH_SettigChecked);
		cnmEditor.putString(ProductIDKey, ProductID);
		cnmEditor.putString(ProductNameKey, ProductName);
		cnmEditor.putString(LocationIDKey, LocationID);
		cnmEditor.putString(LocationNameKey, LocationName);
		cnmEditor.putString(SystemUDIDKey, SystemUDID);
		cnmEditor.putInt(InitViewSettingKey, InitViewSettingValue);
		cnmEditor.putBoolean(AdultAutoAuthKey, AdultAutoAuth);
		cnmEditor.putBoolean(SettopRegAuthKey, SettopRegAuth);
		cnmEditor.putString(SettopKindKey, SettopBoxType);
		cnmEditor.commit();
	}

	private void LoadTVCHCheckList() {
		//currentTVCH = new ArrayList<TVCH_CheckList>();
		TVCh_CheckMap = new HashMap<String, TVCH_CheckList>();
		Cursor dataCursor = TVCH_database.query(TVCH_CHECK_TABLE, 
				new String[] {CHANNEL_ID, MY_CHANNEL, MY_CHANNEL_TIME},//, PROGRAM_ALARM, PROGRAM_ALARM_TIME}, 		// 5 table
				null, null, null, null, null);
		//Cursor dataCursor = TVCH_database.rawQuery("select " +" _id, "+PROGRAM_ID+", "+ PROGRAM_RESERVE+", "+ PROGRAM_MY_CHANNEL+", "+ PROGRAM_MY_PROGRAM+", "+ PROGRAM_HIDE +" from "+TVCH_CHECK_TABLE, null);

		dataCursor.moveToFirst();
		TVCH_CheckList data;
		if (! dataCursor.isAfterLast()) {
			do {
				//int temp = dataCursor.getInt(0);
				String id = dataCursor.getString(0);
				String myChannel = dataCursor.getString(1);
				String myChannelTime = dataCursor.getString(2);
				//String myAlarm = dataCursor.getString(3);
				//String myAlarmTime = dataCursor.getString(4);
				//String myHide = dataCursor.getString(5);
				boolean Channel = Boolean.parseBoolean(myChannel);
				//boolean Alarm = Boolean.parseBoolean(myAlarm);
				//boolean hide = Boolean.parseBoolean(myHide);

				data = new TVCH_CheckList(id);
				//data.setID(id);
				data.setMyChannel(Channel);
				data.set_MyChannel_Time(myChannelTime);
				/*data.setAlarm(Alarm);
				data.setAlarmTime(myAlarmTime);*/
				//data.setHide(hide);

				//currentTVCH.add(data);
				TVCh_CheckMap.put(id, data);
			} while(dataCursor.moveToNext());
		}

		dataCursor.close();
	}

	private void LoadTVAlarmList() {
		//currentTVCH = new ArrayList<TVCH_CheckList>();
		TVAl_CheckMap = new HashMap<String, TVCH_Alarm_CheckList>();
		Cursor dataCursor = TV_Alarm_database.query(TV_ALARM_TABLE, 
				null, 		// 5 table
				null, null, null, null, null);
		//Cursor dataCursor = TVCH_database.rawQuery("select " +" _id, "+PROGRAM_ID+", "+ PROGRAM_RESERVE+", "+ PROGRAM_MY_CHANNEL+", "+ PROGRAM_MY_PROGRAM+", "+ PROGRAM_HIDE +" from "+TVCH_CHECK_TABLE, null);

		dataCursor.moveToFirst();
		TVCH_Alarm_CheckList data;
		if (! dataCursor.isAfterLast()) {
			do {
				String seq = dataCursor.getString(0);
				String proTitle = dataCursor.getString(1);
				String proCHnum = dataCursor.getString(2);
				String proCHname = dataCursor.getString(3);
				String proCHURL = dataCursor.getString(4);
				int proAlarmFlag = dataCursor.getInt(5);
				String proTime = dataCursor.getString(6);
				String proID = dataCursor.getString(7);
				String proTimeText = dataCursor.getString(8);
				int proHd = dataCursor.getInt(9);
				String grade	=dataCursor.getString(10);

				boolean  flag= false;

				if(proAlarmFlag==1)
					flag = true;

				boolean flag2 = false;

				if( proHd==1 )
					flag2=true;


				//boolean hide = Boolean.parseBoolean(myHide);

				data = new TVCH_Alarm_CheckList(seq);
				//data.setID(id);
				data.setDB_PROGRAM_TITLE(proTitle);
				data.setDB_PROGRAM_CH_NUMBER(proCHnum);
				data.setDB_PROGRAM_CH_NANE(proCHname);
				data.setDB_CH_URL(proCHURL);
				data.setDB_ALARM_CHECK(flag);
				data.setDB_ALARM_TIME(proTime);
				data.setDB_PROGRAM_ID(proID);
				data.setDB_ALARM_TIME_TEXT(proTimeText);
				data.setDB_ALARM_HD(flag2);
				data.setDB_PROGRAM_CH_GRADE(grade);
				//data.setHide(hide);

				//currentTVCH.add(data);
				TVAl_CheckMap.put(seq, data);
				TVAl_CheckArr.add(data);
			} while(dataCursor.moveToNext());
		}
		dataCursor.close();
	}

	private void LoadTVPRCheckList() {
		//currentTVCH = new ArrayList<TVCH_CheckList>();
		TVPr_CheckMap = new HashMap<String, TVCH_PR_CheckList>();
		Cursor dataCursor = TVPR_database.query(TVPR_CHECK_TABLE, 
				null,//new String[] {PROGRAM_ID, PROGRAM_MY_CHANNEL, PROGRAM_MY_CHANNEL_TIME, PROGRAM_ALARM, PROGRAM_ALARM_TIME}, 		// 5 table
				null, null, null, null, null);
		//Cursor dataCursor = TVCH_database.rawQuery("select " +" _id, "+PROGRAM_ID+", "+ PROGRAM_RESERVE+", "+ PROGRAM_MY_CHANNEL+", "+ PROGRAM_MY_PROGRAM+", "+ PROGRAM_HIDE +" from "+TVCH_CHECK_TABLE, null);

		dataCursor.moveToFirst();
		TVCH_PR_CheckList data;
		if (! dataCursor.isAfterLast()) {
			do {
				//int temp = dataCursor.getInt(0);
				String id = dataCursor.getString(0);
				String myProgram = dataCursor.getString(1);
				String myProgramTime = dataCursor.getString(2);
				//String myAlarm = dataCursor.getString(3);
				//String myAlarmTime = dataCursor.getString(4);
				//String myHide = dataCursor.getString(5);
				boolean Program = Boolean.parseBoolean(myProgram);
				//boolean Alarm = Boolean.parseBoolean(myAlarm);
				//boolean hide = Boolean.parseBoolean(myHide);

				data = new TVCH_PR_CheckList(id);
				//data.setID(id);
				data.setMyProgram(Program);
				data.set_MyProgram_Time(myProgramTime);
				//data.setAlarm(Alarm);
				//data.setAlarmTime(myAlarmTime);
				//data.setHide(hide);

				//currentTVCH.add(data);
				TVPr_CheckMap.put(id, data);
			} while(dataCursor.moveToNext());
		}
		dataCursor.close();
	}


	public void setTVCh_CheckMap(HashMap<String, TVCH_CheckList> tVCh_CheckMap) {
		TVCh_CheckMap = tVCh_CheckMap;
	}
	public HashMap<String, TVCH_CheckList> getTVCh_CheckMap() {
		return TVCh_CheckMap;
	}
	public void setTVPr_CheckMap(
			HashMap<String, TVCH_PR_CheckList> tVPr_CheckMap) {
		TVPr_CheckMap = tVPr_CheckMap;
	}
	public HashMap<String, TVCH_PR_CheckList> getTVPr_CheckMap() {
		return TVPr_CheckMap;
	}

	public void insertTask(TVCH_CheckList check) {
		assert(null != check);

		ContentValues values = new ContentValues();
		values.put(CHANNEL_ID, check.getID());
		values.put(MY_CHANNEL, Boolean.toString(check.getMyChannel()));
		values.put(MY_CHANNEL_TIME, check.get_MyChannel_Time());
		/*values.put(PROGRAM_ALARM, Boolean.toString(check.getAlarm()));
		values.put(PROGRAM_ALARM_TIME, check.getAlarm_Time());*/

		TVCH_database.insert(TVCH_CHECK_TABLE, null, values);
		//currentTVCH.add(check);
		TVCh_CheckMap.put(check.getID(), check);

	}

	public void insertPRTask(TVCH_PR_CheckList check) {
		assert(null != check);

		ContentValues values = new ContentValues();
		values.put(PROGRAM_ID, check.getID());
		values.put(PROGRAM_MY_CHANNEL, Boolean.toString(check.getMyProgram()));
		values.put(PROGRAM_MY_CHANNEL_TIME, check.get_MyProgram_Time());
		//values.put(PROGRAM_ALARM, Boolean.toString(check.getAlarm()));
		//values.put(PROGRAM_ALARM_TIME, check.getAlarm_Time());

		TVPR_database.insert(TVPR_CHECK_TABLE, null, values);
		//currentTVCH.add(check);
		TVPr_CheckMap.put(check.getID(), check);

	}



	public void insertAlarmTask(TVCH_Alarm_CheckList check) {
		assert(null != check);

		ContentValues values = new ContentValues();
		values.put(SCHEULE_SEQ, check.getDB_SEQ());
		values.put(PROGRAM_TITLE,check.getDB_PROGRAM_TITLE());
		values.put(PROGRAM_CH_NUMBER, check.getDB_PROGRAM_CH_NUMBER());
		values.put(PROGRAM_CH_NAME, check.getDB_PROGRAM_CH_NANE());
		values.put(PROGRAM_CH_URL, check.getDB_CH_URL());
		values.put(PROGRAM_ALARM_CHECK, check.getDB_ALARM_CHECK());
		values.put(PROGRAM_ALARM_TIME, check.getDB_ALARM_TIME());
		values.put(PROGRAM_ID, check.getDB_PROGRAM_ID());
		values.put(PROGRAM_ALARAM_TIME_TEXT, check.getDB_ALARM_TIME_TEXT());
		values.put(PROGRAM_ALARAM_HD, check.getDB_ALARM_HD());
		values.put(PROGRAM_CH_GRADE, check.getDB_PROGRAM_CH_GRADE());

		TV_Alarm_database.insert(TV_ALARM_TABLE, null, values);
		//currentTVCH.add(check);
		TVAl_CheckMap.put(check.getDB_SEQ(),check);
		TVAl_CheckArr.add(check);

	}

	public void updateTask(TVCH_CheckList check) {
		assert (null != check);

		ContentValues values = new ContentValues();
		//values.put(PROGRAM_ID, check.getID());
		String where = String.format("%s = %s", CHANNEL_ID, check.getID());
		values.put(MY_CHANNEL, Boolean.toString(check.getMyChannel()));
		values.put(MY_CHANNEL_TIME, check.get_MyChannel_Time());
		/*values.put(PROGRAM_ALARM, Boolean.toString(check.getAlarm()));
		values.put(PROGRAM_ALARM_TIME, check.getAlarm_Time());//check.getReserve());
		 */		//long id = Long.parseLong(check.getID());
		TVCH_database.update(TVCH_CHECK_TABLE, values, where, null);

	}

	public void updatePRTask(TVCH_PR_CheckList check) {
		assert (null != check);


		ContentValues values = new ContentValues();
		//values.put(PROGRAM_ID, check.getID());
		String where = String.format("%s = %s", PROGRAM_ID, check.getID());
		//String where = String.format("'%s = %s'", PROGRAM_ID);//, check.getID());
		values.put(PROGRAM_ID, check.getID());
		values.put(PROGRAM_MY_CHANNEL, Boolean.toString(check.getMyProgram()));
		values.put(PROGRAM_MY_CHANNEL_TIME, check.get_MyProgram_Time());
		//values.put(PROGRAM_ALARM, Boolean.toString(check.getAlarm()));
		//values.put(PROGRAM_ALARM_TIME, check.getAlarm_Time());//check.getReserve());
		//long id = Long.parseLong(check.getID());
		TVPR_database.update(TVPR_CHECK_TABLE, values, where, null);
		//TVPR_database.update(TVPR_CHECK_TABLE, values, null, null);

		TVPr_CheckMap.get(check.getID()).setMyProgram(check.getMyProgram()) ;
		TVPr_CheckMap.get(check.getID()).set_MyProgram_Time(check.get_MyProgram_Time()) ;
		//TVPr_CheckMap.get(check.getID()).setAlarm(check.getAlarm()) ;
		//TVPr_CheckMap.get(check.getID()).setAlarmTime(check.getAlarm_Time()) ;
	}

	public void updateAlarmTask(TVCH_Alarm_CheckList check) {
		assert (null != check);

		TVAl_CheckMap.get(check.getDB_SEQ()).setDB_SEQ(check.getDB_SEQ()) ;
		TVAl_CheckMap.get(check.getDB_SEQ()).setDB_PROGRAM_TITLE(check.getDB_PROGRAM_TITLE()) ;
		TVAl_CheckMap.get(check.getDB_SEQ()).setDB_PROGRAM_CH_NUMBER(check.getDB_PROGRAM_CH_NUMBER());
		TVAl_CheckMap.get(check.getDB_SEQ()).setDB_CH_URL(check.getDB_CH_URL()) ;
		TVAl_CheckMap.get(check.getDB_SEQ()).setDB_ALARM_CHECK(check.getDB_ALARM_CHECK());
		TVAl_CheckMap.get(check.getDB_SEQ()).setDB_ALARM_TIME(check.getDB_ALARM_TIME());
		TVAl_CheckMap.get(check.getDB_SEQ()).setDB_PROGRAM_CH_GRADE(check.getDB_PROGRAM_CH_GRADE());

		ContentValues values = new ContentValues();
		/*
		//values.put(PROGRAM_ID, check.getID());
		String where = String.format("'%s = %s'", PROGRAM_ID, check.getID());
		values.put(PROGRAM_MY_CHANNEL, Boolean.toString(check.getMyProgram()));
		values.put(PROGRAM_MY_CHANNEL_TIME, check.get_MyProgram_Time());
		values.put(PROGRAM_ALARM, Boolean.toString(check.getAlarm()));
		values.put(PROGRAM_ALARM_TIME, check.getAlarm_Time());//check.getReserve());
		//long id = Long.parseLong(check.getID());
		TVPR_database.update(TVPR_CHECK_TABLE, values, where, null);
		 */
	}



	public void deleteAlarm(String seq){
		assert (null != seq);

		String where = String.format("%s = %s", SCHEULE_SEQ, seq);
		TV_Alarm_database.delete(TV_ALARM_TABLE, where, null);


		for (int i = 0; i < TVAl_CheckArr.size(); i++) {
			if(seq.equals(TVAl_CheckArr.get(i).getDB_SEQ()))
				TVAl_CheckArr.remove(i);
		}
		TVAl_CheckMap.remove(seq);//put(check.getDB_SEQ(),check);
	}

	public void deleteTasks(Long[] ids) {
		StringBuffer idList = new StringBuffer();
		for (int i =0; i< ids.length; i++) {
			idList.append(ids[i]);
			if (i < ids.length -1 ) {
				idList.append(",");
			}
		}

		String where = String.format("%s in (%s)", CHANNEL_ID, idList);
		TVCH_database.delete(TVCH_CHECK_TABLE, where, null);
	}


	public void deletePRTasks(String ids) {
		assert (null != ids);
		/*StringBuffer idList = new StringBuffer();
		for (int i =0; i< ids.length; i++) {
			idList.append(ids[i]);
			if (i < ids.length -1 ) {
				idList.append(",");
			}
		}*/

		//String where = String.format("%s in (%s)", PROGRAM_ID, ids);
		String where = String.format("%s='%s'", PROGRAM_ID, ids);
		//Log.d("Sangtae", "프로그램 테이블 삭제 갯수: "+ TVPR_database.delete(TVPR_CHECK_TABLE, where, null));
		TVPR_database.delete(TVPR_CHECK_TABLE, where, null);
		TVPr_CheckMap.remove(ids);
	}


	public void deleteAllAlarm(){
		TV_Alarm_database.delete(TV_ALARM_TABLE, null, null);
		TVAl_CheckArr.clear();
		TVAl_CheckMap.clear();//put(check.getDB_SEQ(),check);
	}

	public void deleteAllTasks() {
		TVCH_database.delete(TVCH_CHECK_TABLE, null, null);
		TVCh_CheckMap.clear();
	}

	public void deleteAllPRTasks() {
		TVPR_database.delete(TVPR_CHECK_TABLE, null, null);
		TVPr_CheckMap.clear();
	}

	//Defualt
	public void setTv_ALL_DATA_LIST(TV_ALL_DATA_LIST mTv_ALL_DATA_LIST) {
		if (this.mTv_ALL_DATA_LIST.size() > 0)
		this.mTv_ALL_DATA_LIST.clear();
		this.mTv_ALL_DATA_LIST = mTv_ALL_DATA_LIST;
	}

	public TV_ALL_DATA_LIST getTv_ALL_DATA_LIST() {
		return mTv_ALL_DATA_LIST;
	}
	public void setTV_SCUEDULE_LIST(SCUEDULE_DATA_LIST mTV_SCUEDULE_LIST) {
		this.mTV_SCUEDULE_LIST = mTV_SCUEDULE_LIST;
	}


	public RECORD_DATA_LIST getmRecord_DATA_LIST() {
		return mRecord_DATA_LIST;
	}

	public void setmRecord_DATA_LIST(RECORD_DATA_LIST mRecord_DATA_LIST) {
		this.mRecord_DATA_LIST = mRecord_DATA_LIST;
	}

	public TV_ALL_DATA_LIST getTv_HD_DATA_LIST() {
		return mTv_HD_DATA_LIST;
	}

	public void setTv_HD_DATA_LIST(TV_ALL_DATA_LIST mTv_HD_DATA_LIST) {
		this.mTv_HD_DATA_LIST = mTv_HD_DATA_LIST;
	}
	public TV_ALL_DATA_LIST getTv_PAY_DATA_LIST() {
		return mTv_PAY_DATA_LIST;
	}

	public void setTv_PAY_DATA_LIST(TV_ALL_DATA_LIST mTv_PAY_DATA_LIST) {
		this.mTv_PAY_DATA_LIST = mTv_PAY_DATA_LIST;
	}
	public SCUEDULE_DATA_LIST getTV_SCUEDULE_LIST() {
		return mTV_SCUEDULE_LIST;
	}

	public void setVod_GENRE_DATA_LIST(VOD_GENRE_LIST arg) {
		this.mVod_GENRE_DATA_LIST = arg;
	}
	public VOD_GENRE_LIST getVod_GENRE_DATA_LIST() {
		return mVod_GENRE_DATA_LIST;
	}

	public void setVod_GENRE_DATA_DETAIL_LIST(VOD_GENRE_DETAIL_LIST arg) {
		this.mVod_GENRE_DETAIL_DATA_LIST = arg;
	}
	public VOD_GENRE_DETAIL_LIST getVod_GENRE_DETAIL_DATA_LIST() {
		return mVod_GENRE_DETAIL_DATA_LIST;
	}
	public void setVod_PREVIEW_DATA_LIST(VOD_PREVIEW_LIST arg) {
		this.mVod_PREVIEW_DATA_LIST = arg;
	}
	public VOD_PREVIEW_LIST getVod_PREVIEW_DATA_LIST() {
		return mVod_PREVIEW_DATA_LIST;
	}
	public void setVod_NEWMOVIE_DATA_LIST(VOD_NEWMOVIE_LIST arg) {
		this.mVod_NEWMOVIE_DATA_LIST = arg;
	}
	public VOD_NEWMOVIE_LIST getVod_NEWMOVIE_DATA_LIST() {
		return mVod_NEWMOVIE_DATA_LIST;
	}

	public void setVod_TVREPLAY_DATA_LIST(VOD_TVREPLAY_LIST arg) {
		this.mVod_TVREPLAY_DATA_LIST = arg;
	}
	public VOD_TVREPLAY_LIST getVod_TVREPLAY_DATA_LIST() {
		return mVod_TVREPLAY_DATA_LIST;
	}
	public void setSetting_GUIDE_DATA_LIST(GUIDE_DATA_LIST arg) {
		this.mSetting_GUIDE_DATA_LIST = arg;
	}
	public GUIDE_DATA_LIST getSetting_GUIDE_DATA_LIST() {
		return mSetting_GUIDE_DATA_LIST;
	}
	public void setJoyN_DATA_LIST(JOYN_DATA_LIST arg) {
		this.mJoyn_DATA_LIST = arg;
	}
	public JOYN_DATA_LIST getJoyN_DATA_LIST() {
		return mJoyn_DATA_LIST;
	}
	public void setJoyN_DET_DATA_LIST(JOYN_DET_DATA arg) {
		this.mJoyn_DATA_DET_LIST = arg;
	}
	public JOYN_DET_DATA getJoyN_DET_DATA_LIST() {
		return mJoyn_DATA_DET_LIST;
	}


	public NaviBtn_Singleton getNaviBtn_Singleton() {
		return naviBtn_Singleton.getInstance();
	}

	public RadioButton getMainrbFirst() {
		return MainrbFirst;
	}
	public void setMainrbFirst(RadioButton mainrbFirst) {
		MainrbFirst = mainrbFirst;
	}
	public RadioButton getMainrbSecond() {
		return MainrbSecond;
	}

	public RadioButton getMainrbThird() {
		return MainrbThird;
	}

	public RadioButton getMainrbFourth() {
		return MainrbFourth;
	}
	public RadioButton getMainrbFifth() {
		return MainrbFifth;
	}

	public void setMainrbSecond(RadioButton mainrb) {
		MainrbSecond = mainrb;
	}
	public void setMainrbThird(RadioButton mainrb) {
		MainrbThird = mainrb;
	}
	public void setMainrbFourth(RadioButton mainrb) {
		MainrbFourth = mainrb;
	}
	public void setMainrbFifth(RadioButton mainrb) {
		MainrbFifth = mainrb;
	}

	public Boolean getFirstLoding() {
		return FirstLoding;
	}

	public void setProductID(String productID) {
		ProductID = productID;
	}

	public String getProductID() {
		return ProductID;

	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setLocationID(String locationID) {
		LocationID = locationID;
	}

	public String getLocationID() {
		return LocationID;
	}

	public void setLocationName(String locationName) {
		LocationName = locationName;
	}

	public String getLocationName() {
		return LocationName;
	}
	public void setLocationName2(String locationName2) {
		LocationName2 = locationName2;
	}

	public String getLocationName2() {
		return LocationName2;
	}

	public String getSystemUDID() {
		return SystemUDID;
	}

	public void setSystemUDID(String systemUDID) {
		SystemUDID = systemUDID;
		cnmEditor.putString(SystemUDIDKey, SystemUDID);
		cnmEditor.commit();
	}

	public void setTVCH_SettigChecked(Boolean tVCH_SettigChecked) {
		this.TVCH_SettigChecked = tVCH_SettigChecked;
		cnmEditor.putBoolean(TVCH_SettigChecked_KEY, tVCH_SettigChecked);
		cnmEditor.commit();
	}
	public Boolean getTVCH_SettigChecked() {
		return TVCH_SettigChecked;
	}

	public void setInitViewSettingValue(int initValue) {
		InitViewSettingValue = initValue;
	}
	public void commitInitViewSettingValue() {
		cnmEditor.putInt(InitViewSettingKey, InitViewSettingValue);
		cnmEditor.commit();
	}
	public int getInitViewSettingValue() {
		return InitViewSettingValue;
	}

	public void setAdultAuth(Boolean arg) {
		AdultAuth = arg;
	}
	public void setAdultAutoAuth(Boolean arg) {
		AdultAutoAuth = arg;
	}
	public void commitAdultAutoAuth() {
		cnmEditor.putBoolean(AdultAutoAuthKey, AdultAutoAuth);
		cnmEditor.commit();
	}

	public void setSettopRegAuth(Boolean settopRegAuth) {
		SettopRegAuth = settopRegAuth;
	}

	public Boolean getSettopRegAuth() {
		return SettopRegAuth;
	}

	public void commitSettopReg(String aSettopType) {
		cnmEditor.putBoolean(SettopRegAuthKey, SettopRegAuth);
		if (aSettopType != null) {
			if (!aSettopType.equals("NULL") && aSettopType.length() > 0) {
				cnmEditor.putString(SettopKindKey, aSettopType);
				SettopBoxType = aSettopType;
			}
		}
		cnmEditor.commit();
	}

	public String getSettopBoxType() {
		if (!SettopBoxType.equals("NULL")) {
			return SettopBoxType;
		}else {
			return "미등록";
		}
	}

	public Boolean getAdultAuth() {
		return AdultAuth;
	}
	public Boolean getAdultAutoAuth() {
		return AdultAutoAuth;
	}

	//length 전달 인자값 만큼 글자수를 맞춰줌
	public String endEllipsize(String str, int length)
	{

		//		int width = tv.getWidth();

		//		if(width < length)
		if(str.length() > length)
		{
			int cutPostion = str.length() - length;
			int conStart  = str.length() -3 - cutPostion;
			str = str.substring(0, conStart) + "...";
		}

		return str;
	}

	public int GetNetworkInfo()
	{
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();

		if(ni !=null){

			switch(ni.getType())
			{
			case ConnectivityManager.TYPE_WIFI:
			{
				return WIFI_CONNECTED;
			}
			case ConnectivityManager.TYPE_MOBILE:
			{
				return MOBILE_CONNECTED;
			}
			}
		}
		return DECONNECTE;
	}

	public String GetExternalStoragePath()
	{
		String ext= Environment.getExternalStorageState();
		if(ext.equals(Environment.MEDIA_MOUNTED))
		{
			return ExternalStoregePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		else
		{
			return ExternalStoregePath = Environment.MEDIA_UNMOUNTED;
		}
	}

	public void requestKillProcess(final Context context){

		//#1. first check api level.
		int sdkVersion = Integer.parseInt(Build.VERSION.SDK);

		if (sdkVersion < 8){
			//#2. if we can use restartPackage method, just use it.
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			am.restartPackage(getPackageName());
		}
		else
		{
			//#3. else, we should use killBackgroundProcesses method.
			new Thread(new Runnable() {

				@Override
				public void run() {
					ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
					String name = getApplicationInfo().processName;

					//pooling the current application process importance information.
					while(true){
						List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
						for(RunningAppProcessInfo i : list){
							if(i.processName.equals(name) == true){
								//#4. kill the process,
								//only if current application importance is less than IMPORTANCE_BACKGROUND
								if(i.importance >= RunningAppProcessInfo.IMPORTANCE_BACKGROUND)
									am.restartPackage(getPackageName()); //simple wrapper of killBackgrounProcess
								else
									Thread.yield();
								break;
							}
						}
					}
				}
			}, "Process Killer").start();
		}
	}


	public static String getPhoneNumber(Context context)
	{
		TelephonyManager tMgr =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
		String phone = "";
		try{
			if(tMgr.getLine1Number()!=null){
				phone = tMgr.getLine1Number();
			}
			phone = phone.substring(phone.length()-10,phone.length());
			phone="0"+phone;
		}catch(Exception e){
			e.printStackTrace();
		}
		return phone;
	}

	public String getMobileIMIE(Context context)
	{
		String UDID;// = getSystemUDID();
		//if (UDID.equals(""))
			synchronized (SystemUDID) {
				if (SystemUDID.equals("")) {
					//		System.getString(this.getContentResolver(),System.ANDROID_ID);
					TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
					UDID = telephonyManager.getDeviceId();

					if (UDID != null) {
						setSystemUDID(UDID);
						return telephonyManager.getDeviceId();
					}else {
						WifiManager wfManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
						WifiInfo wifiInfo = wfManager.getConnectionInfo();
						UDID = wifiInfo.getMacAddress().replace(":", "");
						setSystemUDID(UDID);
						return UDID;
					}
				}
			}
		return getSystemUDID();
		//return "rumpus_test123$";
	}

}
