package hdlnc.dev2.sangtae.cnm.global;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static hdlnc.dev2.sangtae.cnm.global.TVCH_PR_CheckSQLiteOpenHelper.PROGRAM_ID;
public class TVCH_Alarm_CheckSQLiteOpenHelper extends SQLiteOpenHelper{

	public static final String DB_NAME = "tvalarm.db";
	//public static final String DBPR_NAME = "tvch_check_.db";
	public final static int VERSION = 3;

	public static final String TV_ALARM_TABLE		= "tvch_alarm_table";
	public static final String SCHEULE_SEQ			= "Schedule_seq";// primary key
	public static final String PROGRAM_TITLE	= "Program_Title";
	public static final String PROGRAM_CH_NUMBER 	    = "Channel_number";
	public static final String PROGRAM_CH_NAME 	    = "Channel_name";
	public static final String PROGRAM_CH_GRADE 	    = "Channel_grade";
	public static final String PROGRAM_ALARM_CHECK 		= "Alarm_check";
	public static final String PROGRAM_ALARM_TIME	= "alartime";
	public static final String PROGRAM_CH_URL	= "Channel_Url";
	public static final String PROGRAM_ALARAM_TIME_TEXT	= "Alarm_Time_Text";
	public static final String PROGRAM_ALARAM_HD	= "Alarm_Time_Hd";
	      
	
	//public static final String PROGRAM_COMPLETE		= "_complete";
	
	
	
	public TVCH_Alarm_CheckSQLiteOpenHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		  db.execSQL("CREATE TABLE " + TV_ALARM_TABLE + " ("
                  + SCHEULE_SEQ + " TEXT PRIMARY KEY,"
                  + PROGRAM_TITLE + " TEXT,"
                  + PROGRAM_CH_NUMBER + " TEXT,"
                  + PROGRAM_CH_NAME + " TEXT,"
                  + PROGRAM_CH_URL + " TEXT,"
                  + PROGRAM_ALARM_CHECK + " BOOL,"
                  + PROGRAM_ALARM_TIME  + " DATETIME,"
                  + PROGRAM_ID +" TEXT,"
                  + PROGRAM_ALARAM_TIME_TEXT +" TEXT,"
                  + PROGRAM_ALARAM_HD +" BOOL,"
                  + PROGRAM_CH_GRADE + " TEXT"
                  + ");");
		  
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 db.execSQL("DROP TABLE IF EXISTS " + TV_ALARM_TABLE);
		onCreate(db);
	}

	
}
