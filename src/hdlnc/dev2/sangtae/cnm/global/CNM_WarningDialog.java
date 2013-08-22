/**
 * 
 */
package hdlnc.dev2.sangtae.cnm.global;

import java.text.MessageFormat;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.text.Html;
import android.widget.TextView;




/**
 * @author Sangtae
 *
 */
public class CNM_WarningDialog extends AlertDialog{
	private final String NotRecord	= "채널 변경 불가";
	private final String Info	= "안내";
	
	public static final int 	LIVE_Record_Dupli 		= 1001;
	public static final String	LIVE_Record_Dupli_MSG 	= "<p>셋탑에서 다른 채널이 녹화중인 경우 원격으로는 즉시녹화가 불가합니다.</p>";
	
	public static final int 	LIVE_Record_PIP 		= 1002;
	public static final String	LIVE_Record_PIP_MSG 	= "<p>셋탑에서 동시화면 기능을 사용중인 경우 원격으로는 즉시녹화가 불가합니다.</p>";
	
	public static final int 	Record_OverStorag		= 1003;
	public static final String	Record_OverStorag_MSG 	= "<p>셋탑의 저장공간이 부족합니다.</p> <p>녹화물 목록을 확인해주세요.</p>";
	
	public static final int 	Record_SettopBoxPorwer	= 1004;
	public static final String	Record_SettopBoxPorwer_MSG 	= "<p>셋탑의 뒷 전원이 꺼져 있거나, 통신이 고르지 못해 예약이 불가합니다.</p> <p>셋탑 상태를 확인해 주세요.</p>";
	
	public static final int 	Record_NotService		= 1005;
	public static final String	Record_NotService_MSG 	= "<p>고객님의 셋탑에서 제공되지 않는 상품 또는 지역의 채널입니다. <p>[설정/안내]에서 확인해주세요.</p>";
	
	public static final int 	Record_UserLimit		= 1006;
	public static final String	Record_UserLimit_MSG 	= "<p>고객님의 셋탑 설정에 의한 시청제한으로 예약이 불가합니다.</p> <p>셋탑 설정을 확인해주세요.</p>";

	public static final int 	Limited_Area		= 2001;
	public static final String	Limited_Area_MSG 	= "<p>강남(서울시 강남구)과 울산(울주군, 중구, 북구, 남구, 동구) 지역에서는 [TV채널] 메뉴만 사용 할 수 있습니다.</p>";
	
	public static final String OK_BUTTON = "확  인";

	private OnClickListener dismisListener;
	
	public CNM_WarningDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		dismisListener = new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dismiss();
				
			}
		};
	}

	/**
	 * @return 
	 * 
	 */
	public void setWarningDialogType(int aType) {
		// TODO Auto-generated constructor stub
		
		// 녹화 불가
		setIcon(R.drawable.ic_dialog_info);
		setTitle(NotRecord);
		
		
		switch (aType) {
		case LIVE_Record_Dupli:
			setMessage(Html.fromHtml(LIVE_Record_Dupli_MSG));
			setButton(OK_BUTTON, dismisListener);
			break;
		case LIVE_Record_PIP:
			setMessage(Html.fromHtml(LIVE_Record_PIP_MSG));
			setButton(OK_BUTTON, dismisListener);
			break;
		case Record_OverStorag:
			setMessage(Html.fromHtml(Record_OverStorag_MSG));
			setButton(OK_BUTTON, dismisListener);
			break;
		case Record_SettopBoxPorwer:
			setMessage(Html.fromHtml(Record_SettopBoxPorwer_MSG));
			setButton(OK_BUTTON, dismisListener);
			break;
		case Record_NotService:
			setMessage(Html.fromHtml(Record_NotService_MSG));
			setButton(OK_BUTTON, dismisListener);
			break;
		case Record_UserLimit:
			setMessage(Html.fromHtml(Record_UserLimit_MSG));
			setButton(OK_BUTTON, dismisListener);
			break;
		case Limited_Area:
			setTitle(Info);
			setMessage(Html.fromHtml(Limited_Area_MSG));
			setButton(OK_BUTTON, dismisListener);
			break;

		default:
			break;
		}
		
		
	}
	
}
