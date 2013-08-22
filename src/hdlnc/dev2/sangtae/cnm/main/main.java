package hdlnc.dev2.sangtae.cnm.main;

import static hdlnc.dev2.sangtae.cnm.main.Main_MenuActivity.RESULT_EXIT;
import static hdlnc.dev2.sangtae.cnm.main.Main_MenuActivity.RESULT_SELECT;
import static hdlnc.dev2.sangtae.cnm.main.Main_MenuActivity.RESULT_SELECT_KEY;
import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.DATA.SERVICE_NOTICE_INFO;
import hdlnc.dev2.sangtae.cnm.settings.SettingGroup;
import hdlnc.dev2.sangtae.cnm.tvchannel.TvChannelTabGroup;
import hdlnc.dev2.sangtae.cnm.vod.VodServiceTabGroup;
import pvr.PVRTabGroup;
import remote.RemoteTabGroup;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ToggleButton;

public class main extends TabActivity{
	/** Called when the activity is first created. */
	final String	ACTION_KEYEVENT	= "hdlnc.dev2.sangtae.cnm.SOFT_KEY_EVENT";

	private final static String DEBUG_TAG = "C&M main";
	
	

	private TabHost	mTabHost;
	private NaviBtn_Singleton mNaviBtnSingleton;
	private CNMApplication cnmAPP;
	public int prevTab=-1;
	public void setPrevTab(int prevTab) {
		this.prevTab = prevTab;
	}
	public int getPrevTab() {
		return prevTab;
	}
	
	Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.i("hwang", "main - onCreate() 1");
		
		Log.d("Sangtae", "oncreate - main");
		startActivityForResult(new Intent(this, SplashActivity.class), 1);							//로딩 화면 출력 시작
		context = this;
		cnmAPP = (CNMApplication)getApplication();						// 공용 데이터 접근용
		cnmAPP.getNaviBtn_Singleton().setInstance((ImageButton)findViewById(R.id.Navi_LeftImgBtn), 
				(ToggleButton)findViewById(R.id.Navi_Sub_LeftImgBtn), 
				(ImageButton)findViewById(R.id.Navi_RightImgBtn), 
				(ImageButton)findViewById(R.id.Navi_Sub_RightImgBtn),
				(TextView)findViewById(R.id.Navi_TitleText));

		mNaviBtnSingleton = cnmAPP.getNaviBtn_Singleton().getInstance();

		setRadioBtClick();
		
		Log.i("hwang", "main - onCreate() 2");
		
