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

@Test
public void testBase(){
		//高容错
		//分布式
		//SOA
		//可扩展
		//RPC remote procedure call 远程方法调用
		//REST FUL
		//并发
	
	
	
	}

}
