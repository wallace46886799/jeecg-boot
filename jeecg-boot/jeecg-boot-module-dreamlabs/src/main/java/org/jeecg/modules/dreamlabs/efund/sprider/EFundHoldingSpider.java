package org.jeecg.modules.dreamlabs.efund.sprider;

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
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 爬取持仓列表
 * @author Frank
 *
 */
@Slf4j
public class EFundHoldingSpider extends AbstractEFundSpider {

	protected Map<String, Object> internalTransaction(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception {
		//0.持仓页面
		driver.get("https://e.efunds.com.cn/assets");
		Thread.sleep(LONGEST_WAIT_MILLIS);
		// 1.持仓列表
		List<WebElement> elements = driver.findElements(By.className("m-list-item"));
		int size = elements.size();
		log.info("Total:{}",size);
		
		String prefix = "//*[@id=\"currency156\"]/div[2]/div[";
		String surfix = "]/div[1]/div[1]/a/h3";
		String surfix1 = "]/div[1]/div[2]/div[1]/div[1]/span[2]";
		String surfix2 = "]/div[1]/div[2]/div[2]/div[1]/span[2]";
		String surfix3 = "]/div[1]/div[2]/div[1]/div[2]/span[2]";
		String surfix4 = "]/div[1]/div[2]/div[2]/div[2]/span[2]";
		String surfix5 = "]/div[1]/div[2]/div[1]/div[3]/span[2]";
		String surfix6 = "]/div[1]/div[2]/div[2]/div[3]/span[2]";
		String surfix7 = "]/div[1]/div[2]/div[1]/div[4]/span[2]";
		String surfix8 = "]/div[1]/div[2]/div[2]/div[4]/span[2]";
		String surfix9 = "]/div[1]/div[2]/div[1]/div[5]/span[2]";
		String surfix10 = "]/div[1]/div[2]/div[2]/div[5]/span[2]";
		
		List<FundShareItem> holdShares = new ArrayList<FundShareItem>();
		for (int i = 1; i <= size ; i++) {
			log.info("=====================");
			FundShareItem holdShare = new FundShareItem();
			
			String code = driver.findElement(By.xpath(prefix + String.valueOf(i) + "]")).getAttribute("data-fundcodes");
			log.info("基金编码-code:{}",code);
			holdShare.setCode(code);
			
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix)).getText();
			String name_str = handleName(name);
			log.info("基金名称-name:{}",name_str);
			holdShare.setName(name_str);
			
			//当前市值
			String current_amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			log.info("基金市值-amount:{}",current_amount);
			BigDecimal current_amount_bd = this.converterRegistry.convert(BigDecimal.class, current_amount);
			holdShare.setAmount(current_amount_bd);
			
			//当前份额
			String share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			log.info("基金份额-shares:{}",share);
			BigDecimal share_bd = this.converterRegistry.convert(BigDecimal.class, share);
			holdShare.setShares(share_bd);
			holdShare.setAvailableShares(share_bd);
			
			//成本价
			String orig_price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			log.info("基金成本价格-orig_price:{}", orig_price);
			BigDecimal orig_price_bd = this.converterRegistry.convert(BigDecimal.class, orig_price);
			holdShare.setOrigPrice(orig_price_bd);
			
			//当前净值
			String price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			log.info("基金净值-price:{}",price);
			holdShare.setPrice(this.converterRegistry.convert(BigDecimal.class, price));
			
			//净值日期
			String price_date = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			log.info("基金净值日期-price_date:{}",price_date);
			holdShare.setPriceDate(Convert.toDate(price_date));
			
			//当日涨跌
			String current_percent = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			log.info("基金当日涨跌比例-current_percent:{}",current_percent);
			holdShare.setLastFloatingPercent(this.converterRegistry.convert(BigDecimal.class, current_percent));
			
			//当日收益
			String current_profit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix7)).getText();
			log.info("基金当日盈亏-current_profit:{}",current_profit);
			holdShare.setLastFloatingProfit(this.converterRegistry.convert(BigDecimal.class, current_profit));
			
			// 浮动持仓收益
			String floating_profit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix8)).getText();
			log.info("基金浮动盈亏-floating_profit:{}",floating_profit);
			BigDecimal floating_profit_bd = this.converterRegistry.convert(BigDecimal.class, floating_profit);
			holdShare.setFloatingProfit(floating_profit_bd);
			
			// 累计收益
			String profit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix9)).getText();
			log.info("基金总盈亏-profit:{}",profit);
			BigDecimal profit_bd = this.converterRegistry.convert(BigDecimal.class, profit);
			holdShare.setProfit(profit_bd);
			
			if (BigDecimal.ZERO.compareTo(floating_profit_bd) == 0) {
				floating_profit_bd = profit_bd;
				holdShare.setFloatingProfit(profit_bd);
			}
			
			// 资产占比
			String asset_percent = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix10)).getText();
			log.info("基金资产占比-asset_percent:{}",asset_percent);
			holdShare.setAssetPercent(this.converterRegistry.convert(BigDecimal.class, asset_percent));
			
			// 成本
			BigDecimal orig_amount_bd = BigDecimal.ZERO;
			orig_amount_bd = NumberUtil.sub(current_amount_bd,profit_bd).setScale(2, BigDecimal.ROUND_HALF_UP);
			log.info("基金成本金额-orig_amount:{}", orig_amount_bd);
			holdShare.setOrigAmount(orig_amount_bd);
			
			// 浮动收益率
			if (BigDecimal.ZERO.compareTo(orig_amount_bd) != 0) {
				BigDecimal floating_percent_bd =NumberUtil.round(NumberUtil.div(floating_profit_bd, orig_amount_bd),4);
				log.info("基金浮动收益比例-floating_percent:{}", floating_percent_bd);
				holdShare.setFloatingPercent(floating_percent_bd);
			}
			// 成本价格为0，再次设置成本价格
			if (BigDecimal.ZERO.compareTo(orig_price_bd) == 0) {
				orig_price_bd = NumberUtil.div(orig_amount_bd,share_bd).setScale(2, BigDecimal.ROUND_HALF_UP);
				holdShare.setOrigPrice(orig_price_bd);
			}
			
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
	
	private String handleName(String name) {
		String subname = StrUtil.subBefore(name, "（", true);
		if (subname != null) {
			return subname;
		}else {
			return name;
		}
	}

}
