package hdlnc.dev2.sangtae.cnm.settings;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.GUIDE_DATA;
import hdlnc.dev2.sangtae.cnm.global.DATA.GUIDE_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.GUIDE_PARSER;

import hdlnc.dev2.sangtae.cnm.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Setting_Help extends ListActivity {
	public static final String GUIDE_ITEM_SELECT="GUIDE_SELECT_ITEM";
	public static final String GUIDE_ITEM_TITLE="GUIDE_ITEM_TITLE";
	private CNMApplication app;
	private SetHelpAdapter mAdapter;
	private GUIDE_DATA_LIST mArrayList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_help);
		app = (CNMApplication)getApplication();
		//GUIDE_DATA_LIST
		
		ListView mListView = getListView();
		mListView.setSoundEffectsEnabled(false);
		mListView.setDividerHeight(0);
		mListView.setDivider(getResources().getDrawable(R.drawable.transparent));
		
		new HelpAsyncTask().execute(getIntent().getStringExtra("GuideCategoryID"));
		
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setupNaviArea();
		
	}
	
	private void setupNaviArea() {
		NaviBtn_Singleton mNaviBtnSingleton 	=  app.getNaviBtn_Singleton();//(ImageButton)findViewById(R.id.Navi_LeftImgBtn);
		Resources res = getResources();
		//Drawable drawableBack = res.getDrawable(R.drawable.navi_back_btn);
		ImageButton naviLeftBtn = (ImageButton)mNaviBtnSingleton.getNaviLeftBtn();
		naviLeftBtn.setBackgroundResource(R.drawable.top_button_back);//naviLeftBtn.setImageDrawable(drawableBack);
		naviLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Log.d("Navigation","Home button Click!! - JoyN");
				onBackPressed();
			}
		});
		
		ToggleButton naviLeftSubBtn = (ToggleButton)mNaviBtnSingleton.getNaviLeftSubBtn();
		naviLeftSubBtn.setVisibility(View.GONE);
		TextView txtHeader = (TextView)mNaviBtnSingleton.getNaviHeaderText();
		txtHeader.setText("ÀÌ¿ë¾È³»");
		ImageButton naviRightSubBtn = (ImageButton)mNaviBtnSingleton.getNaviRightSubBtn();
		naviRightSubBtn.setVisibility(View.GONE);
		ImageButton naviRightBtn = (ImageButton)mNaviBtnSingleton.getNaviRightBtn();
		naviRightBtn.setVisibility(View.GONE);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		app.ButtonBeepPlay();
		super.onBackPressed();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		app.ButtonBeepPlay();
		
		GUIDE_DATA guide_data = (GUIDE_DATA)mAdapter.getItem(position);
		Log.d("GUIDE",guide_data.getID());
		if (guide_data.getMore()) {
			Intent guideDetail = new Intent(getParent(), Setting_Help.class);
			guideDetail.putExtra("GuideCategoryID", guide_data.getID());
			guideDetail.putExtra(GUIDE_ITEM_TITLE, guide_data.getTITLE());
			TabGroupActivity parentActivity = (TabGroupActivity)getParent();
			parentActivity.startChildActivity("Setting_Help2", guideDetail);
						
		} else {
			Intent guideDetail = new Intent(getParent(), Setting_Help_Detail.class);
			guideDetail.putExtra(GUIDE_ITEM_SELECT, guide_data.getID());
			guideDetail.putExtra(GUIDE_ITEM_TITLE, guide_data.getTITLE());
			TabGroupActivity parentActivity = (TabGroupActivity)getParent();
			parentActivity.startChildActivity("GuideDetail", guideDetail);
		}
        
		
	}
	
	class SetHelpAdapter extends ArrayAdapter {
		Activity context;
		private LayoutInflater mInflater;
		//private Context context;
		private GUIDE_DATA_LIST items;
		
		SetHelpAdapter(Activity context, GUIDE_DATA_LIST aItems) {
			super(context, R.layout.setting_row, aItems);
			this.items = aItems;
			this.context = context;
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater=context.getLayoutInflater();
			View row=inflater.inflate(R.layout.setting_row, null);
			TextView settingText=(TextView)row.findViewById(R.id.Setting_Item);
			TextView settingValue=(TextView)row.findViewById(R.id.Setting_Item_Value);
			settingText.setText(items.get(position).getTITLE());
			settingValue.setText("");
			
			return row;
		}
	}
	
	
	
	class HelpAsyncTask extends AsyncTask<String, Void, GUIDE_DATA_LIST>
	{

		ProgressDialog pro;
		@Override
		protected GUIDE_DATA_LIST doInBackground(String... params) {
			// TODO Auto-generated method stub
			//if(app.getSetting_GUIDE_DATA_LIST() == null)
			//{
				GUIDE_PARSER PARSER = new GUIDE_PARSER();
				PARSER.start(params[0]);
				//app.setSetting_GUIDE_DATA_LIST(PARSER.getGUIDE_DATA_LIST());
				
				return PARSER.getGUIDE_DATA_LIST();
			//}
			//else
			//	return app.getSetting_GUIDE_DATA_LIST();
		}

		@Override
		protected void onPostExecute(GUIDE_DATA_LIST result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pro.dismiss();
			try
			{
				mArrayList =result;
				if(mArrayList ==null)
				{
					onCancelled();
				}
				else
				if(mArrayList.getRESULT().equals(XML_Address_Define.ErrorCode.ERROR_100))
				{
					mAdapter = new SetHelpAdapter(Setting_Help.this, mArrayList);
					setListAdapter(mAdapter);
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				Log.w("Seeting_Help", e);
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