package hdlnc.dev2.sangtae.cnm.tvchannel;

import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_HD;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_LOG_PATH;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_NAME;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_NUMBER;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_All.TVCH_ITEM_SELECT;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_SCHEDULE_ALARM_TIME;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_SCHEDULE_GRADE;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_SCHEDULE_HD;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_SCHEDULE_ID;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_SCHEDULE_TIME;
import static hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.TVCH_SCHEDULE_TITLE;
import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TVCH_Alarm_CheckList;
import hdlnc.dev2.sangtae.cnm.global.TVCH_PR_CheckList;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



public class TVCH_Alarm_Activity extends Activity {
	// TVCH_Alarm_CheckList

	private TV_Alarm_Adapter mAdapter;
	private CNMApplication tempApp;
	private TVCHImageDownloader imageDownloader;
	private Boolean		mBackBtnCheck=false;
	private HashMap<String, TVCH_Alarm_CheckList> tempTVal_Check;
	private ArrayList<TVCH_Alarm_CheckList> m_SeqArr =new ArrayList<TVCH_Alarm_CheckList>() ;
	private HashMap<String, TVCH_PR_CheckList> m_tvpr_list;
	private ListView m_alarm_list;

	private IntentFilter alarmFilter;
	private BroadcastReceiver alarmReceiver;
	public static final String AlARM_SEND_BC_ACTION = "hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Alarm_Activity";
	public static final String ALARM_EXCUTION_OK = "ALARM_EXCUTION";
	//private SCUEDULE_DATA_LIST mList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tempApp = (CNMApplication) getApplication();

