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

	private final String Class7		=	"7��";// ������";
	private final String Class12	=	"12��";//�̻�";
	private final String Class15	=	"15��";//�̻�";
	private final String Class19	=	"19��";//�̻�";
	private final String ClassALL	=	"��ü";// ������";

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
	
	// �˶� �޴���
	private AlarmManager mAlarmManager;
	
	// ä�� ����
	//private String TVCH_Select;
	private String channelID;
	private String channelNumber;	// ä�� ��ȣ - Ch 5
	private String channelLogoURL;		// ä�� �ΰ� URL
	private String channelName;	// ä�� �̸� - SBS_HD
	
	// ���α׷� ����
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
	
	// ä�� ���� �����ϱ�
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

	// ���α׷� ���� �����ϱ�
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
		
		NaviBtn_Singleton.getInstance().getNaviHeaderText().setText("�󼼺���");
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

	// ���α׷� ��� ǥ��
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
	
	// HD ��� ���� ǥ��
	private void setupProgramDefinitionUI() {
		ImageView myHDIV = (ImageView)findViewById(R.id.icon_hd);

		if (programInfo.getHD()) {
			myHDIV.setVisibility(View.VISIBLE);
		} else {
			myHDIV.setVisibility(View.GONE);
		}	
	}

	// ���� ���α׷� �ʱ� ���� ǥ�� 
	private void setupMyProgramUI() {
		myProgramImage	= (ImageView)findViewById(R.id.myprogram_icon);
		myProgramButton	= (SlideButton)findViewById(R.id.myProTglBtn);

		// �� ���α׷��� ���� ���α׷����� üũ�Ѵ�. 
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
			if (theApplication.getmRecord_DATA_LIST() != null) {	// ��ȭ ������ üũ
				if (theApplication.getmRecord_DATA_LIST().size() > 0) {
					for (int i = 0; i < theApplication.getmRecord_DATA_LIST().size(); i++) {
						if (theApplication.getmRecord_DATA_LIST().get(i).getChannelId().contains(channelID) && theApplication.getmRecord_DATA_LIST().get(i).getRecordStartTime().contains(programInfo.getTime())) {	// ���� ��ȭ �� üũ
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
		} else {	// ä�� ����� ��ȭ ����
			recordLayout.setVisibility(View.GONE);
			Care_MsgImg.setVisibility(View.VISIBLE);
		}
	}
	
	private void setupAlarmUI(boolean isVisible) {

		RelativeLayout alarmLayout = (RelativeLayout)findViewById(R.id.sch_alarm_layout);
		
		if (isVisible) {
			alarmLayout.setVisibility(View.VISIBLE);

			// �� ���α׷��� �˶� ������ �Ǿ� �ִ��� üũ�Ѵ�.
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
		
		// ä�� ����
		( (TextView)findViewById(R.id.Channel_Number) ).setText(channelNumber);
		( (TextView)findViewById(R.id.Copany) ).setText(channelName);
		( (TextView)findViewById(R.id.Copany) ).setSelected(true);
		
		if (channelLogoURL != null) {
			TVCHImageDownloader imageDownloader = new TVCHImageDownloader(getApplicationContext());
			imageDownloader.setContext(getApplicationContext());
			imageDownloader.download(channelLogoURL, ((ImageView)findViewById(R.id.Company_Log)));//, BitmapFactory.decodeResource(getResources(), R.drawable.noimg_logo));
		}
		
		// ���α׷� ����
		TextView  programTitleTextView = (TextView)findViewById(R.id.schedule_set_subject);
		programTitleTextView.setText(programInfo.getTitle());
		programTitleTextView.setSelected(true);
		setupProgramGradeUI();		
		setupProgramDefinitionUI();

		//level 3
		// ���α׷� �濵 �ð�
		TextView 	programTimeTextView	= (TextView)findViewById(R.id.schedule_set_time);
		programTimeTextView.setText(programInfo.getAlarm_time());
		
		// My Program
		setupMyProgramUI();
		myProgramText	= (TextView)findViewById(R.id.mypro_textview);

		// �˶� ����
		myAlarmImage	= (ImageView)findViewById(R.id.myalarm_icon);
		myAlarmButton	= (SlideButton)findViewById(R.id.myAlarmTglBtn);
		myAlarmText	= (TextView)findViewById(R.id.alarm_textview);

		String recordingText;		
		if ( elapseTime(programInfo.getTime()) ) {
			// ���� �濵 ���� ���
			recordingText = "��� ��ȭ";
			setupAlarmUI(false);
		}
		else {
			recordingText = "��ȭ ����";
			setupAlarmUI(true);
		}
		
		// ��ȭ
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
	
	// ��ȭ ����(true)/���(false) ó�� 
	private void reserveRecording(final boolean isRecording) {

		Log.d(DEBUG_TAG, "reserveRecording: "+isRecording);

		final Boolean isNow = elapseTime(programInfo.getTime());

		if (!programInfo.getSeriesId().contains("NULL") && !isNow) { // �ø��� ��ȭ

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
					title.setText("��� ��ȭ");					
					button1.setText("�̹� ȸ���� ��� ��ȭ�ϱ�");
					button2.setText("�̹� ȸ���� ��� ��ü ��ȭ�ϱ�");
				}
				else {
					title.setText("��ȭ ����");
					button1.setText("�̹� ȸ���� ��ȭ �����ϱ�");
					button2.setText("�̹� ȸ���� ��ü ��ȭ �����ϱ�");
				}
			}
			else {
				if (isNow) {
					title.setText("��ȭ ���");
				}
				else {
					title.setText("��ȭ ���� ���");					
				}
				button1.setText("�̹� ȸ���� ��ȭ ����ϱ�");
				button2.setText("�̹� ȸ���� ��ü ��ȭ ����ϱ�");
			}
			rec_dialog.setCustomTitle(title);
			rec_dialog.setNegativeButton("���", new android.content.DialogInterface.OnClickListener() {
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
			
		}else {//����
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
				alert_internet_status.setTitle( (isNow) ? "��ȭ ����" : "��ȭ ���� ���");
				alert_internet_status.setMessage(((isNow) ? "��ȭ�� ����" : "��ȭ ������ ���")+"�Ͻðڽ��ϱ�?");
				alert_internet_status.setPositiveButton("��",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						sendShortRecordReserveCancel();
						dialog.dismiss(); // �ݱ�
					}
				});
				alert_internet_status.setNegativeButton("�ƴϿ�",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss(); // �ݱ�
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


		// ��ȭ ����ġ
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

	// �̹� ȸ���� ��� ��ȭ�ϱ�
	private Boolean sendShortRecordNow() {
		String requestURL = XML_Address_Define.Record.getSetrecordseries(Udid, channelID, programInfo.getTime());
		Log.d(DEBUG_TAG, "### URL Request �̹� ȸ���� ��� ��ȭ: " + requestURL);
		CHECK_RESPONSE_PASER.start(requestURL);

		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			setRecordingUI(true);
			Log.d(DEBUG_TAG, "��� ���� ��ȭ ����");
			return true;
		}
		else {
			Log.d(DEBUG_TAG, "��� ���� ��ȭ ����: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			isSetUI = true;
			setRecordingUI(false);

			return false;
			//showRecordFailMessage();
			//mOneClick=false;
			//recordReserveButton.setChecked(false);
		}
	}
	
	/*
	// �̹� ȸ������ ��ü ��� ��ȭ�ϱ�
	private void sendSeriesRecordNow() {
		String requestURL = XML_Address_Define.Record.getSetrecordseries( Udid, channelID, programInfo.getTime(), programInfo.getSeriesId());
		Log.d(DEBUG_TAG, "### URL Request �ø��� ��ü ��� ��ȭ: " + requestURL);
		CHECK_RESPONSE_PASER.start(requestURL);
		
		response_DATA = CHECK_RESPONSE_PASER.getArrayList();

		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			Log.d("Sangtae", "�ø��� ��� ��ȭ ����");
			setRecordingUI(true);
		}else {
			Log.d("Sangtae", "�ø��� ��� ��ȭ ����: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			isSetUI = true;
			setRecordingUI(false);
			
			showRecordFailMessage();
			//mOneClick=false;
			//recordReserveButton.setChecked(false);
		}
	}
	*/
	
	// �̹� ȸ���� ��ȭ �����ϱ�
	private Boolean sendShortRecordReserve() {
		String requestURL = XML_Address_Define.Record.getSetrecordreserve(Udid, channelID, programInfo.getTime());
		Log.d(DEBUG_TAG, "### URL Request �̹� ȸ���� ��ȭ����: " + requestURL);
		CHECK_RESPONSE_PASER.start(requestURL);

		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			setRecordingUI(true);
			Log.d("Sangtae", "���� ��ȭ ����");
			
			return true;
		}
		else {
			Log.d("Sangtae", "���� ��ȭ ����: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
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
			alert_internet_status.setTitle("��ȭ �Ұ�");
		else 
			alert_internet_status.setTitle("��ȭ ���� �Ұ�");
		alert_internet_status.setMessage(getRecordFailMessage());
		alert_internet_status.setPositiveButton("Ȯ��",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				sendShortRecordReserveCancel();
				dialog.dismiss(); // �ݱ�
			}
		});
		AlertDialog alert = alert_internet_status.create();
		alert.show();		
	}
	/*
	 * 			public static final String	ERROR_001	= "001";			//Mac����ġ(Invalid MacAddress)
			public static final String	ERROR_002	= "002";			
			public static final String	ERROR_003	= "003";			
			public static final String	ERROR_004	= "004";			//Ʃ�ʸ� ��λ���ϰ� �־� ��ȭ/Ʃ�׺Ұ� (Authentication Failure)
			public static final String	ERROR_005	= "005";			
			public static final String	ERROR_006	= "006";			//�̹� ��ȭ �����(Already Recording Reserve Done)
			public static final String	ERROR_007	= "007";			//���α׷� ������ ����(Program Not Found)
			public static final String	ERROR_008	= "008";			//������� ��ȭ�� �����Ұ�( Recording Delete Error)
			public static final String	ERROR_009	= "009";			
			public static final String	ERROR_010	= "010";			
			public static final String	ERROR_011	= "011";			
			public static final String	ERROR_012	= "012";			
			public static final String	ERROR_013	= "013";			
			public static final String	ERROR_014	= "014";			//��� ���(Hold Mode)
			public static final String	ERROR_015	= "015";			
			public static final String	ERROR_016	= "016";			//���� ���� (Delete Processing Error)
			public static final String	ERROR_017	= "017";			//�̸� ���� ���� (Name Replace Error)
			public static final String	ERROR_018	= "018";			//VOD�� ȭ�� ���� ���� (VOD DetailView Error)
			public static final String	ERROR_019	= "019";			//���ι̵�� ����� (Private Media Playing)
			public static final String	ERROR_020	= "020";			//������(������ ����) ������ (Already Playing DataService )
			public static final String	ERROR_021	= "021";			//VOD����� (VOD Playing)

	 */
	private String getRecordFailMessage() {
		String resultCode = response_DATA.getResultCode();
		String errorMessage = null;
		
		if (resultCode.contains("002")) { //�ߺ� ��ȭ �����û (Duplicated Recording Reserve Request)
			errorMessage = "�ߺ��Ǵ� �ð��� �ٸ� ���α׷��� ��ȭ ������ �����Ǿ� �ֽ��ϴ�.";
		}
		else if (resultCode.contains("003")) { //��ũ �뷮����(Disk Capacity Not Enough)
			errorMessage =  "��ž�� ��������� �����մϴ�. ��ȭ�� ����� Ȯ�����ּ���.";
		}
		else if (resultCode.contains("005")) { //��ȭ �Ұ� ä��(Unsupported Recording Channel)
			errorMessage =  "������ ��ž���� �������� �ʴ� ��ǰ �Ǵ� ������ ä���Դϴ�. [����/�ȳ�]���� Ȯ�����ּ���.";
		}
		else if (resultCode.contains("005-1")
				|| resultCode.contains("010")) { //PIP�����(Using PIP Service)
			errorMessage =  "��ž���� ����ȭ�� ����� ������� ��� �������δ� ��ó�ȭ�� �Ұ��մϴ�.";
		}
		else if (resultCode.contains("011") //�ٸ�ä�� ��ȭ��(Already Other Channel Recording)
				|| resultCode.contains("015")) { //�̹� ��ȭ�� (Already Recording)
			errorMessage =  "��ž���� �ٸ� ä���� ��ȭ���� ��� �������δ� ��ó�ȭ�� �Ұ��մϴ�.";
		}
		else if (resultCode.contains("009")		//ä�� ����(Channel Not Found)
				|| resultCode.contains("012")	//��û���� ä��(LimitedView Channel)
				|| resultCode.contains("013")	//���� ä��(Limited Channel)
				|| resultCode.contains("023")
				|| resultCode.contains("024")
				) {
			errorMessage =  "��ȭ �� �� ���� ä�� �Դϴ�.\n��ǰ���� �Ǵ� ��û���Ѽ����� Ȯ�����ּ���.";
		}		
		else if (resultCode.contains("028")
				|| resultCode.contains("206")){
			errorMessage =  "��ž �� ������\n �����ִ� �����̰ų�\n ����� ���� ����\n ������ �Ұ��� �մϴ�.";
		}
		else {
			errorMessage = "ErrorCode: " + resultCode + "\n" + response_DATA.getErrorString();
		}
		
		return errorMessage;
	}
	
	// �̹� ȸ������ ��ü ��ȭ �����ϱ�
	private Boolean sendSeriesRecordReserve() {
		String requestURL = XML_Address_Define.Record.getSetrecordseriesreserve( Udid, channelID, programInfo.getTime(), programInfo.getSeriesId());
		Log.d(DEBUG_TAG, "### URL Request �ø��� ��ü ��ȭ����: " + requestURL);
		CHECK_RESPONSE_PASER.start(requestURL);
		
		response_DATA = CHECK_RESPONSE_PASER.getArrayList();

		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			Log.d("Sangtae", "�ø��� ���� ����");
			setRecordingUI(true);
			
			return true;
		}else {
			Log.d("Sangtae", "�ø��� ���� ����: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			isSetUI = true;
			setRecordingUI(false);
			
			return false;
			//showRecordFailMessage();
			//mOneClick=false;
			//recordReserveButton.setChecked(false);
		}
	}

	// �̹� ȸ���� ��ȭ ����ϱ�
	private Boolean sendShortRecordReserveCancel() {
		
		String requestURL;
		if (programInfo.getSeriesId().length() <= 0) {
			requestURL = XML_Address_Define.Record.getSetrecordcancelreserve(Udid, channelID, programInfo.getTime(), "NULL", XML_Address_Define.Record.RECORD_DEL_ONE);
		}else {
			requestURL = XML_Address_Define.Record.getSetrecordcancelreserve(Udid, channelID, programInfo.getTime(), programInfo.getSeriesId(), XML_Address_Define.Record.RECORD_DEL_ONE);
		}
		Log.d(DEBUG_TAG, "### URL Request �̹� ȸ�� ��ȭ���: " + requestURL);
		
		CHECK_RESPONSE_PASER.start(requestURL);
		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			Log.d("Sangtae", "���� ��� ����");
			setRecordingUI(false);
			
			return true;
		}else {
			Log.d("Sangtae", "���� ��� ����: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			//mOneClick=false;
			isSetUI = true;
			setRecordingUI(true);
			
			return false;
			//showRecordFailMessage();
			//recordReserveButton.setChecked(true);
			
		}
	}
	
	// �̹� ȸ������ ��ü ��ȭ ����ϱ�
	private Boolean sendSeriesRecordReserveCancel() {
		String requestURL = XML_Address_Define.Record.getSetrecordcancelreserve(Udid, channelID, programInfo.getTime(), programInfo.getSeriesId(), XML_Address_Define.Record.RECORD_DEL_ALL);
		Log.d(DEBUG_TAG, "### URL Request �ø��� ��ü ��ȭ���: " + requestURL);
		
		CHECK_RESPONSE_PASER.start(requestURL);
		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			Log.d("Sangtae", "�ø��� ��� ����");
			setRecordingUI(false);
			
			return true;
		}else {
			Log.d("Sangtae", "�ø��� ��� ����: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
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
		//SimpleDateFormat beforeCvtTime = new SimpleDateFormat("MM�� dd�� (E) HH:mm ~ ");

		ParsePosition pos = new ParsePosition(0);
		Date onriginTime = mOriTime.parse(BroadCatTime, pos);
		long startTime=onriginTime.getTime();
		//������ �ð� ����	
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

