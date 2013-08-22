package hdlnc.dev2.sangtae.cnm.main;

import hdlnc.dev2.sangtae.cnm.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_loding);
		
		
		initiallze();
	}


	private void initiallze() {

		Handler handelr = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				//super.handleMessage(msg);
				setEndSplash();
				finish();
			}
		};

		handelr.sendEmptyMessageDelayed(0, 3000);

	}

	
	private void setEndSplash() {
		// TODO Auto-generated method stub
		this.setResult(RESULT_OK);
	}	


}
