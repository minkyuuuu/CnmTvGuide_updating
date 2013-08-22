package pvr;

import hdlnc.dev2.sangtae.cnm.R;
import android.app.Dialog;
import android.content.Context;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class PVR_TitleEditDialog extends Dialog {

	public interface OnEnterKeyListener 
	{
		void onEnterKeyPressed(String str);
	}
	
	private Context mContext;
	private final OnEnterKeyListener mListener;
	private String setHintString="";
	private EditText ed;
	public PVR_TitleEditDialog(Context context,int style, String hintString, OnEnterKeyListener callbackListener) {
		super(context, style);
		mContext = context;
		mListener = callbackListener;
		setHintString = hintString;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
				|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		
		setContentView(R.layout.pvr_titleediter);
		
		ed = (EditText)findViewById(R.id.searchUp_editText);
		Button btn = (Button)findViewById(R.id.searchUp_cancle_button);
		ed.setHint(setHintString);
		ed.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if((actionId == EditorInfo.IME_ACTION_DONE) ||
					(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) 
				{
					mListener.onEnterKeyPressed(v.getText().toString());
					dismiss();
				}
				
				return false;
			}
		});
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onEnterKeyPressed(ed.getText().toString());
				dismiss();
			}
		});
	}
	
	 
}
