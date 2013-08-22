/**
 * 
 */
package remote;

import java.util.HashMap;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.CNM_SearchUpDialog;
import hdlnc.dev2.sangtae.cnm.global.CNM_WarningDialog;
import hdlnc.dev2.sangtae.cnm.global.Intent_KEY_Define;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.CHECK_RESPONSE_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.RESPONSE_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.TV_ALL_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.TV_ALL_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.TV_ALL_PASER;
import hdlnc.dev2.sangtae.cnm.main.main;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Genre;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_HD;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_My;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Paid;
import hdlnc.dev2.sangtae.cnm.tvchannel.TV_Channel;
import hdlnc.dev2.sangtae.cnm.vod.VODSE_Detailview;
import android.R.string;
import android.R.style;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.ToggleButton;

/**
 * @author Sangtae
 *
 */
public class RemoteTab extends TabActivity {

	public static final String DEBUG_TAG = "RemoteTab";

	private static TabHost	mTabHost;
	private static int mCurrentTab=0;
	private RadioGroup rg;

	private RelativeLayout mTransLayout;
	private RelativeLayout mRemoconButtonLayout;
	private ImageButton	mPower;
	private ImageButton	mVolUP;
	private ImageButton	mVolDOWN;

	private CNMApplication tempApp;

	private static int rgID = R.id.remocon_menu_allch;

