package hdlnc.dev2.sangtae.cnm.tvchannel;

import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;

import android.content.Intent;
import android.os.Bundle;

public class TvChannelTabGroup extends TabGroupActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 startChildActivity("TVCannelActivity", new Intent(this,TV_Channel.class));
	}
	
}
