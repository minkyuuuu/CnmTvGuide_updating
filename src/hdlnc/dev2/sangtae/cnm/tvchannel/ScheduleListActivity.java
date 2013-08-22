package hdlnc.dev2.sangtae.cnm.tvchannel;

import hdlnc.dev2.sangtae.cnm.global.DATA.SCUEDULE_DATA_LIST;
import hdlnc.dev2.sangtae.cnm.global.DATA.SCUEDULE_PASER;
import hdlnc.dev2.sangtae.cnm.tvchannel.TVCH_Schedule.ViewHolder;


import hdlnc.dev2.sangtae.cnm.R;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ScheduleListActivity extends ListActivity
{
	private WorkspaceView work;
	
	
	/*private String lv_arr[] = { "Android", "iPhone", "BlackBerry", "AndroidPeople", "Symbian", "iPad","Windows Mobile", "Sony","HTC","Motorola" };
	private String lv_arr2[] = { "Eric Taix", "eric.taix@gmail.com" };*/
	private SCUEDULE_PASER paser;
	private SCUEDULE_DATA_LIST mList; 
	  
    public void onCreate(Bundle savedInstanceState) 
    {
    	// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);        
		setContentView(R.layout.tvch_schedule_list);
		
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
		work = new WorkspaceView(this, null);
		
		work.setTouchSlop(32);
		//Bitmap backGd = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		//work.loadWallpaper(backGd);
		
		/*ListView lv1 = (ListView) inflater.inflate(R.layout.list, null, false);
	    lv1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lv_arr));
	    ListView lv2 = (ListView) inflater.inflate(R.layout.list, null, false);
	    lv2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lv_arr2));
	  
	    
	   
	    work.addView(lv1);
	    work.addView(lv2);
	    */
	  
		//LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.tvch_schedule_list, null);
		/*ListView lv1 = (ListView)findViewById(R.id.tvch_schedule_listview);
		TVCH_Schedule_Adapter adapter =new TVCH_Schedule_Adapter(this, mList);
	    lv1.setAdapter(adapter);
	    TextView tv1 = (TextView)findViewById(R.id.BroadcastDay);
	    tv1.setText("231345642");*/
	    
	    
	    //work.addView(v1);

		setContentView(work);        
        
		
    }
    
    
    /*public class TVCH_Schedule_Adapter extends BaseAdapter{
		private LayoutInflater mInflater;
		//private Context context;
		private SCUEDULE_DATA_LIST items;

		public TVCH_Schedule_Adapter( Context context, SCUEDULE_DATA_LIST items) { //int textViewResourceId, 
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(context);
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

		 (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//CurrentDB = tempApp.getCurrentTVCH();
			final ViewHolder holder;
			final int tempPosition = position;
			final View tempConvertView = convertView;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.tvch_schedulel_row, null);
				//Log.d("Sangtae", "convertView Null");
				holder = new ViewHolder();
				//holder.text = (TextView) convertView.findViewById(R.id.TextView01);
				// 아이콘
				holder.Mych_Icon	= (ImageView)convertView.findViewById(R.id.MyCh_Icon);
				//------메인-------
				holder.Pro_time		= (TextView)convertView.findViewById(R.id.tvch_schedule_ProgramTime);
				holder.Pro_title	= (TextView) convertView.findViewById(R.id.tvch_schedule_ProgramTitle);

				holder.icon_hd		= (ImageView)convertView.findViewById(R.id.tvch_schedule_HD);
				holder.icon_alarm	= (ImageView)convertView.findViewById(R.id.tvch_schedule_Alarm);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				//Log.d("Sangtae", "convertView Not Null");
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			holder.Pro_time.setText(items.get(position).getTime());
			holder.Pro_title.setText(items.get(position).getTitle());
			
			
			
			return convertView;
		}

		//리스트 삭제 변경 

	}




	static class ViewHolder {
		//-----------------
		ImageView	Mych_Icon;
		//-----------------
		TextView	Pro_time;
		TextView	Pro_title;
		ImageView	icon_hd;
		ImageView	icon_alarm;
	}*/
	
    
}