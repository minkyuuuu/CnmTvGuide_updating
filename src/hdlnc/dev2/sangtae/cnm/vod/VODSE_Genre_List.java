package hdlnc.dev2.sangtae.cnm.vod;

import hdlnc.dev2.sangtae.cnm.R;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.ImageDownloader;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_GENRE_DETAIL_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_GENRE_DETAIL_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_INFO;
import hdlnc.dev2.sangtae.cnm.settings.Setting_Adult_Auth;

import java.io.File;
import java.util.ArrayList;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class VODSE_Genre_List extends ListActivity {
	//private final ImageDownloader imageDownloader = new ImageDownloader();
	
	
	private CNMApplication tempApp;
	NaviBtn_Singleton mNaviBtnSingleton;
	private VODSE_Genre_List_Adapter mAdapter;
	private VOD_GENRE_DETAIL_LIST mArrayList;
	
	private static final String PACKGE_NAME = "hdlnc.dev2.sangtae.cnm/cache";
	private final static String DEBUG_TAG = "VODSE_Genre_List";
	
	private String GenreID="";
	private String GenreTitle="";
	
	private String URLs[];
	private String ImagesName[];
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tempApp = (CNMApplication)getApplication();
		mNaviBtnSingleton =  tempApp.getNaviBtn_Singleton();

		Bundle bundle = getIntent().getExtras();
		GenreID = bundle.getString("GenreID");
		GenreTitle = bundle.getString("GenreTitle");
		
		new GenreItemListTask().execute(GenreID);
		
		ListView mListView = getListView();
		mListView.setSoundEffectsEnabled(false);
		mListView.setBackgroundResource(R.drawable.list_backbg_01);
		mListView.setDividerHeight(0);
		mListView.setDivider(getResources().getDrawable(R.drawable.transparent));

	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUpNaviArea();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		tempApp.ButtonBeepPlay();
		
		if(!tempApp.getAdultAuth() && 
				mArrayList.get(position).getGrade().equals(getResources().getString(R.string.vod_grade_19)))
		{
			
			TabGroupActivity parent =(TabGroupActivity) getParent();
			Intent subGenreIntent = new Intent(getParent(), Setting_Adult_Auth.class);
			parent.startChildActivity("VodAdultAuth", subGenreIntent);
			
		}
		else
		{	
			TabGroupActivity parent =(TabGroupActivity) getParent();
			Intent subGenreIntent = new Intent(getParent(), VODSE_Detailview.class);
			subGenreIntent.putExtra("VOD_ITEM", mArrayList.get(position));
			parent.startChildActivity("VodDetailView", subGenreIntent);
			
		}

	}


	public class VODSE_Genre_List_Adapter extends BaseAdapter{

		private final ImageDownloader imageDownloader = new ImageDownloader();
		private LayoutInflater mInflater;
		//private Context context;
		private ArrayList<VOD_INFO> items;

		private final Bitmap NoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noimg_listposter);
		private final Bitmap AdultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.posterbg_list_19);
		
		OnCheckedChangeListener hideListener, myChListener;
		
		public VODSE_Genre_List_Adapter( Context context, ArrayList<VOD_INFO> items) { //int textViewResourceId, 
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(context);
			//this.context = context;
			this.items = items;
			imageDownloader.setContext(getApplicationContext());
		}
			//notifyDataSetChanged();

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub

			return position;
		}

		private void Reload() {
			// TODO Auto-generated method stub
			notifyDataSetChanged();
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.vod_list_row, null);
				//Log.d("Sangtae", "convertView Null");
				holder = new ViewHolder();
				holder.postImage = (ImageView)convertView.findViewById(R.id.vod_ListPoster);
				holder.gradeImage = (GradeImageView)convertView.findViewById(R.id.vod_ListClassIcon);
				holder.vodTypeImage = (TypeImageView)convertView.findViewById(R.id.vod_ListVodTypeIcon);
				holder.titleText=(TextView)convertView.findViewById(R.id.vod_ListTitle);
				holder.directorText=(TextView)convertView.findViewById(R.id.vod_ListDirector);
				holder.castingText=(TextView)convertView.findViewById(R.id.vod_ListCasting);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				//Log.d("Sangtae", "convertView Not Null");
				holder = (ViewHolder) convertView.getTag();
			}

			Bitmap LoadBitmap = BitmapLoadFromFile(ImagesName[position]);
			
			// Bind the data efficiently with the holder.

			if(!tempApp.getAdultAuth() &&
	 				items.get(position).getGrade().equals(getResources().getString(R.string.vod_grade_19)))
				holder.postImage.setImageBitmap(AdultBitmap);
			else
			{
				if(LoadBitmap==null)
					imageDownloader.download(items.get(position).getPosterUrl().trim(), holder.postImage, NoBitmap);
				else
					holder.postImage.setImageBitmap(LoadBitmap);
			}
			holder.gradeImage.setGrade(items.get(position).getGrade().trim());
			holder.vodTypeImage.setType(items.get(position).getHD().trim());
			holder.titleText.setText(tempApp.endEllipsize(items.get(position).getTitle(), tempApp.RowTitleLength));
			holder.directorText.setText("감독 : " + items.get(position).getDirector().trim());
			holder.castingText.setText("주연 : " + items.get(position).getActor().trim());
