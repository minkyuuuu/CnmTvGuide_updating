package hdlnc.dev2.sangtae.cnm.joyn;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.ImageDownloader;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.JOYN_DET_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.JOYN_DET_PARSER;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import hdlnc.dev2.sangtae.cnm.R;

public class JoyNDetail extends Activity {
	private static final String JOYN_ITEM_SELECT = "JOYN_SELECT_ITEM";
	private CNMApplication app;
	private NaviBtn_Singleton mNaviBtnSingleton;
	private JOYN_DET_DATA joyn_det_data;
	private JOYN_DET_PARSER joyn_DET_PARSER;
	private final ImageDownloader imageDownloader = new ImageDownloader();
	private final String DEBUG_TAG = "JoyNDetail";

	private ProgressDialog pro;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joy_n_detail);
		app = (CNMApplication)getApplication();
		mNaviBtnSingleton 	=  app.getNaviBtn_Singleton();
		String sID = getIntent().getStringExtra(JOYN_ITEM_SELECT);
		
		new JoyNAsyncTask().execute(sID);
		
		imageDownloader.setContext(getApplicationContext());
	
	}
	private void setupUI(){
			
		TextView subtitle = (TextView)findViewById(R.id.JoyDET_SubTitle);
		subtitle.setText(joyn_det_data.getSUBTITLE());
		
		String htmlImgdata = "<html><meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-16le\">" 
//				+ "<body background=" + joyn_det_data.getIMG() + ">"
			+ "<body><img width=398 src=" + joyn_det_data.getIMG() + " border=0 hspace=0 vspace=0> </img>"
			+ "</body></html>";

		
		WebView img = (WebView)findViewById(R.id.JoyDET_IMG);
		img.setHorizontalScrollBarEnabled(false);
		img.setVerticalScrollBarEnabled(false);
		img.loadDataWithBaseURL("about:blank", htmlImgdata, "text/html", "utf-8", "");
		img.setBackgroundColor(0);
		img.setInitialScale(100);
		img.setEnabled(false);
		img.getSettings().setBuiltInZoomControls( false ); 
		img.getSettings().setSupportZoom( false ); 
		img.getSettings().setGeolocationEnabled( false ); 
		img.getSettings().setLightTouchEnabled( false ); 
		img.setWebViewClient(new WebViewClient(){

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				
				if(pro !=null)
					pro.dismiss();
				
			}
		
		});
		
		//img.set.setText(joyn_det_data.getSUBTITLE())
		
//		ImageView img = (ImageView)findViewById(R.id.JoyDET_IMG);
//		img.setScaleType(ScaleType.FIT_XY);
//		imageDownloader.download(joyn_det_data.getIMG(), img);
//		imageDownloader.download(joyn_det_data.getIMG(), img, BitmapFactory.decodeResource(getResources(), R.drawable.noimg_poster_2));
		
		Log.d("urlTest", joyn_det_data.getIMG());
		Log.d("urlTest", joyn_det_data.getCONTENT());
		
		String htmldata = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
			+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
			+ "<head><style type= \"text/css\" >html, body { background-color: transparent; }</style></head>"
			+ "<body>"
			+ joyn_det_data.getCONTENT() 
			+ "</body></html>";
		
		WebView content = (WebView)findViewById(R.id.JoyDET_Content);
		content.setBackgroundColor(0);
		content.setHorizontalScrollBarEnabled(false);
		content.setVerticalScrollBarEnabled(false);
		content.setEnabled(false);
		content.getSettings().setBuiltInZoomControls( false ); 
		content.getSettings().setSupportZoom( false ); 
		content.getSettings().setGeolocationEnabled( false ); 
		content.getSettings().setLightTouchEnabled( false );
		
		content.loadDataWithBaseURL("about:blank", htmldata, "text/html", "utf-8", "");
		
//		TextView content = (TextView)findViewById(R.id.JoyDET_Content);
//		content.setText(joyn_det_data.getCONTENT(), TextView.BufferType.SPANNABLE);
		
		
		
//		content.setText(Html.fromHtml(
//		"<font size=30>&lt;ÀÌ¿ë¾È³»&gt;</font>"));
//		content.setText(Html.fromHtml(joyn_det_data.getCONTENT()), TextView.BufferType.SPANNABLE);
		TextView txtHeader = (TextView)mNaviBtnSingleton.getNaviHeaderText();
				
		txtHeader.setText(joyn_det_data.getTITLE());
	}
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Resources res = getResources();
		//Drawable drawableBack = res.getDrawable(R.drawable.navi_back_btn);
		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_back);//ImageDrawable(drawableBack);
		naviLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Log.d("Navigation","Home button Click!! - JoyN");
				app.ButtonBeepPlay();
				onBackPressed();
			}
		});
		ToggleButton naviLeftSubBtn = (ToggleButton)mNaviBtnSingleton.getNaviLeftSubBtn();
		naviLeftSubBtn.setVisibility(View.GONE);
		ImageButton naviRightSubBtn = (ImageButton)mNaviBtnSingleton.getNaviRightSubBtn();
		naviRightSubBtn.setVisibility(View.GONE);
		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		naviRightBtn.setVisibility(View.GONE);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	
	class JoyNAsyncTask extends AsyncTask<String, Void, JOYN_DET_DATA>
	{
		
		@Override
		protected JOYN_DET_DATA doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			if(joyn_DET_PARSER.getJOYN_DET_DATA_LIST()==null)
			{
				joyn_DET_PARSER = new JOYN_DET_PARSER(XML_Address_Define.Service.getGetservicejoinninfo(params[0]));
				joyn_DET_PARSER.start();
				app.setJoyN_DET_DATA_LIST(joyn_DET_PARSER.getJOYN_DET_DATA());
				joyn_det_data = joyn_DET_PARSER.getJOYN_DET_DATA();
				return joyn_DET_PARSER.getJOYN_DET_DATA();
				
			}
			else
				return app.getJoyN_DET_DATA_LIST();
			
		}

		@Override
		protected void onPostExecute(JOYN_DET_DATA result) {
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
				if(joyn_det_data.getRESULT().equals(XML_Address_Define.ErrorCode.ERROR_100))
				{
					setupUI();
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
}
