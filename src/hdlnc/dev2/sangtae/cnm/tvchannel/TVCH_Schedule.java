package hdlnc.dev2.sangtae.cnm.tvchannel;

import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_HD;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_LOG_PATH;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_NAME;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_NUMBER;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_SELECT;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_MY_Activity.TVCH_ITEM_MY_TAB;
import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.Intent_KEY_Define;
import hdlnc.dev2.sangtae.cnm.global.TVCH_Alarm_CheckList;
import hdlnc.dev2.sangtae.cnm.global.TVCH_PR_CheckList;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.RecordReserveList_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.SCUEDULE_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.SCUEDULE_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.SCUEDULE_PASER;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
public class TVCH_Schedule extends Activity {

	private final static String DEBUG_TAG = "TVCH_Schedule";

	private CNMApplication tempApp;
	private String Udid;

	//private Boolean		mBackBtnCheck=false;
	private LinearLayout  mScudule_layout;
	private static SCUEDULE_DATA_LIST mList;
	private HashMap<String, TVCH_PR_CheckList> tempTVPr_Check;
	private HashMap<String, TVCH_Alarm_CheckList> TVAl_CheckMap;
	//private TVCHImageDownloader imageDownloader;
	private WorkspaceView work;


	public static final String TVCH_SCHEDULE_ID 		= "TVCH_SCHEDULE_ID";
	public static final String TVCH_SCHEDULE_TITLE 		= "TVCH_SCHEDULE_TITLE";
	public static final String TVCH_SCHEDULE_TIME		= "TVCH_SCHEDULE_TIME";
	public static final String TVCH_SCHEDULE_GRADE		= "TVCH_SCHEDULE_GRADE";
	public static final String TVCH_SCHEDULE_ALARM_TIME	= "TVCH_SCHEDULE_ALARM_TIME";
	public static final String TVCH_SCHEDULE_HD			= "TVCH_SCHEDULE_HD";
	public static final String TVCH_SERIES_ID			= "TVCH_SERIES_ID";
	public static final String TVCH_CHANNEL_ID 		= "TVCH_CHANNEL_ID";
	public static final String TVCH_PROGRAM_KEY			= "TVCH_PROGRAM_KEY";

