package org.jeecg.config;

import java.math.BigDecimal;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dreamlabs.core.covert.BigDecimalConverter;

import cn.hutool.core.convert.ConverterRegistry;

@Configuration
public class ConverterConfig {
	
	@Bean
	 public ConverterRegistry converterRegistry() {
		ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
		//此处做为示例自定义String转换，因为Hutool中已经提供String转换，请尽量不要替换
		//替换可能引发关联转换异常（例如覆盖String转换会影响全局）
		converterRegistry.putCustom(BigDecimal.class, BigDecimalConverter.class);
		return converterRegistry;
	 }

}
