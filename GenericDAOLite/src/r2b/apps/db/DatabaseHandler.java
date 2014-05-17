/*
 * DatabaseHandler
 * 
 * 0.1
 * 
 * 2014/05/16
 * 
 * (The MIT License)
 * 
 * Copyright (c) R2B Apps <r2b.apps@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

package r2b.apps.db;

import r2b.apps.utils.Cons;
import r2b.apps.utils.Logger;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public final class DatabaseHandler extends SQLiteOpenHelper {

	/*
	 * The SqliteOpenHelper object holds on to one database connection. 
	 * It appears to offer you a read and write connection, but it really 
	 * doesn't. Call the read-only, and you'll get the write database 
	 * connection regardless.
	 * 
	 * So, one helper instance, one db connection. Even if you use it from 
	 * multiple threads, one connection at a time. The SqliteDatabase object 
	 * uses java locks to keep access serialized. So, if 100 threads have 
	 * one db instance, calls to the actual on-disk database are serialized.
	 * 
	 * So, one helper, one db connection, which is serialized in java code. 
	 * One thread, 1000 threads, if you use one helper instance shared between 
	 * them, all of your db access code is serial. And life is good (ish).
	 * 
	 * If you try to write to the database from actual distinct connections at 
	 * the same time, one will fail. It will not wait till the first is done 
	 * and then write. It will simply not write your change.
	 */

	// Tables
	public static final String TABLE_EMPLOYEE = "Employee"; // TODO GET ENTITY NAME

	
	// Employee Columns
	public static final String COL_EMPLOYEE_ID = Cons.DB.COLD_ID;
	public static final String COL_EMPLOYEE_NAME = "name";
	public static final String COL_EMPLOYEE_SURNAME = "surname";
	public static final String COL_EMPLOYEE_ACTIVE = "active";


	/**
	 * Database version.
	 */
	public static final int DATABASE_VERSION = Cons.DB.DATABASE_VERSION;
	/**
	 * Database name.
	 */
	public static final String DATABASE_NAME = Cons.DB.DATABASE_NAME;	
	/**
	 * Handler instance.
	 */
	private static DatabaseHandler instance;
	/**
	 * Database instance.
	 */
	private static SQLiteDatabase db;
	
	/**
	 * Inits the database.
	 * @param context The application context.
	 * @return The db handler.
	 */
	public synchronized static DatabaseHandler init(final Context context) {
		if(instance == null) { 
			instance = new DatabaseHandler(context);
		}
		return instance;
	}
	
	/**
	 * Singleton method.
	 * @return The database on writable mode.
	 */
	public synchronized static SQLiteDatabase getDatabase() {
		if (db == null || db.isReadOnly()) {
			db = instance.getWritableDatabase();
		}
		return db;
	}

	/**
	 * Singleton method.
	 * @return The database on readable mode.
	 */
	public synchronized static SQLiteDatabase getDatabaseReadOnly() {
		if (db == null || !db.isReadOnly()) {
			db = instance.getReadableDatabase();
		}
		return db;
	}
	
	/**
	 * Builder.
	 * @param context The application context.
	 */
	private DatabaseHandler(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(final SQLiteDatabase db) {
		Logger.i(DatabaseHandler.class.getSimpleName(), "onCreate");
		
		final StringBuffer employeeCreateArgs = new StringBuffer();			
		employeeCreateArgs
			.append("(" + COL_EMPLOYEE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,") // Incremental local id
			.append(COL_EMPLOYEE_NAME + " TEXT,");
		employeeCreateArgs
			.append(COL_EMPLOYEE_SURNAME + " TEXT,")
			.append(COL_EMPLOYEE_ACTIVE + " INTEGER)"); // BOOLEAN in 0 (false) and 1 (true)
			
		
		try {
			
			db.execSQL("CREATE TABLE " + TABLE_EMPLOYEE + employeeCreateArgs);
			
			
			// Indexes for most searched
			db.execSQL("CREATE INDEX " + TABLE_EMPLOYEE + "_" + COL_EMPLOYEE_NAME + "_" +"index"  
					+ " ON " + TABLE_EMPLOYEE + "(" + COL_EMPLOYEE_NAME + ")");
			
			
			// Show log info
			Logger.i(DatabaseHandler.class.getSimpleName(), "CREATE TABLE " + TABLE_EMPLOYEE + employeeCreateArgs);
			Logger.i(DatabaseHandler.class.getSimpleName(), "CREATE INDEX " + TABLE_EMPLOYEE + "_" + COL_EMPLOYEE_NAME + "_" +"index"  
					+ " ON " + TABLE_EMPLOYEE + "(" + COL_EMPLOYEE_NAME + ")");
			

		} catch (SQLException e) {
			Logger.e(DatabaseHandler.class.getSimpleName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
		
	}


	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(final SQLiteDatabase db, int oldVersion, int newVersion) {
		Logger.i(DatabaseHandler.class.getSimpleName(), "onUpgrade");
		
		// Drop older table and index if existed
		try {

			db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
			
			db.execSQL("DROP INDEX IF EXISTS " + TABLE_EMPLOYEE + "_" + COL_EMPLOYEE_NAME + "_" +"index");			
			
			// Show log info
			Logger.i(DatabaseHandler.class.getSimpleName(), "DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
			
			Logger.i(DatabaseHandler.class.getSimpleName(), 
					"DROP INDEX IF EXISTS " + TABLE_EMPLOYEE + "_" + COL_EMPLOYEE_NAME + "_" +"index");			
			
			
			// Create tables and indexes again
			onCreate(db);
		} catch (SQLException e) {
			Logger.e(DatabaseHandler.class.getSimpleName(), "Can't drop database", e);
			throw new RuntimeException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#close()
	 */
	@Override
	public synchronized void close() {
		if (db != null) {
			db.close();
		}
	}
	
	/**
	 * Clear all db data.
	 */
	public synchronized void clear() {
		onUpgrade(db, DATABASE_VERSION, DATABASE_VERSION-1);
	}
	
}
