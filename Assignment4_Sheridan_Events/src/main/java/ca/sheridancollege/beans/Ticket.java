package ca.sheridancollege.beans;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ticket implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 3664386182886008015L;
	private Long eventId;
	private Long userId;
	private String ticketNumber;

}
