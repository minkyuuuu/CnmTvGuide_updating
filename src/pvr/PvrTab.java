/**
 * 
 */
package pvr;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.CNM_SearchUpDialog;
import hdlnc.dev2.sangtae.cnm.global.CNM_WarningDialog;
import hdlnc.dev2.sangtae.cnm.global.Intent_KEY_Define;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.main.main;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Genre;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_HD;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_My;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Paid;
import hdlnc.dev2.sangtae.cnm.tvchannel.TV_Channel;
import android.R.style;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
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
public class PvrTab extends TabActivity {
	private static TabHost	mTabHost;
	private static int mCurrentTab=0;
	private RadioGroup rg;

	private CNMApplication tempApp;

	private static int rgID = R.id.pvr_menu_reserve;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pvr_tab);

		tempApp = (CNMApplication)getApplication();
		setupUI();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int a = getTabHost().getCurrentTab();
		if(a != rgID)
			rg.check(rgID);
		
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
		tempApp.getNaviBtn_Singleton().getInstance().getNaviHeaderText().setText("PVR");
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftSubBtn().setVisibility(View.GONE);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().setBackgroundResource(R.drawable.top_button_home);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				//tempApp.setChannelTabID(R.id.TVCH_AllCH_TABBtn);
				rgID = R.id.pvr_menu_reserve;
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

		mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("Firest_Tab").setContent(new Intent(this, Pvr_RecordReserveList.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("Second_Tab").setContent(new Intent(this, Pvr_RecordList.class)));

		rg = (RadioGroup) findViewById(R.id.PVR_Radiogroup);
		tempApp.setmPVRTabGroup(rg);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, final int checkedId) {
				rgID =  checkedId;
				switch (checkedId) {
				case R.id.pvr_menu_reserve:
					getTabHost().setCurrentTab(0);
					mCurrentTab=0;
					break;
				case R.id.pvr_menu_list:
					getTabHost().setCurrentTab(1);
					mCurrentTab=1;
					break;
				}
			}
		});

		//tempApp.setChannelTabGroup(rg);
		setRadioBtClick();
	}

	void setRadioBtClick() {
		RadioButton rb1 = (RadioButton) findViewById(R.id.pvr_menu_list);
		RadioButton rb2 = (RadioButton) findViewById(R.id.pvr_menu_reserve);

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
	}

}
