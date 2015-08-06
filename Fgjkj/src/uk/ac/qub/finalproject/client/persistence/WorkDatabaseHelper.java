/**
 * 
 */
package uk.ac.qub.finalproject.client.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import finalproject.poc.calculationclasses.IDataProcessor;
import finalproject.poc.calculationclasses.ResultsPacketList;
import finalproject.poc.calculationclasses.WorkPacketList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Phil
 *
 */
public class WorkDatabaseHelper implements DataStorage {

	private DatabaseHelper database;
	private SQLiteDatabase db;
	private ByteArrayOutputStream bytesOut;
	private ByteArrayInputStream bytesIn;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private static final String OBJECT_QUERY = "SELECT "
			+ DataObjectsTable.OBJECT_COLUMN + " FROM "
			+ DataObjectsTable.TABLE_NAME + "WHERE "
			+ DataObjectsTable.OBJECT_NAME_COLUMN + "= ?";
	
	private static final String INSERT_TASK_QUERY = "INSERT OR IGNORE INTO "
			+ NetworkTasksTable.TABLE_NAME + "VALUES (?)";

	public WorkDatabaseHelper(Context context) {
		database = new DatabaseHelper(context);
	}
	
	@Override
	public void setupStorage() {
		
		
	}

	@Override
	public void openDatabase() {
		db = database.getWritableDatabase();

	}

	@Override
	public void closeDatabase() {
		database.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * finalproject.poc.calculationclasses.WorkDatabase#saveResultsPacketList
	 * (finalproject.poc.calculationclasses.ResultsPacketList)
	 */
	@Override
	public void saveResultsPacketList(ResultsPacketList resultsList) {
		ContentValues values = new ContentValues();
		bytesOut = new ByteArrayOutputStream();
		try {
			out = new ObjectOutputStream(bytesOut);
			out.writeObject(resultsList);
			values.put(DataObjectsTable.OBJECT_COLUMN, bytesOut.toByteArray());

			out.close();
			bytesOut.close();

			db.update(DataObjectsTable.TABLE_NAME, values,
					DataObjectsTable.OBJECT_NAME_COLUMN + "="
							+ DataObjectsTable.RESULT_LIST, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * finalproject.poc.calculationclasses.WorkDatabase#loadResultsPacketList()
	 */
	@Override
	public ResultsPacketList loadResultsPacketList() {
		// TODO Auto-generated method stub
		try {
			Cursor cursor = db.rawQuery(OBJECT_QUERY,
					new String[] { DataObjectsTable.RESULT_LIST });
			cursor.moveToFirst();
			byte[] resultBytes = cursor.getBlob(0);

			bytesIn = new ByteArrayInputStream(resultBytes);

			in = new ObjectInputStream(bytesIn);
			return (ResultsPacketList) in.readObject();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see finalproject.poc.calculationclasses.WorkDatabase#saveWorkPacketList(
	 * finalproject.poc.calculationclasses.WorkPacketList)
	 */
	@Override
	public void saveWorkPacketList(WorkPacketList workPacketList) {
		ContentValues values = new ContentValues();
		bytesOut = new ByteArrayOutputStream();
		try {
			out = new ObjectOutputStream(bytesOut);
			out.writeObject(workPacketList);
			values.put(DataObjectsTable.OBJECT_COLUMN, bytesOut.toByteArray());

			out.close();
			bytesOut.close();

			db.update(DataObjectsTable.TABLE_NAME, values,
					DataObjectsTable.OBJECT_NAME_COLUMN + "="
							+ DataObjectsTable.WORK_LIST, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * finalproject.poc.calculationclasses.WorkDatabase#loadWorkPacketList()
	 */
	@Override
	public WorkPacketList loadWorkPacketList() {
		try {
			Cursor cursor = db.rawQuery(OBJECT_QUERY,
					new String[] { DataObjectsTable.WORK_LIST });
			cursor.moveToFirst();
			byte[] resultBytes = cursor.getBlob(0);

			bytesIn = new ByteArrayInputStream(resultBytes);

			in = new ObjectInputStream(bytesIn);
			return (WorkPacketList) in.readObject();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void saveProcessorClass(IDataProcessor dataProcessor) {
		ContentValues values = new ContentValues();
		bytesOut = new ByteArrayOutputStream();
		try {
			out = new ObjectOutputStream(bytesOut);
			out.writeObject(dataProcessor);
			values.put(DataObjectsTable.OBJECT_COLUMN, bytesOut.toByteArray());

			out.close();
			bytesOut.close();

			db.update(DataObjectsTable.TABLE_NAME, values,
					DataObjectsTable.OBJECT_NAME_COLUMN + "="
							+ DataObjectsTable.PROCESSOR, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public IDataProcessor loadProcessorClass() {
		try {
			Cursor cursor = db.rawQuery(OBJECT_QUERY,
					new String[] { DataObjectsTable.PROCESSOR });
			cursor.moveToFirst();
			byte[] resultBytes = cursor.getBlob(0);

			bytesIn = new ByteArrayInputStream(resultBytes);

			in = new ObjectInputStream(bytesIn);
			return (IDataProcessor) in.readObject();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void logNetworkRequest(int requestNum) {
		db.execSQL(INSERT_TASK_QUERY, new String[] { requestNum + "" });

	}

	@Override
	public List<Integer> getIncompleteNetworkActions() {
		Cursor cursor = db.rawQuery(OBJECT_QUERY, null);
		List<Integer> taskList = new ArrayList<Integer>();

		if (cursor.moveToFirst()) {
			do {
				taskList.add(cursor.getInt(0));
			} while (cursor.moveToNext());
		}

		return taskList;

	}

	@Override
	public void deleteNetworkRequest(int requestNum) {
		db.delete(NetworkTasksTable.TABLE_NAME, NetworkTasksTable.TASK_ID_ROW
				+ "=" + requestNum, null);

	}

	private class DatabaseHelper extends SQLiteOpenHelper {

		private static final String DATABASE_NAME = "Work Database";
		private static final int DATABASE_VERSION = 1;

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			DataObjectsTable.onCreate(db);
			NetworkTasksTable.onCreate(db);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			DataObjectsTable.onUpgrade(db);
			NetworkTasksTable.onUpgrade(db);

		}

	}

	@Override
	public void deleteAllData() {
		// TODO Auto-generated method stub
		
	}
	
}
