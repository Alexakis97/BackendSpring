package com.aed.demo.security;



import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;






public class CustomPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {


		String hashedString = BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(12));
		System.out.println("before hash: "+rawPassword.toString());
//
//		System.out.println("after hash: "+hashedString);
//		return hashedString;
		return hashedString;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		System.out.println("checks if two passwords matches each other");
		
		
		System.out.println("Raw to string: "+rawPassword.toString());
		//System.out.println("Raw to db"+BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(12)));
		System.out.println("Encoded: "+encodedPassword);

		System.out.println(BCrypt.checkpw(rawPassword.toString(), encodedPassword));
		
		return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
		
		
	}
	
	

}
