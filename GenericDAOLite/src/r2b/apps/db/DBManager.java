/*
 * DBManager
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

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * 
 * Database manager.
 *
 * @param <K> Key
 */
public class DBManager<K> {

	/**
	 * DB handler.
	 */
	private final DatabaseHandler handler;
	/**
	 * Generic DAO.
	 */
	private GenericDao<DBEntity<K>, K> dao;
	
	/**
	 * Builder.
	 * @param context The application context.
	 * @param propertiesRawResource The database creation properties file
	 */
	public DBManager(final Context context, final int propertiesRawResource) {
		handler = DatabaseHandler.init(context.getApplicationContext(), propertiesRawResource);
		
		final SQLiteDatabase db = DatabaseHandler.getDatabase();
		
		dao = new GenericDaoImpl<DBEntity<K>, K>(db);
	}
	
	/**
	 * Insert item on db.
	 * @param e Item to insert.
	 * @return Item with setted id.
	 * @throws IllegalArgumentException, when item is null.
	 */
	public DBEntity<K> create(DBEntity<K> e) {
		return (DBEntity<K>) dao.create(e);
	}
	
    /**
     * Get item with id.
     * @param id Id of the item to get.
     * @param clazz The class of the entity to retrieve.
     * @return Item, null if is not stored.
     * @throws IllegalArgumentException, id is null.
     */
	public DBEntity<K> retrieve(final K id, final Class<DBEntity<K>> clazz) {
		return (DBEntity<K>) dao.retrieve(id, clazz);
	}

    /**
     * Change the values of the item, except id, to new values.
     * @param e Item to update with the new values.
     * @return Item updated, null if is not stored.
     * @throws IllegalArgumentException, item is null.
     */
	public DBEntity<K> update(DBEntity<K> e) {
		return (DBEntity<K>) dao.update(e);		
	}
    
    /**
     * Delete the item if is stored.
     * @param e The item to delete.
     * @throws IllegalArgumentException, item is null.
     */
	public void delete(final DBEntity<K> e) {
		dao.delete(e);
	}
    
    /**
     * List all elements.
     * @param clazz The class of the entity to retrieve.
     * @return List of items, or an empty list. Never null.
     */
	public List<DBEntity<K>> listAll(final Class<DBEntity<K>> clazz) {
		return dao.listAll(clazz);
	}
	
	/**
	 * Close the db.
	 */
	public void close() {
		handler.close();
	}
	
}
