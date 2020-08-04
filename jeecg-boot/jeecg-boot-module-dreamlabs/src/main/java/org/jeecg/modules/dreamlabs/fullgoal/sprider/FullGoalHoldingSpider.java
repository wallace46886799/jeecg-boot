package org.jeecg.modules.dreamlabs.fullgoal.sprider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.transaction.FundShareItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FullGoalHoldingSpider extends AbstractFullGoalSpider {
	
	private static final Pattern p = Pattern.compile("[^0-9]");  

	@Override
	protected Map<String, Object> internalTransaction(WebDriver driver, DreamlabsOrg org,
			Map<String, String> orParamsMap, DreamlabsAccount account, Map<String, String> accountParamsMap)
			throws Exception {

		driver.findElement(By.id("fundZcClickFlg")).click();
		// *[@id="myfunddetail"]/div[1]/div[1]/table/tbody/tr[1]
		Thread.sleep(DEFAULT_WAIT_MILLIS);

		// *[@id="myfunddetail"]/div[1]/div[1]
		List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"myfunddetail\"]/div[1]/div"));
		int size = elements.size();
		// *[@id="myfunddetail"]/div[1]/div[1]/table/tbody/tr[1]/th[3]/span
		//// *[@id="myfunddetail"]/div[1]/div[1]/table/tbody/tr[1]/th[1]/span
		String prefix = "//*[@id=\"myfunddetail\"]/div[1]/div[";
		// 名称、编码
		String surfix = "]/table/tbody/tr[1]/th[1]";
		String surfix1 = "]/table/tbody/tr[1]/th[1]/span";
		// 份额
		// *[@id="myfunddetail"]/div[1]/div[1]/table/tbody/tr[1]/th[2]/span
		String surfix2 = "]/table/tbody/tr[1]/th[2]/span";
		// 市值
		// *[@id="myfunddetail"]/div[1]/div[1]/table/tbody/tr[1]/th[3]/span
		String surfix3 = "]/table/tbody/tr[1]/th[3]/span";
		// 盈亏
		String surfix4 = "]/table/tbody/tr[1]/th[4]/span";
		// 净值
		// *[@id="myfunddetail"]/div[1]/div[1]/table/tbody/tr[1]/th[5]/p
		String surfix5 = "]/table/tbody/tr[1]/th[5]/p";

		List<FundShareItem> holdShares = new ArrayList<FundShareItem>();
		for (int i = 1; i <= size; i++) {
			FundShareItem holdShare = new FundShareItem();
			// 基金代码
			String code = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix)).getText();
			Matcher m = p.matcher(code);  
			code = m.replaceAll("").trim();
			// 基金名称
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			name = StrUtil.subBefore(name, "(", true);
			// 当前市值
			String current_amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			// 当前份额
			String share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			// 成本价
			String orig_price = "0.00";
			// 当前净值
			String price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			// 净值日期
			String price_date = DateUtil.today();
			// 当日涨跌
			String current_percent = "0.00";
			// 当日收益
			String current_profit = "0.00";
			// 持仓收益
			String floating_profit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			// 累计收益
			String profit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			// 资产占比
			String asset_percent = "0.00";
			// 成本价
			BigDecimal orig_amount_bd = NumberUtil.sub(NumberUtil.parseNumber(current_amount), NumberUtil.parseNumber(floating_profit));
			BigDecimal orig_price_bd = NumberUtil.round(NumberUtil.div(orig_amount_bd, NumberUtil.parseNumber(share)), 2);
			BigDecimal floating_percent_bd = NumberUtil.round(NumberUtil.div(NumberUtil.parseNumber(floating_profit), orig_amount_bd),4);

			log.info("=====================");
			log.info("code:{}", code);
			holdShare.setCode(code);
			log.info("name:{}", name);
			holdShare.setName(name);
			log.info("amount:{}", current_amount);
			holdShare.setAmount(this.converterRegistry.convert(BigDecimal.class, current_amount));
			log.info("orig_amount:{}", orig_amount_bd);
			holdShare.setOrigAmount(orig_amount_bd);
			log.info("shares:{}", share);
			holdShare.setShares(this.converterRegistry.convert(BigDecimal.class, share));
			log.info("available_shares:{}", share);
			holdShare.setAvailableShares(this.converterRegistry.convert(BigDecimal.class, share));
			log.info("orig_price:{}", orig_price);
			holdShare.setOrigPrice(orig_price_bd);
			log.info("price:{}", price);
			holdShare.setPrice(this.converterRegistry.convert(BigDecimal.class, price));
			log.info("price_date:{}", price_date);
			holdShare.setPriceDate(Convert.toDate(price_date));
			log.info("current_percent:{}", current_percent);
			holdShare.setLastFloatingPercent(this.converterRegistry.convert(BigDecimal.class, current_percent));
			log.info("current_profit:{}", current_profit);
			holdShare.setLastFloatingProfit(this.converterRegistry.convert(BigDecimal.class, current_profit));
			log.info("floating_profit:{}", floating_profit);
			holdShare.setFloatingProfit(this.converterRegistry.convert(BigDecimal.class, floating_profit));
			log.info("floating_percent",floating_percent_bd);
			holdShare.setFloatingPercent(floating_percent_bd);
			log.info("asset_percent:{}", asset_percent);
			holdShare.setAssetPercent(this.converterRegistry.convert(BigDecimal.class, asset_percent));
			
			holdShares.add(holdShare);
		}
		
		//追加富国富钱包货币的持仓
		// /html/body/div[2]/div/ul/li[2]/a
		FundShareItem holdShare = new FundShareItem();
		// /html/body/div[2]/div/ul/li[2]/a  /html/body/div[2]/div/ul/li[2]/a
		driver.findElement(By.xpath("/html/body/div[2]/div/ul/li[2]/a")).click();
		Thread.sleep(LONGER_WAIT_MILLIS);
		//*[@id="mshare"]
		String qb_amount = driver.findElement(By.xpath("//*[@id=\"mshare\"]")).getText();
		BigDecimal amount_bd = this.converterRegistry.convert(BigDecimal.class, qb_amount);
		if(BigDecimal.ZERO.compareTo(amount_bd) != 0) {
			holdShare.setAmount(amount_bd);
			holdShare.setOrigAmount(amount_bd);
			holdShare.setShares(amount_bd);
			holdShare.setAvailableShares(amount_bd);
			holdShare.setPrice(BigDecimal.ONE);
			holdShare.setOrigPrice(BigDecimal.ONE);
			holdShare.setCode("000638");
			holdShare.setName("富国富钱包货币");
			holdShare.setPriceDate(new Date());
			String floating_profit = driver.findElement(By.xpath("//*[@id=\"mtoincom\"]")).getText();
			BigDecimal floating_profit_bd = this.converterRegistry.convert(BigDecimal.class, floating_profit);
			holdShare.setFloatingProfit(floating_profit_bd);
			holdShare.setFloatingPercent(BigDecimal.ZERO);
			String current_profit = driver.findElement(By.xpath("//*[@id=\"mdayincom\"]")).getText();
			BigDecimal current_profit_bd = this.converterRegistry.convert(BigDecimal.class, current_profit);
			holdShare.setLastFloatingProfit(current_profit_bd);
			holdShares.add(holdShare);
		}
		
		// 2.等待30秒
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", holdShares.size());
		result.put("holding_shares", holdShares);
		result.put("holding_balance", BigDecimal.ZERO);
		return result;

	}

}
