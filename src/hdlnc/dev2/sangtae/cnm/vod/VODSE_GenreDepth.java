package hdlnc.dev2.sangtae.cnm.vod;

import twitter4j.internal.org.json.XML;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_GENRE_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_GENRE_PASER;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class VODSE_GenreDepth extends ListActivity{

	private CNMApplication tempApp;
	private VODSE_Genre_List_Adapter mAdapter;
	private VOD_GENRE_LIST mArrayList;
	
	private static int depth =0; 
	private String GenreID="";
	private String GenreTitle="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tempApp = (CNMApplication)getApplication();
		//----- navigetionBar Edit TogleButton --------
		Bundle bundle = getIntent().getExtras();
		GenreID = bundle.getString("GenreID");
		GenreTitle = bundle.getString("GenreTitle");
		
		ListView mListView = getListView();
		mListView.setSoundEffectsEnabled(false);
		mListView.setBackgroundResource(R.drawable.list_backbg_01);
		mListView.setDividerHeight(0);
		mListView.setDivider(getResources().getDrawable(R.drawable.transparent));
		
		new VodAsyncTask().execute(GenreID);
		
		
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
			TabGroupActivity parent =(TabGroupActivity) getParent();
			Intent subGenreIntent = new Intent(VODSE_GenreDepth.this, VODSE_GenreDepth.class);
			subGenreIntent.putExtra("GenreID", mArrayList.get(position).getID());
			subGenreIntent.putExtra("GenreTitle", mArrayList.get(position).getTitle());
			parent.startChildActivity("VodGenreDepth" + (++depth), subGenreIntent);
		}
		else
		{
			TabGroupActivity parent =(TabGroupActivity) getParent();
			Intent genreListIntent = new Intent(VODSE_GenreDepth.this, VODSE_Genre_List.class);
			genreListIntent.putExtra("GenreID", mArrayList.get(position).getID());
			genreListIntent.putExtra("GenreTitle", mArrayList.get(position).getTitle());
			parent.startChildActivity("VodGenreList", genreListIntent);
		}
		
	}
	
	
	private void setUpNaviArea() {
		NaviBtn_Singleton mNaviBtnSingleton 	=  tempApp.getNaviBtn_Singleton();
		Resources res = getResources(); 
		//Drawable drawableBack = res.getDrawable(R.drawable.navi_back_btn);
		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_back);//ImageDrawable(drawableBack);
		naviLeftBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				onBackPressed();
			}
		});
		ImageButton rightSubBtn = mNaviBtnSingleton.getNaviRightSubBtn();
		rightSubBtn.setVisibility(View.VISIBLE);
		
		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		naviRightBtn.setVisibility(View.VISIBLE);
		
		TextView txtHeader = mNaviBtnSingleton.getNaviHeaderText();
		txtHeader.setText(GenreTitle);
		
	}
	
	class VODSE_Genre_List_Adapter extends BaseAdapter
	{
		
		VOD_GENRE_LIST Items;
		LayoutInflater mInflater;
		public VODSE_Genre_List_Adapter(Context context, VOD_GENRE_LIST items)
		{
			mInflater = getLayoutInflater().from(context);
			
			Items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			convertView = mInflater.inflate(R.layout.vod_genre_row, null);
			TextView tv = (TextView)convertView.findViewById(R.id.vod_genreRoot_text);
			tv.setText(mArrayList.get(position).getTitle());
			
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
	
	class VodAsyncTask extends AsyncTask<String, Void, VOD_GENRE_LIST>
	{
		ProgressDialog pro;
		@Override
		protected VOD_GENRE_LIST doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try
			{
				VOD_GENRE_PASER GenrePaser = new VOD_GENRE_PASER(XML_Address_Define.Vod.getGetvodgenre( params[0].toString()));
				GenrePaser.start();
				return GenrePaser.getArrayList();
			}
			catch (Exception e) {
				// TODO: handle exception
				Log.d("VODSE_GenreDepth", "doInBackground Exception : " + e.getMessage());
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(VOD_GENRE_LIST result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			pro.dismiss();
			
			try
			{
				mArrayList = result;
				if(mArrayList == null)
				{
					onCancelled();
				}
				else
				if(mArrayList.getRESULT().equals(XML_Address_Define.ErrorCode.ERROR_100))
				{
					mAdapter = new VODSE_Genre_List_Adapter(VODSE_GenreDepth.this, mArrayList);
					setListAdapter(mAdapter);
				}
					
			}
			catch (Exception e) {
				// TODO: handle exception
				Log.w("VODSE_GerneDepth", e);
			}
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			pro = ProgressDialog.show(getParent().getParent(), "", getResources().getString(R.string.vod_watting),true);
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			
			AlertShow(getResources().getString(R.string.system_network_warring));
		}
		
		
	}
	
	private void AlertShow(String msg) {
		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
				getParent().getParent());
		alert_internet_status.setTitle("°æ °í");
		alert_internet_status.setMessage(msg);
		alert_internet_status.setPositiveButton("´Ý ±â",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); // ´Ý±â
				finish();
			}
		});
		AlertDialog alert = alert_internet_status.create();
		alert.show();
	}
	  @Override
	  public boolean onKeyUp(int keyCode, KeyEvent event) {
		  Log.d("Sangtae", "KEYCODE_BACK onKeyUp - TabGroupActivity");
	      if (keyCode == KeyEvent.KEYCODE_BACK) {
	          //onBackPressed();
	    	  tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().performClick();
	          return true;
	      }
	      return super.onKeyUp(keyCode, event);
	  }
}
