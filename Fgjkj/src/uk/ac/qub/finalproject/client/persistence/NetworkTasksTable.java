/**
 * 
 */
package uk.ac.qub.finalproject.client.persistence;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author Phil
 *
 */
public class NetworkTasksTable {

	public static final String TABLE_NAME = "tasks";
	public static final String TASK_ID_ROW = "task_id";

	private static final String CREATE_TASKS_TABLE = "CREATE TABLE "
			+ TABLE_NAME + "(" + TASK_ID_ROW + " INTEGER PRIMARY KEY);";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TASKS_TABLE);
	}

	public static void onUpgrade(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
