package org.jeecg.modules.dreamlabs.efund.sprider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.transaction.FundTransactionItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
/**
 * 1278929819496624130
 * @author Frank
 *
 */
@Slf4j
public class EFundTransactionSpider extends AbstractEFundSpider {
	@Override
	protected Map<String, Object> internalTransaction(WebDriver driver, DreamlabsOrg org,
			Map<String, String> orParamsMap, DreamlabsAccount account, Map<String, String> accountParamsMap)
			throws Exception {
		// 1.电子对账单,起始日期为7天前
		String startDate = DateUtils.formatDate(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
		driver.get("https://e.efunds.com.cn/transaction/statements?startDate=" + startDate + "&fundAccount=&currency=");
		Thread.sleep(LONGEST_WAIT_MILLIS);
		List<WebElement> elements = driver
				.findElements(By.xpath("//*[@id=\"order\"]/div[2]/div[3]/div[2]/table/tbody/tr"));
		int size = elements.size();
		log.info("Total size:{}",size);
		//// *[@id="order"]/div[2]/div[3]/div[2]/table/tbody/tr[1]/td[1]
		//// *[@id="order"]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[1]
		String prefix = "//*[@id=\"order\"]/div[2]/div[3]/div[2]/table/tbody/tr[";
		String surfix1 = "]/td[1]";
		String surfix2 = "]/td[2]";
		String surfix3 = "]/td[3]";
		String surfix4 = "]/td[4]";
		String surfix5 = "]/td[5]";
		String surfix6 = "]/td[6]";
		String surfix7 = "]/td[7]";
		String surfix8 = "]/td[8]";
		String surfix9 = "]/td[9]";
		String surfix10 = "]/td[10]";
		List<FundTransactionItem> transactions = new ArrayList<FundTransactionItem>();
		for (int i = 1; i <= size; i++) {
			log.info("==============================");
			FundTransactionItem transactionItem = new FundTransactionItem();
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			String code = this.queryFundByName(name).getCode();
			log.info("code:{}", code);
			transactionItem.setCode(code);
			log.info("name:{}", name);
			transactionItem.setName(name);
			
			//*[@id="currency156"]/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/span[2]
			String confirm_date = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			log.info("confirm_date:{}", confirm_date);
			transactionItem.setConfirmDate(Convert.toDate(confirm_date));
			
			//*[@id="currency156"]/div[2]/div[1]/div[1]/div[2]/div[2]/div[1]/span[2]
			String trans_type = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			log.info("deal_type:{}", trans_type);
			transactionItem.setDealType(trans_type);
			
			String net_price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			log.info("price:{}", net_price);
			transactionItem.setPrice(converterRegistry.convert(BigDecimal.class, net_price));
			
			String deal_share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			log.info("deal_share:{}", deal_share);
			transactionItem.setDealShare(converterRegistry.convert(BigDecimal.class, deal_share));
			transactionItem.setShare(converterRegistry.convert(BigDecimal.class, deal_share));
			
			String confirm_amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			log.info("amount:{}", confirm_amount);
			transactionItem.setDealAmount(converterRegistry.convert(BigDecimal.class, confirm_amount));
			
			String fee = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix7)).getText();
			log.info("fee:{}", fee);
			transactionItem.setFee(converterRegistry.convert(BigDecimal.class, fee));
			
			String unit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix8)).getText();
			log.info("unit:{}", unit);
			transactionItem.setUnit(unit);
			
			String sold_org = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix9)).getText();
			log.info("sold_org:{}", unit);
			transactionItem.setSoldOrg(sold_org);
			
			String confirm_result = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix10)).getText();
			log.info("confirm_result:{}", confirm_result);
			transactionItem.setConfirmResult(confirm_result);
			
			transactions.add(transactionItem);
		}

		Thread.sleep(DEFAULT_WAIT_MILLIS);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", transactions.size());
		result.put("transactions", transactions);
		return result;
	}

}
