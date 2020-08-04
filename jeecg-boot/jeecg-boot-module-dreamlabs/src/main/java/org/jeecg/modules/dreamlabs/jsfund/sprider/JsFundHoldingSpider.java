package org.jeecg.modules.dreamlabs.jsfund.sprider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class JsFundHoldingSpider extends AbstractJsFundSpider {


	@Override
	protected Map<String, Object> internalTransaction(WebDriver driver, DreamlabsOrg org,
			Map<String, String> orParamsMap, DreamlabsAccount account, Map<String, String> accountParamsMap)
			throws Exception {
		// 1.持仓列表
		driver.get("https://e.jsfund.cn/custom");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"showTbl\"]/tbody/tr"));
		int size = elements.size();

		String prefix = "//*[@id=\"showTbl\"]/tbody/tr[";
		// *[@id="showTbl"]/tbody/tr[1]/td[1]/span 类型
		String surfix1 = "]/td[1]/span";
		// *[@id="showTbl"]/tbody/tr[1]/td[2] 名称
		String surfix2 = "]/td[2]";
		// *[@id="showTbl"]/tbody/tr[1]/td[3] 编码
		String surfix3 = "]/td[3]";
		// *[@id="showTbl"]/tbody/tr[1]/td[4] 资产
		String surfix4 = "]/td[4]";
		// *[@id="showTbl"]/tbody/tr[1]/td[5]/span 日收益
		String surfix5 = "]/td[5]/span";
		// *[@id="showTbl"]/tbody/tr[1]/td[6] 净值日期
		String surfix6 = "]/td[6]";
		// *[@id="showTbl"]/tbody/tr[1]/td[7] 净值
		String surfix7 = "]/td[7]";

		// 货币基金部分
		List<FundShareItem> holdShares = new ArrayList<FundShareItem>();
		for (int i = 1; i <= size; i++) {

			FundShareItem holdShare = new FundShareItem();

			String type = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			String code = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			//// *[@id="currency156"]/div[2]/div[1] //*[@id="currency156"]/div[2]/div[2]
			// 当前市值
			String current_amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			// 当前份额 //*[@id="showTb2"]/tbody/tr/td[7]
			String share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			// 成本价 //*[@id="showTbl"]/tbody/tr[1]/td[9]
			String orig_price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix7)).getText();
			// 当前净值
			String price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix7)).getText();
			// 净值日期 //*[@id="showTbl"]/tbody/tr[1]/td[8]
			// //*[@id="showTbl"]/tbody/tr[1]/td[6]
			String price_date = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			// 当日涨跌 //*[@id="showTbl"]/tbody/tr[1]/td[5]/span
			String current_percent = "0.00";
			// 当日收益 //*[@id="showTbl"]/tbody/tr[1]/td[5]/span
			String current_profit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			// 持仓收益 //*[@id="showTbl"]/tbody/tr[1]/td[5]
			String floating_profit = "0.00";
			// 累计收益
			String profit = "0.00";
			// 资产占比
			String asset_percent = "0.00";

			log.info("=====================");
			log.info("type:{}", type);
			holdShare.setType(type);
			log.info("code:{}", code);
			holdShare.setCode(code);
			log.info("name:{}", name);
			holdShare.setName(name);
			log.info("amount:{}", current_amount);
			BigDecimal current_amount_bd = this.converterRegistry.convert(BigDecimal.class, current_amount);
			holdShare.setAmount(current_amount_bd);
			log.info("shares:{}", share);
			BigDecimal share_bd = this.converterRegistry.convert(BigDecimal.class, share);
			holdShare.setShares(share_bd);
			log.info("orig_price:{}", orig_price);
			holdShare.setOrigPrice(this.converterRegistry.convert(BigDecimal.class, orig_price));
			log.info("price:{}", price);
			holdShare.setPrice(this.converterRegistry.convert(BigDecimal.class, price));
			log.info("price_date:{}", price_date);
			holdShare.setPriceDate(Convert.toDate(price_date));
			log.info("current_percent:{}", current_percent);
			holdShare.setLastFloatingPercent(this.converterRegistry.convert(BigDecimal.class, current_percent));
			log.info("current_profit:{}", current_profit);
			holdShare.setLastFloatingProfit(this.converterRegistry.convert(BigDecimal.class, current_profit));
			log.info("floating_profit:{}", floating_profit);
			BigDecimal floating_profit_bd = this.converterRegistry.convert(BigDecimal.class, floating_profit);
			holdShare.setFloatingProfit(floating_profit_bd);
			log.info("profit:{}", profit);
			holdShare.setProfit(this.converterRegistry.convert(BigDecimal.class, profit));
			log.info("asset_percent:{}", asset_percent);
			holdShare.setAssetPercent(this.converterRegistry.convert(BigDecimal.class, asset_percent));
			log.info("available_shares:{}", share);
			holdShare.setAvailableShares(share_bd);
			BigDecimal origAmount = NumberUtil.sub(current_amount_bd, floating_profit_bd);
			log.info("orig_amount:{}", origAmount);
			holdShare.setOrigAmount(origAmount);
			BigDecimal floating_percent_bd =NumberUtil.round(NumberUtil.div(floating_profit_bd, origAmount), 4);
			log.info("floating_percent:{}", floating_percent_bd);
			holdShare.setFloatingPercent(floating_percent_bd);
			
			holdShares.add(holdShare);
		}
		
		// 非货币基金部分
		List<WebElement> elements_tb2 = driver.findElements(By.xpath("//*[@id=\"showTb2\"]/tbody/tr"));
		int size_tb2 = elements_tb2.size();
		String prefix_tb2 = "//*[@id=\"showTb2\"]/tbody/tr[";
		// 类型
		String surfix1_tb2 = "]/td[1]";
		// 名称
		String surfix2_tb2 = "]/td[2]";
		// 代码
		String surfix3_tb2 = "]/td[3]";
		// 资产
		String surfix4_tb2 = "]/td[4]";
		// 持仓收益
		String surfix5_tb2 = "]/td[5]";
		// 昨日收益
		String surfix6_tb2 = "]/td[6]";
		// 份额
		String surfix7_tb2 = "]/td[7]";
		// 净值日期
		String surfix8_tb2 = "]/td[8]";
		// *[@id="showTb2"]/tbody/tr/td[9] 最新净值
		String surfix9_tb2 = "]/td[9]";
		for (int i = 1; i <= size_tb2; i++) {
			FundShareItem holdShare = new FundShareItem();
			
			String type = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix1_tb2)).getText();
			String code = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix3_tb2)).getText();
			String name = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix2_tb2)).getText();
			//// *[@id="currency156"]/div[2]/div[1] //*[@id="currency156"]/div[2]/div[2]
			// 当前市值
			String current_amount = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix4_tb2))
					.getText();
			// 当前份额 //*[@id="showTb2"]/tbody/tr/td[7]
			String share = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix7_tb2)).getText();
			// 当前净值 //*[@id="showTb2"]/tbody/tr/td[9]
			String price = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix9_tb2)).getText();
			// 净值日期 //*[@id="showTbl"]/tbody/tr[1]/td[8]
			String price_date = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix8_tb2)).getText();
			// 当日涨跌 //*[@id="showTb2"]/tbody/tr/td[6]
			// 当日收益 //*[@id="showTbl"]/tbody/tr[1]/td[6]/span
			String current_profit = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix6_tb2))
					.getText();
			// 持仓收益 //*[@id="showTbl"]/tbody/tr[1]/td[5] //*[@id="showTb2"]/tbody/tr/td[5]
			String floating_profit = driver.findElement(By.xpath(prefix_tb2 + String.valueOf(i) + surfix5_tb2))
					.getText();
			// 累计收益
			String profit = floating_profit;
			// 资产占比
			String asset_percent = "0.00";
			// 成本价 //*[@id="showTbl"]/tbody/tr[1]/td[9]
			BigDecimal orig_amount = NumberUtil.sub(NumberUtil.parseNumber(current_amount),NumberUtil.parseNumber(floating_profit));
			BigDecimal orig_price = NumberUtil.round(NumberUtil.div(orig_amount, NumberUtil.parseNumber(share)), 3);
			
			log.info("=====================");
			log.info("type:{}", type);
			holdShare.setType(type);
			log.info("code:{}", code);
			holdShare.setCode(code);
			log.info("name:{}", name);
			holdShare.setName(name);
			log.info("amount:{}", current_amount);
			BigDecimal current_amount_bd = this.converterRegistry.convert(BigDecimal.class, current_amount);
			holdShare.setAmount(current_amount_bd);
			log.info("shares:{}", share);
			BigDecimal share_bd = this.converterRegistry.convert(BigDecimal.class, share);
			holdShare.setShares(share_bd);
			log.info("orig_price:{}", orig_price);
			holdShare.setOrigPrice(orig_price);
			log.info("price:{}", price);
			holdShare.setPrice(this.converterRegistry.convert(BigDecimal.class, price));
			log.info("price_date:{}", price_date);
			holdShare.setPriceDate(Convert.toDate(price_date));
			log.info("current_profit:{}", current_profit);
			BigDecimal current_profit_bd = this.converterRegistry.convert(BigDecimal.class, current_profit);
			holdShare.setLastFloatingProfit(current_profit_bd);
			BigDecimal current_percent_bd = NumberUtil.round(NumberUtil.div(current_profit_bd, NumberUtil.sub(current_amount_bd,current_profit_bd)), 4);
			log.info("current_percent:{}", current_percent_bd);
			holdShare.setLastFloatingPercent(current_percent_bd);
			log.info("floating_profit:{}", floating_profit);
			BigDecimal floating_profit_bd = this.converterRegistry.convert(BigDecimal.class, floating_profit);
			holdShare.setFloatingProfit(floating_profit_bd);
			log.info("profit:{}", profit);
			holdShare.setProfit(this.converterRegistry.convert(BigDecimal.class, profit));
			log.info("asset_percent:{}", asset_percent);
			holdShare.setAssetPercent(this.converterRegistry.convert(BigDecimal.class, asset_percent));
			log.info("available_shares:{}", share);
			holdShare.setAvailableShares(share_bd);
			BigDecimal origAmount = NumberUtil.round(NumberUtil.sub(current_amount_bd, floating_profit_bd),2);
			log.info("orig_amount:{}", origAmount);
			holdShare.setOrigAmount(origAmount);
			BigDecimal floating_percent_bd = NumberUtil.round(NumberUtil.div(floating_profit_bd, origAmount), 4);
			log.info("floating_percent:{}", floating_percent_bd);
			holdShare.setFloatingPercent(floating_percent_bd);
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
