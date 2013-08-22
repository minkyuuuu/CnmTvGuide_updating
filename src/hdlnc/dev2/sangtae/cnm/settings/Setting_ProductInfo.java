package hdlnc.dev2.sangtae.cnm.settings;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.NaviBtn_Singleton;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.LOCATION_INFO_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.PRODUCT_INFO_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.PRODUCT_INFO_PASER;

import hdlnc.dev2.sangtae.cnm.R;
import android.app.Activity;
import android.app.ListActivity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setting_ProductInfo extends ListActivity{

	private CNMApplication app;
	private SettingLocalAdapter Adapter;

	private LOCATION_INFO_PASER location_PASER;
	private PRODUCT_INFO_PASER product_PASER;
	private PRODUCT_INFO_LIST info_LIST;
	private int tempIndex;
	private CNMApplication tempApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		tempApp = (CNMApplication)getApplication();
		setUI();
		Adapter = new SettingLocalAdapter(this);
		//getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		setListAdapter(Adapter);


	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		tempApp.ButtonBeepPlay();
		tempIndex=position;
		setProductInfo(info_LIST.get(position).getID(), info_LIST.get(position).getName());
	}

	public void setUI() {
		app = (CNMApplication)getApplication();
		product_PASER = new PRODUCT_INFO_PASER(XML_Address_Define.Channel.getGetchannelproduct(tempApp.getLocationID()));
		product_PASER.start();
		product_PASER.getProduct_INFO_LIST();
		info_LIST = product_PASER.getProduct_INFO_LIST();

		for (int i = 0; i < info_LIST.size(); i++) {
			if (info_LIST.get(i).getID().equals(app.getProductID())) {
				tempIndex=i;
				break;				
			}        
		}

	}
	 @Override
	    public void onBackPressed() {
	    	// TODO Auto-generated method stub
	    	tempApp.ButtonBeepPlay();
	    	super.onBackPressed();
	    }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		app.getNaviBtn_Singleton().getNaviLeftBtn().setBackgroundResource(R.drawable.top_button_back);
		app.getNaviBtn_Singleton().getNaviLeftBtn().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		app.getNaviBtn_Singleton().getNaviLeftSubBtn().setVisibility(View.GONE);
		app.getNaviBtn_Singleton().getNaviHeaderText().setText("지역/상품 설정");
		app.getNaviBtn_Singleton().getNaviRightSubBtn().setVisibility(View.GONE);
		app.getNaviBtn_Singleton().getNaviRightBtn().setVisibility(View.GONE);
		app.loadPreferences();

	}

	public class SettingLocalAdapter extends ArrayAdapter{
		Activity context;

		SettingLocalAdapter(Activity context){
			super(context, R.layout.setting_row_chk, info_LIST);
			this.context=context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater=context.getLayoutInflater();
			convertView=inflater.inflate(R.layout.setting_row_chk, null);
			TextView settingText=(TextView)convertView.findViewById(R.id.Setting_Item);
			CheckBox chkLocal = (CheckBox)convertView.findViewById(R.id.check);
			settingText.setText(info_LIST.get(position).getName());
			if (tempIndex == position) {
				chkLocal.setChecked(true);
			} else {
				chkLocal.setChecked(false);
			}

			return convertView;
		}
	}
	private void setProductInfo(String ProductID, String ProductName) {
		app.setProductID(ProductID);
		app.setProductName(ProductName);
		app.savePreferences(true);
		app.loadPreferences();
		app.setTVCH_SettigChecked(true);
		Adapter.notifyDataSetChanged();
		
	}

}
