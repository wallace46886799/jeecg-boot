package org.jeecg.modules.dreamlabs.jsfund.sprider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.transaction.FundTransactionItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
/**
 * 1278930202772123649
 * @author Frank
 *
 */
@Slf4j
public class JsFundTransactionSpider extends AbstractJsFundSpider {
	@Override
	protected Map<String, Object> internalTransaction(WebDriver driver, DreamlabsOrg org,
			Map<String, String> orParamsMap, DreamlabsAccount account, Map<String, String> accountParamsMap)
			throws Exception {
		// 1.持仓列表
		driver.get("https://e.jsfund.cn/query/histconf");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		// https://e.efunds.com.cn/transaction/records?partial&sort=&startDate=2020-07-13&endDate=2020-07-17&businType=LC&status=1&fundCode=

		// https://e.efunds.com.cn/transaction/statements?startDate=2020-06-18&fundAccount=&currency=
		//// *[@id="order"]/div[2]/div[3]/div[2]/table/tbody
		// //*[@id="order"]/div[2]/div[3]/div[2]/table/tbody/tr
		List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"showTblconf\"]/tbody/tr"));
		int size = elements.size();
		log.info("Total Size:{}", size);

		//// *[@id="order"]/div[2]/div[3]/div[2]/table/tbody/tr[1]/td[1]
		//// *[@id="order"]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[1]
		String prefix = "//*[@id=\"showTblconf\"]/tbody/tr[";
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
		String surfix11 = "]/td[11]";
		String surfix12 = "]/td[12]";
		List<FundTransactionItem> transactions = new ArrayList<FundTransactionItem>();
		for (int i = 1; i <= size; i++) {
			log.info("==============================");
			FundTransactionItem transaction = new FundTransactionItem();
			
			// *[@id="showTblconf"]/tbody/tr[1]/td[4]
			String code = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			log.info("code:{}", code);
			transaction.setCode(code);
			
			// *[@id="showTblconf"]/tbody/tr[1]/td[5]
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			log.info("name:{}", name);
			transaction.setName(name);
			
			// *[@id="showTblconf"]/tbody/tr[1]/td[3]
			String confirm_date = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			log.info("confirm_date:{}", confirm_date);
			transaction.setConfirmDate(Convert.toDate(confirm_date));
			
			// *[@id="showTblconf"]/tbody/tr[1]/td[6]
			String deal_type = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			log.info("deal_type:{}", deal_type);
			transaction.setDealType(deal_type);
			
			// *[@id="showTblconf"]/tbody/tr[1]/td[11]
			String price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix11)).getText();
			log.info("price:{}", price);
			transaction.setPrice(this.converterRegistry.convert(BigDecimal.class, price));
			
			//*[@id="showTblconf"]/tbody/tr[1]/td[7]
			String deal_amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix7)).getText();
			log.info("deal_amount:{}", price);
			transaction.setDealAmount(this.converterRegistry.convert(BigDecimal.class, deal_amount));
			
			// *[@id="showTblconf"]/tbody/tr[1]/td[10]
			String fee = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix10)).getText();
			log.info("fee:{}", fee);
			transaction.setFee(this.converterRegistry.convert(BigDecimal.class, fee));
			
			String unit = "元";
			log.info("unit:{}", unit);
			transaction.setUnit(unit);
			
			// *[@id="showTblconf"]/tbody/tr[1]/td[1]
			String sold_org = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			log.info("sold_org:{}", sold_org);
			transaction.setSoldOrg(sold_org);
			
			String confirm_result = "已确认";
			log.info("confirm_result:{}", confirm_result);
			transaction.setConfirmResult(confirm_result);
			
			// *[@id="showTblconf"]/tbody/tr[1]/td[8]
			String confirm_share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix8)).getText();
			log.info("deal_share:{}", confirm_share);
			transaction.setDealShare(this.converterRegistry.convert(BigDecimal.class, confirm_share));
			transaction.setShare(this.converterRegistry.convert(BigDecimal.class, confirm_share));
		
			transactions.add(transaction);
		}

		Thread.sleep(DEFAULT_WAIT_MILLIS);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", size);
		result.put("transactions", transactions);
		return result;
	}
}
