/*
 * Employee
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

public class Emp implements DBEntity<Integer> {

	private Integer id;
	private String name;
	private String surname;
	private boolean active;
	
	/* DBEntity implementation */
	
	@Override
	public String getTableName() {
		return Emp.class.getSimpleName();
	}

	@Override
	public ContentValues getTableContentValues() {
		ContentValues values = new ContentValues();	
		
		values.put(COL_ID, id);
	    values.put("name", name);
	    values.put("surname", surname);
	    // BOOLEAN in 0 (false) and 1 (true)
	    if (active) {
	    	values.put("active", 1);	
	    }
	    else {
	    	values.put("active", 0);
	    }
	    
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
		
		Emp e = new Emp();
		
		if(c == null) {
			throw new IllegalArgumentException("Cursor argument is null");
		}
		
		e.id = Integer.valueOf(c.getInt(c.getColumnIndex(COL_ID)));		
	    e.name= c.getString(c.getColumnIndex("name"));	    
	    e.surname = c.getString(c.getColumnIndex("surname"));
	    // BOOLEAN in 0 (false) and 1 (true)
	    e.active = c.getInt(c.getColumnIndex("active")) == 0 ? false : true;
	    
	    return e;
	    
	}
	
	/* End DBEntity implementation */
	
	/**
	 * Builder.
	 * Compulsory.
	 */
	public Emp() {
		super();
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
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
		Emp other = (Emp) obj;
		if (active != other.active)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Emp [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", surname=");
		builder.append(surname);
		builder.append(", active=");
		builder.append(active);
		builder.append("]");
		return builder.toString();
	}
	
}
