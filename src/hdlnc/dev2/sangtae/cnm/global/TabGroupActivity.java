package hdlnc.dev2.sangtae.cnm.global;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

/** http://ericharlow.blogspot.com/search/label/Experience
 * The purpose of this Activity is to manage the activities in a tab.
 * Note: Child Activities can handle Key Presses before they are seen here.
 * @author Eric Harlow
 */
public class TabGroupActivity extends ActivityGroup {

	private ArrayList<String> mIdList;
	private CNMApplication tempApp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		tempApp = (CNMApplication)getApplication();
		if (mIdList == null) mIdList = new ArrayList<String>();
	}

	/**
	 * This is called when a child activity of this one calls its finish method. 
	 * This implementation calls {@link LocalActivityManager#destroyActivity} on the child activity
	 * and starts the previous activity.
	 * If the last child activity just called finish(),this activity (the parent),
	 * calls finish to finish the entire group.
	 */
	@Override
	public void finishFromChild(Activity child) {	
		LocalActivityManager manager = getLocalActivityManager();
		int index = mIdList.size()-1;

		if (index < 1) {
			finish();
			return;
		}
		
		try {

			manager.destroyActivity(mIdList.get(index), true);
			mIdList.remove(index); index--;
			String lastId = mIdList.get(index);
			Log.d("Sangtae", "TabGroupActivity Index: " + lastId);
			Intent lastIntent = manager.getActivity(lastId).getIntent();
			if (lastIntent != null ) {
				Window newWindow = manager.startActivity(lastId, lastIntent);
				setContentView(newWindow.getDecorView());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Starts an Activity as a child Activity to this.
	 * @param Id Unique identifier of the activity to be started.
	 * @param intent The Intent describing the activity to be started.
	 * @throws android.content.ActivityNotFoundException.
	 */
	public void startChildActivity(String Id, Intent intent) {     
		Window window = getLocalActivityManager().startActivity(Id,intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		if (window != null) {
			mIdList.add(Id);
			setContentView(window.getDecorView()); 
		}    
	}

	/**
	 * The primary purpose is to prevent systems before android.os.Build.VERSION_CODES.ECLAIR
	 * from calling their default KeyEvent.KEYCODE_BACK during onKeyDown.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Overrides the default implementation for KeyEvent.KEYCODE_BACK 
	 * so that all systems call onBackPressed().
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.d("Sangtae", "KEYCODE_BACK onKeyUp - TabGroupActivity");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//onBackPressed();
			tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().performClick();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * If a Child Activity handles KeyEvent.KEYCODE_BACK.
	 * Simply override and add this method.
	 */
	@Override
	public void  onBackPressed  () {
		Log.d("Sangtae", "onBackPressed - TabGroupActivity");
		int length = mIdList.size();
		if ( length > 1) {
			Activity current = getLocalActivityManager().getActivity(mIdList.get(length-1));
			current.finish();
		}  
	}

	public void  onHomeBackPressed  (int index) {

		for (int i = mIdList.size(); i > index+1; i--) {
			Activity current = getLocalActivityManager().getActivity(mIdList.get(i-1));
			current.finish();
		}
		Activity current = getLocalActivityManager().getActivity(mIdList.get(index));
		setContentView(current.getWindow().getDecorView());

	}
}

