package com.pifii.test;

import org.junit.Test;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;

public class UpdatePasswordTest {
@Test
public void testchangePassword(){
	ShaPasswordEncoder sha = new ShaPasswordEncoder(256);
	String hash = sha.encodePassword("", "admin");
	System.out.println(hash);
 }


}
