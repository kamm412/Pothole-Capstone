package com.techelevator.controller;

import com.techelevator.model.PotholeDAO;
import com.techelevator.model.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Controller
@SessionAttributes({ "currentUser", "isEmployee" })
public class PotholeController {

	@Autowired
	PotholeDAO potholeDAO;

	@RequestMapping(path = "/Potholes/allPotholes", method = RequestMethod.GET)
	public String showAllPotholes(Model model, @RequestParam(required = false) String orderBy, HttpSession session) {

		if (session.getAttribute("isEmployee") == null)
			session.setAttribute("isEmployee", false);

		if (orderBy == null) {
			orderBy = "report_date";
		}

		model.addAttribute("allPotholes", potholeDAO.getListOfPotholes(orderBy));

		return "/Potholes/allPotholes";
	}

	@RequestMapping(path = "/Potholes/employeePotholeUpdate", method = RequestMethod.GET)
	public String employeeModifyPotholeGet(Model model, @RequestParam long potholeId) {

		model.addAttribute("pothole", potholeDAO.getPotholeById(potholeId));

		return "/Potholes/employeePotholeUpdate";
	}

	@RequestMapping(path = "/Potholes/employeePotholeUpdate", method = RequestMethod.POST)
	public String employeeModifyPotholePost(@RequestParam long potholeId, @RequestParam int severity,
			@RequestParam String statusCode,
			@RequestParam("statusDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date statusDate) {

		LocalDate localDate = statusDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		potholeDAO.updatePotholeById(statusCode, localDate, severity, potholeId);

		return "redirect:/Potholes/allPotholes";
	}

	@RequestMapping(path = "/Potholes/deletePothole", method = RequestMethod.POST)
	public String employeeDeletePothole(@RequestParam long potholeId) {

		potholeDAO.deletePotholeById(potholeId);

		return "redirect:/Potholes/allPotholes";
	}

	@RequestMapping(path = "/Potholes/report", method = RequestMethod.GET)
	public String showReport(Model model, HttpSession session, RedirectAttributes attr) {

		String currentUser = (String) session.getAttribute("currentUser");

		if (currentUser != null) {
			return "/Potholes/reportPothole";
		} else {
			return "redirect:/User/login";
		}
	}
}
