package hdlnc.dev2.sangtae.cnm.settings;


import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.Intent_KEY_Define;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.CHECK_RESPONSE_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.RESPONSE_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.SETTOP_AUTH_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.SETTOP_REG_PARSER;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_ZZIM_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_ZZIM_PASER;
import hdlnc.dev2.sangtae.cnm.main.Main_MenuActivity;

import hdlnc.dev2.sangtae.cnm.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Setting_SettopRegi extends Activity {
	private CNMApplication app;
	private String uDid;
	private String authCode;
	
	private EditText authEditText;
	private VOD_ZZIM_LIST mArrayList;
	
	private final static String DEBUG_TAG = "Setting_SettopRegi";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_settopregi);
		
		app = (CNMApplication)getApplication();	
		uDid =  app.getMobileIMIE(Setting_SettopRegi.this);
		
	}

	
	private void setupUI(){
		
		
		authEditText = (EditText)findViewById(R.id.settopregiedit);
		
		authEditText.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				
				authCode = authEditText.getText().toString();
				
				if(event.getAction() == KeyEvent.ACTION_DOWN
						 && keyCode == KeyEvent.KEYCODE_ENTER)
				{
					if(!authCode.equals(""))
						new SettopRegAsyncTask().execute(authCode, uDid);
					else
						Toast.makeText(Setting_SettopRegi.this,getResources().getString(R.string.setting_settop_warring) , 
								Toast.LENGTH_SHORT).show();
				}
					
				return false;
			}
		});
		
		if(app.getSettopRegAuth())
			authEditText.setHint("등록완료");
		
		ImageButton regBtn = (ImageButton)findViewById(R.id.settopregibtn);
		regBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				app.ButtonBeepPlay();
				authCode = authEditText.getText().toString();
				
				if(!authCode.equals(""))
					new SettopRegAsyncTask().execute(authCode, uDid);
				else
					Toast.makeText(Setting_SettopRegi.this,getResources().getString(R.string.setting_settop_warring) , 
							Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		NaviBtn_Singleton mNaviBtnSingleton 	=  app.getNaviBtn_Singleton();//(ImageButton)findViewById(R.id.Navi_LeftImgBtn);
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
		txtHeader.setText("셋탑등록");
		ImageButton naviRightSubBtn = (ImageButton)mNaviBtnSingleton.getNaviRightSubBtn();
		naviRightSubBtn.setVisibility(View.GONE);
		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		naviRightBtn.setVisibility(View.GONE);
		
		setupUI();

	}
	
	@Override
	public void onPause()
	{
		super.onPause();

		InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(authEditText.getApplicationWindowToken(), 0);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
	class SettopRegAsyncTask extends AsyncTask<String, Void, RESPONSE_DATA>
	{

		private ProgressDialog pro;
		
		@Override
		protected RESPONSE_DATA doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			/*	String regUrl = app.URL_SETTING_SETTOP_REG;
				String authCode = params[0];
				String udid = params[1];
				regUrl += "authCode=" + authCode + "&udid=" + udid;
				Log.d(DEBUG_TAG, regUrl);
			*/
			
			String requestURL = XML_Address_Define.Authenticate.getClientsettopboxregist(params[1], params[0]);
			Log.d(DEBUG_TAG, "### URL Request 셋탑 등록 : "+requestURL);


			if(CHECK_RESPONSE_PASER.start(requestURL))
				return CHECK_RESPONSE_PASER.getArrayList();

			return null;
		}

		@Override
		protected void onPostExecute(RESPONSE_DATA result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			pro.dismiss();
			if(result ==null)
			{
				onCancelled();
			}
			else
			if(result !=null && result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_100))
			{
				app.setSettopRegAuth(true);
				app.commitSettopReg(result.getSetTopBoxKind());
				
				new AlertDialog.Builder(getParent())
				.setMessage(getResources().getString(R.string.setting_settop_regComplete))
				.setNegativeButton("확 인", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						app.setTVCH_SettigChecked(true);
						onBackPressed();
					}
				})
				.show();
			}
			else
			{
				Toast.makeText(Setting_SettopRegi.this,
						getResources().getString(R.string.setting_settop_regError), Toast.LENGTH_SHORT).show();
				
				/*
				new AlertDialog.Builder(getParent())
				.setTitle(result.getResult())
				.setMessage(result.getErrString())
				.setNegativeButton("확 인", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						onBackPressed();
					}
				})
				.show();
				*/
				
				app.setSettopRegAuth(false);
				app.setTVCH_SettigChecked(false);
				app.commitSettopReg("NULL");
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			pro = ProgressDialog.show(getParent(), "", 
					getResources().getString(R.string.setting_settop_regWatting));
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			AlertShow(getResources().getString(R.string.system_network_warring));
		}
		
	}
	
	private void AlertShow(String msg) {
		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
				getParent().getParent());
		alert_internet_status.setTitle("경 고");
		alert_internet_status.setMessage(msg);
		alert_internet_status.setPositiveButton("닫 기",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); // 닫기
				finish();
			}
		});
		AlertDialog alert = alert_internet_status.create();
		alert.show();
	}
	
}


