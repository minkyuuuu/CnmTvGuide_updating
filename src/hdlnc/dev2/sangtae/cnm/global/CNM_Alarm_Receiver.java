package hdlnc.dev2.sangtae.cnm.global;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class CNM_Alarm_Receiver extends BroadcastReceiver {
	  @Override
	  public void onReceive(Context context, Intent intent) {    
	    if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
	      Log.i("MyCalendar", "Intent received");    

	      String s = CNM_Alarm_Service.class.getName();
	      String str = context.getPackageName();
	      
	      
	      //MyCalendarService 서비스를 생성
	      ComponentName cn = new ComponentName(context.getPackageName(), CNM_Alarm_Service.class.getName());
	      //MyCalendarService 하고 서비스를 시작한다.
	      ComponentName svcName = context.startService(new Intent().setComponent(cn));
	      if (svcName == null) 
	        Log.e("MyCalendar", "Could not start service " + cn.toString());
	    }
	  }
}