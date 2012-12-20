package mainBeans;

import java.security.NoSuchAlgorithmException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import options.PasswordManager;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import dataModels.UserModel;

/**
 * 
 * @author KonstantinDobrev
 *
 * This class is designed to take the data
 * from the registration form, validate it,
 * and create rows in the USERS table in the
 * database.
 */
@ManagedBean
public class UserCreator {

	private String username;
	private String password;
	private String repeatedPass;
	private String passHint;
	private String email;
	
	//Setters and getters
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setRepeatedPass(String repeatedPass) {
		this.repeatedPass = repeatedPass;
	}
	
	public String getRepeatedPass() {
		return repeatedPass;
	}
	
	public void setPassHint(String passHint) {
		this.passHint = passHint;
	}
	
	public String getPassHint() {
		return passHint;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	//Validators
	
	public void validateUsername(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String user = (String) value;
		if(user.length() < 3) {
			throw new ValidatorException(new FacesMessage("Username should be at least 3 letters long."));
		}
		else if(!user.matches("^[a-zA-Z]{3,}\\d*$")) {
			throw new ValidatorException(new FacesMessage("Username should have only numbers and letters. "));
		}
	}
	
	public void validatePassword(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String pass = (String) value;
		if(pass.length() < 8) {
			throw new ValidatorException(new FacesMessage("Password should be at least 8 characters long."));
		}
	}
	
	public void validateEmail(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String emailVal = (String) value;
		if(!emailVal.matches("^\\w{3,}\\@\\w+\\.\\w{2,}$")) {
			throw new ValidatorException(new FacesMessage("The email you have entered is not valid."));
		}
	}
	
	/**
	 * Controller method for registration. Takes the input from the registration 
	 * form and adds the data to the database
	 * 
	 * @return the 'success' page confirming the successful registration 
	 * or null if the data from the 'password' and 'repeat password' field does 
	 * not match.
	 */
	public String register() {
		//Comparing the data from the 'password' and 'repeat password' fields
		if(!password.equals(repeatedPass)) {
			FacesMessage fm = new FacesMessage("Passwords you have entered do not match.");
			FacesContext.getCurrentInstance().addMessage("Password matching", fm);
			return null;
		}
		Configuration configuration = new Configuration();
	    configuration.configure();
	    ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
	    SessionFactory factory = configuration.buildSessionFactory(serviceRegistry);
		
		Session regSession = factory.openSession();
		regSession.beginTransaction();
		
		
		UserModel user = new UserModel();
		user.setUsername(username);
		//Creating random salt string for the password
		String salt = PasswordManager.createSalt(64);
		user.setPasswordSalt(salt);
		//Hashing the password and salt and saving the result in the database
		String hashedPass = "";
		try {
			hashedPass = PasswordManager.createHashedPasswordSHA256(password, salt);
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		user.setPasswordHash(hashedPass);
		user.setPasswordHint(passHint);
		user.setEmail(email);
		
		regSession.save(user);
		regSession.getTransaction().commit();
		regSession.close();
		
		return "success";
	}
	
}
