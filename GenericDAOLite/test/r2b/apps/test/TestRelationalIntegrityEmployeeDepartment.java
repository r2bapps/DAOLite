package r2b.apps.test;

import r2b.apps.db.DBManager;
import r2b.apps.model.Department;
import r2b.apps.model.Employee;
import r2b.apps.model.EmployeeDepartment;
import android.database.SQLException;
import android.test.AndroidTestCase;

public class TestRelationalIntegrityEmployeeDepartment extends AndroidTestCase {

	private DBManager<Integer> dbManager;
	
	private Employee e;
	private Employee aux;

	private Department d;
	private Department dAux;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		dbManager = new DBManager<Integer>(getContext());
		
		e = new Employee();
		aux = new Employee();	
		
		d = new Department();
		dAux = new Department();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		dbManager.close();
	}
	
	public void testCreateEmployeeDepartment() {				
		int id = -1;		
		e.setKey(id);
		e.setName("R");
		e.setSurname("R B");
		e.setActive(true);		
		Employee aux = (Employee) dbManager.create(e);
		
		assertTrue(id != aux.getId());	
		
		int idD = -1;		
		d.setKey(idD);
		d.setName("R");		
		Department dAux = (Department) dbManager.create(d);
		
		assertTrue(idD != dAux.getId());
		
		EmployeeDepartment ed = new EmployeeDepartment();
		
		try {
			ed.setId(-1);
			ed.setId_employee(-1);
			ed.setId_department(dAux.getId());
			dbManager.create(ed);
			fail();
		} catch (SQLException ex) {
		}
				
		try {
			ed.setId(-1);
			ed.setId_employee(aux.getId());
			ed.setId_department(-1);				
			dbManager.create(ed);
			fail();
		} catch (SQLException ex) {
		}
		
		
		ed.setId(-1);
		ed.setId_employee(aux.getId());
		ed.setId_department(dAux.getId());
		EmployeeDepartment edAux = (EmployeeDepartment) dbManager.create(ed);	
		assertTrue(edAux.getId() != -1);
		
	}
	
	public void testUpdateEmployeeDepartment() {				
		int id = -1;		
		e.setKey(id);
		e.setName("R");
		e.setSurname("R B");
		e.setActive(true);		
		Employee aux = (Employee) dbManager.create(e);
		
		assertTrue(id != aux.getId());	
		
		int idD = -1;		
		d.setKey(idD);
		d.setName("R");		
		Department dAux = (Department) dbManager.create(d);
		
		assertTrue(idD != dAux.getId());
		
		EmployeeDepartment ed = new EmployeeDepartment();
		
		
		ed.setId(-1);
		ed.setId_employee(aux.getId());
		ed.setId_department(dAux.getId());
		EmployeeDepartment edAux = (EmployeeDepartment) dbManager.create(ed);	
		assertTrue(edAux.getId() != -1);
		
		try {
			edAux.setId_employee(-1);				
			dbManager.update(edAux);
			fail();
		} catch (SQLException ex) {
		}
		
		try {
			edAux.setId_department(-1);				
			dbManager.update(edAux);
			fail();
		} catch (SQLException ex) {
		}
		
	}	
	
	public void testDeleteEmployee() {				
		int id = -1;		
		e.setKey(id);
		e.setName("R");
		e.setSurname("R B");
		e.setActive(true);		
		Employee aux = (Employee) dbManager.create(e);
		
		assertTrue(id != aux.getId());	
		
		int idD = -1;		
		d.setKey(idD);
		d.setName("R");		
		Department dAux = (Department) dbManager.create(d);
		
		assertTrue(idD != dAux.getId());
		
		EmployeeDepartment ed = new EmployeeDepartment();
		
		
		ed.setId(-1);
		ed.setId_employee(aux.getId());
		ed.setId_department(dAux.getId());
		EmployeeDepartment edAux = (EmployeeDepartment) dbManager.create(ed);	
		assertTrue(edAux.getId() != -1);
		
		try {			
			dbManager.delete(e);
			fail();
		} catch (SQLException ex) {
		}
		
	}	
	
	
	public void testDeleteDepartment() {				
		int id = -1;		
		e.setKey(id);
		e.setName("R");
		e.setSurname("R B");
		e.setActive(true);		
		Employee aux = (Employee) dbManager.create(e);
		
		assertTrue(id != aux.getId());	
		
		int idD = -1;		
		d.setKey(idD);
		d.setName("R");		
		Department dAux = (Department) dbManager.create(d);
		
		assertTrue(idD != dAux.getId());
		
		EmployeeDepartment ed = new EmployeeDepartment();
		
		
		ed.setId(-1);
		ed.setId_employee(aux.getId());
		ed.setId_department(dAux.getId());
		EmployeeDepartment edAux = (EmployeeDepartment) dbManager.create(ed);	
		assertTrue(edAux.getId() != -1);
		
		try {				
			dbManager.delete(d);
			fail();
		} catch (SQLException ex) {
		}
		
	}
	
}
