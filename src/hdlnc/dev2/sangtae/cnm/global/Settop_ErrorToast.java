package hdlnc.dev2.sangtae.cnm.global;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class Settop_ErrorToast extends Toast {

	public Settop_ErrorToast(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setGravity(Gravity.CENTER, 0, 0);
	}
	
	private void setMessage(String Code) {
		// TODO Auto-generated method stub
		
		String TempText = null;
		
		// ���� ����
		if (Code.contains(XML_Address_Define.ErrorCode.ERROR_200)) {
			TempText = "�˼����� ����(General Error)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.ERROR_201)) {
			TempText = "�������� �ʴ� ��������(Unsupported Protocol)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.ERROR_202)) {
			TempText = "��������(Authentication Failure)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.ERROR_203)) {
			TempText = "�������� �ʴ� ��������(Unsupported Profile)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.ERROR_204)) {
			TempText = "�߸��� �Ķ���Ͱ�(Invalid Parameter)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.ERROR_205)) {
			TempText = "�ش������ ����(Not Found)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.ERROR_206)) {
			TempText = "���μ�������(Internal Server Error)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.ERROR_207)) {
			TempText = "�������μ��� ����(Internal Processing Error)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.ERROR_211)) {
			TempText = "�Ϲ� DB����(DB General Error)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.ERROR_221)) {
			TempText = "�̹�ó���Ǿ���(Already Done)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.ERROR_223)) {
			TempText = "�̹��߰��� �׸�(Duplicated Insertion)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.ERROR_231)) {
			TempText = "�����ڵ�߱޽���(AuthCode Issue Failure)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.ERROR_232)) {
			TempText = "����������ڵ�(AuthCode Expired)";
		}
		
		// ��ȭ ��û
		if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_001)) {
			TempText = "Mac����ġ(Invalid MacAddress)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_002)) {
			TempText = "�ߺ� ��ȭ �����û (Duplicated Recording Reserve Request)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_003)) {
			TempText = "��ũ �뷮����(Disk Capacity Not Enough)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_004)) {
			TempText = "Ʃ�ʸ� ��λ���ϰ� �־� ��ȭ/Ʃ�׺Ұ� (Authentication Failure)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_005)) {
			TempText = "��ȭ �Ұ� ä��(Unsupported Recording Channel)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_006)) {
			TempText = "�̹� ��ȭ �����(Already Recording Reserve Done)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_007)) {
			TempText = "���α׷� ������ ����(Program Not Found)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_008)) {
			TempText = "������� ��ȭ�� �����Ұ�( Recording Delete Error)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_009)) {
			TempText = "ä�� ����(Channel Not Found)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_010)) {
			TempText = "PIP�����(Using PIP Service)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_011)) {
			TempText = "�ٸ�ä�� ��ȭ��(Already Other Channel Recording)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_012)) {
			TempText = "������ ��ž ������ ���� ��û�������� ��ȭ�� �Ұ��մϴ�. ��ž ������ Ȯ�����ּ���.";//"��û���� ä��(LimitedView Channel)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_013)) {
			TempText = "���� ä��(Limited Channel)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_014)) {
			TempText = "��� ���(Hold Mode)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_015)) {
			TempText = "�̹� ��ȭ�� (Already Recording)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_016)) {
			TempText = "���� ���� (Delete Processing Error)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_017)) {
			TempText = "�̸� ���� ���� (Name Replace Error)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_018)) {
			TempText = "VOD�� ȭ�� ���� ���� (VOD DetailView Error)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_019)) {
			TempText = "���ι̵�� ����� (Private Media Playing)";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_020)) {
			TempText = "������(������ ����) ������ (Already Playing DataService )";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordResponCode.ERROR_021)) {
			TempText = "VOD����� (VOD Playing)";
		}
		
		//��ȭ / Ʃ�׺Ұ� ���ڵ�
		if (Code.contains(XML_Address_Define.ErrorCode.RecordRejectCode.ERROR_005_1)) {
			TempText = "��ž���� ����ȭ�� ����� ������� ��� �������δ� ��ó�ȭ�� �Ұ��մϴ�.";//"PIP�� �������̰� ���� ������ ��ûä�ΰ� �ٸ� ä���� ��� ��ȭ�� ���";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordRejectCode.ERROR_005_2)) {
			TempText = "VOD�� ������ ���(JOY&LIFE)��û�� PIP�� �������ϰ��";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordRejectCode.ERROR_005_3)) {
			TempText = "VOD�� ������ ���(JOY&LIFE)�� ��û���̰� �ٸ�ä���� ��ȭ���ϰ�� �� �ٸ� ä���� ��ȭ ��û��";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordRejectCode.ERROR_005_4)) {
			TempText = "��ž���� �ٸ� ä���� ��ȭ���� ��� �������δ� ��ó�ȭ�� �Ұ��մϴ�.";//"���� �ٸ� ��ä���� ��ȭ���̰� �̶� �� �ٸ�ä���� ��ȭ��û �� ���";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordRejectCode.ERROR_002)) {
			TempText = "��ž�� ���� ������ �����մϴ�. ��ȭ�� ����� Ȯ�����ּ���.";//"��ũ �뷮�� ������ ���";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordRejectCode.ERROR_003)) {
			TempText = "�ߺ��� ��ȭ������ ���";
		} else if (Code.contains(XML_Address_Define.ErrorCode.RecordRejectCode.ERROR_020)) {
			TempText = "JOY&LIFE, VOD, �̵�� �ٹ� ���� ���� ���";
		}
		
		setText(TempText);
	}
	

}
