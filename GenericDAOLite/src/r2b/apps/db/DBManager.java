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

import java.util.ArrayList;
import java.util.List;

import r2b.apps.db.dao.EmployeeDao;
import r2b.apps.db.dao.EmployeeDaoImpl;
import r2b.apps.model.Employee;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class DBManager {

	private final SQLiteDatabase db;
	private final DatabaseHandler handler;
	
	// XXX Add here your DAOs variables
	private EmployeeDao employeeDao;
	
	/**
	 * Builder.
	 * @param context The application context.
	 */
	public DBManager(final Context context) {
		handler = DatabaseHandler.init(context.getApplicationContext());
		
		db = DatabaseHandler.getDatabase();
		
		// XXX Add here your DAOs instantiations
		employeeDao = new EmployeeDaoImpl(db);
	}
	
	public DBEntity create(DBEntity e) {
		if (e instanceof Employee) {
			return employeeDao.create((Employee) e);	
		}
		return null;		
	}
	
	public DBEntity retrieve(final Integer id, final Class<? extends DBEntity> clazz) {
		if (clazz.getSimpleName().equalsIgnoreCase(Employee.class.getSimpleName())) {
			return employeeDao.retrieve(id, Employee.class);	
		}
		return null;
	}

	public DBEntity update(DBEntity e) {
		if (e instanceof Employee) {
			return employeeDao.update((Employee) e);	
		}
		return null;		
	}
    
	public void delete(final DBEntity e) {
		if (e instanceof Employee) {
			employeeDao.delete((Employee) e);	
		}
	}
    
	public List<DBEntity> listAll(final Class<? extends DBEntity> clazz) {
		List<DBEntity> list = new ArrayList<>();		
		if (clazz.getSimpleName().equalsIgnoreCase(Employee.class.getSimpleName())) {
			list.addAll(employeeDao.listAll(Employee.class));	
		}
		return list;
	}
	
	/**
	 * Clear all db data.
	 */
	public void clear() {
		handler.clear();
	}
	
	/**
	 * Close the db.
	 */
	public void close() {
		handler.close();
	}
	
}
