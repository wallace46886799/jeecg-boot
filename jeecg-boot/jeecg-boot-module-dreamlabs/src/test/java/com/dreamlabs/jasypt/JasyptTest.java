package com.dreamlabs.jasypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Before;
import org.junit.Test;
/**
 * cd /Users/Frank/Work/Frameworks/GitHub/jeecg-boot/jeecg-boot/jeecg-boot-module-system
 * mvn jasypt:encrypt-value -Djasypt.encryptor.password="828100" -Djasypt.plugin.value="frank@bj828100" <br>
 * mvn jasypt:decrypt-value -Djasypt.encryptor.password="828100" -Djasypt.plugin.value="PRU/DRSVkHGQ9x/XSbDiZr6jFHI4SmqAac1BZv8zlz35TG4hs1gFIP5J5+TX8HHX" <br>
 * ENC(PRU/DRSVkHGQ9x/XSbDiZr6jFHI4SmqAac1BZv8zlz35TG4hs1gFIP5J5+TX8HHX)<br>
 * @author Frank
 *
 */
public class JasyptTest {
	
	private StandardPBEStringEncryptor stringEncryptor = null;
	
	@Before
	public void init() {
		stringEncryptor = new StandardPBEStringEncryptor();
		stringEncryptor.setPassword("828100");
	}
	
	
	@Test
	public void encrypt() {
		System.out.println(stringEncryptor.encrypt("123456"));
		System.out.println(stringEncryptor.encrypt("frank@bj828100"));
		System.out.println(stringEncryptor.encrypt("jeecg1314"));
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
	
}
