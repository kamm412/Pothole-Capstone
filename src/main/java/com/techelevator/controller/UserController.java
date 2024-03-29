package com.techelevator.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.techelevator.model.MailHelperClass;
import com.techelevator.model.Pothole;
import com.techelevator.model.PotholeDAO;
import com.techelevator.model.User;
import com.techelevator.model.UserDAO;

@Controller
public class UserController {

	private UserDAO userDAO;
	private PotholeDAO potholeDAO;

	@Autowired
	public UserController(UserDAO userDAO, PotholeDAO potholeDAO) {
		this.userDAO = userDAO;
		this.potholeDAO = potholeDAO;
	}

	
	//Handling signup page
	
//	@RequestMapping(path="/Users/new", method=RequestMethod.GET)
//	public String displayNewUserForm(ModelMap modelHolder) {
//		if( ! modelHolder.containsAttribute("user")) {
//			modelHolder.addAttribute("user", new User());
//		}
//		return "Users/newUser";
//	}

	@RequestMapping(path="/Users/new", method=RequestMethod.GET)
	public String displayNewUserForm(ModelMap modelHolder) {
		if( ! modelHolder.containsAttribute("user")) {
			modelHolder.addAttribute("user", new User());
		}
		return "Users/newUser";
	}
	
	@RequestMapping(path="/Users/new", method=RequestMethod.POST)
	public String displayDashBoardForNewlyRegisteredUser(@RequestParam(defaultValue = "false") boolean checkbox,
			@Valid @ModelAttribute User user, BindingResult result, ModelMap modelHolder, RedirectAttributes flash, HttpSession session) throws UnsupportedEncodingException, MessagingException {
//		if( ! modelHolder.containsAttribute("user")) {
//			modelHolder.addAttribute("user", new User());
//		}

	    if (checkbox) {
	    	System.out.println("Checkbox is checked");
	    	user.setEmployee(true);
	    }
	    else {
	    	System.out.println("Checkbox is UNCHECKED");
	    	user.setEmployee(false);
	    }
	    
	    //Uncomment this line to send a confirmation email every time a user registers.
	    //MailHelperClass.sendMail("PotholeVania", user.getEmail(), "Welcome To PotholeVania", "Welcome to PotholeVania "+user.getUserName()+"!\nYour password is: "+user.getPassword());

		userDAO.saveUser(user.getUserName(), user.getEmail(), user.getPhone(), user.getPassword(), user.isEmployee());
		User loggedUser = userDAO.searchForUsernameAndPassword(user.getUserName(), user.getPassword());
		
		session.setAttribute("user", loggedUser);

		if(loggedUser.isEmployee()) {
			return "redirect:/Users/employeeDashboard";

			    }
			    else {
			        return "redirect:/Users/userDashboard";
			    }
	
	}
	
	
    //Handling login Link
	
	@RequestMapping(path="/Users/login", method=RequestMethod.GET)
	public String displayLoginUser(ModelMap modelHolder) {
		return "Users/login";
	}

	// post method after submitting the login 
	
	@RequestMapping(path="/Users/login", method=RequestMethod.POST)
	public String createUser(@Valid @ModelAttribute User user, BindingResult result, 
			RedirectAttributes flash, HttpSession session) {
//		if(result.hasErrors()) {
//			flash.addFlashAttribute("user", user);
//			flash.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "user", result);
//			return "redirect:/Users/newUser";
//		}

		

		//////////////
		User loggedUser = userDAO.searchForUsernameAndPassword(user.getUserName(), user.getPassword());
		
		//if(loogedUser)
		if(loggedUser.getUserName() == null) {
			System.out.println("This user is not in the database");
			flash.addFlashAttribute("UserNotInTheDataBase", "You need to be a registered user to login!");
			return "redirect:/Users/login";
			
		}
		session.setAttribute("user", loggedUser);
		
		//////////////
		
//		userDAO.saveUser(user.getUserName(), user.getEmail(), user.getPhone(), user.getPassword(), user.isEmployee());

		
		//Need to start the session with a user
		
		if(loggedUser.isEmployee()) {
			return "redirect:/Users/employeeDashboard";

			    }
			    else {
			        return "redirect:/Users/userDashboard";
			    }
//
	}
	
//	//added new set of code -create user method
//	@RequestMapping(path="/User/login", method= RequestMethod.POST)
//	public String createUser(@RequestParam String userName, @RequestParam String password) {
//	userDAO.saveUser(userName, password);
//	return "redirect:/Users/userDashboard";
//	}

	
	@RequestMapping(path="/Users/userDashboard", method=RequestMethod.GET)
	public String displayUserDashboard(ModelMap modelHolder, HttpSession session, HttpServletRequest req) {
		User user = (User) session.getAttribute("user");
		modelHolder.addAttribute("potholes", potholeDAO.getListOfPotholesByUserId(user.getUserId()));
		List<Pothole> listOfPotholes = potholeDAO.getListOfAllPotholes();
		req.setAttribute("arrayOfPotholes", potholeDAO.getArrayOfPothole(listOfPotholes));
//		List<Pothole> listOfPotholes = new ArrayList<Pothole>();
//		listOfPotholes = potholeDAO.getListOfPotholesByUserId(user.getUserId());
		return "Users/userDashboard";
	}	

	
	
//	@RequestMapping(path="/User/userDashboard{userid}", method=RequestMethod.GET)
//	public String displayUserDashboard(Map<String, Object> model, @PathVariable String userName) {
//	return "Home";
//	}

	@RequestMapping(path="/Users/employeeDashboard", method=RequestMethod.GET)
	public String displayEmployeeDashboard(ModelMap modelHolder, HttpSession session, HttpServletRequest req) {
		User user = (User) session.getAttribute("user");
		modelHolder.addAttribute("potholes", potholeDAO.getListOfAllPotholes());
		List<Pothole> listOfPotholes = potholeDAO.getListOfAllPotholes();
		req.setAttribute("arrayOfPotholes", potholeDAO.getArrayOfPothole(listOfPotholes));
	// User user = (User) session.getAttribute("user");
	return "Users/employeeDashboard";
	}
	
	@RequestMapping("/Users/redirectAfterReport")
	public String redirectToDashboards(HttpSession session, RedirectAttributes flash) {
	User user = (User) session.getAttribute("user");
	flash.addFlashAttribute("ThankYou", "Thank you for reporting a pothole!");

	    if(user.isEmployee()) {
	        return "redirect:/Users/employeeDashboard";

	            }
	            else {
	                return "redirect:/Users/userDashboard";
	            }
	}
	
}
