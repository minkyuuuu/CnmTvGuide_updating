package hdlnc.dev2.sangtae.cnm.global;

import static hdlnc.dev2.sangtae.cnm.global.TVCH_Alarm_CheckSQLiteOpenHelper.TV_ALARM_TABLE;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Alarm_Activity.ALARM_EXCUTION_OK;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Alarm_Activity.AlARM_SEND_BC_ACTION;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import hdlnc.dev2.sangtae.cnm.R;

public class CNM_Alarm_Service extends Service{

	String TAG = "CNM_Alarm_Service";

	public static final String CNM_Alarm_Service_ACTION = "CNM_Alarm_Service_ACTION";
	public static final String CNM_AlarmDelete_Service_ACTION = "CNM_AlarmDelete_Service_ACTION";
	public static final String CNM_Alarm_EXCUTE = "CNM_Alarm_EXCUTE";
	public static final String CNM_Alarm_Seq = "CNM_Alarm_Seq";
	private String Alarm_Before = "alarmbefore";


	Timer m_timer = null; 
	long m_lstartTime = 0;
	public static SimpleDateFormat m_formatter;
	Context mContext;

	String MEMO_DATE = "date";
	String MEMO_NOTI = "memo";
	String MEMO_LUNAR = "lunar";

	private static final int TIMER_PERIOD = 2 * 1000; 
	private static final int COUNT = 10;
	private int mCounter;	
	private CNMApplication tempApp;
	String m_protitle = "";
	int m_beforeTime=0;
	long m_alarmTime = 0;
	
	int m_NotiNum = 1000;
	int m_lastNotiNum = 0;
	boolean m_lastFlag = false;

	//Init 함수 
	//타이머를 생성하여 60초에 한번씩 호출 하게 설정.
	private void init(){ 
		m_timer = new Timer(); 
		m_lstartTime = System.currentTimeMillis(); 
		TVAlarmTask task = new TVAlarmTask(); 
		m_timer.schedule( task, new Date(), 10000 ); 
		tempApp = (CNMApplication)getApplication();
		//Toast.makeText(mContext, "init", Toast.LENGTH_LONG).show();
	}

	//CalendarTask :Timer 상속받음.
	//60초한번씩 run()메소드가 호출
	class TVAlarmTask extends TimerTask{ 
		public void run(){ 

			Calendar calendar = Calendar.getInstance( );

			Date dAftertime =new Date();
			Date dNowtime =new Date();

			//1분후 시간 설정.
			dAftertime.setTime( calendar.getTimeInMillis()+360000);

			//현재 시간 설정.
			dNowtime.setTime( calendar.getTimeInMillis()-60000);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:");
			//where절 : date컬럼안에 저장되 있는 시간중 현재 1분후 현재시간사이에 비교해서 가져온다.
			String where = "alartime BETWEEN '"+dateFormat.format(dNowtime)+"' AND '" + dateFormat.format(dAftertime)+"'";

			Cursor dataCursor = tempApp.TV_Alarm_database.query(TV_ALARM_TABLE, 
					null, 		// 5 table
					where, null, null, null, null);
			//Cursor dataCursor = TVCH_database.rawQuery("select " +" _id, "+PROGRAM_ID+", "+ PROGRAM_RESERVE+", "+ PROGRAM_MY_CHANNEL+", "+ PROGRAM_MY_PROGRAM+", "+ PROGRAM_HIDE +" from "+TVCH_CHECK_TABLE, null);

			dataCursor.moveToFirst();
			TVCH_Alarm_CheckList data;
			if (! dataCursor.isAfterLast()) {
				do {
					String seq = dataCursor.getString(0);
					String proTitle = dataCursor.getString(1);
					String proCHnum = dataCursor.getString(2);
					String proCHURL = dataCursor.getString(4);
					int proAlarmFlag = dataCursor.getInt(5);
					String proTime = dataCursor.getString(8);

					boolean  flag= false;

					if(proAlarmFlag==1)
						flag = true;

					Log.d(TAG , proTitle);

					//boolean hide = Boolean.parseBoolean(myHide);

					data = new TVCH_Alarm_CheckList(seq);
					//data.setID(id);
					data.setDB_PROGRAM_TITLE(proTitle);
					data.setDB_PROGRAM_CH_NUMBER(proCHnum);
					data.setDB_CH_URL(proCHURL);
					data.setDB_ALARM_CHECK(flag);
					data.setDB_ALARM_TIME(proTime);
					//data.setHide(hide);
					//NotificationManager를 불러온다.
					NotificationManager  nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

					// Create Notification Object
					Notification notification =
						new Notification(R.drawable.icon,
								"씨앤앰 알림", System.currentTimeMillis());


					String expandedTitle=proTitle;


					int index=proTime.indexOf(")");

					proTime=proTime.substring(index+2);

					index=proTime.indexOf(":");
					int hour = Integer.parseInt(proTime.substring(0,index));

					int index2=proTime.indexOf("~");
					int min =calendar.getTime().getMinutes();


					m_beforeTime = Integer.parseInt(proTime.substring(index+1,index2-1));

					Log.d(TAG , "proTime = "+proTime);
					Date date = new Date(calendar.getTime().getYear(),calendar.getTime().getMonth(),calendar.getTime().getDate(),hour,m_beforeTime,0);

					//Log.d(TAG , "m_beforeTime = "+m_beforeTime);
					//Log.d(TAG , "min = "+min);
					//m_beforeTime = m_beforeTime -min;
					//Log.d(TAG , "proTime = "+proTime);
					//Log.d(TAG , "m_beforeTime = "+m_beforeTime);
					m_alarmTime = date.getTime();

					int day=calendar.getTime().getDate();
					long time = date.getTime()/60000- System.currentTimeMillis()/60000;
					m_beforeTime= (int) time;

					String expandedText=String.valueOf( time)+"분전입니다.";
					m_protitle = proTitle;
					Message msg = m_handlerDraw.obtainMessage();
					m_handlerDraw.sendMessage(msg);
					//Toast.makeText(mContext, proTitle+"  10분전입니다.", Toast.LENGTH_LONG).show();
					//PendingIntent pi = PendingIntent.getActivity(
					//		getBaseContext(), 0,intent,PendingIntent.FLAG_ONE_SHOT  );//intent,Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
					//PendingIntent.FLAG_ONE_SHOT);
					//pi.send();
					
					

					

					PendingIntent pi = PendingIntent.getActivity(
							getBaseContext(), 0,null,PendingIntent.FLAG_ONE_SHOT  );
					notification.flags =Notification.FLAG_AUTO_CANCEL;
					notification.setLatestEventInfo(getBaseContext(), expandedTitle, expandedText, pi);
					notification.defaults |= Notification.DEFAULT_ALL;
					//noti를 등록하여 화면 상태바에 띄어준다.
					nm.notify(m_NotiNum, notification);
					m_NotiNum++;
					m_lastNotiNum++;

					tempApp.deleteAlarm(seq);

					//currentTVCH.add(data);
					//TVAl_CheckMap.put(seq, data);
				} while(dataCursor.moveToNext());
			}

			if(m_alarmTime != 0)
			{
				long nowTime=System.currentTimeMillis();

				if(nowTime > m_alarmTime )
				{
					NotificationManager  nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
					nm.cancel(1234);
					m_alarmTime=0;
				}
			}

			dataCursor.close();
		}
	}
	final Handler m_handlerDraw = new Handler() {
		public void handleMessage(Message msg) {

			Intent intent = new Intent(AlARM_SEND_BC_ACTION);
			intent.putExtra(ALARM_EXCUTION_OK, true);
			sendBroadcast(intent);
			Toast.makeText(mContext, m_protitle+":   "+String.valueOf(m_beforeTime)+"분전입니다.", 5000).show();

		}
	};
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();