		//Log.d("Sangtae", XML_Address_Define.Authenticate.getClientsettopboxregist("aaaaaa", "1010"));


	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		CNMApplication tempApp = (CNMApplication)getApplication();
		tempApp.Parser_data_Reset();
	}



	private void setupUI() {
		// Tab Radio Button

		mTabHost	=	(TabHost)findViewById(android.R.id.tabhost);

		RadioButton rbFirst = (RadioButton) findViewById(R.id.first);
		cnmAPP.setMainrbFirst(rbFirst);
		RadioButton rbSecond = (RadioButton) findViewById(R.id.second);
		cnmAPP.setMainrbSecond(rbSecond);
		RadioButton rbThird = (RadioButton) findViewById(R.id.third);
		cnmAPP.setMainrbThird(rbThird);
		RadioButton rbFourth = (RadioButton) findViewById(R.id.fourth);
		cnmAPP.setMainrbFourth(rbFourth);
		RadioButton rbFifth = (RadioButton) findViewById(R.id.fifth);
		cnmAPP.setMainrbFifth(rbFifth);
		
		mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("Firest_Tab").setContent(new Intent(this, TvChannelTabGroup.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("Second_Tab").setContent(new Intent(this, VodServiceTabGroup.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab3").setIndicator("Third_Tab").setContent(new Intent(this, RemoteTabGroup.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab4").setIndicator("Fourth_Tab").setContent(new Intent(this, PVRTabGroup.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab5").setIndicator("Fifth_Tab").setContent(new Intent(this, SettingGroup.class)));

		RadioGroup rg = (RadioGroup) findViewById(R.id.states);

		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, final int checkedId) {
				cnmAPP.setChannelTabID(R.id.TVCH_AllCH_TABBtn);
				prevTab = getTabHost().getCurrentTab();
				switch (checkedId) {
				case R.id.first:
					getTabHost().setCurrentTab(0);
					if (cnmAPP.getMainrbFirst() != null) {
						cnmAPP.getMainrbFirst().performClick();
					}
					break;
				case R.id.second:
					getTabHost().setCurrentTab(1);
					if (cnmAPP.getMainrbSecond() != null) {
						cnmAPP.getMainrbSecond().performClick();
					}
					break;
				case R.id.third:
					getTabHost().setCurrentTab(2);
					if (cnmAPP.getMainrbThird() != null) {
						cnmAPP.getMainrbThird().performClick();
					}
					break;
				case R.id.fourth:
					getTabHost().setCurrentTab(3);
					if (cnmAPP.getMainrbFourth() != null) {
						cnmAPP.getMainrbFourth().performClick();
					}
					break;
				case R.id.fifth:
					getTabHost().setCurrentTab(4);
					if (cnmAPP.getMainrbFifth() != null) {
						cnmAPP.getMainrbFifth().performClick();
					}
					break;
				}
			}
		});

		cnmAPP.setMainTabGroup(rg);
	}

	public void Home_View(int noticeLocation) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getApplicationContext(), Main_MenuActivity.class);
		intent.putExtra("NoticeLocation", noticeLocation);
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		SERVICE_NOTICE_INFO serviceNotice = null;
		
		switch (resultCode) {
		case Activity.RESULT_OK:
			switch (cnmAPP.getInitViewSettingValue()) {
			case 1:
				Home_View(1);
				break;
			case 2:
				serviceNotice = cnmAPP.getServiceNoticeInfo(3);
				if (serviceNotice == null) {
					this.setupUI();
					cnmAPP.getMainTabGroup().check(R.id.second);
				}
				else {
					Home_View(3);
					//showServiceNotice(serviceNotice);
				}
				break;
			case 3:
				serviceNotice = cnmAPP.getServiceNoticeInfo(2);
				if (serviceNotice == null) {
					this.setupUI();
					cnmAPP.getMainTabGroup().check(R.id.first);
				}
				else {
					Home_View(2);
					//showServiceNotice(serviceNotice);
				}
				break;

			default:
				break;
			}
			break;
		case RESULT_EXIT:
			//onBackPressed();
			this.finish();
			break;
		case RESULT_SELECT:
			this.setupUI();
			switch (data.getIntExtra(RESULT_SELECT_KEY, R.id.first)) {
			case R.id.first:
				cnmAPP.getMainTabGroup().check(R.id.first);
				break;
			case R.id.second:
				cnmAPP.getMainTabGroup().check(R.id.second);
				break;
			case R.id.third:
				cnmAPP.getMainTabGroup().check(R.id.third);
				break;
			case R.id.fourth:
				cnmAPP.getMainTabGroup().check(R.id.fourth);
				break;
			case R.id.fifth:
				cnmAPP.getMainTabGroup().check(R.id.fifth);
				break;

			default:
				break;
			}



			break;
		default:
			break;
		}
	}

	void showServiceNotice(SERVICE_NOTICE_INFO serviceNotice) {
		TextView title = new TextView(context);
		title.setBackgroundColor(Color.TRANSPARENT);
		title.setPadding(10, 10, 10,10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.WHITE);
		title.setTextSize(20);
		title.setText(serviceNotice.getTitle());					

		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(context);
		alert_internet_status.setCustomTitle(title);
		alert_internet_status.setMessage(serviceNotice.getContent());
		alert_internet_status.setPositiveButton("닫 기",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); // 닫기
				//finish();
			}
		});
		AlertDialog alert = alert_internet_status.create();
		alert.show();
	}
	
	void setRadioBtClick() {

		OnTouchListener tempTouchListener = new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					cnmAPP.ButtonBeepPlay();
					Log.d(DEBUG_TAG, "setRadioBtClick()");
					SERVICE_NOTICE_INFO serviceNotice = null;
					if (findViewById(R.id.first) == v) {
						serviceNotice = cnmAPP.getServiceNoticeInfo(2);						
						Log.d(DEBUG_TAG, "TV채널 공지");
					}
					else if (findViewById(R.id.second) == v) {				
						serviceNotice = cnmAPP.getServiceNoticeInfo(3);
						Log.d(DEBUG_TAG, "VOD 공지");
					}
					else if (findViewById(R.id.third) == v) {				
						serviceNotice = cnmAPP.getServiceNoticeInfo(4);
						Log.d(DEBUG_TAG, "리모콘 공지");
					}
					else if (findViewById(R.id.fourth) == v) {				
						Log.d(DEBUG_TAG, "PVR 공지");
					}
					else if (findViewById(R.id.fifth) == v) {				
						serviceNotice = cnmAPP.getServiceNoticeInfo(6);
						Log.d(DEBUG_TAG, "안내/설정 공지");
					}
					
					if (serviceNotice != null) {
						Log.d(DEBUG_TAG, "공지 올림.");
						showServiceNotice(serviceNotice);
						return true;
					}
				}
				return false;
			}
		};
		((RadioButton) findViewById(R.id.first)).setOnTouchListener(tempTouchListener);
		((RadioButton) findViewById(R.id.second)).setOnTouchListener(tempTouchListener);
		((RadioButton) findViewById(R.id.third)).setOnTouchListener(tempTouchListener);
		((RadioButton) findViewById(R.id.fourth)).setOnTouchListener(tempTouchListener);
		((RadioButton) findViewById(R.id.fifth)).setOnTouchListener(tempTouchListener);
	}
}