package ca.sheridancollege.beans;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5510929188980109810L;
	private String username;
	private String fullName;
	private String encryptedPassword;
	private String email;
	private String phone;
	private String DOB;
	private String role;
	private Long userId;
	private Long eventId;
	private Long ticketNumber;
	
	public User(String username, String password, String fullName, String email, String phone, String dob) {
		this.username = username;
	    this.encryptedPassword = password;
	    this.fullName = fullName;
	    this.email = email;
	    this.phone = phone;
	    this.DOB = dob;
	}
	

}
