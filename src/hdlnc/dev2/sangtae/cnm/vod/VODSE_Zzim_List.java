package hdlnc.dev2.sangtae.cnm.vod;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.ImageDownloader;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_ZZIM_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_ZZIM_PASER;
import hdlnc.dev2.sangtae.cnm.settings.Setting_Adult_Auth;


import hdlnc.dev2.sangtae.cnm.R;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class VODSE_Zzim_List extends ListActivity{
	//private final ImageDownloader imageDownloader = new ImageDownloader();
	private CNMApplication tempApp;
	private VOD_ZZIM_LIST mArrayList;
	private ZzimAdapter mAdapter;
	
	private static final String DEBUG_TAG = "VODSE_Zzim_List";
	private String Udid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tempApp = (CNMApplication)getApplication();
//		mArrayList = tempApp.getmVodZzimCurrnetList();
    
		setContentView(R.layout.vodse_zzim_list);
	
		Udid = tempApp.getMobileIMIE(VODSE_Zzim_List.this);
		
	}
	
	
	private void setupUI() {

		ListView lv = getListView();
		lv.setDividerHeight(0);
		lv.setDivider(getResources().getDrawable(R.drawable.transparent));
		mAdapter = new ZzimAdapter(VODSE_Zzim_List.this, mArrayList);
		lv.setAdapter(mAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		tempApp.ButtonBeepPlay();
		
		if(!tempApp.getAdultAuth() && 
				mArrayList.get(position).getGrade().equals(getResources().getString(R.string.vod_grade_19)))
		{
			
			TabGroupActivity parent =(TabGroupActivity) getParent().getParent();
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
	
	@Override
	protected void onResume() {
		super.onResume();
		setUpNaviArea();
		// TODO VodAsyncTask 설정 제대로 해줘야 한다.
		new VodAsyncTask().execute(null);
	}

	private void setUpNaviArea() {
		NaviBtn_Singleton mNaviBtnSingleton 	=  tempApp.getNaviBtn_Singleton();//(ImageButton)findViewById(R.id.Navi_LeftImgBtn);
		
		//Drawable drawableHome = res.getDrawable(R.drawable.navi_home_btn);
		
		//Resources res = getResources();
		//Drawable drawableBack = res.getDrawable(R.drawable.navi_back_btn);
		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_back);//ImageDrawable(drawableBack);
		
		naviLeftBtn.setVisibility(View.VISIBLE);
		naviLeftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tempApp.ButtonBeepPlay();
				onBackPressed();
			}
		});
		
		ToggleButton naviLeftSubBtn = (ToggleButton)mNaviBtnSingleton.getNaviLeftSubBtn();
		naviLeftSubBtn.setVisibility(View.GONE);

		TextView txtHeader = (TextView)mNaviBtnSingleton.getNaviHeaderText();
		txtHeader.setText("찜목록");
		
		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		naviRightBtn.setVisibility(View.GONE);
		
		
		//Drawable drawableSrch = res.getDrawable(R.drawable.navi_search_btn);
		ImageButton naviRightSubBtn = (ImageButton)mNaviBtnSingleton.getNaviRightSubBtn();
		naviRightSubBtn.setBackgroundResource(R.drawable.top_button_search);//setImageDrawable(drawableSrch);
		naviRightSubBtn.setVisibility(View.VISIBLE);
		
	}

	
	
	class ZzimAdapter extends BaseAdapter
    {
    		Context mContext;
    		LayoutInflater mInflater;
    		final ImageDownloader imageDownloader = new ImageDownloader();
    		private VOD_ZZIM_LIST items;
    		
    		public ZzimAdapter(Context context, VOD_ZZIM_LIST items)
    		{
    			mContext= context;
    			mInflater = LayoutInflater.from(context);
    			this.items= items;
    			imageDownloader.setContext(getApplicationContext());
		}

			public int getCount() {
				return items.size();
			}

			public Object getItem(int position) {
				return items.get(position);
			}

			public long getItemId(int position) {
				return position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				
				final ViewHolder holder;
				//final int tempPosition = position;

				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.vod_list_row, null);
					
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
					holder = (ViewHolder) convertView.getTag();
				}

				// Bind the data efficiently with the holder.
				
				if(!tempApp.getAdultAuth() &&
		 				items.get(position).getGrade().equals(getResources().getString(R.string.vod_grade_19)))
					holder.postImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.posterbg_list_19));
				else
					imageDownloader.download(items.get(position).getPosterUrl().trim(), holder.postImage,
							BitmapFactory.decodeResource(getResources(), R.drawable.noimg_listposter));
				
				
				holder.gradeImage.setGrade(items.get(position).getGrade());
				holder.vodTypeImage.setType(items.get(position).getHD());
				holder.titleText.setText(tempApp.endEllipsize(items.get(position).getTitle(), tempApp.RowTitleLength) );
				holder.directorText.setText("감독 : " + items.get(position).getDirector());
				holder.castingText.setText("주연 : " + items.get(position).getActor());
				
				
				return convertView;
			}
    	
    }
    
    static class ViewHolder
    {
    		ImageView postImage;
		GradeImageView gradeImage;
		TypeImageView vodTypeImage;
		TextView titleText;
		TextView directorText;
		TextView castingText;
    }
    
    
    class VodAsyncTask extends AsyncTask<Void, Void, VOD_ZZIM_LIST>
	{

		ProgressDialog pro;
		@Override
		protected VOD_ZZIM_LIST doInBackground(Void... params) {
			
			/*String paserUrl = tempApp.URL_ZZIM_LIST + Udid;
			Log.d(DEBUG_TAG, "paserUrl : " + paserUrl);*/
			String requestURL = XML_Address_Define.Vod.getGetvodmylist(Udid);
			Log.d(DEBUG_TAG, "### URL Request VodAsyncTask: " + requestURL);

			VOD_ZZIM_PASER.start(requestURL);
			return VOD_ZZIM_PASER.getArrayList();
		}

		@Override
		protected void onPostExecute(VOD_ZZIM_LIST result) {
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
					setupUI();
			}
			catch (Exception e) {
				Log.w(DEBUG_TAG, e);
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
	
}
