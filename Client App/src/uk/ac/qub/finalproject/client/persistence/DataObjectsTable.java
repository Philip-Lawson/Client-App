/**
 * 
 */
package uk.ac.qub.finalproject.client.persistence;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import finalproject.poc.calculationclasses.ResultsPacketList;
import finalproject.poc.calculationclasses.WorkPacketList;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Phil
 *
 */
public class DataObjectsTable {

	public static final String TABLE_NAME = "data_objects";
	public static final String OBJECT_NAME_COLUMN = "object_name";
	public static final String OBJECT_COLUMN = "object";
	public static final String RESULT_LIST = "result list";
	public static final String WORK_LIST = "work list";
	public static final String PROCESSOR = "processor";
	private static final String CREATE_DATA_TABLE = "CREATE TABLE "
			+ TABLE_NAME + "(" + OBJECT_NAME_COLUMN + "TEXT PRIMARY KEY "
			+ OBJECT_COLUMN + " BLOB );";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DATA_TABLE);
		storeBlankWorkPacketList(db);
		storeBlankResultsPacketList(db);
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (" + PROCESSOR + ", NULL)");
		
	}

	public static void onUpgrade(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);

	}

	private static void storeBlankWorkPacketList(SQLiteDatabase db) {
		try {
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bytesOut);
			ContentValues values = new ContentValues();

			out.writeObject(new WorkPacketList());
			values.put(OBJECT_NAME_COLUMN, WORK_LIST);
			values.put(DataObjectsTable.OBJECT_COLUMN,
					bytesOut.toByteArray());

			out.close();
			bytesOut.close();
			
			db.insert(TABLE_NAME, null, values);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void storeBlankResultsPacketList(SQLiteDatabase db) {
		try {
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bytesOut);
			ContentValues values = new ContentValues();

			out.writeObject(new ResultsPacketList());
			values.put(OBJECT_NAME_COLUMN, RESULT_LIST);
			values.put(DataObjectsTable.OBJECT_COLUMN,
					bytesOut.toByteArray());

			out.close();
			bytesOut.close();
			
			db.insert(TABLE_NAME, null, values);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
