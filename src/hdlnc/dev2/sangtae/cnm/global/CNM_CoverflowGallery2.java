package hdlnc.dev2.sangtae.cnm.global;


import android.R;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class CNM_CoverflowGallery2 extends Gallery {
	
	 public CNM_CoverflowGallery2(Context context) {
	  super(context);
	  
	  if(!isInEditMode())
		  this.setStaticTransformationsEnabled(true);
	 }
	
	 public CNM_CoverflowGallery2(Context context, AttributeSet attrs) {
	  super(context, attrs);
	  
	  if(!isInEditMode())
	        this.setStaticTransformationsEnabled(true);
	 }
	 
	  public CNM_CoverflowGallery2(Context context, AttributeSet attrs, int defStyle) {
	   super(context, attrs, defStyle);
	   
	   if(!isInEditMode())
		   this.setStaticTransformationsEnabled(true);   
	  }


	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		
    		/*
		int position = getSelectedItemPosition();
		
		int KeyCode=0;
		
		if(e1.getX() > e2.getX())
			KeyCode = KeyEvent.KEYCODE_DPAD_LEFT;
		else
			KeyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
		
		if(KeyCode!=0)
			onKeyDown(KeyCode, null);
		
		*/
    		
		velocityX *= 0.15;
		
		//return super.onFling(e1, e2, velocityX, velocityY);
		return false;
	}

//	@Override
//	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//			float distanceY) {
//		// TODO Auto-generated method stub
//		return super.onScroll(e1, e2, distanceX, distanceY);
//	}
	
	

}
