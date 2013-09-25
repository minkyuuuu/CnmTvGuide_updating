package hdlnc.dev2.sangtae.cnm.settings;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.ADULT_AUTH_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.ADULT_AUTH_PARSER;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Setting_Adult_Auth extends Activity{

	private CNMApplication tempApp;
	private NaviBtn_Singleton mNaviBtnSingleton;
	
	private final static String DEBUG_TAG = "Setting_Adult_Auth";
	private String resultMessage="";
	private ADULT_AUTH_DATA authData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tempApp = (CNMApplication)getApplication();
		mNaviBtnSingleton 	=  tempApp.getNaviBtn_Singleton();
		setContentView(R.layout.setting_adult_auth);
		setUpNaviArea();
		SetupUI();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(nameEditText.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(idenEditText.getWindowToken(), 0);

	}


	EditText nameEditText;
	EditText idenEditText;
	RadioButton autoAuthBtn;
	private void SetupUI()
	{
		nameEditText = (EditText)findViewById(R.id.vod_auth_name_text);
		idenEditText = (EditText)findViewById(R.id.vod_auth_idenKey_text);
		autoAuthBtn = (RadioButton)findViewById(R.id.vod_auth_auto_radioBtn);
		autoAuthBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
				if(isChecked)
					tempApp.setAdultAutoAuth(true);
				else
					tempApp.setAdultAutoAuth(false);
				
				tempApp.commitAdultAutoAuth();
				
			}
		});
		
		

		idenEditText.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
				{
					InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					im.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
					
					if(tempApp.getAdultAuth())
						Toast.makeText(Setting_Adult_Auth.this,getResources().getString(R.string.adult_auth_warring02), Toast.LENGTH_SHORT).show();
					else
						new AdultAuthAsyncTask().execute(
								nameEditText.getText().toString(), idenEditText.getText().toString());
				}
				
				return false;
			}
		});
		
		
		if(tempApp.getAdultAutoAuth())
			autoAuthBtn.setChecked(true);
		
		
		if(tempApp.getAdultAuth())
		{
			nameEditText.setHint("인증완료");
			idenEditText.setHint("인증완료");
		}
		
		RelativeLayout radioBtnWrap = (RelativeLayout)findViewById(R.id.vod_radioBtn_warp);
		radioBtnWrap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				tempApp.ButtonBeepPlay();
				
				if(tempApp.getAdultAuth())
				{
					if(autoAuthBtn.isChecked())
						autoAuthBtn.setChecked(false);
					else
						autoAuthBtn.setChecked(true);
					
				}
				else
				{
					if(nameEditText.getText().toString().equals("") || idenEditText.getText().toString().equals(""))
						Toast.makeText(Setting_Adult_Auth.this, getResources().getString(R.string.adult_auth_warring01), Toast.LENGTH_SHORT).show();
					else
					{
						autoAuthBtn.setChecked(true);
						
						new AdultAuthAsyncTask().execute(
								nameEditText.getText().toString(), idenEditText.getText().toString());
					}
				}
				
			}
		});
	}
	
	private void setUpNaviArea() {
		
		Resources res = getResources();
		
		TextView naviHeader = (TextView)mNaviBtnSingleton.getNaviHeaderText();
		naviHeader.setText("성인인증");
		
		//Drawable drawableBack = res.getDrawable(R.drawable.navi_back_btn);
		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_back);//ImageDrawable(drawableBack);
		naviLeftBtn.setVisibility(View.VISIBLE);
		naviLeftBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				onBackPressed();
			}
		});
		ToggleButton naviLeftSubBtn = (ToggleButton)mNaviBtnSingleton.getNaviLeftSubBtn();
		naviLeftSubBtn.setVisibility(View.GONE);
		
		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		naviRightBtn.setVisibility(View.GONE);
		
		ImageButton naviRightSubBtn = (ImageButton)mNaviBtnSingleton.getNaviRightSubBtn();
		naviRightSubBtn.setVisibility(View.GONE);
	}
	
	class AdultAuthAsyncTask extends AsyncTask<String,Void,String>
	{
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
	    		
			//String entityNameValue = tempApp.URL_SETTING_ADULT_AUTH + params[1] +"@";
			try {
				
				//String UTF8Name =	URLEncoder.encode(params[0], "utf-8"); 
				String requestURL = XML_Address_Define.Authenticate.getAuthenticateadult(params[1], params[0]);
				Log.d(DEBUG_TAG, "### URL Request 성인인증: "+requestURL);
				ADULT_AUTH_PARSER PARSER = new ADULT_AUTH_PARSER(
						requestURL);
				
				PARSER.start();
				if(PARSER.getResultData() != null)
				{
					authData = PARSER.getResultData();
					return authData.getResult();
				}
				
			}catch (Exception e) {
				// TODO: handle exception
				Log.w(DEBUG_TAG, e);
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			Log.d(DEBUG_TAG, "resultCode: "+result+"   errString: "+authData.getResultCode());
			if ( result.contains("ok") )
				resultMessage = getAdultAuthMessage(authData.getResultCode());
			else
				resultMessage = "서버 접속을 실패했습니다. 잠시 후 다시 시도하여 주십시오.";
						
			Toast.makeText(getApplication(), resultMessage, 5000).show();

			nameEditText.setHint("인증완료");
			nameEditText.setText("");
			idenEditText.setHint("인증완료");
			idenEditText.setText("");
			/*
			new AlertDialog.Builder(getParent())
//			.setTitle(authData.getResultCode()) //DEBUG용 Title
			.setMessage(resultMessage)
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					if(tempApp.getAdultAuth())
					{
						nameEditText.setHint("인증완료");
						nameEditText.setText("");
						idenEditText.setHint("인증완료");
						idenEditText.setText("");
					}
				}
			})
			.show();
			*/
				
			nameEditText.setEnabled(true);
			idenEditText.setEnabled(true);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			nameEditText.setEnabled(false);
			idenEditText.setEnabled(false);
			
		}
		
	}
	
	private String getAdultAuthMessage(String codeName)
	{
		Resources res = getResources();
		
		tempApp.setAdultAuth(false);
		
		tempApp.setAdultAutoAuth(false);
		tempApp.commitAdultAutoAuth();
		
		if(codeName.equals("A"))
		{
			
			tempApp.setAdultAuth(true);
			if(autoAuthBtn.isChecked())
			{
				tempApp.setAdultAutoAuth(true);
				tempApp.commitAdultAutoAuth();
			}
			
			return "성인 인증이 완료되었습니다.";//res.getString(R.string.Adult_ERRCODE_A);
		}
		else 
		if(codeName.equals("B"))
			return res.getString(R.string.Adult_ERRCODE_B);
		else
		if(codeName.equals("C"))
			return res.getString(R.string.Adult_ERRCODE_C);
		else
		if(codeName.equals("D"))
			return res.getString(R.string.Adult_ERRCODE_D);
		else
		if(codeName.equals("E"))
			return res.getString(R.string.Adult_ERRCODE_E);
		else
		if(codeName.equals("F"))
			return res.getString(R.string.Adult_ERRCODE_F);
		else
		if(codeName.equals("Y"))
			return res.getString(R.string.Adult_ERRCODE_Y);
		else
		if(codeName.equals("G"))
			return res.getString(R.string.Adult_ERRCODE_G);
		else
		if(codeName.equals("H"))
			return res.getString(R.string.Adult_ERRCODE_H);
		else
		if(codeName.equals("Z"))
			return res.getString(R.string.Adult_ERRCODE_Z);
		else
			return res.getString(R.string.Adult_ERRCODE_X);
	}
	
}
