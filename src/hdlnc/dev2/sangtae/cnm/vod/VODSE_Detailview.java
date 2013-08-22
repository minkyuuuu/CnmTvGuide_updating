package hdlnc.dev2.sangtae.cnm.vod;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.ImageDownloader;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.CHECK_RESPONSE_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.RESPONSE_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_INFO;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_ZZIM_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.VOD_ZZIM_PASER;
import hdlnc.dev2.sangtae.cnm.settings.Setting_SettopRegi;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
//import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseDialogListener;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;

public class VODSE_Detailview extends Activity {
	//private final ImageDownloader imageDownloader = new ImageDownloader();
	public static final int VOD_DETAILVIEW_TITLE_LENGTH=15; 
	public static final int VOD_DETAILVIEW_CONTENT_LENGTH=123; 
	public static String VOD_DETAILVIEW_SMS_MESSAGE;
	public static final String VOD_APPSTORE_DOWNLOAD_LINK="http://...";

	public static final String DEBUG_TAG = "VODSE_Detailview";
	private static final String PACKGE_NAME = "hdlnc.dev2.sangtae.cnm/cache";

	private String Udid;

	//private Boolean Edit=false;
	private CNMApplication app;
	private Facebook mFacebook;
	//private Handler mHandler;
	AsyncFacebookRunner mAsyncRunner;

	private final ImageDownloader imageDownloader = new ImageDownloader();

	private String Title = "";
	private String Director = "";
	private String Actor = "";
	private String Contents ="";
	private String RunningTime = "";
	private String Grade = "";
	private String PosterUrl ="";
	private String VideoUrl = "";
	private String Price = "";
	private LayoutInflater factory;
	private EditText name;
	private EditText pass;

	private ArrayList<String> shareArrayList = new ArrayList<String>();
	private VOD_INFO VodItem;

	private ToggleButton zzimButton;
	private ImageButton mSettopTVInfoBtn;
	private RemoteControlAsyncTask mTvPlayTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		app = (CNMApplication)getApplication();

