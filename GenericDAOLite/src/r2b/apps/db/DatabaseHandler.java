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

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import r2b.apps.utils.Cons;
import r2b.apps.utils.Logger;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
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
	 * Creating string.
	 */
	private static StringBuilder strBuilder = new StringBuilder();
	
	/**
	 * Inits the database.
	 * @param context The application context.
	 * @param propertiesRawResource The database creation properties file
	 * @return The db handler.
	 */
	public synchronized static DatabaseHandler init(final Context context, 
			final int propertiesRawResource) {
		if(instance == null) { 
			instance = new DatabaseHandler(context);
			load(context, propertiesRawResource);
		}
		return instance;
	}
	
	/**
	 * Singleton method.
	 * @return The database on writable mode.
	 */
	public synchronized static SQLiteDatabase getDatabase() {
		if (db == null) {
			db = instance.getWritableDatabase();
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
		
		try {
			
			db.execSQL(strBuilder.toString());	
			strBuilder = null;

		} catch (SQLException e) {
			Logger.e(DatabaseHandler.class.getSimpleName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Logger.i(DatabaseHandler.class.getSimpleName(), "onUpgrade");
	}
	
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#close()
	 */
	@Override
	public synchronized void close() {
		if (db != null) {
			db.close();
			instance.close();			
		}
	}
	
	/**
	 * Read from the /res/raw directory
	 * @param context
	 * @param propertiesRawResource
	 */
	private static void load(final Context context, final int propertiesRawResource) {	
		try {
		    InputStream rawResource = context.getResources().openRawResource(propertiesRawResource);
		    Properties properties = new Properties();
		    properties.load(rawResource);
		    create(properties);
		} catch (NotFoundException | IOException e) {
			Logger.e(DatabaseHandler.class.getSimpleName(), "Can't read database properties", e);
			throw new RuntimeException(e);
		}
	}
	
	private static void create(final Properties properties) {
		final Enumeration<Object> e = properties.elements();

	    while (e.hasMoreElements()) {
	      String key = (String) e.nextElement();
	      String value = properties.getProperty(key);
	      
	      String tablename = getTableName(key.trim());
	      String [] index = getCreateIndex(value.trim());
	      
	      
	      strBuilder
	      	.append("CREATE TABLE ")
	      	.append(tablename);
	      strBuilder
	      	.append(" ")
	      	.append(getCreateArgs(value))
	      	.append(";");	      
	      
	      
	      if(index != null) {
	    	  for(int i = 0; i < index.length; i++) {
	    		  strBuilder
		    		  .append("CREATE INDEX ")
		    		  .append(tablename);
	    		  strBuilder
		    		  .append("_")
		    		  .append(index[i].trim());
	    		  strBuilder
		    		  .append("_index")
		    		  .append(" ON ");
	    		  strBuilder
		    		  .append(tablename)
		    		  .append("( ");
	    		  strBuilder
		    		  .append(index[i].trim())
		    		  .append(" );");
	    	  }
	      }
	      
	    }
	    
	}	
	
	private static String getTableName(final String key) {	
        int pos = key.lastIndexOf(".");
        if (pos == -1) return key;
        return key.substring(0, pos);
	}
	
	private static String getCreateArgs(final String value) {
		return value.split(";")[0].trim();
	}
	
	private static String[] getCreateIndex(final String value) {
		if (value.split(";").length > 0) {
			String args = value.split(";")[1].trim();
			return args.split(",");			
		}
		return null;
	}


	
}
