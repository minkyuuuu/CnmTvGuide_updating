package pvr;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.RECORD_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.RECORD_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.RecordList_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.RecordReserveList_PASER;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCHImageDownloader;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import pvr.Pvr_RecordReserveList.RecordReserveAsyncTask;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordSeriesList extends ListActivity {

	private final String SeriesItems_KEY = "SeriesItems_KEY";
	private final String SnippetItem_Key = "SnippetItem_Key";
	private final String ItemType_KEY	= "ItemType_KEY";
	private final String Reserve_Type	= "Reserve_Type";
	private final String Record_Type	= "Record_Type";

	private CNMApplication tempApp;

	private RecordReserveAsyncTask asyncTask;

	private String tempSeriesId;
	private String tempMode;

	private static Boolean mFirstType = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tvch_all_channel);
		tempApp = (CNMApplication)getApplication();
		setupNaviBar();

		Bundle bundle = getIntent().getBundleExtra(SeriesItems_KEY);


		//getListView().setAdapter(new RecoredReserveAdapter((RECORD_DATA_LIST)bundle.getSerializable(SeriesItems_KEY)));

		tempMode = getIntent().getStringExtra(ItemType_KEY);
		tempSeriesId = ((RECORD_DATA_LIST)bundle.getSerializable(SeriesItems_KEY)).get(0).getSeriesId(); 

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String TempURL;
		if (tempMode.equals(Reserve_Type)) {
			TempURL = XML_Address_Define.Record.getGetrecordreservelist(tempApp.getMobileIMIE(this));
		}else{
			TempURL = XML_Address_Define.Record.getGetrecordlist(tempApp.getMobileIMIE(this));
		}
		
		tempApp.getMainrbFourth().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tempApp.getmPVRTabGroup().check(R.id.pvr_menu_reserve);
				finish();
			}
		});


		if (asyncTask == null) {
			asyncTask = new RecordReserveAsyncTask();
			asyncTask.execute(TempURL);
		}else {
			if (!asyncTask.getStatus().equals(AsyncTask.Status.RUNNING) 
					&& !asyncTask.getStatus().equals(AsyncTask.Status.PENDING)
					&& !asyncTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
				asyncTask = new RecordReserveAsyncTask();
				asyncTask.execute(TempURL);
			}
		}
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		RECORD_DATA tItem = (RECORD_DATA)getListView().getItemAtPosition(position);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		RECORD_DATA_LIST tempList = new RECORD_DATA_LIST();
		TabGroupActivity parentActivity = (TabGroupActivity)getParent();
		if (tItem == null) {

		}else {
			Log.d("Sangtae", "snippet");
			tempList.add(tItem);
			mFirstType = true;
			bundle.putSerializable(SnippetItem_Key, tempList);
			intent.setClass(getParent(), PVR_RecordSetting.class);
			intent.putExtra(SnippetItem_Key, bundle);
			intent.putExtra(ItemType_KEY, getIntent().getStringExtra(ItemType_KEY));
			parentActivity.startChildActivity("PVR_RecordSetting", intent);

		}

	}

	private void setupNaviBar() {
		// TODO Auto-generated method stub
		tempApp.getNaviBtn_Singleton().getInstance().getNaviHeaderText().setText("시리즈 녹화예약 관리");
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().setBackgroundResource(R.drawable.top_button_back);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//tempApp.setChannelTabID(R.id.TVCH_AllCH_TABBtn);
				/*rgID = R.id.pvr_menu_reserve;
				main tempActivity = (main)getParent().getParent();
				tempActivity.Home_View();*/
				onBackPressed();
			}
		});
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftSubBtn().setVisibility(View.GONE);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().setVisibility(View.GONE);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightSubBtn().setVisibility(View.GONE);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		tempApp.ButtonBeepPlay();
		mFirstType =  false;
	}

	private void setupUI(RECORD_DATA_LIST aList) {
		// TODO Auto-generated method stub

		if (aList != null) {
			RECORD_DATA_LIST tDataList = new RECORD_DATA_LIST();
			tDataList.clear();
			if (tempMode.equals(Reserve_Type)) {
				Log.d("Sangtae", "Recored Type");
				for (int i = 0; i < aList.size(); i++) {
					if (tempSeriesId.equals(aList.get(i).getSeriesId()) && aList.get(i).getRecordingType()) {
						tDataList.add(aList.get(i));
					}
				}
				if (tDataList.size() <= 0 && mFirstType) {
					mFirstType = false;
					finish();
				}

			}else {
				Log.d("Sangtae", "Reserver Type");
				for (int i = 0; i < aList.size(); i++) {
					if (tempSeriesId.equals(aList.get(i).getSeriesId())) {
						tDataList.add(aList.get(i));
					}
				}
				
				if (tDataList.size() <= 0 && mFirstType) {
					mFirstType = false;
					finish();
				}
			}

			setListAdapter(new RecoredReserveAdapter(tDataList));
		} else {
			Toast.makeText(this, getResources().getString(R.string.NotFound), Toast.LENGTH_SHORT);
		}
	}

	private class RecoredReserveAdapter extends BaseAdapter{
		private RECORD_DATA_LIST mItems;
		private LayoutInflater inflater;
		private TVCHImageDownloader imageDownloader;

		public RecoredReserveAdapter(RECORD_DATA_LIST aList) {
			// TODO Auto-generated constructor stub
			//mItems = aList;
			mItems = new RECORD_DATA_LIST();
			if (aList.size() > 0) {
				mItems.add(aList.get(0));
				for (int i = 1; i < aList.size(); i++) {
					for (int j = 0; j < mItems.size(); j++) {
						if (LongTime(mItems.get(j).getRecordStartTime()) > LongTime(aList.get(i).getRecordStartTime())) {
							if (j == 0) {
								mItems.add(0, aList.get(i));
							} else {
								mItems.add(j-1, aList.get(i));
							}							
							break;
						} else {
							mItems.add(aList.get(i));
							break;
						}
					}
				}
			}else {
				mItems =  new RECORD_DATA_LIST();
			}
			
			inflater =  LayoutInflater.from(getApplicationContext());
			imageDownloader = new TVCHImageDownloader(getApplicationContext());
		}
		
		
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mItems.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mItems.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		private String[] BroadCateTime(String aTime){
			String[] TempStrArray = {"", ""};
			SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
			SimpleDateFormat DayTime = new SimpleDateFormat("MM-dd (E)");
			SimpleDateFormat Time = new SimpleDateFormat("HH:mm");

			ParsePosition pos = new ParsePosition(0);
			Date frmTime = mOriTime.parse(aTime, pos);
			TempStrArray[0] = DayTime.format(frmTime);
			TempStrArray[1] = Time.format(frmTime);
			return TempStrArray;
		}
		
		private long LongTime(String aTime){
			SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);

			ParsePosition pos = new ParsePosition(0);
			Date frmTime = mOriTime.parse(aTime, pos);
			return frmTime.getTime();
		}

		@Override
		public View getView(int Position, View ContentView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			RECORD_DATA tItem;
			String[] tRecordTime = null;


			if (ContentView == null) {
				holder = new ViewHolder();
				ContentView =  inflater.inflate(R.layout.pvr_reserve_row, null);

				holder.PvrStatus	= (ImageView)ContentView.findViewById(R.id.pvr_icon_status);

				holder.IconNoSelect = (ImageView)ContentView.findViewById(R.id.icon_noselct);
				holder.PrgDay	= (TextView)ContentView.findViewById(R.id.pvr_PrgDay);
				holder.PrgPlayTime = (TextView)ContentView.findViewById(R.id.pvr_PlayTime);

				holder.chNumber	= (TextView)ContentView.findViewById(R.id.pvr_chNumber);

				holder.chLogo	= (ImageView)ContentView.findViewById(R.id.pvr_chLoge);
				holder.PrgSubject = (TextView)ContentView.findViewById(R.id.pvr_PrgSubject);
				holder.PrgSubject.setMaxWidth(270);
				holder.PrgSubject.setSelected(true);
				holder.IconKey	= (ImageView)ContentView.findViewById(R.id.icon_key_list);

				ContentView.setTag(holder);
			} else {
				holder = (ViewHolder)ContentView.getTag();
			}



			tItem = mItems.get(Position);
			if (tItem.getRecordStartTime().length() > 1) {
				tRecordTime = BroadCateTime(tItem.getRecordStartTime());
				holder.PrgDay.setVisibility(View.VISIBLE);
				holder.PrgPlayTime.setVisibility(View.VISIBLE);
				holder.PrgDay.setText(tRecordTime[0]);
				holder.PrgPlayTime.setText(tRecordTime[1]);
				holder.PvrStatus.setVisibility(View.GONE);
			}	

			holder.chNumber.setText(String.format("Ch %s", tItem.getChannelNo()));

			imageDownloader.download(tItem.getChannel_logo_img(), holder.chLogo);
			holder.PrgSubject.setText(tItem.getProgramName());

			if (tItem.getRecordProtection() != null) {
				if (tItem.getRecordProtection() && !tempMode.equals(Reserve_Type)) {
					holder.IconKey.setVisibility(View.VISIBLE);
				} else {
					holder.IconKey.setVisibility(View.GONE);
				}
			}

			/*if (tItem.getProgram_Grade() == null) {
				holder.IconKey.setVisibility(View.GONE);
			} else {
				holder.IconKey.setVisibility(View.VISIBLE);
				if (tItem.getProgram_Grade().startsWith(Class7)) {
					holder.IconKey.setBackgroundResource(R.drawable.icon_class_7);
				}else if (tItem.getProgram_Grade().startsWith(Class12)) {
					holder.IconKey.setBackgroundResource(R.drawable.icon_class_12);
				}else if (tItem.getProgram_Grade().startsWith(Class15)) {
					holder.IconKey.setBackgroundResource(R.drawable.icon_class_15);
				}else if (tItem.getProgram_Grade().startsWith(Class19)) {
					holder.IconKey.setBackgroundResource(R.drawable.icon_class_19);
				}else if (tItem.getProgram_Grade().startsWith(ClassALL)) {
					holder.IconKey.setBackgroundResource(R.drawable.icon_class_all);
				}

			}*/

			return ContentView;
		}
	}

	private static class ViewHolder{
		ImageView		PvrStatus;

		ImageView		IconNoSelect;
		TextView		PrgDay;
		TextView		chNumber;
		ImageView		chLogo;
		TextView 		PrgPlayTime;
		TextView 		PrgSubject;
		ImageView		IconKey;
	}

	// 리스트 스레드
	class RecordReserveAsyncTask extends AsyncTask<String, Void, RECORD_DATA_LIST>
	{

		private ProgressDialog pro;
		String seriesId;
		@Override
		protected RECORD_DATA_LIST doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (tempMode.equals(Reserve_Type)) {
				Log.d("Sangtae", "Recored Type");
				RecordReserveList_PASER paser = new RecordReserveList_PASER(params[0]);
				for (int i = 0; i < 2; i++) {
					if(paser.start()){
						return paser.getRecord_LIST();
					}
				}

			}else {
				RecordList_PASER paser = new RecordList_PASER(params[0]);
				for (int i = 0; i < 2; i++) {
					if(paser.start()){
						return paser.getRecord_LIST();
					}
				}
			}


			return null;
		}

		@Override
		protected void onPostExecute(RECORD_DATA_LIST result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			pro.dismiss();
			if (result == null) {
				AlertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
			}else {
				if (result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_100) || result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_205)) { 	// 성공
					setupUI(result);
				} else {
					AlertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
				}
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pro = ProgressDialog.show(getParent().getParent(), "", getResources().getString(R.string.tv_watting),true);
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
				//finish();
			}
		});
		AlertDialog alert = alert_internet_status.create();
		alert.show();
	}
}
