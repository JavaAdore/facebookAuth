package facebookAuth;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.util.SocialAuthUtil;

@ManagedBean(name="userSession") 
@SessionScoped
public class MyUserSessionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private SocialAuthManager manager;
	private String originalURL = "http://localhost:8080/facebookAuth";
	private String providerID ;
	private Profile profile;
    private Set<Entry<String, String>> entries;


	private Map<String,String> profileValues ;
	@PostConstruct
	public void init()
	{
		
		profileValues= new TreeMap<String, String>();
		entries= new HashSet<Entry<String, String>>();
	}
	
	public Set<Entry<String, String>> getEntries() {
		return entries;
	}

	public void setEntries(Set<Entry<String, String>> entries) {
		this.entries = entries;
	}

	public Map<String, String> getProfileValues() {
		return profileValues;
	}

	public void setProfileValues(Map<String, String> profileValues) {
		this.profileValues = profileValues;
	}

	public MyUserSessionBean() {
	
	}

	public void socialConnect()  {
		try
		{
		// Put your keys and secrets from the providers here
		Properties props = System.getProperties();
		props.put("graph.facebook.com.consumer_key", "362611210580435");
		props.put("graph.facebook.com.consumer_secret", "88a9926f85751c06318658533d3742df");
		// Define your custom permission if needed
		props.put("graph.facebook.com.custom_permissions",
				"publish_stream,email,user_birthday,user_location,offline_access");
		// Initiate required components
		SocialAuthConfig config = SocialAuthConfig.getDefault();
		config.load(props);
		manager = new SocialAuthManager();
		manager.setSocialAuthConfig(config);
		// 'successURL' is the page you'll be redirected to on successful login
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		String successURL = "http://localhost:8080/facebookAuth"
				+ "/socialLoginSuccess.xhtml";
		String authenticationURL = manager.getAuthenticationUrl(providerID,
				successURL);
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect(authenticationURL);
		}catch(Exception ex)
		{
			
			ex.printStackTrace();
		}
	}

	public void pullUserInfo() {
		try {
			// Pull user's data from the provider
			ExternalContext externalContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			HttpServletRequest request = (HttpServletRequest) externalContext
					.getRequest();
			Map map = SocialAuthUtil.getRequestParametersMap(request);
			if (this.manager != null) {
				AuthProvider provider = manager.connect(map);
				this.profile = provider.getUserProfile();
				// Do what you want with the data (e.g. persist to the database,
				// etc.)
				
				if(profile !=null)
				{
				  profileValues=	Utils.objectToMap(profile);
				  entries =  profileValues.entrySet();
				}
				System.out.println("User's Social profile: " + profile);
				// Redirect the user back to where they have been before logging
				// in
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(originalURL);
			} else
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								externalContext.getRequestContextPath()
										+ "home.xhtml");
		} catch (Exception ex) {
			System.out.println("UserSession - Exception: " + ex.toString());
		}
	}

	public void logOut() {
		try {
			// Disconnect from the provider
			manager.disconnectProvider(providerID);
			// Invalidate session
			ExternalContext externalContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			HttpServletRequest request = (HttpServletRequest) externalContext
					.getRequest();
			this.invalidateSession(request);
			// Redirect to home page
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.redirect(
							externalContext.getRequestContextPath()
									+ "index.xhtml");
		} catch (IOException ex) {
			System.out.println("UserSessionBean - IOException: "
					+ ex.toString());
		}
	}

	public void invalidateSession(HttpServletRequest request) {
		if (request != null) {
			HttpSession session = request.getSession();
			if (session != null) {
				session.invalidate();
			}

		}

	}

	public SocialAuthManager getManager() {
		return manager;
	}

	public void setManager(SocialAuthManager manager) {
		this.manager = manager;
	}

	public String getOriginalURL() {
		return originalURL;
	}

	public void setOriginalURL(String originalURL) {
		this.originalURL = originalURL;
	}

	public String getProviderID() {
		return providerID;
	}

	public void setProviderID(String providerID) {
		this.providerID = providerID;
	}

	public Profile getProfile() 
	{
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
}
