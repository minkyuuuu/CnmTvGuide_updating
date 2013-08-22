package hdlnc.dev2.sangtae.cnm.tvchannel;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.Intent_KEY_Define;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TVCH_CheckList;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.TV_ALL_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.TV_ALL_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.TV_CHANNEL_PASER;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
//import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_TAG_HASHMAP;
//import hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_HD.TvAllAsyncTask;



public class TVCH_MY_Activity extends ListActivity {

	public static final String DEBUG_TAG = "TVCH_HD";

	//private final ImageDownloader imageDownloader = new ImageDownloader();
	public static final String TVCH_ITEM_SELECT="TVCH_SELECT_ITEM";
	public static final String TVCH_ITEM_NUMBER="TVCH_ITEM_NUMBER";
	public static final String TVCH_ITEM_NAME="TVCH_ITEM_NAME";
	public static final String TVCH_ITEM_LOG_PATH="TVCH_ITEM_LOG_PATH";
	public static final String TVCH_ITEM_HD="TVCH_ITEM_HD";
	public static String TVCH_ITEM_MY_TAB = "TVCH_ITEM_MY_TAB";
	
	private CNMApplication tempApp;
	private TVCH_All_Adapter mAdapter;
	private TV_ALL_DATA_LIST mList;
	private TVCHImageDownloader imageDownloader;
	private HashMap<String, TVCH_CheckList> tempTVch_Check;
	//private VOD_TAG_HASHMAP vodTagHashMap;
	private int EditSelect=-1;
	private TvAllAsyncTask mTask;
	
	
	TabGroupActivity mParent = null;
	Intent mSelectMessage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tvch_all_channel);

		tempApp = (CNMApplication)getApplication();
		imageDownloader = new TVCHImageDownloader(getApplicationContext());
		imageDownloader.setContext(getApplicationContext());
		mList = new TV_ALL_DATA_LIST();

		tempTVch_Check = tempApp.getTVCh_CheckMap();



	}
	
	@Override
	protected void onResume() {

		super.onResume();
		
		if (tempApp.GetNetworkInfo() != 2) {
			mList.clear();
			if (tempApp.getTV_SCUEDULE_LIST() != null) {
				tempApp.getTV_SCUEDULE_LIST().clear();
			}

			if (mTask == null) {
				mTask = new TvAllAsyncTask();
				mTask.execute(null);
			}else {
				if (!mTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
					mTask = new TvAllAsyncTask();
					mTask.execute(null);
				}
			}
			
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
	public void onBackPressed() {

		//super.onBackPressed();
		NaviBtn_Singleton.getInstance().getNaviLeftBtn().performClick();
	}

	private void setupUI() {

		mAdapter = new TVCH_All_Adapter(this, mList);
		setListAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);
		//Log.d("Sangtae", "onListItemClick: "+mList.get(position).getID());
		tempApp.ButtonBeepPlay();
		tempApp.setTVSC_Index(0);
		mSelectMessage = new Intent(getParent(), TVCH_Schedule.class);
		mSelectMessage.putExtra(TVCH_ITEM_SELECT, mList.get(position).getID());
		mSelectMessage.putExtra(TVCH_ITEM_NUMBER, mList.get(position).getNUMBER());
		mSelectMessage.putExtra(TVCH_ITEM_NAME, mList.get(position).getNAME());
		mSelectMessage.putExtra(TVCH_ITEM_LOG_PATH, mList.get(position).getCHANNEL_LOG_IMG());
		mSelectMessage.putExtra(TVCH_ITEM_MY_TAB, true);
		//startActivity(selectMessage);
		mParent = (TabGroupActivity)getParent().getParent();
		//parentActivity.startChildActivity("TVCH_Schedule", selectMessage);

		new ChannelSearchAsyncTask().execute(mList.get(position).getNAME());
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

			mParent.startChildActivity("TVCH_Schedule", mSelectMessage);

		}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			pro = ProgressDialog.show(getParent().getParent(), "", getResources().getString(R.string.vod_watting),true);
		}
	}
	
	public class TVCH_All_Adapter extends BaseAdapter{
		private LayoutInflater mInflater;
		//private Context context;
		private TV_ALL_DATA_LIST items;
		private String mEditModeID;

		public TVCH_All_Adapter( Context context, TV_ALL_DATA_LIST items) { //int textViewResourceId, 

			mInflater = LayoutInflater.from(context);
			//this.context = context;
			this.items = items;
			mEditModeID = null;
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

		/*private void Reload() {
			
			notifyDataSetChanged();
		}*/

		private Boolean editmodeCheck(int index){
			if (mEditModeID != null) {
				if (mEditModeID.equals(items.get(index).getID())) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			//CurrentDB = tempApp.getCurrentTVCH();
			final ViewHolder holder;
			final int tempPosition = position;
			final View tempConvertView;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.all_channel_row, null);
				//Log.d("Sangtae", "convertView Null");
				holder = new ViewHolder();
				//holder.text = (TextView) convertView.findViewById(R.id.TextView01);
				// 아이콘
				holder.Mych_Icon	= (ImageView)convertView.findViewById(R.id.MyCh_Icon);
				//------메인-------
				holder.BasicLayout  = (LinearLayout)convertView.findViewById(R.id.tvch_row_Layout);
				holder.Ch_num		= (TextView)convertView.findViewById(R.id.TVCh_Number);
				holder.log_img		= (ImageView) convertView.findViewById(R.id.LogImage);

				holder.Ch_onTime	= (TextView)convertView.findViewById(R.id.TVCh_Time);
				holder.Ch_NextTime	= (TextView)convertView.findViewById(R.id.TVCh_NextTime);
				holder.edit_btn		= (ToggleButton)convertView.findViewById(R.id.tvch_list_icon_TglBtn);
				// 에디터 레이아웃 버튼
				holder.EditLayout	= (FrameLayout)convertView.findViewById(R.id.tvch_edit_Layout);
				holder.register_btn		= (ToggleButton)convertView.findViewById(R.id.tvch_register_tglBtn);

				holder.edit_btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						tempApp.ButtonBeepPlay();
					}
				});
				holder.register_btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						tempApp.ButtonBeepPlay();	
					}
				});
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				//Log.d("Sangtae", "convertView Not Null");
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			tempConvertView = convertView;
			// 내용
			holder.Ch_num.setText(items.get(position).getNUMBER());
			if (items.get(position).getCHANNEL_onAir_TIME() != null) {
				holder.Ch_onTime.setText(ToHourTime(items.get(position).getCHANNEL_onAir_TIME())+items.get(position).getCHANNEL_onAIR_TITLE());
				holder.Ch_NextTime.setText(ToHourTime(items.get(position).getCHANNEL_nextTIME())+items.get(position).getCHANNEL_nextTITLE());
			}
			// 로그
			String TempIMG = items.get(position).getCHANNEL_LOG_IMG();
			if (TempIMG != null) {
				imageDownloader.download(items.get(position).getCHANNEL_LOG_IMG(), holder.log_img, null);//, BitmapFactory.decodeResource(getResources(), R.drawable.noimg_logo));
			}
			//////////////////////////edit btn start////////////////////////////////
			holder.edit_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					if (isChecked) {
						//Log.d("Sangtae", "on"); // 사라짐.
						holder.EditLayout.setVisibility(View.VISIBLE);
						tempConvertView.setBackgroundResource(R.drawable.list_bg_03_press);
						mEditModeID = items.get(tempPosition).getID();
						if (tempPosition != EditSelect ) {
							notifyDataSetChanged();
							EditSelect = tempPosition;
						}
					} else {
						//Log.d("Sangtae", "off");
						holder.EditLayout.setVisibility(View.GONE);
						tempConvertView.setBackgroundResource(R.drawable.list_bg);
						if (mEditModeID != null) {
							if (mEditModeID.equals(items.get(tempPosition).getID())) {
								mEditModeID=null;
							}
						}
					}

				}
			});
			convertView.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					int extenX = holder.edit_btn.getWidth()/2;
					int extenY = holder.edit_btn.getHeight()/2;
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if ((holder.edit_btn.getLeft() - extenX) <= event.getX() &&
								event.getX() <= (holder.edit_btn.getRight() + extenX) &&
								(holder.edit_btn.getTop() - extenY) <= event.getY() &&
								event.getY() <= (holder.edit_btn.getBottom() + extenY)) {
							if (holder.edit_btn.isChecked()) {
								holder.edit_btn.setChecked(false);
							}else {
								holder.edit_btn.setChecked(true);
							}
							return true;
						}
						return false;



					default:
						break;
					}
					return false;
				}
			});
			holder.register_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					if (isChecked) {
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
					}
				}
			});

			//My ch
			if (tempTVch_Check.size() != 0 ) {
				if (tempTVch_Check.get(mList.get(position).getID()) != null) {
					if (tempTVch_Check.get(mList.get(position).getID()).getMyChannel()) {
						holder.Mych_Icon.setVisibility(View.VISIBLE);
						holder.register_btn.setChecked(true);
					} else {
						holder.Mych_Icon.setVisibility(View.GONE);
						holder.register_btn.setChecked(false);
					}	
				} else {
					holder.Mych_Icon.setVisibility(View.GONE);
					holder.register_btn.setChecked(false);
				}
			} else {
				holder.Mych_Icon.setVisibility(View.GONE);
				holder.register_btn.setChecked(false);
			}


			if (editmodeCheck(position)) {
				convertView.setBackgroundResource(R.drawable.list_bg_03_press);
				holder.edit_btn.setChecked(true);
			} else {
				convertView.setBackgroundResource(R.drawable.list_bg);
				holder.edit_btn.setChecked(false);
			}

			////////////////////////// edit btn end////////////////////////////////

			return convertView;
		}


	}




	static class ViewHolder {
		FrameLayout EditLayout;
		LinearLayout BasicLayout;
		ToggleButton register_btn;
		ToggleButton edit_btn;
		//-----------------
		ImageView	 Mych_Icon;
		TextView	Ch_num;
		ImageView	log_img;

		TextView	Ch_onTime;
		TextView	Ch_NextTime;

	}

	private String ToHourTime(String aTime){
		if (aTime.length() == 0) {
			return "";
		} else {
			SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
			SimpleDateFormat HourTime = new SimpleDateFormat("HH:mm ");

			ParsePosition pos = new ParsePosition(0);
			Date frmTime = mOriTime.parse(aTime, pos);
			return HourTime.format(frmTime);

		}
	}

	// 리스트 스레드
	class TvAllAsyncTask extends AsyncTask<Void, Void, TV_ALL_DATA_LIST>
	{

		ProgressDialog pro;
		@Override
		protected TV_ALL_DATA_LIST doInBackground(Void... params) {
			

			if(tempApp.getTv_ALL_DATA_LIST().size() == 0 || tempApp.getTVCH_SettigChecked())
			{
				TV_ALL_PASER paser = new TV_ALL_PASER();
				Log.d("Sangtae", "전체 채널 정보 새로 파싱");
				String requestURL = XML_Address_Define.Channel.getGetchannellist(tempApp.getLocationID(), tempApp.getProductID(), "", XML_Address_Define.Channel.CHANNEL_LSIT.ALL);
				Log.d(DEBUG_TAG, "### URL Request 전체 채널 정보" + requestURL);
				
				paser.start(requestURL); 
				//mList = tv_ALL_PASER.getTV_ALL_DATA_LIST();
				tempApp.setTv_ALL_DATA_LIST(paser.getTV_ALL_DATA_LIST());					// TV_ALL 채널 데이터을 Application에 저장함.
				return paser.getTV_ALL_DATA_LIST();
			}
			else{
				Log.d("Sangtae", "기존 전체 채널 정보 로딩");
				return tempApp.getTv_ALL_DATA_LIST();
			}

		}

		@Override
		protected void onPostExecute(TV_ALL_DATA_LIST result) {
			
			super.onPostExecute(result);

			try
			{
				for (int i = 0; i < result.size(); i++) {
					if (tempTVch_Check.containsKey(result.get(i).getID())) {
						if (tempTVch_Check.get(result.get(i).getID()).getMyChannel()) {
							Log.d("Sangtae", "tempTVch_Check: "+ tempTVch_Check.size() + ", result.count: "+result.size());
							mList.add(result.get(i));
						}
					}
				}
				
				//mList=result;
				if(mList.size() > 0 )
				{
					setupUI();
				}
			}
			catch (Exception e) {
				
				Log.w("Sangtae", e);
			}		
			pro.dismiss();

		}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			pro = ProgressDialog.show(getParent().getParent(), "", getResources().getString(R.string.tv_watting),true);
		}
	}


}
