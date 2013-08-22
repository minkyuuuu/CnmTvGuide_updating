package hdlnc.dev2.sangtae.cnm.joyn;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;

import android.content.Intent;
import android.os.Bundle;

public class JoyNGroup extends TabGroupActivity {
	private CNMApplication app;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 startChildActivity("JoyN", new Intent(this,JoyNTabActivity.class));
	}
}