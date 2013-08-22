package hdlnc.dev2.sangtae.cnm.tvchannel;


import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.CNM_SearchUpDialog;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.CNM_SearchUpDialog.OnEnterKeyListener;
import hdlnc.dev2.sangtae.cnm.main.main;


import hdlnc.dev2.sangtae.cnm.R;
import android.R.style;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.ToggleButton;

public class TV_Channel extends TabActivity implements OnEnterKeyListener{
	private static TabHost	mTabHost;
	private static int mCurrentTab=0;
	private RadioGroup rg;
	private ToggleButton mGenretoggleBtn;
	private CNMApplication tempApp;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tv_channel);
		tempApp = (CNMApplication)getApplication();
		//tempApp.setChannelTabID(R.id.TVCH_AllCH_TABBtn);
		setupUI();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setupNaviBar();
		rg.check(tempApp.getChannelTabID());
		//getTabHost().setCurrentTab(mCurrentTab);
	}
	
	private void setupNaviBar() {
		// TODO Auto-generated method stub
		tempApp.getNaviBtn_Singleton().getInstance().getNaviHeaderText().setText("TV 채널");
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftSubBtn().setVisibility(View.GONE);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().setBackgroundResource(R.drawable.top_button_home);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				tempApp.setChannelTabID(R.id.TVCH_AllCH_TABBtn);
				main tempActivity = (main)getParent().getParent();
				tempActivity.Home_View(0);
			}
		});
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().setBackgroundResource(R.drawable.top_button_my);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().setVisibility(View.VISIBLE);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightSubBtn().setVisibility(View.VISIBLE);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				Intent intent = new Intent(getParent(), TVCH_My.class);
				TabGroupActivity parentActivity = (TabGroupActivity)getParent();
				parentActivity.startChildActivity("TVCH_My", intent);  

			}
		});

		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightSubBtn().setBackgroundResource(R.drawable.top_button_search);//ImageDrawable(drawableSrch);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightSubBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				new CNM_SearchUpDialog(getParent(), style.Theme_Translucent_NoTitleBar, 
						"프로그램 명으로 검색", TV_Channel.this).show();
			}
		});

	}

	@Override
	public void onEnterKeyPressed(String str) {
		// TODO Auto-generated method stub
		Intent searchResultIntent = new Intent(this, TVCH_SearchResult_List.class);
		TabGroupActivity parent = (TabGroupActivity)getParent(); 
		searchResultIntent.putExtra("KeyWord", str);
		parent.startChildActivity("TVCH_SearchResult_List", searchResultIntent);
	}



	private void setupUI() {
		mTabHost	=	(TabHost)findViewById(android.R.id.tabhost);

		mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("Firest_Tab").setContent(new Intent(this, TVCH_All.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("Second_Tab").setContent(new Intent(this, TVCH_Genre.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("Third_Tab").setContent(new Intent(this, TVCH_HD.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("Fourth_Tab").setContent(new Intent(this, TVCH_Paid.class)));
		//mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("Fifth_Tab").setContent(new Intent(this, TVCH_My.class)));

		rg = (RadioGroup) findViewById(R.id.TvCH_Radiogroup);

		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, final int checkedId) {
				tempApp.setChannelTabID(checkedId);
				switch (checkedId) {
				case R.id.TVCH_AllCH_TABBtn:
					getTabHost().setCurrentTab(0);
					mCurrentTab=0;
					break;
				case R.id.TVCH_Genre_TABBtn:
					getTabHost().setCurrentTab(1);
					mCurrentTab=1;
					break;
				case R.id.TVCH_HDCH_TABBtn:
					getTabHost().setCurrentTab(2);
					mCurrentTab=2;
					break;
				case R.id.TVCH_PaidCH_TABBtn:
					getTabHost().setCurrentTab(3);
					mCurrentTab=3;
					break;
				}
			}
		});

		tempApp.setChannelTabGroup(rg);
		//mGenretoggleBtn = tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftSubBtn();

		// mGenretoggleBtn =
		// tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftSubBtn();
		setRadioBtClick();
	}

	void setRadioBtClick() {
		RadioButton rb1 = (RadioButton) findViewById(R.id.TVCH_AllCH_TABBtn);
		RadioButton rb2 = (RadioButton) findViewById(R.id.TVCH_Genre_TABBtn);
		RadioButton rb3 = (RadioButton) findViewById(R.id.TVCH_HDCH_TABBtn);
		RadioButton rb4 = (RadioButton) findViewById(R.id.TVCH_PaidCH_TABBtn);

		OnTouchListener listener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					Log.d("TEST","onTouch");
					tempApp.ButtonBeepPlay();
				}
				return false;
			}
		};
		rb1.setOnTouchListener(listener);
		rb2.setOnTouchListener(listener);
		rb3.setOnTouchListener(listener);
		rb4.setOnTouchListener(listener);

	}

}
