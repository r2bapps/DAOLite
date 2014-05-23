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

import r2b.apps.R;
import r2b.apps.utils.Cons;
import r2b.apps.utils.Logger;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * NOTE: Foreign key constraints with on delete cascade are supported.
 * It only works since Android 2.2 Froyo which has SQLite 3.6.22
 */
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
	 * Application context.
	 */
	private static Context mContext;
	
	/**
	 * Init the database.
	 * 
	 * WARNING: On Cons.CLEAR_DB_ON_START = true, deletes db file on each start.
	 * 
	 * @param context The application context.
	 * @return The db handler.
	 */
	public synchronized static DatabaseHandler init(final Context context) {
		if(instance == null) { 
			
			if(Cons.DB.CLEAR_DB_ON_START) {
				boolean exit = context.deleteDatabase(DATABASE_NAME);
				if(!exit) {
					Logger.e(DatabaseHandler.class.getSimpleName(), "Can't delete database on startup");
					throw new RuntimeException("Can't delete database on startup");
				}
			}
			
			instance = new DatabaseHandler(context);
			mContext = context.getApplicationContext();
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
			
			db.beginTransaction();

			StringBuilder strBuilder = new StringBuilder();
			
			strBuilder = loadTable(strBuilder, R.raw.create_table);			
			if(strBuilder != null) {
				db.execSQL(strBuilder.toString());
				
				strBuilder.setLength(0);
				
				strBuilder = loadIndex(strBuilder, R.raw.create_index);
				if(strBuilder != null) {
					db.execSQL(strBuilder.toString());	
				}
			}
		
			
			db.setTransactionSuccessful();									

		} catch (SQLException e) {
			Logger.e(DatabaseHandler.class.getSimpleName(), "Can't create database", e);
			throw new RuntimeException(e);
		} finally {
			mContext = null;
			db.endTransaction();
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
	 * @see android.database.sqlite.SQLiteOpenHelper#onOpen(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onOpen(SQLiteDatabase db) {
	    super.onOpen(db);
	    if (!db.isReadOnly()) {
	        // Enable foreign key constraints
	        db.execSQL("PRAGMA foreign_keys=ON;");
	    }
	}
	
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#close()
	 */
	@Override
	public synchronized void close() {
		if (db != null) {
			db.close();
			instance.close();
			db = null;
			instance = null;
		}
	}
	
	/**
	 * Read from the /res/raw directory
	 * @param propertiesFileResId
	 * @return
	 */
	private Properties loadProperties(final int propertiesFileResId) {	
		try {
		    InputStream rawResource = mContext.getResources().openRawResource(propertiesFileResId);
		    Properties properties = new Properties();
		    properties.load(rawResource);
		    return properties;
		} catch (NotFoundException | IOException e) {
			Logger.e(DatabaseHandler.class.getSimpleName(), "Can't read database properties", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Read from the /res/raw directory table info
	 * @param stringBuilder
	 * @param propertiesFileResId
	 * @return
	 */
	private StringBuilder loadTable(StringBuilder stringBuilder, final int propertiesFileResId) {	
	    Properties properties = loadProperties(propertiesFileResId);
	    stringBuilder = createTable(stringBuilder, properties);
	    return stringBuilder;
	}
	
	/**
	 * Read from the /res/raw directory index info
	 * @param stringBuilder
	 * @param propertiesFileResId
	 * @return
	 */
	private StringBuilder loadIndex(StringBuilder stringBuilder, final int propertiesFileResId) {	
	    Properties properties = loadProperties(propertiesFileResId);
	    stringBuilder = createIndex(stringBuilder, properties);
	    return stringBuilder;
	}
	
	/**
	 * Build create table query.
	 * @param strBuilder
	 * @param properties
	 * @return
	 */
	private StringBuilder createTable(StringBuilder strBuilder, final Properties properties) {
		
		if(properties == null) {
			return null;
		}
		
		final Enumeration<Object> e = properties.keys();

	    while (e.hasMoreElements()) {
	      String key = (String) e.nextElement();
	      String value = properties.getProperty(key);
	      	      
	      strBuilder
	      	.append("CREATE TABLE ")
	      	.append(key.trim());
	      strBuilder
	      	.append(" ( ")
	      	.append(value)
	      	.append(" );");	      	      	    	      
	    }
	    
	    return strBuilder;
	    
	}	
	
	/**
	 * Build create index query.
	 * @param strBuilder
	 * @param properties
	 * @return
	 */
	private StringBuilder createIndex(StringBuilder strBuilder, final Properties properties) {
		
		if(properties == null) {
			return null;
		}
		
		final Enumeration<Object> e = properties.keys();

	    while (e.hasMoreElements()) {
	      String key = (String) e.nextElement();
	      String value = properties.getProperty(key);
	      
	      String [] index = getIndex(value.trim());     	      
	      
	      if(index != null) {
	    	  for(int i = 0; i < index.length; i++) {
	    		  
	    		  if(index[i] != null && !"".equals(index[i])) {
		    		  strBuilder
			    		  .append("CREATE INDEX ")
			    		  .append(key.trim());
		    		  strBuilder
			    		  .append("_")
			    		  .append(index[i].trim());
		    		  strBuilder
			    		  .append("_index")
			    		  .append(" ON ");
		    		  strBuilder
			    		  .append(key.trim())
			    		  .append("( ");
		    		  strBuilder
			    		  .append(index[i].trim())
			    		  .append(" );");
	    		  }
	    		  
	    	  }
	      }
	      
	    }
	    
	    return strBuilder;
	    
	}
	
	/**
	 * Split index
	 * @param value
	 * @return
	 */
	private String[] getIndex(final String value) {
		if (value.split(",").length > 0) {
			String[] args = value.split(",");
			return args;			
		}
		return null;
	}
		
	
}
