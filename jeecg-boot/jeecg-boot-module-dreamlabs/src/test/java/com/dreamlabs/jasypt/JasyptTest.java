package com.dreamlabs.jasypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Before;
import org.junit.Test;

import com.dreamlabs.core.jasypt.JasyptStringEncryptor;
/**
 * cd /Users/Frank/Work/Frameworks/GitHub/jeecg-boot/jeecg-boot/jeecg-boot-module-system
 * mvn jasypt:encrypt-value -Djasypt.encryptor.password="******" -Djasypt.plugin.value="UFYEBYSGNMZUCRFW" <br>
 * mvn jasypt:decrypt-value -Djasypt.encryptor.password="******" -Djasypt.plugin.value="PRU/DRSVkHGQ9x/XSbDiZr6jFHI4SmqAac1BZv8zlz35TG4hs1gFIP5J5+TX8HHX" <br>
 * ENC(PRU/DRSVkHGQ9x/XSbDiZr6jFHI4SmqAac1BZv8zlz35TG4hs1gFIP5J5+TX8HHX)<br>
 * ENC(PRU/DRSVkHGQ9x/XSbDiZr6jFHI4SmqAac1BZv8zlz35TG4hs1gFIP5J5+TX8HHX)
 * @author Frank
 *
 */
public class JasyptTest {
	
	private StandardPBEStringEncryptor stringEncryptor = null;
	
	@Before
	public void init() {
		stringEncryptor = new StandardPBEStringEncryptor();
		stringEncryptor.setPassword("******");
	}
	
	
	@Test
	public void encrypt() {
		System.out.println(stringEncryptor.encrypt("******"));
		System.out.println(stringEncryptor.encrypt("******"));
	}
	
	
	@Test
	public void decrypt() {
		/*
		 * q7V14/YDcOF5+Z5Vbk5+ow== 
		 * LQAIBfExqzRAQ128vwb0Ik1lwR1EeTs4
		 * SheBXQRWENvanOrTmDp82yUxjzC560ID
		 */

		System.out.println(stringEncryptor.decrypt("q7V14/YDcOF5+Z5Vbk5+ow=="));
		System.out.println(stringEncryptor.decrypt("LQAIBfExqzRAQ128vwb0Ik1lwR1EeTs4"));
		System.out.println(stringEncryptor.decrypt("SheBXQRWENvanOrTmDp82yUxjzC560ID"));
		
	}
	
	
	
	@Test
	public void encrypt_jse() {
		JasyptStringEncryptor jse = new JasyptStringEncryptor("******");
		System.out.println(jse.encrypt("a2YjuaoEItgbMxEcG+jjcA=="));
		System.out.println(jse.encrypt("Os8Nb73PltCoOaDeW5LfW+Cat7o9Fq9c"));
		System.out.println(jse.encrypt("QDggDqSa5nP62kJQNYcRugjya8y2GW9t"));
		System.out.println(jse.encrypt("VKF7e7forba9ii1jyic5j2L7cL4/K4gOyDdEysSWFi4="));
	}
	
	
	@Test
	public void decrypt_jse() {
		/*
		 * q7V14/YDcOF5+Z5Vbk5+ow== 
		 * LQAIBfExqzRAQ128vwb0Ik1lwR1EeTs4
		 * SheBXQRWENvanOrTmDp82yUxjzC560ID
		 */
		JasyptStringEncryptor jse = new JasyptStringEncryptor("******");
		System.out.println(jse.decrypt("a2YjuaoEItgbMxEcG+jjcA=="));
		System.out.println(jse.decrypt("Os8Nb73PltCoOaDeW5LfW+Cat7o9Fq9c"));
		System.out.println(jse.decrypt("QDggDqSa5nP62kJQNYcRugjya8y2GW9t"));
		System.out.println(jse.decrypt("VKF7e7forba9ii1jyic5j2L7cL4/K4gOyDdEysSWFi4="));
		
		//H21hulitLjy03XaTY2cnfA==
		//Xi2OOaDF+8tIm/amjeLborAFT+IDd2I4
		//2cVaSCRO5rWJLuaLz7unpHklKmfSjiV/
		
	}
}
