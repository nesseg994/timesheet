package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class EmployeServiceImpl implements IEmployeService {

	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	ContratRepository contratRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;

	@Override
	public Employe authenticate(String login, String password) {
		return employeRepository.getEmployeByEmailAndPassword(login, password);
	}

	@Override
	public int addOrUpdateEmploye(Employe employe) {
		employeRepository.save(employe);
		return employe.getId();
	}
	
	@Override
	public void addOrUpdateEmployeJPQL(int id, String nom, String prenom, String email, boolean actif, String pwd, String role) {
		employeRepository.insertEmployeJPQL(id, nom, prenom, email, actif, pwd, role);
	}

	@Override
	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		Employe employe = employeRepository.findById(employeId).orElse(null);
		
		if(employe != null) {
			employe.setEmail(email);
			employeRepository.save(employe);
		}

	}

	@Override
	@Transactional	
	public void affecterEmployeADepartement(int employeId, int depId) {
		Departement depManagedEntity = deptRepoistory.findById(depId).orElse(null);
		Employe employeManagedEntity = employeRepository.findById(employeId).orElse(null);

		if(depManagedEntity != null) {
			if(depManagedEntity.getEmployes() == null){

				List<Employe> employes = new ArrayList<>();
				employes.add(employeManagedEntity);
				depManagedEntity.setEmployes(employes);
			} else{

				depManagedEntity.getEmployes().add(employeManagedEntity);
			}

			// à ajouter? 
			deptRepoistory.save(depManagedEntity); 
		}

	}
	@Override
	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId)
	{
		Departement dep = deptRepoistory.findById(depId).orElse(null);

		if(dep != null) {
			int employeNb = dep.getEmployes().size();
			for(int index = 0; index < employeNb; index++){
				if(dep.getEmployes().get(index).getId() == employeId){
					dep.getEmployes().remove(index);
					break; //a revoir
				}
			}
		}
	} 
	
	// Tablesapce (espace disque) 

	@Override
	public int ajouterContrat(Contrat contrat) {
		contratRepoistory.save(contrat);
		return contrat.getReference();
	}

	@Override
	public void affecterContratAEmploye(int contratId, int employeId) {
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).orElse(null);
		Employe employeManagedEntity = employeRepository.findById(employeId).orElse(null);

		if (contratManagedEntity != null) {
			contratManagedEntity.setEmploye(employeManagedEntity);
			contratRepoistory.save(contratManagedEntity);
		}
	}

	@Override
	public String getEmployePrenomById(int employeId) {
		Employe employeManagedEntity = employeRepository.findById(employeId).orElse(null);
		if (employeManagedEntity != null) 
			return employeManagedEntity.getPrenom();
		else
			return null;
	}
	
	@Override
	public Employe getEmployeById(int employeId) {
		return employeRepository.findById(employeId).orElse(null); // get() can throw exception if employe is not found
	}
	
	@Override
	public int verifyEmployeExistsById(int employeId) {
		return employeRepository.empExists(employeId);
	}
	 
	@Override
	public void deleteEmployeById(int employeId)
	{
		Employe employe = employeRepository.findById(employeId).orElse(null);
		
		//Desaffecter l'employe de tous les departements
		//c'est le bout master qui permet de mettre a jour
		//la table d'association
		if(employe != null) {
			for(Departement dep : employe.getDepartements()){
				dep.getEmployes().remove(employe);
			}
			employeRepository.delete(employe);
		}

	}

	@Override
	public void deleteContratById(int contratId) {
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).orElse(null);
		if (contratManagedEntity != null) {
			contratRepoistory.delete(contratManagedEntity);
		}

	}

	@Override
	public int getNombreEmployeJPQL() {
		return employeRepository.countemp();
	}

	@Override
	public List<String> getAllEmployeNamesJPQL() {
		return employeRepository.employeNames();

	}

	@Override
	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	@Override
	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}
	@Override
	public void deleteAllContratJPQL() {
		employeRepository.deleteAllContratJPQL();
	}

	@Override
	public float getSalaireByEmployeIdJPQL(int employeId) {
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	@Override
	public Double getSalaireMoyenByDepartementId(int departementId) {
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}

	@Override
	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
			Date dateFin) {
		return timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
	}

	@Override
	public List<Employe> getAllEmployes() {
		return (List<Employe>) employeRepository.findAll();
	}

}
