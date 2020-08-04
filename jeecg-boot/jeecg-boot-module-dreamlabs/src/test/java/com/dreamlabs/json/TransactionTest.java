package com.dreamlabs.json;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jeecg.modules.dreamlabs.transaction.FundTransactionItem;
import org.jeecg.modules.dreamlabs.transaction.StockTransactionItem;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.Converter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.math.Money;
import cn.hutool.core.util.StrUtil;

public class TransactionTest {

	@Test
	public void stock() throws JsonProcessingException {
		List<StockTransactionItem> items = new ArrayList<StockTransactionItem>();
		for(int i = 0;i <10 ;i++) {
			StockTransactionItem transactionItem = new StockTransactionItem();
			transactionItem.setDate(new Date());
			transactionItem.setTime(new Date());
			transactionItem.setPrice(BigDecimal.TEN);
			transactionItem.setShare(BigDecimal.TEN);
			transactionItem.setDealShare(BigDecimal.TEN);
			transactionItem.setDealAmount(BigDecimal.TEN);
			transactionItem.setFee(BigDecimal.TEN);
			transactionItem.setDealType("1");
			transactionItem.setUnit("元");
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
		
		
		List<FundTransactionItem> items = new ArrayList<FundTransactionItem>();
		for(int i = 0;i <10 ;i++) {
			FundTransactionItem transactionItem = new FundTransactionItem();
			String dateStr = "2020-07-31";
			Date date = Convert.toDate(dateStr);
			transactionItem.setDate(date);
			String timeStr = "19:32:23";
			Date time = Convert.toDate(timeStr);
			transactionItem.setTime(time);
			Money money = new Money("100000.0000");
			transactionItem.setPrice(money.getAmount());
			transactionItem.setShare(new BigDecimal("10000.00"));
			BigDecimal dealShare = converterRegistry.convert(BigDecimal.class, "1111,111.11111");
			transactionItem.setDealShare(dealShare);
			transactionItem.setDealAmount(new BigDecimal("10000.00"));
			transactionItem.setFee(new BigDecimal("10000.00"));
			transactionItem.setDealType("1");
			transactionItem.setUnit("元");
			transactionItem.setConfirmDate(new Date());
			transactionItem.setSoldOrg("国泰君安");
			transactionItem.setConfirmResult("成功");
			items.add(transactionItem);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonData = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(items);
		System.out.println(jsonData);
	}
	
	
	
	@Test
	public void convert() {
		String a = "¥322,222.11111";
		ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
		//此处做为示例自定义String转换，因为Hutool中已经提供String转换，请尽量不要替换
		//替换可能引发关联转换异常（例如覆盖String转换会影响全局）
		converterRegistry.putCustom(BigDecimal.class, BigDecimalConverter.class);
		BigDecimal result = converterRegistry.convert(BigDecimal.class, a);
		System.out.println(result);
	}
	
	
	public static class CustomConverter implements Converter<BigDecimal>{
		@Override
		public BigDecimal convert(Object value, BigDecimal defaultValue) throws IllegalArgumentException {
			BigDecimal result = new BigDecimal("0.00");
			String currency = (String) value;
			DecimalFormat df = new DecimalFormat("###,###,###,###.00");
			try {
				Number number = df.parse(currency);
				result =  new BigDecimal(number.doubleValue()).setScale(2,BigDecimal.ROUND_HALF_UP);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return result;
		}
	}
	
	public static class BigDecimalConverter implements Converter<BigDecimal>{
		@Override
		public BigDecimal convert(Object value, BigDecimal defaultValue) throws IllegalArgumentException {
			BigDecimal result = defaultValue;
			String origValue = (String) value;
			if (StrUtil.contains(origValue, '%')) {
				origValue = origValue.replace("%", "");
				origValue = origValue.replace(",", "");
				origValue = origValue.replace(" ", "");
				result = new BigDecimal(origValue).multiply(new BigDecimal("0.01")).setScale(4, BigDecimal.ROUND_HALF_UP);
			} else {
				origValue = origValue.replace("¥", "");
				origValue = origValue.replace(",", "");
				origValue = origValue.replace(" ", "");
				result = new BigDecimal(origValue).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			return result;
		}
	}
}
