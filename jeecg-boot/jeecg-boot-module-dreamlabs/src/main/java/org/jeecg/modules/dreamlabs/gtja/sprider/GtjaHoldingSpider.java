package org.jeecg.modules.dreamlabs.gtja.sprider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.transaction.StockShareItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
/**
 * TODO.正确处理三种涨跌幅
 * @author Frank
 *
 */
@Slf4j
public class GtjaHoldingSpider extends AbstractGtjaSpider {

	public Map<String, Object> internalTransaction(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap, DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception {
		log.info("Start to sync gtja holding.");
		// 1.持仓列表
		driver.get("https://i.gtja.com/evercos/securities/stock/trade/index.html");
		Thread.sleep(LONGEST_WAIT_MILLIS);
		
		String holding_balance = driver.findElement(By.xpath("//*[@id=\"enableBalance\"]")).getText();
		log.info("可用资金-holding_balance:{}",holding_balance);
		BigDecimal holding_balance_bd = this.converterRegistry.convert(BigDecimal.class, holding_balance);
		
		List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"holdings-panel\"]/div"));
		int size = elements.size();
		log.info("持仓股票数量-size:{}",size);
		String prefix = "//*[@id='holdings-panel']/div[";
		String surfix1 = "]/div[1]/p[1]";
		String surfix2 = "]/div[1]/p[2]";
		String surfix3 = "]/div[2]/div[1]/p[1]";
		String surfix4 = "]/div[2]/div[1]/p[2]";
		String surfix5 = "]/div[2]/div[2]/p[1]";
		String surfix6 = "]/div[2]/div[2]/p[2]";
		String surfix7 = "]/div[2]/div[3]/p[1]";
		String surfix8 = "]/div[2]/div[3]/p[2]";
		List<StockShareItem> holdShares = new ArrayList<StockShareItem>();
		for (int i = 1; i <= size; i++) {
			log.info("=====================");
			StockShareItem holdShare = new StockShareItem();
			
			String code = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1 + "/i[1]")).getText();
			log.info("股票编码-code:{}",code);
			holdShare.setCode(code);
			
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			log.info("股票名称-name:{}",name);
			holdShare.setName(handleName(name));
			
			String amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			log.info("股票市值-amount:{}",amount);
			holdShare.setAmount(this.converterRegistry.convert(BigDecimal.class, amount));
			
			String share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			log.info("股票份额-shares:{}",share);
			BigDecimal share_bd = this.converterRegistry.convert(BigDecimal.class, share);
			holdShare.setShares(this.converterRegistry.convert(BigDecimal.class, share));
			
			String available_share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			log.info("股票可用份额-available_shares:{}",available_share);
			holdShare.setAvailableShares(this.converterRegistry.convert(BigDecimal.class, available_share));
			
			String price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			log.info("股票价格-price:{}",price);
			holdShare.setPrice(this.converterRegistry.convert(BigDecimal.class, price));
			
			String orig_price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			log.info("股票成本价格-orig_price:{}",orig_price);
			BigDecimal orig_price_bd = this.converterRegistry.convert(BigDecimal.class, orig_price);
			holdShare.setOrigPrice(orig_price_bd);
			
			String floating_profit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix7)).getText();
			log.info("股票浮动盈亏-floating_profit:{}",floating_profit);
			BigDecimal floating_profit_bd = this.converterRegistry.convert(BigDecimal.class, floating_profit);
			holdShare.setFloatingProfit(floating_profit_bd);
			
			String floating_percent = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix8)).getText();
			log.info("股票浮盈比例-floating_percent:{}",floating_percent);
			
			holdShare.setFloatingPercent(this.converterRegistry.convert(BigDecimal.class, floating_percent));
			
			BigDecimal orig_amount_bd = NumberUtil.mul(orig_price_bd, share_bd).setScale(2, BigDecimal.ROUND_HALF_UP);
			log.info("股票成本金额-orig_amount:{}", orig_amount_bd.toString());
			holdShare.setOrigAmount(orig_amount_bd);
			
			//设置占位值
			holdShare.setPriceDate(new Date());
			holdShare.setLastFloatingPercent(BigDecimal.ZERO);
			holdShare.setLastFloatingProfit(BigDecimal.ZERO);
			holdShare.setAssetPercent(BigDecimal.ZERO);
			holdShare.setMarketType("--");
			
			holdShares.add(holdShare);
		}
		// 2.等待5秒然后退出
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", holdShares.size());
		result.put("holding_shares", holdShares);
		result.put("holding_balance", holding_balance_bd);
		log.info("End to sync gtja holding.");
		return result;
	}
	
	private String handleName(String name) {
		String subname = StrUtil.subBefore(name, " ", true);
		if (subname != null) {
			return subname;
		}else {
			return name;
		}
	}
}
