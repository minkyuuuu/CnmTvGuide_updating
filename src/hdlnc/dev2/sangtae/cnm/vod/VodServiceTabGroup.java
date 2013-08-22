package hdlnc.dev2.sangtae.cnm.vod;

import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class VodServiceTabGroup extends TabGroupActivity {
	
	public static int ResultDepth=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 startChildActivity("VODServiceActivity", new Intent(this, VOD_Service.class));
	}
	
}
