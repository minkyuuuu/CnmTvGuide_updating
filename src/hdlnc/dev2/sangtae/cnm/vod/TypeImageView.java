package hdlnc.dev2.sangtae.cnm.vod;

/*
 * VOD 등급 아이콘을 사용하는 ImageView 
 */
import hdlnc.dev2.sangtae.cnm.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class TypeImageView extends ImageView {

	
	String Type="";
	
	public TypeImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public TypeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public TypeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
	public void setType(String type)
	{
		Type = type.trim();
		
		if(Type.equals("YES"))
			setVisibility(View.VISIBLE);
		else
			setVisibility(View.INVISIBLE);
		
	}
	
	
	@Override
	public void setImageResource(int resId) {
		
		Drawable d = getContext().getResources().getDrawable(resId);
		if(d instanceof BitmapDrawable)
			setImageDrawable(d);
		else
			super.setImageResource(resId);
	}



}
