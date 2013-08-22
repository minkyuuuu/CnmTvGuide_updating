package hdlnc.dev2.sangtae.cnm.tvchannel;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;

import hdlnc.dev2.sangtae.cnm.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

public class TVCH_My extends TabActivity implements OnClickListener{
	private CNMApplication tempApp;
	private static int	mCurrentTab=0;
	private TabHost		mTabHost;
	private Boolean		mBackBtnCheck=false;
	private RadioGroup rg;
	private static int rgSelect=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tvch_my);
		tempApp = (CNMApplication)getApplication();
		setupTAB();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		setupUI();
		if (rgSelect == 0) {
			rg.check(R.id.TVCH_MYCH);
			getTabHost().setCurrentTab(0);
		} else {
			rg.check(R.id.TVCH_MY_ALARM);
			getTabHost().setCurrentTab(1);
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		
		/*if (tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().isChecked()) {
			tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().setChecked(false);
		}*/
	}

	private void setupUI() {
		// TODO Auto-generated method stub
		tempApp.getNaviBtn_Singleton().getInstance().getNaviHeaderText().setText("My ¸ñ·Ï");
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().setBackgroundResource(R.drawable.top_button_back);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub.
				tempApp.ButtonBeepPlay();
				mBackBtnCheck=true;
				onBackPressed();
			}
		});
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftSubBtn().setVisibility(View.GONE);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightSubBtn().setVisibility(View.GONE);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().setVisibility(View.GONE);
		tempApp.getMainrbFirst().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//tempApp.ButtonBeepPlay();
				// TODO Auto-generated method stub
				if (tempApp.getMainTabGroup().getCheckedRadioButtonId() == R.id.first) {
					TabGroupActivity parentActivity = (TabGroupActivity)getParent();
					parentActivity.onHomeBackPressed(0);
					tempApp.getChannelTabGroup().check(R.id.TVCH_AllCH_TABBtn);
				}
			}
		});
		/*tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().setChecked(true);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					//getTabHost().setCurrentTab(4);
					Intent intent = new Intent(getParent(), TVCH_My.class);
					TabGroupActivity parentActivity = (TabGroupActivity)getParent();
					parentActivity.startChildActivity("TVCH_My", intent);  
				} else {
					if (!mBackBtnCheck) {
						onBackPressed();
					}
				}
			}
		});*/
	}


	private void setupTAB() {
		// TODO Auto-generated method stub

		mTabHost	=	(TabHost)findViewById(android.R.id.tabhost);

		mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("Firest_Tab").setContent(new Intent(this, TVCH_MY_Activity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("Second_Tab").setContent(new Intent(this, TVCH_Alarm_Activity.class)));

		rg = (RadioGroup) findViewById(R.id.TvMYCH_Radiogroup);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, final int checkedId) {
				switch (checkedId) {
				case R.id.TVCH_MYCH:
					rgSelect=0;
					getTabHost().setCurrentTab(0);
					break;
				case R.id.TVCH_MY_ALARM:
					rgSelect=1;
					getTabHost().setCurrentTab(1);
					break;
				}
			}
		});
		
		RadioButton myRadioButton =  (RadioButton)findViewById(R.id.TVCH_MYCH);
		myRadioButton.setOnClickListener(this);
		RadioButton myAlarmRadioButton =  (RadioButton)findViewById(R.id.TVCH_MY_ALARM);
		myAlarmRadioButton.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		tempApp.ButtonBeepPlay();
	}
}
