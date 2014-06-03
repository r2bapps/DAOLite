package r2b.apps.test;

import r2b.apps.db.DBManager;
import r2b.apps.model.Dept;
import r2b.apps.model.Emp;
import r2b.apps.model.EmpDept;
import android.database.SQLException;
import android.test.AndroidTestCase;

public class TestDeleteCascadeEmpDept extends AndroidTestCase {

	private DBManager<Integer> dbManager;
	
	private Emp e;
	private Emp aux;

	private Dept d;
	private Dept dAux;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		dbManager = new DBManager<Integer>(getContext());
		
		e = new Emp();
		aux = new Emp();	
		
		d = new Dept();
		dAux = new Dept();
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
		Emp aux = (Emp) dbManager.create(e);
		
		assertTrue(id != aux.getId());	
		
		int idD = -1;		
		d.setKey(idD);
		d.setName("R");		
		Dept dAux = (Dept) dbManager.create(d);
		
		assertTrue(idD != dAux.getId());
		
		EmpDept ed = new EmpDept();
		
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
		EmpDept edAux = (EmpDept) dbManager.create(ed);	
		assertTrue(edAux.getId() != -1);
		
	}	
	
	public void testDeleteEmployee() {				
		int id = -1;		
		e.setKey(id);
		e.setName("R");
		e.setSurname("R B");
		e.setActive(true);		
		Emp aux = (Emp) dbManager.create(e);
		
		assertTrue(id != aux.getId());	
		
		int idD = -1;		
		d.setKey(idD);
		d.setName("R");		
		Dept dAux = (Dept) dbManager.create(d);
		
		assertTrue(idD != dAux.getId());
		
		EmpDept ed = new EmpDept();
		
		
		ed.setId(-1);
		ed.setId_employee(aux.getId());
		ed.setId_department(dAux.getId());
		EmpDept edAux = (EmpDept) dbManager.create(ed);	
		assertTrue(edAux.getId() != -1);
				
		dbManager.delete(e);
		// Delete on cascade
		assertNull(dbManager.retrieve(edAux.getId(), EmpDept.class));
				
	}	
	
	
	public void testDeleteDepartment() {				
		int id = -1;		
		e.setKey(id);
		e.setName("R");
		e.setSurname("R B");
		e.setActive(true);		
		Emp aux = (Emp) dbManager.create(e);
		
		assertTrue(id != aux.getId());	
		
		int idD = -1;		
		d.setKey(idD);
		d.setName("R");		
		Dept dAux = (Dept) dbManager.create(d);
		
		assertTrue(idD != dAux.getId());
		
		EmpDept ed = new EmpDept();
		
		
		ed.setId(-1);
		ed.setId_employee(aux.getId());
		ed.setId_department(dAux.getId());
		EmpDept edAux = (EmpDept) dbManager.create(ed);	
		assertTrue(edAux.getId() != -1);
		
		dbManager.delete(d);
		// Delete on cascade
		assertNull(dbManager.retrieve(edAux.getId(), EmpDept.class));		
		
	}
	
}
