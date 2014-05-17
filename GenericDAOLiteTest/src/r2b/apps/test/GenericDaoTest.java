package r2b.apps.test;

import r2b.apps.db.DBManager;
import r2b.apps.model.Employee;
import android.test.AndroidTestCase;

public class GenericDaoTest extends AndroidTestCase {

	private DBManager dbManager = new DBManager(getContext());
	private Employee e;
	private Employee aux;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		dbManager.clear();
		
		e = new Employee();
		aux = new Employee();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCreate() {
		
		int id = -1;
		
		e.setId(id);
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
	
	public void testUpdate() {
		// Insert data to test
		dbManager.create(e);
		
		// Tests
		
		// Update the same return the same
		assertSame(e, dbManager.update(e));
		
		// Update not stored employee return null
		assertNull(dbManager.update(aux));
		
		// Update null crashes
		try {
			dbManager.update(null);
			fail();
		} catch (IllegalArgumentException e) {}
	}
	
	public void testDelete() {
		// Insert data to test
		dbManager.create(e);
		
		// Tests
		
		// Delete stored id return void
		dbManager.delete(e);
		assertNull(dbManager.retrieve(e.getId(), Employee.class));
		
		// Delete not stored employee return void and not crash
		try {
			dbManager.delete(aux);
		} catch (IllegalArgumentException e) {
			fail();
		}
		// Delete null id crash
		try {
			dbManager.delete(null);
			fail();
		} catch (IllegalArgumentException e) {}
		
	}
	
	public void testRetrieve() {
		// Insert data to test
		dbManager.create(e);
		
		// Tests		
		assertEquals(false, ((Employee)dbManager.retrieve(e.getId(), Employee.class)).isActive());
		e.setActive(true);
		dbManager.create(e);
		assertEquals(true, ((Employee)dbManager.retrieve(e.getId(), Employee.class)).isActive());
		
		// Retrieve null id crashes
		try {
			dbManager.retrieve(null, Employee.class);
			fail();
		} catch (IllegalArgumentException e) {}
		// Retrieve not stored id return null employee
		assertNull(dbManager.retrieve(-1, Employee.class));
	}
	
	public void testListAll() {
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

}
