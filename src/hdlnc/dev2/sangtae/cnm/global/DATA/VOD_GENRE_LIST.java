package hdlnc.dev2.sangtae.cnm.global.DATA;

/*
 * XML ���Ǽ� ǥ����� �������� Ŭ���� �̸��� �����Ͽ����� ���ظ� �������� �ΰ������� �ʿ�
 * GENRE_ALL �� VOD �޴��� �帣�� �� ����Ʈ�� �ǹ�  
 */

import java.util.ArrayList;

public class VOD_GENRE_LIST extends ArrayList<VOD_GENRE> implements Cloneable {

	private String RESULT;

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return (VOD_GENRE_LIST)super.clone();
	}

	//get
	public String getRESULT() {
		return RESULT;
	}
	//set
	public void setRESULT(String rESULT) {
		RESULT = rESULT;
	}

}
