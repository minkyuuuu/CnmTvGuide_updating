package hdlnc.dev2.sangtae.cnm.global;

import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TV_MSG_Data implements Serializable 
{

	private static final long serialVersionUID = 1L;

	private Integer	DB_ID;
	private String	DB_Msg_Time;
	private String DB_Msg;
	
	public TV_MSG_Data() {
		// TODO Auto-generated constructor stub
	}
	
	public TV_MSG_Data(String aTime, String aMsg) {
		// TODO Auto-generated constructor stub
		DB_Msg_Time = aTime;
		DB_Msg		= aMsg;
	}
	
	public Integer getDB_ID() {
		return DB_ID;
	}
	public void setDB_ID(Integer dB_ID) {
		DB_ID = dB_ID;
	}
	public String getDB_Msg_Time() {
		return DB_Msg_Time;
	}
	public void setDB_Msg_Time(String dB_Msg_Time) {
		DB_Msg_Time = dB_Msg_Time;
	}
	public String getDB_Msg() {
		return DB_Msg;
	}
	public void setDB_Msg(String dB_Msg) {
		DB_Msg = dB_Msg;
	}
	
}
