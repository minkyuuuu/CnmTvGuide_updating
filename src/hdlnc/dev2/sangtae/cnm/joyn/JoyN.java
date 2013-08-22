package hdlnc.dev2.sangtae.cnm.joyn;

import hdlnc.dev2.sangtae.cnm.R;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.ImageDownloader;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.JOYN_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.JOYN_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.JOYN_PARSER;
import hdlnc.dev2.sangtae.cnm.main.main;

import java.io.File;

import javax.xml.XMLConstants;


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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class JoyN extends ListActivity {
	
	private final String DEBUG_TAG ="JoyN";
	private static final String PACKGE_NAME = "hdlnc.dev2.sangtae.cnm/cache";
	
	public static final String JOYN_ITEM_SELECT="JOYN_SELECT_ITEM";
	private JoyNAdapter joyAdapter;
	private CNMApplication tempApp;
	private JOYN_PARSER joyn_PARSER;
	
	private String URLs[];
	private String ImagesName[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joy_n);
		// JOY&
		
		tempApp = (CNMApplication)getApplication();
		
		ListView mListView = getListView();
		mListView.setSoundEffectsEnabled(false);
		mListView.setDividerHeight(0);
		mListView.setDivider(getResources().getDrawable(R.drawable.transparent));
		
		new JoyNAsyncTask().execute(null);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setupNaviArea();
	}
	
	// NAVI Button & Header Text
	private void setupNaviArea() {	

		NaviBtn_Singleton mNaviBtnSingleton 	=  tempApp.getNaviBtn_Singleton();//(ImageButton)findViewById(R.id.Navi_LeftImgBtn);
		Resources res = getResources();
		//Drawable drawableHome = res.getDrawable(R.drawable.navi_home_btn);
		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_back);//ImageDrawable(drawableHome);
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
		txtHeader.setText("Joy & Life");
		ImageButton naviRightSubBtn = (ImageButton)mNaviBtnSingleton.getNaviRightSubBtn();
		naviRightSubBtn.setVisibility(View.GONE);
		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		naviRightBtn.setVisibility(View.GONE);
		
		tempApp.getMainrbThird().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				JoyN.this.onResume();
				getListView().setSelection(0);
				TabGroupActivity parentActivity = (TabGroupActivity)getParent().getParent();
				parentActivity.onHomeBackPressed(0);
				
			}
		});
		
	}

	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		tempApp.ButtonBeepPlay();
		
		JOYN_DATA joy_data = (JOYN_DATA)joyAdapter.getItem(position);
		Log.d("JOYN",joy_data.getID());
		Intent joyNDetail = new Intent(getParent(), JoyNDetail.class);
		joyNDetail.putExtra(JOYN_ITEM_SELECT, joy_data.getID());
        TabGroupActivity parentActivity = (TabGroupActivity)getParent();
        parentActivity.startChildActivity("JoyNDetail", joyNDetail);
        //TextView txt = (TextView)v.findViewById(R.id.JoyN_HiddenID);

	}
	
	public class JoyNAdapter extends BaseAdapter{

		private final ImageDownloader imageDownloader = new ImageDownloader();
		private LayoutInflater mInflater;
		//private Context context;
		private JOYN_DATA_LIST items;

		private final Bitmap NoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noimg_joylist);
		
		//OnCheckedChangeListener hideListener, myChListener;


		public JoyNAdapter( Context context, JOYN_DATA_LIST items) { //int textViewResourceId, 
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(context);
			imageDownloader.setContext(getApplicationContext());
			//this.context = context;
			this.items = items;

		}

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
			//CurrentDB = tempApp.getCurrentTVCH();
			final ViewHolder holder;
			final int tempPosition = position;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.joy_n_row, null);
				//Log.d("Sangtae", "convertView Null");
				holder = new ViewHolder();
				//holder.text = (TextView) convertView.findViewById(R.id.TextView01);
				// 에디터 메뉴 버튼
				//holder.EditLayout	= (LinearLayout)convertView.findViewById(R.id.TVCH_EditLayout);
				//holder.hide_btn		= (ToggleButton)convertView.findViewById(R.id.TVCH_Hide_ToggleBtn);
				//holder.Mych_btn		= (ToggleButton)convertView.findViewById(R.id.TVCH_Mych_ToggleBtn);
				holder.thumbnail_img	= (ImageView)convertView.findViewById(R.id.JoyN_Icon);
				holder.thumbnail_img.setScaleType(ScaleType.FIT_XY);
				//-------------
				holder.title		= (TextView)convertView.findViewById(R.id.JoyN_Title);
				holder.desc		= (TextView) convertView.findViewById(R.id.JoyN_Desc);
				holder.id = (TextView)convertView.findViewById(R.id.JoyN_HiddenID);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				//Log.d("Sangtae", "convertView Not Null");
				holder = (ViewHolder) convertView.getTag();
			}

			Bitmap LoadBitmap = BitmapLoadFromFile(ImagesName[position]);
			
			// Bind the data efficiently with the holder.
			holder.title.setText(items.get(position).getTITLE());
			holder.desc.setText(items.get(position).getSUBTITLE());
			holder.id.setText(items.get(position).getID());
			
			if(LoadBitmap == null)
				imageDownloader.download(items.get(position).getTHUMBNAIL_IMG(), holder.thumbnail_img, 
					NoBitmap);
			else
				holder.thumbnail_img.setImageBitmap(LoadBitmap);

			convertView.setId(Integer.parseInt(items.get(position).getID()));

			return convertView;
		}

		//리스트 삭제 변경 

	}


	static class ViewHolder {
		//LinearLayout EditLayout;
		ImageView	thumbnail_img;

		TextView	 title;
		TextView	 desc;
		TextView id;

	}
	
	class JoyNAsyncTask extends AsyncTask<Void, Void, JOYN_DATA_LIST>
	{

		ProgressDialog pro;
		@Override
		protected JOYN_DATA_LIST doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(joyn_PARSER.getJOYN_DATA_LIST()==null)
			{
				joyn_PARSER = new JOYN_PARSER(XML_Address_Define.Service.getGetservicejoinnlist());
				joyn_PARSER.start();
				tempApp.setJoyN_DATA_LIST(joyn_PARSER.getJOYN_DATA_LIST());
				return joyn_PARSER.getJOYN_DATA_LIST();
			}
			else
				return tempApp.getJoyN_DATA_LIST();
			
		}

		@Override
		protected void onPostExecute(JOYN_DATA_LIST result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			pro.dismiss();
			
			if(result ==null)
			{
				onCancelled();
			}
			else
			{
				joyAdapter = new JoyNAdapter(JoyN.this, result);
				setListAdapter(joyAdapter);
				
				URLs = new String[result.size()];
				ImagesName = new String[result.size()];
				
				for(int i=0; i< result.size(); i++)
				{
					URLs[i] = result.get(i).getTHUMBNAIL_IMG();
					int lastPos = URLs[i].lastIndexOf("/");
					ImagesName[i] = URLs[i].substring(lastPos);
				}
				
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
//    		String path = Environment.getExternalStorageDirectory()
  //  				.getAbsolutePath() + "/"+PACKGE_NAME;
    		
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		tempApp .ButtonBeepPlay();
		super.onBackPressed();
	}
	
}
