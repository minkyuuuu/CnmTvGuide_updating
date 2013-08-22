package hdlnc.dev2.sangtae.cnm.settings;

import static hdlnc.dev2.sangtae.cnm.global.CNM_Alarm_Service.CNM_AlarmDelete_Service_ACTION;
import static hdlnc.dev2.sangtae.cnm.global.CNM_Alarm_Service.CNM_Alarm_Service_ACTION;
import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.CHECK_RESPONSE_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.RESPONSE_DATA;
//import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_ZZIM_LIST;
//import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_ZZIM_PASER;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
//import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Setting_Refurbish extends ListActivity {
	private CNMApplication app;
	private RefurbishAdapter refurbishAd;

	private static final String DEBUG_TAG = "Setting_Refurbish";

	String[] items={"My채널",
			"My프로그램",
			"예약알람"
			,"찜 목록"
			,"셋탑 등록"};
	// Create an anonymous implementation of OnClickListener
	private OnClickListener mBtnListener0 = null;
	private OnClickListener mBtnListener1 = null;
	private OnClickListener mBtnListener2 = null;
	private OnClickListener mBtnListener3 = null;

	private String Udid;
	//private VOD_ZZIM_LIST mArrayList;

	//private AlertDialog.Builder ad;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		app = (CNMApplication)getApplication();
		Udid = app.getMobileIMIE(Setting_Refurbish.this);
		setContentView(R.layout.setting_refurbish);
	}

	@Override
	public void onBackPressed() {

		app.ButtonBeepPlay();
		super.onBackPressed();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {

		super.onResume();
		NaviBtn_Singleton mNaviBtnSingleton 	=  app.getNaviBtn_Singleton();
		//Resources res = getResources();
		//Drawable drawableBack = res.getDrawable(R.drawable.navi_back_btn);
		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_back);//naviLeftBtn.setImageDrawable(drawableBack);
		naviLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				onBackPressed();
			}
		});

		ToggleButton naviLeftSubBtn = (ToggleButton)mNaviBtnSingleton.getNaviLeftSubBtn();
		naviLeftSubBtn.setVisibility(View.GONE);
		TextView txtHeader = (TextView)mNaviBtnSingleton.getNaviHeaderText();
		txtHeader.setText("설정 초기화");
		ImageButton naviRightSubBtn = (ImageButton)mNaviBtnSingleton.getNaviRightSubBtn();
		naviRightSubBtn.setVisibility(View.GONE);
		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		naviRightBtn.setVisibility(View.GONE);
		app.loadPreferences();

		setupUI();

	}



	private void setupUI(){

		refurbishAd = new RefurbishAdapter(this);
		setListAdapter(refurbishAd);
		refurbishAd.notifyDataSetChanged();
	}



	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);
		Log.d("Sangtae", "onListItemClick: "+v.getId());
	}



	class RefurbishAdapter extends ArrayAdapter {
		Activity context;

		RefurbishAdapter(Activity context) {
			super(context, R.layout.setting_refurbish_row, items);

			this.context=context;
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater=context.getLayoutInflater();
			View row=inflater.inflate(R.layout.setting_refurbish_row, null);
			TextView settRefText=(TextView)row.findViewById(R.id.Setting_Refurbish_Item);
			//ImageButton settingValue=(TextView)row.findViewById(R.id.Setting_Item_Value);
			ImageButton imgBtn = (ImageButton)row.findViewById(R.id.Setting_Refurbish_ImgBtn);
			imgBtn.setSoundEffectsEnabled(false);
			switch(position)
			{
			case 0:
				//if(mBtnListener0 == null){
				mBtnListener0 = new OnClickListener() {    
					public void onClick(View v) {      
						app.ButtonBeepPlay();
						AlertDialog.Builder ad = new AlertDialog.Builder(getParent());
						ad.setTitle("주 의");
						ad.setMessage("My채널을 초기화 하시겠습니까?");
						ad.setPositiveButton("초기화", new DialogInterface.OnClickListener()  {
							public void onClick(DialogInterface dialog, int which) {

								app.deleteAllTasks();
								Toast.makeText(getParent(), "My채널 초기화 완료",Toast.LENGTH_LONG).show();
							}
						});
						ad.setNegativeButton("취소", null);
						ad.show();
					}
				};
				imgBtn.setOnClickListener(mBtnListener0);
				//}
				break;
			case 1:
				//if(mBtnListener1 == null){
				mBtnListener1 = new OnClickListener() {    
					public void onClick(View v) {    
						app.ButtonBeepPlay();
						AlertDialog.Builder ad = new AlertDialog.Builder(getParent());
						ad.setTitle("주 의");
						ad.setMessage("My프로그램을 초기화  하시겠습니까?");
						ad.setPositiveButton("초기화", new DialogInterface.OnClickListener()  {
							public void onClick(DialogInterface dialog, int which) {

								app.deleteAllPRTasks();
								Toast.makeText(getParent(), "My프로그램 초기화 완료",Toast.LENGTH_LONG).show();
							}
						});
						ad.setNegativeButton("취소", null);
						ad.show();

					}
				};
				imgBtn.setOnClickListener(mBtnListener1);
				//}
				break;
			case 2:
				//if(mBtnListener2 == null){
				mBtnListener2 = new OnClickListener() {    
					public void onClick(View v) {     
						app.ButtonBeepPlay();
						AlertDialog.Builder ad = new AlertDialog.Builder(getParent());
						ad.setTitle("주 의");
						ad.setMessage("예약알람을 초기화 하시겠습니까?");
						ad.setPositiveButton("초기화", new DialogInterface.OnClickListener()  {
							public void onClick(DialogInterface dialog, int which) {
								
								deleteAlarmManager();
								
								app.deleteAllAlarm();
								
								Toast.makeText(getParent(), "예약알림 초기화 완료",Toast.LENGTH_LONG).show();
							}
						});
						ad.setNegativeButton("취소", null);
						ad.show();
					}
				};
				imgBtn.setOnClickListener(mBtnListener2);
				break;
			case 3:
				if(mBtnListener3 == null){
					mBtnListener3 = new OnClickListener() {    
						public void onClick(View v) {      
							app.ButtonBeepPlay();
							AlertDialog.Builder ad = new AlertDialog.Builder(getParent());
							ad.setTitle("주 의");
							ad.setMessage("찜 목록을 초기화 하시겠습니까?");
							ad.setPositiveButton("초기화", new DialogInterface.OnClickListener()  {
								public void onClick(DialogInterface dialog, int which) {

									new ZzimAsyncTask().execute(XML_Address_Define.Vod.getDeleteallmyvod(Udid));

								}
							});
							ad.setNegativeButton("취소", null);
							ad.show();
						}
					};
					imgBtn.setOnClickListener(mBtnListener3);
				}
				break;
			case 4:
				imgBtn.setOnClickListener(new OnClickListener() {    
					public void onClick(View v) {      
						app.ButtonBeepPlay();
						AlertDialog.Builder ad = new AlertDialog.Builder(getParent());
						ad.setTitle("주 의");
						ad.setMessage("셋탑 등록 설정을 초기화 하시겠습니까?");
						ad.setPositiveButton("초기화", new DialogInterface.OnClickListener()  {
							public void onClick(DialogInterface dialog, int which) {

								new SettopAsyncTask().execute(XML_Address_Define.Vod.getSetmyvod(Udid, "", "", XML_Address_Define.Vod.VOD_MY_MODE_USER_AND_CONTENT_ALL_DELETE));

							}
						});
						ad.setNegativeButton("취소", null);
						ad.show();
					}
				});
				break;
			default:

			}
			settRefText.setText(items[position]);
			return row;
		}
	}


	class SettopAsyncTask extends AsyncTask<String, Void, RESPONSE_DATA>
	{

		ProgressDialog pro;
		@Override
		protected RESPONSE_DATA doInBackground(String... params) {

			/*String paserUrl = app.URL_SETTING_SETTOP_REFUBLESH + Udid + "&AllDelete=Y";
			Log.d(DEBUG_TAG, "paserUrl : " + paserUrl);*/

			String requestURL = params[0];
			Log.d(DEBUG_TAG, "### URL Request 셋탑 등록 초기화: "+requestURL);
		
			CHECK_RESPONSE_PASER.start(requestURL);
			return CHECK_RESPONSE_PASER.getArrayList();
		}

		@Override
		protected void onPostExecute(RESPONSE_DATA result) {

			super.onPostExecute(result);

			try
			{
				if(result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_100))
				{
					app.setSettopRegAuth(false);
					app.commitSettopReg("NULL");
					Toast.makeText(getParent(), "셋탑 등록 초기화 완료",Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(getParent(), "셋탑 등록 초기화 실패 Error Code:"+result.getResultCode()+"Error_String: "+result.getErrorString(),Toast.LENGTH_LONG).show();
				}
			}
			catch (Exception e) {

				Log.w(DEBUG_TAG, e);
			}

			pro.dismiss();
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			pro = ProgressDialog.show(getParent(), "", getResources().getString(R.string.vod_watting),true);
		}
	}


	class ZzimAsyncTask extends AsyncTask<String, Void, RESPONSE_DATA>
	{

		ProgressDialog pro;
		@Override
		protected RESPONSE_DATA doInBackground(String... params) {

			/*String paserUrl = app.URL_ZZIM_DELET + Udid;
			Log.d(DEBUG_TAG, "paserUrl : " + paserUrl);
*/
			String requestURL = params[0];
			Log.d(DEBUG_TAG, "### URL Request 찜 등록 초기화: "+requestURL);

			CHECK_RESPONSE_PASER.start(requestURL);
			return CHECK_RESPONSE_PASER.getArrayList();
		}

		@Override
		protected void onPostExecute(RESPONSE_DATA result) {

			super.onPostExecute(result);

			try
			{
				if ( result != null
						&& (result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_100)
								|| result.getResultCode().equals("ok") )
						) {
					//					app.deleteAllZzim();
					Toast.makeText(getParent(), "찜 목록 초기화 완료",Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(getParent(), "찜 목록 초기화 실패",Toast.LENGTH_LONG).show();
				}
			}
			catch (Exception e) {

				Log.w(DEBUG_TAG, e);
			}

			pro.dismiss();
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			pro = ProgressDialog.show(getParent(), "", getResources().getString(R.string.vod_watting),true);

		}

		@Override
		protected void onCancelled() {

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

	void deleteAlarmManager(){
		AlarmManager mAlarmManager;
		
		mAlarmManager =  (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		
		Intent intent =  new Intent(CNM_Alarm_Service_ACTION);
		Intent intent2=new Intent(CNM_AlarmDelete_Service_ACTION);
		for(int i=0;i<app.getTVAl_CheckArr().size();i++)
		{			
			
			int seq=Integer.parseInt(app.getTVAl_CheckArr().get(i).getDB_SEQ());
			
			Log.d("delete",""+seq);
			mAlarmManager.cancel(PendingIntent.getBroadcast(getApplicationContext(),seq , intent, PendingIntent.FLAG_UPDATE_CURRENT));
			mAlarmManager.cancel(PendingIntent.getBroadcast(getApplicationContext(), seq, intent2, PendingIntent.FLAG_UPDATE_CURRENT));
		}
	}
	
}
