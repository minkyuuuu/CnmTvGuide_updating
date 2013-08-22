package hdlnc.dev2.sangtae.cnm.settings;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.Intent_KEY_Define;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.CHECK_RESPONSE_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.RESPONSE_DATA;
//import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_ZZIM_LIST;
import hdlnc.dev2.sangtae.cnm.joyn.JoyN;
import hdlnc.dev2.sangtae.cnm.main.main;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
//import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Setting extends ListActivity {

	private final static String DEBUG_TAG = "Setting";

	private CNMApplication tempApp;
	private RESPONSE_DATA mArrayList;
	private SettingAdapter setAdapter;
	private String uDid;
	String[] items={"초기화면설정",
			"지역/상품 설정",
			"설정 초기화",
			"셋탑 등록",
			"성인 인증",
			"이용 안내",
			"Joy & Life"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		tempApp = (CNMApplication)getApplication();	
		setContentView(R.layout.settings);
		setAdapter = new SettingAdapter(this);
		setListAdapter(setAdapter);

		ListView mListView = getListView();
		mListView.setDividerHeight(0);
		mListView.setSoundEffectsEnabled(false);
		mListView.setDivider(getResources().getDrawable(R.drawable.transparent));

		uDid =  tempApp.getMobileIMIE(Setting.this);

	}

	public void setupUI()
	{
		tempApp.loadPreferences();
		setAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {

		super.onResume();

		NaviBtn_Singleton mNaviBtnSingleton 	=  tempApp.getNaviBtn_Singleton();//(ImageButton)findViewById(R.id.Navi_LeftImgBtn);
		//Resources res = getResources();
		//Drawable drawableHome = res.getDrawable(R.drawable.navi_home_btn);
		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_home);//naviLeftBtn.setImageDrawable(drawableHome);
		naviLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Log.d("Navigation","Home button Click!! - JoyN");
				tempApp.ButtonBeepPlay();
				main tempActivity = (main)getParent().getParent().getParent();
				tempActivity.Home_View(0);
			}
		});
		ToggleButton naviLeftSubBtn = (ToggleButton)mNaviBtnSingleton.getNaviLeftSubBtn();
		naviLeftSubBtn.setVisibility(View.GONE);
		TextView txtHeader = (TextView)mNaviBtnSingleton.getNaviHeaderText();
		txtHeader.setText("안내/설정");
		ImageButton naviRightSubBtn = (ImageButton)mNaviBtnSingleton.getNaviRightSubBtn();
		naviRightSubBtn.setVisibility(View.GONE);
		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		naviRightBtn.setVisibility(View.GONE);

		tempApp.getMainrbFifth().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Setting.this.onResume();

				TabGroupActivity parentActivity = (TabGroupActivity)getParent().getParent();
				parentActivity.onHomeBackPressed(0);

			}
		});


		TabGroupActivity parentActivity = (TabGroupActivity)getParent().getParent();
		parentActivity.onHomeBackPressed(0);
		new SettopRegCheckAsyncTask().execute(null);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onWindowFocusChanged(boolean)
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			switch (tempApp.getGoToSetting()) {
			case Intent_KEY_Define.Set_Mode.MODE.SETTOP_JOIN:
				tempApp.setGoToSetting(Intent_KEY_Define.Set_Mode.MODE.NORMAL);
				onListItemClick(getListView(),getListView() , 3, 0);
				break;
			case Intent_KEY_Define.Set_Mode.MODE.AUDLT_JOIN:
				tempApp.setGoToSetting(Intent_KEY_Define.Set_Mode.MODE.NORMAL);
				onListItemClick(getListView(), getListView(), 4, 0);
				break;
			case Intent_KEY_Define.Set_Mode.MODE.GUIDE_INFO:
				tempApp.setGoToSetting(Intent_KEY_Define.Set_Mode.MODE.NORMAL);
				onListItemClick(getListView(), getListView(), 5, 0);
				break;
			default:
				break;
			}

		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);

		tempApp.ButtonBeepPlay();

		switch(position)
		{
		case 0:
			Intent subSetting0 = new Intent(getParent(), Setting_InitView.class);
			TabGroupActivity parentActivity0 = (TabGroupActivity)getParent().getParent();
			parentActivity0.startChildActivity("Setting_InitView", subSetting0);   
			break;
		case 1:
			Intent subSetting2 = new Intent(getParent(), Setting_LocationInfo.class);
			TabGroupActivity parentActivity2 = (TabGroupActivity)getParent().getParent();
			parentActivity2.startChildActivity("Setting_LocationInfo", subSetting2); 
			break;  			
		case 2:
			Intent subSetting4 = new Intent(getParent(), Setting_Refurbish.class);
			TabGroupActivity parentActivity4 = (TabGroupActivity)getParent().getParent();
			parentActivity4.startChildActivity("Setting_Refurbish", subSetting4); 
			break;	
		case 3:
			Intent subSetting5 = new Intent(getParent(), Setting_SettopRegi.class);
			TabGroupActivity parentActivity5 = (TabGroupActivity)getParent().getParent();
			parentActivity5.startChildActivity("Setting_SettopRegi", subSetting5); 
			break;
		case 4:
			Intent subSetting6 = new Intent(getParent(), Setting_Adult_Auth.class);
			TabGroupActivity parentActivity6 = (TabGroupActivity)getParent().getParent();
			parentActivity6.startChildActivity("Setting_AdultAuth", subSetting6); 
			break;	
		case 5:
			Intent subSetting7 = new Intent(getParent(), Setting_Help.class);
			TabGroupActivity parentActivity7 = (TabGroupActivity)getParent().getParent();
			parentActivity7.startChildActivity("Setting_Help", subSetting7); 
			break;				
		case 6:
			Intent subSetting3 = new Intent(getParent(), JoyN.class);
			TabGroupActivity parentActivity3 = (TabGroupActivity)getParent().getParent();
			parentActivity3.startChildActivity("Setting_ProductInfo", subSetting3); 
			break;
		default:
			break;
		}
	}

	class SettingAdapter extends ArrayAdapter {
		Activity context;

		SettingAdapter(Activity context) {
			super(context, R.layout.setting_row, items);
			this.context=context;
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater=context.getLayoutInflater();
			View row=inflater.inflate(R.layout.setting_row, null);
			TextView settingText=(TextView)row.findViewById(R.id.Setting_Item);
			TextView settingValue=(TextView)row.findViewById(R.id.Setting_Item_Value);
			TextView SubsettingValue=(TextView)row.findViewById(R.id.Setting_SubItem_Value);
			settingText.setText(items[position]);
			settingValue.setText("");
			SubsettingValue.setText("");
			SubsettingValue.setVisibility(View.GONE);
			switch(position)
			{
			case 0: //초기화면설정
				int initValue = tempApp.getInitViewSettingValue();
				if(initValue == 1)
				{
					settingValue.setText("Home 화면");
				} else if(initValue == 2)
				{
					settingValue.setText("VOD화면");
				} else if(initValue == 3)
				{
					settingValue.setText("TV채널화면");
				}
				break;

			case 1: //지역정보설정
				if (tempApp.getLocationName()!= null) {
					settingValue.setText(tempApp.getLocationName());
				} else {
					settingValue.setText("미지정");
				}
				if (tempApp.getProductName() != null) {
					SubsettingValue.setText(tempApp.getProductName());
				} else {
					SubsettingValue.setText("미지정");
				}
				SubsettingValue.setVisibility(View.VISIBLE);
				break;
			case 2: //설정초기화
				break;
			case 3: //셋탑등록
			{
				if(tempApp.getSettopRegAuth())
					settingValue.setText(tempApp.getSettopBoxType());
				else
					settingValue.setText("미등록");
				break;
			}
			case 4: //성인인증
				if(tempApp.getAdultAuth() || tempApp.getAdultAutoAuth())
					settingValue.setText("인증");
				else
					settingValue.setText("미인증");
				break;
			case 5: //joyN
				break;
			case 6: //이용안내
				break;
			default:	
				break;
			}
			return row;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().performClick();
	}


	class SettopRegCheckAsyncTask extends AsyncTask<Void, Void, RESPONSE_DATA>
	{

		ProgressDialog pro;
		@Override
		protected RESPONSE_DATA doInBackground(Void... params) {

			String requestURL = XML_Address_Define.Authenticate.getCheckregistuser(uDid);
			Log.d(DEBUG_TAG, "### URL Request 셋탑 등록여부 체크 : "+requestURL);

			CHECK_RESPONSE_PASER.start(requestURL);
			return CHECK_RESPONSE_PASER.getArrayList();
		}

		@Override
		protected void onPostExecute(RESPONSE_DATA result) {

			super.onPostExecute(result);

			try
			{
				mArrayList =result;
				if(mArrayList == null)
				{
					onCancelled();
				}
				else
					if(mArrayList.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_100))
					{
						tempApp.setSettopRegAuth(true);
						tempApp.setTVCH_SettigChecked(true);
					}
					else
					{
						tempApp.setSettopRegAuth(false);
						tempApp.setTVCH_SettigChecked(false);
					}

				tempApp.commitSettopReg(result.getSetTopBoxKind());

				setupUI();
			}
			catch (Exception e) {

			}

			pro.dismiss();
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			pro = ProgressDialog.show(getParent().getParent(), "", getResources().getString(R.string.vod_watting),true);
		}

		@Override
		protected void onCancelled() {

			super.onCancelled();
		}
	}
}

