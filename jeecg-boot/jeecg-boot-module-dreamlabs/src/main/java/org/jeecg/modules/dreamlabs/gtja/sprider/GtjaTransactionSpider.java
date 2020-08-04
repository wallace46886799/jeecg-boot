package org.jeecg.modules.dreamlabs.gtja.sprider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.transaction.StockTransactionItem;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GtjaTransactionSpider extends AbstractGtjaSpider {
	@Override
	protected Map<String, Object> internalTransaction(WebDriver driver, DreamlabsOrg org,
			Map<String, String> orParamsMap, DreamlabsAccount account, Map<String, String> accountParamsMap)
			throws Exception {
		// 1.当日交易
		driver.get("https://i.gtja.com/evercos/securities/stock/query/deal.html");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		List<WebElement> elements = driver.findElements(ById.xpath("//*[@id=\"current-panel\"]/div"));
		int size = elements.size();
		log.info("Total size:{}",size);
		
		List<StockTransactionItem> transactions = new ArrayList<StockTransactionItem>();
		String message = "您当日没有成交信息";
		String messageStr = driver.findElement(By.xpath("//*[@id=\"current-panel\"]/div/div")).getText();
		if(message.equals(messageStr)) {
			log.info(message);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", transactions.size());
			result.put("transactions", transactions);
			return result;
		}
		
		// *[@id="current-panel"]/div/div[1]/p[1] 交易标的
		// *[@id="current-panel"]/div/div[2]/p[1] 成交时间
		// *[@id="current-panel"]/div/div[2]/p[2] 成交价格
		// *[@id="current-panel"]/div/div[3]/p[1] 委托数量
		// *[@id="current-panel"]/div/div[3]/p[2] 成交数量
		// *[@id="current-panel"]/div/div[4]/p[1] 成交类型
		// *[@id="current-panel"]/div/div[4]/p[2] 成交金额
		String prefix = "//*[@id=\"current-panel\"]/div[";
		String surfix =  "]/div[1]/p[1]";
		String surfix1 = "]/div[2]/p[1]";
		String surfix2 = "]/div[2]/p[2]";
		String surfix3 = "]/div[3]/p[1]";
		String surfix4 = "]/div[3]/p[2]";
		String surfix5 = "]/div[4]/p[1]";
		String surfix6 = "]/div[4]/p[2]";
		for(int i = 1 ; i<= size ;i++) {
			log.info("==============================");
			StockTransactionItem transactionItem = new StockTransactionItem();
			
			String name = driver.findElement(By.xpath(prefix+i+surfix)).getText();
			log.info("name:{}",name);
			transactionItem.setName(name);
			String code = this.queryStockByName(name).getCode();
			transactionItem.setName(code);
			
			log.info("date:{}",DateUtil.today());
			transactionItem.setDate(new Date());
			
			String timeStr = driver.findElement(By.xpath(prefix+i+surfix1)).getText();
			log.info("time:{}",timeStr);
			Date time = Convert.toDate(timeStr);
			transactionItem.setTime(time);
			
			String price = driver.findElement(By.xpath(prefix+i+surfix2)).getText();
			log.info("price:{}",price);
			transactionItem.setPrice(converterRegistry.convert(BigDecimal.class, price));
			
			String share = driver.findElement(By.xpath(prefix+i+surfix3)).getText();
			log.info("share:{}",share);
			transactionItem.setShare(converterRegistry.convert(BigDecimal.class, share));
			
			String deal_share = driver.findElement(By.xpath(prefix+i+surfix4)).getText();
			log.info("deal_share:{}",deal_share);
			transactionItem.setDealShare(converterRegistry.convert(BigDecimal.class, handleGu(deal_share)));
			
			String deal_type = driver.findElement(By.xpath(prefix+i+surfix5)).getText();
			log.info("deal_type:{}",deal_type);
			transactionItem.setDealType(handleType(deal_type));

			String deal_amount = driver.findElement(By.xpath(prefix+i+surfix6)).getText();
			log.info("deal_amount:{}",deal_amount);
			transactionItem.setDealShare(converterRegistry.convert(BigDecimal.class, deal_amount));
			
			transactions.add(transactionItem);
		}

		// 2.等待5秒然后退出
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", transactions.size());
		result.put("transactions", transactions);
		return result;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String handleGu(String value) {
		return StrUtil.removeAll(value, "股");
	}
	
	/**
	 * 废单:-1
	 * 
	 * @param value
	 * @return
	 */
	private String handleType(String value) {
		return StrUtil.removeAll(value, "股");
	}
}
