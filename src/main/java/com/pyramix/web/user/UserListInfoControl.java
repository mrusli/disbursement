package com.pyramix.web.user;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;

import com.pyramix.domain.user.User;
import com.pyramix.persistence.user.dao.UserDao;
import com.pyramix.web.common.GFCBaseController;

public class UserListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3358715032341965320L;

	private UserDao userDao;
	
	private Label userNameLabel;
	
	public void onCreate$userListInfoWin(Event event) throws Exception {
		User user =	getUserDao().findUserById(6L);
		
		userNameLabel.setValue("User : ["+user.getId()+"] "+user.getUser_name());
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
}
