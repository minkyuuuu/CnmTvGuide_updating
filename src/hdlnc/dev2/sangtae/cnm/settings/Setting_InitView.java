package hdlnc.dev2.sangtae.cnm.settings;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;

import hdlnc.dev2.sangtae.cnm.R;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Setting_InitView extends Activity implements OnClickListener{
	private CNMApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_init_view);
		setupUI();
	}
	
	private RelativeLayout menuLayout;
	private RelativeLayout vodLayout; 
	private RelativeLayout tvLayout;
	
	private RadioButton rdoMenu;
	private RadioButton rdoVod;
	private RadioButton rdoTv;

	private void setupUI(){
		app = (CNMApplication)getApplication();	
		int initValue = app.getInitViewSettingValue();
		
		menuLayout = (RelativeLayout)findViewById(R.id.Radio_Menu_image);
		vodLayout = (RelativeLayout)findViewById(R.id.Radio_VOD_image);
		tvLayout = (RelativeLayout)findViewById(R.id.Radio_TVChannel_image);
		
		rdoMenu = (RadioButton)findViewById(R.id.Radio_Menu);
		rdoTv = (RadioButton)findViewById(R.id.Radio_TVChannel);
		rdoVod = (RadioButton)findViewById(R.id.Radio_VOD_Button);
		
		rdoVod.setOnClickListener(Setting_InitView.this);
		rdoMenu.setOnClickListener(Setting_InitView.this);
		rdoTv.setOnClickListener(Setting_InitView.this);
		
		
		
		menuLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				app.ButtonBeepPlay();
				homeSelected();
				
			}
		});
		vodLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				app.ButtonBeepPlay();
				vodSelected();
				
			}
		});
		tvLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				app.ButtonBeepPlay();
				tvSelected();
			}
		});
		
		switch(initValue)
		{
		case 1:
		{
			homeSelected();
			break;
		}
		case 2:
		{
			vodSelected();
			break;
		}
		case 3:
		{
			tvSelected();
			break;
		}
		}
		
		
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		NaviBtn_Singleton mNaviBtnSingleton 	=  app.getNaviBtn_Singleton();//(ImageButton)findViewById(R.id.Navi_LeftImgBtn);
		Resources res = getResources();
		//Drawable drawableBack = res.getDrawable(R.drawable.navi_back_btn);
		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_back);//naviLeftBtn.setImageDrawable(drawableBack);
		naviLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Log.d("Navigation","Home button Click!! - JoyN");
				app.ButtonBeepPlay();
				onBackPressed();
			}
		});
		
		ToggleButton naviLeftSubBtn = (ToggleButton)mNaviBtnSingleton.getNaviLeftSubBtn();
		naviLeftSubBtn.setVisibility(View.GONE);
		TextView txtHeader = (TextView)mNaviBtnSingleton.getNaviHeaderText();
		txtHeader.setText("초기화면설정");
		ImageButton naviRightSubBtn = (ImageButton)mNaviBtnSingleton.getNaviRightSubBtn();
		naviRightSubBtn.setVisibility(View.GONE);
		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		naviRightBtn.setVisibility(View.GONE);
	}
	
	
	private void vodSelected()
	{
		
		rdoVod.setChecked(true);
		rdoTv.setChecked(false);
		rdoMenu.setChecked(false);
		
		app.setInitViewSettingValue(2);
		app.commitInitViewSettingValue();
		
		menuLayout.setBackgroundColor(Color.TRANSPARENT);
		vodLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.set_selectbg));
		tvLayout.setBackgroundColor(Color.TRANSPARENT);
	}
	
	private void tvSelected()
	{
		
		rdoMenu.setChecked(false);
		rdoVod.setChecked(false);
		rdoTv.setChecked(true);
		
		app.setInitViewSettingValue(3);
		app.commitInitViewSettingValue();
		
		menuLayout.setBackgroundColor(Color.TRANSPARENT);
		vodLayout.setBackgroundColor(Color.TRANSPARENT);
		tvLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.set_selectbg));
	}
	
	private void homeSelected()
	{
		
		rdoMenu.setChecked(true);
		rdoTv.setChecked(false);
		rdoVod.setChecked(false);
		
		app.setInitViewSettingValue(1);
		app.commitInitViewSettingValue();
		
		menuLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.set_selectbg));
		tvLayout.setBackgroundColor(Color.TRANSPARENT);
		vodLayout.setBackgroundColor(Color.TRANSPARENT);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		app.ButtonBeepPlay();
		
		int id = v.getId();
		
		switch (id) {
		case R.id.Radio_VOD_Button:
		{
			vodSelected();
			break;
		}
		case R.id.Radio_TVChannel:
		{
			tvSelected();
			break;
		}
		case R.id.Radio_Menu:
		{
			homeSelected();
			break;
		}
		default:
			break;
		}
	}

}
