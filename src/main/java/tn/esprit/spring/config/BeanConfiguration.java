package tn.esprit.spring.config;

import tn.esprit.spring.repository.EmployeRepository;

//@Configuration
//@ComponentScan(basePackages = "tn.esprit.spring")
public class BeanConfiguration {
	//@Bean 
	//@Qualifier("service") 
	public EmployeRepository getEmployeRepository(EmployeRepository employeRepository){ 
		return employeRepository;
	}
}