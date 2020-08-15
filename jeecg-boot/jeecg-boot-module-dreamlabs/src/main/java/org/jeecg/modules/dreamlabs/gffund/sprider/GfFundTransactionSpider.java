package org.jeecg.modules.dreamlabs.gffund.sprider;

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
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
/**
 * 1278930296481263617
 * @author Frank
 *
 */
@Slf4j
public class GfFundTransactionSpider extends AbstractGfFundSpider {
	@Override
	protected Map<String, Object> internalTransaction(WebDriver driver, DreamlabsOrg org,
			Map<String, String> orParamsMap, DreamlabsAccount account, Map<String, String> accountParamsMap)
			throws Exception {
		// /html/body/div[2]/div/ul/li[5]/a
		// 1.持仓列表
		driver.get("https://trade.gffunds.com.cn/record/bill-detail");
		Thread.sleep(DEFAULT_WAIT_MILLIS);

		List<WebElement> elements = driver.findElements(
				By.xpath("//*[@id=\"container\"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div"));
		int size = elements.size();
		log.info("Total Size:{}", size);
		String prefix = "//*[@id=\"container\"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[";
		// 确认日期
		String surfix1 = "]/div[1]";
		// 业务类型
		String surfix2 = "]/div[2]";
		// 产品名称
		String surfix3 = "]/div[3]";
		// 确认份额
		String surfix4 = "]/div[4]";
		// 确认金额
		String surfix5 = "]/div[5]";
		// 币种
		String surfix6 = "]/div[6]";
		// 成交净值
		String surfix7 = "]/div[7]";
		// 手续费
		String surfix8 = "]/div[8]";
		// 收费方式
		String surfix9 = "]/div[9]";
		// 销售机构
		String surfix10 = "]/div[10]";
		// *[@id="container"]/div/div/div[3]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[202]/div[1]
		List<FundTransactionItem> transactions = new ArrayList<FundTransactionItem>();
		for (int i = size; i >= size - 50; i--) {
			// *[@id="showTblconf"]/tbody/tr[1]/td[5]
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			String code = this.queryFundByName(name).getCode();
			
			// *[@id="showTblconf"]/tbody/tr[1]/td[3]
			String confirm_date = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			confirm_date = DateUtil.parse(confirm_date).toDateStr();
			// *[@id="showTblconf"]/tbody/tr[1]/td[6]
			String deal_type = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[11]
			String net_price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix7)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[8]
			// //*[@id="showTblconf"]/tbody/tr[1]/td[7]
			String confirm_amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[10]
			String fee = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix8)).getText();
			String unit = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			// *[@id="showTblconf"]/tbody/tr[1]/td[1]
			String sold_org = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix10)).getText();
			String confirm_share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			confirm_share = StrUtil.removeSuffix(confirm_share, "份");
			String confirm_result = "--";

			FundTransactionItem transaction = new FundTransactionItem();
			log.info("==============================");
			log.info("code:{}", code);
			transaction.setCode(code);
			log.info("name:{}", name);
			transaction.setName(name);
			log.info("confirm_date:{}", confirm_date);
			transaction.setConfirmDate(Convert.toDate(confirm_date));
			log.info("deal_type:{}", deal_type);
			transaction.setDealType(deal_type);
			log.info("price:{}", net_price);
			transaction.setPrice(this.converterRegistry.convert(BigDecimal.class,net_price));
			log.info("deal_share:{}", confirm_share);
			transaction.setDealShare(this.converterRegistry.convert(BigDecimal.class,confirm_share));
			transaction.setShare(this.converterRegistry.convert(BigDecimal.class,confirm_share));
			log.info("deal_amount:{}", confirm_amount);
			transaction.setDealAmount(this.converterRegistry.convert(BigDecimal.class, confirm_amount));
			log.info("fee:{}", fee);
			transaction.setFee(this.converterRegistry.convert(BigDecimal.class, fee));
			log.info("unit:{}", unit);
			transaction.setUnit(unit);
			log.info("sold_org:{}", sold_org);
			transaction.setSoldOrg(sold_org);
			log.info("confirm_result:{}", confirm_result);
			transaction.setConfirmResult(confirm_result);
			transactions.add(transaction);
		}

		Thread.sleep(DEFAULT_WAIT_MILLIS);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", transactions.size());
		result.put("transactions", transactions);
		return result;

	}
	
	@Override
	protected void preTransaction(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception {
		return;
	}

	@Override
	protected void postTransaction(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap, Map<String, Object> result)
			throws Exception {
		return;
	}
}
