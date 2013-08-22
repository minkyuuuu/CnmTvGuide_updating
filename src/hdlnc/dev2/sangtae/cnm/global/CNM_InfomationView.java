/**
 * 
 */
package hdlnc.dev2.sangtae.cnm.global;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.DATA.GuideImage_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.GuideImage_DATA_LIST_PASER;
import hdlnc.dev2.sangtae.cnm.main.main;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCHImageDownloader;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Sangtae
 *
 */
public class CNM_InfomationView extends Activity {

	private final static String DEBUG_TAG = "CNM_InfomationView";

	private CNMApplication tempApp;
	private String tempSettopStatus;
	private String tempCurrentTab;
	private String tempGuideURL;

	private TVCHImageDownloader imageDownloader;

	//private TextView mSubject;
	private ImageView mImageView;
	private TextView  mContent;
	private ImageButton mButton;

	private GuideImage_DATA_LIST mGuideList;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cnm_infomation);
		//mSubject	=	(TextView)findViewById(R.id.InfoSubject);
		mImageView	=	(ImageView)findViewById(R.id.CNM_Info_Img);
		mContent	=	(TextView)findViewById(R.id.infoContent);

		mButton		=	(ImageButton)findViewById(R.id.CNM_info_btn);

		tempApp = (CNMApplication)getApplication();
		mGuideList = new GuideImage_DATA_LIST();
		imageDownloader = new TVCHImageDownloader(this);

		setupUI();
		
		new GuideImageTask().execute(XML_Address_Define.Service.getGetserviceguideimage());

		//mWebView.loadUrl("http://m.naver.com");
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		setupNaviBar();
	}

	@Override
	public void onBackPressed() {
		
		//super.onBackPressed();
		NaviBtn_Singleton.getInstance().getNaviLeftBtn().performClick();
	}

	private void setupNaviBar() {

		tempCurrentTab	= getIntent().getStringExtra(Intent_KEY_Define.MAIN_TAB.SELECT_MAIN_TAB);
		
		if (tempCurrentTab.equals(Intent_KEY_Define.MAIN_TAB.TYPE.PVR)) { // PVR 탭인 경우
			NaviBtn_Singleton.getInstance().getNaviHeaderText().setText("PVR");
		}
		else if (tempCurrentTab.equals(Intent_KEY_Define.MAIN_TAB.TYPE.REMOCON)) { // 리모콘 탭인 경우
			NaviBtn_Singleton.getInstance().getNaviHeaderText().setText("리모콘");
		}
		NaviBtn_Singleton.getInstance().getNaviLeftSubBtn().setVisibility(View.GONE);
		NaviBtn_Singleton.getInstance().getNaviLeftBtn().setBackgroundResource(R.drawable.top_button_home);
		NaviBtn_Singleton.getInstance().getNaviLeftBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				tempApp.ButtonBeepPlay();
				main tempActivity = (main)getParent().getParent();
				tempActivity.Home_View(0);
			}
		});
		NaviBtn_Singleton.getInstance().getNaviRightBtn().setVisibility(View.GONE);
		NaviBtn_Singleton.getInstance().getNaviRightSubBtn().setVisibility(View.VISIBLE);
		NaviBtn_Singleton.getInstance().getNaviRightSubBtn().setBackgroundResource(R.drawable.top_button_useguide);
		NaviBtn_Singleton.getInstance().getNaviRightSubBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				tempApp.ButtonBeepPlay();
				goToGuideInfo();
			}
		});
	}
	
	private void goToGuideInfo() {
		
		tempApp.setGoToSetting(Intent_KEY_Define.Set_Mode.MODE.GUIDE_INFO);
		tempApp.getMainrbFifth().performClick();
	}

	private void setupUI() {

		tempSettopStatus = getIntent().getStringExtra(Intent_KEY_Define.SETTOP.STATUS);
		tempCurrentTab	= getIntent().getStringExtra(Intent_KEY_Define.MAIN_TAB.SELECT_MAIN_TAB);
		//setGuideImage();
		if (null != tempSettopStatus) {
			if (tempSettopStatus.equals(Intent_KEY_Define.SETTOP.MODE.UNREGISTERED)) {
				mButton.setBackgroundResource(R.drawable.button_stbsign);
				mButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						
						goToSettopRegi();
					}
				});
			} else if (tempSettopStatus.equals(Intent_KEY_Define.SETTOP.MODE.NOT_SUPPORT)) {
				// 상품 안내 웹페이지 표시
				mButton.setBackgroundResource(R.drawable.button_serviceguide);
				mButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						
						goToSettopPreview();
					}
				});

			}
			mButton.setVisibility(View.VISIBLE);
		} else {
			// 정상적인 값이 아닌 경우 버튼을 숨김.
			mButton.setVisibility(View.GONE);
		}

	}

	private void  setGuideImage(){
		if (tempCurrentTab.equals(Intent_KEY_Define.MAIN_TAB.TYPE.PVR)) { // PVR 탭인 경우
			if (tempSettopStatus.equals(Intent_KEY_Define.SETTOP.MODE.UNREGISTERED)) {
				for (int i = 0; i < mGuideList.size(); i++) {
					if (mGuideList.get(i).getGroupType().equals(XML_Address_Define.Service.PVR_UNREGISTERED)) {
						// PVR 셋탑 미등록
						//mSubject.setText(mGuideList.get(i).getSubject());
						mContent.setText(mGuideList.get(i).getContent());
						imageDownloader.download(mGuideList.get(i).getImageFileURL(), mImageView);
						tempGuideURL = mGuideList.get(i).getGuideURL();
						break;
					}
				}
			} else if (tempSettopStatus.equals(Intent_KEY_Define.SETTOP.MODE.NOT_SUPPORT)) {
				for (int i = 0; i < mGuideList.size(); i++) {
					if (mGuideList.get(i).getGroupType().equals(XML_Address_Define.Service.SD_HD_REGISTERED)) {
						//  SD,HD셋탑등록 
						//mSubject.setText(mGuideList.get(i).getSubject());
						mContent.setText(mGuideList.get(i).getContent());
						imageDownloader.download(mGuideList.get(i).getImageFileURL(), mImageView);
						tempGuideURL = mGuideList.get(i).getGuideURL();
						break;
					}
				}
			}
		} else if (tempCurrentTab.equals(Intent_KEY_Define.MAIN_TAB.TYPE.REMOCON)) { // 리모콘 탭인 경우
			if (tempSettopStatus.equals(Intent_KEY_Define.SETTOP.MODE.UNREGISTERED)) {
				for (int i = 0; i < mGuideList.size(); i++) {
					if (mGuideList.get(i).getGroupType().equals(XML_Address_Define.Service.REMOCON_UNREGISTERED)) {
						// 리모컨 미등록  
						//mSubject.setText(mGuideList.get(i).getSubject());
						mContent.setText(mGuideList.get(i).getContent());
						imageDownloader.download(mGuideList.get(i).getImageFileURL(), mImageView);
						tempGuideURL = mGuideList.get(i).getGuideURL();
						break;
					}
				}
			} else if (tempSettopStatus.equals(Intent_KEY_Define.SETTOP.MODE.NOT_SUPPORT)) {
				for (int i = 0; i < mGuideList.size(); i++) {
					if (mGuideList.get(i).getGroupType().equals(XML_Address_Define.Service.SD_SETTOP_REGISTERED)) {
						// 리모컨 SD셋탑등록
						//mSubject.setText(mGuideList.get(i).getSubject());
						mContent.setText(mGuideList.get(i).getContent());
						imageDownloader.download(mGuideList.get(i).getImageFileURL(), mImageView);
						tempGuideURL = mGuideList.get(i).getGuideURL();
						break;
					}
				}
			}
		}
	}

	private void goToSettopRegi() {
		
		tempApp.setGoToSetting(Intent_KEY_Define.Set_Mode.MODE.SETTOP_JOIN);
		tempApp.getMainrbFifth().performClick();
	}
	
	

	private void goToSettopPreview() {
		
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tempGuideURL));
		startActivity(intent);
		/*Intent intent = new Intent(getParent().getParent(), Setting_SettopRegi.class);
		TabGroupActivity parentActivity = (TabGroupActivity)getParent();
		parentActivity.startChildActivity("SettopRegi", intent);*/
	}


	private class GuideImageTask extends AsyncTask<String, Void, GuideImage_DATA_LIST>{
		private ProgressDialog pro;
		private GuideImage_DATA_LIST_PASER paser;
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			pro = ProgressDialog.show(getParent().getParent(), "", getResources().getString(R.string.tv_watting),true);
		}

		@Override
		protected GuideImage_DATA_LIST doInBackground(String... params) {
			
			paser = new GuideImage_DATA_LIST_PASER(params[0]);

			for (int i = 0; i < 3; i++) {
				if (paser.start()) {
					return paser.getGuideImage_DATA_LIST();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(GuideImage_DATA_LIST result) {
			
			super.onPostExecute(result);
			pro.dismiss();

			if (result != null) {

				if (result.getResultCode().equals(XML_Address_Define.ErrorCode.ERROR_100)) {
					mGuideList = result;
					setGuideImage();
					// setupUI();	
				} else {
					// 에러 처리
				}
			}
		}	

	}

}
