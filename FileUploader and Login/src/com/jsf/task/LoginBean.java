package com.jsf.task;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import com.jsf.task.db.operations.*;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
    private String password;
    private boolean loggedIn;
    private UserDao userDao = new UserDao();

    public String login() {
    	System.out.println("in Login");
        if (userDao.validateUser(username, password)) {
        	System.out.println("Logged in");
 
            loggedIn = true;
            
            System.out.println("Getting User Data");
            User user = userDao.getUserData(username);

            // Store the user data in the session
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
            session.setAttribute("user", user);
            return "dashboard.xhtml?faces-redirect=true"; // Redirect to a success page
        } else {
        	System.out.println("not Logged in");
            loggedIn = false;
            return "login.xhtml?faces-redirect=true"; // Redirect to a success page
        }
    }

    public String logout() {
    	System.out.println("logged out");
        loggedIn = false;
        username= "";
        password= "";
        return "login.xhtml?faces-redirect=true"; // Redirect to a success page
    }

    // Getters and setters for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getters and setters for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for loggedIn
    public boolean isLoggedIn() {
        return loggedIn;
    }
    
    // Setter for loggedIn
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}