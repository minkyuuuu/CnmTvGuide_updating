/**
 * 
 */
package hdlnc.dev2.sangtae.cnm.settings;

import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.tvchannel.TV_Channel;


import android.content.Intent;
import android.os.Bundle;

public class SettingGroup extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 startChildActivity("SettingTab", new Intent(this, SettingTabActivity.class));
	}
}
