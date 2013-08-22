package hdlnc.dev2.sangtae.cnm.global;

import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TVCH_PR_CheckList implements Serializable 
{

	private static final long serialVersionUID = 1L;

	private String	DB_ID;
	//private String	DB_Alarm_Time;
	private String	DB_MyProgram_Time;
	//private Boolean	DB_Alarm;
	private Boolean DB_MyProgram;


	public TVCH_PR_CheckList(String ID) {
		// TODO Auto-generated constructor stub
		DB_ID=ID;
		//DB_Alarm_Time		= "NULL";
		//DB_Alarm	= false;
		DB_MyProgram	= false;
	}
	//get
	public String getID() {
		return DB_ID;
	}
	public Boolean getMyProgram() {
		return DB_MyProgram;
	}
	public String get_MyProgram_Time() {
		return DB_MyProgram_Time;
	}
	
	/*public Boolean getAlarm() {
		return DB_Alarm;
	}
	public String getAlarm_Time() {
		return DB_Alarm_Time;
	}*/


	//set
	public void setID(String iD) {
		DB_ID = iD;
	}
	public void setMyProgram(Boolean myChannel) {
		DB_MyProgram = myChannel;
	}
	public void set_MyProgram_Time(String Time) {
		DB_MyProgram_Time = Time;
	}
	/*public void setAlarm(Boolean Alarm) {
		DB_Alarm = Alarm;
	}
	public void setAlarmTime(String aTime) {
		DB_Alarm_Time = aTime;
	}*/

	
}