	private String Udid;
	private RemoteControlAsyncTask mPowerTask;
	private RemoteControlAsyncTask mVolumeTask;
	private final String NET_ERR_TASK = "RemoteException";
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_tab);

		tempApp = (CNMApplication)getApplication();
		Udid = tempApp.getMobileIMIE(this);
		
		setupUI();
	}

	/* (non-Javadoc)
	 * @see android.app.ActivityGroup#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (getTabHost().getCurrentTab() != rgID) {
			rg.check(rgID);
		}
		setupNaviBar();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().performClick();
	}

	private void setupNaviBar() {
		// TODO Auto-generated method stub
		tempApp.getNaviBtn_Singleton().getInstance().getNaviHeaderText().setText("리모콘");
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftSubBtn().setVisibility(View.GONE);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().setBackgroundResource(R.drawable.top_button_home);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				rgID = R.id.remocon_menu_allch;
				//tempApp.setChannelTabID(R.id.TVCH_AllCH_TABBtn);
				main tempActivity = (main)getParent().getParent();
				tempActivity.Home_View(0);
			}
		});
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().setVisibility(View.GONE);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightSubBtn().setVisibility(View.VISIBLE);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightSubBtn().setBackgroundResource(R.drawable.top_button_useguide);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightSubBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				goToGuideInfo();
			}
		});
	}
	
	private void goToGuideInfo() {
		// TODO Auto-generated method stub
		tempApp.setGoToSetting(Intent_KEY_Define.Set_Mode.MODE.GUIDE_INFO);
		tempApp.getMainrbFifth().performClick();
	}

	private void setupUI() {
		mTabHost	=	(TabHost)findViewById(android.R.id.tabhost);

		mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("Firest_Tab").setContent(new Intent(this, Remote_All_CH.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("Second_Tab").setContent(new Intent(this, Remote_My_CH.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab3").setIndicator("Third_Tab").setContent(new Intent(this, TV_Message.class)));

		rg = (RadioGroup) findViewById(R.id.Remote_Radiogroup);
		tempApp.setmRemoteTabGroup(rg);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, final int checkedId) {
				rgID =  checkedId;
				switch (checkedId) {
				case R.id.remocon_menu_allch:
					mRemoconButtonLayout.setVisibility(View.VISIBLE);
					getTabHost().setCurrentTab(0);
					mCurrentTab=0;
					break;
				case R.id.remocon_menu_mych:
					mRemoconButtonLayout.setVisibility(View.VISIBLE);
					getTabHost().setCurrentTab(1);
					mCurrentTab=1;
					break;
				case R.id.remocon_menu_tvm:
					mRemoconButtonLayout.setVisibility(View.GONE);
					getTabHost().setCurrentTab(2);
					mCurrentTab=2;
					break;
				}
			}
		});

		//tempApp.setChannelTabGroup(rg);
		setRadioBtClick();
	}

	void setRadioBtClick() {
		RadioButton rb1 = (RadioButton) findViewById(R.id.remocon_menu_allch);
		RadioButton rb2 = (RadioButton) findViewById(R.id.remocon_menu_mych);
		RadioButton rb3 = (RadioButton) findViewById(R.id.remocon_menu_tvm);

		OnTouchListener listener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					tempApp.ButtonBeepPlay();
				}
				return false;
			}
		};
		rb1.setOnTouchListener(listener);
		rb2.setOnTouchListener(listener);
		rb3.setOnTouchListener(listener);

		//	////	////	////	////	////	////	////	////	////	////	////	////	////	////	////	////	////	////	////	////	//
		mTransLayout = (RelativeLayout)findViewById(R.id.TransLayout);
		mRemoconButtonLayout	=	(RelativeLayout)findViewById(R.id.remocon_button_layout);
		mPower					= 	(ImageButton)findViewById(R.id.remocon_button_power);
		mVolUP 					=	(ImageButton)findViewById(R.id.remocon_button_volup);
		mVolDOWN				=	(ImageButton)findViewById(R.id.remocon_button_voldown);
		tempApp.setTransLayout(mTransLayout);
		tempApp.setPower(mPower);
		tempApp.setVolUP(mVolUP);
		tempApp.setVolDOWN(mVolDOWN);
		
		mPowerTask = new RemoteControlAsyncTask();
		mPower.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				if (mPowerTask.getStatus() != AsyncTask.Status.RUNNING) {
					mPowerTask = new RemoteControlAsyncTask();
					mPowerTask.execute(XML_Address_Define.RemoteController.getSetremotepowercontrol(Udid, XML_Address_Define.RemoteController.REMOTE_CON_POW_OFF));
				}
			}
		});

		mVolumeTask = new RemoteControlAsyncTask();
		mVolUP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				if (mVolumeTask.getStatus() != AsyncTask.Status.RUNNING) {
					mVolumeTask = new RemoteControlAsyncTask();
					mVolumeTask.execute(XML_Address_Define.RemoteController.getSetremotevolumecontrol(Udid, XML_Address_Define.RemoteController.REMOTE_CON_VOLUM_UP));
				}
			}
		});

		mVolDOWN.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				if (mVolumeTask.getStatus() != AsyncTask.Status.RUNNING) {
					mVolumeTask = new RemoteControlAsyncTask();
					mVolumeTask.execute(XML_Address_Define.RemoteController.getSetremotevolumecontrol(Udid, XML_Address_Define.RemoteController.REMOTE_CON_VOLUM_DOWN));
				}
			}
		});

	}

	// 리스트 스레드
	class RemoteControlAsyncTask extends AsyncTask<String, Void, RESPONSE_DATA>{
		@Override
		protected RESPONSE_DATA doInBackground(String... params) {

			String requestURL = params[0];
			Log.d(DEBUG_TAG, "### URL Request 리모콘 명령: "+requestURL);
			if (CHECK_RESPONSE_PASER.start(requestURL)) {
				return CHECK_RESPONSE_PASER.getArrayList();
			}

			return null;
		}

		@Override
		protected void onPostExecute(RESPONSE_DATA result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Message tMessage =  tempApp.getGlobalRemotHandl().obtainMessage();
			if (result != null) {
				if (result.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)) {
					tMessage.what = 666;
					Log.d("Sangtae", "리모콘 명령 성공");
				}else {
					Log.d("Sangtae", "리모콘 명령 실패: "+ result.getErrorString());
					tMessage.what = 3304;
					Bundle bundle = new Bundle();
					bundle.putString(NET_ERR_TASK, String.format("'%s' 에러가 발생하였습니다. ", result.getErrorString()));
					tMessage.setData(bundle);
				}
			} else {
				// 에러 코드 표시
				tMessage.what = 3304;
				Bundle bundle = new Bundle();
				bundle.putString(NET_ERR_TASK, "응답이 없습니다.");
				tMessage.setData(bundle);
			}
			tempApp.getGlobalRemotHandl().sendMessageDelayed(tMessage, 1000);
		}
	}
}
