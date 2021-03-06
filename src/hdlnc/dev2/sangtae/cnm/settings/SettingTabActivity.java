package hdlnc.dev2.sangtae.cnm.settings;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.vod.VOD_Service;


import hdlnc.dev2.sangtae.cnm.R;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;


public class SettingTabActivity extends TabActivity{
	
	
	private static TabHost	mTabHost;
	public static final String DEBUG_TAG="EtcTabActivity";

	private CNMApplication mApp;
	private static int mCurrentTab =0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	      
		setContentView(R.layout.setting_tab);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setupUI();
	}
	
	private void setupUI()
	{
		
		mApp = (CNMApplication)getApplication();	
		
		mTabHost	=	(TabHost)findViewById(android.R.id.tabhost);
		mTabHost.addTab(mTabHost.newTabSpec("Tab").setIndicator("Firest_Tab").setContent(new Intent(this, Setting.class)));
		
		getTabHost().setCurrentTab(mCurrentTab);
		
	}
	
}
