package hdlnc.dev2.sangtae.cnm.global;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.ToggleButton;
import android.widget.TextView;

public class NaviBtn_Singleton {

	private	 ImageButton				NaviLeftBtn;
	private	 ToggleButton				NaviLeftSubBtn;
	private  ImageButton				NaviRightBtn;
	private	 ImageButton				NaviRightSubBtn;
	private  TextView                   NaviHeaderText;

	private	static NaviBtn_Singleton instance;
	private NaviBtn_Singleton(){
	}

	private NaviBtn_Singleton(ImageButton LeftBtn, ToggleButton LeftSubBtn, ImageButton RightBtn, ImageButton RightSubBtn, TextView HeaderText){
		if (instance == null) {
			instance	= new NaviBtn_Singleton();
		}
		instance.NaviLeftBtn	= LeftBtn;
		instance.NaviLeftSubBtn	= LeftSubBtn;
		instance.NaviRightBtn	= RightBtn;
		instance.NaviRightSubBtn= RightSubBtn;
		instance.NaviHeaderText = HeaderText;

	}

	public static void setInstance(ImageButton LeftBtn, ToggleButton LeftSubBtn, ImageButton RightBtn, ImageButton RightSubBtn, TextView HeaderText) {
		if (instance == null) {
			instance	= new NaviBtn_Singleton(LeftBtn, LeftSubBtn, RightBtn, RightSubBtn, HeaderText);
			//Log.d("Sangtae", "setInstance(ImageButton LeftBtn, ImageButton LeftSubBtn, ImageButton RightBtn, ImageButton RightSubBtn)");
		}
		instance.NaviLeftBtn	= LeftBtn;
		instance.NaviLeftSubBtn	= LeftSubBtn;
		instance.NaviRightBtn	= RightBtn;
		instance.NaviRightSubBtn= RightSubBtn;
		instance.NaviHeaderText = HeaderText;
	}

	public static NaviBtn_Singleton getInstance() {
		if (instance == null) {
			instance	= new NaviBtn_Singleton();
			Log.d("Sangtae", "null instance");
		}
		return instance;
	}

	
	public ImageButton getNaviLeftBtn() {
		return NaviLeftBtn;
	}
	
	public ToggleButton getNaviLeftSubBtn() {
		return NaviLeftSubBtn;
	}
	public ImageButton getNaviRightBtn() {
		return NaviRightBtn;
	}
	public ImageButton getNaviRightSubBtn() {
		return NaviRightSubBtn;
	}
	public TextView getNaviHeaderText() {
		return NaviHeaderText;
	}



}
