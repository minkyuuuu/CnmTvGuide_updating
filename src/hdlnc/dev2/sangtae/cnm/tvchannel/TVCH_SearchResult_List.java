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

import hdlnc.dev2.sangtae.cnm.global.ImageDownloader;
import hdlnc.dev2.sangtae.cnm.global.Intent_KEY_Define;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.TV_ALL_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.TV_CHANNEL_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.TV_SEARCH_PASER;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TVCH_SearchResult_List extends Activity{
	
	private final static String DEBUG_TAG = "TVCH_SearchResult_List";
	
	//private Boolean Edit=false;
	private CNMApplication tempApp;
	//NaviBtn_Singleton mNaviBtnSingleton;
	private TV_SearchResult_List_Adapter mAdapter;
	private TV_ALL_DATA_LIST mArrayList;
	private ListView searchResultListView;
	TabGroupActivity mParent = null;
	Intent mSelectMessage = null;

	@Override
	protected void onRestart() {
		
		super.onRestart();
		Log.d("Sangtae", "재시작");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.vod_searchresult);

		tempApp = (CNMApplication)getApplication();
		//mNaviBtnSingleton =  tempApp.getNaviBtn_Singleton();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String KeyWord = bundle.getString("KeyWord");

		new TVSearchAsyncTask().execute(KeyWord);
		
		mParent =(TabGroupActivity) getParent();
	}


	@Override
	protected void onResume() {
		
		super.onResume();
		Log.d("Sangtae", "시작 중");
		//setUpNaviArea();
		setupNaviBar();
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		Log.d("Sangtae", "일시 정지");
	}
	@Override
	protected void onStop() {
		
		super.onStop();
		Log.d("Sangtae", "정지");
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
		Log.d("Sangtae", "상태 저장");
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		
		super.onRestoreInstanceState(savedInstanceState);
		Log.d("Sangtae", "복원");
	}

	public void setUpUI()
	{
		searchResultListView = (ListView)findViewById(R.id.vod_searchResult_listview);

		searchResultListView.setDividerHeight(0);
		searchResultListView.setDivider(getResources().getDrawable(R.drawable.transparent));

		TextView headerTextView = (TextView)findViewById(R.id.vod_searchResult_header);
		headerTextView.setText(mAdapter.getCount() + "개의 프로그램이 검색되었습니다.");

		searchResultListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position,
					long id) {
				
				tempApp.ButtonBeepPlay();
				//mParent =(TabGroupActivity) getParent();
				mSelectMessage = new Intent(getParent(), TVCH_Schedule_Setting.class);
				mSelectMessage.putExtra(TVCH_ITEM_SELECT, mArrayList.get(position).getPROGRAM_nextID());		// onair seq로 사용함
				mSelectMessage.putExtra(TVCH_ITEM_HD, mArrayList.get(position).getPROGRAM_onAIR_HD());// 2011 01 26 홍성일.
				mSelectMessage.putExtra(TVCH_ITEM_NUMBER, "ch"+ mArrayList.get(position).getNUMBER());
				mSelectMessage.putExtra(TVCH_ITEM_NAME, mArrayList.get(position).getNAME());
				mSelectMessage.putExtra(TVCH_ITEM_LOG_PATH, mArrayList.get(position).getCHANNEL_LOG_IMG());


				//SCUEDULE_DATA intent
				mSelectMessage.putExtra(TVCH_SCHEDULE_ID, mArrayList.get(position).getPROGRAM_onAIR_ID());
				mSelectMessage.putExtra(TVCH_SCHEDULE_TITLE, mArrayList.get(position).getCHANNEL_onAIR_TITLE());
				mSelectMessage.putExtra(TVCH_SCHEDULE_TIME, mArrayList.get(position).getCHANNEL_onAir_TIME());
				mSelectMessage.putExtra(TVCH_SCHEDULE_GRADE, mArrayList.get(position).getCHANNEL_nextTITLE());	// 쓰지 않은 자료에 넣었음
				mSelectMessage.putExtra(TVCH_SCHEDULE_HD, mArrayList.get(position).getPROGRAM_onAIR_HD());

				if (mArrayList.get(position).getCHANNEL_nextTIME().length() == 0)
				{
					mSelectMessage.putExtra(TVCH_SCHEDULE_ALARM_TIME, BrodcatTime(mArrayList.get(position).getCHANNEL_onAir_TIME(), "00700"));
				}else{
					mSelectMessage.putExtra(TVCH_SCHEDULE_ALARM_TIME, BrodcatTime(mArrayList.get(position).getCHANNEL_onAir_TIME(), mArrayList.get(position).getCHANNEL_nextTIME())); // 2011 01 27 홍성일
				}
				//parent.startChildActivity("TVCH_Schedule_Setting", selectMessage);

				new ChannelSearchAsyncTask().execute(mArrayList.get(position).getNAME());
			}
		});

		searchResultListView.setAdapter(mAdapter);

	}

	private void setupNaviBar() {
		
		NaviBtn_Singleton.getInstance().getNaviHeaderText().setText("검색결과");
		NaviBtn_Singleton.getInstance().getNaviLeftBtn().setBackgroundResource(R.drawable.top_button_back);
		NaviBtn_Singleton.getInstance().getNaviLeftBtn().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//mBackBtnCheck=true;
				tempApp.ButtonBeepPlay();
				onBackPressed();
			}
		});
		
		NaviBtn_Singleton.getInstance().getNaviRightSubBtn().setVisibility(View.GONE);
		NaviBtn_Singleton.getInstance().getNaviRightBtn().setVisibility(View.GONE);
		/*if (getIntent().getBooleanExtra(TVCH_ITEM_MY_TAB, false)) {
			NaviBtn_Singleton.getInstance().getNaviRightBtn().setVisibility(View.GONE);
		}else {
			NaviBtn_Singleton.getInstance().getNaviRightBtn().setVisibility(View.VISIBLE);
			//NaviBtn_Singleton.getInstance().getNaviRightBtn().setChecked(true);
			NaviBtn_Singleton.getInstance().getNaviRightBtn().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					if (isChecked) {
						//getTabHost().setCurrentTab(4);
						Intent intent = new Intent(getParent(), TVCH_My.class);
						TabGroupActivity parentActivity = (TabGroupActivity)getParent();
						parentActivity.startChildActivity("TVCH_My", intent);  
					} else {
					getTabHost().setCurrentTab(mCurrentTab);
				}
				}
			});
		}*/
	}

	private String BrodcatTime(String BeforeTime, String AfterTime){
		//ori
		SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
		SimpleDateFormat beforeCvtTime = new SimpleDateFormat("MM월 dd일 (E) HH:mm ~ ");
		SimpleDateFormat afterCvtTime = new SimpleDateFormat("HH:mm");
		//SimpleDateFormat allTime = new SimpleDateFormat("yyyy-MM-dd ", Locale.KOREAN);

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


	class TVSearchAsyncTask extends AsyncTask<String, Void, TV_ALL_DATA_LIST>
	{

		ProgressDialog pro;
		@Override
		protected TV_ALL_DATA_LIST doInBackground(String... params) {
			
			String requestURL = XML_Address_Define.Search.getSearchprogram(params[0], "0", "0", tempApp.getLocationID(), tempApp.getProductID());
			Log.d(DEBUG_TAG, "## URL Request TV프로그램 검색: " + requestURL);
			TV_SEARCH_PASER PARSER = new TV_SEARCH_PASER(requestURL);
			PARSER.start();

			return PARSER.getTV_Search_DATA_LIST();

			
//			TV_SEARCH_PASER PARSER = new TV_SEARCH_PASER(tempApp.getLocationID(), tempApp.getProductID(), params[0]);
//			PARSER.start();
//			return PARSER.getTV_Search_DATA_LIST();

		}

		@Override
		protected void onPostExecute(TV_ALL_DATA_LIST result) {
			
			super.onPostExecute(result);

			mArrayList =result;
			if(mArrayList.getResultCode() != null)
			{
				mAdapter = new TV_SearchResult_List_Adapter(TVCH_SearchResult_List.this, mArrayList);
				setUpUI();
			}
			pro.dismiss();
		}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			pro = ProgressDialog.show(getParent().getParent(), "", getResources().getString(R.string.vod_watting),true);
		}
	}

	class ChannelSearchAsyncTask extends AsyncTask<String, Intent, Boolean>
	{

		ProgressDialog pro;
		@Override
		protected Boolean doInBackground(String... params) {
			
			String requestURL = XML_Address_Define.Search.getSearchchannel(params[0], "0", "0", tempApp.getLocationID(), tempApp.getProductID());
			Log.d(DEBUG_TAG, "## URL Request TV채널 검색: " + requestURL);
			TV_CHANNEL_PASER PARSER = new TV_CHANNEL_PASER();
			PARSER.start(requestURL);

			TV_ALL_DATA_LIST channelList = PARSER.getTV_ALL_DATA_LIST();
			for (int i = 0; i < channelList.size(); i++) {
				if (channelList.get(i).getNAME().equalsIgnoreCase(params[0]))
					return channelList.get(i).getCHANNEL_RECORDING();
			}
			return false;

			
//			TV_SEARCH_PASER PARSER = new TV_SEARCH_PASER(tempApp.getLocationID(), tempApp.getProductID(), params[0]);
//			PARSER.start();
//			return PARSER.getTV_Search_DATA_LIST();

		}

		@Override
		protected void onPostExecute(Boolean isAllowRecording) {
			
			super.onPostExecute(isAllowRecording);

			pro.dismiss();

			mSelectMessage.putExtra(Intent_KEY_Define.Recording.ChannelRecoddingMode, isAllowRecording);

			mParent.startChildActivity("TVCH_Schedule_Setting", mSelectMessage);

		}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			pro = ProgressDialog.show(getParent().getParent(), "", getResources().getString(R.string.vod_watting),true);
		}
	}



	public class TV_SearchResult_List_Adapter extends BaseAdapter{

		private final ImageDownloader imageDownloader = new ImageDownloader();
		private LayoutInflater mInflater;
		//private Context context;
		private TV_ALL_DATA_LIST items;

		OnCheckedChangeListener hideListener, myChListener;


		public TV_SearchResult_List_Adapter( Context context, TV_ALL_DATA_LIST items) { //int textViewResourceId, 

			mInflater = LayoutInflater.from(context);
			//this.context = context;
			this.items = items;
			imageDownloader.setContext(getApplicationContext());


			//notifyDataSetChanged();

		}

		@Override
		public int getCount() {
			
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			

			return position;
		}

/*		private void Reload() {
			
			notifyDataSetChanged();
		}
*/

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			

			final ViewHolder holder;
			//final int tempPosition = position;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.tv_search_row, null);
				//Log.d("Sangtae", "convertView Null");
				holder = new ViewHolder();
				holder.numberText = (TextView)convertView.findViewById(R.id.vod_ListNumber);
				holder.programCh  = (TextView)convertView.findViewById(R.id.tvch_numb);
				holder.postImage = (ImageView)convertView.findViewById(R.id.tvch_logimg);
				/*holder.gradeImage = (GradeImageView)convertView.findViewById(R.id.vod_ListClassIcon);
				holder.vodTypeImage = (TypeImageView)convertView.findViewById(R.id.vod_ListVodTypeIcon);*/
				holder.titleText=(TextView)convertView.findViewById(R.id.tvch_title);
				holder.programDay=(TextView)convertView.findViewById(R.id.tvch_day);
				holder.programTime=(TextView)convertView.findViewById(R.id.tvch_time);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				//Log.d("Sangtae", "convertView Not Null");
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			holder.numberText.setText(String.format("%03d",position+1));
			holder.programCh.setText("ch "+ items.get(position).getNUMBER());
			if (items.get(position).getCHANNEL_LOG_IMG() != null) {
				imageDownloader.download(items.get(position).getCHANNEL_LOG_IMG(), holder.postImage);	
			}
			holder.titleText.setText(items.get(position).getCHANNEL_onAIR_TITLE());

			holder.programDay.setText(TVDay(items.get(position).getCHANNEL_onAir_TIME()));
			holder.programTime.setText(TVTime(items.get(position).getCHANNEL_onAir_TIME()));
			//			convertView.setId(Integer.parseInt(items.get(position).getID()));


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
		ImageView postImage;
		/*GradeImageView gradeImage;
		TypeImageView vodTypeImage;*/
		TextView titleText;
		TextView programDay;
		TextView programTime;
		TextView programCh;
		TextView numberText;
	}

