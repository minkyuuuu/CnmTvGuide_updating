package hdlnc.dev2.sangtae.cnm.vod;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_GENRE_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_GENRE_PASER;
import hdlnc.dev2.sangtae.cnm.main.main;
import hdlnc.dev2.sangtae.cnm.vod.VODSE_Preview.VodAsyncTask;


import hdlnc.dev2.sangtae.cnm.R;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class VODSE_GenreRoot extends ListActivity{

	private CNMApplication tempApp;
	private VODSE_Genre_List_Adapter mAdapter;
	private VOD_GENRE_LIST mArrayList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tempApp = (CNMApplication)getApplication();
		//----- navigetionBar Edit TogleButton --------

		ListView mListView = getListView();
		mListView.setSoundEffectsEnabled(false);
		mListView.setDividerHeight(0);
		mListView.setDivider(getResources().getDrawable(R.drawable.transparent));

		if (tempApp.GetNetworkInfo() != 2) {
			new VodAsyncTask().execute(null);
		} else {
			AlertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUpNaviArea();

	}

	@Override
	protected void onListItemClick(ListView listView, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(listView, v, position, id);

		tempApp.ButtonBeepPlay();

		if(mArrayList.get(position).getMore().equals("YES"))
		{
			TabGroupActivity parent =(TabGroupActivity) getParent().getParent();
			Intent subGenreIntent = new Intent(VODSE_GenreRoot.this, VODSE_GenreDepth.class);
			subGenreIntent.putExtra("GenreID", mArrayList.get(position).getID());
			subGenreIntent.putExtra("GenreTitle", mArrayList.get(position).getTitle());
			parent.startChildActivity("VodSubGenre", subGenreIntent);

			Log.d("VODSE_GenreRoot", "GenreID : " + mArrayList.get(position).getID() + " position : " + position);

		}
		else
		{
			TabGroupActivity parent =(TabGroupActivity) getParent().getParent();
			Intent subGenreIntent = new Intent(VODSE_GenreRoot.this, VODSE_Genre_List.class);
			subGenreIntent.putExtra("GenreID", mArrayList.get(position).getID());
			parent.startChildActivity("VodSubGenre", subGenreIntent);
		}


	}

	private void setUpNaviArea() {

		Resources res = getResources();

		NaviBtn_Singleton mNaviBtnSingleton 	=  tempApp.getNaviBtn_Singleton();
		ToggleButton naviLeftSubBtn = (ToggleButton)mNaviBtnSingleton.getNaviLeftSubBtn();
		naviLeftSubBtn.setVisibility(View.INVISIBLE);

		//Drawable drawableHome = res.getDrawable(R.drawable.navi_home_btn);
		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_home);//ImageDrawable(drawableHome);
		ImageButton rightSubBtn = mNaviBtnSingleton.getNaviRightSubBtn();
		rightSubBtn.setVisibility(View.VISIBLE);

		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		naviRightBtn.setVisibility(View.VISIBLE);

		TextView txtHeader = mNaviBtnSingleton.getNaviHeaderText();
		txtHeader.setText("VOD");

	}

	class VODSE_Genre_List_Adapter extends BaseAdapter
	{
		VOD_GENRE_LIST Items;
		LayoutInflater mInflater;
		public VODSE_Genre_List_Adapter(Context context, int res, VOD_GENRE_LIST items)
		{
			mInflater = getLayoutInflater().from(context);
			Items= items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			convertView = mInflater.inflate(R.layout.vod_genre_row, null);
			TextView tv = (TextView)convertView.findViewById(R.id.vod_genreRoot_text);
			tv.setText(Items.get(position).getTitle());

			return convertView; 
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return Items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub

			return position;
		}

	}


	class VodAsyncTask extends AsyncTask<Void, Void, VOD_GENRE_LIST>
	{
		Toast toast;
		ProgressDialog pro;
		@Override
		protected VOD_GENRE_LIST doInBackground(Void... params) {
			// TODO Auto-generated method stub

			if(VOD_GENRE_PASER.getArrayList() ==null)
			{

				VOD_GENRE_PASER PARSER = new VOD_GENRE_PASER(XML_Address_Define.Vod.getGetvodgenre(""));
				PARSER.start();
				tempApp.setVod_GENRE_DATA_LIST(PARSER.getArrayList());
				return PARSER.getArrayList();

			}
			else
				return tempApp.getVod_GENRE_DATA_LIST();


		}

		@Override
		protected void onPostExecute(VOD_GENRE_LIST result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			pro.dismiss();

			try
			{
				mArrayList =result;

				if(mArrayList == null)
				{
					onCancelled();
				}
				else
					if(mArrayList.getRESULT().equals(XML_Address_Define.ErrorCode.ERROR_100))	
					{
						mAdapter = new VODSE_Genre_List_Adapter(VODSE_GenreRoot.this, R.layout.vod_genre_row , mArrayList);
						setListAdapter(mAdapter);
					}else
						 toast.show();

			}
			catch (Exception e) {
				// TODO: handle exception
				Log.w("VODSE_GenreRoot", e);
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			toast = Toast.makeText(VODSE_GenreRoot.this, getResources().getString(R.string.vod_error), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			pro = ProgressDialog.show(getParent().getParent(), "", getResources().getString(R.string.vod_watting),true);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			toast.show();
			//AlertShow(getResources().getString(R.string.system_network_warring));
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
		// TODO Auto-generated method stub
		//super.onBackPressed();
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().performClick();
	}
}
