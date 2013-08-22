package hdlnc.dev2.sangtae.cnm.tvchannel;

import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;

import hdlnc.dev2.sangtae.cnm.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TVCH_Genre extends Activity {
	public static String TVCH_GENRE_CODE = "TVCH_GENRE_CODE";
	private CNMApplication tempApp;

	private Button Button01;
	private Button Button02;
	private Button Button03;
	private Button Button04;
	private Button Button05;
	private Button Button06;
	private Button Button07;
	private Button Button08;
	private Button Button09;
	private Button Button10;
	private Button Button11;
	private Button Button12;

	private Boolean mMode	= true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tvch_genre);
		tempApp = (CNMApplication)getApplication();						// 공용 데이터 접근용
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (tempApp.getLocationID().equals(XML_Address_Define.Channel.LIMITED_AREA_CODE.KANGNAM)
				&& tempApp.getLocationID().equals(XML_Address_Define.Channel.LIMITED_AREA_CODE.UlSAN)) {
			mMode = false;
		}else{
			mMode = true;
		}
		SetupUI();
		ChargeButton(mMode);
		/*cnmAPP.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cnmAPP.ButtonBeepPlay();
				onBackPressed();
				cnmAPP.setChannelTabID(R.id.TVCH_AllCH_TABBtn);
			}
		});*/
	}

	private void SetupUI() {
		// TODO Auto-generated method stub
		OnClickListener clickListener =	 new OnClickListener() {

			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			String TempSubject;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent previewMessage = new Intent(getParent(), TVCH_Genre_Activity.class);
				TabGroupActivity parentActivity = (TabGroupActivity)getParent().getParent();
				((CNMApplication)getApplication()).ButtonBeepPlay();
				if (mMode) {
					switch (v.getId()) {
					case R.id.TVCH_GenreBtn01:
						TempSubject = "지상파/지역";
						previewMessage.putExtra(TVCH_GENRE_CODE, 1);
						break;
					case R.id.TVCH_GenreBtn02:
						TempSubject = "영화";
						previewMessage.putExtra(TVCH_GENRE_CODE, 7);
						break;
					case R.id.TVCH_GenreBtn03:
						TempSubject = "드라마/여성";
						previewMessage.putExtra(TVCH_GENRE_CODE, 8);
						break;
					case R.id.TVCH_GenreBtn04:
						TempSubject = "음악/오락";
						previewMessage.putExtra(TVCH_GENRE_CODE, 3);
						break;
					case R.id.TVCH_GenreBtn05:
						TempSubject = "스포츠/게임";
						previewMessage.putExtra(TVCH_GENRE_CODE, 4);
						break;
					case R.id.TVCH_GenreBtn06:
						TempSubject = "뉴스/다큐";
						previewMessage.putExtra(TVCH_GENRE_CODE, 6);
						break;
					case R.id.TVCH_GenreBtn07:
						TempSubject = "교육/키즈";
						previewMessage.putExtra(TVCH_GENRE_CODE, 2);
						break;
					case R.id.TVCH_GenreBtn08:
						TempSubject = "종교/기타";
						previewMessage.putExtra(TVCH_GENRE_CODE, 5);
						break;
					case R.id.TVCH_GenreBtn09:
						TempSubject = "홈쇼핑";
						previewMessage.putExtra(TVCH_GENRE_CODE, 9);
						break;
					default:
						break;
					}
				} else {
					switch (v.getId()) {
					case R.id.TVCH_GenreBtn01:
						previewMessage.putExtra(TVCH_GENRE_CODE, 1);
						break;
					case R.id.TVCH_GenreBtn02:
						previewMessage.putExtra(TVCH_GENRE_CODE, 7);
						break;
					case R.id.TVCH_GenreBtn03:
						previewMessage.putExtra(TVCH_GENRE_CODE, 8);
						break;
					case R.id.TVCH_GenreBtn04:
						previewMessage.putExtra(TVCH_GENRE_CODE, 3);
						break;
					case R.id.TVCH_GenreBtn05:
						previewMessage.putExtra(TVCH_GENRE_CODE, 4);
						break;
					case R.id.TVCH_GenreBtn06:
						previewMessage.putExtra(TVCH_GENRE_CODE, 6);
						break;
					case R.id.TVCH_GenreBtn07:
						previewMessage.putExtra(TVCH_GENRE_CODE, 2);
						break;
					case R.id.TVCH_GenreBtn08:
						previewMessage.putExtra(TVCH_GENRE_CODE, 5);
						break;
					case R.id.TVCH_GenreBtn09:
						previewMessage.putExtra(TVCH_GENRE_CODE, 9);
						break;
					case R.id.TVCH_GenreBtn10:
						previewMessage.putExtra(TVCH_GENRE_CODE, 9);
						break;
					case R.id.TVCH_GenreBtn11:
						previewMessage.putExtra(TVCH_GENRE_CODE, 9);
						break;
					case R.id.TVCH_GenreBtn12:
						previewMessage.putExtra(TVCH_GENRE_CODE, 9);
						break;
					default:
						break;
					}
				}
				previewMessage.putExtra("TVCH_Genre_NaviName", TempSubject);
				parentActivity.startChildActivity("TVCH_Genre_Activity", previewMessage);

			}
		};	
		Button01 = (Button)findViewById(R.id.TVCH_GenreBtn01);
		Button02 = (Button)findViewById(R.id.TVCH_GenreBtn02);
		Button03 = (Button)findViewById(R.id.TVCH_GenreBtn03);
		Button04 = (Button)findViewById(R.id.TVCH_GenreBtn04);
		Button05 = (Button)findViewById(R.id.TVCH_GenreBtn05);
		Button06 = (Button)findViewById(R.id.TVCH_GenreBtn06);
		Button07 = (Button)findViewById(R.id.TVCH_GenreBtn07);
		Button08 = (Button)findViewById(R.id.TVCH_GenreBtn08);
		Button09 = (Button)findViewById(R.id.TVCH_GenreBtn09);
		Button10 = (Button)findViewById(R.id.TVCH_GenreBtn10);
		Button11 = (Button)findViewById(R.id.TVCH_GenreBtn11);
		Button12 = (Button)findViewById(R.id.TVCH_GenreBtn12);

		Button01.setOnClickListener(clickListener);
		Button02.setOnClickListener(clickListener);
		Button03.setOnClickListener(clickListener);
		Button04.setOnClickListener(clickListener);
		Button05.setOnClickListener(clickListener);
		Button06.setOnClickListener(clickListener);
		Button07.setOnClickListener(clickListener);
		Button08.setOnClickListener(clickListener);
		Button09.setOnClickListener(clickListener);
		Button10.setOnClickListener(clickListener);
		Button11.setOnClickListener(clickListener);
		Button12.setOnClickListener(clickListener);


	}

	private void ChargeButton(Boolean aMode){
		if (aMode) {
			Button01.setBackgroundResource(R.drawable.genre_button_ground);
			Button02.setBackgroundResource(R.drawable.genrega_button_movie);
			Button03.setBackgroundResource(R.drawable.genre_button_drama);
			Button04.setBackgroundResource(R.drawable.genre_button_music);
			Button05.setBackgroundResource(R.drawable.genre_button_sport);
			Button06.setBackgroundResource(R.drawable.genrega_button_new);
			Button07.setBackgroundResource(R.drawable.genre_button_child);
			Button08.setBackgroundResource(R.drawable.genre_button_religion);
			Button09.setBackgroundResource(R.drawable.genre_button_homeshop);
			Button10.setVisibility(View.GONE);
			Button11.setVisibility(View.GONE);
			Button12.setVisibility(View.GONE);
		} else {
			Button01.setBackgroundResource(R.drawable.genrega_button_ground);
			Button02.setBackgroundResource(R.drawable.genrega_button_hd);
			Button03.setBackgroundResource(R.drawable.genrega_button_entert);
			Button04.setBackgroundResource(R.drawable.genrega_button_new);
			Button05.setBackgroundResource(R.drawable.genrega_button_movie);
			Button06.setBackgroundResource(R.drawable.genrega_button_woman);
			Button07.setBackgroundResource(R.drawable.genrega_button_drama);
			Button08.setBackgroundResource(R.drawable.genrega_button_child);
			Button09.setBackgroundResource(R.drawable.genrega_button_sport);
			Button10.setVisibility(View.VISIBLE);
			Button11.setVisibility(View.VISIBLE);
			Button12.setVisibility(View.VISIBLE);
			Button10.setBackgroundResource(R.drawable.genrega_button_hobby);
			Button11.setBackgroundResource(R.drawable.genrega_button_religion);
			Button12.setBackgroundResource(R.drawable.genrega_button_info);


		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftSubBtn().setChecked(false);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		tempApp.getNaviBtn_Singleton().getInstance().getNaviLeftBtn().performClick();
	}
}
