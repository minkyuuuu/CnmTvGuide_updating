package hdlnc.dev2.sangtae.cnm.tvchannel;

import hdlnc.dev2.sangtae.cnm.R;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

public class slideView  extends HorizontalScrollView  {
	private static final int SWIPE_MIN_DISTANCE = 200;
	private static final int SWIPE_THRESHOLD_VELOCITY = 1;

	private ArrayList<String> mItems = null;
	private GestureDetector mGestureDetector;
	private int mActiveFeature = 0;
	public int getmActiveFeature() {

		if (this == null) {
			return 0;
		}else {
			try {

				int iW =this.getWidth();
				int sW =this.getScrollX();
				mActiveFeature =	sW / iW;
				return mActiveFeature;
			} catch (Exception e) {
				// TODO: handle exception
				return 0;
			}
		}

	}

	private int childWidth = 0;
	private LinearLayout mTempLayout;
	int m_lastScroll = 0;
	private OnClickListener mSlideLeftListener;
	private OnClickListener mSlideRightListener;
	boolean m_flag=false;
	//public HorizontalScrollView m_horHorizontalScrollView;
	int m_BGsize = 0;
	int index=0;
	float StartX=0;
	int MoveCount=0;
	private VelocityTracker mVelocityTracker;
	private CNMApplication tempApp;

