package hdlnc.dev2.sangtae.cnm.global;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TVCH_PR_CheckSQLiteOpenHelper extends SQLiteOpenHelper {

	public static final String DBPR_NAME = "tvprcheck.db";
	//public static final String DBPR_NAME = "tvch_check_.db";
	public final static int VERSION = 3;

	public static final String TVPR_CHECK_TABLE		= "tvch_pr_table";
	public static final String PROGRAM_ID			= "proramid";
	public static final String PROGRAM_MY_CHANNEL	= "progrm";
	public static final String PROGRAM_MY_CHANNEL_TIME	= "proram_time";
	public static final String PROGRAM_ALARM 		= "alam";
	public static final String PROGRAM_ALARM_TIME	= "alartime";
	//public static final String PROGRAM_COMPLETE		= "_complete";

	
	//private String CREATE = "CREATE TABLE IF NOT EXISTS ";
	
	public TVCH_PR_CheckSQLiteOpenHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DBPR_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		  db.execSQL("CREATE TABLE " + TVPR_CHECK_TABLE + " ("
                  + PROGRAM_ID +  " TEXT PRIMARY KEY,"
                  + PROGRAM_MY_CHANNEL + " TEXT,"
                  + PROGRAM_MY_CHANNEL_TIME + " TEXT"
                 // + PROGRAM_ALARM + " TEXT,"
                 // + PROGRAM_ALARM_TIME + " TEXT"
                  + ");");
		  
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 db.execSQL("DROP TABLE IF EXISTS " + TVPR_CHECK_TABLE);
		onCreate(db);
	}

}
