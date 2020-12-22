package com.aed.demo.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA512 {
	
	   public static String hash(String toHash) throws NoSuchAlgorithmException {
	        MessageDigest md = MessageDigest.getInstance("SHA-512");
	        byte[] digest = md.digest(toHash.getBytes());
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < digest.length; i++) {
	            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        System.out.println(sb);
	        return sb.toString();
	    }
}