//			convertView.setId(Integer.parseInt(items.get(position).getID()));


			return convertView;
		}

		//리스트 삭제 변경 

	}


	static class ViewHolder {
		ImageView postImage;
		GradeImageView gradeImage;
		TypeImageView vodTypeImage;
		TextView titleText;
		TextView directorText;
		TextView castingText;
	}


	
	private void setUpNaviArea() {

		Resources res = getResources(); 
		
		NaviBtn_Singleton mNaviBtnSingleton 	=  tempApp.getNaviBtn_Singleton();
		ImageButton leftNaviBtn = mNaviBtnSingleton.getNaviLeftBtn();
		leftNaviBtn.setBackgroundResource(R.drawable.top_button_back);
		
		ImageButton rightSubBtn = mNaviBtnSingleton.getNaviRightSubBtn();
		rightSubBtn.setVisibility(View.VISIBLE);
		
		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		naviRightBtn.setVisibility(View.VISIBLE);
		
		TextView txtHeader = mNaviBtnSingleton.getNaviHeaderText();
		txtHeader.setText("VOD목록");
		
	}
	
	class GenreItemListTask extends AsyncTask<String, Void,VOD_GENRE_DETAIL_LIST>
	{

		ProgressDialog pro;
		@Override
		protected VOD_GENRE_DETAIL_LIST doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try
			{
				String GenreId = params[0];
				VOD_GENRE_DETAIL_PASER PARSER= new VOD_GENRE_DETAIL_PASER(XML_Address_Define.Vod.getGetvodgenreinfo(GenreId));
				
				PARSER.start();
				tempApp.setVod_GENRE_DATA_DETAIL_LIST(PARSER.getArrayList());
				return PARSER.getArrayList();
			}
			catch (Exception e) {
				// TODO: handle exception
				Log.w("VODSE_GenreDepth", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(VOD_GENRE_DETAIL_LIST result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			pro.dismiss();
			
			try
			{
				mArrayList = result;
				mAdapter = new VODSE_Genre_List_Adapter(VODSE_Genre_List.this, mArrayList);
				URLs = new String[mArrayList.size()];
				ImagesName = new String[mArrayList.size()];
				
				for(int i=0; i< mArrayList.size(); i++)
				{
					URLs[i] = mArrayList.get(i).getPosterUrl();
					int lastPos = URLs[i].lastIndexOf("/");
					ImagesName[i] = URLs[i].substring(lastPos);
				}
				
				if(mAdapter.getCount() > 0)
					setListAdapter(mAdapter);
				else
					onCancelled();
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
	
	
	private Bitmap BitmapLoadFromFile(String fn)
    {
    	
		int lastPos = fn.lastIndexOf("/");
		int dotPos = fn.lastIndexOf(".");
		fn = fn.substring(lastPos, dotPos);
		
		File externalCacheDir = getApplicationContext().getExternalCacheDir();		
		String path = null;
		if (externalCacheDir != null) {
			path = externalCacheDir.toString();
		}
		else {
			path = Environment.getExternalStorageDirectory()
    				.getAbsolutePath() + "/"+PACKGE_NAME;
		}

    	//	String path = Environment.getExternalStorageDirectory()
    	//			.getAbsolutePath() + "/"+PACKGE_NAME;
    		
          Bitmap origbmp=null;
          
          try
          {
    			origbmp = BitmapFactory.decodeFile(path +fn + "list");
          }catch( Exception e)
          {
              Log.w(DEBUG_TAG, e);
          }
          
          return origbmp;
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
