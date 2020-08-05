/**
 * 
 */
package com.dreamlabs.junit;

import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Frank
 *
 */
@Slf4j
public class BaseTest {
	
	public String encrypt(String value) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		String password = System.getProperty("jasypt.encryptor.password");
		textEncryptor.setPassword(password);
		String encryptStr = textEncryptor.encrypt(value);
		log.info("encrypt:{}",encryptStr);
		return encryptStr;
	}
	
	public String decrypt(String value) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		String password = System.getProperty("jasypt.encryptor.password");
		textEncryptor.setPassword(password);
		String decryptStr = textEncryptor.decrypt(value);
		log.info("decrypt:{}",decryptStr);
		return decryptStr;
	}
	
	
	public String enryptPassword() {
		return this.encrypt("19820801");
	}
	
	
	public String decrypt8Password() {
		return this.decrypt("X35aiauugJDaHj3hwEY/5Q==");
	}
	
	public String decrypt1Password() {
		return this.decrypt("nWdzarCvPzGmXRuwoo7G49XzWlcAEsX3");
	}
	
	public String decryptzPassword() {
		return this.decrypt("tWRfA+cYK6wJJVtBGvYaDTcTFTHg/51B");
	}
	
	public String decryptfPassword() {
		return this.decrypt("GHXAZsREkPOtdc+Ls0vPwdLTRj55u8an");
	}
	
	@Test
	public void enrypting() {
		this.encrypt("8*****");
	}
	
	/**
	 * 8*****:X35aiauugJDaHj3hwEY/5Q==
	 * 1*******:nWdzarCvPzGmXRuwoo7G49XzWlcAEsX3
	 * z*******:tWRfA+cYK6wJJVtBGvYaDTcTFTHg/51B
	 * *f********:GHXAZsREkPOtdc+Ls0vPwdLTRj55u8an
	 */
	@Test
	public void decrypting() {
		this.decrypt("X35aiauugJDaHj3hwEY/5Q==");
	}
	
	
}
