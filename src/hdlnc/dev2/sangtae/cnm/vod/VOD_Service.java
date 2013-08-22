package hdlnc.dev2.sangtae.cnm.vod;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.CNM_SearchUpDialog;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.CNM_SearchUpDialog.OnEnterKeyListener;
import hdlnc.dev2.sangtae.cnm.main.main;
import hdlnc.dev2.sangtae.cnm.settings.Setting_SettopRegi;


import hdlnc.dev2.sangtae.cnm.R;
import android.R.style;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ToggleButton;


public class VOD_Service extends TabActivity implements OnEnterKeyListener, OnClickListener {

	private static TabHost	mTabHost;
	private static int mCurrentTab=0;
	private static int mCurrentTabID= R.id.vodService_previewList_radiobutton;
	private RadioGroup rg;
	private ToggleButton mTypeToggleBtn;
	private CNMApplication tempApp;
	private  NaviBtn_Singleton mNaviBtnSingleton;

	public static boolean[] ModeChangeFlags ={false, true};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vod_service);
		tempApp = (CNMApplication)getApplication();	
		mNaviBtnSingleton = tempApp.getNaviBtn_Singleton().getInstance();

		if(tempApp.getAdultAutoAuth())
			tempApp.setAdultAuth(true);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setupUI();

		setupNaviBar();
		getTabHost().setCurrentTab(mCurrentTab);
		rg.check(mCurrentTabID);

		Log.d("test", "call onResume!");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	private void setupNaviBar() {

		mTypeToggleBtn = mNaviBtnSingleton.getNaviLeftSubBtn();
		mTypeToggleBtn.setBackgroundResource(R.drawable.top_button_list);
		mTypeToggleBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();

				if(mCurrentTab == 1 && mTypeToggleBtn.isChecked())
				{
					mCurrentTab=4;
					ModeChangeFlags[0] = true;
				}
				else
					if(mCurrentTab == 4 && !mTypeToggleBtn.isChecked())
					{
						mCurrentTab=1;
						ModeChangeFlags[0] = false;
					}
					else
						if(mCurrentTab == 2 && !mTypeToggleBtn.isChecked())
						{
							mCurrentTab=5;
							ModeChangeFlags[1] = false;
						}
						else
							if(mCurrentTab == 5 && mTypeToggleBtn.isChecked())
							{
								mCurrentTab=2;
								ModeChangeFlags[1] = true;
							}

				getTabHost().setCurrentTab(mCurrentTab);
			}
		});

		TextView txtHeader = mNaviBtnSingleton.getNaviHeaderText();
		txtHeader.setText("VOD");

		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_home);
		naviLeftBtn.setVisibility(View.VISIBLE);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				main tempActivity = (main)getParent().getParent();
				tempActivity.Home_View(0);
			}
		});

		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		naviRightBtn.setBackgroundResource(R.drawable.top_button_choice);
		naviRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				tempApp.ButtonBeepPlay();

				if(tempApp.getSettopRegAuth())
				{
					Intent intent = new Intent(getParent(), VODSE_Zzim_List.class);
					TabGroupActivity parentActivity = (TabGroupActivity)getParent();
					parentActivity.startChildActivity("Zzim_List", intent);
				}
				else
				{
					Intent intent = new Intent(getParent(), Setting_SettopRegi.class);
					TabGroupActivity parentActivity = (TabGroupActivity)getParent();
					parentActivity.startChildActivity("SettopRegi", intent);
				}
			}
		});

		ImageButton naviRightSubBtn = (ImageButton)mNaviBtnSingleton.getNaviRightSubBtn();
		naviRightSubBtn.setBackgroundResource(R.drawable.top_button_search);//ImageDrawable(drawableSrch);
		naviRightSubBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				tempApp.ButtonBeepPlay();

				// TODO Auto-generated method stub
				new CNM_SearchUpDialog(getParent(), style.Theme_Translucent_NoTitleBar, 
						"VOD 명으로 검색", VOD_Service.this).show();
			}
		});
	}
	private void setupUI() {

		mTabHost	=	(TabHost)findViewById(android.R.id.tabhost);

		mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("Firest_Tab").setContent(new Intent(this, VODSE_Preview.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("Second_Tab").setContent(new Intent(this, VODSE_NewMovie_Coverflow.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("Third_Tab").setContent(new Intent(this, VODSE_TvReplay_List.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("Fourth_Tab").setContent(new Intent(this, VODSE_GenreRoot.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("Fifth_Tab").setContent(new Intent(this, VODSE_NewMovie_List.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("Sixth_Tab").setContent(new Intent(this, VODSE_TvReplay_Coverflow.class)));


		RadioButton rb1 = (RadioButton)findViewById(R.id.vodService_previewList_radiobutton);
		RadioButton rb2 = (RadioButton)findViewById(R.id.vodService_newVideo_radiobutton);
		RadioButton rb3 = (RadioButton)findViewById(R.id.vodService_tvReplay_radiobutton);
		RadioButton rb4 = (RadioButton)findViewById(R.id.vodService_category_radiobutton);
		rb1.setOnClickListener(VOD_Service.this);
		rb2.setOnClickListener(VOD_Service.this);
		rb3.setOnClickListener(VOD_Service.this);
		rb4.setOnClickListener(VOD_Service.this);

		rg = (RadioGroup) findViewById(R.id.vodService_radiogroup);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, final int checkedId) {

				mCurrentTabID = checkedId;

				switch (checkedId) {
				case R.id.vodService_previewList_radiobutton:
				{
					mCurrentTab = 0;
					break;
				}
				case R.id.vodService_newVideo_radiobutton:
				{
					mTypeToggleBtn.setChecked(ModeChangeFlags[0]);
					if(mTypeToggleBtn.isChecked())
						mCurrentTab = 4;
					else
						mCurrentTab = 1;

					break;
				}
				case R.id.vodService_tvReplay_radiobutton:
				{
					mTypeToggleBtn.setChecked(ModeChangeFlags[1]);
					if(!mTypeToggleBtn.isChecked())
						mCurrentTab = 5;
					else
						mCurrentTab = 2;

					break;
				}
				case R.id.vodService_category_radiobutton:
				{
					mCurrentTab = 3;
					break;
				}
				}

				getTabHost().setCurrentTab(mCurrentTab);
			}
		});



	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	public void onEnterKeyPressed(String str) {
		// TODO Auto-generated method stub
		Intent searchResultIntent = new Intent(this, VODSE_SearchResult_List.class);
		TabGroupActivity parent = (TabGroupActivity)getParent(); 
		searchResultIntent.putExtra("KeyWord", str);
		parent.startChildActivity("VODSE_SearchResult_List", searchResultIntent);
	}

	public static void initTabHost() {
		mCurrentTab=0;
		mCurrentTabID= R.id.vodService_previewList_radiobutton;
		VODSE_Preview.CurrentPosition=0;
		VODSE_NewMovie_Coverflow.CurrentPosition=0;
		VODSE_TvReplay_Coverflow.CurrentPosition=0;
		VOD_Service.ModeChangeFlags[0] = false;
		VOD_Service.ModeChangeFlags[1] = true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		tempApp.ButtonBeepPlay();
	}

}
