package com.pyramix.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pyramix.domain.user.User;
import com.pyramix.domain.user.UserRole;

public class UserSecurityDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4459827020269594458L;

	private User user;
	
	private final Logger log = Logger.getLogger(UserSecurityDetails.class);
	
	public UserSecurityDetails(User user) {
		super();
		
		log.info("Assigned registered user (from db) to UserSecurityDetails ["+user.getUser_name()+"]");
		
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return getGrantedAuthorities(getUser());
	}

	private Collection<GrantedAuthority> getGrantedAuthorities(User user) {
		
		List<GrantedAuthority> rolesGrantedAuthorityList = new ArrayList<GrantedAuthority>();
		
		for (UserRole userRole : user.getUser_roles()) {
			rolesGrantedAuthorityList.add(new SimpleGrantedAuthority(userRole.getRole_name()));			
		}
		
		log.info("Login User Roles : "+rolesGrantedAuthorityList.toString());
		
		return rolesGrantedAuthorityList;
	}	
	
	
	@Override
	public String getPassword() {
		log.info("Login User Password = "+getUser().getUser_password());
		
		return getUser().getUser_password();
	}

	@Override
	public String getUsername() {
		log.info("Login User Name = "+getUser().getUser_name());
		
		return getUser().getUser_name();
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		log.info("Login User is " + (getUser().isEnabled() ? "Enabled" : "Disabled"));
		
		return getUser().isEnabled();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
