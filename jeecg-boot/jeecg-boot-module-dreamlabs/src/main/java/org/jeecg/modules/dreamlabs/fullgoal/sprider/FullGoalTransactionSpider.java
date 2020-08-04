package org.jeecg.modules.dreamlabs.fullgoal.sprider;

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
 * 富国基金的电子对账单
 * @author Frank
 *
 */
@Slf4j
public class FullGoalTransactionSpider extends AbstractFullGoalSpider {
	@Override
	protected Map<String, Object> internalTransaction(WebDriver driver, DreamlabsOrg org,
			Map<String, String> orParamsMap, DreamlabsAccount account, Map<String, String> accountParamsMap)
			throws Exception {
		// /html/body/div[2]/div/ul/li[5]/a
		// 1.持仓列表
		driver.findElement(By.xpath("/html/body/div[2]/div/ul/li[5]/a")).click();
		
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		// //*[@id="60"]/li[3]/a  //*[@id="60"]/li[7]/a
		driver.findElement(By.xpath("//*[@id=\"60\"]/li[7]/a")).click();
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		// <a menuid="60006" ui:a="" shape="rect" href="/etrading/query/statement/init?initPage=1&amp;menuId=60006&amp;SECURE_TOKEN=aoH5U3XRnDw6i88pofihlCzHuftENst9IDXCALLq1o">电子对账单</a>
		// https://e.efunds.com.cn/transaction/records?partial&sort=&startDate=2020-07-13&endDate=2020-07-17&businType=LC&status=1&fundCode=
		// *[@id="view_config_view_account_tradeapply_list"]/tbody/tr[2]
		List<WebElement> elements = driver
				.findElements(By.xpath("//*[@id=\"view_config_view_query_statement_list\"]/tbody/tr"));
		int size = elements.size();
		log.info("Total size:{}",size);

		String prefix = "//*[@id=\"view_config_view_query_statement_list\"]/tbody/tr[";
		// 基金名称
		String surfix1 = "]/td[1]";
		// 业务类型
		String surfix2 = "]/td[2]";
		// 金额
		String surfix3 = "]/td[3]";
		// 份额
		String surfix4 = "]/td[4]";
		// 手续费
		String surfix5 = "]/td[5]";
		// 净值
		String surfix6 = "]/td[6]";
		// 申请时间
		String surfix7 = "]/td[7]";
		// 确认时间
		String surfix8 = "]/td[8]";
		// 确认状态
		String surfix9 = "]/td[9]";
		
		List<FundTransactionItem> transactions = new ArrayList<FundTransactionItem>();
		
		for (int i = 2; i <= size; i++) {
			String name = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix1)).getText();
			String code = this.queryFundByName(name).getCode();
			String confirm_date = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix8)).getText();
			confirm_date = DateUtil.parse(confirm_date).toDateStr();
			String deal_type = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix2)).getText();
			String net_price = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix6)).getText();
			String confirm_amount = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix3)).getText();
			String fee = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix5)).getText();
			String unit = "元";
			String sold_org = "--";
			String confirm_share = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix4)).getText();
			confirm_share = StrUtil.removeSuffix(confirm_share, "份");
			String confirm_result = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix9)).getText();
			String apply_date = driver.findElement(By.xpath(prefix + String.valueOf(i) + surfix7)).getText();

			FundTransactionItem transaction = new FundTransactionItem();
			log.info("==============================");
			log.info("code:{}", code);
			transaction.setCode(code);
			log.info("name:{}", name);
			transaction.setName(name);
			log.info("apply_date:{}", apply_date);
			transaction.setDate(Convert.toDate(apply_date));
			log.info("confirm_date:{}", confirm_date);
			transaction.setConfirmDate(Convert.toDate(confirm_date));
			log.info("deal_type:{}", deal_type);
			transaction.setDealType(deal_type);
			log.info("price:{}", net_price);
			transaction.setPrice(this.converterRegistry.convert(BigDecimal.class,net_price));
			log.info("deal_share:{}", confirm_share);
			transaction.setDealShare(this.converterRegistry.convert(BigDecimal.class,confirm_share));
			log.info("share:{}", confirm_share);
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

}
