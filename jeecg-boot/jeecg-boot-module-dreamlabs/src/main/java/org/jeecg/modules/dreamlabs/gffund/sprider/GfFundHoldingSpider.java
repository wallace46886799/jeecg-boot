package org.jeecg.modules.dreamlabs.gffund.sprider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
public class GfFundHoldingSpider extends AbstractGfFundSpider {
	
	@Override
	protected Map<String, Object> internalTransaction(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception {

		driver.get("https://trade.gffunds.com.cn/account/income/holding-profit");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		
		List<WebElement> elements = driver.findElements(By.xpath(
				"//*[@id=\"container\"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr"));
		int size = elements.size();
		
		String prefix = "//*[@id=\"container\"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr[";
		// 名称
		//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div/div[1]/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[1]/div/span/span[1]/a
		String surfix1 = "]/td[1]/span[2]/span[1]/a";
		// 编码
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[1]/span[2]/span[2]
		String surfix2 = "]/td[1]/span[2]/span[2]";
		// 持仓收益
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[2]/span
		String surfix3 = "]/td[2]/span";
		// 持仓收益率
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[3]/span
		String surfix4 = "]/td[3]/span";
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[4]/span
		// 成本价格
		//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div/div[1]/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[5]
		String surfix5 = "]/td[4]/span";
		// 最近净值
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr
		//*[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div/div[1]/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[6]/span
		String surfix6 = "]/td[5]/span";
		// 持仓成本
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[7]/span
		String surfix7 = "]/td[6]/span";
		// 资产
		//*[@id="container"]/div/div/div[3]/div[2]/div[1]/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div/div/span/div/table/tbody/tr[1]/td[7]/span
		String surfix8 = "]/td[7]/span";
		
		List<FundShareItem> holdShares = new ArrayList<FundShareItem>();
		
		for (int i = 1; i <= size; i++) {
			FundShareItem holdShare = new FundShareItem();
			// 基金名称			
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			// 基金代码
			String code = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			// 当前市值
			String current_amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix8)).getText();
			// 当前份额
			String share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix8)).getText();
			// 成本价
			String orig_price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			// 当前净值
			String price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			// 净值日期
			String price_date = DateUtil.today();
			// 当日涨跌
			String current_percent = "0.00";
			// 当日收益
			String current_profit = "0.00";
			// 持仓收益
			String floating_profit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			// 累计收益
			String profit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			// 资产占比
			String asset_percent = "0.00";

			// 份额，当前市值除以当前净值
			BigDecimal share_bd = NumberUtil.round(NumberUtil.div(NumberUtil.parseNumber(current_amount), NumberUtil.parseNumber(price)), 2);
			
			log.info("=====================");
			log.info("code:{}", code);
			holdShare.setCode(code);
			log.info("name:{}", name);
			holdShare.setName(StrUtil.subBefore(name, "(", true));
			log.info("amount:{}", current_amount);
			BigDecimal current_amount_bd = this.converterRegistry.convert(BigDecimal.class, current_amount);
			holdShare.setAmount(current_amount_bd);
			log.info("shares:{}", share);
			holdShare.setShares(share_bd);
			holdShare.setAvailableShares(share_bd);
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
			holdShare.setFloatingProfit(this.converterRegistry.convert(BigDecimal.class, floating_profit));
			log.info("profit:{}", profit);
			holdShare.setProfit(this.converterRegistry.convert(BigDecimal.class, profit));
			log.info("asset_percent:{}", asset_percent);
			holdShare.setAssetPercent(this.converterRegistry.convert(BigDecimal.class, asset_percent));
			
			BigDecimal orig_amount_bd =NumberUtil.round(NumberUtil.sub(current_amount_bd,floating_profit_bd),2);
			log.info("orig_amount:{}", orig_amount_bd);
			holdShare.setOrigAmount(orig_amount_bd);
			
			BigDecimal floating_percent_bd = NumberUtil.round(NumberUtil.div(floating_profit_bd,orig_amount_bd),2);
			log.info("floating_percent:{}", floating_percent_bd);
			holdShare.setFloatingPercent(floating_percent_bd);
			
			holdShares.add(holdShare);
		}
		
		//追加广发钱袋子货币的持仓
		FundShareItem holdShare = new FundShareItem();
		driver.get("https://trade.gffunds.com.cn/wallet/my-wallet");
		String qb_amount = driver.findElement(By.xpath("//*[@id=\"container\"]/div/div/div[3]/div[2]/div/div[1]/div/div[1]/div[3]/span")).getText();
		log.info("amount:{}", qb_amount);
		BigDecimal amount_bd = this.converterRegistry.convert(BigDecimal.class, qb_amount);
		if(BigDecimal.ZERO.compareTo(amount_bd) != 0) {
			holdShare.setAmount(amount_bd);
			holdShare.setOrigAmount(amount_bd);
			holdShare.setShares(amount_bd);
			holdShare.setAvailableShares(amount_bd);
			holdShare.setPrice(BigDecimal.ONE);
			holdShare.setOrigPrice(BigDecimal.ONE);
			holdShare.setCode("000509");
			holdShare.setName("广发钱袋子货币A");
			holdShare.setPriceDate(new Date());
			
			String floating_profit = driver.findElement(By.xpath("//*[@id=\"container\"]/div/div/div[3]/div[2]/div/div[1]/div/div[2]/div[1]/div[2]/div[2]/span[1]")).getText();
			log.info("floating_profit:{}", floating_profit);
			BigDecimal floating_profit_bd = this.converterRegistry.convert(BigDecimal.class, floating_profit);
			holdShare.setFloatingProfit(floating_profit_bd);
			holdShare.setFloatingPercent(BigDecimal.ZERO);
			
			String current_profit = driver.findElement(By.xpath("//*[@id=\"container\"]/div/div/div[3]/div[2]/div/div[1]/div/div[2]/div[1]/div[1]/div[2]/span[1]")).getText();
			log.info("current_profit:{}", current_profit);
			BigDecimal current_profit_bd = this.converterRegistry.convert(BigDecimal.class, current_profit);
			holdShare.setLastFloatingProfit(current_profit_bd);
			holdShare.setLastFloatingPercent(BigDecimal.ZERO);
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
