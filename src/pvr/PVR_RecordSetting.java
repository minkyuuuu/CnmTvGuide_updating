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

	private final String Class7		=	"7��";// ������";
	private final String Class12	=	"12��";//�̻�";
	private final String Class15	=	"15��";//�̻�";
	private final String Class19	=	"19��";//�̻�";
	private final String ClassALL	=	"��ü";// ������";

	private CNMApplication tempApp;
	private String Udid;

	// ä�� ����
	//private String TVCH_Select;

	// ���α׷� ����
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
				//Toast.makeText(mContext, "����", Toast.LENGTH_SHORT).show();

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
		Log.d("Sangtae", "Ÿ��Ʋ ���� �Է�: "+str);

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
			tempTitle = "�󼼺���";
			mOneLayout.setVisibility(View.GONE);
			mTwoLayout.setVisibility(View.GONE);
			mSlideTitle.setText("��ȭ ����");
		} else {
			tempTitle = "��ȭ�� �󼼺���";
			mOneLayout.setVisibility(View.VISIBLE);
			mTwoLayout.setVisibility(View.VISIBLE);
			mSlideTitle.setText("��ȭ�� ��ȣ");

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

	// ���α׷� ��� ǥ��
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

	// HD ��� ���� ǥ��
	private void setupProgramDefinitionUI() {
		ImageView myHDIV = (ImageView)findViewById(R.id.icon_hd);

		if (mData.getRecordHD()) {
			myHDIV.setVisibility(View.VISIBLE);
		} else {
			myHDIV.setVisibility(View.GONE);
		}	
	}


	private void setupUI() {

		// ä�� ����
		( (TextView)findViewById(R.id.Channel_Number) ).setText(String.format("Ch %s", mData.getChannelNo()));
		( (TextView)findViewById(R.id.Copany) ).setText(mData.getChannelName());
		if (mData.getChannel_logo_img() != null) {
			TVCHImageDownloader imageDownloader = new TVCHImageDownloader(getApplicationContext());
			imageDownloader.setContext(getApplicationContext());
			imageDownloader.download(mData.getChannel_logo_img(), ((ImageView)findViewById(R.id.Company_Log)));//, BitmapFactory.decodeResource(getResources(), R.drawable.noimg_logo));
		}

		// ���α׷� ����
		TextView  programTitleTextView = (TextView)findViewById(R.id.schedule_set_subject);
		programTitleTextView.setText(mData.getProgramName());
		programTitleTextView.setSelected(true);
		setupProgramGradeUI();		
		setupProgramDefinitionUI();

		//level 3
		// ���α׷� �濵 �ð�
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
						"�ִ� 12�� �̳�", PVR_RecordSetting.this).show();
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
					if (Record_Mode.equals(Reserve_Type)) {	// ���� ��ȭ
						if (mData.getRecordStatus()) {
							/*if (mRecordSlideBtn.isChecked()) {
								Log.d("Sangtae", "��� ��ȭ ���");
								//sendRespone(XML_Address_Define.Record.getSetrecordstop(Udid, mData.getChannelId()));
								reserveRecording(true);
							} else {
								Log.d("Sangtae", "��� ��ȭ ����");
								//sendRespone(XML_Address_Define.Record.getSetrecord(Udid, mData.getChannelId()));
							}*/
							reserveRecording(!mRecordSlideBtn.isChecked());

						} else {
							/*if (mRecordSlideBtn.isChecked()) {
								Log.d("Sangtae", "�����ȭ ���");
								//sendRespone(XML_Address_Define.Record.getSetrecordcancelreserve(Udid, mData.getChannelId(), mData.getRecordStartTime(), mData.getSeriesId(), "2"));
								reserveRecording(true);
							} else {
								Log.d("Sangtae", "�����ȭ ����");
								reserveRecording(false);
								//sendRespone(XML_Address_Define.Record.getSetrecordreserve(Udid, mData.getChannelId(), mData.getRecordStartTime()));
							}*/
							reserveRecording(!mRecordSlideBtn.isChecked());
						}
					} else { // ���� ��ȭ�� 
						//��ȭ�� ��ȣ ����/���
						if (mRecordSlideBtn.isChecked()) {
							Log.d("Sangtae", "��ȭ�� ��ȣ ���");
							sendRespone(XML_Address_Define.Record.getSetrecordprotection(mData.getRecordId(), Udid, mData.getChannelId(), XML_Address_Define.Record.RECORD_PROTECTION_NO));
						} else {
							Log.d("Sangtae", "��ȭ�� ��ȣ ����");
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
		SimpleDateFormat beforeCvtTime = new SimpleDateFormat("MM�� dd�� (E) HH:mm ~ ");
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
					// ���� ó��
					tempToast(result);
				}

			} else {
				AlertShow("Wifi Ȥ�� 3G���� ������� �ʾҰų� ��Ȱ���� �ʽ��ϴ�.��Ʈ��ũ Ȯ���� �ٽ� ������ �ּ���!");
			}

		}

	}

	private void tempToast(RESPONSE_DATA aData){
		Toast.makeText(mContext, String.format("ResultCode: %s, ErrString: %s", aData.getResultCode(), aData.getErrorString()), Toast.LENGTH_SHORT).show();
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

		if (!mData.getSeriesId().contains("NULL") && !isNow) { // �ø��� ��ȭ

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
			}else {
				if (isNow) {
					
					title.setText("��ȭ ���");
				}
				else {
					title.setText("��ȭ  ���� ���");
				}
				
				button1.setText("�̹� ȸ���� ��ȭ ����ϱ�");
				button2.setText("�̹� ȸ���� ��ü ��ȭ ����ϱ�");
			}
			
			rec_dialog.setCustomTitle(title);
			rec_dialog.setNegativeButton("���", new android.content.DialogInterface.OnClickListener() {
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
						if (isNow) {	// ���� ��� ��ȭ
							if ( !sendShortRecordNow() )
								showRecordFailMessage();
						}
						else {// ����  ����
							if ( !sendShortRecordReserve() )
								showRecordFailMessage();								
						}
					}
					else// ��� & ���� ���� ���
						sendShortRecordReserveCancel();

					recordingDlg.dismiss();
				}
			});

			button2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (isRecording) {	
						if (isNow) {
							if ( !sendShortRecordNow() )// ��ô��� ��ȭ
								showRecordFailMessage();
							else {
								if ( !sendSeriesRecordReserve() ) // �ø��� ����
									showRecordFailMessage();								
							}
						}
						else {	
							if ( !sendSeriesRecordReserve() )// �ø��� ����
								showRecordFailMessage();
						}
					}
					else
						sendSeriesRecordReserveCancel(); // �ø��� ���

					recordingDlg.dismiss();
				}
			});

			recordingDlg.show();

		}
		else {//����
			if (isRecording) {
				if (isNow) {
					if ( !sendShortRecordNow() ) // ��� ���� ����
						showRecordFailMessage();								
				}
				else {
					if ( !sendShortRecordReserve() ) // ���� ����
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
						mRecordSlideBtn.setChecked( !isRecording );
					}
				});
				AlertDialog alert = alert_internet_status.create();
				alert.show();
			}
		}

	}


	// �̹� ȸ������ ��ü ��ȭ ����ϱ�
	private Boolean sendSeriesRecordReserveCancel() {
		String requestURL = XML_Address_Define.Record.getSetrecordcancelreserve(Udid, mData.getChannelId(), mData.getRecordStartTime(), mData.getSeriesId(), XML_Address_Define.Record.RECORD_DEL_ALL);
		Log.d(DEBUG_TAG, "### URL Request �ø��� ��ü ��ȭ���: " + requestURL);

		CHECK_RESPONSE_PASER.start(requestURL);
		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			//Log.d("Sangtae", "�ø��� ��� ����");
			mRecordSlideBtn.setChecked(false);

			return true;
		}else {
			Log.d("Sangtae", "�ø��� ��� ����: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			//mOneClick=false;
			mRecordSlideBtn.setChecked(true);

			return false;
			//showRecordFailMessage();
			//recordReserveButton.setChecked(true);
		}

	}
	// �̹� ȸ������ ��ü ��ȭ �����ϱ�
	private Boolean sendSeriesRecordReserve() {
		String requestURL = XML_Address_Define.Record.getSetrecordseriesreserve( Udid, mData.getChannelId(), mData.getRecordStartTime(), mData.getSeriesId());
		Log.d(DEBUG_TAG, "### URL Request �ø��� ��ü ��ȭ����: " + requestURL);
		CHECK_RESPONSE_PASER.start(requestURL);

		response_DATA = CHECK_RESPONSE_PASER.getArrayList();

		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			Log.d("Sangtae", "�ø��� ���� ����");
			mRecordSlideBtn.setChecked(true);

			return true;
		}else {
			Log.d("Sangtae", "�ø��� ���� ����: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			mRecordSlideBtn.setChecked(false);

			return false;
			//showRecordFailMessage();
			//mOneClick=false;
			//recordReserveButton.setChecked(false);
		}
	}

	// �̹� ȸ���� ��ȭ �����ϱ�
	private Boolean sendShortRecordReserve() {
		String requestURL = XML_Address_Define.Record.getSetrecordreserve(Udid, mData.getChannelId(), mData.getRecordStartTime());
		Log.d(DEBUG_TAG, "### URL Request �̹� ȸ���� ��ȭ����: " + requestURL);
		CHECK_RESPONSE_PASER.start(requestURL);

		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			mRecordSlideBtn.setChecked(true);
			Log.d("Sangtae", "���� ��ȭ ����");

			return true;
		}
		else {
			Log.d("Sangtae", "���� ��ȭ ����: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			mRecordSlideBtn.setChecked(false);

			return false;
			//showRecordFailMessage();
			//mOneClick=false;
			//recordReserveButton.setChecked(false);
		}
	}

	// �̹� ȸ���� ��� ��ȭ�ϱ�
	private Boolean sendShortRecordNow() {
		String requestURL = XML_Address_Define.Record.getSetrecordseries(Udid, mData.getChannelId(), mData.getRecordStartTime());
		Log.d(DEBUG_TAG, "### URL Request �̹� ȸ���� ��� ��ȭ: " + requestURL);
		CHECK_RESPONSE_PASER.start(requestURL);

		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			mRecordSlideBtn.setChecked(true);
			Log.d(DEBUG_TAG, "��� ���� ��ȭ ����");
			return true;
		}
		else {
			Log.d(DEBUG_TAG, "��� ���� ��ȭ ����: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
			mRecordSlideBtn.setChecked(false);

			return false;
			//showRecordFailMessage();
			//mOneClick=false;
			//recordReserveButton.setChecked(false);
		}
	}


	// �̹� ȸ���� ��ȭ ����ϱ�
	private Boolean sendShortRecordReserveCancel() {
		String requestURL = XML_Address_Define.Record.getSetrecordcancelreserve(Udid, mData.getChannelId(), mData.getRecordStartTime(), mData.getSeriesId(), XML_Address_Define.Record.RECORD_DEL_ONE);
		Log.d(DEBUG_TAG, "### URL Request �̹� ȸ�� ��ȭ���: " + requestURL);

		CHECK_RESPONSE_PASER.start(requestURL);
		response_DATA = CHECK_RESPONSE_PASER.getArrayList();
		if (response_DATA.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)){
			Log.d("Sangtae", "���� ��� ����");
			mRecordSlideBtn.setChecked(false);

			return true;
		}else {
			Log.d("Sangtae", "���� ��� ����: "+ response_DATA.getResultCode()+response_DATA.getErrorString());
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
		alert_internet_status.setTitle("��ȭ �Ұ�");
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

	private String getRecordFailMessage() {
		String resultCode = response_DATA.getResultCode();
		String errorMessage = null;

		if (resultCode.contains("002")) {
			errorMessage = "�ߺ��Ǵ� �ð��� �ٸ� ���α׷��� ��ȭ ������ �����Ǿ� �ֽ��ϴ�.";
		}
		else if (resultCode.contains("003")) {
			errorMessage =  "��ž�� ��������� �����մϴ�. ��ȭ�� ����� Ȯ�����ּ���.";
		}
		else if (resultCode.contains("005")) {
			errorMessage =  "������ ��ž���� �������� �ʴ� ��ǰ �Ǵ� ������ ä���Դϴ�. [����/�ȳ�]���� Ȯ�����ּ���.";
		}
		else if (resultCode.contains("005-1")
				|| resultCode.contains("010")) {
			errorMessage =  "��ž���� ����ȭ�� ����� ������� ��� �������δ� ��ó�ȭ�� �Ұ��մϴ�.";
		}
		else if (resultCode.contains("011")
				|| resultCode.contains("015")) {
			errorMessage =  "��ž���� �ٸ� ä���� ��ȭ���� ��� �������δ� ��ó�ȭ�� �Ұ��մϴ�.";
		}
		else if (resultCode.contains("009")
				|| resultCode.contains("012")
				|| resultCode.contains("013")
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
}

