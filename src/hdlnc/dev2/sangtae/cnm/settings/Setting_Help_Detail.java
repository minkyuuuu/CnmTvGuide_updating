package hdlnc.dev2.sangtae.cnm.settings;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.GUIDE_DET_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.GUIDE_DET_PARSER;

import hdlnc.dev2.sangtae.cnm.R;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Setting_Help_Detail extends Activity {
	public static final String GUIDE_ITEM_SELECT="GUIDE_SELECT_ITEM";
	public static final String GUIDE_ITEM_TITLE="GUIDE_ITEM_TITLE";
	private CNMApplication app;
	private GUIDE_DET_PARSER guide_DET_PARSER;
	private GUIDE_DET_DATA guide_det_data;
	private String guide_det_title;
	String htmldata;
	private WebView browser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_help_detail);
		app = (CNMApplication)getApplication();
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setupUI();
				
			}
		});
		
	}

	
	private void setupUI(){
			
		String sID = getIntent().getStringExtra(GUIDE_ITEM_SELECT);
		guide_DET_PARSER = new GUIDE_DET_PARSER(sID);
		guide_DET_PARSER.start();
		guide_det_data = guide_DET_PARSER.getGUIDE_DET_DATA(sID);
		
		guide_det_title = getIntent().getStringExtra(GUIDE_ITEM_TITLE);
		
		if(guide_det_data != null) {
			htmldata = "<html><meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-16le\">" 
						+ "<body background=\"file:///android_asset/list_backbg_04.png\">"
						+ guide_det_data.getCONTENT() 
						+ "</body></html>";
			

			browser=(WebView)findViewById(R.id.webkit);
			browser.setHorizontalScrollBarEnabled(false);
			browser.setVerticalScrollBarEnabled(false);
		
			//browser.loadUrl("http://commonsware.com");
			//browser.loadData(guide_det_data.getCONTENT(), "text/html", "utf-16le");
//			browser.loadData(htmldata, "text/html", "utf-8"); //Android Issue, Can't load by background image res
			browser.loadDataWithBaseURL("about:blank", htmldata, "text/html", "utf-8", "");
		}	
	}
	
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		NaviBtn_Singleton mNaviBtnSingleton 	=  app.getNaviBtn_Singleton();//(ImageButton)findViewById(R.id.Navi_LeftImgBtn);
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
		TextView txtHeader = (TextView)mNaviBtnSingleton.getNaviHeaderText();
		txtHeader.setText(guide_det_title);
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
}
