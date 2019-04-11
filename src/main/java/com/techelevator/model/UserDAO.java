package com.techelevator.model;

public interface UserDAO {

	public void saveUser(String userName, String password, boolean isEmployee);

	public boolean searchForUsernameAndPassword(String userName, String password);

	public void updatePassword(String userName, String password);

	public Object getUserByUserName(String userName);
	
	public boolean seeIfUserIsEmployee(String userName);

}
