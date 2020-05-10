package ca.sheridancollege.security;

import org.springframework.stereotype.Repository;

@Repository
public class OneTimePassword {
	
	public static String getOTP(int size) 
    { 
   
        String data = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz"; 
  
        StringBuilder sb = new StringBuilder(size); 
  
        for (int i = 0; i < size; i++) { 
            int j = (int)(data.length() * Math.random()); 
            sb.append(data.charAt(j)); 
        } 
  
        return sb.toString(); 
    } 

}
