package com.patrickchristensen.qup.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QupDBAdapter {
	
	private SQLiteHelper dbHelper;
	
	public QupDBAdapter(Context context) {
		dbHelper = new SQLiteHelper(context);
	}
	
	public long insertSong(long songId, int votes){
		ContentValues cv = new ContentValues();
		cv.put(SQLiteHelper.UID, songId);
		cv.put(SQLiteHelper.VOTES, votes);
		return dbHelper.getWritableDatabase().insert(SQLiteHelper.TABLE_SONGVOTES, null, cv);
	}
	
	public long updateSong(long songId, int votes){
		ContentValues cv = new ContentValues();
		cv.put(SQLiteHelper.UID, songId);
		cv.put(SQLiteHelper.VOTES, votes);
		return dbHelper.getWritableDatabase().update(SQLiteHelper.TABLE_SONGVOTES, cv, SQLiteHelper.UID+"="+songId, null);
	}
	
	public int getSongVotes(long songId){
		String query = "SELECT " + SQLiteHelper.VOTES + " FROM " + SQLiteHelper.TABLE_SONGVOTES + " WHERE " + SQLiteHelper.UID + " = " + songId;
		Cursor result = dbHelper.getWritableDatabase().rawQuery(query, null);
		if(result.moveToFirst()){
			return result.getInt(result.getColumnIndex(SQLiteHelper.VOTES));
		}
		return 0;
	}
	
	private static class SQLiteHelper extends SQLiteOpenHelper {
		
		private static final String 		DATABASE_NAME = "qup.db";
		private static final int 			DATABASE_VERSION = 2;
		
		private static final String 		TABLE_SONGVOTES = "SONGVOTES";
		private static final String 		UID = "_id";
		private static final String			VOTES = "Votes";
		private static final String			CREATE_TABLE_SONGVOTES = "CREATE TABLE "+TABLE_SONGVOTES+"("+UID+" INTEGER PRIMARY KEY, "+VOTES+" INTEGER);";
		private static final String			DROP_TABLE_SONGVOTES = "DROP TABLE IF EXISTS "+TABLE_SONGVOTES;
	
		public SQLiteHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE_SONGVOTES);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DROP_TABLE_SONGVOTES);
			onCreate(db);
		}
		
	}
	
}
