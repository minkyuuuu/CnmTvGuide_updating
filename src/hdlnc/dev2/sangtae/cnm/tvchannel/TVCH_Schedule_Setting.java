package hdlnc.dev2.sangtae.cnm.tvchannel;

import static hdlnc.dev2.sangtae.cnm.global.CNM_Alarm_Service.CNM_AlarmDelete_Service_ACTION;
import static hdlnc.dev2.sangtae.cnm.global.CNM_Alarm_Service.CNM_Alarm_Service_ACTION;
//import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_HD;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_LOG_PATH;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_NAME;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_NUMBER;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_SELECT;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_CHANNEL_ID;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_PROGRAM_KEY;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_SCHEDULE_ALARM_TIME;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_SCHEDULE_GRADE;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_SCHEDULE_HD;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_SCHEDULE_ID;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_SCHEDULE_TIME;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_SCHEDULE_TITLE;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_SERIES_ID;
import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.Intent_KEY_Define;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.SlideButton;
import hdlnc.dev2.sangtae.cnm.global.TVCH_Alarm_CheckList;
import hdlnc.dev2.sangtae.cnm.global.TVCH_PR_CheckList;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.CHECK_RESPONSE_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.RESPONSE_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.SCUEDULE_DATA;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
//import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TVCH_Schedule_Setting extends Activity  {
	private final static String DEBUG_TAG = "TVCH_Schedule_Setting";

	private final String Class7		=	"7세";// 관람가";
	private final String Class12	=	"12세";//이상";
	private final String Class15	=	"15세";//이상";
	private final String Class19	=	"19세";//이상";
	private final String ClassALL	=	"전체";// 관람가";

	private CNMApplication theApplication;
	private String Udid;
	//private SCUEDULE_DATA_LIST mList;
	private HashMap<String, TVCH_PR_CheckList> myProgramList;
	private HashMap<String, TVCH_Alarm_CheckList> myAlarmList;
	
	private TextView myProgramText;
	private TextView myAlarmText;
	private TextView recordReserveText;
	
	private ImageView	myProgramImage;
	private ImageView	myAlarmImage;
	private ImageView	recordReserveImage;

	private ImageView	Care_MsgImg;

	private SlideButton myProgramButton;
	private SlideButton myAlarmButton;
	private SlideButton recordReserveButton;
	
	// 알람 메니저
	private AlarmManager mAlarmManager;
	
	// 채널 정보
	//private String TVCH_Select;
	private String channelID;
	private String channelNumber;	// 채널 번호 - Ch 5
	private String channelLogoURL;		// 채널 로고 URL
	private String channelName;	// 채널 이름 - SBS_HD
	
	// 프로그램 정보
	private SCUEDULE_DATA programInfo;
	
	//private boolean TVCH_bHd = false;
	//private String Alarm_Before = "alarmbefore";
	private Boolean isAllowRecording;

	private RESPONSE_DATA response_DATA;
	private Context mContext;
	//private Boolean mOneClick = false;
	private Boolean isSetUI = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tvch_schedule_setting);
		mContext = this;
		programInfo	= new SCUEDULE_DATA();
		
		setupChannelInfo();
		
		setupProgramInfo();
		
		Log.d(DEBUG_TAG, "==================");

		theApplication = (CNMApplication)getApplication();
		Udid = theApplication.getMobileIMIE(this);
		//mList	= tempApp.getTV_SCUEDULE_LIST();
		myProgramList = theApplication.getTVPr_CheckMap();
		myAlarmList = theApplication.getTVAl_CheckMap();


		mAlarmManager =  (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		isAllowRecording = getIntent().getBooleanExtra(Intent_KEY_Define.Recording.ChannelRecoddingMode, false);

		setupUI();
	}
	
	// 채널 정보 설정하기
	private void setupChannelInfo() {

		//TVCH_Select	   = getIntent().getStringExtra(TVCH_ITEM_SELECT);
		channelNumber = getIntent().getStringExtra(TVCH_ITEM_NUMBER);
		channelID = getIntent().getStringExtra(TVCH_CHANNEL_ID);
		channelName   = getIntent().getStringExtra(TVCH_ITEM_NAME);
		channelLogoURL	   = getIntent().getStringExtra(TVCH_ITEM_LOG_PATH);
		//TVCH_bHd	   = getIntent().getBooleanExtra(TVCH_ITEM_HD, false);	

		Log.d(DEBUG_TAG, "== Channel Info ==");
		//Log.d(DEBUG_TAG, "TVCH_Select: " + TVCH_Select);
		Log.d(DEBUG_TAG, "TVCH_CpyNumber: " + channelNumber);
		Log.d(DEBUG_TAG, "TVCH_ChannelID: " + channelID);
		Log.d(DEBUG_TAG, "TVCH_CpyName: " + channelName);
		Log.d(DEBUG_TAG, "TVCH_LogURL: " + channelLogoURL);
	}

	// 프로그램 정보 설정하기
	private void setupProgramInfo() {
		Log.d(DEBUG_TAG, "== Program Info ==");

		programInfo.setID(getIntent().getStringExtra(TVCH_SCHEDULE_ID));
		programInfo.setSeq(getIntent().getStringExtra(TVCH_ITEM_SELECT));
		programInfo.setTitle(getIntent().getStringExtra(TVCH_SCHEDULE_TITLE));
		programInfo.setHD(getIntent().getBooleanExtra(TVCH_SCHEDULE_HD, false));
		programInfo.setTime(getIntent().getStringExtra(TVCH_SCHEDULE_TIME));
		programInfo.setGrade(getIntent().getStringExtra(TVCH_SCHEDULE_GRADE));
		programInfo.setAlarm_time(getIntent().getStringExtra(TVCH_SCHEDULE_ALARM_TIME));
		programInfo.setSeriesId(getIntent().getStringExtra(TVCH_SERIES_ID));
		programInfo.setProgramKey(getIntent().getStringExtra(TVCH_PROGRAM_KEY));

		Log.d(DEBUG_TAG, "ID: " + programInfo.getID());
		Log.d(DEBUG_TAG, "Seq: " + programInfo.getSeq());
		Log.d(DEBUG_TAG, "Title: " + programInfo.getTitle());
		Log.d(DEBUG_TAG, "HD: " + programInfo.getHD());
		Log.d(DEBUG_TAG, "Time: " + programInfo.getTime());
		Log.d(DEBUG_TAG, "Grade: " + programInfo.getGrade());
		Log.d(DEBUG_TAG, "Alarm_time: " + programInfo.getAlarm_time());
		Log.d(DEBUG_TAG, "SeriesId: " + programInfo.getSeriesId());
		Log.d(DEBUG_TAG, "ProgramKey: " + programInfo.getProgramKey());

	}


	@Override
	protected void onResume() {
		
		super.onResume();
		setupNaviBar();
	}

	private void setupNaviBar() {
		
		NaviBtn_Singleton.getInstance().getNaviHeaderText().setText("상세보기");
		NaviBtn_Singleton.getInstance().getNaviLeftBtn().setBackgroundResource(R.drawable.top_button_back);
		NaviBtn_Singleton.getInstance().getNaviLeftBtn().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				theApplication.ButtonBeepPlay();
				onBackPressed();
			}
		});

		theApplication.getMainrbFirst().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//tempApp.ButtonBeepPlay();
				if (theApplication.getMainTabGroup().getCheckedRadioButtonId() == R.id.first) {
					TabGroupActivity parentActivity = (TabGroupActivity)getParent();
					parentActivity.onHomeBackPressed(0);
					theApplication.getChannelTabGroup().check(R.id.TVCH_AllCH_TABBtn);
				}
			}
		});
		NaviBtn_Singleton.getInstance().getNaviLeftSubBtn().setVisibility(View.GONE);
		NaviBtn_Singleton.getInstance().getNaviRightBtn().setVisibility(View.GONE);
		NaviBtn_Singleton.getInstance().getNaviRightSubBtn().setVisibility(View.GONE);
	}

	// 프로그램 등급 표시
	private void setupProgramGradeUI() {
		ImageView myProIV = (ImageView)findViewById(R.id.icon_class);

		if (programInfo.getGrade() != null) {
			myProIV.setVisibility(View.VISIBLE);
			if (programInfo.getGrade().startsWith(Class7)) {
				myProIV.setBackgroundResource(R.drawable.icon_class_7);
			}else if (programInfo.getGrade().startsWith(Class12)) {
				myProIV.setBackgroundResource(R.drawable.icon_class_12);
			}else if (programInfo.getGrade().startsWith(Class15)) {
				myProIV.setBackgroundResource(R.drawable.icon_class_15);
			}else if (programInfo.getGrade().startsWith(Class19)) {
				myProIV.setBackgroundResource(R.drawable.icon_class_19);
			}else if (programInfo.getGrade().startsWith(ClassALL)) {
				myProIV.setBackgroundResource(R.drawable.icon_class_all);
			}
		}else {
			myProIV.setVisibility(View.GONE);
		}
	}
	
	// HD 방송 여부 표시
	private void setupProgramDefinitionUI() {
		ImageView myHDIV = (ImageView)findViewById(R.id.icon_hd);

		if (programInfo.getHD()) {
			myHDIV.setVisibility(View.VISIBLE);
		} else {
			myHDIV.setVisibility(View.GONE);
		}	
	}

	// 마이 프로그램 초기 설정 표시 
	private void setupMyProgramUI() {
		myProgramImage	= (ImageView)findViewById(R.id.myprogram_icon);
		myProgramButton	= (SlideButton)findViewById(R.id.myProTglBtn);

		// 이 프로그램이 마이 프로그램인지 체크한다. 
		String programID = programInfo.getID();
		if (myProgramList.containsKey(programID) 
				&& myProgramList.get(programID).getMyProgram()) {
			setMyProgramUI(true);
		} else {
			setMyProgramUI(false);
		}
		Log.d(DEBUG_TAG, "setupMyProgramUI");
	}

	private void setupRecordingUI(String recordingText) {

		RelativeLayout recordLayout = (RelativeLayout)findViewById(R.id.sch_record_layout);

		// my Record icon check
		String settopType = theApplication.getSettopBoxType();
		if (settopType.contains("SD") || settopType.contains("HD")) {
			recordLayout.setVisibility(View.GONE);
			Care_MsgImg.setVisibility(View.GONE);			
		} 
		else if (isAllowRecording) {	
			
			recordReserveText.setText(recordingText);
			
			recordLayout.setVisibility(View.VISIBLE);
			Care_MsgImg.setVisibility(View.GONE);

			setRecordingUI(false);
			// Record
			if (theApplication.getmRecord_DATA_LIST() != null) {	// 녹화 중인지 체크
				if (theApplication.getmRecord_DATA_LIST().size() > 0) {
					for (int i = 0; i < theApplication.getmRecord_DATA_LIST().size(); i++) {
						if (theApplication.getmRecord_DATA_LIST().get(i).getChannelId().contains(channelID) && theApplication.getmRecord_DATA_LIST().get(i).getRecordStartTime().contains(programInfo.getTime())) {	// 현재 녹화 중 체크
							setRecordingUI(true);
							Log.d(DEBUG_TAG, "Initial RecordingUI - true");
							break;
						}
//						else {
//							setRecordingUI(false);
//						}
					}
				}
			}
//			else {
//				setRecordingUI(false);
//			}
		} else {	// 채널 사업자 녹화 제한
			recordLayout.setVisibility(View.GONE);
			Care_MsgImg.setVisibility(View.VISIBLE);
		}
	}
	
	private void setupAlarmUI(boolean isVisible) {

		RelativeLayout alarmLayout = (RelativeLayout)findViewById(R.id.sch_alarm_layout);
		
		if (isVisible) {
			alarmLayout.setVisibility(View.VISIBLE);

			// 이 프로그램이 알람 예약이 되어 있는지 체크한다.
			String programSequence = programInfo.getSeq();
			if (myAlarmList.containsKey(programSequence)
					&& myAlarmList.get(programSequence).getDB_ALARM_CHECK()) {
				setAlarmUI(true);
			}
			else {
				setAlarmUI(false);
			}
		}
		else {
			alarmLayout.setVisibility(View.GONE);		
		}
	}
	
	private void setupUI() {
		
		// 채널 정보
		( (TextView)findViewById(R.id.Channel_Number) ).setText(channelNumber);
		( (TextView)findViewById(R.id.Copany) ).setText(channelName);
		( (TextView)findViewById(R.id.Copany) ).setSelected(true);
		
		if (channelLogoURL != null) {
			TVCHImageDownloader imageDownloader = new TVCHImageDownloader(getApplicationContext());
			imageDownloader.setContext(getApplicationContext());
			imageDownloader.download(channelLogoURL, ((ImageView)findViewById(R.id.Company_Log)));//, BitmapFactory.decodeResource(getResources(), R.drawable.noimg_logo));
		}
		
		// 프로그램 제목
		TextView  programTitleTextView = (TextView)findViewById(R.id.schedule_set_subject);
		programTitleTextView.setText(programInfo.getTitle());
		programTitleTextView.setSelected(true);
		setupProgramGradeUI();		
		setupProgramDefinitionUI();

		//level 3
		// 프로그램 방영 시간
		TextView 	programTimeTextView	= (TextView)findViewById(R.id.schedule_set_time);
		programTimeTextView.setText(programInfo.getAlarm_time());
		
		// My Program
		setupMyProgramUI();
		myProgramText	= (TextView)findViewById(R.id.mypro_textview);

		// 알람 예약
		myAlarmImage	= (ImageView)findViewById(R.id.myalarm_icon);
		myAlarmButton	= (SlideButton)findViewById(R.id.myAlarmTglBtn);
		myAlarmText	= (TextView)findViewById(R.id.alarm_textview);

		String recordingText;		
		if ( elapseTime(programInfo.getTime()) ) {
			// 현재 방영 중인 경우
			recordingText = "즉시 녹화";
			setupAlarmUI(false);
		}
		else {
			recordingText = "녹화 예약";
			setupAlarmUI(true);
		}
		
		// 녹화
		Care_MsgImg	= (ImageView)findViewById(R.id.set_care_text_img);
		recordReserveImage	= (ImageView)findViewById(R.id.myRecord_icon);		
		recordReserveButton= (SlideButton)findViewById(R.id.myRecordBtn);
		recordReserveText	= (TextView)findViewById(R.id.record_textview);

		setupRecordingUI(recordingText);

		isSetUI = false;
		setButtonOnClickListner();
	}

	private void setMyProgramUI(boolean isEnabled) {
		if (isEnabled) {
			myProgramImage.setBackgroundResource(R.drawable.icon_myprogram);
			myProgramButton.close();
		}
		else {
			myProgramButton.open();
			myProgramImage.setBackgroundResource(R.drawable.icon_myprogram_unselect);			
		}
	}

	private void setAlarmUI(boolean isEnabled) {
		if (isEnabled) {
			myAlarmImage.setBackgroundResource(R.drawable.tvch_icon_alarm_on);
			myAlarmButton.close();
		}
		else {
			myAlarmButton.open();
			myAlarmImage.setBackgroundResource(R.drawable.tvch_icon_alarm_off);			
		}
	}
	
	private void setRecordingUI(boolean isEnabled) {
		//isSetUI = true;
		
		if (isEnabled) {
			recordReserveImage.setBackgroundResource(R.drawable.set_icon_record_on);
			recordReserveButton.close();
		}
		else {
			recordReserveButton.open();
			recordReserveImage.setBackgroundResource(R.drawable.set_icon_record_off);			
		}
	}
	
	// 녹화 예약(true)/취소(false) 처리 
	private void reserveRecording(final boolean isRecording) {

		Log.d(DEBUG_TAG, "reserveRecording: "+isRecording);

		final Boolean isNow = elapseTime(programInfo.getTime());

		if (!programInfo.getSeriesId().contains("NULL") && !isNow) { // 시리즈 녹화

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
			}
			else {
				if (isNow) {
					title.setText("녹화 취소");
				}
				else {
					title.setText("녹화 예약 취소");					
				}
				button1.setText("이번 회차만 녹화 취소하기");
				button2.setText("이번 회부터 전체 녹화 취소하기");
			}
			rec_dialog.setCustomTitle(title);
			rec_dialog.setNegativeButton("취소", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.dismiss();
					
					isSetUI = true;
					setRecordingUI( !isRecording );
				}
			} )
			.setView(layout);

			final AlertDialog recordingDlg = rec_dialog.create();

			button1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if (isRecording) {
						if (isNow) {
							if ( !sendShortRecordNow() )
								showRecordFailMessage();
						}
						else {
							if ( !sendShortRecordReserve() )
								showRecordFailMessage();								
						}
					}
					else
						sendShortRecordReserveCancel();
					
					recordingDlg.dismiss();
				}
			});

			button2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if (isRecording) {
						if (isNow) {
							if ( !sendShortRecordNow() )
								showRecordFailMessage();
							else {
								if ( !sendSeriesRecordReserve() )
									showRecordFailMessage();								
							}
						}
						else {
							if ( !sendSeriesRecordReserve() )
								showRecordFailMessage();
						}
					}
					else
						sendSeriesRecordReserveCancel();
					
					recordingDlg.dismiss();
				}
			});

			recordingDlg.show();
			
		}else {//단편
			if (isRecording) {
				if (isNow) {
					if ( !sendShortRecordNow() )
						showRecordFailMessage();								
				}
				else {
					if ( !sendShortRecordReserve() )
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
						isSetUI = true;
						setRecordingUI( !isRecording );
					}
				});
				AlertDialog alert = alert_internet_status.create();
				alert.show();
			}
		}

	}
		
	private void setButtonOnClickListner() {
		
		Log.d(DEBUG_TAG, "setButtonOnClickListner()");
		// my program 
		myProgramButton.setOnCheckChangedListner(new SlideButton.OnCheckChangedListner() {
			@Override
			public void onCheckChanged(View v, boolean isChecked) {
				

				/*if (isChecked) {
						holder.Mych_Icon.setVisibility(View.VISIBLE);
						if (tempTVch_Check.get(mList.get(tempPosition).getID()) == null) {
							TVCH_CheckList temp = new TVCH_CheckList(mList.get(tempPosition).getID());
							temp.setMyChannel(true);
							tempApp.insertTask(temp);
						} else {
							tempTVch_Check.get(mList.get(tempPosition).getID()).setMyChannel(true);
							tempApp.updateTask(tempTVch_Check.get(mList.get(tempPosition).getID()));
						}
					} else {
						holder.Mych_Icon.setVisibility(View.GONE);
						if (tempTVch_Check.get(mList.get(tempPosition).getID()) != null) {
							tempTVch_Check.get(mList.get(tempPosition).getID()).setMyChannel(false);
							tempApp.updateTask(tempTVch_Check.get(mList.get(tempPosition).getID()));
						}
					}*/
				theApplication.ButtonBeepPlay();
				
				if (isChecked) {
					Log.d(DEBUG_TAG, "My Program is checked.");
					myProgramImage.setBackgroundResource(R.drawable.icon_myprogram);
					if (myProgramList.get(programInfo.getID()) == null) {
						TVCH_PR_CheckList temp = new TVCH_PR_CheckList(programInfo.getID());
						temp.setMyProgram(true);
						theApplication.insertPRTask(temp);
					} else {
						myProgramList.get(programInfo.getID()).setMyProgram(true);
						//tempApp.deletePRTasks(mSelectData.getID());
						theApplication.deletePRTasks(programInfo.getID());//(tempTVPr_Check.get(mSelectData.getID()));
					}
				} else {
					Log.d(DEBUG_TAG, "My Program is unchecked.");
					myProgramImage.setBackgroundResource(R.drawable.icon_myprogram_unselect);
					if (myProgramList.get(programInfo.getID()) != null) {
						myProgramList.get(programInfo.getID()).setMyProgram(false);
						//tempApp.updatePRTask(tempTVPr_Check.get(mSelectData.getID()));
						theApplication.deletePRTasks(programInfo.getID());
						//tempApp.updatePRTask(tempTVPr_Check.get(mSelectData.getID()));
					}
				}


			}
		});

		myProgramText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (myProgramButton.isChecked()) {
					myProgramButton.setChecked(false);
				} else {
					myProgramButton.setChecked(true);
				}	
			}
		});

		// Alarm
		myAlarmButton.setOnCheckChangedListner(new SlideButton.OnCheckChangedListner() {
			@Override
			public void onCheckChanged(View v, boolean isChecked) {
				
				Intent intent =  new Intent(CNM_Alarm_Service_ACTION);
				Intent intent2=new Intent(CNM_AlarmDelete_Service_ACTION);
				//int tmepID =Integer.parseInt(mSelectData.getSeq());
				theApplication.ButtonBeepPlay();
				//intent.putExtra(CNM_Alarm_EXCUTE, mSelectData.getTitle());
				//intent.putExtra(CNM_Alarm_Seq, mSelectData.getSeq());
				if (isChecked) {
					if (myAlarmList.get(programInfo.getSeq()) == null) {
						TVCH_Alarm_CheckList temp = new TVCH_Alarm_CheckList(programInfo.getSeq());
						temp.setDB_PROGRAM_TITLE(programInfo.getTitle());
						temp.setDB_PROGRAM_CH_NUMBER(channelNumber);
						temp.setDB_PROGRAM_CH_NANE(channelName);
						temp.setDB_CH_URL(channelLogoURL);
						temp.setDB_ALARM_CHECK(true);
						temp.setDB_PROGRAM_ID(programInfo.getID());

						temp.setDB_ALARM_TIME(programInfo.getTime());
						temp.setDB_ALARM_TIME_TEXT(programInfo.getAlarm_time());
						temp.setDB_ALARM_HD(programInfo.getHD());
						temp.setDB_PROGRAM_CH_GRADE(programInfo.getGrade());

						myAlarmImage.setBackgroundResource(R.drawable.tvch_icon_alarm_on);

						//tempApp.getTVAl_CheckArr().add(temp);
						//tempApp.getTVAl_CheckMap().put(temp.getDB_PROGRAM_ID(), temp);
						theApplication.insertAlarmTask(temp);
						Log.d(DEBUG_TAG,"My Alarm Added("+myAlarmList.size()+"): "+temp);


						//GregorianCalendar mCalendar = new GregorianCalendar();
						//intent.putExtra(CNM_Alarm_Seq, mSelectData.getSeq());	

						String time=setNoti(programInfo.getTime());

						Log.d("TEST","time = "+time);

						//intent.putExtra(Alarm_Before, time);

						//intent.putExtra("1", 3);

						String[] szbuf=new String[4];

						szbuf[0]=programInfo.getTitle();
						szbuf[1]=programInfo.getSeq();
						szbuf[2]=time;
						szbuf[3]="TEST";

						intent.putExtra("2", szbuf);


						mAlarmManager.set(AlarmManager.RTC_WAKEUP, AlarmTime(programInfo.getTime()), PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(programInfo.getSeq()), intent, PendingIntent.FLAG_UPDATE_CURRENT));





						intent2.putExtra("2", szbuf);

						mAlarmManager.set(AlarmManager.RTC_WAKEUP, getTime(programInfo.getTime()), PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(programInfo.getSeq()), intent2, PendingIntent.FLAG_UPDATE_CURRENT));





					}else{
						//tempTVAl_Check.get(mSelectData.getSeq()).setDB_SEQ(mSelectData.getSeq());
						//tempApp.updatePRTask(tempTVPr_Check.get(mSelectData.getID()));

						theApplication.deleteAlarm(programInfo.getSeq());
						theApplication.getTVAl_CheckMap().remove(programInfo.getSeq());
						myAlarmImage.setBackgroundResource(R.drawable.tvch_icon_alarm_off);
						mAlarmManager.cancel(PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(programInfo.getSeq()), intent, PendingIntent.FLAG_UPDATE_CURRENT));
						mAlarmManager.cancel(PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(programInfo.getSeq()), intent2, PendingIntent.FLAG_UPDATE_CURRENT));

					}
				}else{
					theApplication.deleteAlarm(programInfo.getSeq());
					theApplication.getTVAl_CheckMap().remove(programInfo.getSeq());
					myAlarmImage.setBackgroundResource(R.drawable.tvch_icon_alarm_off);
					mAlarmManager.cancel(PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(programInfo.getSeq()), intent, PendingIntent.FLAG_UPDATE_CURRENT));
					mAlarmManager.cancel(PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(programInfo.getSeq()), intent2, PendingIntent.FLAG_UPDATE_CURRENT));
				}
			}
		});

		myAlarmText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (myAlarmButton.isChecked()) {
					myAlarmButton.setChecked(false);
				} else {
					myAlarmButton.setChecked(true);
				}	
			}
		});


		// 녹화 스위치
		recordReserveButton.setOnCheckChangedListner(new SlideButton.OnCheckChangedListner() {

			@Override
			public void onCheckChanged(View v, boolean isChecked) {		
				if (isSetUI) {
					isSetUI = false;
					Log.d(DEBUG_TAG, "Only UI");
				}
				else {		
					if (theApplication.getSettopRegAuth()) {
						theApplication.ButtonBeepPlay();
						reserveRecording(isChecked);
					}
					else {
						theApplication.getMainrbFourth().performClick();
					}
				}
			}
		});


		recordReserveText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//mOneClick = true;
				if (recordReserveButton.isChecked()) {
					recordReserveButton.setChecked(false);
				} else {
					recordReserveButton.setChecked(true);
				}
			}
		});
		
		recordReserveButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				//mOneClick = true;
				return false;
			}
		});
	}

	// 이번 회차만 즉시 녹화하기
	private Boolean sendShortRecordNow() {
		String requestURL = XML_Address_Define.Record.getSetrecordseries(Udid, channelID, programInfo.getTime());
		Log.d(DEBUG_TAG, "### URL Request 이번 회차만 즉시 녹화: " + requestURL);
		CHECK_RESPONSE_PASER.start(requestURL);

		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			setRecordingUI(true);
			Log.d(DEBUG_TAG, "즉시 단편 녹화 성공");
			return true;
		}
		else {
			Log.d(DEBUG_TAG, "즉시 단편 녹화 실패: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			isSetUI = true;
			setRecordingUI(false);

			return false;
			//showRecordFailMessage();
			//mOneClick=false;
			//recordReserveButton.setChecked(false);
		}
	}
	
	/*
	// 이번 회차부터 전체 즉시 녹화하기
	private void sendSeriesRecordNow() {
		String requestURL = XML_Address_Define.Record.getSetrecordseries( Udid, channelID, programInfo.getTime(), programInfo.getSeriesId());
		Log.d(DEBUG_TAG, "### URL Request 시리즈 전체 즉시 녹화: " + requestURL);
		CHECK_RESPONSE_PASER.start(requestURL);
		
		response_DATA = CHECK_RESPONSE_PASER.getArrayList();

		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			Log.d("Sangtae", "시리즈 즉시 녹화 성공");
			setRecordingUI(true);
		}else {
			Log.d("Sangtae", "시리즈 즉시 녹화 실패: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			isSetUI = true;
			setRecordingUI(false);
			
			showRecordFailMessage();
			//mOneClick=false;
			//recordReserveButton.setChecked(false);
		}
	}
	*/
	
	// 이번 회차만 녹화 예약하기
	private Boolean sendShortRecordReserve() {
		String requestURL = XML_Address_Define.Record.getSetrecordreserve(Udid, channelID, programInfo.getTime());
		Log.d(DEBUG_TAG, "### URL Request 이번 회차만 녹화예약: " + requestURL);
		CHECK_RESPONSE_PASER.start(requestURL);

		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			setRecordingUI(true);
			Log.d("Sangtae", "단편 녹화 성공");
			
			return true;
		}
		else {
			Log.d("Sangtae", "단편 녹화 실패: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			isSetUI = true;
			setRecordingUI(false);

			return false;
			//showRecordFailMessage();
			//mOneClick=false;
			//recordReserveButton.setChecked(false);
		}
	}

	private void showRecordFailMessage() {
		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
				getParent().getParent());
		if ( elapseTime(programInfo.getTime()) )
			alert_internet_status.setTitle("녹화 불가");
		else 
			alert_internet_status.setTitle("녹화 예약 불가");
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
	/*
	 * 			public static final String	ERROR_001	= "001";			//Mac불일치(Invalid MacAddress)
			public static final String	ERROR_002	= "002";			
			public static final String	ERROR_003	= "003";			
			public static final String	ERROR_004	= "004";			//튜너를 모두사용하고 있어 녹화/튜닝불가 (Authentication Failure)
			public static final String	ERROR_005	= "005";			
			public static final String	ERROR_006	= "006";			//이미 녹화 예약됨(Already Recording Reserve Done)
			public static final String	ERROR_007	= "007";			//프로그램 정보가 없음(Program Not Found)
			public static final String	ERROR_008	= "008";			//재생중인 녹화물 삭제불가( Recording Delete Error)
			public static final String	ERROR_009	= "009";			
			public static final String	ERROR_010	= "010";			
			public static final String	ERROR_011	= "011";			
			public static final String	ERROR_012	= "012";			
			public static final String	ERROR_013	= "013";			
			public static final String	ERROR_014	= "014";			//대기 모드(Hold Mode)
			public static final String	ERROR_015	= "015";			
			public static final String	ERROR_016	= "016";			//삭제 오류 (Delete Processing Error)
			public static final String	ERROR_017	= "017";			//이름 변경 오류 (Name Replace Error)
			public static final String	ERROR_018	= "018";			//VOD상세 화면 띄우기 오류 (VOD DetailView Error)
			public static final String	ERROR_019	= "019";			//개인미디어 재생중 (Private Media Playing)
			public static final String	ERROR_020	= "020";			//독립형(데이터 서비스) 실행중 (Already Playing DataService )
			public static final String	ERROR_021	= "021";			//VOD재생중 (VOD Playing)

	 */
	private String getRecordFailMessage() {
		String resultCode = response_DATA.getResultCode();
		String errorMessage = null;
		
		if (resultCode.contains("002")) { //중복 녹화 예약요청 (Duplicated Recording Reserve Request)
			errorMessage = "중복되는 시간에 다른 프로그램의 녹화 예약이 설정되어 있습니다.";
		}
		else if (resultCode.contains("003")) { //디스크 용량부족(Disk Capacity Not Enough)
			errorMessage =  "셋탑의 저장공간이 부족합니다. 녹화물 목록을 확인해주세요.";
		}
		else if (resultCode.contains("005")) { //녹화 불가 채널(Unsupported Recording Channel)
			errorMessage =  "고객님의 셋탑에서 제공되지 않는 상품 또는 지역의 채널입니다. [설정/안내]에서 확인해주세요.";
		}
		else if (resultCode.contains("005-1")
				|| resultCode.contains("010")) { //PIP사용중(Using PIP Service)
			errorMessage =  "셋탑에서 동시화면 기능을 사용중인 경우 원격으로는 즉시녹화가 불가합니다.";
		}
		else if (resultCode.contains("011") //다른채널 녹화중(Already Other Channel Recording)
				|| resultCode.contains("015")) { //이미 녹화중 (Already Recording)
			errorMessage =  "셋탑에서 다른 채널이 녹화중인 경우 원격으로는 즉시녹화가 불가합니다.";
		}
		else if (resultCode.contains("009")		//채널 없음(Channel Not Found)
				|| resultCode.contains("012")	//시청제한 채널(LimitedView Channel)
				|| resultCode.contains("013")	//제한 채널(Limited Channel)
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
	
	// 이번 회차부터 전체 녹화 예약하기
	private Boolean sendSeriesRecordReserve() {
		String requestURL = XML_Address_Define.Record.getSetrecordseriesreserve( Udid, channelID, programInfo.getTime(), programInfo.getSeriesId());
		Log.d(DEBUG_TAG, "### URL Request 시리즈 전체 녹화예약: " + requestURL);
		CHECK_RESPONSE_PASER.start(requestURL);
		
		response_DATA = CHECK_RESPONSE_PASER.getArrayList();

		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			Log.d("Sangtae", "시리즈 예약 성공");
			setRecordingUI(true);
			
			return true;
		}else {
			Log.d("Sangtae", "시리즈 예약 실패: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			isSetUI = true;
			setRecordingUI(false);
			
			return false;
			//showRecordFailMessage();
			//mOneClick=false;
			//recordReserveButton.setChecked(false);
		}
	}

	// 이번 회차만 녹화 취소하기
	private Boolean sendShortRecordReserveCancel() {
		
		String requestURL;
		if (programInfo.getSeriesId().length() <= 0) {
			requestURL = XML_Address_Define.Record.getSetrecordcancelreserve(Udid, channelID, programInfo.getTime(), "NULL", XML_Address_Define.Record.RECORD_DEL_ONE);
		}else {
			requestURL = XML_Address_Define.Record.getSetrecordcancelreserve(Udid, channelID, programInfo.getTime(), programInfo.getSeriesId(), XML_Address_Define.Record.RECORD_DEL_ONE);
		}
		Log.d(DEBUG_TAG, "### URL Request 이번 회만 녹화취소: " + requestURL);
		
		CHECK_RESPONSE_PASER.start(requestURL);
		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			Log.d("Sangtae", "단편 취소 성공");
			setRecordingUI(false);
			
			return true;
		}else {
			Log.d("Sangtae", "단편 취소 실패: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			//mOneClick=false;
			isSetUI = true;
			setRecordingUI(true);
			
			return false;
			//showRecordFailMessage();
			//recordReserveButton.setChecked(true);
			
		}
	}
	
	// 이번 회차부터 전체 녹화 취소하기
	private Boolean sendSeriesRecordReserveCancel() {
		String requestURL = XML_Address_Define.Record.getSetrecordcancelreserve(Udid, channelID, programInfo.getTime(), programInfo.getSeriesId(), XML_Address_Define.Record.RECORD_DEL_ALL);
		Log.d(DEBUG_TAG, "### URL Request 시리즈 전체 녹화취소: " + requestURL);
		
		CHECK_RESPONSE_PASER.start(requestURL);
		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			Log.d("Sangtae", "시리즈 취소 성공");
			setRecordingUI(false);
			
			return true;
		}else {
			Log.d("Sangtae", "시리즈 취소 실패: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			//mOneClick=false;
			isSetUI = true;
			setRecordingUI(true);
			
			return false;
			//showRecordFailMessage();
			//recordReserveButton.setChecked(true);
		}

	}



	private long AlarmTime(String BroadCatTime){

		long time = getRemineTime(BroadCatTime);
		if(time < 300000)
			return System.currentTimeMillis();

		return System.currentTimeMillis()+time-300000;
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
}

