package hdlnc.dev2.sangtae.cnm.main;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.CHECK_RESPONSE_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.LOCATION_INFO_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.LOCATION_INFO_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.PRODUCT_INFO_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.RESPONSE_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.SERVICE_NOTICE_INFO;
import hdlnc.dev2.sangtae.cnm.vod.VOD_Service;

import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class Main_MenuActivity extends Activity {
	public static final int RESULT_EXIT = 10001;
	public static final int RESULT_SELECT = 10002;
	public static final String RESULT_SELECT_KEY = "RESULT_SELECT_KEY";

	public static final String DEBUG_TAG = "Main_MenuActivity";

	private CNMApplication cnmAPP;
	private LOCATION_INFO_PASER location_PASER;
	private PRODUCT_INFO_PASER product_PASER;

	//private AnimationDrawable bgAnimation;
	private ImageView bgImage;

	private ImageButton		mTVChannelBtn;
	private ImageButton		mVODBtn;
	private ImageButton		mRemoteBtn;
	private ImageButton		mPVRBtn;
	private ImageButton		mSettingsBtn;
	private OnClickListener		mButtonListener;
	private int tempIndex=0;
	private Boolean mFastBoot=false;

	private String Udid;
	private RESPONSE_DATA mArrayList;

	private boolean m_clickFlag =false;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("Sangtae", "Main Manu onCreate");
		setContentView(R.layout.main_menuactivity);
		
		Log.i("hwang", "Main_MenuActivity - onCreate()");

		// overridePendingTransition(R.anim.fade, R.anim.hold);										// activty 전화 에니메이션
		// 하단에서 지연 발생 작업들 시작
		//bgImage = (ImageView) findViewById(R.id.Main_MenuImgView);
		//bgImage.setBackgroundResource(R.anim.main_menu_ani_bg);
		cnmAPP = (CNMApplication)getApplication();						// 공용 데이터 접근용
		mContext = this;
		Udid = cnmAPP.getMobileIMIE(Main_MenuActivity.this);
		//AnimationUtils.loadAnimation(this.getApplicationContext(), R.drawable.main_menu_ani_bg)
		//bgAnimation = (AnimationDrawable) bgImage.getBackground();
		
		int noticeLocation = getIntent().getIntExtra("NoticeLocation", 0);
		
		SERVICE_NOTICE_INFO serviceNotice = null;
		serviceNotice = cnmAPP.getServiceNoticeInfo(noticeLocation);
		if (serviceNotice != null)
			showServiceNotice(serviceNotice);
		
		mButtonListener	= new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent	tempIntent = new Intent(getApplicationContext(), main.class);
				if(m_clickFlag == false)
					cnmAPP.ButtonBeepPlay();

				m_clickFlag =false;

				Intent data = new Intent();
				SERVICE_NOTICE_INFO serviceNotice = null;
				if (v == mTVChannelBtn) {
					//cnmAPP.getMainTabGroup().check(R.id.first);
					serviceNotice = cnmAPP.getServiceNoticeInfo(2);
					if (serviceNotice == null)
						data.putExtra(RESULT_SELECT_KEY, R.id.first);
					//tempIntent.setAction(TV_CHANNEL_ACTION);
					//startActivity(tempIntent);
				} else if(v == mVODBtn) {
					//cnmAPP.getMainTabGroup().check(R.id.second);
					serviceNotice = cnmAPP.getServiceNoticeInfo(3);
					if (serviceNotice == null)
						data.putExtra(RESULT_SELECT_KEY, R.id.second);
					//tempIntent.setAction(VOD_ACTION);
					//startActivity(tempIntent);
				}else if (v == mRemoteBtn) {
					//cnmAPP.getMainTabGroup().check(R.id.third);
					serviceNotice = cnmAPP.getServiceNoticeInfo(4);
					if (serviceNotice == null)
						data.putExtra(RESULT_SELECT_KEY, R.id.third);
					//tempIntent.setAction(JOY_N_ACTION);
					//startActivity(tempIntent);
				}else if (v == mPVRBtn) {
					//cnmAPP.getMainTabGroup().check(R.id.fourth);
					serviceNotice = cnmAPP.getServiceNoticeInfo(5);
					if (serviceNotice == null)
						data.putExtra(RESULT_SELECT_KEY, R.id.fourth);
					//tempIntent.setAction(SETTINGS_ACTION);
					//startActivity(tempIntent);
				}else if (v == mSettingsBtn) {
					//cnmAPP.getMainTabGroup().check(R.id.fourth);
					serviceNotice = cnmAPP.getServiceNoticeInfo(6);
					if (serviceNotice == null)
						data.putExtra(RESULT_SELECT_KEY, R.id.fifth);
					//tempIntent.setAction(SETTINGS_ACTION);
					//startActivity(tempIntent);
				}
				
				if (serviceNotice == null) {
					setResult(RESULT_SELECT, data);
					onBackPressed();					
				}
				else {
					showServiceNotice(serviceNotice);
				}
/*				String tempAreaCode = cnmAPP.getLocationID();

				if (tempAreaCode.equals(XML_Address_Define.Channel.LIMITED_AREA_CODE.KANGNAM) || tempAreaCode.equals(XML_Address_Define.Channel.LIMITED_AREA_CODE.UlSAN)) {
					if (v == mVODBtn || v == mRemoteBtn || v == mPVRBtn) {
						CNM_WarningDialog dialog =  new CNM_WarningDialog(mContext);
						dialog.setWarningDialogType(CNM_WarningDialog.Limited_Area);
						dialog.show();
					}else{
						setResult(RESULT_SELECT, data);
						onBackPressed();
					}
				}else{
					setResult(RESULT_SELECT, data);
					onBackPressed();
				}
*/
			}
		};

		mTVChannelBtn	=	(ImageButton)findViewById(R.id.Menu_Tvch_ImgBtn);
		mVODBtn	=	(ImageButton)findViewById(R.id.Menu_VOD_ImgBtn);
		mRemoteBtn	=	(ImageButton)findViewById(R.id.Menu_Remocon_ImgBtn);
		mPVRBtn	=	(ImageButton)findViewById(R.id.Menu_PVR_ImgBtn);
		mSettingsBtn	=	(ImageButton)findViewById(R.id.Menu_Settings_ImgBtn);

		mTVChannelBtn.setOnClickListener(mButtonListener);
		mVODBtn.setOnClickListener(mButtonListener);
		mRemoteBtn.setOnClickListener(mButtonListener);
		mPVRBtn.setOnClickListener(mButtonListener);
		mSettingsBtn.setOnClickListener(mButtonListener);
	}

	void showServiceNotice(SERVICE_NOTICE_INFO serviceNotice) {
		TextView title = new TextView(mContext);
		title.setBackgroundColor(Color.TRANSPARENT);
		title.setPadding(10, 10, 10,10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.WHITE);
		title.setTextSize(20);
		title.setText(serviceNotice.getTitle());					

		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(mContext);
		alert_internet_status.setCustomTitle(title);
		alert_internet_status.setMessage(serviceNotice.getContent());
		alert_internet_status.setPositiveButton("닫 기",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); // 닫기
				//finish();
			}
		});
		AlertDialog alert = alert_internet_status.create();
		alert.show();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("Sangtae", "Main Manu OnResume");
		/*if (mFastBoot && cnmAPP.getInitViewSettingValue() != 1) {
			finish();
		}*/
		mFastBoot=true;
		VOD_Service.initTabHost();
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			//bgAnimation.start();
			// 처음 실행인지 확인한다.

			if (!cnmAPP.getFirstLoding()) {													// 첫 실행인지 확인한다. 
				new SettopRegCheckAsyncTask().execute(null);  //셋탑 등록여부 확인 
				Location_dalog();  
			}
		} else {
			//bgAnimation.stop();
		}


	}



	private void Product_dalog() {
		// TODO Auto-generated method stub

		// 상품 정보 파서

		if (cnmAPP.GetNetworkInfo() != 2) {
			Log.d("Sangtae", "areaCode: "+ cnmAPP.getLocationID());
			
			String requestURL = XML_Address_Define.Channel.getGetchannelproduct(cnmAPP.getLocationID());
			Log.d(DEBUG_TAG, "### URL Request 상품정보요청: " + requestURL);
			product_PASER = new PRODUCT_INFO_PASER(requestURL);
			product_PASER.start();
			/*product_PASER.getProduct_INFO_LIST();
			String[] tempList = new String[product_PASER.getProduct_INFO_LIST().size()];
			for (int i = 0; i < product_PASER.getProduct_INFO_LIST().size(); i++) {
				tempList[i]=product_PASER.getProduct_INFO_LIST().get(i).getName();
			}*/
			tempIndex =product_PASER.getProduct_INFO_LIST().getDefaultIndx();
			
			final ArrayAdapter arrayAdapter = setProductAdapter();	// 2013-06-28
			
			Builder puc_dialog = new AlertDialog.Builder(this).setTitle(R.string.product_dlg_title)
			.setNeutralButton("확인", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Log.d("Sangtae", "loc_dialog onClick"+ which);
					cnmAPP.ButtonBeepPlay();
					cnmAPP.setProductID(product_PASER.getProduct_INFO_LIST().get(tempIndex).getID());
					cnmAPP.setProductName(product_PASER.getProduct_INFO_LIST().get(tempIndex).getName());
					cnmAPP.savePreferences(true);
					dialog.dismiss();
				}
			})
			.setSingleChoiceItems(arrayAdapter, tempIndex, new DialogInterface.OnClickListener() {	// 2013-06-28

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					/*cnmAPP.setLocationID(product_PASER.getProduct_INFO_LIST().get(which).getID());
				cnmAPP.setLocationName(product_PASER.getProduct_INFO_LIST().get(which).getName());*/
					tempIndex=which;
					arrayAdapter.notifyDataSetChanged();	// 2013-06-28
				}
			});
			puc_dialog.create().show();
		} else {
			AlertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
		}

	}

	private void Location_dalog() {
		// TODO Auto-generated method stub
		// 지역정보 피서
		if (cnmAPP.GetNetworkInfo() != 2) {
			location_PASER = new LOCATION_INFO_PASER();
			location_PASER.start();
			//location_PASER.getLocation_info();
			String[] tempList = new String[location_PASER.getLocation_info().size()];
			for (int i = 0; i < location_PASER.getLocation_info().size(); i++) {
				tempList[i]=location_PASER.getLocation_info().get(i).getName();
			}
			tempIndex=0;
			
			final ArrayAdapter arrayAdapter = setLocationAdapter();	// 2013-06-28

			Builder loc_dialog = new AlertDialog.Builder(this).setTitle(R.string.location_dlg_title)
			.setSingleChoiceItems(arrayAdapter, 0,new  DialogInterface.OnClickListener() {	// 2013-06-28

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cnmAPP.ButtonBeepPlay();
					Log.d("Sangtae", "loc_dialog onClick"+ which);
					tempIndex=which;
					arrayAdapter.notifyDataSetChanged();	// 2013-06-28
				}}

			).setNeutralButton("확인", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cnmAPP.ButtonBeepPlay();
					cnmAPP.setLocationID(location_PASER.getLocation_info().get(tempIndex).getID());
					cnmAPP.setLocationName(location_PASER.getLocation_info().get(tempIndex).getName());
					cnmAPP.setLocationName2(location_PASER.getLocation_info().get(tempIndex).getName2());
					cnmAPP.savePreferences(true);
					
					dialog.dismiss();
					Product_dalog();

				}
			});
			
			
			loc_dialog.create().show();
		}else {
			AlertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
		}
	}

	public class CustomLocAdapter extends ArrayAdapter<String> {
		private Vector items;
		private Activity activity;
		private int textViewResourceId;
		public CustomLocAdapter(Context context, int textViewResourceId,Vector items, Activity activity) {
			super(context, textViewResourceId, items);
			this.items = items;
			this.activity = activity;
			this.textViewResourceId = textViewResourceId;

		}
		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			cnmAPP.ButtonBeepPlay();
			return super.getItem(position);
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(textViewResourceId, null);
			}
			return searchRow(position, v) ;
		}
		public View searchRow(int position, View v){
			Custom p = (Custom) items.get(position);
			if (p != null) {
				TextView text1 = (TextView) v.findViewById(R.id.text1);
				TextView text2 = (TextView) v.findViewById(R.id.text2);
				text1.setText(p.getText1());
				text2.setText(p.getText2());
				RadioButton radioButton = (RadioButton)v.findViewById(R.id.Pud_RBtn);
				if (tempIndex == position) {
					radioButton.setChecked(true);
				}else {
					radioButton.setChecked(false);
				}

			}
			return v;
		}
	}

	private CustomLocAdapter setLocationAdapter(){

		Vector<Custom> vector = new Vector<Custom>();
		LOCATION_INFO_LIST info_LIST= location_PASER.getLocation_info();
		for (int i = 0; i < info_LIST.size(); i++) {
			//tempList[i]=location_PASER.getLocation_info().get(i).getName();
			vector.add(new Custom(info_LIST.get(i).getName(),info_LIST.get(i).getName2()));
		}


		return new CustomLocAdapter(this, R.layout.location_list_row,vector, this);
	}



	public class Custom {
		private String Text1, Text2;
		public Custom(String _Text1, String _Text2) {
			this.Text1 = _Text1;
			this.Text2 = _Text2;
		}
		public Custom(String _Text1) {
			this.Text1 = _Text1;
		}
		public String getText1() {
			return Text1;
		}
		public String getText2() {
			return Text2;
		}
	}


	// Product
	public class CustomProAdapter extends ArrayAdapter<String> {
		private Vector items;
		private Activity activity;
		private int textViewResourceId;
		public CustomProAdapter(Context context, int textViewResourceId,Vector items, Activity activity) {
			super(context, textViewResourceId, items);
			this.items = items;
			this.activity = activity;
			this.textViewResourceId = textViewResourceId;

		}

		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			cnmAPP.ButtonBeepPlay();
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(textViewResourceId, null);
			}
			return searchRow(position, v) ;
		}
		public View searchRow(int position, View v){
			Custom p = (Custom) items.get(position);
			if (p != null) {
				TextView text1 = (TextView) v.findViewById(R.id.Pct_Name);
				text1.setText(p.getText1());
				RadioButton radioButton = (RadioButton)v.findViewById(R.id.Pud_RBtn);
				if (tempIndex == position) {
					radioButton.setChecked(true);
				}else {
					radioButton.setChecked(false);
				}

			}
			return v;
		}
	}

	private CustomProAdapter setProductAdapter(){

		Vector<Custom> vector = new Vector<Custom>();
		//LOCATION_INFO_LIST info_LIST= location_PASER.getLocation_info();

		//product

		product_PASER = new PRODUCT_INFO_PASER(XML_Address_Define.Channel.getGetchannelproduct(cnmAPP.getLocationID()));
		product_PASER.start();
		product_PASER.getProduct_INFO_LIST();
		String[] tempList = new String[product_PASER.getProduct_INFO_LIST().size()];
		for (int i = 0; i < product_PASER.getProduct_INFO_LIST().size(); i++) {
			tempList[i]=product_PASER.getProduct_INFO_LIST().get(i).getName();
		}
		tempIndex =product_PASER.getProduct_INFO_LIST().getDefaultIndx();

		for (int i = 0; i < tempList.length; i++) {
			//tempList[i]=location_PASER.getLocation_info().get(i).getName();
			vector.add(new Custom(tempList[i], null));
		}



		return new CustomProAdapter(this, R.layout.product_row,vector, this);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		super.onKeyDown(keyCode, event);

		if(event.getAction() == KeyEvent.ACTION_DOWN 
				&& keyCode == KeyEvent.KEYCODE_BACK)
		{
			cnmAPP.requestKillProcess(Main_MenuActivity.this);

			if(!cnmAPP.getAdultAutoAuth())
			{
				cnmAPP.setAdultAuth(false);
			}
		}
		return true;
	}
	private void AlertShow(String msg) {
		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
				this);
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

	class SettopRegCheckAsyncTask extends AsyncTask<Void, Void, RESPONSE_DATA>
	{

		ProgressDialog pro;
		@Override
		protected RESPONSE_DATA doInBackground(Void... params) {
			// TODO Auto-generated method stub

			String requestURL = XML_Address_Define.Authenticate.getCheckregistuser(Udid);

			Log.d(DEBUG_TAG, "### URL Request 셋탑 등록 체크: "+requestURL);
			//CHECK_RESPONSE_PASER PARSER = new CHECK_RESPONSE_PASER(paserUrl);
			CHECK_RESPONSE_PASER.start(requestURL);
			return CHECK_RESPONSE_PASER.getArrayList();
		}

		@Override
		protected void onPostExecute(RESPONSE_DATA result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try
			{
				mArrayList =result;
				if(mArrayList.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_100))
				{
					cnmAPP.setSettopRegAuth(true);
				}
				else
				{
					cnmAPP.setSettopRegAuth(false);
				}

				cnmAPP.commitSettopReg(result.getSetTopBoxKind());
			}
			catch (Exception e) {
				// TODO: handle exception
			}

			pro.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pro = ProgressDialog.show(Main_MenuActivity.this, "", getResources().getString(R.string.vod_watting),true);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("씨앤앰을 종료 하시겠습니까?")
			.setCancelable(false)
			.setPositiveButton("예", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					appExit();
					//onBackPressed();
					//				finish(); 
				}
			})
			.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
			break;

		default:
			break;
		}

		//return super.onKeyUp(keyCode, event);
		return false;
	}

	private void appExit() {
		// TODO Auto-generated method stub
		this.setResult(RESULT_EXIT);
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
