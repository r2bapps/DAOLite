package r2b.apps.test;

import r2b.apps.db.DBManager;
import r2b.apps.model.Department;
import r2b.apps.model.Employee;
import android.test.AndroidTestCase;

public class TestGenericDaoImpl extends AndroidTestCase {

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

	public void testCreateEmployee() {				
		int id = -1;
		
		e.setKey(id);
		e.setName("R");
		e.setSurname("R B");
		e.setActive(true);
		
		Employee aux = (Employee) dbManager.create(e);
		int auxId = aux.getId();
		// Inserted "id" is autoincremental.
		assertTrue(id != aux.getId());	
		// Inserted is equals object as returned. Create method sets "id"
		assertEquals(e, aux);
		// Inserted is the same object as returned. Create method returns the same reference with setted "id"
		assertSame(e, aux);
			
		auxId = aux.getId();
		aux = (Employee) dbManager.create(e);
		
		// Can insert the same object because "id" is autoincremental.
		assertNotNull(aux);		
		// Inserted "id" is autoincremental.
		assertTrue(aux.getId() != auxId);	
				
		
		// Create null crashes
		try {
			dbManager.create(null);
			fail();
		} catch (IllegalArgumentException e) {}
	}
	
	public void testCreateDepartment() {				
		int id = -1;
		
		d.setKey(id);
		d.setName("R");
		
		Department aux = (Department) dbManager.create(d);		
		int auxId = aux.getId();
		// Inserted "id" is autoincremental.
		assertTrue(id != aux.getId());	
		// Inserted is equals object as returned. Create method sets "id"
		assertEquals(d, aux);
		// Inserted is the same object as returned. Create method returns the same reference with setted "id"
		assertSame(d, aux);
			
		auxId = aux.getId();
		aux = (Department) dbManager.create(d);
		
		// Can insert the same object because "id" is autoincremental.
		assertNotNull(aux);		
		// Inserted "id" is autoincremental.
		assertTrue(aux.getId() != auxId);	
				
		
		// Create null crashes
		try {
			dbManager.create(null);
			fail();
		} catch (IllegalArgumentException e) {}
	}

	public void testRetrieveEmployee() {				
		// Insert data to test
		dbManager.create(e);
		
		// Tests		
		assertEquals(false, ((Employee)dbManager.retrieve(e.getKey(), Employee.class)).isActive());
		e.setActive(true);
		dbManager.create(e);
		assertEquals(true, ((Employee)dbManager.retrieve(e.getKey(), Employee.class)).isActive());
		
		// Retrieve null id crashes
		try {
			dbManager.retrieve(null, Employee.class);
			fail();
		} catch (IllegalArgumentException e) {}
		// Retrieve not stored id return null employee
		assertNull(dbManager.retrieve(-1, Employee.class));
	}

	public void testRetrieveDepartment() {				
		// Insert data to test
		dbManager.create(d);
		
		// Tests		
		assertEquals(null, ((Department)dbManager.retrieve(d.getKey(), Department.class)).getName());

		d.setName("jooola");
		dbManager.create(d);
		assertEquals("jooola", ((Department)dbManager.retrieve(d.getKey(), Department.class)).getName());
		
		// Retrieve null id crashes
		try {
			dbManager.retrieve(null, Department.class);
			fail();
		} catch (IllegalArgumentException d) {}
		// Retrieve not stored id return null Department
		assertNull(dbManager.retrieve(-1, Department.class));
	}

	
	public void testUpdateEmployee() {		
		// Insert data to test
		dbManager.create(e);
		
		// Tests
		
		// Update the same return the same
		assertSame(e, dbManager.update(e));
		
		// Update not stored crashes		
		try {
			assertNull(dbManager.update(aux));
			fail();
		} catch (IllegalArgumentException e) {}
		
		// Update null crashes
		try {
			dbManager.update(null);
			fail();
		} catch (IllegalArgumentException e) {}
	}

	public void testUpdateDepartment() {		
		// Insert data to test
		dbManager.create(d);
		
		// Tests
		
		// Update the same return the same
		assertSame(d, dbManager.update(d));
		
		// Update not stored crashes		
		try {
			assertNull(dbManager.update(dAux));
			fail();
		} catch (IllegalArgumentException e) {}
		
		// Update null crashes
		try {
			dbManager.update(null);
			fail();
		} catch (IllegalArgumentException e) {}
	}
	
	public void testDeleteEmployee() {		
		// Insert data to test
		dbManager.create(e);
		
		// Tests
		
		// Delete stored id return void
		dbManager.delete(e);
		assertNull(dbManager.retrieve(e.getKey(), Employee.class));
		
		// Delete not stored employee crashes
		try {
			dbManager.delete(aux);
			fail();
		} catch (IllegalArgumentException e) {}
		// Delete null crash
		try {
			dbManager.delete(null);
			fail();
		} catch (IllegalArgumentException e) {}
	}

	public void testDeleteDepartment() {		
		// Insert data to test
		dbManager.create(d);
		
		// Tests
		
		// Delete stored id return void
		dbManager.delete(d);
		assertNull(dbManager.retrieve(d.getKey(), Department.class));
		
		// Delete not stored employee crashes
		try {
			dbManager.delete(dAux);
			fail();
		} catch (IllegalArgumentException e) {}
		// Delete null crash
		try {
			dbManager.delete(null);
			fail();
		} catch (IllegalArgumentException e) {}
	}
	
	public void testListAllEmployee() {	
		// ListAll return empty list
		assertNotNull(dbManager.listAll(Employee.class));
		assertTrue(dbManager.listAll(Employee.class).isEmpty());
		
		// ListAll return one item
		dbManager.create(e);
		assertTrue(dbManager.listAll(Employee.class).size() == 1);
		
		// ListAll return two item
		dbManager.create(aux);
		assertTrue(dbManager.listAll(Employee.class).size() == 2);
		
		// ListAll return one item
		dbManager.delete(e);
		assertTrue(dbManager.listAll(Employee.class).size() == 1);
		
		// ListAll return empty list
		dbManager.delete(aux);
		assertNotNull(dbManager.listAll(Employee.class));
		assertTrue(dbManager.listAll(Employee.class).isEmpty());
	}

	public void testListAllDepartment() {	
		// ListAll return empty list
		assertNotNull(dbManager.listAll(Department.class));
		assertTrue(dbManager.listAll(Department.class).isEmpty());
		
		// ListAll return one item
		dbManager.create(d);
		assertTrue(dbManager.listAll(Department.class).size() == 1);
		
		// ListAll return two item
		dbManager.create(dAux);
		assertTrue(dbManager.listAll(Department.class).size() == 2);
		
		// ListAll return one item
		dbManager.delete(d);
		assertTrue(dbManager.listAll(Department.class).size() == 1);
		
		// ListAll return empty list
		dbManager.delete(dAux);
		assertNotNull(dbManager.listAll(Department.class));
		assertTrue(dbManager.listAll(Department.class).isEmpty());
	}

}
