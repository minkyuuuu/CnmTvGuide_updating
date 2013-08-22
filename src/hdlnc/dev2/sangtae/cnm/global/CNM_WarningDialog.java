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
	private final String NotRecord	= "ä�� ���� �Ұ�";
	private final String Info	= "�ȳ�";
	
	public static final int 	LIVE_Record_Dupli 		= 1001;
	public static final String	LIVE_Record_Dupli_MSG 	= "<p>��ž���� �ٸ� ä���� ��ȭ���� ��� �������δ� ��ó�ȭ�� �Ұ��մϴ�.</p>";
	
	public static final int 	LIVE_Record_PIP 		= 1002;
	public static final String	LIVE_Record_PIP_MSG 	= "<p>��ž���� ����ȭ�� ����� ������� ��� �������δ� ��ó�ȭ�� �Ұ��մϴ�.</p>";
	
	public static final int 	Record_OverStorag		= 1003;
	public static final String	Record_OverStorag_MSG 	= "<p>��ž�� ��������� �����մϴ�.</p> <p>��ȭ�� ����� Ȯ�����ּ���.</p>";
	
	public static final int 	Record_SettopBoxPorwer	= 1004;
	public static final String	Record_SettopBoxPorwer_MSG 	= "<p>��ž�� �� ������ ���� �ְų�, ����� ���� ���� ������ �Ұ��մϴ�.</p> <p>��ž ���¸� Ȯ���� �ּ���.</p>";
	
	public static final int 	Record_NotService		= 1005;
	public static final String	Record_NotService_MSG 	= "<p>������ ��ž���� �������� �ʴ� ��ǰ �Ǵ� ������ ä���Դϴ�. <p>[����/�ȳ�]���� Ȯ�����ּ���.</p>";
	
	public static final int 	Record_UserLimit		= 1006;
	public static final String	Record_UserLimit_MSG 	= "<p>������ ��ž ������ ���� ��û�������� ������ �Ұ��մϴ�.</p> <p>��ž ������ Ȯ�����ּ���.</p>";

	public static final int 	Limited_Area		= 2001;
	public static final String	Limited_Area_MSG 	= "<p>����(����� ������)�� ���(���ֱ�, �߱�, �ϱ�, ����, ����) ���������� [TVä��] �޴��� ��� �� �� �ֽ��ϴ�.</p>";
	
	public static final String OK_BUTTON = "Ȯ  ��";

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
		
		// ��ȭ �Ұ�
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
