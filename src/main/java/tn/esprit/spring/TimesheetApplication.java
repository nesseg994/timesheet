package tn.esprit.spring;

import java.util.EnumSet;

import javax.faces.webapp.FacesServlet;
import javax.servlet.DispatcherType;

import org.ocpsoft.rewrite.servlet.RewriteFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import tn.esprit.spring.config.LoginFilter;
import tn.esprit.spring.repository.EmployeRepository;

@SpringBootApplication
@EnableAutoConfiguration
//@EnableJpaRepositories("tn.esprit.spring.repository")
//@EntityScan("tn.esprit.spring.entities")
//@ComponentScan("tn.esprit.spring.services")
public class TimesheetApplication {

	public static void main(String[] args) {SpringApplication.run(TimesheetApplication.class, args);}

	@Bean
	public ServletRegistrationBean<FacesServlet> servletRegistrationBean() {
		FacesServlet servlet = new FacesServlet();
		return new ServletRegistrationBean<>(servlet, "*.jsf"); }

	@Bean
	public FilterRegistrationBean<RewriteFilter> rewriteFilter() {
		FilterRegistrationBean<RewriteFilter> rwFilter = new FilterRegistrationBean<>(new RewriteFilter());
		rwFilter.setDispatcherTypes(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR));
		rwFilter.addUrlPatterns("/*");
		return rwFilter;
	}


	@Bean
	public FilterRegistrationBean<LoginFilter> loginFilter() {
		FilterRegistrationBean<LoginFilter> registration = new FilterRegistrationBean<>();
		registration.addUrlPatterns("/pages/*");
		registration.setFilter(new LoginFilter());
		return registration;
	}
	
	@Bean  // Need it else cannot find repository
	public EmployeRepository getEmployeRepository(EmployeRepository employeRepository){ 
		return employeRepository;
	}
 
}

