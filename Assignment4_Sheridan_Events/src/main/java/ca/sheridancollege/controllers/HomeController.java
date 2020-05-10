package ca.sheridancollege.controllers;

import java.util.ArrayList;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.EmailService.EmailServiceImpl;
import ca.sheridancollege.beans.Event;
import ca.sheridancollege.beans.Ticket;
import ca.sheridancollege.beans.User;
import ca.sheridancollege.database.DatabaseAccess;
import ca.sheridancollege.security.OneTimePassword;


@Controller
public class HomeController {
	
	@Autowired
	@Lazy
	private DatabaseAccess da;
	
	@Autowired
	EmailServiceImpl esi;
	
	@Autowired 
	OneTimePassword otp;
	
	String otpStr = OneTimePassword.getOTP(8);
	String username1 = "";
	@GetMapping("/")
	public String goHome() {
		return "redirect:/member/event.view";
	}
	@GetMapping("/member/event.view")
	public String goView(Model model, Authentication auth) {
		model.addAttribute("events",da.getAllEvents());
		ArrayList<String> roles= new ArrayList<String>();
		for(GrantedAuthority ga: auth.getAuthorities()) {
			roles.add(ga.getAuthority());
		}
		for (String st : roles) {
			if(st.contains("ROLE_MEMBER")) {
				return "users/viewEvents.html";
			}
			if(st.contains("ROLE_ADMIN")) {
				return "users/viewEventsAdmin.html";
			}
		}
		return "users/viewEvents.html";
	}
	@GetMapping("/admin/event.add.form")
	public String addEvent() {
		return "users/updated-addEvent.html";
	}
    @GetMapping("/admin/event.add")
    public String goAdd(@RequestParam String type, @RequestParam String location, @RequestParam String
    		eTime, @RequestParam String eDate, @RequestParam String description, @RequestParam int  maxTickets,
    		@RequestParam int ticketPrice){
    	da.addEvent(new Event(type, location, eTime, eDate, description, maxTickets, ticketPrice));
    	return "redirect:/member/event.view" ;
    }
	@GetMapping("/admin/event.modify.form/{id}")
	public String modifyEvent(@PathVariable Long id, Model model) {
		model.addAttribute(da.getEventById(id));
		return "users/updated-editEvent.html";
	}

