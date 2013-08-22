package pvr;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.SlideButton;
import hdlnc.dev2.sangtae.cnm.global.SlideButton.OnCheckChangedListner;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.CHECK_RESPONSE_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.RECORD_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.RECORD_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.RESPONSE_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.SCUEDULE_DATA;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCHImageDownloader;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pvr.PVR_TitleEditDialog;
import pvr.PVR_TitleEditDialog.OnEnterKeyListener;


import android.R.style;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class PVR_RecordSetting extends Activity implements OnEnterKeyListener {
	private final static String DEBUG_TAG = "PVR_RecordSetting";

	private final String Class7		=	"7세";// 관람가";
	private final String Class12	=	"12세";//이상";
	private final String Class15	=	"15세";//이상";
	private final String Class19	=	"19세";//이상";
	private final String ClassALL	=	"전체";// 관람가";

	private CNMApplication tempApp;
	private String Udid;

	// 채널 정보
	//private String TVCH_Select;

	// 프로그램 정보
	private SCUEDULE_DATA currentProgramInfo;

	//private boolean TVCH_bHd = false;
	//private String Alarm_Before = "alarmbefore";
	private Boolean mChannelRecording;

	private RESPONSE_DATA response_DATA;
	private Context mContext;
	private Boolean mOneClick = false;

	//////////////////////////////
	private final String SnippetItem_Key = "SnippetItem_Key";
	private final String ItemType_KEY	= "ItemType_KEY";
	private final String Reserve_Type	= "Reserve_Type";
	private final String Record_Type	= "Record_Type";

	private String Record_Mode;
	private RECORD_DATA mData;


	private RelativeLayout mOneLayout;
	private RelativeLayout mTwoLayout;
	//private RelativeLayout mThreeLayout;

	private ImageButton mRecordDeleteBtn;
	private ImageButton mTitleChangeBtn;

	private ImageView	mSlideIcon;
	private TextView	mSlideTitle;
	private SlideButton mRecordSlideBtn;


	private DetailRecoredAsyncTask asyncTask;

	private Handler handler = new Handler(){

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				//Toast.makeText(mContext, "성공", Toast.LENGTH_SHORT).show();

				break;

			default:
				break;
			}
		};

	};


	// /// // / / / / / / /

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pvr_record_setting);
		mContext = this;
		currentProgramInfo	= new SCUEDULE_DATA();

		mOneLayout = (RelativeLayout)findViewById(R.id.one_layout);
		mTwoLayout = (RelativeLayout)findViewById(R.id.two_layout);
		//mThreeLayout = (RelativeLayout)findViewById(R.id.three_layout);


		mRecordDeleteBtn = (ImageButton)findViewById(R.id.ReserveDeleteImgBtn);
		mTitleChangeBtn	 = (ImageButton)findViewById(R.id.ChangeTitleBtn);

		mSlideIcon  = (ImageView)findViewById(R.id.myRecord_icon);
		mSlideTitle = (TextView)findViewById(R.id.record_textview);
		mRecordSlideBtn = (SlideButton)findViewById(R.id.myRecordBtn);

		//setupProgramInfo();

		Log.d(DEBUG_TAG, "==================");

		tempApp = (CNMApplication)getApplication();
		Udid = tempApp.getMobileIMIE(this);
		Record_Mode = getIntent().getStringExtra(ItemType_KEY);
		Bundle bundle = getIntent().getBundleExtra(SnippetItem_Key);
		RECORD_DATA_LIST list = (RECORD_DATA_LIST)bundle.getSerializable(SnippetItem_Key);
		mData = list.get(0);

		//mList	= tempApp.getTV_SCUEDULE_LIST();
		/*myProgramList = tempApp.getTVPr_CheckMap();
		myAlarmList = tempApp.getTVAl_CheckMap();


		mAlarmManager =  (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		mChannelRecording = getIntent().getBooleanExtra(Intent_KEY_Define.Recording.ChannelRecoddingMode, false);
		 */
		if (!Record_Mode.equals(Reserve_Type)){
			mRecordSlideBtn.setChecked(mData.getRecordProtection());
		}

		ChangMyRecordIcon();
		setupUI();
	}



	@Override
	protected void onResume() {

		super.onResume();
		setupNaviBar();
	}

	@Override
	public void onEnterKeyPressed(String str) {
		// TODO Auto-generated method stub
		Log.d("Sangtae", "타이틀 변경 입력: "+str);

		sendRespone(XML_Address_Define.Record.getSetrecordnamereplace(mData.getRecordId(), Udid, mData.getChannelId(), mData.getRecordStartTime(), mData.getSeriesId(), str));
		
	}

	private void sendRespone(String aCommand){
		synchronized (aCommand) {
			if (asyncTask == null) {
				asyncTask = new DetailRecoredAsyncTask();
				asyncTask.execute(aCommand);
			}else if(!asyncTask.getStatus().equals(AsyncTask.Status.PENDING)
					&& !asyncTask.getStatus().equals(AsyncTask.Status.RUNNING)
					&& asyncTask.getStatus().equals(AsyncTask.Status.FINISHED)){
				asyncTask = new DetailRecoredAsyncTask();
				asyncTask.execute(aCommand);
			}
		}
	}

	private void setupNaviBar() {
		String tempTitle;
		if (Record_Mode.equals(Reserve_Type)) {
			tempTitle = "상세보기";
			mOneLayout.setVisibility(View.GONE);
			mTwoLayout.setVisibility(View.GONE);
			mSlideTitle.setText("녹화 예약");
		} else {
			tempTitle = "녹화물 상세보기";
			mOneLayout.setVisibility(View.VISIBLE);
			mTwoLayout.setVisibility(View.VISIBLE);
			mSlideTitle.setText("녹화물 보호");

		}
		NaviBtn_Singleton.getInstance().getNaviHeaderText().setText(tempTitle);
		NaviBtn_Singleton.getInstance().getNaviLeftBtn().setBackgroundResource(R.drawable.top_button_back);
		NaviBtn_Singleton.getInstance().getNaviLeftBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				tempApp.ButtonBeepPlay();
				onBackPressed();
			}
		});

		NaviBtn_Singleton.getInstance().getNaviLeftSubBtn().setVisibility(View.GONE);
		NaviBtn_Singleton.getInstance().getNaviRightBtn().setVisibility(View.GONE);
		NaviBtn_Singleton.getInstance().getNaviRightSubBtn().setVisibility(View.GONE);
	}

	// 프로그램 등급 표시
	private void setupProgramGradeUI() {
		ImageView myProIV = (ImageView)findViewById(R.id.icon_class);

		if (mData.getProgram_Grade() != null) {
			myProIV.setVisibility(View.VISIBLE);
			if (mData.getProgram_Grade().startsWith(Class7)) {
				myProIV.setBackgroundResource(R.drawable.icon_class_7);
			}else if (mData.getProgram_Grade().startsWith(Class12)) {
				myProIV.setBackgroundResource(R.drawable.icon_class_12);
			}else if (mData.getProgram_Grade().startsWith(Class15)) {
				myProIV.setBackgroundResource(R.drawable.icon_class_15);
			}else if (mData.getProgram_Grade().startsWith(Class19)) {
				myProIV.setBackgroundResource(R.drawable.icon_class_19);
			}else if (mData.getProgram_Grade().startsWith(ClassALL)) {
				myProIV.setBackgroundResource(R.drawable.icon_class_all);
			}
		}else {
			myProIV.setVisibility(View.GONE);
		}
	}

	// HD 방송 여부 표시
	private void setupProgramDefinitionUI() {
		ImageView myHDIV = (ImageView)findViewById(R.id.icon_hd);

		if (mData.getRecordHD()) {
			myHDIV.setVisibility(View.VISIBLE);
		} else {
			myHDIV.setVisibility(View.GONE);
		}	
	}


	private void setupUI() {

		// 채널 정보
		( (TextView)findViewById(R.id.Channel_Number) ).setText(String.format("Ch %s", mData.getChannelNo()));
		( (TextView)findViewById(R.id.Copany) ).setText(mData.getChannelName());
		if (mData.getChannel_logo_img() != null) {
			TVCHImageDownloader imageDownloader = new TVCHImageDownloader(getApplicationContext());
			imageDownloader.setContext(getApplicationContext());
			imageDownloader.download(mData.getChannel_logo_img(), ((ImageView)findViewById(R.id.Company_Log)));//, BitmapFactory.decodeResource(getResources(), R.drawable.noimg_logo));
		}

		// 프로그램 제목
		TextView  programTitleTextView = (TextView)findViewById(R.id.schedule_set_subject);
		programTitleTextView.setText(mData.getProgramName());
		programTitleTextView.setSelected(true);
		setupProgramGradeUI();		
		setupProgramDefinitionUI();

		//level 3
		// 프로그램 방영 시간
		TextView 	programTimeTextView	= (TextView)findViewById(R.id.schedule_set_time);
		//if(mData.getRecordStartTime() != null  && mData.getRecordEndTime() != null)
		programTimeTextView.setText(BrodcatTime(mData.getRecordStartTime(), mData.getRecordEndTime()));


		mRecordDeleteBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendRespone(XML_Address_Define.Record.getSetrecorddele(mData.getRecordId(), Udid, mData.getChannelId(), mData.getRecordStartTime(), XML_Address_Define.Record.RECORD_DEL_ONE));

			}
		});

		mTitleChangeBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				new PVR_TitleEditDialog(getParent(), style.Theme_Translucent_NoTitleBar, 
						"최대 12자 이내", PVR_RecordSetting.this).show();
			}
		});



		mRecordSlideBtn.setOnCheckChangedListner(new OnCheckChangedListner() {

			@Override
			public void onCheckChanged(View v, boolean isChecked) {
				// TODO Auto-generated method stub
				ChangMyRecordIcon();
				Log.d("Sangtae", "MyRecored onCheckChanged");
			}
		});
		mRecordSlideBtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.d("Sangtae", "MyRecored OnClickListener: "+event.getAction());
				if (event.getAction() == MotionEvent.ACTION_UP)
					if (Record_Mode.equals(Reserve_Type)) {	// 예약 녹화
						if (mData.getRecordStatus()) {
							/*if (mRecordSlideBtn.isChecked()) {
								Log.d("Sangtae", "즉시 녹화 취소");
								//sendRespone(XML_Address_Define.Record.getSetrecordstop(Udid, mData.getChannelId()));
								reserveRecording(true);
							} else {
								Log.d("Sangtae", "즉시 녹화 시작");
								//sendRespone(XML_Address_Define.Record.getSetrecord(Udid, mData.getChannelId()));
							}*/
							reserveRecording(!mRecordSlideBtn.isChecked());

						} else {
							/*if (mRecordSlideBtn.isChecked()) {
								Log.d("Sangtae", "예약녹화 취소");
								//sendRespone(XML_Address_Define.Record.getSetrecordcancelreserve(Udid, mData.getChannelId(), mData.getRecordStartTime(), mData.getSeriesId(), "2"));
								reserveRecording(true);
							} else {
								Log.d("Sangtae", "예약녹화 시작");
								reserveRecording(false);
								//sendRespone(XML_Address_Define.Record.getSetrecordreserve(Udid, mData.getChannelId(), mData.getRecordStartTime()));
							}*/
							reserveRecording(!mRecordSlideBtn.isChecked());
						}
					} else { // 예약 녹화물 
						//녹화물 보호 설정/취소
						if (mRecordSlideBtn.isChecked()) {
							Log.d("Sangtae", "녹화물 보호 취소");
							sendRespone(XML_Address_Define.Record.getSetrecordprotection(mData.getRecordId(), Udid, mData.getChannelId(), XML_Address_Define.Record.RECORD_PROTECTION_NO));
						} else {
							Log.d("Sangtae", "녹화물 보호 시작");
							sendRespone(XML_Address_Define.Record.getSetrecordprotection(mData.getRecordId(), Udid, mData.getChannelId(), XML_Address_Define.Record.RECORD_PROTECTION_YES));

						}
					}
				return false;
			}
		});


	}

	private void ChangMyRecordIcon(){
		if (Record_Mode.equals(Record_Type)) {
			if(mRecordSlideBtn.isChecked()){
				mSlideIcon.setBackgroundResource(R.drawable.pvr_icon_key_on);
			}else {
				mSlideIcon.setBackgroundResource(R.drawable.pvr_icon_key_off);
			}
		}else if (Record_Mode.equals(Reserve_Type)) {
			if(mRecordSlideBtn.isChecked()){
				mSlideIcon.setBackgroundResource(R.drawable.set_icon_record_on);
			}else {
				mSlideIcon.setBackgroundResource(R.drawable.set_icon_record_off);
			}
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
		if (AfterTime == null) {
			//Before += "24:00";
		} else {
			pos = new ParsePosition(0);
			Date afterTime = mOriTime.parse(AfterTime, pos);
			Before += afterCvtTime.format(afterTime);
		}
		return Before;
	}


	String setNoti(String aTime){
		SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);

		ParsePosition pos = new ParsePosition(0);
		Date frmTime = mOriTime.parse(aTime, pos);

		long reTime=getRemineTime(aTime);
		String expandedText = "";
		Log.d("TEST","reTime = "+reTime);


		if(reTime < 300000)
		{
			Date date = new Date(System.currentTimeMillis());

			int min=frmTime.getMinutes() - date.getMinutes();

			if(min <0)
			{
				min=(frmTime.getMinutes()+60) - date.getMinutes();

			}

			/*
			long time=frmTime.getTime() - date.getTime();

			date = new Date(time);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MILLISECOND, );
			int hour = cal.get(Calendar.HOUR);
			 */

			expandedText=String.valueOf( min);
			Log.d("TEST","expandedText = "+expandedText);
			frmTime = date;




		}
		else{
			frmTime.setMinutes(frmTime.getMinutes() - 5);
			expandedText=String.valueOf( 5);
			Log.d("TEST","expandedText = "+expandedText);
		}

		return expandedText;

	}

	long getRemineTime(String BroadCatTime){

		//ori
		SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
		//SimpleDateFormat beforeCvtTime = new SimpleDateFormat("MM월 dd일 (E) HH:mm ~ ");

		ParsePosition pos = new ParsePosition(0);
		Date onriginTime = mOriTime.parse(BroadCatTime, pos);
		long startTime=onriginTime.getTime();
		//현재의 시간 설정	
		Calendar cal=Calendar.getInstance();
		Date endDate=cal.getTime();
		long endTime=endDate.getTime();

		long time=startTime - endTime;

		return time;
	}
	long getTime(String atime){

		//ori
		SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);


		ParsePosition pos = new ParsePosition(0);
		Date onriginTime = mOriTime.parse(atime, pos);


		return onriginTime.getTime();
	}

	private class DetailRecoredAsyncTask extends AsyncTask<String, Void, RESPONSE_DATA>{
		private ProgressDialog pro;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pro = ProgressDialog.show(getParent().getParent(), "", getResources().getString(R.string.tv_watting),true);
		}

		@Override
		protected RESPONSE_DATA doInBackground(String... params) {
			// TODO Auto-generated method stub
			for (int i = 0; i < 2; i++) {
				if (CHECK_RESPONSE_PASER.start(params[0])) {
					return CHECK_RESPONSE_PASER.getArrayList();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(RESPONSE_DATA result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pro.dismiss();

			if (result != null) {
				if (result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_100)) {
					handler.sendMessage(handler.obtainMessage(100));
				}else {
					// 실패 처리
					tempToast(result);
				}

			} else {
				AlertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
			}

		}

	}

	private void tempToast(RESPONSE_DATA aData){
		Toast.makeText(mContext, String.format("ResultCode: %s, ErrString: %s", aData.getResultCode(), aData.getErrorString()), Toast.LENGTH_SHORT).show();
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


	/// // / / / / / / / / / / / /// // / / / / / / / / / / / / // // / / / / / / / / / / / / // // / / / / / / / / / / / / // // / / / / / / / / / / / / // // / / / / / / / / / / / / // // / / / / / / / / / / / /  

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

	private void reserveRecording(final boolean isRecording) {

		Log.d(DEBUG_TAG, "reserveRecording: "+isRecording);

		final Boolean isNow = elapseTime(mData.getRecordStartTime());

		if (!mData.getSeriesId().contains("NULL") && !isNow) { // 시리즈 녹화

			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.recored_select, null);

			Builder rec_dialog = new AlertDialog.Builder(getParent().getParent());

			TextView title = new TextView(getApplicationContext());
			title.setBackgroundColor(Color.BLACK);
			title.setPadding(10, 10, 10,10);
			title.setGravity(Gravity.CENTER);
			title.setTextColor(Color.WHITE);
			title.setTextSize(20);

			Button button1 = (Button)layout.findViewById(R.id.Select_button1);
			Button button2 = (Button)layout.findViewById(R.id.Select_button2);
			if (isRecording) {
				if (isNow) {
					title.setText("즉시 녹화");					
					button1.setText("이번 회차만 즉시 녹화하기");
					button2.setText("이번 회부터 즉시 전체 녹화하기");
				}
				else {
					title.setText("녹화 예약");
					button1.setText("이번 회차만 녹화 예약하기");
					button2.setText("이번 회부터 전체 녹화 예약하기");
				}
			}else {
				if (isNow) {
					
					title.setText("녹화 취소");
				}
				else {
					title.setText("녹화  예약 취소");
				}
				
				button1.setText("이번 회차만 녹화 취소하기");
				button2.setText("이번 회부터 전체 녹화 취소하기");
			}
			
			rec_dialog.setCustomTitle(title);
			rec_dialog.setNegativeButton("취소", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					dialog.dismiss();

					mRecordSlideBtn.setChecked(!isRecording );
				}
			} )
			.setCustomTitle(title)
			.setView(layout);

			final AlertDialog recordingDlg = rec_dialog.create();

			button1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (isRecording) {
						if (isNow) {	// 단편 즉시 녹화
							if ( !sendShortRecordNow() )
								showRecordFailMessage();
						}
						else {// 단편  예약
							if ( !sendShortRecordReserve() )
								showRecordFailMessage();								
						}
					}
					else// 즉시 & 단편 예약 취소
						sendShortRecordReserveCancel();

					recordingDlg.dismiss();
				}
			});

			button2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (isRecording) {	
						if (isNow) {
							if ( !sendShortRecordNow() )// 즉시단편 녹화
								showRecordFailMessage();
							else {
								if ( !sendSeriesRecordReserve() ) // 시리즈 예약
									showRecordFailMessage();								
							}
						}
						else {	
							if ( !sendSeriesRecordReserve() )// 시리즈 예약
								showRecordFailMessage();
						}
					}
					else
						sendSeriesRecordReserveCancel(); // 시리즈 취소

					recordingDlg.dismiss();
				}
			});

			recordingDlg.show();

		}
		else {//단편
			if (isRecording) {
				if (isNow) {
					if ( !sendShortRecordNow() ) // 즉시 단편 예약
						showRecordFailMessage();								
				}
				else {
					if ( !sendShortRecordReserve() ) // 단편 예약
						showRecordFailMessage();								
				}
			}
			else {
				AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
						getParent().getParent());
				alert_internet_status.setTitle( (isNow) ? "녹화 중지" : "녹화 예약 취소");
				alert_internet_status.setMessage(((isNow) ? "녹화를 중지" : "녹화 예약을 취소")+"하시겠습니까?");
				alert_internet_status.setPositiveButton("예",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						sendShortRecordReserveCancel();
						dialog.dismiss(); // 닫기
					}
				});
				alert_internet_status.setNegativeButton("아니오",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss(); // 닫기
						mRecordSlideBtn.setChecked( !isRecording );
					}
				});
				AlertDialog alert = alert_internet_status.create();
				alert.show();
			}
		}

	}


	// 이번 회차부터 전체 녹화 취소하기
	private Boolean sendSeriesRecordReserveCancel() {
		String requestURL = XML_Address_Define.Record.getSetrecordcancelreserve(Udid, mData.getChannelId(), mData.getRecordStartTime(), mData.getSeriesId(), XML_Address_Define.Record.RECORD_DEL_ALL);
		Log.d(DEBUG_TAG, "### URL Request 시리즈 전체 녹화취소: " + requestURL);

		CHECK_RESPONSE_PASER.start(requestURL);
		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			//Log.d("Sangtae", "시리즈 취소 성공");
			mRecordSlideBtn.setChecked(false);

			return true;
		}else {
			Log.d("Sangtae", "시리즈 취소 실패: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			//mOneClick=false;
			mRecordSlideBtn.setChecked(true);

			return false;
			//showRecordFailMessage();
			//recordReserveButton.setChecked(true);
		}

	}
	// 이번 회차부터 전체 녹화 예약하기
	private Boolean sendSeriesRecordReserve() {
		String requestURL = XML_Address_Define.Record.getSetrecordseriesreserve( Udid, mData.getChannelId(), mData.getRecordStartTime(), mData.getSeriesId());
		Log.d(DEBUG_TAG, "### URL Request 시리즈 전체 녹화예약: " + requestURL);
		CHECK_RESPONSE_PASER.start(requestURL);

		response_DATA = CHECK_RESPONSE_PASER.getArrayList();

		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			Log.d("Sangtae", "시리즈 예약 성공");
			mRecordSlideBtn.setChecked(true);

			return true;
		}else {
			Log.d("Sangtae", "시리즈 예약 실패: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			mRecordSlideBtn.setChecked(false);

			return false;
			//showRecordFailMessage();
			//mOneClick=false;
			//recordReserveButton.setChecked(false);
		}
	}

	// 이번 회차만 녹화 예약하기
	private Boolean sendShortRecordReserve() {
		String requestURL = XML_Address_Define.Record.getSetrecordreserve(Udid, mData.getChannelId(), mData.getRecordStartTime());
		Log.d(DEBUG_TAG, "### URL Request 이번 회차만 녹화예약: " + requestURL);
		CHECK_RESPONSE_PASER.start(requestURL);

		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			mRecordSlideBtn.setChecked(true);
			Log.d("Sangtae", "단편 녹화 성공");

			return true;
		}
		else {
			Log.d("Sangtae", "단편 녹화 실패: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			mRecordSlideBtn.setChecked(false);

			return false;
			//showRecordFailMessage();
			//mOneClick=false;
			//recordReserveButton.setChecked(false);
		}
	}

	// 이번 회차만 즉시 녹화하기
	private Boolean sendShortRecordNow() {
		String requestURL = XML_Address_Define.Record.getSetrecordseries(Udid, mData.getChannelId(), mData.getRecordStartTime());
		Log.d(DEBUG_TAG, "### URL Request 이번 회차만 즉시 녹화: " + requestURL);
		CHECK_RESPONSE_PASER.start(requestURL);

		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			mRecordSlideBtn.setChecked(true);
			Log.d(DEBUG_TAG, "즉시 단편 녹화 성공");
			return true;
		}
		else {
			Log.d(DEBUG_TAG, "즉시 단편 녹화 실패: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			mRecordSlideBtn.setChecked(false);

			return false;
			//showRecordFailMessage();
			//mOneClick=false;
			//recordReserveButton.setChecked(false);
		}
	}


	// 이번 회차만 녹화 취소하기
	private Boolean sendShortRecordReserveCancel() {
		String requestURL = XML_Address_Define.Record.getSetrecordcancelreserve(Udid, mData.getChannelId(), mData.getRecordStartTime(), mData.getSeriesId(), XML_Address_Define.Record.RECORD_DEL_ONE);
		Log.d(DEBUG_TAG, "### URL Request 이번 회만 녹화취소: " + requestURL);

		CHECK_RESPONSE_PASER.start(requestURL);
		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			Log.d("Sangtae", "단편 취소 성공");
			mRecordSlideBtn.setChecked(false);

			return true;
		}else {
			Log.d("Sangtae", "단편 취소 실패: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			//mOneClick=false;
			mRecordSlideBtn.setChecked(true);

			return false;
			//showRecordFailMessage();
			//recordReserveButton.setChecked(true);

		}
	}

	private void showRecordFailMessage() {
		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
				getParent().getParent());
		alert_internet_status.setTitle("녹화 불가");
		alert_internet_status.setMessage(getRecordFailMessage());
		alert_internet_status.setPositiveButton("확인",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				sendShortRecordReserveCancel();
				dialog.dismiss(); // 닫기
			}
		});
		AlertDialog alert = alert_internet_status.create();
		alert.show();		
	}

	private String getRecordFailMessage() {
		String resultCode = response_DATA.getResultCode();
		String errorMessage = null;

		if (resultCode.contains("002")) {
			errorMessage = "중복되는 시간에 다른 프로그램의 녹화 예약이 설정되어 있습니다.";
		}
		else if (resultCode.contains("003")) {
			errorMessage =  "셋탑의 저장공간이 부족합니다. 녹화물 목록을 확인해주세요.";
		}
		else if (resultCode.contains("005")) {
			errorMessage =  "고객님의 셋탑에서 제공되지 않는 상품 또는 지역의 채널입니다. [설정/안내]에서 확인해주세요.";
		}
		else if (resultCode.contains("005-1")
				|| resultCode.contains("010")) {
			errorMessage =  "셋탑에서 동시화면 기능을 사용중인 경우 원격으로는 즉시녹화가 불가합니다.";
		}
		else if (resultCode.contains("011")
				|| resultCode.contains("015")) {
			errorMessage =  "셋탑에서 다른 채널이 녹화중인 경우 원격으로는 즉시녹화가 불가합니다.";
		}
		else if (resultCode.contains("009")
				|| resultCode.contains("012")
				|| resultCode.contains("013")
				|| resultCode.contains("023")
				|| resultCode.contains("024")
				) {
			errorMessage =  "녹화 할 수 없는 채널 입니다.\n상품설정 또는 시청제한설정을 확인해주세요.";
		}		
		else if (resultCode.contains("028")
				|| resultCode.contains("206")){
			errorMessage =  "셋탑 뒷 전원이\n 꺼져있는 상태이거나\n 통신이 고르지 못해\n 연결이 불가능 합니다.";
		}
		else {
			errorMessage = "ErrorCode: " + resultCode + "\n" + response_DATA.getErrorString();
		}

		return errorMessage;
	}
}

