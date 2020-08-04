package org.jeecg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.logger.controller.aspect.GenericControllerAspect;

/**
 * @Author: fumin
 * @Description:
 * @Date: Create in 2019/5/20 10:26
 */
@Configuration
public class ControllerLogConfig {
	
	@Bean
	 public GenericControllerAspect genericControllerAspect() {
	     return new GenericControllerAspect();
	 }
}
