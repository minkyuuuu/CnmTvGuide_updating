package hdlnc.dev2.sangtae.cnm.global.DATA;

/*
 * XML 정의서 표기법을 기준으로 클래스 이름을 정의하였으나 이해를 돕기위해 부가설명이 필요
 * GENRE_ALL 은 VOD 메뉴의 장르별 탭 리스트를 의미  
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
