package remote;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.CNM_WarningDialog;
import hdlnc.dev2.sangtae.cnm.global.TVCH_CheckList;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.CHECK_RESPONSE_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.CHECK_SETTOPSTATUS_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.RESPONSE_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.SettopStatus_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.TV_ALL_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.TV_ALL_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.TV_ALL_PASER;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCHImageDownloader;
import hdlnc.dev2.sangtae.cnm.vod.VODSE_Preview;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.ServiceState;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Remote_All_CH extends ListActivity {
	// private final ImageDownloader imageDownloader = new ImageDownloader();
	public static final String TVCH_ITEM_SELECT = "TVCH_SELECT_ITEM";
	public static final String TVCH_ITEM_NUMBER = "TVCH_ITEM_NUMBER";
	public static final String TVCH_ITEM_ID = "TVCH_ITEM_ID";
	public static final String TVCH_ITEM_NAME = "TVCH_ITEM_NAME";
	public static final String TVCH_ITEM_LOG_PATH = "TVCH_ITEM_LOG_PATH";
	public static final String TVCH_ITEM_HD = "TVCH_ITEM_HD";

	final private static long AlarmTimer = (1000*60)*10;  // 10분
	private Timer mReloadTimer;
	
	private CNMApplication tempApp;
	private TVCH_All_Adapter mAdapter;
	private TV_ALL_DATA_LIST mList;

	private SettopStatus_DATA mSettopStatus_DATA;

	private HashMap<String, TVCH_CheckList> tempTVch_Check;
	// private int EditSelect=-1;

	private ProgressDialog pro;
	private TvAllAsyncTask mTvAllAsyncTask;
	private SettopStatusTask mSettoStatusTask;
	private final String NET_ERR_TASK = "RemoteException";
	private String Udid;
	private RemoteControlAsyncTask mChannelTask;

	public static final String DEBUG_TAG = "Remote_All_CH";

	CNM_WarningDialog dialog;
	private RelativeLayout mTransLayout;

	private Handler WarningHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			mTransLayout.setVisibility(View.GONE);
			//settopPowerOffAlertShow();
			//msg.arg1  -1이면 상태 검사
			if (msg.what != 28) {
				tempApp.getTransLayout().setVisibility(View.GONE);
				tempApp.getPower().setEnabled(true);
				tempApp.getVolUP().setEnabled(true);
				tempApp.getVolDOWN().setEnabled(true);
			}
			switch (msg.what) {
			case 0:
				getListView().setEnabled(true);
				if (msg.arg1 < 0) {
					if(mSettopStatus_DATA.getWatchingchannel() != null && mAdapter != null && mList != null){
						if (mList.size() != 0) {
							if (!mSettopStatus_DATA.getWatchingchannel().equals(String.valueOf(msg.arg2)) && msg.arg2 > 0) { // 이전과  시청 방송이 다른 경우
								mSettopStatus_DATA.setWatchingchannel(String.valueOf(msg.arg2));
								mAdapter.notifyDataSetChanged();
							} else {		// 변화가 없는 경우 현재 시청 중인 방성으로 이동
								mAdapter.notifyDataSetChanged();
								pro.dismiss();
							}
						}else{
							TVChannelTask();	
						}
					} else {
						// tv 채널 리스트 등록
						TVChannelTask();
					}
				}else {	// 셋탑이 On 상태이고 채널 변경을 하려는 경우
					ChangingChannel(msg.arg1);
				}
				break;
			case 1:
				if (msg.arg1 < 0) {
					if (mSettopStatus_DATA.getWatchingchannel() != null && mAdapter == null) {
						TVChannelTask();
					}
				}else {
					if(((TV_ALL_DATA)getListView().getItemAtPosition(0)).getID() != null)
						mAdapter.notifyDataSetChanged();
					pro.dismiss();	
					Toast.makeText(getParent().getParent(),	"VOD 시청중에는 채널이 변경되지 않습니다.", Toast.LENGTH_SHORT).show();
				}
				break;
			case 2:
				if (msg.arg1 < 0) {
					if (mSettopStatus_DATA.getWatchingchannel() != null && mAdapter == null) {
						TVChannelTask();
					}
				}else {
					if(((TV_ALL_DATA)getListView().getItemAtPosition(0)).getID() != null)
						mAdapter.notifyDataSetChanged();
					pro.dismiss();	
					Toast.makeText(getParent().getParent(),	"Joy & Lift 시청중에는 채널이 변경되지 않습니다.", Toast.LENGTH_SHORT).show();
				}
				break;
			case 5:
				if (msg.arg1 < 0) {
					if (mSettopStatus_DATA.getWatchingchannel() != null && mAdapter == null) {
						TVChannelTask();
					}
				}else {
					if(((TV_ALL_DATA)getListView().getItemAtPosition(0)).getID() != null)
						mAdapter.notifyDataSetChanged();
					pro.dismiss();	
					Toast.makeText(getParent().getParent(),	"미디어 앨범 시청중에는 채널이 변경되지 않습니다.", Toast.LENGTH_SHORT).show();
				}
				break;
			case 6:
				if (msg.arg1 < 0) {
					if (mSettopStatus_DATA.getWatchingchannel() != null && mAdapter == null) {
						TVChannelTask();
					}
				}else {
					if(((TV_ALL_DATA)getListView().getItemAtPosition(0)).getID() != null)
						mAdapter.notifyDataSetChanged();
					pro.dismiss();	
					Toast.makeText(getParent().getParent(),	"녹화물 시청중에는 채널이 변경되지 않습니다.", Toast.LENGTH_SHORT).show();
				}
				break;
			case 11:
				if (msg.arg1 < 0) {
					if (mSettopStatus_DATA.getWatchingchannel() != null && mAdapter == null) {
						TVChannelTask();
					}
				}else {
					if(((TV_ALL_DATA)getListView().getItemAtPosition(0)).getID() != null)
						mAdapter.notifyDataSetChanged();
					dialog.setWarningDialogType(CNM_WarningDialog.LIVE_Record_Dupli);
					pro.dismiss();
					dialog.show();
				}
				break;
			case 10:
				if (msg.arg1 < 0) {
					if (mSettopStatus_DATA.getWatchingchannel() != null && mAdapter == null) {
						TVChannelTask();
					}
				}else {
					if(((TV_ALL_DATA)getListView().getItemAtPosition(0)).getID() != null)
						mAdapter.notifyDataSetChanged();
					dialog.setWarningDialogType(CNM_WarningDialog.LIVE_Record_PIP);
					pro.dismiss();
					dialog.show();
				}
				break;
			case 3:
				if (msg.arg1 < 0) {
					if (mSettopStatus_DATA.getWatchingchannel() != null && mAdapter == null) {
						TVChannelTask();
					}
				}else {
					if(((TV_ALL_DATA)getListView().getItemAtPosition(0)).getID() != null)
						mAdapter.notifyDataSetChanged();
					dialog.setWarningDialogType(CNM_WarningDialog.Record_OverStorag);
					pro.dismiss();
					dialog.show();
				}
				break;
				// ------------
			case 9:
				if (msg.arg1 < 0) {
					if (mSettopStatus_DATA.getWatchingchannel() != null && mAdapter == null) {
						TVChannelTask();
					}
				}else {
					if(((TV_ALL_DATA)getListView().getItemAtPosition(0)).getID() != null)
						mAdapter.notifyDataSetChanged();
					dialog.setWarningDialogType(CNM_WarningDialog.Record_NotService);
					pro.dismiss();
					dialog.show();
				}
				break;
			case 23:
				if (msg.arg1 < 0) {
					if (mSettopStatus_DATA.getWatchingchannel() != null && mAdapter == null) {
						TVChannelTask();
					}
				}else {
					if(((TV_ALL_DATA)getListView().getItemAtPosition(0)).getID() != null)
						mAdapter.notifyDataSetChanged();
					dialog.setWarningDialogType(CNM_WarningDialog.Record_NotService);
					pro.dismiss();
					dialog.show();
				}
				break;
			case 24:
				if (msg.arg1 < 0) {
					if (mSettopStatus_DATA.getWatchingchannel() != null && mAdapter == null) {
						TVChannelTask();
					}
				}else {
					if(((TV_ALL_DATA)getListView().getItemAtPosition(0)).getID() != null)
						mAdapter.notifyDataSetChanged();
					dialog.setWarningDialogType(CNM_WarningDialog.Record_NotService);
					pro.dismiss();
					dialog.show();
				}
				break;
				// -----
			case 12:
				if (msg.arg1 < 0) {
					if (mSettopStatus_DATA.getWatchingchannel() != null && mAdapter == null) {
						TVChannelTask();
					}
				}else {
					if(((TV_ALL_DATA)getListView().getItemAtPosition(0)).getID() != null)
						mAdapter.notifyDataSetChanged();
					dialog.setWarningDialogType(CNM_WarningDialog.Record_UserLimit);
					pro.dismiss();
					dialog.show();
				}
				break;
			case 13:
				if (msg.arg1 < 0) {
					if (mSettopStatus_DATA.getWatchingchannel() != null && mAdapter == null) {
						TVChannelTask();
					}
				}else {
					if(((TV_ALL_DATA)getListView().getItemAtPosition(0)).getID() != null)
						mAdapter.notifyDataSetChanged();
					dialog.setWarningDialogType(CNM_WarningDialog.Record_UserLimit);
					pro.dismiss();
					dialog.show();
				}
				break;
			case 4:
				// dialog.setWarningDialogType(CNM_WarningDialog.Record_SettopBoxPorwer);
				mTransLayout.setVisibility(View.VISIBLE);
				getListView().setEnabled(false);
				pro.dismiss();
				// dialog.show();

				break;
			case 666:
				CheckSettopStatus(null);
				break;
			case 28:
				pro.dismiss();
				tempApp.getTransLayout().setVisibility(View.VISIBLE);
				tempApp.getPower().setEnabled(false);
				tempApp.getVolUP().setEnabled(false);
				tempApp.getVolDOWN().setEnabled(false);
				getListView().setEnabled(false);
				settopPowerOffAlertShow();
				break;
			case 3303:
				mList.clear();
				CheckSettopStatus(null);
				break;
			case 3304:
				pro.dismiss();
				Toast.makeText(getParent().getParent(), "서버 오류: "+ msg.getData().getString(NET_ERR_TASK), Toast.LENGTH_SHORT).show();
				break;
			default:
				pro.dismiss();
				break;
			}

		};

	};

	
	private void ChangingChannel(Integer aChannelIdx){
		if (mChannelTask == null) {
			mChannelTask = new RemoteControlAsyncTask();
			mChannelTask.execute(XML_Address_Define.RemoteController
					.getSetremotechannelcontrol(
							Udid,
							((TV_ALL_DATA) getListView()
									.getItemAtPosition(
											aChannelIdx))
											.getID()),
											((TV_ALL_DATA) getListView().getItemAtPosition(
													aChannelIdx)).getID());
		} else if (mChannelTask.getStatus() != AsyncTask.Status.PENDING
				&& mChannelTask.getStatus() != AsyncTask.Status.RUNNING
				&& mChannelTask.getStatus() == AsyncTask.Status.FINISHED) {
			mChannelTask = new RemoteControlAsyncTask();
			mChannelTask.execute(
					XML_Address_Define.RemoteController
					.getSetremotechannelcontrol(
							Udid,
							((TV_ALL_DATA) getListView()
									.getItemAtPosition(
											aChannelIdx))
											.getID()),
											((TV_ALL_DATA) getListView().getItemAtPosition(
													aChannelIdx)).getID());
		}

	}

	private void TVChannelTask(){
		if (mTvAllAsyncTask == null)
			mTvAllAsyncTask = new TvAllAsyncTask();
		synchronized (mTvAllAsyncTask) {
			if (mTvAllAsyncTask.getStatus().equals(AsyncTask.Status.PENDING)) {
				mTvAllAsyncTask.execute(null);
			} else if (!mTvAllAsyncTask.getStatus().equals(
					AsyncTask.Status.PENDING)
					&& !mTvAllAsyncTask.getStatus().equals(
							AsyncTask.Status.RUNNING)
							&& mTvAllAsyncTask.getStatus().equals(
									AsyncTask.Status.FINISHED)) {
				mTvAllAsyncTask = new TvAllAsyncTask();
				mTvAllAsyncTask.execute(null);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_channel_listview);
		mTransLayout = (RelativeLayout) findViewById(R.id.TransLayout);
		tempApp = (CNMApplication) getApplication();
		
		dialog = new CNM_WarningDialog(getParent().getParent());
		Udid = tempApp.getMobileIMIE(this);
		mList = new TV_ALL_DATA_LIST();
		tempTVch_Check = tempApp.getTVCh_CheckMap();

		tempApp.getMainrbThird().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(tempApp.getmRemoteTabGroup() != null)
					tempApp.getmRemoteTabGroup().check(R.id.remocon_menu_allch);
					onResume();
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		
		/*StringBuffer stacktrace = new StringBuffer();
		StackTraceElement[] stackTrace = new Exception().getStackTrace();
		for(int x=0; x<stackTrace.length; x++)
		{
		 stacktrace.append(stackTrace[x].toString() + "\n");
		}
		Log.d(DEBUG_TAG, stacktrace.toString());
		Log.d(DEBUG_TAG, "---------------------------- onResume()");*/
		super.onResume();
		mReloadTimer = new Timer();
		TimerTask mReloadTask =  new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d("Sangtae", "Alaram Receiver ");
				Message tMessage = WarningHandler.obtainMessage();
				tMessage.what = 3303;
				WarningHandler.sendMessage(tMessage);
			}
		};
		mReloadTimer.schedule(mReloadTask, AlarmTimer, AlarmTimer);
		tempApp.setGlobalRemotHandl(WarningHandler);
		CheckSettopStatus(null);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mReloadTimer.cancel();
	}

	private void CheckSettopStatus(Integer aChannelIdx) {

		if (mSettoStatusTask == null) 
			mSettoStatusTask = new SettopStatusTask();

		synchronized (mSettoStatusTask) {
			if (tempApp.GetNetworkInfo() != 2) {

				if (!mSettoStatusTask.getStatus().equals(
						AsyncTask.Status.PENDING)
						&& !mSettoStatusTask.getStatus().equals(
								AsyncTask.Status.RUNNING)
								&& mSettoStatusTask.getStatus().equals(
										AsyncTask.Status.FINISHED)) {
					mSettoStatusTask = new SettopStatusTask();
				}

				if(mSettoStatusTask.getStatus().equals(AsyncTask.Status.PENDING))
					if (aChannelIdx != null) {
						mSettoStatusTask.execute(
								XML_Address_Define.RemoteController
								.getGetsettopstatus(Udid), String
								.valueOf(aChannelIdx));
					} else {
						if (!this.hasWindowFocus())
						mSettoStatusTask.execute(
								XML_Address_Define.RemoteController
								.getGetsettopstatus(Udid), null);
					}

			} else {
				AlertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
			}
		}

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			moveCurrentChannelFocues();
		}
	}

	private void moveCurrentChannelFocues(){
		if(mSettopStatus_DATA != null)
			if (mSettopStatus_DATA.getWatchingchannel().equals("null")) {
				getListView().setSelection(0);
			}else{

				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).getID() != null) {
						if (mSettopStatus_DATA.getWatchingchannel().equals(
								mList.get(i).getID()) && mSettopStatus_DATA.getState().equals("0")) {
							if ((getListView().getChildCount() / 2) > 0) {
								getListView().setSelection(
										i - (getListView().getChildCount() / 2));
								/*if (getListView().getChildAt(i) != null) {
									getListView().getChildAt(i).setEnabled(false);*/
									return;
								//}
							}
						}
					}
				}
				
				/*if (getListView().getChildAt(0) != null) {
					getListView().getChildAt(0).setEnabled(false);
				}*/
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

	private void setupUI() {

		mAdapter = new TVCH_All_Adapter(this, mList);
		setListAdapter(mAdapter);
		moveCurrentChannelFocues();
		pro.dismiss();

	}
	
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);
		// Log.d("Sangtae", "onListItemClick: "+mList.get(position).getID());
		tempApp.ButtonBeepPlay();
		CheckSettopStatus(position);// ((TV_ALL_DATA)getListView().getItemAtPosition(position)).getID());

	}

	public class TVCH_All_Adapter extends BaseAdapter {
		private LayoutInflater mInflater;
		// private Context context;
		private TV_ALL_DATA_LIST items;
		private final TVCHImageDownloader imageDownloader = new TVCHImageDownloader(
				getApplicationContext());

		public TVCH_All_Adapter(Context context, TV_ALL_DATA_LIST items) { // int
			// textViewResourceId,

			mInflater = LayoutInflater.from(context);
			imageDownloader.setContext(getApplicationContext());
			// this.context = context;
			this.items = items;
			showSettopStatusRow();
		}

		private void showSettopStatusRow() {
			TV_ALL_DATA tData = new TV_ALL_DATA();
			if (mSettopStatus_DATA.getState().equals("0")) {
				if (this.items.size() > 0) {
					if (this.items.get(0).getID() == null) {
						this.items.remove(0);
					}
				}
			} else if (mSettopStatus_DATA.getState().equals("1")) {
				tData.setCHANNEL_onAIR_TITLE("VOD시청중");
				mSettopStatus_DATA.setWatchingchannel("null");
			} else if (mSettopStatus_DATA.getState().equals("2")) {
				tData.setCHANNEL_onAIR_TITLE("Joy & Lift 시청중");
				mSettopStatus_DATA.setWatchingchannel("null");
			} else if (mSettopStatus_DATA.getState().equals("5")) {
				tData.setCHANNEL_onAIR_TITLE("미디어 앨범 시청중");
				mSettopStatus_DATA.setWatchingchannel("null");
			} else if (mSettopStatus_DATA.getState().equals("6")) {
				tData.setCHANNEL_onAIR_TITLE("녹화물 시청중");
				mSettopStatus_DATA.setWatchingchannel("null");
			}

			if (tData.getCHANNEL_onAIR_TITLE() != null) {
				this.items.add(0, tData);
			} else if (tData.getCHANNEL_onAIR_TITLE() == null
					&& this.items.size() > 0
					&& !mSettopStatus_DATA.getState().equals("0")) {
				this.items.get(0).setCHANNEL_onAIR_TITLE(
						tData.getCHANNEL_onAIR_TITLE());
			}
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			showSettopStatusRow();
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getID() != null)
					if (items.get(i).getID().equals(mSettopStatus_DATA.getWatchingchannel()) 
							&& !mSettopStatus_DATA.getState().equals("1") 
							&& !mSettopStatus_DATA.getState().equals("2")
							&& !mSettopStatus_DATA.getState().equals("5")
							&& !mSettopStatus_DATA.getState().equals("6")) {
						getListView().setSelection(i - (getListView().getChildCount() / 2));
					}
			}
			super.notifyDataSetChanged();
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TV_ALL_DATA tItem;
			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.remocon_channel_row,
						null);
				// Log.d("Sangtae", "convertView Null");
				holder = new ViewHolder();
				holder.Mych_Icon = (ImageView) convertView
						.findViewById(R.id.icon_mych);
				holder.Ch_num = (TextView) convertView
						.findViewById(R.id.ch_number);
				holder.log_img = (ImageView) convertView
						.findViewById(R.id.channel_logo);

				holder.Ch_Subject = (TextView) convertView
						.findViewById(R.id.program_subject);
				holder.Ch_Subject.setSelected(true);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}
			// Bind the data efficiently with the holder.

			tItem = items.get(position);

			if (tItem.getID() == null) {
				holder.Mych_Icon.setVisibility(View.GONE);
				holder.Ch_num.setVisibility(View.GONE);
				holder.log_img.setVisibility(View.GONE);

				holder.Ch_Subject.setGravity(Gravity.CENTER);
				holder.Ch_Subject.setText(tItem.getCHANNEL_onAIR_TITLE());
				convertView.setBackgroundResource(R.drawable.remocon_livebar);
				return convertView;
			} else {
				holder.Mych_Icon.setVisibility(View.VISIBLE);
				holder.Ch_num.setVisibility(View.VISIBLE);
				holder.log_img.setVisibility(View.VISIBLE);
				convertView.setBackgroundResource(R.drawable.list_bg);
				// holder.Ch_Subject.setGravity(Gravity.CENTER);
			}

			if (mSettopStatus_DATA != null) {
				if (tItem.getID().equals(
						mSettopStatus_DATA.getWatchingchannel())) {
					convertView.setEnabled(false);
				} else {
					convertView.setEnabled(true);
				}
			}

			// 내용
			holder.Ch_num.setText(String.format("Ch %s", tItem.getNUMBER()));
			if (tItem.getCHANNEL_onAir_TIME() != null) {
				holder.Ch_Subject.setText(ToHourTime(tItem
						.getCHANNEL_onAir_TIME())
						+ tItem.getCHANNEL_onAIR_TITLE());
			}
			// 로그
			String TempIMG = tItem.getCHANNEL_LOG_IMG();
			if (TempIMG != null) {
				imageDownloader.download(tItem.getCHANNEL_LOG_IMG(),
						holder.log_img, null);// ,
				// BitmapFactory.decodeResource(getResources(),
				// R.drawable.noimg_logo));
			}

			// My ch
			if (tempTVch_Check.size() != 0) {
				if (tempTVch_Check.get(mList.get(position).getID()) != null) {
					if (tempTVch_Check.get(mList.get(position).getID())
							.getMyChannel()) {
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
			return convertView;
		}

	}

	static class ViewHolder {
		ImageView Mych_Icon;
		TextView Ch_num;
		ImageView log_img;

		TextView Ch_Subject;

	}

	private String ToHourTime(String aTime) {
		if (aTime.length() == 0) {
			return "";
		} else {
			SimpleDateFormat mOriTime = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
			SimpleDateFormat HourTime = new SimpleDateFormat("HH:mm ");

			ParsePosition pos = new ParsePosition(0);
			Date frmTime = mOriTime.parse(aTime, pos);
			return HourTime.format(frmTime);

		}
	}

	// 리스트 스레드
	class TvAllAsyncTask extends AsyncTask<Void, Void, TV_ALL_DATA_LIST> {

		Toast toast;
		@Override
		protected TV_ALL_DATA_LIST doInBackground(Void... params) {
			TV_ALL_PASER paser = new TV_ALL_PASER();

			if(tempApp.getTVCH_SettigChecked()) {

				Log.d("Sangtae", "전체 채널 정보 새로 파싱");
				String requestURL = XML_Address_Define.Channel.getGetchannellist(
						tempApp.getLocationID(), tempApp.getProductID(), "",
						XML_Address_Define.Channel.CHANNEL_LSIT.ALL);
				Log.d(DEBUG_TAG, "### URL Request 전체 채널 정보" + requestURL);

				paser.start(requestURL);
				return paser.getTV_ALL_DATA_LIST();

			} else{ Log.d("Sangtae", "기존 전체 채널 정보 로딩"); 
			return tempApp.getTv_ALL_DATA_LIST(); 
			}


		}

		@Override
		protected void onPostExecute(TV_ALL_DATA_LIST result) {

			super.onPostExecute(result);
			HashMap<String, TV_ALL_DATA> mSortDataMap = new HashMap<String, TV_ALL_DATA>();
			try {
				mList.clear();
				int temp = 0;
				for (int i = 0; i < result.size(); i++) {
					String tempStr = result.get(i).getNUMBER();
					// Log.d("Sangtae",
					// "result.get("+i+").getNUMBER(): "+tempStr+", result.get(i).getCHANNEL_VIEW(): "+result.get(i).getCHANNEL_VIEW());
					if (tempStr != null) {

						int temptemp = Integer.parseInt(tempStr);
						mSortDataMap.put(tempStr, result.get(i));
						if (temp <= temptemp) {
							temp = temptemp;
						}
					} else {
						Log.d("Sangtae", "result.get(" + i + ").getNUMBER(): "
								+ tempStr
								+ ", result.get(i).getCHANNEL_VIEW(): "
								+ result.get(i).getCHANNEL_VIEW());

					}
				}

				for (int i = 0; i <= temp; i++) {
					if (mSortDataMap.containsKey(String.valueOf(i))) {
						String tempStr = mSortDataMap.get(String.valueOf(i))
								.getCHANNEL_VIEW();
						if (tempStr != null) {
							if (tempStr.equals("YES")) {
								mList.add(mSortDataMap.get(String.valueOf(i)));
							}
						} else {
							Log.d("Sangtae", "mSortDataMap.get("
									+ i
									+ ")).getCHANNEL_VIEW(): "
									+ mSortDataMap.get(String.valueOf(i))
									.getCHANNEL_VIEW());

						}
					}
				}

				// mList=result;
				if (mList.size() > 0) {
					setupUI();
					pro.dismiss();
				} else {
					toast.show();
					onResume();
				}
			} catch (Exception e) {

				Log.w("Sangtae", e);
				pro.dismiss();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			toast = Toast.makeText(Remote_All_CH.this, getResources().getString(R.string.vod_error), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			pro = ProgressDialog.show(getParent().getParent(), "",
					getResources().getString(R.string.tv_watting), true);
		}
	}

	class RemoteControlAsyncTask extends AsyncTask<String, Void, RESPONSE_DATA> {

		String mMoveChannelId;

		@Override
		protected RESPONSE_DATA doInBackground(String... params) {

			String requestURL = params[0];
			mMoveChannelId = params[1];
			Log.d(DEBUG_TAG, "### URL Request 리모콘 채널 변경: " + requestURL);
			if (CHECK_RESPONSE_PASER.start(requestURL)) {
				return CHECK_RESPONSE_PASER.getArrayList();
			}

			return null;
		}

		@Override
		protected void onPostExecute(RESPONSE_DATA result) {

			super.onPostExecute(result);
			pro.dismiss();
			Message tMessage = WarningHandler.obtainMessage();
			if (result != null) {
				//CheckSettopStatus(null);
				if (result.getResultCode().contains(
						XML_Address_Define.ErrorCode.ERROR_100)) {
					Log.d("Sangtae", "리모콘 채널 변경 성공");
					tMessage.what = 0;
					tMessage.arg1 = -1;
					tMessage.arg2 = Integer.parseInt(mMoveChannelId);
					WarningHandler.sendMessage(tMessage);
				} else {
					Log.d("Sangtae", "리모콘 채널 변경 실패: " + result.getErrorString());
					tMessage.what = Integer.parseInt(result.getResultCode());
					WarningHandler.sendMessage(tMessage);
				}
			} else {
				// 에러 코드 표시
				tMessage.what = 3304;
				Bundle bundle = new Bundle();
				bundle.putString(NET_ERR_TASK, "응답이 없습니다.");
				tMessage.setData(bundle);
				WarningHandler.sendMessage(tMessage);
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn()
		.performClick();
	}

	private class SettopStatusTask extends
	AsyncTask<String, Void, SettopStatus_DATA> {
		private String mChanneldx;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pro = ProgressDialog.show(getParent().getParent(), "",
					getResources().getString(R.string.tv_watting), true);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			Log.d("Sangtae", "SettopStatusTask onCancelled" );
			pro.dismiss();
		}
		
		@Override
		protected void finalize() throws Throwable {
			// TODO Auto-generated method stub
			super.finalize();
			Log.d("Sangtae", "SettopStatusTask finalize" );
			pro.dismiss();
		}
		@Override
		protected SettopStatus_DATA doInBackground(String... params) {
			// TODO Auto-generated method stub
			mChanneldx = params[1];
			for (int i = 0; i < 2; i++) {
				if (CHECK_SETTOPSTATUS_PASER.start(params[0])) {
					return CHECK_SETTOPSTATUS_PASER.getData();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(SettopStatus_DATA result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			Message tMessage = WarningHandler.obtainMessage();
			if (result == null) {
				pro.dismiss();
				tMessage.what = 3304;
				Bundle bundle = new Bundle();
				bundle.putString(NET_ERR_TASK, "셋탑 상태 확인이 안됩니다.");
				tMessage.setData(bundle);
				WarningHandler.sendMessage(tMessage);
			} else {
				if (mSettopStatus_DATA == null) {
					mSettopStatus_DATA = result;
				}
				if (result.getResultCode().equals(
						XML_Address_Define.ErrorCode.ERROR_100)) {
					tMessage.what = Integer.parseInt(result.getState());
					if (mChanneldx != null) {
						tMessage.arg1 = Integer.parseInt(mChanneldx);
					} else {
						pro.dismiss();
						tMessage.arg1 = -1;
					}
					WarningHandler.sendMessage(tMessage);
				} else if (result.getResultCode().equals(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_028) 
						|| result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_206)) { // STB
					// OFF
					/*Message tMessage = WarningHandler.obtainMessage();
					tMessage.what = 4;
					WarningHandler.sendMessage(tMessage);*/
					//settopPowerOffAlertShow();
					//pro.dismiss();
					tMessage.what = 28;
					WarningHandler.sendMessage(tMessage);

				} else {
					pro.dismiss();
				}
			}
		}
	}

	private void settopPowerOffAlertShow() {
		LayoutInflater inflater = LayoutInflater.from(this);
		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
				getParent().getParent());
		View view = inflater.inflate(R.layout.remote_pop_layout, null);
		alert_internet_status.setView(view);
		alert_internet_status.setPositiveButton("확 인",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = alert_internet_status.create();
		alert.show();
	}
}
