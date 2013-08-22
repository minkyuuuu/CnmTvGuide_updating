package hdlnc.dev2.sangtae.cnm.vod;


import hdlnc.dev2.sangtae.cnm.R;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.CNM_CoverflowGallery;
import hdlnc.dev2.sangtae.cnm.global.CNM_ReflectedImageCreator;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_TVREPLAY_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_TVREPLAY_PASER;
import hdlnc.dev2.sangtae.cnm.main.main;
import hdlnc.dev2.sangtae.cnm.settings.Setting_Adult_Auth;
import hdlnc.dev2.sangtae.cnm.vod.VODSE_Preview.VodAsyncTask;

import java.io.File;


import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;

public class VODSE_TvReplay_Coverflow extends Activity {

	private CNMApplication tempApp;
	public static final int VOD_NEWVIDEO_COVERFLOW_TITLE_LENGTH=15;
	private static final String PACKGE_NAME = "hdlnc.dev2.sangtae.cnm/cache";

	private CoverflowImageDownloader imageDownloader;

	private VOD_TVREPLAY_LIST mArrayList;

	public static int CurrentPosition=0;
	private static final int SetCoverflowAlpha=125;

	private static boolean FisrtCoverflowCreate=false;

	private String URLs[];
	private String ImagesName[];
	private int savePos=0;

