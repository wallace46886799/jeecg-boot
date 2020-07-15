package org.jeecg.modules.dreamlabs.controller;




import java.util.HashMap;

import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试新的module
 * @author chengtg
 *
 */
@Slf4j
@Api(tags="新建module--gtja")
@RestController
@RequestMapping("/hello")
public class HelloController  {
	
 
	
	@GetMapping(value="/")
	public Result<String> hello(){
		log.info("hello word!");
		
		
		Result<String> result = new Result<String>();
		result.setResult("hello word!");
		result.setSuccess(true);
		return result;
	}
}