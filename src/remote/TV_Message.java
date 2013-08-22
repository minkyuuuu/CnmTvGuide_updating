package remote;

import static hdlnc.dev2.sangtae.cnm.global.TV_MSG_SQLiteOpenHelper.MESSAGE_BODY;
import static hdlnc.dev2.sangtae.cnm.global.TV_MSG_SQLiteOpenHelper.MESSAGE_DATE;
import static hdlnc.dev2.sangtae.cnm.global.TV_MSG_SQLiteOpenHelper.MESSAGE_ID;
import static hdlnc.dev2.sangtae.cnm.global.TV_MSG_SQLiteOpenHelper.TV_MSG_TABLE;
import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.CNM_WarningDialog;
import hdlnc.dev2.sangtae.cnm.global.TV_MSG_Data;
import hdlnc.dev2.sangtae.cnm.global.TV_MSG_SQLiteOpenHelper;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.CHECK_RESPONSE_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.CHECK_SETTOPSTATUS_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.RESPONSE_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.SettopStatus_DATA;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TV_Message extends ListActivity implements OnTouchListener{

	public static final String DEBUG_TAG = "TV_Message";

	private CNMApplication tempApp;

	private TV_MSG_SQLiteOpenHelper mHelper;
	private SQLiteDatabase	mDatabase;

	private String Udid;

	private ArrayList<TV_MSG_Data> mList;
	private TvMsgAdapter mAdapter; 

	private EditText	mInputEdit;
	private Button 		mSendBtn;

	private ProgressDialog pro;
	private SettopStatus_DATA mSettopStatus_DATA;
	private SettopStatusTask mSettoStatusTask;

	private CNM_WarningDialog dialog;

	private RemoteControlAsyncTask mTask;
	private Handler mHandler;
	private Boolean sendMsgLock = false;
	private String ErrMsg = "셋탑이 대기모드에 있는 경우 TV메시지를 전송 할 수  없습니다.";
	private Handler WarningHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			//settopPowerOffAlertShow();
			//msg.arg1  -1이면 상태 검사
			if (msg.what != 28) {
				tempApp.getTransLayout().setVisibility(View.GONE);
				tempApp.getPower().setEnabled(true);
				tempApp.getVolUP().setEnabled(true);
				tempApp.getVolDOWN().setEnabled(true);
			}
			sendMsgLock = true;
			switch (msg.what) {
			case 0:
				pro.dismiss();	
				break;
			case 1:
				pro.dismiss();	
				Toast.makeText(getParent().getParent(),	"VOD 시청중에는 채널이 변경되지 않습니다.", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				pro.dismiss();	
				Toast.makeText(getParent().getParent(),	"Joy & Lift 시청중에는 채널이 변경되지 않습니다.", Toast.LENGTH_SHORT).show();
				break;
			case 5:
				pro.dismiss();	
				Toast.makeText(getParent().getParent(),	"미디어 앨범 시청중에는 채널이 변경되지 않습니다.", Toast.LENGTH_SHORT).show();
				break;
			case 6:
				pro.dismiss();	
				Toast.makeText(getParent().getParent(),	"녹화물 시청중에는 채널이 변경되지 않습니다.", Toast.LENGTH_SHORT).show();
				break;
			case 11:
				dialog.setWarningDialogType(CNM_WarningDialog.LIVE_Record_Dupli);
				pro.dismiss();
				dialog.show();
				break;
			case 10:
				dialog.setWarningDialogType(CNM_WarningDialog.LIVE_Record_PIP);
				pro.dismiss();
				dialog.show();
				break;
			case 3:
				dialog.setWarningDialogType(CNM_WarningDialog.Record_OverStorag);
				pro.dismiss();
				dialog.show();
				break;
				// ------------
			case 9:
				dialog.setWarningDialogType(CNM_WarningDialog.Record_NotService);
				pro.dismiss();
				dialog.show();
				break;
			case 23:
				dialog.setWarningDialogType(CNM_WarningDialog.Record_NotService);
				pro.dismiss();
				dialog.show();
				break;
			case 24:
				dialog.setWarningDialogType(CNM_WarningDialog.Record_NotService);
				pro.dismiss();
				dialog.show();
				break;
				// -----
			case 12:
				dialog.setWarningDialogType(CNM_WarningDialog.Record_UserLimit);
				pro.dismiss();
				dialog.show();
				break;
			case 13:
				dialog.setWarningDialogType(CNM_WarningDialog.Record_UserLimit);
				pro.dismiss();
				dialog.show();
				break;
			case 4:
				// 팝업 셋탑 사용 할수 없음 팝업 표시
				pro.dismiss();
				ErrMsg = "셋탑이 대기모드에 있는 경우 TV메시지를 전송 할 수  없습니다.";
				sendMsgLock = false;
				break;
			case 28:
				ErrMsg = "셋탑 전원이 꺼져 있는 경우 TV메시지를 전송 할 수  없습니다.";
				sendMsgLock = false;
				pro.dismiss();
				tempApp.getTransLayout().setVisibility(View.GONE);
				tempApp.getPower().setEnabled(false);
				tempApp.getVolUP().setEnabled(false);
				tempApp.getVolDOWN().setEnabled(false);
				getListView().setEnabled(false);
				settopPowerOffAlertShow();
				break;
			default:
				pro.dismiss();
				break;
			}
			// Toast.makeText(getParent().getParent(),
			// String.format("리모콘 셋탑 리스폰 값: %d", msg.what),
			// Toast.LENGTH_SHORT).show();

		};

	};

	InputMethodManager mgr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_tv_msg_list);
		tempApp = (CNMApplication)getApplication();
		Udid = tempApp.getMobileIMIE(this);
		dialog = new CNM_WarningDialog(getParent().getParent());
		mHelper = new TV_MSG_SQLiteOpenHelper(this);
		mDatabase = mHelper.getWritableDatabase();
		mTask = new RemoteControlAsyncTask();
		mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mInputEdit	= (EditText)findViewById(R.id.TvMessage_Body);
		mSendBtn	= (Button)findViewById(R.id.TvMessage_SendBtn);
		mgr.showSoftInput(mInputEdit, InputMethodManager.SHOW_IMPLICIT);
		
		
		
		mList = new ArrayList<TV_MSG_Data>();
		LoadDB();
		mAdapter = new TvMsgAdapter(this);
		setListAdapter(mAdapter);
		getListView().smoothScrollToPosition(getListView().getCount());

		mInputEdit.setOnTouchListener(this);
		mInputEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				int a;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				int a;

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() == 60) {
					Toast.makeText(TV_Message.this, "메시지 60자 제한", Toast.LENGTH_SHORT).show();
				}
				
				if (sendMsgLock) {
					
				} else {

				}
			}
		});

		mSendBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mInputEdit.getText().toString().length() > 0) {
					if (mTask.getStatus() != Status.RUNNING) {
						if (mTask.getStatus() == AsyncTask.Status.FINISHED)
							mTask = new RemoteControlAsyncTask();
						mTask.execute(XML_Address_Define.RemoteController.getSetremotemessage(Udid, mInputEdit.getText().toString()));
					}
				}
				//getListView().smoothScrollToPosition(getListView().getCount());
				//mAdapter.notifyDataSetChanged();
			}
		});

		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				mgr.hideSoftInputFromWindow(mInputEdit.getWindowToken(), 0);
				if (msg.what == 1001) {
					SimpleDateFormat mCurrentTime = new SimpleDateFormat("MM.dd (E) HH:mm", Locale.KOREAN);
					AddColum(new TV_MSG_Data(mCurrentTime.format(new Date(System.currentTimeMillis())), mInputEdit.getText().toString()));
					mInputEdit.setText("");
					mAdapter.notifyDataSetChanged();
				} else if (msg.what == 1002) {
					Toast.makeText(getApplicationContext(), "메시지 전달에 실패 하였습니다.", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		CheckSettopStatus(null);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mDatabase.close();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn()
		.performClick();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(sendMsgLock){
			mInputEdit.setFocusable(true);
		}else{
			mInputEdit.setFocusable(false);
			Toast.makeText(getParent().getParent(), ErrMsg, Toast.LENGTH_SHORT).show();
		}
		return false;
	}





	private class TvMsgAdapter extends BaseAdapter{
		private LayoutInflater mInflater;

		public TvMsgAdapter(Context context) {
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			LoadDB();
			getListView().smoothScrollToPosition(getListView().getCount());
			super.notifyDataSetChanged();
		}



		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return mList.get(position).getDB_ID();
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			TV_MSG_Data data = mList.get(position);

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.tv_message_row, null);
				//Log.d("Sangtae", "convertView Null");
				holder = new ViewHolder();
				holder.Delte_btn	= (ImageButton)convertView.findViewById(R.id.tvmessage_icon_delete);
				holder.Msg_Time		= (TextView)convertView.findViewById(R.id.tvmessage_date);
				holder.Msg_Body		= (TextView)convertView.findViewById(R.id.tvmessage_msg);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			holder.Msg_Time.setText(data.getDB_Msg_Time());
			holder.Msg_Body.setText(data.getDB_Msg());

			holder.Delte_btn.setId(data.getDB_ID());
			holder.Delte_btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Log.d("Sangtae", "TV 메시지 ID: "+v.getId());
					DeleteColum(v.getId());
				}
			});

			return convertView;
		}
	}

	static class ViewHolder {
		ImageButton	Delte_btn;
		TextView	Msg_Time;
		TextView	Msg_Body;

	}





	private void LoadDB() {
		// TODO Auto-generated method stub
		mList.clear();
		LoadTvMsgList();
	}


	private void LoadTvMsgList() {
		Cursor dataCursor = mDatabase.query(TV_MSG_TABLE, 
				new String[] {MESSAGE_ID, MESSAGE_DATE, MESSAGE_BODY}, 	
				null, null, null, null, null);

		dataCursor.moveToFirst();
		TV_MSG_Data data;
		if (! dataCursor.isAfterLast()) {
			do {
				data = new TV_MSG_Data();
				data.setDB_ID(dataCursor.getInt(0));
				data.setDB_Msg_Time(dataCursor.getString(1));
				data.setDB_Msg(dataCursor.getString(2));


				mList.add(data);
			} while(dataCursor.moveToNext());
		}
		dataCursor.close();
	}

	private void DeleteColum(Integer ID) {
		// TODO Auto-generated method stub
		assert (null != ID);

		String where = String.format("%s='%d'", MESSAGE_ID, ID);
		Integer temp = mDatabase.delete(TV_MSG_TABLE, where, null);
		if (temp > 0) {
			mAdapter.notifyDataSetChanged();
		}

		/*for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i).getDB_ID() == ID) {
				mList.remove(i);
				break;
			}
		}*/
	}

	private void AddColum(TV_MSG_Data data) {
		// TODO Auto-generated method stub
		assert(null != data);

		ContentValues values = new ContentValues();
		//values.put(MESSAGE_ID, data.getDB_ID());
		values.put(MESSAGE_DATE, data.getDB_Msg_Time());
		values.put(MESSAGE_BODY, data.getDB_Msg());
		mDatabase.insert(TV_MSG_TABLE, null, values);
	}

	private void UpdateColum(TV_MSG_Data data) {
		assert (null != data);


		ContentValues values = new ContentValues();
		String where = String.format("%s = '%d;", MESSAGE_ID, data.getDB_ID());
		values.put(MESSAGE_ID, data.getDB_ID());
		values.put(MESSAGE_DATE, data.getDB_Msg_Time());
		values.put(MESSAGE_BODY, data.getDB_Msg());

		mDatabase.update(TV_MSG_TABLE, values, where, null);

		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i).getDB_ID() == data.getDB_ID()) {
				mList.add(i, data);
				mList.remove(i+1);
				break;
			}
		}

	}

	class RemoteControlAsyncTask extends AsyncTask<String, Void, RESPONSE_DATA>{
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
			String requestURL = params[0];
			Log.d(DEBUG_TAG, "### URL Request 메세지전송: "+requestURL);
			if (CHECK_RESPONSE_PASER.start(requestURL)) {
				return CHECK_RESPONSE_PASER.getArrayList();
			}

			return null;
		}

		@Override
		protected void onPostExecute(RESPONSE_DATA result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pro.dismiss();
			if (result != null) {
				if (result.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)) {
					Log.d("Sangtae", "메시지 전송 성공");
					mHandler.sendMessage(mHandler.obtainMessage(1001));
				}else {
					Log.d("Sangtae", "메시지 전송  실패: "+ result.getErrorString());
					mHandler.sendMessage(mHandler.obtainMessage(1002));
				}
			} else {
				// 에러 코드 표시
				//Log.d("Sangtae", "메시지 전송  실패: "+ result.getErrorString());
				mHandler.sendMessage(mHandler.obtainMessage(1002));
			}
		}
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

			if (result == null) {
				pro.dismiss();
			} else {
				if (mSettopStatus_DATA == null) {
					mSettopStatus_DATA = result;
				}
				if (result.getResultCode().equals(
						XML_Address_Define.ErrorCode.ERROR_100)) {
					Message tMessage = WarningHandler.obtainMessage();
					tMessage.what = Integer.parseInt(result.getState());
					if (mChanneldx != null) {
						tMessage.arg1 = Integer.parseInt(mChanneldx);
					} else {
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
					Message tMessage = WarningHandler.obtainMessage();
					tMessage.what = 28;
					WarningHandler.sendMessage(tMessage);

				} else {
					pro.dismiss();
				}
			}
		}
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
						mSettoStatusTask.execute(
								XML_Address_Define.RemoteController
								.getGetsettopstatus(Udid), null);
					}

			} else {
				AlertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
			}
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