    @GetMapping("/admin/event.modify")
    public String modifyEvent(@RequestParam Long eventId, @RequestParam String type, @RequestParam String location, @RequestParam String
    		eTime, @RequestParam String eDate, @RequestParam String description, @RequestParam int  maxTickets,
    		@RequestParam int ticketPrice) {
    	
    	da.updateEvent(new Event(type, location, eTime, eDate, description, maxTickets, ticketPrice, eventId));
    	return "redirect:/member/event.view";
    }
    @GetMapping("/admin/event.delete/{id}")
    public String deleteEvent(@PathVariable int id) {
    	da.deleteEvent(id);
    	return "redirect:/member/event.view";
    }
    @GetMapping("/member/buyTickets/{id}")
    public String buyTickets(@PathVariable Long id, Authentication auth) {
    	da.buyTicket(id);
    	String ticketNumber = OneTimePassword.getOTP(5);
    	DatabaseAccess.addTicket(da.findUserAccount(auth.getName()).getUserId(), id, ticketNumber);
    	
    	return "redirect:/member/sendemailinline";
    	
    }
    @GetMapping("/member/myTickets")
    public String myTickets(Authentication auth, Model model){
    	ArrayList<Event> events = new ArrayList<Event>();
    	ArrayList<Ticket> tickets= da.getTicketsByUserId(da.findUserAccount(auth.getName()).getUserId());
    	for(Ticket t: tickets) {
    		events.add(da.getEventById(t.getEventId()));
    	}
    	model.addAttribute("events", events);
    	return "users/myTickets.html";
    }
    @GetMapping("/member/cancelTickets/{id}")
    public String cancelTickets(@PathVariable Long id, Authentication auth) {
    	da.cancelTicket(id);
    	DatabaseAccess.deleteTicket(da.findUserAccount(auth.getName()).getUserId(), id);
    	return "redirect:/member/myTickets";
    	
    }
    @GetMapping("/login")
    public String login() {
        return "Updated-login.html";
    }
    @GetMapping("/forgotPassword")
    public String forgotPasswrd() {
		
    	return "updated-forgotPassword.html";
    }
    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam String userName) {
    	
    	username1 = userName;
    	try {
    	User user = da.findUserAccount(userName);
		esi.sendMailWithInline(user.getFullName(), "Here is your email reset one time password: " + otpStr,"", user.getEmail(),"Account Reset");

    	}
    	catch (Exception e) {
    		System.out.println(e);
    	}
        
    	return "updated-OTP.html";
    	
    }
    @PostMapping("/otpConfirmation")
    public String otpConfirmation(@RequestParam String otpInput) {
    	if(otpInput.equals(otpStr)){
    		return "updated-resetPassword.html";
    	}
    	return "redirect:/login";
}
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String newPassword) {
    	DatabaseAccess.resetPassword(username1, encryptPassword(newPassword));
    	return "redirect:/login";
    
    }
    @GetMapping("/dummy")
    public String generateDummy() {
    	da.addDummyRecords();
    	return "redirect:/member/event.view";
    }
    

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access_denied.html";
    }
	@GetMapping("/register")
	public String goRegistration () {
		return "updated-register.html";
	}
	@PostMapping("/register")
	public String doRegistration(@RequestParam String username,
						@RequestParam String password, @RequestParam String user, @RequestParam String fullName,
						@RequestParam String DOB, @RequestParam String email, @RequestParam String phone) {
		
		DatabaseAccess.addUser(new User(username, encryptPassword(password), fullName, email, phone, DOB));
		long userId = da.findUserAccount(username).getUserId();
		if(user.contains("member")) {
		DatabaseAccess.addRole(userId, 2);
		}
		if(user.contains("admin")) {
		DatabaseAccess.addRole(userId, 1);
		}

		return "redirect:/login";
	}
	@GetMapping("/member/event.search")
	public String srch(@RequestParam String searchTerm, @RequestParam String searchType, Model model){
		ArrayList<Event> event = new ArrayList<Event>();
		if(searchType.equals("byLocation")) {
			event.addAll(da.getEventByLocation(searchTerm));
		}
		if(searchType.equals("byDate")) {
			event.addAll(da.getEventByDate(searchTerm));
		}
		if(searchType.equals("byType")) {
			event.addAll(da.getEventByType(searchTerm));
		}
		if(searchType.equals("byPrice")) {
			int param1 = Integer.parseInt(searchTerm.split(",")[0]);
			int param2 = Integer.parseInt(searchTerm.split(",")[1]);
			event.addAll(da.getEventByPrice(param1, param2));
		}
		if(searchType.equals("byTickets")) {
			int param1 = Integer.parseInt(searchTerm.split(",")[0]);
			int param2 = Integer.parseInt(searchTerm.split(",")[1]);
			event.addAll(da.getEventByQty(param1, param2));
		}
		model.addAttribute("events", event);
		return "users/viewEvents.html";
	}
	public static String encryptPassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}
	@GetMapping("/member/sendemail")
	public String email(Authentication auth) {
		//esi.sendSimpleMessage(da.findUserAccount(auth.getName()).getEmail(), "Test", "Test");
		System.out.println(da.findUserAccount(auth.getName()).getEmail());
    return "redirect:/member/event.view";	
}
	
	@GetMapping("/member/sendemailinline")
	public String test2(Authentication auth){
		try {
			esi.sendMailWithInline(da.findUserAccount(auth.getName()).getFullName(), "Your ticket has been confirmed", "Thank You", da.findUserAccount(auth.getName()).getEmail(),"Ticket Confirmation");
		}
		catch(MessagingException e){
			System.out.println(e);
		}
	return "redirect:/member/myTickets";
	}

}
