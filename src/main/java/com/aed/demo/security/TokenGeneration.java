package com.aed.demo.security;

import java.util.UUID;

public class TokenGeneration {
	
	public static String generateToken() {
	    final String uuid = UUID.randomUUID().toString().replace("-", "");
	    return uuid;
	}

}
