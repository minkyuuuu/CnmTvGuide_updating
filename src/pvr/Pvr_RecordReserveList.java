package pvr;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.RECORD_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.RECORD_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.RecordReserveList_PASER;
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
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;




public class Pvr_RecordReserveList extends ListActivity {

	private final static String DEBUG_TAG = "Pvr_RecordReserveList";

	private CNMApplication tempApp;
	private RecordReserveAsyncTask asyncTask;
	private HashMap<String, RECORD_DATA> tSeriesID = new HashMap<String, RECORD_DATA>();
	private HashMap<String, RECORD_DATA> tNowRecording = new HashMap<String, RECORD_DATA>();

	private RECORD_DATA_LIST OrigianlList = new RECORD_DATA_LIST();

	private final String SeriesItems_KEY = "SeriesItems_KEY";
	private final String SnippetItem_Key = "SnippetItem_Key";
	private final String ItemType_KEY	= "ItemType_KEY";
	private final String Reserve_Type	= "Reserve_Type";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tvch_all_channel);
		Log.d(DEBUG_TAG, ">>>>>>>onCreate()");

		tempApp = (CNMApplication)getApplication();
		tempApp.getMainrbFourth().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//tempApp.ButtonBeepPlay();
				if(tempApp.getmPVRTabGroup() != null)
					tempApp.getmPVRTabGroup().check(R.id.pvr_menu_reserve);
				runRecordReserveAsyncTask();
				onResume();
			}
		});

		runRecordReserveAsyncTask();
	}

	protected void runRecordReserveAsyncTask() {
		if (tempApp.GetNetworkInfo() != 2) {
			if(asyncTask == null)
				asyncTask = new RecordReserveAsyncTask();
			synchronized (asyncTask) {
				if (asyncTask.getStatus().equals(AsyncTask.Status.PENDING)) {
					asyncTask.execute(XML_Address_Define.Record.getGetrecordreservelist(tempApp.getMobileIMIE(this)));
				}else {
					if (!asyncTask.getStatus().equals(AsyncTask.Status.RUNNING) 
							&& !asyncTask.getStatus().equals(AsyncTask.Status.PENDING)
							&& asyncTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
						asyncTask = new RecordReserveAsyncTask();
						asyncTask.execute(XML_Address_Define.Record.getGetrecordreservelist(tempApp.getMobileIMIE(this)));
					}
				}
			}
		} else {
			AlertShow("Wifi Ȥ�� 3G���� ������� �ʾҰų� ��Ȱ���� �ʽ��ϴ�.��Ʈ��ũ Ȯ���� �ٽ� ������ �ּ���!");
		}
	}

	@Override
	protected void onResume() {

		Log.d(DEBUG_TAG, "============================ onResume()");
		StringBuffer stacktrace = new StringBuffer();
		StackTraceElement[] stackTrace = new Exception().getStackTrace();
		for(int x=0; x<stackTrace.length; x++)
		{
			stacktrace.append(stackTrace[x].toString() + "\n");
		}
		Log.d(DEBUG_TAG, stacktrace.toString());
		Log.d(DEBUG_TAG, "---------------------------- onResume()");

		super.onResume();
		/*		if(asyncTask == null)
			asyncTask = new RecordReserveAsyncTask();
		synchronized (asyncTask) {
			if (asyncTask.getStatus().equals(AsyncTask.Status.PENDING)) {
				asyncTask.execute(XML_Address_Define.Record.getGetrecordreservelist(tempApp.getMobileIMIE(this)));
			}else {
				if (!asyncTask.getStatus().equals(AsyncTask.Status.RUNNING) 
						&& !asyncTask.getStatus().equals(AsyncTask.Status.PENDING)
						&& asyncTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
					asyncTask = new RecordReserveAsyncTask();
					asyncTask.execute(XML_Address_Define.Record.getGetrecordreservelist(tempApp.getMobileIMIE(this)));
				}
			}
		}
		 */	}



	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);

		RECORD_DATA tItem = (RECORD_DATA)getListView().getItemAtPosition(position);
		TabGroupActivity parentActivity = (TabGroupActivity)getParent().getParent();

		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		RECORD_DATA_LIST tempList = new RECORD_DATA_LIST();

		if (tItem == null) {

		}else {
			if (!tItem.getRecordingType()) {  // �ø��� �ΰ��

				for (int i = 0; i < OrigianlList.size(); i++) {
					if (tItem.getSeriesId().equals(OrigianlList.get(i).getSeriesId()) && !OrigianlList.get(i).getRecordingType()) {
						tempList.add(OrigianlList.get(i));
						break;
					}
				}

				Log.d("Sangtae", "�ø���");
				bundle.putSerializable(SeriesItems_KEY, tempList);
				intent.setClass(getParent(), RecordSeriesList.class);
				intent.putExtra(SeriesItems_KEY, bundle);
				intent.putExtra(ItemType_KEY, Reserve_Type);
				parentActivity.startChildActivity("PVR_RecoredList", intent);
			} else {
				Log.d("Sangtae", "snippet");
				tempList.add(tItem);
				bundle.putSerializable(SnippetItem_Key, tempList);
				intent.setClass(getParent(), PVR_RecordSetting.class);
				intent.putExtra(SnippetItem_Key, bundle);
				intent.putExtra(ItemType_KEY, Reserve_Type);
				parentActivity.startChildActivity("PVR_RecordSetting", intent);
			}

		}

	}

	@Override
	public void onBackPressed() {

		//super.onBackPressed();
		NaviBtn_Singleton.getInstance().getNaviLeftBtn().performClick();
	}



	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	private void setupUI(RECORD_DATA_LIST aList) {

		synchronized (OrigianlList) {
			OrigianlList.clear();
			tSeriesID.clear();
			tNowRecording.clear();
			OrigianlList.addAll(aList);


			if (OrigianlList != null) {
				RECORD_DATA_LIST tDataList = new RECORD_DATA_LIST();
				tDataList.clear();
				// �ø��� ���� ��ȭ ���� �׸� �з�
				for (int i = OrigianlList.size()-1; i >= 0; i--) {	// �ø���� ���� ��ȭ ���� �׸� Ȯ��
					if(OrigianlList.get(i).getRecordingType() != null && OrigianlList.get(i).getRecordPaytype() != null )
						if (!OrigianlList.get(i).getRecordingType() && !OrigianlList.get(i).getRecordPaytype()) {
							tSeriesID.put(OrigianlList.get(i).getSeriesId(), OrigianlList.get(i));		// �ø��� ���� ��ȭ�� ���̵� ����
						}else if (OrigianlList.get(i).getRecordStatus() != null) {			// ��ȭ ���� ���α׷� �ִ� �� Ȯ��
							if (OrigianlList.get(i).getRecordStatus()){
								tNowRecording.put(OrigianlList.get(i).getScheduleId(), OrigianlList.get(i));
							}
						}
				}

				// ��ȭ ���� �׸� �߰�
				if (tNowRecording.size() == 0) {
					tDataList.add(null);
				} else {
					for (int i = OrigianlList.size()-1; i >= 0 ; i--) {
						if (tNowRecording.containsKey(OrigianlList.get(i).getScheduleId()) && !OrigianlList.get(i).getRecordStatus()) {
							OrigianlList.remove(i);		// ���� ��ȭ �ߺ��׸� ����
						}else if (tNowRecording.containsKey(OrigianlList.get(i).getScheduleId()) && OrigianlList.get(i).getRecordStatus()){						
							tDataList.add(OrigianlList.get(i));
						}
					}
				}


				// �ø�� ���� �׸��� �ִ� ���
				if (tSeriesID.size() == 0) {
					for (int i = OrigianlList.size()-1; i >= 0; i--) {
						tDataList.add(OrigianlList.get(i));
					}
				} else {
					for (int i = OrigianlList.size()-1; i >= 0; i--) {
						if (tSeriesID.containsKey(OrigianlList.get(i).getSeriesId()) && !OrigianlList.get(i).getRecordingType()) {
							tDataList.add(OrigianlList.get(i));
						}else if (!tSeriesID.containsKey(OrigianlList.get(i).getSeriesId()) && !tNowRecording.containsKey(OrigianlList.get(i).getScheduleId())) {
							tDataList.add(OrigianlList.get(i));
						}
					}
				}
				setListAdapter(new RecoredReserveAdapter(tDataList));
			} else {
				Toast.makeText(this, getResources().getString(R.string.NotFound), Toast.LENGTH_SHORT);
			}
		}
	}


	private class RecoredReserveAdapter extends BaseAdapter{
		private RECORD_DATA_LIST mItems;
		private LayoutInflater inflater;
		private TVCHImageDownloader imageDownloader;

		public RecoredReserveAdapter(RECORD_DATA_LIST aList) {
			mItems =  new RECORD_DATA_LIST();
			mItems.addAll(aList);
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

		@Override
		public View getView(int Position, View ContentView, ViewGroup arg2) {

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
				holder.PrgSubject.setSelected(true);
				holder.IconKey	= (ImageView)ContentView.findViewById(R.id.icon_key_list);
				holder.IconArrow = (ImageView)ContentView.findViewById(R.id.vod_ListIconArrow);

				ContentView.setTag(holder);
			} else {
				holder = (ViewHolder)ContentView.getTag();
				if (holder == null) {
					holder = new ViewHolder();
					ContentView =  inflater.inflate(R.layout.pvr_reserve_row, null);

					holder.PvrStatus	= (ImageView)ContentView.findViewById(R.id.pvr_icon_status);

					holder.IconNoSelect = (ImageView)ContentView.findViewById(R.id.icon_noselct);
					holder.PrgDay	= (TextView)ContentView.findViewById(R.id.pvr_PrgDay);
					holder.PrgPlayTime = (TextView)ContentView.findViewById(R.id.pvr_PlayTime);

					holder.chNumber	= (TextView)ContentView.findViewById(R.id.pvr_chNumber);

					holder.chLogo	= (ImageView)ContentView.findViewById(R.id.pvr_chLoge);
					holder.PrgSubject = (TextView)ContentView.findViewById(R.id.pvr_PrgSubject);
					holder.PrgSubject.setSelected(true);
					holder.IconKey	= (ImageView)ContentView.findViewById(R.id.icon_key_list);
					holder.IconArrow = (ImageView)ContentView.findViewById(R.id.vod_ListIconArrow);

					ContentView.setTag(holder);
				}
			}

			tItem = mItems.get(Position);

			/*holder.chNumber.setTypeface(Typeface.MONOSPACE, Typeface.NORMAL);
			holder.PrgSubject.setTypeface(Typeface.MONOSPACE, Typeface.NORMAL);
			holder.PrgSubject.setTextColor(Color.parseColor("#940167")); //PLUM Color
			 */			
			if (tItem == null) {
				ContentView =  inflater.inflate(R.layout.pvr_livebar_row, null);
				return ContentView;
			}else if (tItem.getRecordingType() && tItem.getRecordStatus()) {
				holder.PvrStatus.setVisibility(View.VISIBLE);
				holder.PrgDay.setVisibility(View.GONE);
				holder.PrgPlayTime.setVisibility(View.GONE);
				holder.PvrStatus.setBackgroundResource(R.drawable.tvch_icon_recording);
				holder.IconArrow.setVisibility(View.GONE);
				ContentView.setBackgroundResource(R.drawable.remocon_livebar);
			}else if (tItem.getRecordingType() && tItem.getRecordStartTime().length() > 1) {
				tRecordTime = BroadCateTime(tItem.getRecordStartTime());
				holder.PrgDay.setVisibility(View.VISIBLE);
				holder.PrgPlayTime.setVisibility(View.VISIBLE);
				holder.PrgDay.setText(tRecordTime[0]);
				holder.PrgPlayTime.setText(tRecordTime[1]);
				holder.PvrStatus.setVisibility(View.GONE);
				holder.IconArrow.setVisibility(View.VISIBLE);
				ContentView.setBackgroundResource(R.drawable.list_bg);
			}else {
				holder.PvrStatus.setVisibility(View.VISIBLE);
				holder.PrgDay.setVisibility(View.GONE);
				holder.PrgPlayTime.setVisibility(View.GONE);
				holder.IconArrow.setVisibility(View.VISIBLE);
				holder.PvrStatus.setBackgroundResource(R.drawable.icon_series);
				ContentView.setBackgroundResource(R.drawable.list_bg);
			}	


			holder.chNumber.setText(String.format("Ch %s", tItem.getChannelNo()));

			imageDownloader.download(tItem.getChannel_logo_img(), holder.chLogo);
			holder.PrgSubject.setText(tItem.getProgramName());


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
		ImageView		IconArrow;
	}





	// ����Ʈ ������
	class RecordReserveAsyncTask extends AsyncTask<String, Void, RECORD_DATA_LIST>
	{

		private ProgressDialog pro;
		@Override
		protected RECORD_DATA_LIST doInBackground(String... params) {


			Log.d(DEBUG_TAG, "## URL Request ��ȭ ���� ��� ��û: "+params[0]);
			RecordReserveList_PASER paser = new RecordReserveList_PASER(params[0]);

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

			}else {
				if (result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_100) || result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_205)) { 	// ����
					setupUI(result);
				} else if (result.getResultCode().equals(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_028) || result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_206)) {
					settopPowerOffAlertShow();
				} else {

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
		alert_internet_status.setTitle("�� ��");
		alert_internet_status.setMessage(msg);
		alert_internet_status.setPositiveButton("�� ��",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); // �ݱ�
				finish();
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
		alert_internet_status.setPositiveButton("Ȯ ��",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = alert_internet_status.create();
		alert.show();
	}

}