		Log.i(TAG, "Service started at the BOOT_COMPLETED.");


		mContext = this;
		//init();

		registerReceiver(new AlarmBroadCast(), new IntentFilter(CNM_Alarm_Service_ACTION));
		registerReceiver(new AlarmDeleteBroadCast(), new IntentFilter(CNM_AlarmDelete_Service_ACTION));


	}

	@Override 
	public void onStart(Intent intent, int startId){

		super.onStart(intent, startId);
		Log.i(TAG, "Service onStart");

	}

	class AlarmBroadCast extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			
			
			//Log.d("TEST"," ="+intent.getIntExtra("1",0));
			
			
			NotificationManager  nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			 
			 // Create Notification Object
			 Notification notification =
			 	new Notification(R.drawable.icon,
			 			"씨앤앰 알림",System.currentTimeMillis());
			
			PendingIntent pi = PendingIntent.getActivity(
					getBaseContext(), 0,null,0 );
			notification.flags =Notification.FLAG_AUTO_CANCEL;
			
			String[]szbuf=new String[4];
			
			szbuf=intent.getStringArrayExtra("2");
			
		
			
				notification.setLatestEventInfo(getBaseContext(),szbuf[0],szbuf[2]+"분 전입니다.",pi);// intent.getStringExtra(CNM_Alarm_EXCUTE), remineTime, pi);
				notification.defaults |= Notification.DEFAULT_ALL;
				
				nm.notify(Integer.parseInt(szbuf[1]), notification);
				
				 
				 
				
				Toast.makeText(mContext, szbuf[0]+":   "+szbuf[2]+"분 전입니다.", 5000).show();
			
		}

	}
	
	
	class AlarmDeleteBroadCast extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			
			String[]szbuf=new String[4];
			
			szbuf=intent.getStringArrayExtra("2");
			
			
			Log.d("TEST","remineTime = "+szbuf[3]);
			
			
				
				if(tempApp == null){
					tempApp = (CNMApplication)getApplication();
				}
				
				tempApp.deleteAlarm(szbuf[1]);
				
				Intent Sendintent = new Intent(AlARM_SEND_BC_ACTION);
				Sendintent.putExtra(ALARM_EXCUTION_OK, true);
				sendBroadcast(Sendintent);
				
				NotificationManager  nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
				nm.cancel(Integer.parseInt(szbuf[1]));
			//}
		}

	}

}
