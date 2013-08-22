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
import android.widget.ImageView;

public class GradeImageView extends ImageView {

	int[] gradeIconResources= 
	{
		R.drawable.icon_class_all,
		R.drawable.icon_class_7,
		R.drawable.icon_class_12,
		R.drawable.icon_class_15,
		R.drawable.icon_class_19
	};
	
	String Grade="";
	
	public GradeImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public GradeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public GradeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
	public void setGrade(String grade)
	{
		Grade = grade.trim();

		Resources res = getResources();
		if(Grade.equals(getResources().getString(R.string.vod_grade_7)))
			setBackgroundDrawable(res.getDrawable(gradeIconResources[1]));
		else if(Grade.equals(getResources().getString(R.string.vod_grade_12)))
			setBackgroundDrawable(res.getDrawable(gradeIconResources[2]));
		else if(Grade.equals(getResources().getString(R.string.vod_grade_15)))
			setBackgroundDrawable(res.getDrawable(gradeIconResources[3]));
		else if(Grade.equals(getResources().getString(R.string.vod_grade_19)))
			setBackgroundDrawable(res.getDrawable(gradeIconResources[4]));
		else 
			setBackgroundDrawable(res.getDrawable(gradeIconResources[0]));
		
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
