package com.dreamlabs.json;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jeecg.modules.dreamlabs.transaction.FundShareItem;
import org.jeecg.modules.dreamlabs.transaction.StockShareItem;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.core.convert.Converter;
import cn.hutool.core.convert.ConverterRegistry;

public class HoldingShareTest {

	@Test
	public void stock() throws JsonProcessingException {
		ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
		//此处做为示例自定义String转换，因为Hutool中已经提供String转换，请尽量不要替换
		//替换可能引发关联转换异常（例如覆盖String转换会影响全局）
		converterRegistry.putCustom(BigDecimal.class, BigDecimalConverter.class);
		List<StockShareItem> items = new ArrayList<StockShareItem>();
		for(int i = 0;i <10 ;i++) {
			StockShareItem transactionItem = new StockShareItem();
			System.out.println(transactionItem);
			items.add(transactionItem);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonData = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(items);
		System.out.println(jsonData);
	}
	
	
	@Test
	public void fund() throws JsonProcessingException {
		ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
		//此处做为示例自定义String转换，因为Hutool中已经提供String转换，请尽量不要替换
		//替换可能引发关联转换异常（例如覆盖String转换会影响全局）
		converterRegistry.putCustom(BigDecimal.class, BigDecimalConverter.class);
		List<FundShareItem> items = new ArrayList<FundShareItem>();
		for(int i = 0;i <10 ;i++) {
			FundShareItem transactionItem = new FundShareItem();
			System.out.println(transactionItem);
			items.add(transactionItem);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonData = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(items);
		System.out.println(jsonData);
	}
	
	
	
	@Test
	public void convert() {
		String a = "33.33%";
		ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
		//此处做为示例自定义String转换，因为Hutool中已经提供String转换，请尽量不要替换
		//替换可能引发关联转换异常（例如覆盖String转换会影响全局）
		converterRegistry.putCustom(BigDecimal.class, PercentageConverter.class);
		BigDecimal result = converterRegistry.convert(BigDecimal.class, a);
		System.out.println(result);
	}
	
	
	public static class PercentageConverter implements Converter<BigDecimal>{
		@Override
		public BigDecimal convert(Object value, BigDecimal defaultValue) throws IllegalArgumentException {
			BigDecimal result = defaultValue;
			String percentage = (String) value;
			percentage = percentage.replace("%", "");
			percentage = percentage.replace(",", "");
			percentage = percentage.replace(" ", "");
			result = new BigDecimal(percentage).multiply(new BigDecimal("0.01")).setScale(4, BigDecimal.ROUND_HALF_UP);
			return result;
		}
	}
	
	public static class BigDecimalConverter implements Converter<BigDecimal>{
		@Override
		public BigDecimal convert(Object value, BigDecimal defaultValue) throws IllegalArgumentException {
			BigDecimal result = defaultValue;
			String currency = (String) value;
			currency = currency.replace("¥", "");
			currency = currency.replace(",", "");
			currency = currency.replace(" ", "");
			result = new BigDecimal(currency).setScale(2, BigDecimal.ROUND_HALF_UP);
			return result;
		}
	}

}
