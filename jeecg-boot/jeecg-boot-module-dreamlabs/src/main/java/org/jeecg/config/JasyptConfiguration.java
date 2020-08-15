/**
 * 
 */
package org.jeecg.config;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.dreamlabs.core.jasypt.JasyptStringEncryptor;

/**
 * @author Frank
 *
 */
@Configuration
public class JasyptConfiguration {
	
	
   @Bean("jasyptStringEncryptor")
   public StringEncryptor stringEncryptor(Environment environment){
       return new JasyptStringEncryptor(environment);
   }
}
