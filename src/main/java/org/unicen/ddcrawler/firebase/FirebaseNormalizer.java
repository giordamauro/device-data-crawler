package org.unicen.ddcrawler.firebase;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class FirebaseNormalizer {

	public String normalizeKey(String key){
		
		Objects.requireNonNull(key, "Key cannot be null") ;
		
		if(key.isEmpty()){
			return "value";
	    }
	    	
		String normalizedKey = Optional.of(key.indexOf(" ("))
				.filter(indexOfP -> indexOfP != -1)
				.map(indexOfP -> key.substring(indexOfP + 2, key.indexOf(")")))
				.map(betweenParenthesis -> key.replace(" (" + betweenParenthesis + ")", ""))
				.orElse(key);	
				
		return normalizedKey.toLowerCase()
				.replaceAll("[^a-z0-9]" , "-");
	}
	
	public Object normalizeValue(Object value){
		
		Objects.requireNonNull(value, "Value cannot be null") ;
		
		if(String.class.isAssignableFrom(value.getClass())){
			try {
				return Long.valueOf((String) value);
			}
			catch(Exception e){
			}
		}
		
		return value;
	}
}