	public slideView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public slideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		/*mItems = new ArrayList();
		mItems.add("1");
		mItems.add("2");
		mItems.add("3");
		mItems.add("4");
		mItems.add("5");
		mItems.add("6");*/
		//get screen size
		tempApp = (CNMApplication)context.getApplicationContext();
		mGestureDetector = new GestureDetector(new MyGestureDetector());
		mSlideLeftListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				mActiveFeature--;
				smoothScrollTo(mActiveFeature*childWidth, 0);
			}
		};

		mSlideRightListener =  new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tempApp.ButtonBeepPlay();
				mActiveFeature++;
				smoothScrollTo(mActiveFeature*childWidth, 0);
			}
		};


		Display display = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		//mScreenWidth = display.getWidth();       

		//set sub layout
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mTempLayout = (LinearLayout)inflater.inflate(R.layout.slide_temp_layout, null);       
		// ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, 100);

		//setFeatureItems(mItems);
		//setFeatureItems(mItems);
	}

	public void setPostion(int aPostion){

		smoothScrollTo(aPostion*childWidth, 0);
	}

	public void setItem(View child) {
		// TODO Auto-generated method stub
		mItems = new ArrayList();
		ImageButton Rightbutton =(ImageButton)child.findViewById(R.id.SlideRightBtn);
		ImageButton Leftbutton =(ImageButton)child.findViewById(R.id.SlideLeftBtn);

		if (index == 0) {
			Rightbutton.setVisibility(View.GONE);
			Leftbutton.setVisibility(View.GONE);
			mItems.add("0");
		} else {
			for (Integer i = 0; i < index; i++) {
				mItems.add(i.toString());
			}
			Rightbutton.setVisibility(View.GONE);
		}

		Leftbutton.setOnClickListener(mSlideLeftListener);

		mTempLayout.addView(child);

		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(200, LayoutParams.FILL_PARENT);
		addView(mTempLayout, params);
		setFeatureItems(mItems);

	}

	public void addView(View child) {
		// TODO Auto-generated method stub
		ImageButton Rightbutton =(ImageButton)child.findViewById(R.id.SlideRightBtn);
		ImageButton Leftbutton =(ImageButton)child.findViewById(R.id.SlideLeftBtn);

		if (index == 0) {
			childWidth = this.getWidth();
			Leftbutton.setVisibility(View.GONE);
		}

		Leftbutton.setOnClickListener(mSlideLeftListener);
		Rightbutton.setOnClickListener(mSlideRightListener);

		mTempLayout.addView(child);

		index++;
	}

	public slideView(Context context) {
		super(context);
	}


	public void setFeatureItems(ArrayList<String> items) {/*
		LinearLayout internalWrapper = new LinearLayout(getContext());
		internalWrapper.setLayoutParams(new LayoutParams(
				640, LayoutParams.FILL_PARENT));
		internalWrapper.setOrientation(LinearLayout.HORIZONTAL);
		addView(internalWrapper);
		this.mItems = items;
		for (int i = 0; i < items.size(); i++) {
			LinearLayout featureLayout = (LinearLayout) View.inflate(
					this.getContext(), R.layout.menu_layout, null);
			// ...
			// Create the view for each screen in the scroll view
			// ...
			internalWrapper.addView(featureLayout);
		}*/

		//int x=internalWrapper.getWidth();
		//Log.d("HONG","x = "+x);





		setOnTouchListener(new View.OnTouchListener() {
			/*
			 * 터치 다운 현좌 좌표 저장 (시작 지점) 이동 여부 초기화
			 * 터치 이동 bool 이동 여부 확인 
			 * 터치 업 이동 여부 확인 후 좌우 스크롤 확인
			 * 좌우측 관련 이동 스크롤 계산 및 이동 에니메이션 적용
			 * */
			private VelocityTracker mVelocityTracker;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// If the user swipes
				/*if (mGestureDetector.onTouchEvent(event)) {
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					Log.d("HONG", "ACTION_UP");

					int scrollX = getScrollX();
					int featureWidth = v.getMeasuredWidth();
					Log.d("HONG", "v.getMeasuredWidth(): "+v.getMeasuredWidth());
					mActiveFeature = ((scrollX + (featureWidth / 2)) / featureWidth);
					Log.d("HONG", "scrollX: "+scrollX+" featureWidth: "+featureWidth+" mActiveFeature: "+mActiveFeature);

					Log.d("HONG", "mActiveFeature =" + mActiveFeature);
					// mActiveFeature는 현재 몇번째 페이지인가..첫번째는 0
					int scrollTo = mActiveFeature * featureWidth;


					Log.d("HONG","scrollTo = "+scrollTo);
					smoothScrollTo(scrollTo, 0);

					return true;
				} else {
					if (event.getAction() == MotionEvent.ACTION_MOVE) {

						int scrollflag = m_lastScroll - (int)event.getX();

						int scrollX = getScrollX();
						Log.e("HONG","Move - scrollX() = "+scrollX);
						Log.d("HONG","getMaxScrollAmount() = "+getMaxScrollAmount());
						m_lastScroll = (int)event.getX();

						Log.d("HONG","(int)event.getX() = "+(int)event.getX());
					}
					return false;
				}}
				 */
				if (mVelocityTracker == null) {
					mVelocityTracker = VelocityTracker.obtain();
				}
				mVelocityTracker.addMovement(event);
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:		

					if (! customFling(StartX, event.getX(), mVelocityTracker.getXVelocity(), mVelocityTracker.getYVelocity())) {

						int scrollX = getScrollX();
						int featureWidth = v.getMeasuredWidth();
						Log.d("HONG", "v.getMeasuredWidth(): "+v.getMeasuredWidth());
						mActiveFeature = ((scrollX + (featureWidth / 2)) / featureWidth);
						Log.d("HONG", "scrollX: "+scrollX+" featureWidth: "+featureWidth+" mActiveFeature: "+mActiveFeature);
						Log.d("HONG", "mActiveFeature =" + mActiveFeature);
						// mActiveFeature는 현재 몇번째 페이지인가..첫번째는 0
						int scrollTo = mActiveFeature * featureWidth;


						Log.d("HONG","scrollTo = "+scrollTo);
						smoothScrollTo(scrollTo, 0);
					}

					StartX=0;
					if (mVelocityTracker != null) {
						mVelocityTracker.recycle();
						mVelocityTracker = null;
					}
					return true;
				case MotionEvent.ACTION_MOVE:

					mVelocityTracker.computeCurrentVelocity(1);

					return false;
				default:
					return false;
				}
				return true;
			}
		});

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			StartX = ev.getX();
			//Log.e("HONG", "X: "+ev.getX()+" , Y: "+ev.getY());
			break;
		case MotionEvent.ACTION_UP:
			//Log.e("HONG", "X: "+ev.getX()+" , Y: "+ev.getY());
			break;
		case MotionEvent.ACTION_MOVE:
			//Log.e("HONG", "X: "+ev.getX()+" , Y: "+ev.getY());
			break;
		case MotionEvent.ACTION_CANCEL:
			//Log.e("HONG", "X: "+ev.getX()+" , Y: "+ev.getY());
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	int m_x = 0;
	int m_lastImageX=0;


	public boolean customFling(float StartX, float EndX, float velocityX,
			float velocityY) {

		try {
			// right to left
			if (StartX > EndX && (StartX - EndX) > SWIPE_MIN_DISTANCE
			&& (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY || velocityX == 0 )) {


				//return false;
				Log.d("HONG", "OldActiveFeature =" + mActiveFeature);
				int featureWidth = getMeasuredWidth();
				if (mActiveFeature != mItems.size()) {
					mActiveFeature = (mActiveFeature < (mItems.size())) ? mActiveFeature + 1: mItems.size() - 1;
				}
				Log.v("HONG", "right to left");
				Log.d("HONG", "featureWidth =" + featureWidth);
				Log.d("HONG", "mActiveFeature =" + mActiveFeature);
				Log.d("HONG", "mItems.size() =" + mItems.size());
				Log.d("HONG", "mActiveFeature * featureWidth =" + mActiveFeature * featureWidth);
				Log.d("HONG", "(StartX - EndX) =" + (StartX - EndX));
				Log.d("HONG", "velocityX =" + velocityX);
				smoothScrollTo(mActiveFeature * featureWidth, 0);
				//m_horHorizontalScrollView.smoothScrollTo(mActiveFeature*64, 0);
				//m_imageView.scrollTo(106, 0);


				return true;
			}
			// left to right
			else if (EndX > StartX && (EndX - StartX) > SWIPE_MIN_DISTANCE
			&& (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY || velocityX == 0 )) {
				Log.d("HONG", "OldActiveFeature =" + mActiveFeature);
				int featureWidth = getMeasuredWidth();
				mActiveFeature = (mActiveFeature > 0) ? mActiveFeature - 1: 0;
				/*
				 * if(mActiveFeature==0) return true;
				 */
				Log.v("HONG", "left to right");
				Log.d("HONG", "featureWidth =" + featureWidth);
				Log.d("HONG", "mActiveFeature =" + mActiveFeature);
				Log.d("HONG", "mItems.size() =" + mItems.size());
				Log.d("HONG", "mActiveFeature * featureWidth =" + mActiveFeature * featureWidth);
				Log.d("HONG", "(EndX - StartX) =" + (EndX - StartX));
				Log.d("HONG", "velocityX =" + velocityX);
				smoothScrollTo(mActiveFeature * featureWidth, 0);
				//m_horHorizontalScrollView.smoothScrollTo(mActiveFeature*64, 0);
				//m_imageView.scrollTo(106, 0);
				//m_imageView.invalidate();

				return true;
			}
		} catch (Exception e) {
			Log.e("Fling", "There was an error processing the Fling event:"
					+ e.getMessage());
		}
		return false;
	}

	class MyGestureDetector extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				// right to left
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {


					//return false;

					int featureWidth = getMeasuredWidth();
					mActiveFeature = (mActiveFeature < (mItems.size() - 1)) ? mActiveFeature + 1
							: mItems.size() - 1;
					Log.d("HONG", "right to left");
					Log.d("HONG", "mActiveFeature =" + mActiveFeature);
					smoothScrollTo(mActiveFeature * featureWidth, 0);
					//m_horHorizontalScrollView.smoothScrollTo(mActiveFeature*64, 0);
					//m_imageView.scrollTo(106, 0);


					return true;
				}
				// left to right
				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					int featureWidth = getMeasuredWidth();
					mActiveFeature = (mActiveFeature > 0) ? mActiveFeature - 1
							: 0;
					/*
					 * if(mActiveFeature==0) return true;
					 */
					Log.d("HONG", "left to right");
					Log.d("HONG", "mActiveFeature =" + mActiveFeature);
					Log.d("HONG", "featureWidth =" + featureWidth);
					smoothScrollTo(mActiveFeature * featureWidth, 0);
					//m_horHorizontalScrollView.smoothScrollTo(mActiveFeature*64, 0);
					//m_imageView.scrollTo(106, 0);
					//m_imageView.invalidate();

					return true;
				}
			} catch (Exception e) {
				Log.e("Fling", "There was an error processing the Fling event:"
						+ e.getMessage());
			}
			return false;
		}
	}
}
