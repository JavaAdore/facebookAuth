package facebookAuth;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;


@ManagedBean
@ViewScoped
public class LoginHandlerBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private String userName;
	private String password;
	
	@ManagedProperty(value="#{userSession}") 
	private MyUserSessionBean userSession;
	
	
	
	public MyUserSessionBean getUserSession() {
		return userSession;
	}

	public void setUserSession(MyUserSessionBean userSession) {
		this.userSession = userSession;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
	

}
