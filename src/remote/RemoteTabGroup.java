package remote;

import pvr.PvrTab;
import hdlnc.dev2.sangtae.cnm.global.CNMApplication;
import hdlnc.dev2.sangtae.cnm.global.CNM_InfomationView;
import hdlnc.dev2.sangtae.cnm.global.CNM_WarningDialog;
import hdlnc.dev2.sangtae.cnm.global.Intent_KEY_Define;
import hdlnc.dev2.sangtae.cnm.global.TabGroupActivity;
import hdlnc.dev2.sangtae.cnm.global.XML_Address_Define;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class RemoteTabGroup extends TabGroupActivity {
	private CNMApplication tempApp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tempApp = (CNMApplication)getApplication();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String tempAreaCode = tempApp.getLocationID();

		if (tempAreaCode.equals(XML_Address_Define.Channel.LIMITED_AREA_CODE.KANGNAM) || tempAreaCode.equals(XML_Address_Define.Channel.LIMITED_AREA_CODE.UlSAN)) {
			// 강남 & 울산인 경우
			// 안내 팝업
			// Tv 채널로 이동
			StartActivityMode(Intent_KEY_Define.SETTOP.MODE.LIMITED_AREA);
		}else if (!tempApp.getSettopRegAuth()) {

			//셋탑 미등록
			StartActivityMode(Intent_KEY_Define.SETTOP.MODE.UNREGISTERED);

		}else if (tempApp.getSettopRegAuth()) {
			//셋탑  미지원
			if (tempApp.getSettopBoxType().contains(XML_Address_Define.Authenticate.AUTHE_SETTOP_PVR) || tempApp.getSettopBoxType().contains(XML_Address_Define.Authenticate.AUTHE_SETTOP_HD)) {
				StartActivityMode(Intent_KEY_Define.SETTOP.MODE.SUPPORT);
			} else {
				StartActivityMode(Intent_KEY_Define.SETTOP.MODE.NOT_SUPPORT);
			}
		}else {
			//조건 완족
			StartActivityMode(Intent_KEY_Define.SETTOP.MODE.SUPPORT);

		}
	}


	private void StartActivityMode(String aMode) {
		// TODO Auto-generated method stub
		if (aMode.contains(Intent_KEY_Define.SETTOP.MODE.LIMITED_AREA)) {
			CNM_WarningDialog dialog =  new CNM_WarningDialog(getParent());
			dialog.setWarningDialogType(CNM_WarningDialog.Limited_Area);
			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					tempApp.getMainrbFirst().performClick();
				}
			});
			dialog.show();
		} else if (aMode == Intent_KEY_Define.SETTOP.MODE.UNREGISTERED) {
			Intent intent = new Intent(this,CNM_InfomationView.class);
			intent.putExtra(Intent_KEY_Define.NAVI_TITLE, "리모콘");
			intent.putExtra(Intent_KEY_Define.SETTOP.STATUS, Intent_KEY_Define.SETTOP.MODE.UNREGISTERED);
			intent.putExtra(Intent_KEY_Define.MAIN_TAB.SELECT_MAIN_TAB, Intent_KEY_Define.MAIN_TAB.TYPE.REMOCON);
			startChildActivity("CNM_InfomationView", intent);
		}else if (aMode.contains(Intent_KEY_Define.SETTOP.MODE.NOT_SUPPORT)) {
			Intent intent = new Intent(this,CNM_InfomationView.class);
			intent.putExtra(Intent_KEY_Define.NAVI_TITLE, "리모콘");
			intent.putExtra(Intent_KEY_Define.SETTOP.STATUS, Intent_KEY_Define.SETTOP.MODE.NOT_SUPPORT);
			intent.putExtra(Intent_KEY_Define.MAIN_TAB.SELECT_MAIN_TAB, Intent_KEY_Define.MAIN_TAB.TYPE.REMOCON);
			startChildActivity("CNM_InfomationView", intent);
		}else if(aMode.contains(Intent_KEY_Define.SETTOP.MODE.SUPPORT)){
			startChildActivity("RemoteTab", new Intent(this,RemoteTab.class));
		}
	}
}