/*	private void setUpNaviArea() {
		Resources res = getResources();

		ToggleButton naviLeftSubBtn = (ToggleButton)NaviBtn_Singleton.getInstance().getNaviLeftSubBtn();
		naviLeftSubBtn.setChecked(true);
		naviLeftSubBtn.setVisibility(View.INVISIBLE);

		TextView naviHeaderText = (TextView)NaviBtn_Singleton.getInstance().getNaviHeaderText();
		naviHeaderText.setText("검색결과");

		//Drawable drawableBack = res.getDrawable(R.drawable.navi_back_btn);
		ImageButton naviLeftBtn = (ImageButton)NaviBtn_Singleton.getInstance().getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_back);//ImageDrawable(drawableBack);

		ImageButton	naviRightSubBtn =(ImageButton) NaviBtn_Singleton.getInstance().getNaviRightSubBtn();
		naviRightSubBtn.setVisibility(View.VISIBLE);
		naviRightSubBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				tempApp.ButtonBeepPlay();
				new CNM_SearchUpDialog(getParent(), style.Theme_Translucent_NoTitleBar_Fullscreen, 
						"VOD 명으로 검색", new OnEnterKeyListener() {

					@Override
					public void onEnterKeyPressed(String str) {

						onBackPressed();
						Intent searchResultIntent = new Intent(TVCH_SearchResult_List.this, TVCH_SearchResult_List.class);
						TabGroupActivity parent = (TabGroupActivity)getParent(); 
						searchResultIntent.putExtra("KeyWord", str);
						parent.startChildActivity("VODSE_SearchResult_List", searchResultIntent);

					}
				}).show();
			}
		});

	}
*/
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.d("Sangtae", "KEYCODE_BACK onKeyUp - TabGroupActivity");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//onBackPressed();
			NaviBtn_Singleton.getInstance().getNaviLeftBtn().performClick();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
}