	private final String DEBUG_TAG ="VODSE_NewMovie_Coverflow";

	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			//Multi Thead Implements
		}
	};

	private Runnable mRunnable = new Runnable()
	{
		int ThreadDelay=0;

		public void run()
		{
			//			Message msg =  mHandler.obtainMessage();

			if(savePos < mArrayList.size())
			{
				if(imageDownloader.AsyncCheckFlag)
				{
					imageDownloader.download(URLs[savePos]);
					savePos++;
					mHandler.sendEmptyMessage(savePos);

					imageAdapter.notifyDataSetChanged();

				}
				mHandler.postDelayed(mRunnable, ThreadDelay);
			}
			else
			{
				//				downloadPro.dismiss();
				//				SetupUI();
				mHandler.removeCallbacks(this);
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tempApp = (CNMApplication)getApplication();

		imageDownloader = new CoverflowImageDownloader();
		imageDownloader.setContext(getApplicationContext());

		if (tempApp.GetNetworkInfo() != 2) {
			new VodAsyncTask().execute(null);
		} else {
			AlertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
		}

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUpNaviArea();
	}

	@Override
	public void onPause()
	{
		super.onPause();

	}

	private CNM_CoverflowGallery coverflowGallery;
	private ImageAdapter imageAdapter;	

	private void SetupUI()
	{

		FisrtCoverflowCreate = true;

		coverflowGallery = new CNM_CoverflowGallery(this);
		imageAdapter =  new ImageAdapter(this, mArrayList);
		coverflowGallery.setAdapter(imageAdapter);

		coverflowGallery.setSpacing(-100);
		coverflowGallery.setSelection(CurrentPosition, true);
		coverflowGallery.setAnimationDuration(500);
		coverflowGallery.setSoundEffectsEnabled(false);
		coverflowGallery.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		LayoutInflater inflater = getLayoutInflater();

		coverflowGallery.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int pos, long arg3) {
				// TODO Auto-generated method stub

				TextView tv = (TextView)findViewById(R.id.vod_new_movie_title);
				tv.setText(tempApp.endEllipsize(mArrayList.get(pos).getTitle(), VOD_NEWVIDEO_COVERFLOW_TITLE_LENGTH));
				GradeImageView gi= (GradeImageView)findViewById(R.id.vod_new_movie_grade);
				gi.setGrade(mArrayList.get(pos).getGrade());
				TypeImageView ti = (TypeImageView)findViewById(R.id.vod_new_movie_type);
				ti.setType(mArrayList.get(pos).getHD());
				CurrentPosition = pos;

				for(int i=0; i < parent.getChildCount(); i++)
				{
					ImageView iv = (ImageView)parent.getChildAt(i);
					iv.clearAnimation();
					iv.getDrawable().setAlpha(SetCoverflowAlpha);
				}

				ImageView iv = (ImageView)v;
				Animation ani = AnimationUtils.loadAnimation(VODSE_TvReplay_Coverflow.this, R.anim.scale_to_big);
				iv.setAnimation(ani);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		coverflowGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				if(mArrayList.get(CurrentPosition).getGrade().equals(getResources().getString(R.string.vod_grade_19)))
				{
					if(tempApp.getAdultAuth())
					{
						TabGroupActivity parent =(TabGroupActivity) getParent().getParent();
						Intent subGenreIntent = new Intent(getParent(), VODSE_Detailview.class);
						subGenreIntent.putExtra("VOD_ITEM", mArrayList.get(CurrentPosition));
						parent.startChildActivity("VodDetailView", subGenreIntent);
					}
					else
					{
						TabGroupActivity parent =(TabGroupActivity) getParent().getParent();
						Intent subGenreIntent = new Intent(getParent(), Setting_Adult_Auth.class);
						parent.startChildActivity("VodAdultAuth", subGenreIntent);
					}
				}
				else
				{
					TabGroupActivity parent =(TabGroupActivity) getParent().getParent();
					Intent subGenreIntent = new Intent(getParent(), VODSE_Detailview.class);
					subGenreIntent.putExtra("VOD_ITEM", mArrayList.get(CurrentPosition));
					parent.startChildActivity("VodDetailView", subGenreIntent);
				}
			}
		});

		RelativeLayout parentView =(RelativeLayout) inflater.inflate(R.layout.vod_new_movie, null);
		RelativeLayout galleryLayout =(RelativeLayout) parentView.findViewById(R.id.vod_gallery_layout);
		galleryLayout.addView(coverflowGallery);

		setContentView(parentView);

		TextView tv = (TextView)findViewById(R.id.vod_new_movie_title);
		tv.setText(tempApp.endEllipsize(mArrayList.get(CurrentPosition).getTitle(), VOD_NEWVIDEO_COVERFLOW_TITLE_LENGTH));
		GradeImageView gi= (GradeImageView)findViewById(R.id.vod_new_movie_grade);
		gi.setGrade(mArrayList.get(CurrentPosition).getGrade());
		TypeImageView ti = (TypeImageView)findViewById(R.id.vod_new_movie_type);
		ti.setType(mArrayList.get(CurrentPosition).getHD());


	}


	public class ImageAdapter extends BaseAdapter 
	{
		private Context mContext;
		private VOD_TVREPLAY_LIST Items;
		private static final int width = 210;
		//	     private static final int height = 280;
		private static final int height = 400;

		private final CNM_ReflectedImageCreator ReflectedImageCreator = new CNM_ReflectedImageCreator();
		private static final int ReflectedImageRatio =6;

		private final Bitmap AdultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.posterbg_cover_19);
		private final  Bitmap NoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noimg_poster);
		private final Bitmap R_AdultBitmap = ReflectedImageCreator.createReflectedImages(
				AdultBitmap, ReflectedImageRatio);
		private final Bitmap R_NoBitmap = ReflectedImageCreator.createReflectedImages(
				NoBitmap, ReflectedImageRatio);

		public ImageAdapter(Context c, VOD_TVREPLAY_LIST items)
		{
			mContext = c;
			Items = items;
		}


		public int getCount() {
			return ImagesName.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			//Use this code if you want to load from resources

			final ViewHolder holder;

			if(convertView ==null)
			{
				holder = new ViewHolder();
				convertView = new ImageView(VODSE_TvReplay_Coverflow.this);

				holder.CoverflowImage = (ImageView)convertView;
				holder.CoverflowImage.setScaleType(ScaleType.FIT_XY);
				holder.CoverflowImage.setLayoutParams(new CNM_CoverflowGallery.LayoutParams(width,height));
				holder.CoverflowImage.setAdjustViewBounds(true);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}

			Bitmap LoadBitmap = BitmapLoadFromFile(ImagesName[position]);

			if(tempApp.GetExternalStoragePath().equals(Environment.MEDIA_UNMOUNTED))
			{
				if(!tempApp.getAdultAuth() &&
						Items.get(position).getGrade().equals(getResources().getString(R.string.vod_grade_19)))
					holder.CoverflowImage.setImageBitmap(R_AdultBitmap);
				else
					imageDownloader.download(URLs[position], 
							holder.CoverflowImage, R_NoBitmap);
			}
			else
			{
				if(!tempApp.getAdultAuth() &&
						Items.get(position).getGrade().equals(getResources().getString(R.string.vod_grade_19)))
					holder.CoverflowImage.setImageBitmap(R_AdultBitmap);
				else
				{
					if(LoadBitmap == null)
						imageDownloader.download(URLs[position], holder.CoverflowImage, 
								R_NoBitmap);
					else
						holder.CoverflowImage.setImageBitmap(LoadBitmap);
				}
			}

			holder.CoverflowImage.getDrawable().setAlpha(SetCoverflowAlpha);

			if(CurrentPosition == position)
				holder.CoverflowImage.getDrawable().setAlpha(255);


			return holder.CoverflowImage;

		}


		/** Returns the size (0.0f to 1.0f) of the views 
		 * depending on the 'offset' to the center. */ 
		public float getScale(boolean focused, int offset) { 
			/* Formula: 1 / (2 ^ offset) */ 
			return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset))); 
		} 
	}

	static class ViewHolder {
		ImageView CoverflowImage;
	}

	// View.OnClickListner

	private void setUpNaviArea() {
		NaviBtn_Singleton mNaviBtnSingleton 	=  tempApp.getNaviBtn_Singleton();//(ImageButton)findViewById(R.id.Navi_LeftImgBtn);
		Resources res = getResources();

		ToggleButton naviLeftSubBtn = (ToggleButton)mNaviBtnSingleton.getNaviLeftSubBtn();
		naviLeftSubBtn.setChecked(false);
		naviLeftSubBtn.setVisibility(View.VISIBLE);

		//Drawable drawableHome = res.getDrawable(R.drawable.navi_home_btn);
		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_home);//ImageDrawable(drawableHome);
		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		//naviRightBtn.setChecked(false);
		naviRightBtn.setVisibility(View.VISIBLE);

		ImageButton naviRightSubBtn = (ImageButton)mNaviBtnSingleton.getNaviRightSubBtn();
		naviRightSubBtn.setVisibility(View.VISIBLE);

		TextView txtHeader = mNaviBtnSingleton.getNaviHeaderText();
		txtHeader.setText("VOD");

	}



	class VodAsyncTask extends AsyncTask<Void, Void, VOD_TVREPLAY_LIST>
	{

		Toast toast;
		ProgressDialog pro;
		@Override
		protected VOD_TVREPLAY_LIST doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(VOD_TVREPLAY_PASER.getArrayList() ==null)
			{
				VOD_TVREPLAY_PASER PARSER = new VOD_TVREPLAY_PASER(XML_Address_Define.Vod.getGetvodtv());
				PARSER.start();
				tempApp.setVod_TVREPLAY_DATA_LIST(PARSER.getArrayList());
				return PARSER.getArrayList();
			}
			else
				return tempApp.getVod_TVREPLAY_DATA_LIST();
		}

		@Override
		protected void onPostExecute(VOD_TVREPLAY_LIST result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			pro.dismiss();
			try
			{
				if(result ==null)
				{
					onCancelled();
				}
				else
					if(result.getRESULT().equals("ok") && result.size() > 0)
					{
						mArrayList= result;
						URLs = new String[mArrayList.size()];
						ImagesName = new String[mArrayList.size()];

						for(int i=0; i< mArrayList.size(); i++)
						{
							URLs[i] = mArrayList.get(i).getPosterUrl();
							int lastPos = URLs[i].lastIndexOf("/");
							ImagesName[i] = URLs[i].substring(lastPos);
						}

						SetupUI();

					}
			}
			catch (Exception e) {
				// TODO: handle exception
				Log.w(DEBUG_TAG, e);
			}


		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			toast = Toast.makeText(VODSE_TvReplay_Coverflow.this, getResources().getString(R.string.vod_error), Toast.LENGTH_LONG);
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


	private Bitmap BitmapLoadFromFile(String fn )
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
		Bitmap resized=null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		try
		{
			//    	  		origbmp = BitmapFactory.decodeFile(path +fn, options);
			File mkDir = getApplicationContext().getExternalCacheDir();
			if (mkDir == null) {
				mkDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), PACKGE_NAME);        	  
			}
			File file = new File(mkDir.toString(), fn);
			//  			if (!file.exists())
			//  				return ReflectedImageCreator.createReflectedImages(BitmapFactory.decodeResource(getResources(), R.drawable.noimg_poster), 5);

			origbmp = BitmapFactory.decodeFile(path +fn);
			//             resized = Bitmap.createScaledBitmap(origbmp, width ,height , true);
			//    	  		origbmp.recycle();
		}catch( Exception e)
		{
			Log.w(DEBUG_TAG, e);
		}
		return origbmp;
		//          return resized;
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
