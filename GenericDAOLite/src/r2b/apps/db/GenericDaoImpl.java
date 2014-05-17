/*
 * GenericDaoImpl
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import r2b.apps.utils.Logger;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


/**
 * GenericDao implementation.
 * 
 * @param <T extends DBEntity> Object
 * @param <K> Key
 */
public class GenericDaoImpl<T extends DBEntity, K> implements GenericDao<T, K> {
	
	// This values depends on DBEntity signature
	private static final String VALUE_OF_METHOD = "valueOf";
	private static final String GET_TABLE_NAME_METHOD = "getTableName";
	
	/**
	 * Database instance.
	 */
	protected SQLiteDatabase db;
	
	/**
	 * Builder.
	 * @param db the database instance.
	 */
	public GenericDaoImpl(SQLiteDatabase db) {	
		this.db = db;
	}

	@Override
	public T create(T t) throws IllegalArgumentException {
		
		if(t == null) {
			throw new IllegalArgumentException("T argument is null");
		}
		else if(t.getTableName() == null) {
			throw new IllegalArgumentException("T table name is null");
		}		
		
		// IMPORTANT: We do this because db table keys are AUTOINCREMENTAL
		// Removes primary key on content values if it exist
		ContentValues values = t.getTableContentValues();
		if(values != null) {
			values.remove(DBEntity.COL_ID);			
		}			

		// Return: the row ID of the newly inserted row, or -1 if an error occurred
		int exit = (int) db.insert(t.getTableName(), null, values);
	    
	    if(exit == -1) {
	    	throw new SQLiteException("Can not create element: \n" + t.toString() + "\n, exit code -1");
	    } else {	    	
	    	((DBEntity)t).setTableKey(exit);    		    	
	    	Logger.i(GenericDaoImpl.class.getSimpleName(), "Element created: \n" + t.toString());	    	
	    }
	    
	    return t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T retrieve(final K id, final Class<T> clazz) throws IllegalArgumentException {
		
		if(id == null) {
			throw new IllegalArgumentException("Id argument is null");
		}
		else if(clazz == null) {
			throw new IllegalArgumentException("Clazz argument is null or empty");
		}
		
		try {
			String tableName = null;
			{
				Object obj = null;
				Method method = clazz.getMethod(GET_TABLE_NAME_METHOD);
				method.invoke(obj);
				tableName = (String) obj;
			}
			
			T element = null;

			final String selection = DBEntity.COL_ID + " LIKE ?"; 
			final String[] selectionArgs = { String.valueOf(id) };
			
			final Cursor c = db.query(
				tableName,
			    null,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    null);
			
			// There is one item at least
			if (c.moveToFirst()) {
				
				{
					Object obj = null;
					Method method = clazz.getMethod(VALUE_OF_METHOD, Cursor.class);
					method.invoke(obj, new Object[] { c });
					element = (T) obj;
				}

				Logger.i(GenericDaoImpl.class.getSimpleName(), "Retrieve element with id: " + String.valueOf(id));
				
			} else {
				Logger.i(GenericDaoImpl.class.getSimpleName(), "There is no element with id: " + String.valueOf(id));
			}
			
			c.close();
			
			return element;
			
		} catch (NoSuchMethodException | 
				IllegalAccessException | 
				IllegalArgumentException | 
				InvocationTargetException | 
				ClassCastException e) {
			throw new IllegalStateException(e.toString());
		}
	}
	
	@Override
	public T update(T t) throws IllegalArgumentException {
		
		if(t == null) {
			throw new IllegalArgumentException("T argument is null");
		}
		else if(t.getTableKey() == null) {
			throw new IllegalArgumentException("T table key is null");
		}
		else if(t.getTableName() == null) {
			throw new IllegalArgumentException("T table name is null");
		}
		
		T updated = t;
		
		// IMPORTANT: We do this because db table keys are AUTOINCREMENTAL
		// Removes primary key on content values if it exist
		ContentValues values = t.getTableContentValues();
		if(values != null) {
			values.remove(DBEntity.COL_ID);			
		}

		// Which row to update, based on the ID
		final String selection = DBEntity.COL_ID + " LIKE ?";
		final String[] selectionArgs = { t.getTableKey() };
		
		// Return: the number of rows affected. Should be 1
		int exit = (int) db.update(t.getTableName(), values, selection, selectionArgs);
	    
	    if(exit == 1) {
	    	((DBEntity)updated).setTableKey(exit);    		    	
	    	Logger.i(GenericDaoImpl.class.getSimpleName(), "Element updated: \n" + t.toString());
	    } else {	    	
	    	Logger.e(GenericDaoImpl.class.getSimpleName(),
	    			"Can not update element: \n" + t.toString() + "\n, exit code distinct than 1");
	    	updated = null;
	    }
	    
	    return updated;
	}
	
	@Override
	public void delete(final T t) throws IllegalArgumentException {
		
		if(t == null) {
			throw new IllegalArgumentException("T argument is null");
		}
		else if(t.getTableKey() == null) {
			throw new IllegalArgumentException("T table key is null");
		}
		
		// Which row to delete, based on the ID
		String selection = DBEntity.COL_ID + " LIKE ?";
		String[] selectionArgs = { t.getTableKey() };
		
		// Return: the number of rows affected. Should be 1
		int exit = db.delete(t.getTableName(), selection, selectionArgs);
		
	    if(exit == 1) {	    	
	    	Logger.i(GenericDaoImpl.class.getSimpleName(), "Element deleted: \n" + t.toString());
	    } else {	    	
	    	Logger.e(GenericDaoImpl.class.getSimpleName(),
	    			"Can not delete element: \n" + t.toString() + "\n, exit code distinct than 1");
	    }
	    
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> listAll(final Class<T> clazz) {
		
		if(clazz == null) {
			throw new IllegalArgumentException("Clazz argument is null or empty");
		}
		
		try {
			String tableName = null;
			{
				Object obj = null;
				Method method = clazz.getMethod(GET_TABLE_NAME_METHOD);
				method.invoke(obj);
				tableName = (String) obj;
			}
			
			List<T> elements = new ArrayList<T>();
			
			final Cursor c = db.query(
				tableName,
			    null,
			    null,
			    null,
			    null,
			    null,
			    null);
			
			if (c.moveToFirst()) {

				do {		
					
					T element;
					{
						Object obj = null;
						Method method = clazz.getMethod(VALUE_OF_METHOD, Cursor.class);
						method.invoke(obj, new Object[] { c });
						element = (T) obj;	
					}
					
					elements.add(element);	
					
					Logger.i(GenericDaoImpl.class.getSimpleName(), 
							"Retrieve element with id: " + String.valueOf(c.getInt(0)));	    		
					
				} while (c.moveToNext());
				
			} else {
				Logger.i(GenericDaoImpl.class.getSimpleName(), "There are no elements.");
			}
			
			c.close();
			
			return elements;
			
		} catch (NoSuchMethodException | 
				IllegalAccessException | 
				IllegalArgumentException | 
				InvocationTargetException | 
				ClassCastException e) {
			throw new IllegalStateException(e.toString());
		}
	}
	
	
}
