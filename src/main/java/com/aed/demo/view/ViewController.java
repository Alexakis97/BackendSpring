package com.aed.demo.view;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aed.demo.entity.AED;
import com.aed.demo.entity.Ambulance;
import com.aed.demo.entity.Event;
import com.aed.demo.entity.MarketingObject;
import com.aed.demo.entity.Payment;
import com.aed.demo.entity.PaymentDetails;
import com.aed.demo.entity.Reports;
import com.aed.demo.entity.User;
import com.aed.demo.repositories.AEDRepository;
import com.aed.demo.repositories.AmbulanceRepository;
import com.aed.demo.repositories.EventRepository;
import com.aed.demo.repositories.MarketingRepository;
import com.aed.demo.repositories.PaymentRepository;
import com.aed.demo.repositories.ReportsRepository;
import com.aed.demo.repositories.UserRepository;
import com.aed.demo.security.AES;
import com.aed.demo.security.CustomPasswordEncoder;
import com.aed.demo.security.Mail;
import com.aed.demo.stripe.service.StripeService;
import com.aed.demo.stripe.utils.Response;
import com.stripe.exception.StripeException;
import com.stripe.model.Coupon;
import com.stripe.model.Subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ViewController {

	@Autowired
	AEDRepository aedRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	ReportsRepository reportsRepo;

	@Autowired
	EventRepository eventsRepo;

	@Autowired
	AmbulanceRepository ambulanceRepo;

	@Autowired
	PaymentRepository paymentRepo;

	@Autowired
	MarketingRepository marketingRepo;

	@GetMapping("/dashboard")
	public String getResource(Model model, HttpServletResponse response) throws IOException {

		User loggedinUser = getLoggedInUser();

		List<AED> aeds = aedRepo.findAll();
		List<AED> aedsByCluster = new ArrayList<AED>();
		List<User> users = userRepo.findAll();
		List<User> usersByCluster = new ArrayList<User>();
		List<Reports> reports = reportsRepo.findAll();
		List<Integer> idAedByCluster = new ArrayList<Integer>();

		List<Event> events = eventsRepo.findAll();

		List<Event> eventsByCountry = new ArrayList<Event>();

		List<Event> eventsByCluster = new ArrayList<Event>();
		List<Event> eventsByClusterDate = new ArrayList<Event>();

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Athens"));
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);

		month++; // auksanoyme ton mina kata 1 giati ksekinaei i arithmisi apo to 0

		String dayStr = String.valueOf(day);
		String monthStr = String.valueOf(month);
		String yearStr = String.valueOf(year);

		if (month < 10) {
			monthStr = "0" + month;
		}
		if (day < 10) {
			dayStr = "0" + day;
		}

		model.addAttribute("loggedinUser", loggedinUser);
		String usercluster[] = loggedinUser.getClusterMunicipality().split(",");

		for (AED aed : aeds) {
			for (String municipality : usercluster) {
				if (aed.getMunicipality() != null && aed.getMunicipality().equalsIgnoreCase(municipality)) {
					aedsByCluster.add(aed);
					idAedByCluster.add(aed.getAed_id());
				}
			}
		}

		model.addAttribute("aedsByClusterLength", aedsByCluster.size());
		for (User user : users) {
			for (String municipality : usercluster) {
				if (user.getMunicipality() != null) {
					if (user.getMunicipality().equalsIgnoreCase(municipality)) {
						usersByCluster.add(user);
					}
				}

			}
		}

		int fixed = 0;
		int notFixed = 0;
		for (Reports report : reports) {
			if (idAedByCluster.contains(report.getAed().getAed_id())) {

				if (report.getStatus().equals("0")) {

					notFixed++;
				} else {
					fixed++;
				}
			}
		}

		for (Event event : events) {
			for (String municipality : usercluster) {
				if (event.getMunicipality() != null && event.getHospital() != null && loggedinUser.getHospital() != null
						&& event.getMunicipality().equals(municipality)
						&& event.getHospital().getHospital_id() == loggedinUser.getHospital().getHospital_id()) {
					eventsByCluster.add(event);

				}
			}

			if (event.getCountry() != null && event.getCountry().equals(loggedinUser.getCountry())) {
				eventsByCountry.add(event);
			}
		}

		float pososto_fixed = 0;
		float pososto_not_fixed = 0;
		if ((notFixed + fixed) != 0) {
			pososto_fixed = ((float) (fixed) / (notFixed + fixed)) * 100;
			pososto_not_fixed = ((float) (notFixed) / (notFixed + fixed)) * 100;

		}

		int kardiaka = 0;
		int egkefaliko = 0;
		int karkinopathis = 0;
		int troxaio = 0;
		int katagma = 0;
		int spasimo = 0;
		int agnosto = 0;

		float pososto_kardiakwn = 0;
		float pososto_egkefaliko = 0;
		float pososto_karkinopathis = 0;
		float pososto_troxaio = 0;
		float pososto_katagma = 0;
		float pososto_spasimo = 0;
		float pososto_agnosto = 0;

		for (Event event : eventsByCluster) {
			if (event.getCreationDate().split("/")[0].equals(dayStr)
					&& event.getCreationDate().split("/")[1].equals(monthStr)
					&& event.getCreationDate().split("/")[2].split(" ")[0].equals(yearStr)
					&& event.getHospital().getHospital_id() == loggedinUser.getHospital().getHospital_id()) {
				System.out.println("EVT" + event);
				eventsByClusterDate.add(event);
			}

		}

		for (Event event : eventsByCluster) {

			if (event.getStatus().equalsIgnoreCase("Cerebral")) {
				kardiaka++;
			}
			if (event.getStatus().equalsIgnoreCase("Heart Attack")) {
				egkefaliko++;
			}
			if (event.getStatus().equalsIgnoreCase("Cancer Trasnfered")) {
				karkinopathis++;
			}
			if (event.getStatus().equalsIgnoreCase("Car Accident")) {
				troxaio++;
			}
			if (event.getStatus().equalsIgnoreCase("Pelvic Fractures")) {
				katagma++;
			}
			if (event.getStatus().equalsIgnoreCase("Bone Fractures")) {
				spasimo++;
			}
			if (event.getStatus().equalsIgnoreCase("Unknown")) {
				agnosto++;
			}
		}

		if (eventsByCluster.size() > 0) {
			pososto_kardiakwn = ((float) kardiaka / eventsByCluster.size()) * 100;
			pososto_egkefaliko = ((float) egkefaliko / eventsByCluster.size()) * 100;
			pososto_karkinopathis = ((float) karkinopathis / eventsByCluster.size()) * 100;
			pososto_troxaio = ((float) troxaio / eventsByCluster.size()) * 100;
			pososto_katagma = ((float) katagma / eventsByCluster.size()) * 100;
			pososto_spasimo = ((float) spasimo / eventsByCluster.size()) * 100;
			pososto_agnosto = ((float) agnosto / eventsByCluster.size()) * 100;
		}

		model.addAttribute("pososto_fixed", String.format("%.2f", (float) pososto_fixed));
		model.addAttribute("pososto_not_fixed", String.format("%.2f", (float) pososto_not_fixed));
		model.addAttribute("fixed", fixed);
		model.addAttribute("notFixed", notFixed);

		model.addAttribute("aedsByCluster", aedsByCluster);

		model.addAttribute("usersByCluster", usersByCluster);
		model.addAttribute("usersByClusterLength", usersByCluster.size());

		model.addAttribute("eventsByCountrySize", eventsByCountry.size());

		model.addAttribute("eventsByCluster", eventsByCluster);
		model.addAttribute("eventsByClusterSize", eventsByCluster.size());

		System.out.println(eventsByClusterDate.toString());

		model.addAttribute("eventsByClusterDate", eventsByClusterDate);
		model.addAttribute("eventsByClusterDateSize", eventsByClusterDate.size());

		model.addAttribute("pososto_kardiakwn", String.format("%.2f", (float) pososto_kardiakwn));
		model.addAttribute("pososto_egkefaliko", String.format("%.2f", (float) pososto_egkefaliko));
		model.addAttribute("pososto_karkinopathis", String.format("%.2f", (float) pososto_karkinopathis));
		model.addAttribute("pososto_troxaio", String.format("%.2f", (float) pososto_troxaio));
		model.addAttribute("pososto_katagma", String.format("%.2f", (float) pososto_katagma));
		model.addAttribute("pososto_spasimo", String.format("%.2f", (float) pososto_spasimo));
		model.addAttribute("pososto_agnosto", String.format("%.2f", (float) pososto_agnosto));

		model.addAttribute("usersInterceptor", users);
		model.addAttribute("paymentsInterceptor", paymentRepo.findAll());

		response.setHeader("Set-Cookie", "HttpOnly;Secure;SameSite=None");

		List<Ambulance> ambulances = ambulanceRepo.findAll();
		List<Ambulance> ambulanceByCity = new ArrayList<Ambulance>();

		for (Ambulance ambulance : ambulances) {

			if (ambulance.getHospital().getHospital_id() == loggedinUser.getHospital().getHospital_id()) {
				ambulanceByCity.add(ambulance);
			}

		}

		model.addAttribute("ambulanceByCityLength", ambulanceByCity.size());
		model.addAttribute("ambulanceByCity", ambulanceByCity);

		return "dashboard";
	}

	// private void handleIsActive(int isActive,User loggedinUser,
	// HttpServletResponse response) throws IOException {

	// Optional<PaymentRepository> paymentOptional =
	// paymentRepo.findByUser(loggedinUser);
	// Payment payment = (Payment) paymentOptional.get();

	// Subscription subscription=null;
	// try {
	// subscription = Subscription.retrieve(payment.getSubscriptionName());
	// } catch (StripeException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// String status = subscription.getStatus();

	// if(status.equalsIgnoreCase("canceled") || status.equalsIgnoreCase("unpaid"))
	// {
	// loggedinUser.setIsActive(0);
	// userRepo.save(loggedinUser);
	// }

	// if(isActive==2)
	// {
	// response.sendRedirect("/setUserCredentials");
	// }
	// else if (isActive==3)
	// {
	// response.sendRedirect("/plans");
	// }

	// Retrieve created subscription

	// Subscription subscription = Subscription.retrieve(subscriptionID);
	// subscription.getStatus();

	// }

	@GetMapping("/ekab/admin")
	public String getAdminView(Model model, HttpServletResponse response) throws IOException {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		if (loggedinUser != null) {

			List<AED> aeds = aedRepo.findAll();
			List<AED> aedsByCountry = new ArrayList<AED>();
			List<User> users = userRepo.findAll();
			List<User> usersByCountry = new ArrayList<User>();
			List<Reports> reports = reportsRepo.findAll();
			List<Integer> idAedByCountry = new ArrayList<Integer>();

			List<Event> events = eventsRepo.findAll();
			List<Event> eventsByCountry = new ArrayList<Event>();
			List<Event> eventsByCountryDate = new ArrayList<Event>();

			model.addAttribute("loggedinUser", loggedinUser);

			String usercountry[] = loggedinUser.getCountry().split(",");

			for (AED aed : aeds) {
				for (String country : usercountry) {
					if (aed.getCountry().equalsIgnoreCase(country)) {
						aedsByCountry.add(aed);
						idAedByCountry.add(aed.getAed_id());
					}
				}
			}

			model.addAttribute("aedsByCountryLength", aedsByCountry.size());

			for (User user : users) {
				for (String country : usercountry) {
					if (user.getCity() != null) {
						if (user.getCity().equalsIgnoreCase(country)) {
							usersByCountry.add(user);
						}
					}

				}
			}

			int fixed = 0;
			int notFixed = 0;
			for (Reports report : reports) {
				if (idAedByCountry.contains(report.getAed().getAed_id())) {

					if (report.getStatus().equals("0")) {

						notFixed++;
					} else {
						fixed++;
					}
				}
			}

			for (Event event : events) {
				for (String country : usercountry) {
					if (event.getCountry().equals(country)) {
						eventsByCountry.add(event);
					}
				}

				if (event.getCountry().equals(loggedinUser.getCountry())) {
					eventsByCountry.add(event);
				}
			}

			float pososto_fixed = 0;
			float pososto_not_fixed = 0;
			if ((notFixed + fixed) != 0) {
				pososto_fixed = ((float) (fixed) / (notFixed + fixed)) * 100;
				pososto_not_fixed = ((float) (notFixed) / (notFixed + fixed)) * 100;

			}

			String countryToSearch = loggedinUser.getCountry();
			HashMap<String, Integer> cities = new HashMap<>();
			List<String> citiesString = new ArrayList<>();

			int count_current_city = 0;
			List<Event> allEvents = eventsRepo.findAll();

			for (Event e : allEvents) {

				if (e.getCountry().equalsIgnoreCase(countryToSearch)) {
					String cur_city = e.getCity();
					if (!citiesString.contains(cur_city)) {
						citiesString.add(cur_city);
						count_current_city = 0;
					}

					count_current_city++;
					cities.put(e.getCity(), count_current_city);
				}

			}

			List<String> AllCities = new ArrayList<>();
			List<Integer> AllCounters = new ArrayList<>();

			for (HashMap.Entry<String, Integer> entry : cities.entrySet()) {
				AllCities.add(entry.getKey());
				AllCounters.add(entry.getValue());
				// do something with key and/or tab
			}

			String temp1 = AllCities.get(AllCities.size() - 1);
			model.addAttribute("last_city", temp1);
			AllCities.remove(AllCities.size() - 1);

			model.addAttribute("allCities", AllCities);

			Integer last_counter = AllCounters.get(AllCounters.size() - 1);
			model.addAttribute("last_counter", last_counter);
			AllCounters.remove(AllCounters.size() - 1);
			model.addAttribute("allCounters", AllCounters);

			model.addAttribute("citySize", AllCities.size());

			model.addAttribute("pososto_fixed", String.format("%.2f", (float) pososto_fixed));
			model.addAttribute("pososto_not_fixed", String.format("%.2f", (float) pososto_not_fixed));
			model.addAttribute("fixed", fixed);
			model.addAttribute("notFixed", notFixed);

			model.addAttribute("aedsByCountry", aedsByCountry);

			model.addAttribute("usersByCountry", usersByCountry);
			model.addAttribute("usersByCountryLength", usersByCountry.size());

			model.addAttribute("eventsByCountry", eventsByCountry);
			model.addAttribute("eventsByCountrySize", eventsByCountry.size());

			model.addAttribute("eventsByCountryDate", eventsByCountryDate);
			model.addAttribute("eventsByCountryDateSize", eventsByCountryDate.size());

		}

		return "redirect:/ekavAdmin";

	}

	@GetMapping("/radio")
	public String getRadio(Model model, HttpServletResponse response) throws IOException {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		if (loggedinUser != null) {

			List<AED> aeds = aedRepo.findAll();
			List<AED> aedsByCluster = new ArrayList<AED>();
			List<User> users = userRepo.findAll();
			List<User> usersByCluster = new ArrayList<User>();
			List<Reports> reports = reportsRepo.findAll();
			List<Integer> idAedByCity = new ArrayList<Integer>();

			List<Event> events = eventsRepo.findAll();
			List<Event> eventsByCluster = new ArrayList<Event>();
			List<Event> eventsByClusterDate = new ArrayList<Event>();
			List<Event> eventsByCountry = new ArrayList<Event>();

			// Date date; // your date
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Athens"));
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);

			month++; // auksanoyme ton mina kata 1 giati ksekinaei i arithmisi apo to 0

			String dayStr = String.valueOf(day);
			String monthStr = String.valueOf(month);
			String yearStr = String.valueOf(year);

			if (month < 10) {
				monthStr = "0" + month;

			}
			if (day < 10) {
				dayStr = "0" + day;
			}

			model.addAttribute("loggedinUser", loggedinUser);

			String userCluster[] = loggedinUser.getClusterMunicipality().split(",");

			for (AED aed : aeds) {
				for (String municipality : userCluster) {
					if (aed.getMunicipality() != null && aed.getMunicipality().equalsIgnoreCase(municipality)) {
						aedsByCluster.add(aed);

						idAedByCity.add(aed.getAed_id());
					}
				}
			}

			model.addAttribute("aedsByClusterLength", aedsByCluster.size());

			for (User user : users) {
				for (String municipality : userCluster) {
					if (user.getMunicipality() != null && user.getMunicipality().equalsIgnoreCase(municipality)) {
						usersByCluster.add(user);
					}
				}
			}

			int fixed = 0;
			int notFixed = 0;
			for (Reports report : reports) {
				if (idAedByCity.contains(report.getAed().getAed_id())) {

					if (report.getStatus().equals("0")) {

						notFixed++;
					} else {
						fixed++;
					}
				}
			}

			for (Event event : events) {
				for (String municipality : userCluster) {
					if (event.getMunicipality().equals(municipality)
							&& event.getHospital().getHospital_id() == loggedinUser.getHospital().getHospital_id()) {
						eventsByCluster.add(event);
					}
				}

				if (event.getCountry().equals(loggedinUser.getCountry())) {
					eventsByCountry.add(event);
				}
			}

			float pososto_fixed = 0;
			float pososto_not_fixed = 0;
			if ((notFixed + fixed) != 0) {
				pososto_fixed = ((float) (fixed) / (notFixed + fixed)) * 100;
				pososto_not_fixed = ((float) (notFixed) / (notFixed + fixed)) * 100;

			}

			int kardiaka = 0;
			int egkefaliko = 0;
			int karkinopathis = 0;
			int troxaio = 0;
			int katagma = 0;
			int spasimo = 0;
			int agnosto = 0;

			float pososto_kardiakwn = 0;
			float pososto_egkefaliko = 0;
			float pososto_karkinopathis = 0;
			float pososto_troxaio = 0;
			float pososto_katagma = 0;
			float pososto_spasimo = 0;
			float pososto_agnosto = 0;

			for (Event event : eventsByCluster) {

				if (event.getCreationDate().split("/")[0].equals(dayStr)
						&& event.getCreationDate().split("/")[1].equals(monthStr)
						&& event.getCreationDate().split("/")[2].split(" ")[0].equals(yearStr)) {
					eventsByClusterDate.add(event);

				}

				if (event.getStatus().equalsIgnoreCase("Καρδιακή Ανακοπή")) {
					kardiaka++;
				}
				if (event.getStatus().equalsIgnoreCase("Εγκεφαλικό επεισοδιο")) {
					egkefaliko++;
				}
				if (event.getStatus().equalsIgnoreCase("Μεταφορά Καρκνινοπαθή")) {
					karkinopathis++;
				}
				if (event.getStatus().equalsIgnoreCase("Τροχαίο Ατύχημα")) {
					troxaio++;
				}
				if (event.getStatus().equalsIgnoreCase("Κατάγματα λεκάνης")) {
					katagma++;
				}
				if (event.getStatus().equalsIgnoreCase("Σπάσιμο οστών")) {
					spasimo++;
				}
				if (event.getStatus().equalsIgnoreCase("Άγνωστο")) {
					agnosto++;
				}
			}

			if (eventsByCluster.size() > 0) {
				pososto_kardiakwn = ((float) kardiaka / eventsByCluster.size()) * 100;
				pososto_egkefaliko = ((float) egkefaliko / eventsByCluster.size()) * 100;
				pososto_karkinopathis = ((float) karkinopathis / eventsByCluster.size()) * 100;
				pososto_troxaio = ((float) troxaio / eventsByCluster.size()) * 100;
				pososto_katagma = ((float) katagma / eventsByCluster.size()) * 100;
				pososto_spasimo = ((float) spasimo / eventsByCluster.size()) * 100;
				pososto_agnosto = ((float) agnosto / eventsByCluster.size()) * 100;
			}

			List<Ambulance> ambulances = ambulanceRepo.findAll();
			List<Ambulance> ambulanceByClinic = new ArrayList<Ambulance>();

			// String usercity[] = loggedinUser.getCity().split(",");

			for (Ambulance ambulance : ambulances) {

				// ---------------------------------------------
				if (ambulance.getHospital().getHospital_id() == loggedinUser.getHospital().getHospital_id()) {
					ambulanceByClinic.add(ambulance);
				}

			}

			model.addAttribute("ambulanceByClinicLength", ambulanceByClinic.size());
			model.addAttribute("ambulanceByClinic", ambulanceByClinic);

			model.addAttribute("pososto_fixed", String.format("%.2f", (float) pososto_fixed));
			model.addAttribute("pososto_not_fixed", String.format("%.2f", (float) pososto_not_fixed));
			model.addAttribute("fixed", fixed);
			model.addAttribute("notFixed", notFixed);

			model.addAttribute("aedsByCluster", aedsByCluster);

			model.addAttribute("usersByCluster", usersByCluster);
			model.addAttribute("usersByClusterLength", usersByCluster.size());

			model.addAttribute("eventsByCluster", eventsByCluster);
			model.addAttribute("eventsByClusterSize", eventsByCluster.size());
			model.addAttribute("eventsByCountrySize", eventsByCountry.size());

			model.addAttribute("eventsByClusterDate", eventsByClusterDate);
			model.addAttribute("eventsByClusterDateSize", eventsByClusterDate.size());

			model.addAttribute("pososto_kardiakwn", String.format("%.2f", (float) pososto_kardiakwn));
			model.addAttribute("pososto_egkefaliko", String.format("%.2f", (float) pososto_egkefaliko));
			model.addAttribute("pososto_karkinopathis", String.format("%.2f", (float) pososto_karkinopathis));
			model.addAttribute("pososto_troxaio", String.format("%.2f", (float) pososto_troxaio));
			model.addAttribute("pososto_katagma", String.format("%.2f", (float) pososto_katagma));
			model.addAttribute("pososto_spasimo", String.format("%.2f", (float) pososto_spasimo));
			model.addAttribute("pososto_agnosto", String.format("%.2f", (float) pososto_agnosto));

			response.setHeader("Set-Cookie", "HttpOnly;Secure;SameSite=None");
			response.setHeader("Connection", "keep-alive");

		}

		return "radio";
	}

	@GetMapping("/manageReports")
	public String getReports(Model model, HttpServletResponse response) throws IOException {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		int isActive = loggedinUser.getIsActive();

		if (loggedinUser == null) {
			return "redirect:/login";
		}

		List<AED> aeds = aedRepo.findAll();
		List<AED> reportedAedsByCluster = new ArrayList<AED>();

		List<Reports> reports = reportsRepo.findAll();
		List<Reports> reportsByCluster = new ArrayList<Reports>();

		String userMunicipality[] = loggedinUser.getClusterMunicipality().split(",");

		for (Reports report : reports) {

			for (AED aed : aeds) {
				if (report.getAed().getAed_id() == aed.getAed_id()) {

					for (String municipality : userMunicipality) {
						if (aed.getMunicipality().equalsIgnoreCase(municipality)) {
							reportedAedsByCluster.add(aed);
							reportsByCluster.add(report);
						}
					}
				}
			}
		}

		model.addAttribute("aedsByClusterLength", reportedAedsByCluster.size());

		model.addAttribute("aedsByCluster", reportedAedsByCluster);
		model.addAttribute("loggedinUser", loggedinUser);
		model.addAttribute("reportsByCluster", reportsByCluster);

		return "manageReports";
	}

	@GetMapping("/manageAeds")
	public String getbuttons(Model model, HttpServletResponse response) throws IOException {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		if (loggedinUser == null) {
			return "redirect:/login";
		}
		List<AED> aeds = aedRepo.findAll();
		List<AED> aedsByCluster = new ArrayList<AED>();

		String userCluster[] = loggedinUser.getClusterMunicipality().split(",");
		int useraedsInClusterCounter[] = new int[userCluster.length];

		for (AED aed : aeds) {
			for (int i = 0; i < userCluster.length; i++) {
				if (aed.getMunicipality() != null && aed.getMunicipality().equalsIgnoreCase(userCluster[i])) {
					aedsByCluster.add(aed);
					useraedsInClusterCounter[i]++;
				}
			}

		}

		model.addAttribute("aedsByClusterLength", aedsByCluster.size());
		model.addAttribute("aeds", aeds.size());
		model.addAttribute("cluster", userCluster);
		model.addAttribute("clusterAsArray", Arrays.asList(userCluster));

		model.addAttribute("clusterSize", userCluster.length);

		model.addAttribute("clusterCounter", useraedsInClusterCounter);
		model.addAttribute("aedsByCluster", aedsByCluster);
		model.addAttribute("loggedinUser", loggedinUser);

		return "manageAeds";

	}

	@GetMapping("/manageEmployees")
	public String manageTilAsirmatiste(Model model, HttpServletResponse response) throws IOException {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		List<User> employees = userRepo.findAll();
		List<User> employeesByCluster = new ArrayList<User>();

		String userCluster[] = loggedinUser.getClusterMunicipality().split(",");
		// int useraedsInCitiesCounter[] = new int[usercity.length];

		for (User employee : employees) {
			for (int i = 0; i < userCluster.length; i++) {
				if (employee.getClusterMunicipality() != null
						&& employee.getClusterMunicipality().equalsIgnoreCase(userCluster[i])
						&& employee.getHospital().getHospital_id() == loggedinUser.getHospital().getHospital_id()) {
					if (employee.getRoles().contains("TILEFONITIS") || employee.getRoles().contains("ASIRMATISTIS"))
						employeesByCluster.add(employee);
					// useraedsInCitiesCounter[i]++;
				}
			}

		}

		model.addAttribute("employeesByClusterLength", employeesByCluster.size());

		model.addAttribute("employeesByCluster", employeesByCluster);
		model.addAttribute("loggedinUser", loggedinUser);

		return "manageEmployees";

	}

	@GetMapping("/manageEvents")
	public String getManage(Model model, HttpServletResponse response) throws IOException {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		// List<User> users = userRepo.findAll();
		if (loggedinUser == null) {
			return "redirect:/login";
		}

		List<Event> events = eventsRepo.findAll();
		List<Event> eventsByCluster = new ArrayList<Event>();

		String userCluster[] = loggedinUser.getClusterMunicipality().split(",");

		for (Event event : events) {
			for (String municipality : userCluster) {
				if (event.getMunicipality() != null && event.getMunicipality().equalsIgnoreCase(municipality)
						&& event.getHospital().getHospital_id() == loggedinUser.getHospital().getHospital_id()) {
					eventsByCluster.add(event);
				}
			}

		}

		model.addAttribute("eventsByClusterLength", eventsByCluster.size());

		model.addAttribute("eventsByCluster", eventsByCluster);
		model.addAttribute("loggedinUser", loggedinUser);

		return "manageEvents";

	}

	@GetMapping("/manageAmbulance")
	public String getAmbulance(Model model, HttpServletResponse response) throws IOException {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		// List<User> users = userRepo.findAll();
		if (loggedinUser == null) {
			return "redirect:/login";
		}

		List<Ambulance> ambulances = ambulanceRepo.findAll();
		List<Ambulance> ambulanceByCity = new ArrayList<Ambulance>();

		String usercity[] = loggedinUser.getCity().split(",");

		for (Ambulance ambulance : ambulances) {

			if (ambulance.getHospital().getHospital_id() == loggedinUser.getHospital().getHospital_id())
				ambulanceByCity.add(ambulance);

		}

		model.addAttribute("ambulanceByCityLength", ambulanceByCity.size());
		model.addAttribute("ambulanceByCity", ambulanceByCity);
		System.out.println(ambulanceByCity);
		model.addAttribute("loggedinUser", loggedinUser);

		return "manageAmbulance";

	}

	@GetMapping("/charts")
	public String getChartInfo(Model model, HttpServletResponse response) throws IOException {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		List<User> users = userRepo.findAll();
		List<Ambulance> ambu = ambulanceRepo.findAll();

		if (loggedinUser == null) {
			return "redirect:/login";
		}

		model.addAttribute("loggedinUser", loggedinUser);
		String months[] = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
		int monthsCounters[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		List<Event> allEvents = eventsRepo.findAll();

		HashMap<String, Integer> eventByMonth = new HashMap<>();

		HashMap<String, HashMap<String, Integer>> eventByMonthAndYear = new HashMap<>();

		HashSet<String> allYears = new HashSet<String>();

		for (int i = 0; i < allEvents.size(); i++) {

			String all_date_string = allEvents.get(i).getCreationDate();
			String month_position[] = all_date_string.split("/");
			allYears.add(month_position[2].split(" ")[0]);
		}

		for (String year : allYears) {
			for (int i = 0; i < allEvents.size(); i++) {

				String all_date_string = allEvents.get(i).getCreationDate();
				String month_position[] = all_date_string.split("/");
				String month = month_position[1];

				for (int j = 0; j < months.length; j++) {

					if (month.equals(months[j])) {
						monthsCounters[j]++;
						eventByMonth.put(month, monthsCounters[j]);
						eventByMonthAndYear.put(year, eventByMonth);

					}
				}

			}
		}

		System.out.println("Year & Month" + eventByMonthAndYear);

		List<String> GreekMonths = new ArrayList<>();
		GreekMonths.add("Ιανουάριος");
		GreekMonths.add("Φεβρουάριος");
		GreekMonths.add("Μάρτιος");
		GreekMonths.add("Απρίλιος");
		GreekMonths.add("Μάϊος");
		GreekMonths.add("Ιούνιος");
		GreekMonths.add("Ιούλιος");
		GreekMonths.add("Αύγουστος");
		GreekMonths.add("Σεπτέμβριος");
		GreekMonths.add("Οκτώβριος");
		GreekMonths.add("Νοέμβριος");
		GreekMonths.add("Δεκέμβριος");
		for (int i = 0; i < GreekMonths.size(); i++) {
			model.addAttribute("name" + months[i], GreekMonths.get(i));
			model.addAttribute("value" + months[i], months[i]);
		}

		model.addAttribute("eventByMonthAndYear", eventByMonthAndYear);
		String countryToSearch = loggedinUser.getCountry();
		HashMap<String, Integer> clusters = new HashMap<>();
		List<String> clustersString = new ArrayList<>();

		int count_current_city = 0;

		for (Event e : allEvents) {

			if (e.getCountry().equalsIgnoreCase(countryToSearch)) {
				String cur_municipality = e.getMunicipality();
				if (!clustersString.contains(cur_municipality)) {
					clustersString.add(cur_municipality);
					count_current_city = 0;
				}

				count_current_city++;
				clusters.put(e.getMunicipality(), count_current_city);
			}

		}

		List<String> AllClusters = new ArrayList<>();
		List<Integer> AllCounters = new ArrayList<>();

		for (HashMap.Entry<String, Integer> entry : clusters.entrySet()) {
			AllClusters.add(entry.getKey());
			AllCounters.add(entry.getValue());
			// do something with key and/or tab
		}

		System.out.println("AllClusters: " + AllClusters);

		System.out.println("AllClusters: " + AllCounters);

		model.addAttribute("allClusters", AllClusters);

		model.addAttribute("allCounters", AllCounters);

		model.addAttribute("clustersSize", AllClusters.size());

		ArrayList<Integer> colors = new ArrayList<Integer>();
		int j = 0;
		for (int i = 0; i < AllClusters.size(); i++) {

			j += 25;
			colors.add(j);

		}

		model.addAttribute("colors", colors);

		String userClusters[] = loggedinUser.getClusterMunicipality().split(",");

		model.addAttribute("userClusters", userClusters);
		model.addAttribute("userClustersSize", userClusters.length);
		System.out.println("UserCitySize: " + userClusters.length);

		float userAmbulanceAssignTime[] = new float[userClusters.length];
		float userAmbulanceReceiptTime[] = new float[userClusters.length];
		float userAmbulanceFinishedtTime[] = new float[userClusters.length];

		HashMap<String, ArrayList<Integer>> hmaptimes = new HashMap<String, ArrayList<Integer>>();
		ArrayList<Integer> allAssignTimes = new ArrayList<Integer>();

		for (int i = 0; i < userClusters.length; i++) {

			for (Event event : allEvents) {
				if (event.getMunicipality() != null && event.getMunicipality().equalsIgnoreCase(userClusters[i])) {

					// String eventAssigned = event.getAssignmentDate();
					if (event.getAssignmentDate() != null && event.getCreationDate() != null) {
						System.out.println("Event" + event.getCreationDate());
						System.out.println("Split: " + (event.getCreationDate().split(":")[0]).split(" ")[1]);
						int hour = Integer.parseInt((event.getCreationDate().split(":")[0]).split(" ")[1]);
						int minute = Integer.parseInt(event.getCreationDate().split(":")[1]);
						int seconds = Integer.parseInt((event.getCreationDate().split(":")[2]));
						System.out.println("Seconds Creation: " + seconds);

						int totalSecondsCreationDate = seconds + minute * 60 + hour * 60 * 60;
						System.out.println("AllSecs" + totalSecondsCreationDate);

						System.out.println("Event" + event.getAssignmentDate());
						System.out.println("Split: " + (event.getAssignmentDate().split(":")[0]).split(" ")[1]);
						int hour2 = Integer.parseInt((event.getAssignmentDate().split(":")[0]).split(" ")[1]);
						int minute2 = Integer.parseInt(event.getAssignmentDate().split(":")[1]);
						int seconds2 = Integer.parseInt(event.getAssignmentDate().split(":")[2]);
						System.out.println("Seconds Assignment: " + seconds);

						int totalSecondsAssignmentDate = seconds2 + minute2 * 60 + hour2 * 60 * 60;
						System.out.println("AllSecs" + totalSecondsAssignmentDate);

						allAssignTimes.add(((totalSecondsAssignmentDate - totalSecondsCreationDate) / 60) % 60);

						System.out.println(
								"Minutes: " + (((totalSecondsAssignmentDate - totalSecondsCreationDate) / 60) % 60));

						// int createdAt = Integer.parseInt(event.getCreationDate().split(":")[1]) ;
						// int assignedAt = Integer.parseInt(event.getAssignmentDate().split(":")[1]) ;
						// int receiptAt = Integer.parseInt(event.getReceiptDate().split(":")[1]) ;
						// int finishedAt = Integer.parseInt(event.getFinishedDate().split(":")[1]) ;

						// userAmbulanceAssignTime[i] = assignedAt - createdAt;
						// userAmbulanceReceiptTime[i] = receiptAt - assignedAt;
						// userAmbulanceFinishedtTime[i] = finishedAt - createdAt;

						System.out.println("Cluster: " + userClusters[i]);

						System.out.println("allAssignTimes" + allAssignTimes);

					}
				}
			}
			System.out.println("RENEW!");
			System.out.println(hmaptimes);
			hmaptimes.put(userClusters[i], allAssignTimes);
			allAssignTimes = new ArrayList<Integer>();

		}

		for (int i = 0; i < userClusters.length; i++) {
			ArrayList<Integer> eachMO = hmaptimes.get(userClusters[i]);
			float mo = 0;
			int lilsum = 0;
			for (Integer n : eachMO) {
				lilsum += n;
			}
			if (eachMO.size() != 0) {
				mo = lilsum / eachMO.size();
			}
			userAmbulanceAssignTime[i] = mo;
		}

		model.addAttribute("userAmbulanceAssignTime", userAmbulanceAssignTime);

		System.out.println("Map" + hmaptimes);

		hmaptimes = new HashMap<>();
		allAssignTimes = new ArrayList<Integer>();

		for (int i = 0; i < userClusters.length; i++) {

			for (Event event : allEvents) {
				if (event.getMunicipality() != null && event.getMunicipality().equalsIgnoreCase(userClusters[i])) {

					// String eventAssigned = event.getAssignmentDate();
					if (event.getAssignmentDate() != null && event.getReceiptDate() != null) {
						System.out.println("Event" + event.getAssignmentDate());
						System.out.println("Split: " + (event.getAssignmentDate().split(":")[0]).split(" ")[1]);
						int hour = Integer.parseInt((event.getAssignmentDate().split(":")[0]).split(" ")[1]);
						int minute = Integer.parseInt(event.getAssignmentDate().split(":")[1]);
						int seconds = Integer.parseInt((event.getAssignmentDate().split(":")[2]));
						System.out.println("Seconds Creation: " + seconds);

						int totalSecondsCreationDate = seconds + minute * 60 + hour * 60 * 60;
						System.out.println("AllSecs" + totalSecondsCreationDate);

						System.out.println("Event" + event.getReceiptDate());
						System.out.println("Split: " + (event.getReceiptDate().split(":")[0]).split(" ")[1]);
						int hour2 = Integer.parseInt((event.getReceiptDate().split(":")[0]).split(" ")[1]);
						int minute2 = Integer.parseInt(event.getReceiptDate().split(":")[1]);
						int seconds2 = Integer.parseInt(event.getReceiptDate().split(":")[2]);
						System.out.println("Seconds Assignment: " + seconds);

						int totalSecondsAssignmentDate = seconds2 + minute2 * 60 + hour2 * 60 * 60;
						System.out.println("AllSecs" + totalSecondsAssignmentDate);

						allAssignTimes.add(((totalSecondsAssignmentDate - totalSecondsCreationDate) / 60) % 60);

						System.out.println(
								"Minutes: " + (((totalSecondsAssignmentDate - totalSecondsCreationDate) / 60) % 60));

						// int createdAt = Integer.parseInt(event.getCreationDate().split(":")[1]) ;
						// int assignedAt = Integer.parseInt(event.getAssignmentDate().split(":")[1]) ;
						// int receiptAt = Integer.parseInt(event.getReceiptDate().split(":")[1]) ;
						// int finishedAt = Integer.parseInt(event.getFinishedDate().split(":")[1]) ;

						// userAmbulanceAssignTime[i] = assignedAt - createdAt;
						// userAmbulanceReceiptTime[i] = receiptAt - assignedAt;
						// userAmbulanceFinishedtTime[i] = finishedAt - createdAt;

						System.out.println("Cluster: " + userClusters[i]);

						System.out.println("allAssignTimes" + allAssignTimes);

					}
				}
			}
			System.out.println("RENEW!");
			System.out.println(hmaptimes);
			hmaptimes.put(userClusters[i], allAssignTimes);
			allAssignTimes = new ArrayList<Integer>();

		}

		for (int i = 0; i < userClusters.length; i++) {
			ArrayList<Integer> eachMO = hmaptimes.get(userClusters[i]);
			float mo = 0;
			int lilsum = 0;
			for (Integer n : eachMO) {
				lilsum += n;
			}
			if (eachMO.size() != 0) {
				mo = lilsum / eachMO.size();
			}
			userAmbulanceReceiptTime[i] = mo;
		}

		model.addAttribute("userAmbulanceReceiptTime", userAmbulanceReceiptTime);

		System.out.println("Map" + hmaptimes);

		hmaptimes = new HashMap<>();
		allAssignTimes = new ArrayList<Integer>();

		for (int i = 0; i < userClusters.length; i++) {

			for (Event event : allEvents) {
				if (event.getMunicipality() != null && event.getMunicipality().equalsIgnoreCase(userClusters[i])) {

					// String eventAssigned = event.getAssignmentDate();
					if (event.getCreationDate() != null && event.getFinishedDate() != null) {
						System.out.println("Event" + event.getCreationDate());
						System.out.println("Split: " + (event.getCreationDate().split(":")[0]).split(" ")[1]);
						int hour = Integer.parseInt((event.getCreationDate().split(":")[0]).split(" ")[1]);
						int minute = Integer.parseInt(event.getCreationDate().split(":")[1]);
						int seconds = Integer.parseInt((event.getCreationDate().split(":")[2]));
						System.out.println("Seconds Creation: " + seconds);

						int totalSecondsCreationDate = seconds + minute * 60 + hour * 60 * 60;
						System.out.println("AllSecs" + totalSecondsCreationDate);

						System.out.println("Event" + event.getFinishedDate());
						System.out.println("Split: " + (event.getFinishedDate().split(":")[0]).split(" ")[1]);
						int hour2 = Integer.parseInt((event.getFinishedDate().split(":")[0]).split(" ")[1]);
						int minute2 = Integer.parseInt(event.getFinishedDate().split(":")[1]);
						int seconds2 = Integer.parseInt(event.getFinishedDate().split(":")[2]);
						System.out.println("Seconds Assignment: " + seconds);

						int totalSecondsAssignmentDate = seconds2 + minute2 * 60 + hour2 * 60 * 60;
						System.out.println("AllSecs" + totalSecondsAssignmentDate);

						allAssignTimes.add(((totalSecondsAssignmentDate - totalSecondsCreationDate) / 60) % 60);

						System.out.println(
								"Minutes: " + (((totalSecondsAssignmentDate - totalSecondsCreationDate) / 60) % 60));

						// int createdAt = Integer.parseInt(event.getCreationDate().split(":")[1]) ;
						// int assignedAt = Integer.parseInt(event.getAssignmentDate().split(":")[1]) ;
						// int receiptAt = Integer.parseInt(event.getReceiptDate().split(":")[1]) ;
						// int finishedAt = Integer.parseInt(event.getFinishedDate().split(":")[1]) ;

						// userAmbulanceAssignTime[i] = assignedAt - createdAt;
						// userAmbulanceReceiptTime[i] = receiptAt - assignedAt;
						// userAmbulanceFinishedtTime[i] = finishedAt - createdAt;

						System.out.println("Cluster: " + userClusters[i]);

						System.out.println("allAssignTimes" + allAssignTimes);

					}
				}
			}
			System.out.println("RENEW!");
			System.out.println(hmaptimes);
			hmaptimes.put(userClusters[i], allAssignTimes);
			allAssignTimes = new ArrayList<Integer>();

			HashMap<String, Integer> eventByHour = new HashMap<String, Integer>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				{
					put("00:00 - 00:59", 0);
					put("01:00 - 01:59", 0);
					put("02:00 - 02:59", 0);
					put("03:00 - 03:59", 0);
					put("04:00 - 04:59", 0);
					put("05:00 - 05:59", 0);
					put("06:00 - 06:59", 0);
					put("07:00 - 07:59", 0);
					put("08:00 - 08:59", 0);
					put("09:00 - 09:59", 0);
					put("10:00 - 10:59", 0);
					put("11:00 - 11:59", 0);
					put("12:00 - 12:59", 0);
					put("13:00 - 13:59", 0);
					put("14:00 - 14:59", 0);
					put("15:00 - 15:59", 0);
					put("16:00 - 16:59", 0);
					put("17:00 - 17:59", 0);
					put("18:00 - 18:59", 0);
					put("19:00 - 19:59", 0);
					put("20:00 - 20:59", 0);
					put("21:00 - 21:59", 0);
					put("22:00 - 22:59", 0);
					put("23:00 - 23:59", 0);
				}
			};

			// calculate by hour
			for (String munycipality : userClusters) {
				for (Event event : allEvents) {
					if (event.getMunicipality() != null && event.getMunicipality().equalsIgnoreCase(munycipality)) {
						int hour = Integer.parseInt((event.getCreationDate().split(":")[0]).split(" ")[1]);
						System.out.println("Hour: " + hour);
						String addup;
						if (hour >= 0 && hour <= 9) {
							addup = "0" + hour + ":00 - " + "0" + hour + ":59";
						} else {
							addup = hour + ":00 - " + hour + ":59";

						}

						System.out.println("Hour Addup:" + addup);
						int currentCounter = eventByHour.get(addup);
						currentCounter++;
						eventByHour.put(addup, currentCounter);

					}

				}
			}

			System.out.println("Calculation by hour: " + eventByHour);
			model.addAttribute("eventByHour", eventByHour);

		}

		for (int i = 0; i < userClusters.length; i++) {
			ArrayList<Integer> eachMO = hmaptimes.get(userClusters[i]);
			float mo = 0;
			int lilsum = 0;
			for (Integer n : eachMO) {
				lilsum += n;
			}
			if (eachMO.size() != 0) {
				mo = lilsum / eachMO.size();
			}
			userAmbulanceFinishedtTime[i] = mo;
		}

		model.addAttribute("userAmbulanceFinishedTime", userAmbulanceFinishedtTime);

		System.out.println("Map" + hmaptimes);

		hmaptimes = new HashMap<>();
		allAssignTimes = new ArrayList<Integer>();

		ArrayList<User> userByClusterArrayList = new ArrayList<>();
		for (int i = 0; i < userClusters.length; i++) {

			for (Event event : allEvents) {
				if (event.getMunicipality().equalsIgnoreCase(userClusters[i])
						&& event.getHospital().getHospital_id() == loggedinUser.getHospital().getHospital_id()) {
					userByClusterArrayList.add(event.getUser());
				}
			}
		}

		HashMap<String, Integer> bloodTypeCountersHashMap = new HashMap<>();
		Integer bloodTypeCounters[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		String bloodtypes[] = { "A+", "A-", "B+", "B-", "AB+", "AB-", "O-", "O-" };

		for (User usr : users) {

			String bloodTypeString = usr.getBloodType();

			for (int i = 0; i < bloodtypes.length; i++) {

				// if(bloodTypeString!=null && !bloodTypeString.equals("null")) {
				if (bloodTypeString != null && !bloodTypeString.equals("null")) {
					System.out.println("Type Decrtypting: " + bloodTypeString);
					if (AES.decrypt(bloodTypeString, usr.getUsername() + usr.getPassword())
							.equalsIgnoreCase(bloodtypes[i])) {
						bloodTypeCounters[i]++;

					}
				}

			}
		}
		for (int i = 0; i < bloodtypes.length; i++) {
			bloodTypeCountersHashMap.put(bloodtypes[i], bloodTypeCounters[i]);
		}

		model.addAttribute("userBloodTypes", bloodTypeCountersHashMap);

		HashSet<String> set = new HashSet<>();
		HashMap<String, Integer> hashmapCounters = new HashMap<>();
		for (User usr : users) {
			if (usr.getAlergies() != null && !usr.getAlergies().equals("null"))

				set.add(AES.decrypt(usr.getAlergies(), usr.getUsername() + usr.getPassword()));
		}

		for (String alergia : set) {
			int counterAlerg = 0;
			for (User usr : users) {
				if (usr.getAlergies() != null && !usr.getAlergies().equals("null")) {

					System.out.println(AES.decrypt(usr.getAlergies(), usr.getUsername() + usr.getPassword()));

					if (AES.decrypt(usr.getAlergies(), usr.getUsername() + usr.getPassword())
							.equalsIgnoreCase(alergia)) {
						counterAlerg++;
						hashmapCounters.put(alergia, counterAlerg);
					}
				}
			}
		}
		ArrayList<Integer> countersAlerg = new ArrayList<>();
		for (String alergia : set) {
			countersAlerg.add(hashmapCounters.get(alergia));
		}
		ArrayList<String> rgbArrayList = new ArrayList<String>();
		Random random = new Random();
		for (int i = 0; i < set.size(); i++) {
			int r = random.nextInt(256);
			int g = random.nextInt(256);
			int b = random.nextInt(256);

			rgbArrayList.add("rgba(" + r + "," + g + "," + b + ",0.8)");
		}
		System.out.println(hashmapCounters);
		model.addAttribute("namesAlergies", set);
		model.addAttribute("alergiesCounter", countersAlerg);
		model.addAttribute("rgbArrayList", rgbArrayList);

		HashSet<String> farmakaset = new HashSet<String>();

		for (User usr : users) {
			if (usr.getMedicines() != null && !usr.getMedicines().equals("null"))
				farmakaset.add(AES.decrypt(usr.getMedicines(), usr.getUsername() + usr.getPassword()));
		}

		HashMap<String, Integer> hashmapCountersfarmaka = new HashMap<String, Integer>();

		for (String farmaka : farmakaset) {
			int farmakaCounter = 0;
			for (User usr : users) {
				if (usr.getMedicines() != null && !usr.getMedicines().equals("null")) {

					if (AES.decrypt(usr.getMedicines(), usr.getUsername() + usr.getPassword())
							.equalsIgnoreCase(farmaka)) {
						farmakaCounter++;
						hashmapCountersfarmaka.put(farmaka, farmakaCounter);
					}
				}
			}
		}
		ArrayList<Integer> countersFarmaka = new ArrayList<>();
		for (String farmaka : farmakaset) {
			countersFarmaka.add(hashmapCountersfarmaka.get(farmaka));
		}
		ArrayList<String> rgbArrayListfarmaka = new ArrayList<String>();

		for (int i = 0; i < farmakaset.size(); i++) {
			int r = random.nextInt(256);
			int g = random.nextInt(256);
			int b = random.nextInt(256);

			rgbArrayListfarmaka.add("rgba(" + r + "," + g + "," + b + ",0.8)");
		}
		System.out.println(hashmapCounters);

		model.addAttribute("namesFarmaka", farmakaset);
		model.addAttribute("farmakaCounter", countersFarmaka);
		model.addAttribute("rgbArrayListFarmaka", rgbArrayListfarmaka);

		HashSet<String> ambuCity = new HashSet<String>();
		HashMap<String, Integer> counterAmbu = new HashMap<String, Integer>();

		for (Ambulance am : ambu) {
			ambuCity.add(am.getCity());
		}

		for (String city : ambuCity) {
			int counterCityAmbu = 0;
			for (Ambulance am : ambu) {

				if (am.getCity().equals(city)) {
					counterCityAmbu++;
					counterAmbu.put(city, counterCityAmbu);

				}

			}
		}
		ArrayList<Integer> countersAmbuCity = new ArrayList<>();
		for (String ambulance : ambuCity) {
			countersAmbuCity.add(counterAmbu.get(ambulance));
		}

		ArrayList<String> rgbArrayListAmbulance = new ArrayList<String>();

		for (int i = 0; i < ambuCity.size(); i++) {
			int r = random.nextInt(256);
			int g = random.nextInt(256);
			int b = random.nextInt(256);

			rgbArrayListAmbulance.add("rgba(" + r + "," + g + "," + b + ",0.8)");
		}

		System.out.println(counterAmbu);
		model.addAttribute("ambulanceCity", ambuCity);
		model.addAttribute("ambulanceCityCounter", countersAmbuCity);
		model.addAttribute("rgbArrayListAmbulance", rgbArrayListAmbulance);

		return "charts";
	}

	@RequestMapping(value = "/deleteAed", method = RequestMethod.GET)
	public String handleDeleteUser(@RequestParam(name = "reqIdAed") String requestId, HttpServletResponse response) {

		aedRepo.deleteById(Integer.parseInt(requestId));
		return "redirect:/manageAeds";
	}

	@GetMapping("/profile")
	public String returnProfile(Model model, HttpServletResponse response) throws IOException {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		// List<User> users = userRepo.findAll();

		if (loggedinUser == null) {
			return "redirect:/login";
		}
		List<AED> allAeds = aedRepo.findAll();
		List<AED> userAed = new ArrayList<AED>();
		for (AED currentAed : allAeds) {
			User aedUser = currentAed.getUser();
			if (aedUser.getUserId() == loggedinUser.getUserId()) {
				userAed.add(currentAed);
			}

		}
		model.addAttribute("loggedinUser", loggedinUser);
		model.addAttribute("userAed", userAed);
		model.addAttribute("userAedSize", userAed.size());

		return "profile";

	}

	@RequestMapping(value = "/userProfile", method = RequestMethod.GET)
	public String handleShowUser(@RequestParam(name = "reqIdUser") String reqIdUser, Model model,
			HttpServletResponse response) throws IOException {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		List<User> users = userRepo.findAll();
		User myuser = null;
		for (User user : users) {
			if (user.getUserId() == Integer.parseInt(reqIdUser)) {
				myuser = user;
			}
		}

		model.addAttribute("loggedinUser", myuser);
		return "profile";

	}

	@GetMapping("/redirect")
	public void getAuth(HttpServletResponse response) throws IOException {
		User loggedinUser = getLoggedInUser();

		int isActive = loggedinUser.getIsActive();

		String myRole = loggedinUser.getRoles();

		switch (isActive) {
			case 1:
				if (myRole.equals("ADMIN")) {
					response.sendRedirect("/admin");

				} else if (myRole.equals("EKAB")) {
					response.sendRedirect("/ekab");

				} else if (myRole.equals("DIMOS")) {
					response.sendRedirect("/dimos");

				} else {
					response.sendRedirect("/");

				}
				break;
			default:
				break;
		}

		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

	}

	@GetMapping("/admin")
	public String admin(Model model, HttpServletResponse response) throws IOException {
		User loggedinUser = getLoggedInUser();
		int isActive = loggedinUser.getIsActive();

		model.addAttribute("loggedIn", loggedinUser);

		List<User> users = userRepo.findAll();
		List<User> userAsim = new ArrayList<>();
		List<User> userTil = new ArrayList<>();
		List<User> vip = new ArrayList<>();

		for (User user : users) {
			if (user.getRoles().equals("STELEXOS")) {
				vip.add(user);
			} else if (user.getRoles().equals("TILEFONITIS")) {
				userTil.add(user);
			} else if (user.getRoles().equals("ASIRMATISTIS")) {
				userAsim.add(user);
			}

		}

		// sort by city
		Collections.sort(vip, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return o1.getCity().compareTo(o2.getCity());
			}
		});

		Collections.sort(userAsim, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return o1.getCity().compareTo(o2.getCity());
			}
		});
		Collections.sort(userTil, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return o1.getCity().compareTo(o2.getCity());
			}
		});

		model.addAttribute("vip", vip);

		model.addAttribute("userAsim", userAsim);

		model.addAttribute("userTil", userTil);

		System.out.println(users);

		return "admin";
	}

	@RequestMapping("/403")
	public String accessDenied() {
		return "403";
	}

	@GetMapping("/")
	public String getDefault(Model model, HttpServletResponse response, HttpServletRequest request) throws IOException {

		Locale currentLocale = request.getLocale();
		String countryCode = currentLocale.getCountry();
		String countryName = currentLocale.getDisplayCountry();

		String langCode = currentLocale.getLanguage();
		String langName = currentLocale.getDisplayLanguage();

		System.out.println(countryCode + ": " + countryName);
		System.out.println(langCode + ": " + langName);

		// String[] languages = Locale.getISOLanguages();

		// for(String language : languages ) {
		// Locale locale = new Locale(language);
		// }

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		// List<User> users = userRepo.findAll();

		return "redirect:/dashboard";

	}

	public User getLoggedInUser() {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		boolean isEnabled = false;
		int isActive;

		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();

		} else {
			username = principal.toString();
		}

		List<User> users = userRepo.findAll();

		for (User user : users) {
			if (user.getUsername().equalsIgnoreCase(username)) {
				return user;
			}

		}
		return null;
	}

	/********* PRICING CALCULATOR CONTROLLER **********/

	@RequestMapping("/pricingCalculator")
	public String pricingCalculator(Model model, HttpServletResponse response) throws IOException {

		model.addAttribute("paymentDetails", new PaymentDetails());

		return "pricingCalculator";
	}

	/********* STRIPE PAYMENTS CONTROLLER **********/

	// Reading the value from the application.properties file
	@Value("${STRIPE_PUBLIC_KEY}")
	private String API_PUBLIC_KEY;

	private StripeService stripeService;

	@RequestMapping("/plans")
	public String plans(Model model, HttpServletResponse response) throws IOException {

		// User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),response);

		model.addAttribute("paymentDetails", new PaymentDetails());

		return "plans";
	}

	public ViewController(StripeService stripeService) {
		this.stripeService = stripeService;
	}

	@GetMapping("/success")
	public String success(Model model, @RequestParam(name = "ambNumber") String ambNumber,
			@RequestParam(name = "licensePerAmbPrice") String licensePerAmbPrice,
			@RequestParam(name = "ambPrice") String ambPrice, @RequestParam(name = "userNumber") String userNumber,
			@RequestParam(name = "licensePerUserPrice") String licensePerUserPrice,
			@RequestParam(name = "usersPrice") String usersPrice,
			@RequestParam(name = "adminNumber") String adminNumber,
			@RequestParam(name = "licensePerAdminPrice") String licensePerAdminPrice,
			@RequestParam(name = "adminsPrice") String adminsPrice,
			@RequestParam(name = "yearsNumber") String yearsNumber,
			@RequestParam(name = "planAmount") String planAmount, @RequestParam(name = "interval") String interval,
			HttpServletResponse response) throws IOException {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		int isActive = loggedinUser.getIsActive();

		model.addAttribute("ambNumber", ambNumber);
		model.addAttribute("licensePerAmbPrice", licensePerAmbPrice);
		model.addAttribute("ambPrice", ambPrice);
		model.addAttribute("userNumber", userNumber);
		model.addAttribute("licensePerUserPrice", licensePerUserPrice);
		model.addAttribute("usersPrice", usersPrice);
		model.addAttribute("adminNumber", adminNumber);
		model.addAttribute("licensePerAdminPrice", licensePerAdminPrice);
		model.addAttribute("adminsPrice", adminsPrice);
		model.addAttribute("yearsNumber", yearsNumber);
		model.addAttribute("planAmount", planAmount);
		model.addAttribute("interval", interval);

		return "success";
	}

	// small change to push vs code

	@PostMapping("/subscription")
	public String subscriptionPage(Model model, @ModelAttribute("paymentDetails") PaymentDetails paymentDetails,
			BindingResult result, HttpServletResponse response) throws IOException {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		model.addAttribute("stripePublicKey", API_PUBLIC_KEY);

		if (result.hasErrors()) {
			return "error";
		}

		model.addAttribute("ambNumber", paymentDetails.getAmbulances());
		model.addAttribute("userNumber", paymentDetails.getUsers());
		model.addAttribute("adminNumber", paymentDetails.getAdmins());
		model.addAttribute("yearsNumber", paymentDetails.getYears());

		int ambulancesNumber = Integer.parseInt(paymentDetails.getAmbulances());

		int usersNumber = Integer.parseInt(paymentDetails.getUsers());

		int adminsNumber = Integer.parseInt(paymentDetails.getAdmins());

		int yearsNumber = Integer.parseInt(paymentDetails.getYears());

		// TIER 1 PRICING

		// double tier1_year1_AmbPrice = 200,tier1_year2_AmbPrice =
		// 175,tier1_year3_AmbPrice = 150;
		//
		// double tier1_year1_UserPrice = 0,tier1_year2_UserPrice =
		// 0,tier1_year3_UserPrice = 0;
		//
		// double tier1_year1_AdminPrice = 0,tier1_year2_AdminPrice =
		// 0,tier1_year3_AdminPrice = 0;

		// TIER 2 PRICING

		// double tier2_year1_AmbPrice = 0,tier2_year2_AmbPrice = 0,tier2_year3_AmbPrice
		// =
		// 0;
		//
		// double tier2_year1_UserPrice = 0,tier2_year2_UserPrice =
		// 0,tier2_year3_UserPrice = 0;
		//
		// double tier2_year1_AdminPrice = 0,tier2_year2_AdminPrice =
		// 0,tier2_year3_AdminPrice = 0;

		// TIER 3 PRICING

		// double tier3_year1_AmbPrice = 0,tier3_year2_AmbPrice = 0,tier3_year3_AmbPrice
		// =
		// 0;
		//
		// double tier3_year1_UserPrice = 0,tier3_year2_UserPrice =
		// 0,tier3_year3_UserPrice = 0;
		//
		// double tier3_year1_AdminPrice = 0,tier3_year2_AdminPrice =
		// 0,tier3_year3_AdminPrice = 0;

		int ambPrice = 0, usersPrice = 0, adminsPrice = 0;

		int licensePerAmbPrice = 0, licensePerUserPrice = 0, licensePerAdminPrice = 0;

		/* --------------------------AMBULANCES----------------------------------- */

		if (ambulancesNumber >= 1 && ambulancesNumber <= 5) {

			System.out.println("Ambulance TIER 1");

			switch (yearsNumber) {
				case 1:
					// code block

					licensePerAmbPrice = 200;
					ambPrice = ambulancesNumber * licensePerAmbPrice;

					break;
				case 2:
					// code block

					licensePerAmbPrice = 175;
					ambPrice = ambulancesNumber * licensePerAmbPrice;

					break;
				case 3:

					// code block
					licensePerAmbPrice = 150;
					ambPrice = ambulancesNumber * licensePerAmbPrice;
					break;
				default:
					// code block
					ambPrice = 0;
			}

		}

		else if (ambulancesNumber >= 6 && ambulancesNumber <= 10) {

			System.out.println("Ambulance TIER 2");

			switch (yearsNumber) {
				case 1:
					// code block
					licensePerAmbPrice = 175;
					ambPrice = ambulancesNumber * licensePerAmbPrice;
					break;
				case 2:
					// code block
					licensePerAmbPrice = 150;
					ambPrice = ambulancesNumber * licensePerAmbPrice;
					break;
				case 3:
					// code block

					licensePerAmbPrice = 125;

					ambPrice = ambulancesNumber * licensePerAmbPrice;
					break;
				default:
					// code block
					ambPrice = 0;
			}

		}

		else if (ambulancesNumber >= 11 && ambulancesNumber <= 15) {
			System.out.println("Ambulance TIER 3");

			switch (yearsNumber) {
				case 1:
					// code block

					licensePerAmbPrice = 150;
					ambPrice = ambulancesNumber * licensePerAmbPrice;
					break;
				case 2:
					// code block
					licensePerAmbPrice = 125;
					ambPrice = ambulancesNumber * licensePerAmbPrice;
					break;
				case 3:
					// code block
					licensePerAmbPrice = 100;
					ambPrice = ambulancesNumber * licensePerAmbPrice;
					break;
				default:
					// code block
					ambPrice = 0;
			}

		}

		/* --------------------------USERS----------------------------------- */

		if (usersNumber >= 1 && usersNumber <= 5) {

			switch (yearsNumber) {
				case 1:
					// code block

					licensePerUserPrice = 500;
					usersPrice = usersNumber * licensePerUserPrice;
					break;
				case 2:
					// code block
					licensePerUserPrice = 450;
					usersPrice = usersNumber * licensePerUserPrice;
					break;
				case 3:
					// code block
					licensePerUserPrice = 400;
					usersPrice = usersNumber * licensePerUserPrice;
					break;
				default:
					// code block
					usersPrice = 0;
			}

		}

		else if (usersNumber >= 6 && usersNumber <= 10) {
			switch (yearsNumber) {
				case 1:
					// code block
					licensePerUserPrice = 450;
					usersPrice = usersNumber * licensePerUserPrice;
					break;
				case 2:
					// code block
					licensePerUserPrice = 400;
					usersPrice = usersNumber * licensePerUserPrice;
					break;
				case 3:
					// code block
					licensePerUserPrice = 350;
					usersPrice = usersNumber * licensePerUserPrice;
					break;
				default:
					// code block
					usersPrice = 0;
			}

		}

		else if (usersNumber >= 11 && usersNumber <= 15) {
			switch (yearsNumber) {
				case 1:
					// code block
					licensePerUserPrice = 400;
					usersPrice = usersNumber * licensePerUserPrice;
					break;
				case 2:
					// code block
					licensePerUserPrice = 350;
					usersPrice = usersNumber * licensePerUserPrice;
					break;
				case 3:
					// code block
					licensePerUserPrice = 300;
					usersPrice = usersNumber * licensePerUserPrice;
					break;
				default:
					// code block
					usersPrice = 0;
			}

		}
		/* -----------------------------ADMINS-------------------------------- */

		if (adminsNumber >= 1 && adminsNumber <= 5) {

			switch (yearsNumber) {
				case 1:
					// code block

					licensePerAdminPrice = 800;
					adminsPrice = adminsNumber * licensePerAdminPrice;
					break;
				case 2:
					// code block
					licensePerAdminPrice = 700;
					adminsPrice = adminsNumber * licensePerAdminPrice;
					break;
				case 3:
					// code block
					licensePerAdminPrice = 600;
					adminsPrice = adminsNumber * licensePerAdminPrice;
					break;
				default:
					// code block
					adminsPrice = 0;
			}

		}

		else if (adminsNumber >= 6 && adminsNumber <= 10) {
			switch (yearsNumber) {
				case 1:
					// code block
					licensePerAdminPrice = 700;
					adminsPrice = adminsNumber * licensePerAdminPrice;
					break;
				case 2:
					// code block
					licensePerAdminPrice = 600;
					adminsPrice = adminsNumber * licensePerAdminPrice;
					break;
				case 3:
					// code block
					licensePerAdminPrice = 500;
					adminsPrice = adminsNumber * licensePerAdminPrice;
					break;
				default:
					// code block
					adminsPrice = 0;
			}

		}

		else if (adminsNumber >= 11 && adminsNumber <= 15) {
			switch (yearsNumber) {
				case 1:
					// code block
					licensePerAdminPrice = 600;
					adminsPrice = adminsNumber * licensePerAdminPrice;
					break;
				case 2:
					// code block
					licensePerAdminPrice = 500;
					adminsPrice = adminsNumber * licensePerAdminPrice;
					break;
				case 3:
					// code block
					licensePerAdminPrice = 400;
					adminsPrice = adminsNumber * licensePerAdminPrice;
					break;
				default:
					// code block
					adminsPrice = 0;
			}

		}

		NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));

		model.addAttribute("licensePerAmbPrice", licensePerAmbPrice);
		String licensePerAmbPriceStr = nf.format(licensePerAmbPrice);
		model.addAttribute("licensePerAmbPriceStr", licensePerAmbPriceStr);

		model.addAttribute("ambPrice", ambPrice);
		String ambPriceStr = nf.format(ambPrice);
		model.addAttribute("ambPriceStr", ambPriceStr);

		model.addAttribute("licensePerUserPrice", licensePerUserPrice);
		String licensePerUserPriceStr = nf.format(licensePerUserPrice);
		model.addAttribute("licensePerUserPriceStr", licensePerUserPriceStr);

		model.addAttribute("usersPrice", usersPrice);
		String usersPriceStr = nf.format(usersPrice);
		model.addAttribute("usersPriceStr", usersPriceStr);

		model.addAttribute("licensePerAdminPrice", licensePerAdminPrice);
		String licensePerAdminPriceStr = nf.format(licensePerAdminPrice);
		model.addAttribute("licensePerAdminPriceStr", licensePerAdminPriceStr);

		model.addAttribute("adminsPrice", adminsPrice);
		String adminsPriceStr = nf.format(adminsPrice);
		model.addAttribute("adminsPriceStr", adminsPriceStr);

		/* ------------------------------------------------------------------------ */

		// monthly total pricing
		int totalMonthlyPrice;
		totalMonthlyPrice = ambPrice + usersPrice + adminsPrice;
		model.addAttribute("totalMonthlyPrice", totalMonthlyPrice);

		String totalMonthlyPriceStr = nf.format(totalMonthlyPrice);
		model.addAttribute("totalMonthlyPriceStr", totalMonthlyPriceStr);

		/* ------------------------------------------------------------------------ */

		// yearly total pricing
		int totalYearlyPrice;
		totalYearlyPrice = 12 * totalMonthlyPrice;
		model.addAttribute("totalYearlyPrice", totalYearlyPrice);

		String totalYearlyPriceStr = nf.format(totalYearlyPrice);
		model.addAttribute("totalYearlyPriceStr", totalYearlyPriceStr);

		/* ------------------------------------------------------------------------ */

		// discount for yearly payments
		double discountPercentage = 0.1;
		double discount;
		discount = (totalYearlyPrice * discountPercentage);
		discount = (int) discount;
		model.addAttribute("discount", discount);

		String discountStr = nf.format(discount);
		model.addAttribute("discountStr", discountStr);

		/* ------------------------------------------------------------------------ */

		// price of yearly payments
		double discountedYearlyPrice;
		discountedYearlyPrice = totalYearlyPrice - discount;

		int discountedYearlyPriceInt = (int) discountedYearlyPrice;
		model.addAttribute("discountedYearlyPrice", discountedYearlyPriceInt);

		String discountedYearlyPriceStr = nf.format(discountedYearlyPrice);
		model.addAttribute("discountedYearlyPriceStr", discountedYearlyPriceStr);

		/* ------------------------------------------------------------------------ */

		return "subscription";
	}

	// @GetMapping("/charge")
	// public String chargePage(Model model) {
	// model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
	// return "charge";
	// }

	@PostMapping("/create-subscription")
	public @ResponseBody Response createSubscription(String email, String interval, String token, String planAmount,
			String coupon, String ambNumber, String licensePerAmbPrice, String ambPrice, String userNumber,
			String licensePerUserPrice, String usersPrice, String adminNumber, String licensePerAdminPrice,
			String adminsPrice, String yearsNumber, HttpServletResponse response) throws StripeException, IOException {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		if (token == null || planAmount.isEmpty()) {
			return new Response(false, "Stripe payment token is missing. Please try again later.");
		}

		// 1st step create a customer

		String customerId = stripeService.createCustomer(email, token);

		if (customerId == null) {
			return new Response(false, "An error occurred while trying to create customer");
		}

		// 2nd step create product and plan

		String planId = stripeService.createPlan(planAmount, interval, email, ambNumber, licensePerAmbPrice, ambPrice,
				userNumber, licensePerUserPrice, usersPrice, adminNumber, licensePerAdminPrice, adminsPrice,
				yearsNumber);

		// 3rd step create tax rate

		String taxRate = "txr_1HlKT8JPJVx42IJ5OF7JCEKk";

		// 4th step create trial days

		int trialDays = 30;

		System.out.println("customer id: " + customerId + "\nplan id: " + planId + "\ntax rate: " + taxRate
				+ "\ntrial days: " + trialDays);

		// 5th step create subscription with customer,plan,coupon and tax rate

		// stripeService.createSubscription(customerId, planId, coupon, taxRate,
		// trialDays);

		String subscriptionID = stripeService.createSubscription(customerId, planId, coupon, trialDays);

		// Retrieve created subscription
		Subscription subscription = Subscription.retrieve(subscriptionID);

		// subscription.getStatus();

		long paymentDatelong = subscription.getCreated();

		System.out.println("Payment date long: " + paymentDatelong);

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Athens"));
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);

		month++; // auksanoyme ton mina kata 1 giati ksekinaei i arithmisi apo to 0

		String dayStr = String.valueOf(day);
		String monthStr = String.valueOf(month);
		String yearStr = String.valueOf(year);

		String paymentDate = dayStr + "/" + monthStr + "/" + yearStr;

		// String subscriptionId = "LifeTime Subscription from: " + email;

		/*
		 * --------------- SAVE PAYMENT DETAILS TO OUR DATABASE ---------------
		 */

		int userNumberInt = Integer.parseInt(userNumber);
		int ambNumberInt = Integer.parseInt(ambNumber);
		int adminNumberInt = Integer.parseInt(adminNumber);

		// User loggedinUser = getLoggedInUser();

		Payment payment = new Payment();

		payment.setUser(loggedinUser);

		payment.setSubscriptionName(subscriptionID);
		payment.setAmount(Integer.parseInt(planAmount));
		payment.setPaymentDate(paymentDate);

		payment.setUserUnit(userNumberInt);

		payment.setAmbulancesUnit(ambNumberInt);
		payment.setMasterUnit(adminNumberInt);

		payment.setUserPrice(Integer.parseInt(licensePerUserPrice));

		payment.setAmbulancesPrice(Double.parseDouble(licensePerAmbPrice));
		payment.setMasterPrice(Double.parseDouble(licensePerAdminPrice));
		payment.setYearsOfSubscription(Integer.parseInt(yearsNumber));
		payment.setPaymentInterval(interval);

		payment.setHospital(loggedinUser.getHospital());

		paymentRepo.save(payment);

		ArrayList<User> userLicense = new ArrayList();
		ArrayList<Ambulance> ambLicense = new ArrayList();
		ArrayList<User> adminLicense = new ArrayList();
		CustomPasswordEncoder customPasswordEncoder = new CustomPasswordEncoder();

		for (int i = 1; i <= userNumberInt; i++) {
			User userDefault = new User();

			String userName = subscriptionID + "_user_" + i;
			String userPassword = generateRandomPassword(10, 33, 126);

			// CustomPasswordEncoder customPasswordEncoder = new CustomPasswordEncoder();
			String encodedPass = customPasswordEncoder.encode(userPassword);

			userDefault.setUsername(userName);
			userDefault.setPassword(encodedPass);
			userDefault.setRoles("TILEFONITIS");
			userDefault.setHospital(loggedinUser.getHospital());
			userDefault.setIsActive(2);

			userRepo.save(userDefault);

			userDefault.setPassword(userPassword); // ksanavazoume sto entity gia password to plain gia na to steiloume
													// sto email
			userLicense.add(userDefault);

		}

		for (int i = 1; i <= ambNumberInt; i++) {

			Ambulance ambDefault = new Ambulance();

			String ambUserName = subscriptionID + "_amb_" + i;
			String ambPassword = generateRandomPassword(10, 33, 126);

			// CustomPasswordEncoder customPasswordEncoder = new CustomPasswordEncoder();
			String encodedPass = customPasswordEncoder.encode(ambPassword);

			System.out.println("Ambulance encoded pass :" + encodedPass);

			ambDefault.setUsername(ambUserName);
			ambDefault.setPassword(encodedPass);
			ambDefault.setHospital(loggedinUser.getHospital());
			ambDefault.setIsActive(2);
			ambulanceRepo.save(ambDefault);

			ambDefault.setPassword(ambPassword);
			ambLicense.add(ambDefault);

		}

		for (int i = 1; i <= adminNumberInt; i++) {

			User masterClinicDefault = new User();

			String adminUserName = subscriptionID + "_master_" + i;
			String adminPassword = generateRandomPassword(10, 33, 126);

			// CustomPasswordEncoder customPasswordEncoder = new CustomPasswordEncoder();
			String encodedPass = customPasswordEncoder.encode(adminPassword);

			System.out.println("Admin encoded pass :" + encodedPass);

			masterClinicDefault.setUsername(adminUserName);
			masterClinicDefault.setPassword(encodedPass);
			masterClinicDefault.setIsActive(2);
			masterClinicDefault.setRoles("MASTER_CLINIC");
			masterClinicDefault.setHospital(loggedinUser.getHospital());

			userRepo.save(masterClinicDefault);

			masterClinicDefault.setPassword(adminPassword);
			adminLicense.add(masterClinicDefault);

		}

		Mail mail = new Mail();
		mail.sendDefaultCredentialsMail(loggedinUser.getEmail(), "Default Credentials",
				loggedinUser.getHospital().getName(), userLicense, ambLicense, adminLicense);

		return new Response(true, "OK");

	}

	@PostMapping("/cancel-subscription")
	public @ResponseBody Response cancelSubscription(String subscriptionId) {

		boolean subscriptionStatus = stripeService.cancelSubscription(subscriptionId);

		if (!subscriptionStatus) {
			return new Response(false, "Faild to cancel subscription. Please try again later");
		}

		return new Response(true, "Subscription cancelled successfully");
	}

	@PostMapping("/coupon-validator")
	public @ResponseBody Response couponValidator(String code) {

		Coupon coupon = stripeService.retriveCoupon(code);

		if (coupon != null && coupon.getValid()) {
			String details = (coupon.getPercentOff() == null ? "$" + (coupon.getAmountOff() / 100)
					: coupon.getPercentOff() + "%") + "OFF" + coupon.getDuration();
			return new Response(true, details);
		}
		return new Response(false, "This coupon code is not available. This may be because it has expired or has "
				+ "already been applied to your account.");
	}

	// @PostMapping("/create-charge")
	// public @ResponseBody Response createCharge(String email, String token) {

	// if (token == null) {
	// return new Response(false, "Stripe payment token is missing. please try again
	// later.");
	// }

	// String chargeId = stripeService.createCharge(email, token, 99);// 9.99 usd

	// if (chargeId == null) {
	// return new Response(false, "An error accurred while trying to charge.");
	// }

	// // You may want to store charge id along with order information

	// return new Response(true, "Success your charge id is " + chargeId);
	// }

	// Function to generate random alpha-numeric password of specific length
	public static String generateRandomPassword(int len, int randNumOrigin, int randNumBound) {
		SecureRandom random = new SecureRandom();
		return random.ints(randNumOrigin, randNumBound + 1)
				.filter(i -> Character.isAlphabetic(i) || Character.isDigit(i)).limit(len)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
	}

	/*
	 * ---- SET USER CREDENTIALS ----
	 */

	@GetMapping("/setUserCredentials")
	public String setUserCredentials(Model model, @ModelAttribute("user") User user) {

		User loggedinUser = getLoggedInUser();

		String hospital = loggedinUser.getHospital().getName();

		String username = loggedinUser.getUsername();

		String role = loggedinUser.getRoles();

		model.addAttribute("hospitalName", hospital);
		model.addAttribute("username", username);
		model.addAttribute("role", role);

		model.addAttribute("user", new User());

		return "setUserCredentials";
	}

	@GetMapping("/communications")
	public String communications(Model model) {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		List<MarketingObject> allMarketingObjects = marketingRepo.findAll();

		List<MarketingObject> marketing = new ArrayList();

		for (MarketingObject marketingObject : allMarketingObjects) {

			if (marketingObject.getUser().getUserId() == loggedinUser.getUserId()) {
				marketing.add(marketingObject);
			}

		}

		model.addAttribute("loggedinUser", loggedinUser);
		model.addAttribute("marketing", marketing);

		return "communications2";
	}

	@GetMapping("/announcement")
	public String announcement(Model model) {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		model.addAttribute("loggedinUser", loggedinUser);

		return "announcement";
	}

	@GetMapping("/createAnnouncement")
	public String createAnnouncement(Model model) {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		model.addAttribute("loggedinUser", loggedinUser);

		return "createAnnouncement";
	}

	@GetMapping("/employeeChatList")
	public String employeeChatList(Model model) {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		List<User> allUsers = userRepo.findAll();

		List<User> hospitalUsers = new ArrayList();

		for (User user : allUsers) {

			if (user.getHospital() != null) {
				if (user.getHospital().getHospital_id() == loggedinUser.getHospital().getHospital_id()) {
					hospitalUsers.add(user);
				}
			}

		}

		model.addAttribute("loggedinUser", loggedinUser);
		model.addAttribute("hospitalUsers", hospitalUsers);

		return "employeeChatList";
	}

	@GetMapping("/employeeChats")
	public String employeeChats(Model model, @RequestParam(name = "userId") int userId) {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		Optional<User> userOptional = userRepo.findById(userId);

		User user = userOptional.get();

		model.addAttribute("loggedinUser", loggedinUser);
		model.addAttribute("user", user);

		return "employeeChats";
	}

	@GetMapping("/clientChatList")
	public String clientChatList(Model model) {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		List<User> allUsers = userRepo.findAll();

		List<User> hospitalUsers = new ArrayList();

		for (User user : allUsers) {

			if (user.getHospital() != null) {
				if (user.getHospital().getHospital_id() == loggedinUser.getHospital().getHospital_id()) {
					hospitalUsers.add(user);
				}
			}

		}

		model.addAttribute("loggedinUser", loggedinUser);
		model.addAttribute("hospitalUsers", hospitalUsers);

		return "clientChatList";
	}

	@GetMapping("/clientChats")
	public String clientChats(Model model) {

		User loggedinUser = getLoggedInUser();
		// handleIsActive(loggedinUser.getIsActive(),loggedinUser,response);

		model.addAttribute("loggedinUser", loggedinUser);

		return "clientChats";
	}

}
