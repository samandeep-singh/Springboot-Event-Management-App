package ca.sheridancollege.beans;
import java.io.Serializable;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Event implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7951808128133256881L;
	String type;
	String location;
	String eTime;
	String eDate;
	String description;
	int maxTickets;
	int ticketPrice;
	Long eventId;
	
	public Event(String type, String location, String eTime, String eDate, String description, int maxTickets, int ticketPrice) {
		this.type = type;
		this.location = location;
		this.eTime = eTime;
		this.description=description;
		this.maxTickets=maxTickets;
		this.ticketPrice=ticketPrice;
		this.eDate=eDate;
	}
	

}
