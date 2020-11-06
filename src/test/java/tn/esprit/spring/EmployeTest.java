package tn.esprit.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.services.EmployeServiceImpl;
import tn.esprit.spring.services.IEmployeService;

@SpringBootTest(classes = { EmployeServiceImpl.class })
@AutoConfigureTestDatabase(replace = Replace.NONE) // To run the test on the actual DB
@Rollback(false) // So that the data will be committed to the DB so that it's available for the subsequent tests
@RunWith( SpringJUnit4ClassRunner.class ) // Else service == null
//@RunWith(SpringRunner.class)
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { BeanConfiguration.class })
@ContextConfiguration(classes = { TimesheetApplication.class }) // To know where the Repo Bean def is for exp
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeTest {     
    @Autowired
    IEmployeService service;
    
    int empId;
    
    private static final Logger l = Logger.getLogger(EmployeTest.class);
            	
    //@org.testng.annotations.BeforeClass // BeforeClass of JUnit needs static methond
    @Before
	public void init() {
		empId = 4;
	}

	@Test
	public void aTestAddEmploye() {
		l.info("a");  // for the order of execution
		service.addOrUpdateEmployeJPQL(empId, "nesrine", "ness", "nesrine.eg94@gmail.com", true, "pwd", Role.INGENIEUR.name());
		
		l.info("Service Add " + empId);
		
        assertThat(empId).isGreaterThan(0);
	}
	
	@Test
	public void bTestFindEmployeById() {
		l.info("b");
		l.info("Service Find " + empId);
		
	    String empName = service.getEmployePrenomById(empId);
	    
	    assertThat(empName).isEqualTo("nesrine");
	}
	
	
	@Test
	public void dTestListEmps() {
		l.info("d");
		l.info("Service List");
		
	    List<Employe> emps = service.getAllEmployes();
	    assertThat(emps).size().isGreaterThan(0);
	}

	@Test
	public void cTestUpdateEmploye() {
		l.info("c");
		l.info("Service Update " + empId);
		
	    Employe emp = service.getEmployeById(empId);
	    emp.setNom("el ghoul");
	     
	    service.addOrUpdateEmploye(emp);
	     
	    Employe updatedEmploye = service.getEmployeById(empId);
	     
	    assertThat(updatedEmploye.getNom()).isEqualTo("el ghoul");
	}
	
	
	@Test
	public void eTestDeleteEmploye() {
		l.info("e");
		l.info("Service Delete " + empId);
		
	    Employe emp = service.getEmployeById(empId);
	     
	    service.deleteEmployeById(emp.getId());
	    
	    int verifyExists = service.verifyEmployeExistsById(empId);
	     
	    assertThat(verifyExists).isEqualTo(0);
	}
}