		mFacebook = new Facebook(app.FACEBOOK_APPLICATION_ID);
		//mHandler = new Handler();
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);

		setContentView(R.layout.vod_detailview);

		Bundle bundle = getIntent().getExtras();
		VodItem = bundle.getParcelable("VOD_ITEM");
		factory = LayoutInflater.from(this);
		VOD_DETAILVIEW_SMS_MESSAGE = "[씨앤앰VOD추천]" + 
		VodItem.getTitle() + 
		" 감독: " + VodItem.getDirector() +
		" 출연: " + VodItem.getActor();

		Udid = app.getMobileIMIE(VODSE_Detailview.this);
		//		, VodItem.getCATEGORY()
		imageDownloader.setContext(getApplicationContext());

		SetupUI();
	}

	@Override
	protected void onResume() {

		super.onResume();
		
		setUpNaviArea();
		
		new ZzimExistsAsyncTask().execute(Udid, VodItem.getID(), VodItem.getCATEGORY());
	}

	ArrayList<ImageView> imageViewArrayList = new ArrayList<ImageView>();

	private void SetupUI()
	{

		TextView titleText = (TextView)findViewById(R.id.vod_detailview_title);
		TextView contentText = (TextView)findViewById(R.id.vod_detailview_content_text);
		TextView directorText = (TextView)findViewById(R.id.vod_detailView_detactor_text);
		TextView actorText = (TextView)findViewById(R.id.vod_detailView_actor_text);
		TextView gradeText = (TextView)findViewById(R.id.vod_detailView_grade_text);
		TextView runtimeText = (TextView)findViewById(R.id.vod_detailView_runningTime_text);
		TextView	 priceText = (TextView)findViewById(R.id.vod_detailView_price_text);

		GradeImageView gradeImage = (GradeImageView)findViewById(R.id.vod_detailview_grade);
		TypeImageView typeImage = (TypeImageView)findViewById(R.id.vod_detailview_type);
		ImageView posterImage = (ImageView)findViewById(R.id.vod_detailview_vodPoster_image);

		Title = VodItem.getTitle();
		Contents = app.endEllipsize(VodItem.getContents(), VOD_DETAILVIEW_CONTENT_LENGTH);
		Director = VodItem.getDirector();
		Actor =  VodItem.getActor();
		Grade = VodItem.getGrade();
		RunningTime = VodItem.getRunningTime();
		Price = VodItem.getPrice();
		PosterUrl = VodItem.getPosterUrl();
		VideoUrl = VodItem.getVideoUrl();


		gradeImage.setGrade(Grade);
		typeImage.setType(VodItem.getHD());

		titleText.setText(Title);
		titleText.setSelected(true);
		contentText.setText(Contents);
		directorText.setText(Director);
		actorText.setText(Actor);
		gradeText.setText(Grade);
		runtimeText.setText(RunningTime);
		priceText.setText(Price);

		posterImage.setScaleType(ScaleType.FIT_XY);

		Log.d(DEBUG_TAG, "PosterUrl : " + PosterUrl);
		int lastPos = PosterUrl.lastIndexOf("/");
		String ImageName = PosterUrl.substring(lastPos);
		Bitmap LoadBitmap = BitmapLoadFromFile(ImageName);
		if(LoadBitmap==null)
			imageDownloader.download(PosterUrl, posterImage, BitmapFactory.decodeResource(getResources(), R.drawable.noimg_poster_2));
		else
			posterImage.setImageBitmap(LoadBitmap);

		shareArrayList.add("Twitter");
		shareArrayList.add("Face book");
		shareArrayList.add("SMS(문자)");

		ImageButton vodPlayButton = (ImageButton)findViewById(R.id.vod_detailview_vodplay_button);
		vodPlayButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				app.ButtonBeepPlay();

				if(app.GetNetworkInfo() == app.MOBILE_CONNECTED)
				{
					new AlertDialog.Builder(getParent().getParent())
					.setTitle("주 의")
					.setMessage(getResources().getString(R.string.vod_warring))
					.setNegativeButton("확인", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							Intent i = new Intent(Intent.ACTION_VIEW , Uri.parse(VideoUrl) );
							startActivity(i);
						}
					})
					.create().show(); 
				}
				else
				{
					Intent i = new Intent(Intent.ACTION_VIEW , Uri.parse(VideoUrl) );
					startActivity(i);
				}
			}
		});

		if(VideoUrl.equals(""))
			vodPlayButton.setVisibility(View.INVISIBLE);

		ImageButton shareButton = (ImageButton)findViewById(R.id.vod_detailview_share_button);
		shareButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				app.ButtonBeepPlay();

				TextView title = new TextView(getApplicationContext());
				title.setText("공유하기");
				title.setBackgroundColor(Color.BLACK);
				title.setPadding(10, 10, 10,10);
				title.setGravity(Gravity.CENTER);
				title.setTextColor(Color.WHITE);
				title.setTextSize(20);

				new AlertDialog.Builder(getParent().getParent())
				.setCustomTitle(title)
				.setCancelable(true)
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						app.ButtonBeepPlay();
						dialog.dismiss(); // 닫기
					}
				})
				.setAdapter(new VODSE_SHARE_DIALOG_Adapter(getApplicationContext(), R.layout.vod_detail_share_row,shareArrayList ) , 
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(which)
						{
						case 0:
						{


							final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
							name = (EditText)textEntryView.findViewById(R.id.username_edit);
							pass = (EditText)textEntryView.findViewById(R.id.password_edit);
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(getParent().getParent())
							.setTitle("트위터 로그인")
							.setView(textEntryView)
							.setPositiveButton("확인", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {

									try {
										AccessToken accessToken = null;
										ConfigurationBuilder builder = new ConfigurationBuilder();
										builder.setOAuthConsumerKey(app.TwitterConsumerKey);
										builder.setOAuthConsumerSecret(app.TwitterSecretKey); 
										Configuration config =  builder.build();
										OAuthAuthorization oauth = new OAuthAuthorization(config , app.TwitterConsumerKey, app.TwitterSecretKey );
										accessToken = oauth.getOAuthAccessToken(name.getText().toString(), pass.getText().toString()); 
										TwitterFactory facrory = new TwitterFactory(config);
										Twitter twitter = facrory.getOAuthAuthorizedInstance(accessToken);
										Status status = twitter.updateStatus(VOD_DETAILVIEW_SMS_MESSAGE);
										Log.d("Sangtae","Succesefull Update!!" +  status.getText());
										Toast.makeText(getParent().getParent(),
												"등록 성공",
												Toast.LENGTH_SHORT).show();

									}
									catch (TwitterException e) {
										Log.d("Sangtae" , "Failed : " + e.getMessage() + "Code: "+e.getStatusCode());
										String errMessage = "";
										switch (e.getStatusCode()) {
										case 401:
											errMessage = "사용자 인증 오류 :  ID/Pass를 확인 바랍니다.";
											break;
										case 403:
											errMessage = "팔로잉 제한 : 트워터 팔로잉 제한으로 등록되지 않았습니다.";
											break;
										case -1:
											errMessage = "사용자 인증 오류 :  ID/Pass를 입력 바랍니다.";
											break;
										case 500:
											errMessage = "네트워크 오류 : 등록 실패하였습니다.";
											break;
										case 503:
											errMessage = "서버 과부화 :  잠시 후 재시도 바랍니다.";
											break;
										}

										Toast.makeText(getParent().getParent(),
												errMessage,
												Toast.LENGTH_SHORT).show();
									}
									/* User clicked OK so do some stuff */
								}
							})
							.setNegativeButton("취소", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {

									/* User clicked cancel so do some stuff */
								}
							});
							alertDialog.show();

							//			    	   		ProgressDialog.show(getParent() , "", "잠시만 기다려주세요");

							break;
						}
						case 1:
						{

							//				        	parameters.putString("picture", );
							//				        	parameters.putString("link", );
							//				        	parameters.putString("name", "by 씨앤앰 TV 가이드");
							//				        	parameters.putString("caption", "by 씨앤앰 TV 가이드");

							SessionStore.restore(mFacebook, VODSE_Detailview.this);
							SessionEvents.addAuthListener(new SessionListener());
							SessionEvents.addLogoutListener(new SessionListener());

							mFacebook.authorize(VODSE_Detailview.this.getParent(), new String[] {},
									new LoginDialogListener());

							break;
						}			    	   	
						case 2:
						{
							Intent intent = new Intent(Intent.ACTION_SENDTO);
							Uri uri = Uri.parse("sms:" + "");
							intent.setData(uri);
							intent.putExtra("sms_body", VOD_DETAILVIEW_SMS_MESSAGE);

							startActivity(intent);
							break;
						}
						}

					}
				})
				.create().show(); 
			}
		});

		zzimButton = (ToggleButton)findViewById(R.id.vod_detailview_zzim_button);
		zzimButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				app.ButtonBeepPlay();
				if(app.getSettopRegAuth())
				{
					if(zzimButton.isChecked())
					{
						new ZzimInsertAsyncTask().execute(Udid,VodItem.getID(), VodItem.getCATEGORY());
					}
					else
					{
						new ZzimDeleteAsyncTask().execute(Udid, VodItem.getID(), VodItem.getCATEGORY());
					}
				}
				else
				{
					Intent intent = new Intent(getParent().getParent(), Setting_SettopRegi.class);
					TabGroupActivity parentActivity = (TabGroupActivity)getParent();
					parentActivity.startChildActivity("SettopRegi", intent);
				}

			}
		});
		
		mTvPlayTask = new RemoteControlAsyncTask(); 
		mSettopTVInfoBtn = (ImageButton)findViewById(R.id.vod_detailview_tvview_button);
		mSettopTVInfoBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if (mTvPlayTask.getStatus() != AsyncTask.Status.RUNNING) {
					if (mTvPlayTask.getStatus() == AsyncTask.Status.FINISHED) {
						mTvPlayTask = new RemoteControlAsyncTask();
					}
					mTvPlayTask.execute(XML_Address_Define.Vod.getSetvodsettopdisplayinfo(Udid, VodItem.getID()));
				}				
			}
		});
		
	}

	private void setUpNaviArea() {
		CNMApplication tempApp = (CNMApplication)getApplication();	

		//Resources res = getResources(); 

		NaviBtn_Singleton mNaviBtnSingleton 	=  tempApp.getNaviBtn_Singleton();//(ImageButton)findViewById(R.id.Navi_LeftImgBtn);
		ToggleButton naviLeftSubBtn = (ToggleButton)mNaviBtnSingleton.getNaviLeftSubBtn();
		naviLeftSubBtn.setVisibility(View.INVISIBLE);

		//Drawable drawableBack = res.getDrawable(R.drawable.navi_back_btn);
		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_back);//)ImageDrawable(drawableBack);

		naviLeftBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				app.ButtonBeepPlay();
				onBackPressed();
			}
		});

		TextView naviHeaderText = (TextView)mNaviBtnSingleton.getNaviHeaderText();
		naviHeaderText.setText("상세보기");

		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		//naviRightBtn.setChecked(false);
		//		naviRightBtn.setVisibility(View.VISIBLE);
		naviRightBtn.setVisibility(View.GONE);

		ImageButton naviRightSubBtn = (ImageButton)mNaviBtnSingleton.getNaviRightSubBtn();
		naviRightSubBtn.setVisibility(View.GONE);
		//		naviRightSubBtn.setVisibility(View.VISIBLE);

	}

	class VODSE_SHARE_DIALOG_Adapter extends ArrayAdapter<String>
	{

		LayoutInflater mInflater;
		ArrayList<String> itemsArray;
		public VODSE_SHARE_DIALOG_Adapter(Context context, int res, ArrayList<String> items)
		{
			super(context, res, items);
			itemsArray = items;
			//mInflater = getLayoutInflater().from(context);
			mInflater = LayoutInflater.from(context);
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if(convertView ==null)
			{
				convertView = mInflater.inflate(R.layout.vod_detail_share_row, null);
			}

			TextView tv = (TextView)convertView.findViewById(R.id.vod_detail_share_text);
			tv.setText(itemsArray.get(position));

			ImageView iv = (ImageView)convertView.findViewById(R.id.vod_detail_share_icon);
			switch(position)
			{
			case 0:
				iv.setBackgroundResource(R.drawable.icon_twitter);
				break;
			case 1:
				iv.setBackgroundResource(R.drawable.icon_facebook);
				break;
			case 2:
				iv.setBackgroundResource(R.drawable.icon_sms);
				break;
			}
			return convertView; 
		}


	}


	public class WallPostDialogListener extends BaseDialogListener {

		public void onComplete(Bundle values) {
			final String postId = values.getString("post_id");
			if (postId != null) {
				Log.d("test", "Dialog Success! post_id=" + postId);
				mAsyncRunner.request(postId, new WallPostRequestListener());
			} else {
				Log.d("test", "No wall post made");
			}
		}

		@Override
		public void onFacebookError(FacebookError e) {

			
		}

		@Override
		public void onError(DialogError e) {

			
		}
	}

	public class WallPostRequestListener extends BaseRequestListener {

		public void onComplete(final String response) {
			Log.d("Facebook-Example", "Got response: " + response);
			String message;// = "null";
			try {
				JSONObject json = Util.parseJson(response);
				message = json.getString("message");
				Log.d(DEBUG_TAG, "Facebook 담벼락쓰기 완료 메세지: "+message);
			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
		}
	}

	private final class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			SessionEvents.onLoginSuccess();

			Bundle parameters = new Bundle();
			parameters.putString("message", VOD_DETAILVIEW_SMS_MESSAGE);
			parameters.putString("description", "by 씨앤앰 TV 가이드");

			Log.d(DEBUG_TAG, "Call isSessionValid");
			mFacebook.dialog(
					getParent().getParent(), 
					"stream.publish", parameters, new WallPostDialogListener());

		}

		public void onFacebookError(FacebookError error) {
			SessionEvents.onLoginError(error.getMessage());
			Log.d(DEBUG_TAG, "Call onFacebookError : " + error.getMessage());
		}

		public void onError(DialogError error) {
			SessionEvents.onLoginError(error.getMessage());
			Log.d(DEBUG_TAG, "Call onError : " + error.getMessage());
		}

		public void onCancel() {
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	/*
	 * TODO: 일단은 안쓰이는 듯하여 막아놨지만 나중에 쓰일수도...
	private class LogoutRequestListener extends BaseRequestListener {
		public void onComplete(String response) {
			// callback should be run in the original thread, 
			// not the background thread
			mHandler.post(new Runnable() {
				public void run() {
					SessionEvents.onLogoutFinish();
				}
			});
		}
	}
	*/

	private class SessionListener implements AuthListener, LogoutListener {

		public void onAuthSucceed() {
			SessionStore.save(mFacebook, VODSE_Detailview.this);
			Log.d(DEBUG_TAG, "call onAuthSucceed");
		}

		public void onAuthFail(String error) {
			Log.d(DEBUG_TAG, "Call onAuthFail");
		}

		public void onLogoutBegin() {   
			Log.d(DEBUG_TAG, "call onLogoutBegin");
		}

		public void onLogoutFinish() {
			SessionStore.clear(VODSE_Detailview.this);
			Log.d(DEBUG_TAG, "call onLogoutFinish");
		}
	}

	class TVViewAsyncTask extends AsyncTask<String, Void, RESPONSE_DATA>
	{

		ProgressDialog pro;
		@Override
		protected RESPONSE_DATA doInBackground(String... params) {

			/*String MemberId = params[0];
			String AssetId = "&AssetID=" + params[1];
			String Category = "&genre_Id=" + params[2];
			String paserUrl = app.URL_ZZIM_INSERT+ MemberId + AssetId + Category;
			Log.d(DEBUG_TAG, "paserUrl : " + paserUrl);
			VOD_ZZIM_PASER PARSER = new VOD_ZZIM_PASER(XML_Address_Define.Vod.getSetmyvod(params[0],  params[1], params[2], XML_Address_Define.Vod.VOD_MY_MODE_INPUT));
			PARSER.start();*/
			
			String requestURL = XML_Address_Define.Vod.getSetvodsettopdisplayinfo(Udid, params[1]);
			Log.d(DEBUG_TAG, "### URL Request VOD상세정보: " + requestURL);
			CHECK_RESPONSE_PASER.start(requestURL);
			
			return CHECK_RESPONSE_PASER.getArrayList();
		}

		@Override
		protected void onPostExecute(RESPONSE_DATA result) {

			super.onPostExecute(result);

			pro.dismiss();

			try
			{

				if(result != null && result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_100))
				{
					zzimButton.setChecked(true);
					zzimButton.setBackgroundResource(R.drawable.detailview_zzim_dissolve_btn);
					//					app.insertZzimItem(VodItem);
				}
				else
				{
					zzimButton.setChecked(false);
					Toast.makeText(VODSE_Detailview.this, "찜 등록 실패. 잠시뒤에 다시 시도해주세요.", Toast.LENGTH_SHORT).show();

				}
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
	}
	
	class ZzimInsertAsyncTask extends AsyncTask<String, Void, VOD_ZZIM_LIST>
	{

		ProgressDialog pro;
		@Override
		protected VOD_ZZIM_LIST doInBackground(String... params) {

			/*String MemberId = params[0];
			String AssetId = "&AssetID=" + params[1];
			String Category = "&genre_Id=" + params[2];
			String paserUrl = app.URL_ZZIM_INSERT+ MemberId + AssetId + Category;
			Log.d(DEBUG_TAG, "paserUrl : " + paserUrl);*/
			String requestURL = XML_Address_Define.Vod.getInsertmyvod(params[0],  params[1], params[2]);
			Log.d(DEBUG_TAG, "### URL Request 찜하기: " + requestURL);
			VOD_ZZIM_PASER.start(requestURL);
			return VOD_ZZIM_PASER.getArrayList();
		}

		@Override
		protected void onPostExecute(VOD_ZZIM_LIST result) {
			super.onPostExecute(result);

			pro.dismiss();

			try
			{

				Log.d(DEBUG_TAG, "result.getRESULT() : " + result.getRESULT());
				if(result != null && (result.getRESULT().equals(XML_Address_Define.ErrorCode.ERROR_100)
						|| result.getRESULT().equals("ok")) )
				{
					zzimButton.setChecked(true);
					zzimButton.setBackgroundResource(R.drawable.detailview_zzim_dissolve_btn);
					//					app.insertZzimItem(VodItem);
				}
				else
				{
					zzimButton.setChecked(false);
					Toast.makeText(VODSE_Detailview.this, "찜 등록 실패. 잠시뒤에 다시 시도해주세요.", Toast.LENGTH_SHORT).show();

				}
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
	}

	class ZzimDeleteAsyncTask extends AsyncTask<String, Void, VOD_ZZIM_LIST>
	{

		ProgressDialog pro;
		@Override
		protected VOD_ZZIM_LIST doInBackground(String... params) {

			/*String MemberId = params[0];
			String AssetId = "&AssetID=" + params[1];
			String Category = "&genre_Id=" + params[2];
			String paserUrl = app.URL_ZZIM_DELET+ MemberId + AssetId + Category;

			Log.d(DEBUG_TAG, "paserUrl : " + paserUrl);*/
			String requestURL = XML_Address_Define.Vod.getSetmyvod(params[0], params[1], params[2], XML_Address_Define.Vod.VOD_MY_MODE_SPECIFIC_CONTENT_DELETE);
			Log.d(DEBUG_TAG, "### URL Request 찜삭제: " + requestURL);

			VOD_ZZIM_PASER.start(requestURL);
			return VOD_ZZIM_PASER.getArrayList();
		}

		@Override
		protected void onPostExecute(VOD_ZZIM_LIST result) {

			super.onPostExecute(result);

			pro.dismiss();

			try
			{
				if(result != null && (result.getRESULT().equals(XML_Address_Define.ErrorCode.ERROR_100)
					|| result.getRESULT().equals("ok")) )
				{
					zzimButton.setChecked(false);
					zzimButton.setBackgroundResource(R.drawable.detailview_zzim_btn);
					//					app.deleteZzimItems(VodItem);
				}
				else
				{
					zzimButton.setChecked(true);
					Toast.makeText(VODSE_Detailview.this, "찜 삭제 실패. 잠시뒤에 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
				}
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

	}

	class ZzimExistsAsyncTask extends AsyncTask<String, Void, VOD_ZZIM_LIST>
	{

		ProgressDialog pro;
		@Override
		protected VOD_ZZIM_LIST doInBackground(String... params) {
	
			String requestURL = XML_Address_Define.Vod.getGetvodmylist(params[0]);
			Log.d(DEBUG_TAG, "## URL Request 찜목록(ZzimExistsAsyncTask): " + requestURL);

			//VOD_ZZIM_PASER PARSER = new VOD_ZZIM_PASER();  // 개별 확인 api가 사라져 전체 목록에서 확인함.
			VOD_ZZIM_PASER.start(requestURL);
			VOD_ZZIM_LIST list = VOD_ZZIM_PASER.getArrayList();
			Log.d(DEBUG_TAG, "VOD_ZZIM_LIST: " + list);
			Log.d(DEBUG_TAG, "VOD_ZZIM_LIST: " + list);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if (params[1].contains(list.get(i).getID())) {
						return VOD_ZZIM_PASER.getArrayList();
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(VOD_ZZIM_LIST result) {

			super.onPostExecute(result);

			pro.dismiss();

			try
			{
				//if(result ==null)
				//{
				//	onCancelled();
				//}
				//else
					if(result !=null && result.getRESULT().equals(XML_Address_Define.ErrorCode.ERROR_100))
					{
						zzimButton.setChecked(true);
						zzimButton.setBackgroundResource(R.drawable.detailview_zzim_dissolve_btn);
					}
					else 
					{
						zzimButton.setChecked(false);
						zzimButton.setBackgroundResource(R.drawable.detailview_zzim_btn);
					}
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
	
	
	class RemoteControlAsyncTask extends AsyncTask<String, Void, RESPONSE_DATA>{

		@Override
		protected RESPONSE_DATA doInBackground(String... params) {

			String requestURL = params[0];
			Log.d(DEBUG_TAG, "### URL Request TV로 보기: "+requestURL);
			if (CHECK_RESPONSE_PASER.start(requestURL)) {
				return CHECK_RESPONSE_PASER.getArrayList();
			}

			return null;
		}

		@Override
		protected void onPostExecute(RESPONSE_DATA result) {

			super.onPostExecute(result);

			if (result != null) {
				if (result.getResultCode().contains(XML_Address_Define.ErrorCode.ERROR_100)) {
					Log.d("Sangtae", "TV로보기 명령 성공");
				}else {
					Log.d("Sangtae", "TV로보기 명령 실패: "+ result.getErrorString());
				}
			} else {
				// 에러 코드 표시
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		mFacebook.authorizeCallback(requestCode, resultCode, data);

		Log.d(DEBUG_TAG, "Call onActivityResult");
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

}
