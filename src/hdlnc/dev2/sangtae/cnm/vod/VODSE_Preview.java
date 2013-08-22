package hdlnc.dev2.sangtae.cnm.vod;

import hdlnc.dev2.sangtae.cnm.R;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.CNM_CoverflowGallery2;
import hdlnc.dev2.sangtae.cnm.global.CNM_ReflectedImageCreator;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_DETAIL_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_INFO;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_PREVIEW_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_PREVIEW_PASER;
import hdlnc.dev2.sangtae.cnm.settings.Setting_Adult_Auth;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class VODSE_Preview extends Activity implements AnimationListener, OnTouchListener {


	private static final String PACKGE_NAME = "hdlnc.dev2.sangtae.cnm/cache";


	private CNMApplication tempApp;
	private NaviBtn_Singleton mNaviBtnSingleton;
	private PreviewImageDownloader imageDownloader;
	private VOD_PREVIEW_LIST mArrayList;
	private VOD_PREVIEW_LIST mMoreArrayList;
	private ImageAdapter imageAdapter;

	private static boolean FisrtCoverflowCreate=false;

	public static int CurrentPosition=0;

	private String URLs[];
	private String ImagesName[];
	private int savePos=0;

	private VodAsyncTask VodAsync;

	private final String DEBUG_TAG = "VODSE_Preview";
	
	private final float galleryRatio = 1.54f;
	private final int defaultGalleryWidth = 340;
	private int galleryWidth = 0;
	private int galleryHeight = 0;

	//	private ProgressDialog downloadPro;

	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			//Multi Thead Implements
			Log.d(DEBUG_TAG, "Current SavaPos : " + msg.what);

			//			downloadPro.setProgress(msg.what);
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
					imageDownloader.download(URLs[savePos],BitmapFactory.decodeResource(getResources(), R.drawable.noimg_poster));
					Log.d(DEBUG_TAG,"DownloadStartPosition : " + savePos );
					savePos++;
					//					mHandler.sendEmptyMessage(savePos);
					imageAdapter.notifyDataSetChanged();
				}
				mHandler.postDelayed(mRunnable, ThreadDelay);


			}
			else
			{
				//				downloadPro.dismiss();
				//				SetupUI();
				imageAdapter.notifyDataSetChanged();
				mHandler.removeCallbacks(this);
				Log.d(DEBUG_TAG,"removeCallbacks!");

			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tempApp = (CNMApplication)getApplication();
		mNaviBtnSingleton =  tempApp.getNaviBtn_Singleton();
		imageDownloader = new PreviewImageDownloader();

		imageDownloader.setContext(getApplicationContext());

		Log.d(DEBUG_TAG, "FisrtCoverflowCreate : " + FisrtCoverflowCreate);

		setContentView(R.layout.vod_preview);
		
		// check device width and re-calculate gallery width depanding on 480 width
		int deviceWidth = getResources().getDisplayMetrics().widthPixels;
		galleryWidth = Math.round(deviceWidth * (defaultGalleryWidth / 480.0f));
		galleryHeight = Math.round(galleryWidth * galleryRatio);

	}

	//View Define
	ImageView vodPreviewPre;
	ImageView vodPreviewNext;
	ImageButton vodDetailView;
	ImageButton vodPlayerView;

	LinearLayout vodPreviewContLayout;

	private CNM_CoverflowGallery2 vodPreviewGallery;
	//private Gallery vodPreviewGallery;
	//Animation Define
	Animation fadeOnAni;

	private void SetupUI()
	{

		FisrtCoverflowCreate= true;

		fadeOnAni = AnimationUtils.loadAnimation(VODSE_Preview.this, R.anim.vod_preview_fade);
		fadeOnAni.setAnimationListener(VODSE_Preview.this);

		vodPreviewContLayout = (LinearLayout)findViewById(R.id.vod_preview_controller_layout);
		vodPreviewContLayout.setAnimation(fadeOnAni);

		vodPreviewPre = (ImageView)findViewById(R.id.vod_preview_pre);
		vodPreviewNext = (ImageView)findViewById(R.id.vod_preview_next);
		
		vodPlayerView = (ImageButton)findViewById(R.id.vod_preview_vodpaly_btn);
		vodPlayerView.setOnClickListener(new View.OnClickListener() {

			String videoUrl="";
			int currentPos;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				tempApp.ButtonBeepPlay();

				//				currentPos = vodPreviewFlipper.getDisplayedChild();
				currentPos = CurrentPosition;
				videoUrl =  mArrayList.get(currentPos).getVideoUrl().trim();


				if(!tempApp.getAdultAuth() &&
						mArrayList.get(currentPos).getGrade().equals(getResources().getString(R.string.vod_grade_19)))
				{
					TabGroupActivity parent =(TabGroupActivity) getParent().getParent();
					Intent subGenreIntent = new Intent(getParent(), Setting_Adult_Auth.class);
					parent.startChildActivity("AuthAdultSetting", subGenreIntent);
				}
				else
				{
					Log.d("Sangtae", videoUrl);
					if(tempApp.GetNetworkInfo() == tempApp.MOBILE_CONNECTED)
					{
						new AlertDialog.Builder(getParent().getParent())
						.setTitle("주 의")
						.setMessage(getResources().getString(R.string.vod_warring))
						.setNegativeButton("확인", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Intent i = new Intent(Intent.ACTION_VIEW , Uri.parse(videoUrl) );
								startActivity(i);
							}
						})
						.create().show(); 
					}
					else
					{
						Intent i = new Intent(Intent.ACTION_VIEW , Uri.parse(videoUrl) );
						startActivity(i);
					}
				}
			}
		});

		vodDetailView = (ImageButton)findViewById(R.id.vod_preview_detail_btn);
		vodDetailView.setOnClickListener(new View.OnClickListener() {
			int currentPos;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				tempApp.ButtonBeepPlay();

				//				currentPos = vodPreviewFlipper.getDisplayedChild();
				currentPos = CurrentPosition;


				if(mArrayList.get(currentPos).getMore().equals("YES"))
				{
					new VodTagAsyncTask().execute(mArrayList.get(currentPos).getTag());
				}
				else {
					if(mArrayList.get(currentPos).getMore().equals("NO"))
					{
						TabGroupActivity parent =(TabGroupActivity) getParent().getParent();
						Intent subGenreIntent = new Intent(getParent(), VODSE_Detailview.class);
						subGenreIntent.putExtra("VOD_ITEM", mArrayList.get(currentPos));
						parent.startChildActivity("VodDetailView", subGenreIntent);
					}
					else {
						if(!mArrayList.get(currentPos).getGrade().equals(getResources().getString(R.string.vod_grade_19)) ||
								tempApp.getAdultAuth())
						{
							TabGroupActivity parent =(TabGroupActivity) getParent().getParent();
							Intent subGenreIntent = new Intent(getParent(), VODSE_Detailview.class);
							subGenreIntent.putExtra("VOD_ITEM", mArrayList.get(currentPos));
							parent.startChildActivity("VodDetailView", subGenreIntent);
						}
						else
						{
							TabGroupActivity parent =(TabGroupActivity) getParent().getParent();
							Intent subGenreIntent = new Intent(getParent(), Setting_Adult_Auth.class);
							parent.startChildActivity("AuthAdultSetting", subGenreIntent);
						}
					}
				}
			}
		});

		imageAdapter = new ImageAdapter(VODSE_Preview.this, mArrayList);
		vodPreviewGallery = (CNM_CoverflowGallery2)findViewById(R.id.vod_preview_gallery);
		vodPreviewGallery.setAdapter(imageAdapter);
		vodPreviewGallery.setSelection(CurrentPosition);
		vodPreviewGallery.setOnTouchListener(VODSE_Preview.this);
		vodPreviewGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				CurrentPosition = pos; 

				if(CurrentPosition ==0)
					vodPreviewPre.setVisibility(View.INVISIBLE);
				else if(vodPreviewGallery.getCount()-1 == CurrentPosition)
					vodPreviewNext.setVisibility(View.INVISIBLE);
				else
				{
					vodPreviewPre.setVisibility(View.VISIBLE);
					vodPreviewNext.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		if(CurrentPosition ==0)
			vodPreviewPre.setVisibility(View.INVISIBLE);
		else if(vodPreviewGallery.getCount()-1 == CurrentPosition)
			vodPreviewNext.setVisibility(View.INVISIBLE);
		else
		{
			vodPreviewPre.setVisibility(View.VISIBLE);
			vodPreviewNext.setVisibility(View.VISIBLE);
		}

		//		imageAdapter.notifyDataSetChanged();

	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUpNaviArea();

		if (tempApp.GetNetworkInfo() != 2) {
			VodAsync = new VodAsyncTask();
			VodAsync.execute(null);
		} else {
			AlertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private void setUpNaviArea() {
		CNMApplication tempApp = (CNMApplication)getApplication();	

		mNaviBtnSingleton 	=  tempApp.getNaviBtn_Singleton();//(ImageButton)findViewById(R.id.Navi_LeftImgBtn);
		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_home);//ImageDrawable(drawableHome);
		naviLeftBtn.setVisibility(View.VISIBLE);

		ToggleButton naviLeftSubBtn = (ToggleButton)mNaviBtnSingleton.getNaviLeftSubBtn();
		naviLeftSubBtn.setVisibility(View.GONE);

		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		//naviRightBtn.setChecked(false);
		naviRightBtn.setVisibility(View.VISIBLE);

		ImageButton naviRightSubBtn = (ImageButton)mNaviBtnSingleton.getNaviRightSubBtn();
		naviRightSubBtn.setVisibility(View.VISIBLE);

		TextView txtHeader = (TextView)mNaviBtnSingleton.getNaviHeaderText();
		txtHeader.setText("VOD");

		tempApp.getMainrbSecond().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(!VodAsync.getStatus().equals(AsyncTask.Status.RUNNING))
					VODSE_Preview.this.onResume();
				VOD_Service.initTabHost();

				TabActivity tabHost = (TabActivity)getParent();
				RadioGroup rg = (RadioGroup)tabHost.findViewById(R.id.vodService_radiogroup);
				rg.check(R.id.vodService_previewList_radiobutton);
				TabGroupActivity parentActivity = (TabGroupActivity)getParent().getParent();
				parentActivity.onHomeBackPressed(0);
			}
		});
	}


	public class ImageAdapter extends BaseAdapter 
	{
		//private Context mContext;
		private int width = galleryWidth;
		private int height = galleryHeight;
//		private static final int width = 339;
//		private static final int height = 522;
		private VOD_PREVIEW_LIST Items;

		final CNM_ReflectedImageCreator ReflectedImageCreator = new CNM_ReflectedImageCreator();
		private static final int ReflectedImageRatio =9;

		private final Bitmap AdultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.posterbg_cover_19);
		private final  Bitmap NoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noimg_poster);
		private final Bitmap R_AdultBitmap = ReflectedImageCreator.createReflectedImages(
				AdultBitmap, ReflectedImageRatio);
		private final Bitmap R_NoBitmap = ReflectedImageCreator.createReflectedImages(
				NoBitmap, ReflectedImageRatio);


		public ImageAdapter(Context c, VOD_PREVIEW_LIST items)
		{
			//mContext = c;
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
				convertView = new ImageView(VODSE_Preview.this);

				holder.PreviewImage = (ImageView)convertView;
				holder.PreviewImage.setScaleType(ScaleType.FIT_XY);
				holder.PreviewImage.setLayoutParams(new Gallery.LayoutParams(width,height));
				holder.PreviewImage.setAdjustViewBounds(true);

				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}


			if(tempApp.GetExternalStoragePath().equals(Environment.MEDIA_UNMOUNTED))
			{
				if(!tempApp.getAdultAuth() &&
						Items.get(position).getGrade().equals(getResources().getString(R.string.vod_grade_19)))
					holder.PreviewImage.setImageBitmap(R_AdultBitmap);
				else{
					Log.d(DEBUG_TAG,"imageDownloader");
					imageDownloader.download(URLs[position], 
							holder.PreviewImage, R_NoBitmap);
				}

			}
			else
			{
				if(!tempApp.getAdultAuth() &&
						Items.get(position).getGrade().equals(getResources().getString(R.string.vod_grade_19)))
					holder.PreviewImage.setImageBitmap(R_AdultBitmap);
				else
				{
					Bitmap LoadBitmap = BitmapLoadFromFile(ImagesName[position]);

					if(LoadBitmap == null)
						holder.PreviewImage.setImageBitmap(R_NoBitmap);
					else
						holder.PreviewImage.setImageBitmap(LoadBitmap);
				}
			}

			return holder.PreviewImage;

		}


		/** Returns the size (0.0f to 1.0f) of the views 
		 * depending on the 'offset' to the center. */ 
		public float getScale(boolean focused, int offset) { 
			/* Formula: 1 / (2 ^ offset) */ 
			return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset))); 
		} 
	}

	static class ViewHolder {
		ImageView PreviewImage;
	}

	class VodTagAsyncTask extends AsyncTask<String, Void, VOD_PREVIEW_LIST>
	{

		ProgressDialog pro;
		Toast toast;

		@Override
		protected VOD_PREVIEW_LIST doInBackground(String... params) {
			// TODO Auto-generated method stub

			//String UTF8Keyword = URLEncoder.encode(params[0], "utf-8");
			String urlString = XML_Address_Define.Vod.getGetvodtag(params[0]);

			Log.d(DEBUG_TAG, "VodTagAsyncTask URL: "+urlString);

			//VOD_DETAIL_PASER detail_PARSER = new VOD_DETAIL_PASER(urlString);
			VOD_DETAIL_PASER.start(urlString);

			return VOD_DETAIL_PASER.getArrayList();

		}

		@Override
		protected void onPostExecute(VOD_PREVIEW_LIST result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			mMoreArrayList = result;
			pro.dismiss();

			try
			{

				//Log.d(DEBUG_TAG, "mMoreArrayList: " + mMoreArrayList+": "+mMoreArrayList.getRESULT());

				if(mMoreArrayList == null)
				{
					onCancelled();
				}
				if(mMoreArrayList !=null && mMoreArrayList.getRESULT().equals(XML_Address_Define.ErrorCode.ERROR_100) )
				{
					final String vodGrade = mArrayList.get(CurrentPosition).getGrade();
					String [] moreContentArray = new String[mMoreArrayList.size()];

					Log.d(DEBUG_TAG, "mMoreArrayList size: " + mMoreArrayList.size());

					int pos =0;
					for(VOD_INFO item : mMoreArrayList)
					{
						moreContentArray[pos] = item.getTitle();
						pos++;
						Log.d(DEBUG_TAG, "item.getTitle() : " + item.getTitle());
					}

					TextView title = new TextView(getApplicationContext());
					title.setText(mArrayList.get(CurrentPosition).getTag());
					title.setBackgroundColor(Color.BLACK);
					title.setPadding(10, 10, 10,10);
					title.setGravity(Gravity.CENTER);
					title.setTextColor(Color.WHITE);
					title.setTextSize(20);

					new AlertDialog.Builder(getParent().getParent())
					//.setTitle(mArrayList.get(CurrentPosition).getTag())
					.setCustomTitle(title)
					.setNegativeButton("취소", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							tempApp.ButtonBeepPlay();
							dialog.dismiss(); // 닫기
						}
					})
					.setItems(moreContentArray, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							tempApp.ButtonBeepPlay();

							if(!vodGrade.equals(getResources().getString(R.string.vod_grade_19)) ||
									tempApp.getAdultAuth())
							{
								TabGroupActivity parent =(TabGroupActivity) getParent().getParent();
								Intent subGenreIntent = new Intent(getParent(), VODSE_Detailview.class);
								subGenreIntent.putExtra("VOD_ITEM", mMoreArrayList.get(which));
								parent.startChildActivity("VodDetailView", subGenreIntent);
							}
							else
							{
								TabGroupActivity parent =(TabGroupActivity) getParent().getParent();
								Intent subGenreIntent = new Intent(getParent(), Setting_Adult_Auth.class);
								parent.startChildActivity("AuthAdultSetting", subGenreIntent);
							}

						}
					}).show(); 
				}
				else
					toast.show();
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
			toast = Toast.makeText(VODSE_Preview.this, getResources().getString(R.string.vod_error), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			pro = ProgressDialog.show(getParent().getParent(), "", getResources().getString(R.string.vod_watting),true);

		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();

			//AlertShow(getResources().getString(R.string.system_network_warring));
		}

	}

	class VodAsyncTask extends AsyncTask<Void, Void, VOD_PREVIEW_LIST>
	{
		ProgressDialog pro;
		Toast toast;

		@Override
		protected VOD_PREVIEW_LIST doInBackground(Void... params) {
			// TODO Auto-generated method stub

			if(VOD_PREVIEW_PASER.getArrayList() ==null)
			{
				String urlString = XML_Address_Define.Vod.getGetvodtrailer();

				Log.d(DEBUG_TAG, "VodAsyncTask URL: "+urlString);
				//VOD_PREVIEW_PASER preview_PARSER = new VOD_PREVIEW_PASER(urlString);
				VOD_PREVIEW_PASER.start(urlString);
				tempApp.setVod_PREVIEW_DATA_LIST(VOD_PREVIEW_PASER.getArrayList());
				return VOD_PREVIEW_PASER.getArrayList();
			}
			else
				return tempApp.getVod_PREVIEW_DATA_LIST();
		}

		@Override
		protected void onPostExecute(VOD_PREVIEW_LIST result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			pro.dismiss();
			try
			{

				if(result ==null){
					onCancelled();
				}else{
					if(result.getRESULT().equals(XML_Address_Define.ErrorCode.ERROR_100) && result.size() > 0)
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

						if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !FisrtCoverflowCreate)
						{
							mHandler.post(mRunnable);
							SetupUI();
						}
						else
							SetupUI();
					}else{
						toast.show();
					}
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				Log.w("VODSE_Preview", e);
			}		

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// TODO Auto-generated method stub
			toast = Toast.makeText(VODSE_Preview.this, getResources().getString(R.string.vod_error), Toast.LENGTH_LONG);
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


	private Bitmap BitmapLoadFromFile( String fn )
	{

		int lastPos = fn.lastIndexOf("/");
		int dotPos = fn.lastIndexOf(".");
		fn = fn.substring(lastPos, dotPos);


		//		final int width = 339;
		//		final int height = 542;


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

		Log.d(DEBUG_TAG, path +fn);
		Bitmap origbmp=null;
		//Bitmap resized=null;
		try
		{
			File mkDir = getApplicationContext().getExternalCacheDir();
			if (mkDir == null) {
				mkDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), PACKGE_NAME);        	  
			}

			//File file = new File(mkDir.toString(), fn);
			//  			if (!file.exists())
			//  				return ReflectedImageCreator.createReflectedImages(BitmapFactory.decodeResource(getResources(), R.drawable.noimg_poster), 9);

			origbmp = BitmapFactory.decodeFile(path +fn);

			//		final int width = 339;
			//		final int height = 542;
			//             resized = Bitmap.createScaledBitmap(origbmp, width ,height , true);
		}catch( Exception e)
		{
			Log.w(DEBUG_TAG, e);
		}
		return origbmp;
		//          return resized;
	}
	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		vodPreviewContLayout.setVisibility(View.GONE);
		vodPlayerView.setEnabled(false);
		vodDetailView.setEnabled(false);
	}
	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		vodPreviewContLayout.setVisibility(View.VISIBLE);
		vodPlayerView.setEnabled(true);
		vodDetailView.setEnabled(true);
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		vodPreviewContLayout.startAnimation(fadeOnAni);
		return false;
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
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().performClick();
	}
}