		setupUI();
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		NaviBtn_Singleton.getInstance().getNaviLeftBtn().performClick();
	}

	private void setupUI() {
		setContentView(R.layout.tv_alarm_layout);


		imageDownloader = new TVCHImageDownloader(getApplicationContext());
		imageDownloader.setContext(getApplicationContext());
		tempTVal_Check = tempApp.getTVAl_CheckMap();
		m_SeqArr = tempApp.getTVAl_CheckArr();
		TimeCompare compare =  new TimeCompare();
		Collections.sort(m_SeqArr, compare);
		m_tvpr_list = tempApp.getTVPr_CheckMap();
		m_alarm_list = (ListView)findViewById(R.id.alarm_list);

		
		// Use a custom layout file

		// Set up our adapter
		mAdapter = new TV_Alarm_Adapter(this, m_tvpr_list);
		m_alarm_list.setAdapter(mAdapter);

		alarmFilter =  new IntentFilter(AlARM_SEND_BC_ACTION);
		alarmReceiver =  new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				if (intent.getBooleanExtra(ALARM_EXCUTION_OK, false)) {
					Log.d("TVCH_Alarm_Activity","onReceive");
					mAdapter.notifyDataSetChanged();
				}
			}
		};
		registerReceiver(alarmReceiver, alarmFilter);



		m_alarm_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				tempApp.ButtonBeepPlay();
				TVCH_Alarm_CheckList check=m_SeqArr.get(arg2);

				Intent selectMessage = new Intent(getParent(), TVCH_Schedule_Setting.class);
				selectMessage.putExtra(TVCH_ITEM_SELECT,check.getDB_SEQ());
				selectMessage.putExtra(TVCH_ITEM_HD, check.getDB_ALARM_HD());// 2011 01 26 홍성일.
				selectMessage.putExtra(TVCH_ITEM_NUMBER, check.getDB_PROGRAM_CH_NUMBER());
				selectMessage.putExtra(TVCH_ITEM_NAME, check.getDB_PROGRAM_CH_NANE());
				selectMessage.putExtra(TVCH_ITEM_LOG_PATH, check.getDB_CH_URL());

				//SCUEDULE_DATA intent
				selectMessage.putExtra(TVCH_SCHEDULE_ID, check.getDB_PROGRAM_ID());
				selectMessage.putExtra(TVCH_SCHEDULE_TITLE,  check.getDB_PROGRAM_TITLE());
				selectMessage.putExtra(TVCH_SCHEDULE_TIME,  check.getDB_ALARM_TIME());
				selectMessage.putExtra(TVCH_SCHEDULE_GRADE,  check.getDB_PROGRAM_CH_GRADE());
				selectMessage.putExtra(TVCH_SCHEDULE_HD,  check.getDB_ALARM_HD());
				selectMessage.putExtra(TVCH_SCHEDULE_ALARM_TIME, check.getDB_ALARM_TIME()); // 2011 01 27 홍성일

				TabGroupActivity parentActivity = (TabGroupActivity)getParent().getParent();
				parentActivity.startChildActivity("TVCH_Schedule_Setting", selectMessage);
			}
		});
	}


	/*@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);
		TVCH_Alarm_CheckList check=m_SeqArr.get(position);

		Intent selectMessage = new Intent(getParent(), TVCH_Schedule_Setting.class);
		selectMessage.putExtra(TVCH_ITEM_SELECT,check.getDB_SEQ());
		selectMessage.putExtra(TVCH_ITEM_HD, check.getDB_ALARM_HD());// 2011 01 26 홍성일.
		selectMessage.putExtra(TVCH_ITEM_NUMBER, check.getDB_PROGRAM_CH_NUMBER());
		selectMessage.putExtra(TVCH_ITEM_NAME, check.getDB_PROGRAM_TITLE());
		selectMessage.putExtra(TVCH_ITEM_LOG_PATH, check.getDB_CH_URL());
		selectMessage.putExtra(TVCH_ITEM_MY_TAB, true);
		TabGroupActivity parentActivity = (TabGroupActivity)getParent().getParent();
		parentActivity.startChildActivity("TVCH_Schedule_Setting", selectMessage);


//		Intent selectMessage = new Intent(getParent(), TVCH_Schedule.class);
//		selectMessage.putExtra(TVCH_ITEM_SELECT, mList.get(position).getID());
//		selectMessage.putExtra(TVCH_ITEM_NUMBER, mList.get(position).getNUMBER());
//		selectMessage.putExtra(TVCH_ITEM_NAME, mList.get(position).getNAME());
//		selectMessage.putExtra(TVCH_ITEM_LOG_PATH, mList.get(position).getCHANNEL_LOG_IMG());
//		selectMessage.putExtra(TVCH_ITEM_MY_TAB, true);
//		
//		TabGroupActivity parentActivity = (TabGroupActivity)getParent().getParent();
//		parentActivity.startChildActivity("TVCH_Schedule", selectMessage);
	}*/




	public class TV_Alarm_Adapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private HashMap<String, TVCH_PR_CheckList> items;
		//private ArrayList<Integer> mPhotos = new ArrayList<Integer>();

		public TV_Alarm_Adapter(Context c, HashMap<String, TVCH_PR_CheckList> item) {
			mInflater = LayoutInflater.from(c);
			this.items = item;
		}

		public int getCount() {
			return tempTVal_Check.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// Make an ImageView to show a photo
			final ViewHolder holder;
			final int tempPosition = position;
			final View tempConvertView;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.tvch_alarm_row, null);
				//Log.d("Sangtae", "convertView Null");
				holder = new ViewHolder();


				holder.Alarm_My_ImageView = (ImageView)convertView.findViewById(R.id.Alarm_My_ImageView);
				holder.AlarmDayTextView = (TextView)convertView.findViewById(R.id.AlarmDayTextView);
				holder.AlarmTimeTextView = (TextView)convertView.findViewById(R.id.AlarmTimeTextView);
				holder.Alarm_CH_TextView = (TextView)convertView.findViewById(R.id.Alarm_CH_TextView);
				holder.Alarm_Title_TextView = (TextView)convertView.findViewById(R.id.Alarm_Title_TextView);
				holder.Alarm_Logo_ImageView = (ImageView)convertView.findViewById(R.id.Alarm_Logo_ImageView);
				holder.Alarm_HD_ImageView=(ImageView)convertView.findViewById(R.id.Alarm_HD_ImageView);
				convertView.setTag(holder);
			}else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				//Log.d("Sangtae", "convertView Not Null");
				holder = (ViewHolder) convertView.getTag();
			}
			if (m_SeqArr.size() < position) {
				notifyDataSetChanged();
			}
			
			// TODO: position이 m_SeqArr.size()를 넘어가서 죽는 문제 해결해야함.
			TVCH_Alarm_CheckList check=m_SeqArr.get(position);
			holder.Alarm_CH_TextView.setText( check.getDB_PROGRAM_CH_NUMBER());
			holder.Alarm_Title_TextView.setText( check.getDB_PROGRAM_TITLE());

			holder.AlarmDayTextView.setText(TVDay(check.getDB_ALARM_TIME()));
			holder.AlarmTimeTextView.setText(TVTime(check.getDB_ALARM_TIME()));



			// 로그
			String TempIMG = check.getDB_CH_URL();
			if (TempIMG != null ) {
				imageDownloader.download(TempIMG, holder.Alarm_Logo_ImageView, null);//, BitmapFactory.decodeResource(getResources(), R.drawable.noimg_logo));
			}

			String proid=check.getDB_PROGRAM_ID();
			if(!proid.equals("NULL"))
			{
				if(items.containsKey(proid))
				{
					TVCH_PR_CheckList ch_check=items.get(proid);
					boolean flag = ch_check.getMyProgram();
					if(flag)
					{
						holder.Alarm_My_ImageView.setVisibility(View.VISIBLE);
					}
					else{
						holder.Alarm_My_ImageView.setVisibility(View.GONE);
					}

				}else{
					holder.Alarm_My_ImageView.setVisibility(View.GONE);
				}

			}else{
				holder.Alarm_My_ImageView.setVisibility(View.GONE);
			}

			boolean flag=check.getDB_ALARM_HD();

			if(flag)
				holder.Alarm_HD_ImageView.setVisibility(View.VISIBLE);
			else{
				holder.Alarm_HD_ImageView.setVisibility(View.GONE);
			}




			return convertView;
		}

		private String TVDay(String onTime){
			//ori
			SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
			SimpleDateFormat mCvtTime = new SimpleDateFormat("MM.dd (E)");

			ParsePosition pos = new ParsePosition(0);
			Date beforeTime = mOriTime.parse(onTime, pos);

			return mCvtTime.format(beforeTime);
		}

		private long getmilliTime(String onTime){
			SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);

			ParsePosition pos = new ParsePosition(0);
			Date beforeTime = mOriTime.parse(onTime, pos);

			return beforeTime.getTime();
		}


		private String TVTime(String onTime){
			//ori
			SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
			SimpleDateFormat mCvtTime = new SimpleDateFormat("HH:mm");

			ParsePosition pos = new ParsePosition(0);
			Date beforeTime = mOriTime.parse(onTime, pos);

			return mCvtTime.format(beforeTime);
		}
	}

	static class ViewHolder {

		//-----------------
		ImageView	Alarm_My_ImageView;
		TextView	AlarmDayTextView;
		TextView	AlarmTimeTextView;
		TextView	Alarm_CH_TextView;
		TextView	Alarm_Title_TextView;
		ImageView	Alarm_Logo_ImageView;
		ImageView	Alarm_HD_ImageView;		

	}
	
	
	class TimeCompare implements Comparator<TVCH_Alarm_CheckList>{

		@Override
		public int compare(TVCH_Alarm_CheckList object1, TVCH_Alarm_CheckList object2) {

			return object1.getDB_ALARM__MilliTime().compareTo(object2.getDB_ALARM__MilliTime());
		}
	}

}