	// 타이틀 정보
	private String TVCH_CpyNumber;
	private String TVCH_ChannelID;
	private String TVCH_LogURL;
	private String TVCH_CpyName;
	private slideView mSlideView;
	private Boolean mChannelRecording;
	private int mActiveFeature;
	private Handler WarningHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			mSlideView.setPostion(mActiveFeature);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tvch_schedule);

		/*if (savedInstanceState != null) {
			mActiveFeature = savedInstanceState.getInt("ActiveFeature", 0);
			String ActiveFeature = savedInstanceState.getString("ActiveFeatureS");
		}else {
			mActiveFeature = 0;
		}*/
		//imageDownloader = new TVCHImageDownloader();
		tempApp = (CNMApplication)getApplication();
		Udid = tempApp.getMobileIMIE(this);
		mList = new SCUEDULE_DATA_LIST();
		//tempApp.setTV_SCUEDULE_LIST(null);

		tempTVPr_Check = tempApp.getTVPr_CheckMap();
		TVAl_CheckMap	= tempApp.getTVAl_CheckMap();

		mScudule_layout = (LinearLayout)findViewById(R.id.tvch_Schedu_layout);
		TextView	mCh_num	= (TextView)findViewById(R.id.Channel_Number);
		ImageView	mCp_Log	= (ImageView)findViewById(R.id.Company_Log);
		TextView	mCh_name= (TextView)findViewById(R.id.Copany);

		TVCH_CpyNumber = "Ch "+ getIntent().getStringExtra(TVCH_ITEM_NUMBER);
		TVCH_ChannelID = getIntent().getStringExtra(TVCH_ITEM_SELECT);
		TVCH_CpyName   = getIntent().getStringExtra(TVCH_ITEM_NAME);
		TVCH_LogURL	   = getIntent().getStringExtra(TVCH_ITEM_LOG_PATH);

		mChannelRecording = getIntent().getBooleanExtra(Intent_KEY_Define.Recording.ChannelRecoddingMode, false);

		mCh_num.setText(TVCH_CpyNumber);
		mCh_name.setText(TVCH_CpyName);
		mCh_num.setSelected(true);
		mCh_name.setSelected(true);
		mSlideView = (slideView)findViewById(R.id.CusttomScroll);

		if (TVCH_LogURL != null) {
			TVCHImageDownloader imageDownloader = new TVCHImageDownloader(getApplicationContext());
			imageDownloader.setContext(getApplicationContext());
			imageDownloader.download(TVCH_LogURL, mCp_Log);//, BitmapFactory.decodeResource(getResources(), R.drawable.noimg_logo));
		}
		work = new WorkspaceView(this, null);

		work.setTouchSlop(32);

		if (tempApp.GetNetworkInfo() != 2) {
			new TvScheduleAsyncTask().execute(null);
		} else {
			AlertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setupNaviBar();
		mActiveFeature = tempApp.getTVSC_Index();
		tempTVPr_Check = tempApp.getTVPr_CheckMap();
		TVAl_CheckMap	= tempApp.getTVAl_CheckMap();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		tempApp.setTVSC_Index(mSlideView.getmActiveFeature());
		/*if (tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().isChecked()) {
			tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().setChecked(false);
		}*/
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		tempApp.ButtonBeepPlay();
		super.onBackPressed();
	}

	private void setupNaviBar() {
		// TODO Auto-generated method stub
		tempApp.getNaviBtn_Singleton().getInstance().getNaviHeaderText().setText("CH편성표");
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().setBackgroundResource(R.drawable.top_button_back);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		tempApp.getMainrbFirst().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//tempApp.ButtonBeepPlay();
				if (tempApp.getMainTabGroup().getCheckedRadioButtonId() == R.id.first) {
					TabGroupActivity parentActivity = (TabGroupActivity)getParent();
					parentActivity.onHomeBackPressed(0);
					tempApp.getChannelTabGroup().check(R.id.TVCH_AllCH_TABBtn);
				}
			}
		});

		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftSubBtn().setVisibility(View.GONE);
		tempApp.getNaviBtn_Singleton().getInstance().getNaviRightSubBtn().setVisibility(View.VISIBLE);
		if (getIntent().getBooleanExtra(TVCH_ITEM_MY_TAB, false)) {
			tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().setVisibility(View.GONE);
		}else {
			tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().setVisibility(View.VISIBLE);
			tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().setBackgroundResource(R.drawable.top_button_my);
			//tempApp.getNaviBtn_Singleton().getInstance().getNaviRightBtn().setChecked(true);
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
		}
	}

	private String BrodcatTime(String BeforeTime, String AfterTime){
		//ori
		SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
		SimpleDateFormat beforeCvtTime = new SimpleDateFormat("MM월 dd일 (E) HH:mm ~ ");
		SimpleDateFormat afterCvtTime = new SimpleDateFormat("HH:mm");
		SimpleDateFormat allTime = new SimpleDateFormat("yyyy-MM-dd ", Locale.KOREAN);

		ParsePosition pos = new ParsePosition(0);
		Date beforeTime = mOriTime.parse(BeforeTime, pos);
		String Before =beforeCvtTime.format(beforeTime);
		if (AfterTime.equals("00700")) {
			//Before += "24:00";
		} else {
			pos = new ParsePosition(0);
			Date afterTime = mOriTime.parse(AfterTime, pos);
			Before += afterCvtTime.format(afterTime);
		}
		return Before;
	}

	private Boolean CurrentAirTime(String BeforeTime, String AfterTime){
		//ori
		SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);

		ParsePosition pos = new ParsePosition(0);
		Date beforeTime = mOriTime.parse(BeforeTime, pos);

		pos = new ParsePosition(0);
		Date afterTime = mOriTime.parse(AfterTime, pos);

		long currentTime = System.currentTimeMillis();
		if (currentTime <= afterTime.getTime() && currentTime >= beforeTime.getTime()) {
			return true;
		}else {
			return false;
		}

	}

	private String weekTime(String aTime){
		SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
		SimpleDateFormat WeekTime = new SimpleDateFormat("MM월 dd일 E요일 ");

		ParsePosition pos = new ParsePosition(0);
		Date frmTime = mOriTime.parse(aTime, pos);
		return WeekTime.format(frmTime);
	}

	private Boolean elapseTime(String aTime){
		SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);

		ParsePosition pos = new ParsePosition(0);
		Date frmTime = mOriTime.parse(aTime, pos);
		Long tarTime = frmTime.getTime();
		Long Current = System.currentTimeMillis();
		Long result = tarTime - Current;
		if (result < 0) {
			return true;
		} else {
			return false;
		}

	}
	private Boolean toDay(String aTime){
		SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
		SimpleDateFormat CurretkTime = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);

		ParsePosition pos = new ParsePosition(0);
		Date frmTime = mOriTime.parse(aTime, pos);
		Date Current =  new Date(System.currentTimeMillis());
		CurretkTime.format(frmTime);
		if (frmTime.getMonth() == Current.getMonth() && frmTime.getDate() == Current.getDate()) {
			return true;
		} else {
			return false;
		}

	}


	public class TVCH_Schedule_Adapter extends BaseAdapter{
		private LayoutInflater mInflater;
		//private Context context;
		private SCUEDULE_DATA_LIST items;
		public TVCH_Schedule_Adapter( Context context, SCUEDULE_DATA_LIST items) { //int textViewResourceId, 
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(context);
			//this.context = context;
			this.items = items;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub

			return position;
		}

		private void Reload() {
			// TODO Auto-generated method stub
			notifyDataSetChanged();
		}

		private String DayHoureConverter(String aTime) {
			// TODO Auto-generated method stub
			SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
			SimpleDateFormat m24Time = new SimpleDateFormat("HH:mm");

			ParsePosition pos = new ParsePosition(0);
			Date frmTime = mOriTime.parse(aTime, pos);
			return m24Time.format(frmTime);
		}



		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//CurrentDB = tempApp.getCurrentTVCH();
			final ViewHolder holder;
			final int tempPosition = position;
			//final View tempConvertView = convertView;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.tvch_schedulel_row, null);
				//Log.d("Sangtae", "convertView Null");
				holder = new ViewHolder();
				//holder.text = (TextView) convertView.findViewById(R.id.TextView01);
				// 아이콘
				holder.Mych_Icon	= (ImageView)convertView.findViewById(R.id.MyCh_Icon);
				//------메인-------
				holder.Pro_time		= (TextView)convertView.findViewById(R.id.tvch_schedule_ProgramTime);
				holder.Mych_Recording	= (ImageView)convertView.findViewById(R.id.tvch_icon_recording);

				holder.Pro_title	= (TextView) convertView.findViewById(R.id.tvch_schedule_ProgramTitle);

				holder.icon_hd		= (ImageView)convertView.findViewById(R.id.tvch_schedule_HD);
				holder.icon_alarm	= (ImageView)convertView.findViewById(R.id.tvch_schedule_Alarm);
				holder.icon_record	= (ImageView)convertView.findViewById(R.id.tvch_icon_record);
				holder.Pro_title.setSelected(true);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				//Log.d("Sangtae", "convertView Not Null");
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setId(Integer.parseInt(items.get(position).getSeq()));
			// Bind the data efficiently with the holder.

			holder.Pro_time.setText(DayHoureConverter(items.get(position).getTime()));
			holder.Pro_title.setText(items.get(position).getTitle());


			if (items.get(position).getHD()) {
				holder.icon_hd.setVisibility(View.VISIBLE);
			} else {
				holder.icon_hd.setVisibility(View.GONE);
			}


			if (items.size() != position+1) { // 다음 편성이 있는 경우 
				if (CurrentAirTime(items.get(position).getTime(), items.get(position+1).getTime()) && toDay(items.get(position).getTime())) {
					convertView.setBackgroundResource(R.drawable.tvch_schedulebar_air);
				}else {
					convertView.setBackgroundResource(R.drawable.list_bg);
				}

				if (elapseTime(items.get(position+1).getTime())) { // 다음 편성표를 전달하여 일정이 지났는지 확인
					convertView.setFocusable(true);
					AlphaAnimation animation = new AlphaAnimation(1, 0.65f);
					animation.setFillAfter(true);
					convertView.setAnimation(animation);
				} else {
					convertView.setFocusable(false);
					AlphaAnimation animation = new AlphaAnimation(1, 1);
					animation.setFillAfter(true);
					convertView.setAnimation(animation);
				}				
			} else {
				convertView.setFocusable(false);
				AlphaAnimation animation = new AlphaAnimation(1, 1);
				animation.setFillAfter(true);
				convertView.setAnimation(animation);

				String CurrentMaxTime = items.get(position).getTime().substring(0, 10);
				CurrentMaxTime += " 24:00:00";

				if (CurrentAirTime(items.get(position).getTime(), CurrentMaxTime) && toDay(items.get(position).getTime())) {
					convertView.setBackgroundResource(R.drawable.tvch_schedulebar_air);
				}
			}


			//My program
			if (tempTVPr_Check.size() != 0 ) {
				if (tempTVPr_Check.get(items.get(position).getID()) != null) {
					if (tempTVPr_Check.get(items.get(position).getID()).getMyProgram()) {
						holder.Mych_Icon.setVisibility(View.VISIBLE);
					} else {
						holder.Mych_Icon.setVisibility(View.GONE);
					}	
				} else {
					holder.Mych_Icon.setVisibility(View.GONE);
				}
			} else {
				holder.Mych_Icon.setVisibility(View.GONE);
			}

			// Alarm
			if (TVAl_CheckMap.size() != 0 ) {
				if (TVAl_CheckMap.containsKey(items.get(position).getSeq())) {
					holder.icon_alarm.setVisibility(View.VISIBLE);
				} else {
					holder.icon_alarm.setVisibility(View.GONE);
				}	
			} else {
				holder.icon_alarm.setVisibility(View.GONE);
			}

			// Record
			if (tempApp.getmRecord_DATA_LIST() != null) {
				if (tempApp.getmRecord_DATA_LIST().size() > 0) {
					for (int i = 0; i < tempApp.getmRecord_DATA_LIST().size(); i++) {
						if (tempApp.getmRecord_DATA_LIST().get(i).getChannelId().contains(TVCH_ChannelID) && tempApp.getmRecord_DATA_LIST().get(i).getRecordStartTime().contains(items.get(position).getTime())) {
							if (tempApp.getmRecord_DATA_LIST().get(i).getRecordStatus()) {
								holder.Mych_Recording.setVisibility(View.VISIBLE);			
								holder.icon_record.setVisibility(View.GONE);
								break;
							} else {
								holder.Mych_Recording.setVisibility(View.GONE);
								holder.icon_record.setVisibility(View.VISIBLE);
								break;
							}
						}else {
							holder.Mych_Recording.setVisibility(View.GONE);
							holder.icon_record.setVisibility(View.GONE);
						}
					}
				} else {
					holder.Mych_Recording.setVisibility(View.GONE);
					holder.icon_record.setVisibility(View.GONE);
				}
			}else {
				holder.Mych_Recording.setVisibility(View.GONE);
				holder.icon_record.setVisibility(View.GONE);
			}

			return convertView;
		}

	}




	static class ViewHolder {
		//-----------------
		ImageView	Mych_Icon;
		ImageView	Mych_Recording;
		//-----------------
		TextView	Pro_time;
		TextView	Pro_title;
		ImageView	icon_hd;
		ImageView	icon_alarm;
		ImageView	icon_record;
	}

	// 리스트 스레드
	class TvScheduleAsyncTask extends AsyncTask<Void, Void, SCUEDULE_DATA_LIST>
	{

		ProgressDialog pro;
		@Override
		protected SCUEDULE_DATA_LIST doInBackground(Void... params) {
			// TODO Auto-generated method stub

			String requestURL = XML_Address_Define.Record.getGetrecordreservelist(Udid);
			Log.d(DEBUG_TAG, "## URL Request 스케줄 요청(TvScheduleAsyncTask): "+requestURL);
			RecordReserveList_PASER reserveList_PASER = new RecordReserveList_PASER(requestURL);//tempApp.getMobileIMIE(getApplicationContext())));
			for (int i = 0; i < 2; i++) {
				if (reserveList_PASER.start()) {
					tempApp.setmRecord_DATA_LIST(reserveList_PASER.getRecord_LIST());
					break;
				}	
			}

			if(tempApp.getTV_SCUEDULE_LIST() ==null )
			{
				Log.d("Sangtae", "새로운 스케쥴 정보 가져오기");
				SCUEDULE_PASER paser	= new SCUEDULE_PASER();
				paser.start(getIntent().getStringExtra(TVCH_ITEM_SELECT));
				tempApp.setTV_SCUEDULE_LIST(paser.getScuedule_DATA_LIST());
				return tempApp.getTV_SCUEDULE_LIST();

			}else {
				if (tempApp.getTV_SCUEDULE_LIST().size() == 0) {
					Log.d("Sangtae", "새로운 스케쥴 정보 가져오기");
					SCUEDULE_PASER paser	= new SCUEDULE_PASER();
					paser.start(getIntent().getStringExtra(TVCH_ITEM_SELECT));
					tempApp.setTV_SCUEDULE_LIST(paser.getScuedule_DATA_LIST());
					return tempApp.getTV_SCUEDULE_LIST();

				} else {
					Log.d("Sangtae", "기존 스케쥴 정보 가져오기");
					return tempApp.getTV_SCUEDULE_LIST();
				}
			}

		}

		@Override
		protected void onPostExecute(SCUEDULE_DATA_LIST result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try
			{
				mList=result;

				if(mList.size() > 0 )
				{
					setupUI();
					Message tMessage = WarningHandler.obtainMessage();
					WarningHandler.sendMessageDelayed(tMessage, 1000);

				}
			}
			catch (Exception e) {
				// TODO: handle exception
				Log.w("VODSE_Preview", e);
			}		

			pro.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pro = ProgressDialog.show(getParent().getParent(), "", getResources().getString(R.string.tv_watting),true);
		}
	}

	private void setupUI() {
		// TODO Auto-generated method stub		
		int tempCount =0;
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		SCUEDULE_DATA_LIST mTempList = new SCUEDULE_DATA_LIST();
		if (mList.size() != 0) {
			for (int i = 0; i < mList.size(); i++) {
				if (i == 0) {
					// 새로운 배열
					mTempList.add(new SCUEDULE_DATA(mList.get(i)));
					//tempApp.setTV_SCUEDULE_LIST(mList);

				}else if (i < mList.size()-1) {
					//Log.d("Sangtae", "Schedule i: "+ i);
					if (mList.get(i).getDate().equals(mList.get(i+1).getDate())) {
						//같은 날짜에 경우
						mTempList.add(new SCUEDULE_DATA(mList.get(i)));
					}else {
						//다른 날짜의 경우
						mTempList.add(new SCUEDULE_DATA(mList.get(i))); // 현재까지는 저장
						LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.tvch_schedule_list, null);
						
						ListView lv = (ListView)lay.findViewById(R.id.tvch_schedule_listview);
						
						DisplayMetrics dm = getResources().getDisplayMetrics();
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								dm.widthPixels,
								ViewGroup.LayoutParams.FILL_PARENT,
								0.0F);
						lv.setLayoutParams(params);
						
						if (toDay(mTempList.get(0).getTime())) {
							for (int j = 0; j <mTempList.size(); j++) {
								if (!elapseTime(mTempList.get(j).getTime()) && toDay(mTempList.get(j).getTime())) {
									if (j == 0) {
										lv.setAdapter(new TVCH_Schedule_Adapter(getApplicationContext(), mTempList.clone()));
										lv.setSelection(j);	
									}else {
										lv.setAdapter(new TVCH_Schedule_Adapter(getApplicationContext(), mTempList.clone()));
										lv.setSelection(j-1);
									}
									break;
								}
							}
						}else {
							lv.setAdapter(new TVCH_Schedule_Adapter(getApplicationContext(), mTempList.clone()));
							//lv.setAdapter(new TVCH_Schedule_Adapter(getApplicationContext(), mTempList.clone(),mTempList.size()));
						}

						lv.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
								// TODO Auto-generated method stub
								Log.d("Sangtae", "arg1 "+arg1.getId() +"arg2: "+arg2 + " arg3: "+arg3);
								tempApp.ButtonBeepPlay();
								for (int j = 0; j <  mList.size(); j++) {
									if (mList.get(j).getSeq().equals(String.valueOf(arg1.getId()))) {
										Intent selectMessage = new Intent(getParent(), TVCH_Schedule_Setting.class);
										selectMessage.putExtra(TVCH_ITEM_SELECT, mList.get(j).getSeq());
										selectMessage.putExtra(TVCH_ITEM_HD, mList.get(j).getHD());// 2011 01 26 홍성일.
										selectMessage.putExtra(TVCH_ITEM_NUMBER, TVCH_CpyNumber);
										selectMessage.putExtra(TVCH_ITEM_NAME, TVCH_CpyName);
										selectMessage.putExtra(TVCH_ITEM_LOG_PATH, TVCH_LogURL);

										selectMessage.putExtra(Intent_KEY_Define.Recording.ChannelRecoddingMode, mChannelRecording);

										//SCUEDULE_DATA intent
										selectMessage.putExtra(TVCH_CHANNEL_ID,TVCH_ChannelID);

										selectMessage.putExtra(TVCH_SCHEDULE_ID, mList.get(j).getID());
										selectMessage.putExtra(TVCH_SCHEDULE_TITLE, mList.get(j).getTitle());
										selectMessage.putExtra(TVCH_SCHEDULE_TIME, mList.get(j).getTime());
										selectMessage.putExtra(TVCH_SCHEDULE_GRADE, mList.get(j).getGrade());

										selectMessage.putExtra(TVCH_SERIES_ID, mList.get(j).getSeriesId());
										selectMessage.putExtra(TVCH_PROGRAM_KEY, mList.get(j).getProgramKey());

										selectMessage.putExtra(TVCH_SCHEDULE_HD, mList.get(j).getHD());
										for (int i = 0; i < mList.size(); i++) {
											if (mList.size() == (i+1))
											{
												selectMessage.putExtra(TVCH_SCHEDULE_ALARM_TIME, BrodcatTime(mList.get(i).getTime(), "00700"));
												break;
											}
											if (mList.get(i).getSeq().equals(mList.get(j).getSeq())) {
												selectMessage.putExtra(TVCH_SCHEDULE_ALARM_TIME, BrodcatTime(mList.get(i).getTime(), mList.get(i+1).getTime())); // 2011 01 27 홍성일
												break;
											}
										}
										TabGroupActivity parentActivity = (TabGroupActivity)getParent();
										parentActivity.startChildActivity("TVCH_Schedule_Setting", selectMessage);
									}
								}
							}
						});



						TextView tv = (TextView)lay.findViewById(R.id.BroadcastDay);
						tv.setText(weekTime(mTempList.get(0).getDate()));
						//work.addView(lay);
						tempCount++;
						mSlideView.addView(lay);
						mTempList.clear();	// 리스트 초기화


					}

				}else if (mList.get(i).getDate().equals(mList.get(i).getDate())) {	// 마지막 같은 경우
					mTempList.add(new SCUEDULE_DATA(mList.get(i))); // 현재까지는 저장
					LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.tvch_schedule_list, null);
					
					ListView lv = (ListView)lay.findViewById(R.id.tvch_schedule_listview);
					
					DisplayMetrics dm = getResources().getDisplayMetrics();
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							dm.widthPixels,
							ViewGroup.LayoutParams.FILL_PARENT,
							0.0F);
					lv.setLayoutParams(params);
					
					lv.setAdapter(new TVCH_Schedule_Adapter(getApplicationContext(), mTempList.clone()));
					lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
							// TODO Auto-generated method stub
							Log.d("Sangtae", "arg1 "+arg1.getId() +"arg2: "+arg2 + " arg3: "+arg3);
							tempApp.ButtonBeepPlay();
							for (int j = 0; j <  mList.size(); j++) {
								if (mList.get(j).getSeq().equals(String.valueOf(arg1.getId()))) {
									Intent selectMessage = new Intent(getParent(), TVCH_Schedule_Setting.class);
									selectMessage.putExtra(TVCH_ITEM_SELECT, mList.get(j).getSeq());
									selectMessage.putExtra(TVCH_ITEM_NUMBER, TVCH_CpyNumber);
									selectMessage.putExtra(TVCH_ITEM_HD, mList.get(j).getHD());// 2011 01 26 홍성일.
									selectMessage.putExtra(TVCH_ITEM_NAME, TVCH_CpyName);
									selectMessage.putExtra(TVCH_ITEM_LOG_PATH, TVCH_LogURL);

									selectMessage.putExtra(Intent_KEY_Define.Recording.ChannelRecoddingMode, mChannelRecording);

									//SCUEDULE_DATA intent
									selectMessage.putExtra(TVCH_CHANNEL_ID,TVCH_ChannelID);

									selectMessage.putExtra(TVCH_SCHEDULE_ID, mList.get(j).getID());
									selectMessage.putExtra(TVCH_SCHEDULE_TITLE, mList.get(j).getTitle());
									selectMessage.putExtra(TVCH_SCHEDULE_TIME, mList.get(j).getTime());
									selectMessage.putExtra(TVCH_SCHEDULE_GRADE, mList.get(j).getGrade());

									selectMessage.putExtra(TVCH_SERIES_ID, mList.get(j).getSeriesId());
									selectMessage.putExtra(TVCH_PROGRAM_KEY, mList.get(j).getProgramKey());

									selectMessage.putExtra(TVCH_SCHEDULE_HD, mList.get(j).getHD());
									for (int i = 0; i < mList.size(); i++) {
										if (mList.size() == (i+1))
										{
											selectMessage.putExtra(TVCH_SCHEDULE_ALARM_TIME, BrodcatTime(mList.get(i).getTime(), "00700"));
											break;
										}
										if (mList.get(i).getSeq().equals(mList.get(j).getSeq())) {
											selectMessage.putExtra(TVCH_SCHEDULE_ALARM_TIME, BrodcatTime(mList.get(i).getTime(), mList.get(i+1).getTime())); // 2011 01 27 홍성일
											break;
										}
									}
									TabGroupActivity parentActivity = (TabGroupActivity)getParent();
									parentActivity.startChildActivity("TVCH_Schedule_Setting", selectMessage);
								}
							}
						}
					});
					TextView tv = (TextView)lay.findViewById(R.id.BroadcastDay);
					tv.setText(weekTime(mTempList.get(0).getDate()));
					//work.addView(lay);
					if (tempCount <= 1) {
						if (toDay(mTempList.get(0).getTime())) {
							for (int j = 0; j <mTempList.size(); j++) {
								if (!elapseTime(mTempList.get(j).getTime()) && toDay(mTempList.get(j).getTime())) {
									if (j == 0) {
										lv.setAdapter(new TVCH_Schedule_Adapter(getApplicationContext(), mTempList.clone()));
										lv.setSelection(j);	
									}else {
										lv.setAdapter(new TVCH_Schedule_Adapter(getApplicationContext(), mTempList.clone()));
										lv.setSelection(j-1);
									}

									break;
								}
							}
						}
					}
					mSlideView.setItem(lay);
				}
			}
		}

	}



}
