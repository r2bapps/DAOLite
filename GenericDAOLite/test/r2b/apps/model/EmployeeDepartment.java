/*
 * Department
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

package r2b.apps.model;

import r2b.apps.db.DBEntity;
import android.content.ContentValues;
import android.database.Cursor;

public class EmployeeDepartment implements DBEntity<Integer> {

	private Integer id;
	private Integer id_employee;
	private Integer id_department;
	
	/* DBEntity implementation */
	
	@Override
	public String getTableName() {
		return EmployeeDepartment.class.getSimpleName();
	}

	@Override
	public ContentValues getTableContentValues() {
		ContentValues values = new ContentValues();	
		
		values.put(COL_ID, id);
	    values.put("id_employee", id_employee);
	    values.put("id_department", id_department);
	    
	    return values;
	}
	
	@Override
	public Integer getKey() {
		return id;
	}

	@Override
	public void setKey(final Integer id) {
		this.id = id;
	}
	
	public DBEntity<Integer> valueOf(final Cursor c) {
		
		EmployeeDepartment e = new EmployeeDepartment();
		
		if(c == null) {
			throw new IllegalArgumentException("Cursor argument is null");
		}
		
		e.id = Integer.valueOf(c.getInt(c.getColumnIndex(COL_ID)));		
		e.id_employee = Integer.valueOf(c.getInt(c.getColumnIndex("id_employee")));	
		e.id_department = Integer.valueOf(c.getInt(c.getColumnIndex("id_department")));	
	    
	    return e;
	    
	}
	
	/* End DBEntity implementation */
	
	/**
	 * Builder.
	 * Compulsory.
	 */
	public EmployeeDepartment() {
		super();
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId_employee() {
		return id_employee;
	}

	public void setId_employee(Integer id_employee) {
		this.id_employee = id_employee;
	}

	public Integer getId_department() {
		return id_department;
	}

	public void setId_department(Integer id_department) {
		this.id_department = id_department;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((id_department == null) ? 0 : id_department.hashCode());
		result = prime * result
				+ ((id_employee == null) ? 0 : id_employee.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeDepartment other = (EmployeeDepartment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (id_department == null) {
			if (other.id_department != null)
				return false;
		} else if (!id_department.equals(other.id_department))
			return false;
		if (id_employee == null) {
			if (other.id_employee != null)
				return false;
		} else if (!id_employee.equals(other.id_employee))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EmployeeDepartment [id=");
		builder.append(id);
		builder.append(", id_employee=");
		builder.append(id_employee);
		builder.append(", id_department=");
		builder.append(id_department);
		builder.append("]");
		return builder.toString();
	}

}
