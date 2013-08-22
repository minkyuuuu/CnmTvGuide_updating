package hdlnc.dev2.sangtae.cnm.global;

import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TVCH_Alarm_CheckList implements Serializable 
{

	private static final long serialVersionUID = 1L;

	private String	DB_SEQ;
	private String	DB_PROGRAM_TITLE;
	private String	DB_PROGRAM_CH_NUMBER;
	private String	DB_PROGRAM_CH_NANE;
	private String	DB_PROGRAM_CH_GRADE;
	private String	DB_CH_URL;
	private Boolean DB_ALARM_CHECK;
	private String DB_ALARM_TIME = "NULL";
	private Long	DB_ALARM__MilliTime;





	private String DB_ALARM_TIME_TEXT ="NULL";
	private Boolean DB_ALARM_HD=false;
	
	
	public String getDB_PROGRAM_CH_NANE() {
		return DB_PROGRAM_CH_NANE;
	}
	
	
	
	
	
	public void setDB_PROGRAM_CH_NANE(String dBPROGRAMCHNANE) {
		DB_PROGRAM_CH_NANE = dBPROGRAMCHNANE;
	}
	public String getDB_PROGRAM_CH_GRADE() {
		return DB_PROGRAM_CH_GRADE;
	}
	
	
	
	
	
	public void setDB_PROGRAM_CH_GRADE(String dB_PROGRAM_CH_GRADE) {
		DB_PROGRAM_CH_GRADE = dB_PROGRAM_CH_GRADE;
	}
	
	
	
	
	public Boolean getDB_ALARM_HD() {
		return DB_ALARM_HD;
	}





	public void setDB_ALARM_HD(Boolean dB_ALARM_HD) {
		 this.DB_ALARM_HD = dB_ALARM_HD;
	}





	public String getDB_ALARM_TIME_TEXT() {
		return DB_ALARM_TIME_TEXT;
	}





	public void setDB_ALARM_TIME_TEXT(String dB_ALARM_TIME_TEXT) {
		
		DB_ALARM_TIME_TEXT = dB_ALARM_TIME_TEXT;
	}





	public String getDB_PROGRAM_ID() {
		return DB_PROGRAM_ID;
	}





	public void setDB_PROGRAM_ID(String dB_PROGRAM_ID) {
		DB_PROGRAM_ID = dB_PROGRAM_ID;
	}





	private String DB_PROGRAM_ID;
	
	
	
	
	public TVCH_Alarm_CheckList(String SEQ) {
		// TODO Auto-generated constructor stub
		DB_SEQ=SEQ;
		DB_PROGRAM_TITLE		= "NULL";
		DB_PROGRAM_CH_NUMBER		= "NULL";
		DB_CH_URL		= "NULL";
		
		DB_ALARM_CHECK	= false;
		DB_ALARM_TIME	= "NULL";
		DB_PROGRAM_ID = "NULL";
	}





	public String getDB_SEQ() {
		return DB_SEQ;
	}





	public void setDB_SEQ(String dB_SEQ) {
		DB_SEQ = dB_SEQ;
	}





	public String getDB_PROGRAM_TITLE() {
		return DB_PROGRAM_TITLE;
	}





	public void setDB_PROGRAM_TITLE(String dB_PROGRAM_TITLE) {
		DB_PROGRAM_TITLE = dB_PROGRAM_TITLE;
	}





	public String getDB_PROGRAM_CH_NUMBER() {
		return DB_PROGRAM_CH_NUMBER;
	}





	public void setDB_PROGRAM_CH_NUMBER(String dB_PROGRAM_CH_NUMBER) {
		DB_PROGRAM_CH_NUMBER = dB_PROGRAM_CH_NUMBER;
	}





	public String getDB_CH_URL() {
		return DB_CH_URL;
	}





	public void setDB_CH_URL(String dB_CH_URL) {
		DB_CH_URL = dB_CH_URL;
	}





	public Boolean getDB_ALARM_CHECK() {
		return DB_ALARM_CHECK;
	}





	public void setDB_ALARM_CHECK(Boolean dB_ALARM_CHECK) {
		DB_ALARM_CHECK = dB_ALARM_CHECK;
	}





	public String getDB_ALARM_TIME() {
		return DB_ALARM_TIME;
	}

	public Long getDB_ALARM__MilliTime() {
		return DB_ALARM__MilliTime;
	}
	public void setDB_ALARM_TIME(String dB_ALARM_TIME) {
		this.DB_ALARM__MilliTime = getmilliTime(dB_ALARM_TIME);
		DB_ALARM_TIME = dB_ALARM_TIME;
	}
	
	private Long getmilliTime(String onTime){
		SimpleDateFormat mOriTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);

		ParsePosition pos = new ParsePosition(0);
		Date beforeTime = mOriTime.parse(onTime, pos);

		return beforeTime.getTime();
	}
	
}
