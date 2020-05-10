package ca.sheridancollege.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.beans.Event;
import ca.sheridancollege.beans.Ticket;
import ca.sheridancollege.beans.User;

@Repository
public class DatabaseAccess {
	@Autowired
	protected NamedParameterJdbcTemplate jdbc;
	
	public void addEvent(Event e) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "insert into event (location, eTime, eDate, type, description, maxTickets, ticketPrice)" + 
				"values (:location, :time, :date, :type,:description," + 
				":maxTickets, :ticketPrice);";
		parameters.addValue("location", e.getLocation());
		parameters.addValue("time", e.getETime());
		parameters.addValue("date", e.getEDate());
		parameters.addValue("type", e.getType());
		parameters.addValue("description", e.getDescription());
		parameters.addValue("maxTickets", e.getMaxTickets());
		parameters.addValue("ticketPrice", e.getTicketPrice());
		
		jdbc.update(query, parameters);
	}
	public void updateEvent(Event e) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "UPDATE event SET location=:location, "
				+ "eTime=:time, eDate=:date, type=:type, description=:description,"
				+ "maxTickets=:maxTickets, ticketPrice=:ticketPrice WHERE eventId=:eventId";
		parameters.addValue("location", e.getLocation());
		parameters.addValue("time", e.getETime());
		parameters.addValue("date", e.getEDate());
		parameters.addValue("type", e.getType());
		parameters.addValue("description", e.getDescription());
		parameters.addValue("maxTickets", e.getMaxTickets());
		parameters.addValue("ticketPrice", e.getTicketPrice());
		parameters.addValue("eventId", e.getEventId());
		
		jdbc.update(query, parameters);
	}
	public void buyTicket(Long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		int newQty = this.getEventById(id).getMaxTickets() - 1;
		String query = "UPDATE event SET maxTickets=:newQty WHERE eventId=:id";
		parameters.addValue("newQty", newQty);
		parameters.addValue("id", id);

		
		jdbc.update(query, parameters);
	}
	public void cancelTicket(Long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		int newQty = this.getEventById(id).getMaxTickets() + 1;
		String query = "UPDATE event SET maxTickets=:newQty WHERE eventId=:id";
		parameters.addValue("newQty", newQty);
		parameters.addValue("id", id);

		
		jdbc.update(query, parameters);
	}
	public void addDummyRecords() {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query="Insert into event (location, eTime, eDate, type, description, maxTickets, ticketPrice) Values "+
		"('Mississauga', '11AM', '2019-11-11', 'Seminar','Event event event!',100, 20),"+
		"('Brampton','12AM','2019-11-13','Job Fair','Get Hired today',98,10),"+
		"('Oakville','2PM','2019-10-04','Movie Night','Enjoy Free Popcorn',20,15),"+
		"('Brampton','11PM','2019-11-09','Desi Jam','Bollywood Music',50,5),"+
		"('Brampton','8PM','2019-07-19','Paint Day','Paint your heart out',10,10),"+
		"('Oakville','8PM','2019-11-09','Dance With Me','Dance dance dance!',30,5),"+
		"('Mississauga','5PM','2019-11-10','Alumni Meet','Remember your Past',200,20),"+
		"('Brampton','11PM','2019-09-09','Paw-licious','Meet therapy dogs',89,20),"+
		"('Mississauga','4PM','2019-08-08','Netflix-and-Chill','Watch netflix with your lover',50,9),"+
		"('Brampton','5PM','2019-04-29','Design and Win','Show us your skills',100,9),"+
		"('Oakville','3PM','2019-07-20','Meet and greet','meet your favourite pop stars',500,90),"+
		"('Mississauga','8PM','2019-11-19','Horror night',' Watch horror movies',50,5);";
		jdbc.update(query, parameters);

		}
	
	public void deleteEvent(int eventId) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "DELETE from event where eventId=:eventId";
		parameters.addValue("eventId", eventId);
		
		jdbc.update(query,parameters);
	}
	public ArrayList<Event> getAllEvents() {
		String query = "SELECT * FROM event";
		ArrayList<Event> events = 
				(ArrayList<Event>)jdbc.query(query,
				new BeanPropertyRowMapper<Event>(Event.class));
		return events;
		
	}
	public Event getEventById(Long eventId) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM event WHERE eventId=:eventId";
		parameters.addValue("eventId", eventId);
		ArrayList<Event> events = 
				(ArrayList<Event>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<Event>(Event.class));
		if (events.size()>0)
			return events.get(0);
		else
			return null;
		
	}
	public ArrayList<Event> getEventByLocation(String location){
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM event WHERE location=:location";
		parameters.addValue("location", location);
		ArrayList<Event> event = 
				(ArrayList<Event>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<Event>(Event.class));
		return event;
			
		}
	public ArrayList<Event> getEventByType(String type){
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM event WHERE type=:type";
		parameters.addValue("type", type);
		ArrayList<Event> event = 
				(ArrayList<Event>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<Event>(Event.class));
		return event;
			
		}
	public ArrayList<Event> getEventByDate(String date){
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM event WHERE eDate=:date";
		parameters.addValue("date", date);
		ArrayList<Event> event = 
				(ArrayList<Event>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<Event>(Event.class));
		return event;
			
		}
	public ArrayList<Event> getEventByPrice(int param1, int param2) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM event WHERE ticketPrice>=:param1 and ticketPrice<=:param2";
		parameters.addValue("param1", param1);
		parameters.addValue("param2", param2);
		ArrayList<Event> event = 
				(ArrayList<Event>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<Event>(Event.class));
		return event;
	}
	public ArrayList<Event> getEventByQty(int param1, int param2) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM event WHERE maxTickets>=:param1 and maxTickets<=:param2";
		parameters.addValue("param1", param1);
		parameters.addValue("param2", param2);
		ArrayList<Event> event = 
				(ArrayList<Event>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<Event>(Event.class));
		return event;
	}
	public ArrayList<Ticket> getTicketsByUserId(long userId) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String qu = "Select * from user_event WHERE userId = :userId";
		parameters.addValue("userId", userId);
		ArrayList<Ticket> ar = new ArrayList<>();
		List<Map<String, Object>> rows = jdbc.queryForList(qu, parameters);
		for(Map<String, Object> row : rows) {
			Ticket tk = new Ticket();
			tk.setUserId((long)(row.get("userId")));
			tk.setEventId((long)(row.get("eventId")));
			tk.setTicketNumber((String)(row.get("ticketNumber")));
            ar.add(tk);
			
		}
		return ar;
	}
	
	
	public User findUserAccount(String userName) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM sec_user where userName=:userName";
		parameters.addValue("userName", userName);
		ArrayList<User> users = (ArrayList<User>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<User>(User.class));
		if (users.size()>0)
			return users.get(0);
		else
			return null;
	}
	public User findUserAccountByEmail(String email) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM sec_user where userName=:userName";
		parameters.addValue("userName", email);
		ArrayList<User> users = (ArrayList<User>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<User>(User.class));
		if (users.size()>0)
			return users.get(0);
		else
			return null;
	}
	public List<String> getRolesById(long userId) {
		ArrayList<String> roles = new ArrayList<String>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "select user_role.userId, sec_role.roleName "
				+ "FROM user_role, sec_role "
				+ "WHERE user_role.roleId=sec_role.roleId "
				+ "and userId=:userId";
		parameters.addValue("userId", userId);
		List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);
		for (Map<String, Object> row : rows) {
			roles.add((String)row.get("roleName"));
		}
		return roles;
	}
	public static void addUser(User user) {
		try {
            		Class.forName("com.mysql.cj.jdbc.Driver");
            		Connection conn = null; 
            		conn = DriverManager.getConnection
            		("jdbc:mysql://localhost/Assignment4?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "root@123");        
			String q = 
				"insert into SEC_User "
				+ "(USERNAME, ENCRYPTEDPASSWORD, EMAIL, FULLNAME, PHONE, DOB, ENABLED)" 
				+ " values (?,?,?,?,?,?,1)";
			PreparedStatement ps = conn.prepareStatement(q);
			
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getEncryptedPassword());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getFullName());
			ps.setString(5, user.getPhone());
			ps.setString(6, user.getDOB());
			ps.executeUpdate();	
			
            		conn.close();
        	} catch (Exception e) {
            		System.out.println(e);
        	}
	}
	public static void resetPassword(String username, String password) {
		try {
            		Class.forName("com.mysql.cj.jdbc.Driver");
            		Connection conn = null; 
            		conn = DriverManager.getConnection
            		("jdbc:mysql://localhost/Assignment4?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "root@123");        
			String q = 
				"UPDATE SEC_User "
				+ "SET encryptedPassword = ?" 
				+ "WHERE userName = ?";
			PreparedStatement ps = conn.prepareStatement(q);
			
			ps.setString(1, password);
			ps.setString(2, username);

			ps.executeUpdate();	
			
            		conn.close();
        	} catch (Exception e) {
            		System.out.println(e);
        	}
	}
	public static void addRole(long userId, long roleId) {
		try {
       		Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = null; 
			conn = DriverManager.getConnection
				("jdbc:mysql://localhost/Assignment4?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "root@123");
			String q = 
				"insert into user_role (USERID, ROLEID)" + 
				"values (?, ?);";
			PreparedStatement ps = conn.prepareStatement(q);
			
			ps.setLong(1, userId);
			ps.setLong(2, roleId);
			ps.executeUpdate();	
			
            		conn.close();
        	} catch (Exception e) {
            		System.out.println(e);
        	}
	}
	public static void addTicket(long userId, long eventId, String ticketNumber) {
		try {
       		Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = null; 
			conn = DriverManager.getConnection
				("jdbc:mysql://localhost/Assignment4?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "root@123");
			String q = 
				"insert into user_event (USERID, EVENTID, TICKETNUMBER)" + 
				"values (?, ?, ?);";
			PreparedStatement ps = conn.prepareStatement(q);
			
			ps.setLong(1, userId);
			ps.setLong(2, eventId);
			ps.setString(3, ticketNumber);
			ps.executeUpdate();	
			
            		conn.close();
        	} catch (Exception e) {
            		System.out.println(e);
        	}
	}
	public static void deleteTicket(long userId, long eventId) {
		try {
       		Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = null; 
			conn = DriverManager.getConnection
				("jdbc:mysql://localhost/Assignment4?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "root@123");
			String q = 
				"DELETE FROM user_event WHERE userId = ? AND eventId = ?";
			PreparedStatement ps = conn.prepareStatement(q);
			
			ps.setLong(1, userId);
			ps.setLong(2, eventId);
			ps.executeUpdate();	
			
            		conn.close();
        	} catch (Exception e) {
            		System.out.println(e);
        	}
	}




}
