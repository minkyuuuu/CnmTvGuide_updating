package hdlnc.dev2.sangtae.cnm.settings;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;
import hdlnc.dev2.sangtae.cnm.global.DATA.LOCATION_INFO_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.LOCATION_INFO_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.PRODUCT_INFO_PASER;
import hdlnc.dev2.sangtae.cnm.global.DATA.SERVICE_NOTICE_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.SERVICE_NOTICE_PARSER;

import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class Setting_LocationInfo extends ListActivity {

	//private CNMApplication app;
	private SettingLocalAdapter setAdapter;

	private LOCATION_INFO_PASER location_PASER;
	private PRODUCT_INFO_PASER product_PASER;
	//String[] tempList,tempList2;
	//ArrayList<String[]> tmpList;
	String strChecked;
	private LOCATION_INFO_LIST info_LIST;    
	private int tempIndex;
	private int tempProIndex;
	private CNMApplication tempApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_setting_list);

		tempApp = (CNMApplication)getApplication();

		setUI();
		setAdapter = new SettingLocalAdapter(this);
		//getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		setListAdapter(setAdapter);
	}


	public void setUI() {
		location_PASER = new LOCATION_INFO_PASER();
		location_PASER.start();
		//slocation_PASER.getLocation_info();
		info_LIST	= location_PASER.getLocation_info();

		for (int i = 0; i < info_LIST.size(); i++) {
			if (info_LIST.get(i).getID().equals(tempApp.getLocationID())) {
				tempIndex= i;
				break;
			}
		}
		/*tmpList = new ArrayList<String[]>(location_PASER.getLocation_info().size());
        for (int i = 0; i < location_PASER.getLocation_info().size(); i++) {
            tmpList.add(new String[] {location_PASER.getLocation_info().get(i).getName(),location_PASER.getLocation_info().get(i).getName2()});
         }*/
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		tempApp.ButtonBeepPlay();
		tempIndex=position;
		setLocationInfo(info_LIST.get(position).getID(), info_LIST.get(position).getName(),	info_LIST.get(position).getName2());
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		tempApp.ButtonBeepPlay();
		tempApp.getNaviBtn_Singleton().getNaviRightSubBtn().setVisibility(View.GONE);
		tempApp.getNaviBtn_Singleton().getNaviRightSubBtn().setBackgroundResource(R.drawable.top_button_search);
		super.onBackPressed();
	}
	@Override
	protected void onResume() {
		//        // TODO Auto-generated method stub
		super.onResume();

		tempApp.getNaviBtn_Singleton().getNaviLeftBtn().setBackgroundResource(R.drawable.top_button_back);
		tempApp.getNaviBtn_Singleton().getNaviLeftBtn().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		tempApp.getNaviBtn_Singleton().getNaviLeftSubBtn().setVisibility(View.GONE);
		tempApp.getNaviBtn_Singleton().getNaviHeaderText().setText("지역/상품 설정");
		tempApp.getNaviBtn_Singleton().getNaviRightSubBtn().setVisibility(View.VISIBLE);
		tempApp.getNaviBtn_Singleton().getNaviRightSubBtn().setBackgroundResource(R.drawable.top_button_serviceset);
		tempApp.getNaviBtn_Singleton().getNaviRightSubBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Product_dalog();
			}
		});
		tempApp.getNaviBtn_Singleton().getNaviRightBtn().setVisibility(View.GONE);
		tempApp.loadPreferences();

	}

	public class SettingLocalAdapter extends ArrayAdapter{
		Activity context;

		SettingLocalAdapter(Activity context){
			super(context, R.layout.setting_2row_chk, info_LIST);
			this.context=context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = context.getLayoutInflater();
			convertView=inflater.inflate(R.layout.setting_2row_chk, null);
			TextView txtLctName1 = (TextView)convertView.findViewById(R.id.Lct_Name1);
			TextView txtLctName2 = (TextView)convertView.findViewById(R.id.Lct_Name2);
			CheckBox chkLocal = (CheckBox)convertView.findViewById(R.id.check);

			String Text1 = info_LIST.get(position).getID();//tmpList.get(position)[0];
			txtLctName1.setText(info_LIST.get(position).getName());//tmpList.get(position)[0]);
			txtLctName2.setText(info_LIST.get(position).getName2());//tmpList.get(position)[1]);

			if (tempIndex == position) {
				chkLocal.setChecked(true);
			} else {
				chkLocal.setChecked(false);
			}

			/*chkLocal.setTag(info_LIST.get(position).getID()+","+info_LIST.get(position).getName()+"<"+info_LIST.get(position).getName2());
            if(info_LIST.get(position).getName().equals(app.getLocationName())) {
                chkLocal.setChecked(true);
            }*/

			return convertView;
		}
	}

	private void setLocationInfo(String LocationID, String LocationName, String LocationName2) {
		if (!tempApp.getLocationID().equals(LocationID)) {
			tempApp.setLocationID(LocationID);
			Product_dalog();
		}
		tempApp.setLocationName(LocationName);
		tempApp.setLocationName2(LocationName2);
		tempApp.savePreferences(true);
		tempApp.loadPreferences();
		tempApp.setTVCH_SettigChecked(true);
		setAdapter.notifyDataSetChanged();
	}


	//    //    //    //    //    //    //    //    //    //    //    //    //    //    //    //    //    //    //    //    //    //    //    //    //

	class ServiceNoticeAsyncTask extends AsyncTask<String, Void, SERVICE_NOTICE_LIST>
	{

		private ProgressDialog pro;
		@Override
		protected SERVICE_NOTICE_LIST doInBackground(String... params) {
			

			SERVICE_NOTICE_PARSER paser = new SERVICE_NOTICE_PARSER(params[0]);
			Log.d("Setting_LocationInfo","## URL Request 공지사항 리로딩: "+params[0]);
			
			for (int i = 0; i < 2; i++) {
				if(paser.start()){
					return paser.getArrayList();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(SERVICE_NOTICE_LIST result) {
			
			tempApp.setmServiceNoticeList(result);
			
			super.onPostExecute(result);
			
			
		}
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}
	}


	private void Product_dalog() {
		// TODO Auto-generated method stub

		// 상품 정보 파서

		if (tempApp.GetNetworkInfo() != 2) {
			Log.d("Sangtae", "areaCode: "+ tempApp.getLocationID());
			product_PASER = new PRODUCT_INFO_PASER(XML_Address_Define.Channel.getGetchannelproduct(tempApp.getLocationID()));
			product_PASER.start();
			/*product_PASER.getProduct_INFO_LIST();
			String[] tempList = new String[product_PASER.getProduct_INFO_LIST().size()];
			for (int i = 0; i < product_PASER.getProduct_INFO_LIST().size(); i++) {
				tempList[i]=product_PASER.getProduct_INFO_LIST().get(i).getName();
			}*/
			tempProIndex =product_PASER.getProduct_INFO_LIST().getDefaultIndx();
			
			final ArrayAdapter arrayAdapter = setProductAdapter();	// 2013-06-28
			
			Builder puc_dialog = new AlertDialog.Builder(getParent()).setTitle(R.string.product_dlg_title)
			.setNeutralButton("확인", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Log.d("Sangtae", "loc_dialog onClick"+ which);
					tempApp.ButtonBeepPlay();
					tempApp.setProductID(product_PASER.getProduct_INFO_LIST().get(tempProIndex).getID());
					tempApp.setProductName(product_PASER.getProduct_INFO_LIST().get(tempProIndex).getName());
					tempApp.savePreferences(true);
										
					dialog.dismiss();

					if (tempApp.getLocationID() != "" && tempApp.getProductID() != "")
						new ServiceNoticeAsyncTask().execute(XML_Address_Define.Service.getGetserviceNoticeinfo(tempApp.getLocationID(), tempApp.getProductID()));//tempApp.getMobileIMIE(getApplicationContext()));
				}
			})
			//.setSingleChoiceItems(setProductAdapter(), tempProIndex, new DialogInterface.OnClickListener() {
			.setSingleChoiceItems(arrayAdapter, tempProIndex, new DialogInterface.OnClickListener() {	// 2013-06-28

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					/*tempApp.setLocationID(product_PASER.getProduct_INFO_LIST().get(which).getID());
				tempApp.setLocationName(product_PASER.getProduct_INFO_LIST().get(which).getName());*/
					tempProIndex=which;
					arrayAdapter.notifyDataSetChanged();	// 2013-06-28
				}
			});
			puc_dialog.create().show();
		} else {
			AlertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
		}

	}

	private CustomProAdapter setProductAdapter(){

		Vector<Custom> vector = new Vector<Custom>();
		//LOCATION_INFO_LIST info_LIST= location_PASER.getLocation_info();

		//product

		product_PASER = new PRODUCT_INFO_PASER(XML_Address_Define.Channel.getGetchannelproduct(tempApp.getLocationID()));
		product_PASER.start();
		product_PASER.getProduct_INFO_LIST();
		String[] tempList = new String[product_PASER.getProduct_INFO_LIST().size()];
		for (int i = 0; i < product_PASER.getProduct_INFO_LIST().size(); i++) {
			tempList[i]=product_PASER.getProduct_INFO_LIST().get(i).getName();
		}
		tempProIndex =product_PASER.getProduct_INFO_LIST().getDefaultIndx();

		for (int i = 0; i < tempList.length; i++) {
			//tempList[i]=location_PASER.getLocation_info().get(i).getName();
			vector.add(new Custom(tempList[i], null));
		}



		return new CustomProAdapter(getApplicationContext(), R.layout.product_row,vector, this);
	}

	public class Custom {
		private String Text1, Text2;
		public Custom(String _Text1, String _Text2) {
			this.Text1 = _Text1;
			this.Text2 = _Text2;
		}
		public Custom(String _Text1) {
			this.Text1 = _Text1;
		}
		public String getText1() {
			return Text1;
		}
		public String getText2() {
			return Text2;
		}
	}


	// Product
	public class CustomProAdapter extends ArrayAdapter<String> {
		private Vector items;
		private Activity activity;
		private int textViewResourceId;
		public CustomProAdapter(Context context, int textViewResourceId,Vector items, Activity activity) {
			super(context, textViewResourceId, items);
			this.items = items;
			this.activity = activity;
			this.textViewResourceId = textViewResourceId;

		}

		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			tempApp.ButtonBeepPlay();
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(textViewResourceId, null);
			}
			return searchRow(position, v) ;
		}
		public View searchRow(int position, View v){
			Custom p = (Custom) items.get(position);
			if (p != null) {
				TextView text1 = (TextView) v.findViewById(R.id.Pct_Name);
				text1.setText(p.getText1());
				RadioButton radioButton = (RadioButton)v.findViewById(R.id.Pud_RBtn);
				if (tempProIndex == position) {
					radioButton.setChecked(true);
				}else {
					radioButton.setChecked(false);
				}

			}
			return v;
		}
	}

	private void AlertShow(String msg) {
		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
				this);
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
