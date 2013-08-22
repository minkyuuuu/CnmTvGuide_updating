package hdlnc.dev2.sangtae.cnm.vod;

import hdlnc.dev2.sangtae.cnm.R;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.CNM_SearchUpDialog;
import hdlnc.dev2.sangtae.cnm.global.ImageDownloader;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.CNM_SearchUpDialog.OnEnterKeyListener;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_INFO;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_SEARCH_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_SEARCH_PASER;
import hdlnc.dev2.sangtae.cnm.settings.Setting_Adult_Auth;

import java.io.File;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
import java.util.ArrayList;


import android.R.style;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.res.Resources;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;

public class VODSE_SearchResult_List extends Activity{

	private static final String PACKGE_NAME = "hdlnc.dev2.sangtae.cnm/cache";
	private final static String DEBUG_TAG = "VODSE_SearchResult_List";
	
	//private Boolean Edit=false;
	private CNMApplication tempApp;
	NaviBtn_Singleton mNaviBtnSingleton;
	private VODSE_SearchResult_List_Adapter mAdapter;
	private VOD_SEARCH_LIST mArrayList;
	private ListView searchResultListView;
	
	private String URLs[];
	private String ImagesName[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.vod_searchresult);
		
		tempApp = (CNMApplication)getApplication();
		mNaviBtnSingleton =  tempApp.getNaviBtn_Singleton();
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String KeyWord = bundle.getString("KeyWord");
		
		new VodAsyncTask().execute(KeyWord);
	}

	
	@Override
	protected void onResume() {
		
		super.onResume();
		setUpNaviArea();
	}
	
	public void setUpUI()
	{
		searchResultListView = (ListView)findViewById(R.id.vod_searchResult_listview);
		
		searchResultListView.setDividerHeight(0);
		searchResultListView.setDivider(getResources().getDrawable(R.drawable.transparent));
		
		TextView headerTextView = (TextView)findViewById(R.id.vod_searchResult_header);
		headerTextView.setText(mAdapter.getCount() + "개의 VOD가 검색되었습니다.");
		
		searchResultListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position,
					long id) {
				
				
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
		});
		
		searchResultListView.setAdapter(mAdapter);
		
	}
	

	
	class VodAsyncTask extends AsyncTask<String, Void, VOD_SEARCH_LIST>
	{

		ProgressDialog pro;
		@Override
		protected VOD_SEARCH_LIST doInBackground(String... params) {
			//String UTF8Keyword = URLEncoder.encode(params[0], "utf-8");
			String requestURL = XML_Address_Define.Search.getSearchvod(params[0], "0", "0", "TitleAsc");
			VOD_SEARCH_PASER PARSER = new VOD_SEARCH_PASER(requestURL);
			PARSER.start();

			return PARSER.getArrayList();
		}

		@Override
		protected void onPostExecute(VOD_SEARCH_LIST result) {
			
			super.onPostExecute(result);
			pro.dismiss();
			try
			{
				mArrayList =result;
				if(mArrayList==null)
					onCancelled();
				else
				if(mArrayList.getRESULT() != null)
				{
					mAdapter = new VODSE_SearchResult_List_Adapter(VODSE_SearchResult_List.this, mArrayList);
					
					URLs = new String[mArrayList.size()];
					ImagesName = new String[mArrayList.size()];
					
					for(int i=0; i< mArrayList.size(); i++)
					{
						URLs[i] = mArrayList.get(i).getPosterUrl();
						int lastPos = URLs[i].lastIndexOf("/");
						ImagesName[i] = URLs[i].substring(lastPos);
					}
					
					setUpUI();
					
				}
			}
			catch (Exception e) {

				Log.w("VODSE_TvReplay_List", e);
			}
			
		}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			pro = ProgressDialog.show(getParent().getParent(), "", getResources().getString(R.string.vod_watting),true);
		}
		
		@Override
		protected void onCancelled() {
			
			super.onCancelled();
			AlertShow(getResources().getString(R.string.system_network_warring));
		}
	}
	
	
	
	public class VODSE_SearchResult_List_Adapter extends BaseAdapter{

		private final ImageDownloader imageDownloader = new ImageDownloader();
		private LayoutInflater mInflater;
		//private Context context;
		private ArrayList<VOD_INFO> items;

		private final Bitmap NoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noimg_listposter);
		private final Bitmap AdultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.posterbg_list_19);

		public VODSE_SearchResult_List_Adapter( Context context, ArrayList<VOD_INFO> items) { //int textViewResourceId, 

			mInflater = LayoutInflater.from(context);
			//this.context = context;
			this.items = items;
			
			imageDownloader.setContext(getApplicationContext());
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
				convertView = mInflater.inflate(R.layout.vod_search_row, null);
				//Log.d("Sangtae", "convertView Null");
				holder = new ViewHolder();
				holder.numberText = (TextView)convertView.findViewById(R.id.vod_ListNumber);
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
				if(LoadBitmap ==null)
					imageDownloader.download(items.get(position).getPosterUrl().trim(), holder.postImage, NoBitmap);
				else
					holder.postImage.setImageBitmap(LoadBitmap);
			}
			holder.numberText.setText(String.format("%03d",position+1));
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
		TextView numberText;
	}

	private void setUpNaviArea() {
		//Resources res = getResources();
		
		ToggleButton naviLeftSubBtn = (ToggleButton)mNaviBtnSingleton.getNaviLeftSubBtn();
//		naviLeftSubBtn.setChecked(true);
		naviLeftSubBtn.setVisibility(View.INVISIBLE);
		
		TextView naviHeaderText = (TextView)mNaviBtnSingleton.getNaviHeaderText();
		naviHeaderText.setText("검색결과");
		
		//Drawable drawableBack = res.getDrawable(R.drawable.navi_back_btn);
		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_back);//ImageDrawable(drawableBack);
		naviLeftBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				tempApp.ButtonBeepPlay();
				onBackPressed();
			}
		});
		
		ImageButton	naviRightSubBtn =(ImageButton) mNaviBtnSingleton.getNaviRightSubBtn();
		naviRightSubBtn.setVisibility(View.VISIBLE);
		naviRightSubBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				tempApp.ButtonBeepPlay();
				new CNM_SearchUpDialog(getParent(), style.Theme_Translucent_NoTitleBar, 
						"VOD 명으로 검색", new OnEnterKeyListener() {
							
							@Override
							public void onEnterKeyPressed(String str) {
								
								onBackPressed();
								Intent searchResultIntent = new Intent(VODSE_SearchResult_List.this, VODSE_SearchResult_List.class);
								TabGroupActivity parent = (TabGroupActivity)getParent(); 
								searchResultIntent.putExtra("KeyWord", str);
								parent.startChildActivity("VODSE_SearchResult_List", searchResultIntent);
								
							}
						}).show();
			}
		});
		
		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		//naviRightBtn.setChecked(false);
		naviRightBtn.setVisibility(View.GONE);
		
		
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
	    	  NaviBtn_Singleton.getInstance().getNaviLeftBtn().performClick();
	          return true;
	      }
	      return super.onKeyUp(keyCode, event);
	  }
}
