package com.recruitment.crud.recruitmentapi;

import java.util.Base64;

import javax.crypto.Mac;

import javax.crypto.spec.SecretKeySpec;

public class jwt {
	public String generateJWTToken(String secretKey) throws RuntimeException {
	
		   String header = "{\"typ\":\"JWT\",\"alg\":\"HS256\"}";
		
		   String base64UrlHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes());
		
	
		
		   // JWT token expires 60 seconds from now
		
		   long timeSecs = (System.currentTimeMillis() / 1000) + 60;
		
		 
		
		   String payload = "{\"iss\":\"some_key\",\"exp\":" + String.valueOf(timeSecs) + "}";
		
		   String base64UrlPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
		
		 
		
		   try {
		
		      String base64UrlSignature = hmacEncode(base64UrlHeader + "." + base64UrlPayload, secretKey);
		
		 
		
		      return base64UrlHeader + "." + base64UrlPayload + "." + base64UrlSignature;
		
		   } catch (Exception e) {
		
		      throw new RuntimeException("Unable to generate a JWT token.");
		
		   }
		
		}
	
	private String hmacEncode(String data, String key) throws Exception {
		
		   Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		
		   SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
		
		   sha256_HMAC.init(secret_key);
		
		 
		
		   return Base64.getUrlEncoder().withoutPadding().encodeToString(sha256_HMAC.doFinal(data.getBytes()));
		
		}


}
