package pvr;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.RECORD_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.RECORD_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.RecordList_PASER;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCHImageDownloader;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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

// 녹화물 목록
public class Pvr_RecordList extends ListActivity {

	private final static String DEBUG_TAG = "Pvr_RecordList";

	private CNMApplication tempApp;
	private RecordAsyncTask asyncTask;
	private HashMap<String, RECORD_DATA> tSeriesID = new HashMap<String, RECORD_DATA>();

	private RECORD_DATA_LIST OrigianlList = new RECORD_DATA_LIST();

	private final String SeriesItems_KEY = "SeriesItems_KEY";
	private final String SnippetItem_Key = "SnippetItem_Key";
	private final String ItemType_KEY	= "ItemType_KEY";
	private final String Record_Type	= "Record_Type";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tvch_all_channel);

		tempApp = (CNMApplication)getApplication();

	}

	@Override
	protected void onResume() {

		super.onResume();
		if(asyncTask == null){
			asyncTask = new RecordAsyncTask();
			asyncTask.execute(XML_Address_Define.Record.getGetrecordlist(tempApp.getMobileIMIE(this)));
		}else if (!asyncTask.getStatus().equals(AsyncTask.Status.PENDING)
				&& !asyncTask.getStatus().equals(AsyncTask.Status.RUNNING)
				&& !asyncTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
			asyncTask = new RecordAsyncTask();
			asyncTask.execute(XML_Address_Define.Record.getGetrecordlist(tempApp.getMobileIMIE(this)));
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn()
		.performClick();
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);

		RECORD_DATA tItem = (RECORD_DATA)getListView().getItemAtPosition(position);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		RECORD_DATA_LIST tempList = new RECORD_DATA_LIST();
		TabGroupActivity parentActivity = (TabGroupActivity)getParent().getParent();


		if (tItem == null) {

		}else {
			if (!tItem.getRecordingType()) {  // 시리즈 인경우

				for (int i = 0; i < OrigianlList.size(); i++) {
					if (tItem.getSeriesId().equals(OrigianlList.get(i).getSeriesId()) && !OrigianlList.get(i).getRecordingType()) {
						tempList.add(OrigianlList.get(i));
					}
				}

				Log.d("Sangtae", "시리즈");
				bundle.putSerializable(SeriesItems_KEY, tempList);
				intent.setClass(getParent(), RecordSeriesList.class);
				intent.putExtra(SeriesItems_KEY, bundle);
				intent.putExtra(ItemType_KEY, Record_Type);
				parentActivity.startChildActivity("PVR_RecoredList", intent);
			} else {
				Log.d("Sangtae", "snippet");
				tempList.add(tItem);
				bundle.putSerializable(SnippetItem_Key, tempList);
				intent.setClass(getParent(), PVR_RecordSetting.class);
				intent.putExtra(SnippetItem_Key, bundle);
				intent.putExtra(ItemType_KEY, Record_Type);
				parentActivity.startChildActivity("PVR_RecordSetting", intent);
			}

		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	private void setupUI(RECORD_DATA_LIST aList) {

		OrigianlList.clear();
		tSeriesID.clear();
		OrigianlList.addAll(aList);

		if (aList != null) {
			RECORD_DATA_LIST tDataList = new RECORD_DATA_LIST();
			RECORD_DATA_LIST tSeriesList = new RECORD_DATA_LIST();

			// 시리즈 물과 녹화 중인 항목 분류
			for (int i = aList.size()-1; i >= 0; i--) {
				if (!aList.get(i).getRecordingType() && !tSeriesID.containsKey(aList.get(i).getSeriesId())) {
					tSeriesList.add(aList.get(i));
					tSeriesID.put(aList.get(i).getSeriesId(), aList.get(i));
				}else if (aList.get(i).getRecordingType()) {
					tDataList.add(aList.get(i));
				}
				Log.d(DEBUG_TAG, "===========================" );
				Log.d(DEBUG_TAG, "RecordingType: "+aList.get(i).getRecordingType() );
				Log.d(DEBUG_TAG, "SeriesId: "+aList.get(i).getSeriesId() );
				Log.d(DEBUG_TAG, "ScheduleId: "+aList.get(i).getScheduleId() );
				Log.d(DEBUG_TAG, "ChannelId: "+aList.get(i).getChannelId() );
				Log.d(DEBUG_TAG, "ChannelNo: "+aList.get(i).getChannelNo() );
				Log.d(DEBUG_TAG, "ChannelName: "+aList.get(i).getChannelName() );
				Log.d(DEBUG_TAG, "Program_Grade: "+aList.get(i).getProgram_Grade() );
				Log.d(DEBUG_TAG, "ProgramName: "+aList.get(i).getProgramName() );
				Log.d(DEBUG_TAG, "RecordStartTime: "+aList.get(i).getRecordStartTime() );
				Log.d(DEBUG_TAG, "RecordEndTime: "+aList.get(i).getRecordEndTime() );
				Log.d(DEBUG_TAG, "RecordHD: "+aList.get(i).getRecordHD() );
				Log.d(DEBUG_TAG, "RecordPaytype: "+aList.get(i).getRecordPaytype() );
				Log.d(DEBUG_TAG, "RecordProtection: "+aList.get(i).getRecordProtection() );
				Log.d(DEBUG_TAG, "RecordStatus: "+aList.get(i).getRecordStatus() );
			}

			for (int i = tSeriesList.size()-1; i >= 0; i--) {
				tDataList.add(0, tSeriesList.get(i));
			}

			setListAdapter(new RecoredAdapter(tDataList));
		} else {
			Toast.makeText(this, getResources().getString(R.string.NotFound), Toast.LENGTH_SHORT);
		}
	}


	private class RecoredAdapter extends BaseAdapter{
		private RECORD_DATA_LIST mItems;
		private LayoutInflater inflater;
		private TVCHImageDownloader imageDownloader;
		public RecoredAdapter(RECORD_DATA_LIST aList) {
			mItems = aList;
			inflater =  LayoutInflater.from(getApplicationContext());
			imageDownloader = new TVCHImageDownloader(getApplicationContext());
		}
		@Override
		public int getCount() {

			return mItems.size();
		}

		@Override
		public Object getItem(int arg0) {

			return mItems.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return Long.parseLong(mItems.get(arg0).getRecordId());
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

		@Override
		public View getView(int Position, View ContentView, ViewGroup arg2) {

			ViewHolder holder;
			RECORD_DATA tItem;
			String[] tRecordTime = null;

			if (ContentView == null) {
				holder = new ViewHolder();
				ContentView =  inflater.inflate(R.layout.pvr_reserve_row, null);

				holder.PvrStatus	= (ImageView)ContentView.findViewById(R.id.pvr_icon_status);
				holder.PvrStatus.setBackgroundResource(R.drawable.icon_series);

				//holder.IconNoSelect = (ImageView)ContentView.findViewById(R.id.icon_noselct);
				holder.PrgDay	= (TextView)ContentView.findViewById(R.id.pvr_PrgDay);
				holder.PrgPlayTime = (TextView)ContentView.findViewById(R.id.pvr_PlayTime);

				holder.chNumber	= (TextView)ContentView.findViewById(R.id.pvr_chNumber);

				holder.chLogo	= (ImageView)ContentView.findViewById(R.id.pvr_chLoge);
				holder.PrgSubject = (TextView)ContentView.findViewById(R.id.pvr_PrgSubject);
				holder.PrgSubject.setSelected(true);
				holder.IconKey	= (ImageView)ContentView.findViewById(R.id.icon_key_list);
				holder.IconArrow = (ImageView)ContentView.findViewById(R.id.vod_ListIconArrow);
				holder.IconArrow.setVisibility(View.VISIBLE);
				ContentView.setTag(holder);
			} else {
				holder = (ViewHolder)ContentView.getTag();
			}

			tItem = mItems.get(Position);

			if (tItem.getRecordingType()) {
				tRecordTime = BroadCateTime(tItem.getRecordStartTime());
				holder.PrgDay.setVisibility(View.VISIBLE);
				holder.PrgPlayTime.setVisibility(View.VISIBLE);
				holder.PrgDay.setText(tRecordTime[0]);
				holder.PrgPlayTime.setText(tRecordTime[1]);
				holder.PvrStatus.setVisibility(View.GONE);
			}else {
				holder.PvrStatus.setVisibility(View.VISIBLE);
				holder.PrgDay.setVisibility(View.GONE);
				holder.PrgPlayTime.setVisibility(View.GONE);				
			}

			holder.chNumber.setText(String.format("Ch %s", tItem.getChannelNo()));

			imageDownloader.download(tItem.getChannel_logo_img(), holder.chLogo);
			holder.PrgSubject.setText(tItem.getProgramName());


			if (tItem.getRecordProtection()) {
				holder.IconKey.setVisibility(View.VISIBLE);
			} else {
				holder.IconKey.setVisibility(View.GONE);
			}

			return ContentView;
		}
	}

	private static class ViewHolder{

		ImageView		PvrStatus;

		//ImageView		IconNoSelect;
		TextView		PrgDay;
		TextView		chNumber;
		ImageView		chLogo;
		TextView 		PrgPlayTime;
		TextView 		PrgSubject;
		ImageView		IconKey;
		ImageView		IconArrow;
	}





	// 리스트 스레드
	class RecordAsyncTask extends AsyncTask<String, Void, RECORD_DATA_LIST>
	{

		private ProgressDialog pro;
		@Override
		protected RECORD_DATA_LIST doInBackground(String... params) {


			RecordList_PASER paser = new RecordList_PASER(params[0]);

			for (int i = 0; i < 2; i++) {
				if(paser.start()){
					return paser.getRecord_LIST();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(RECORD_DATA_LIST result) {

			super.onPostExecute(result);

			pro.dismiss();
			if (result == null) {
				AlertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
			}else {
				if (result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_100) || result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_205)) { 	// 성공
					setupUI(result);
				} else if (result.getResultCode().equals(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_028) || result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_206)) {
					settopPowerOffAlertShow();
				}else {
					AlertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");

				}
			}
		}

		@Override
		protected void onPreExecute() {

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
	
	private void settopPowerOffAlertShow() {
		LayoutInflater inflater = LayoutInflater.from(this);
		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
				getParent().getParent());
		View view = inflater.inflate(R.layout.remote_pop_layout, null);
		alert_internet_status.setView(view);
		alert_internet_status.setPositiveButton("확 인",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = alert_internet_status.create();
		alert.show();
	}
}
