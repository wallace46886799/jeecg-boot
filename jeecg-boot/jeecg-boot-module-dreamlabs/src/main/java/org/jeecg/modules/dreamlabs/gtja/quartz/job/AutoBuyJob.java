package org.jeecg.modules.dreamlabs.gtja.quartz.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 爬取国泰君安的网站进行自动打新。
 * 
 * 1、迭代金融机构、迭代账户 2、查询新股 3、查询新股额度 4、进行打新 5、打新记录
 * 
 * @Author Frank
 */
@Slf4j
public class AutoBuyJob extends AbstractGtjaJob {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("==============》自动买入开始，日期:{}，时间:{}", DateUtils.getDate("yyyy-MM-dd"), DateUtils.now());
		try {
			LambdaQueryWrapper<DreamlabsOrg> org_q = new LambdaQueryWrapper<DreamlabsOrg>();
			org_q.eq(DreamlabsOrg::getOrgType, "2");
			List<DreamlabsOrg> orgs = this.queryOrgs(org_q);
			for (DreamlabsOrg org : orgs) {
				log.info("金融机构开始:{}", org.getOrgName());
				LambdaQueryWrapper<DreamlabsAccount> account_q = new LambdaQueryWrapper<DreamlabsAccount>();
				account_q.eq(DreamlabsAccount::getOrgOwner, org.getId());
				account_q.eq(DreamlabsAccount::getStatus, "1");
				List<DreamlabsAccount> accounts = this.queryAccounts(account_q);
				for (DreamlabsAccount account : accounts) {
					log.info("自动买入开始:{}", account.getAccountName());
					this.doTransaction(account.getId());
					log.info("自动买入结束:{}", account.getAccountName());
				}
				log.info("金融机构结束:{}", org.getOrgName());
			}
		} catch (Exception e) {
			log.error("Something error.", e);
		}

		log.info("《==============自动买入结束，日期:{}，时间:{}", DateUtils.getDate("yyyy-MM-dd"), DateUtils.now());

	}

	@Override
	protected Map<String, Object> internalTransaction(WebDriver driver, DreamlabsOrg org,
			Map<String, String> orParamsMap, DreamlabsAccount account, Map<String, String> accountParamsMap)
			throws Exception {

		try {
			// 5.买入页面
			driver.get("https://i.gtja.com/evercos/securities/stock/trade/buy_index.html");
			Thread.sleep(LONGER_WAIT_MILLIS);
			driver.findElement(By.id("stockCode")).sendKeys("000002");
			driver.findElement(By.id("stockCode")).sendKeys(Keys.TAB);
			Thread.sleep(LONGER_WAIT_MILLIS);
			driver.findElement(By.id("entrustPrice")).sendKeys("28.650");
			driver.findElement(By.cssSelector("[class='stepper-btn refresh-price']")).click();
			Thread.sleep(LONGER_WAIT_MILLIS);
			for (int i = 0; i < 100; i++) {
				driver.findElement(By.cssSelector("[class='stepper-btn before']")).click();
			}
			driver.findElement(By.id("entrustPrice")).sendKeys(Keys.TAB);
			driver.findElement(By.id("entrustAmount")).sendKeys("100");
			Thread.sleep(LONGER_WAIT_MILLIS);
			driver.findElement(By.id("submitBuy")).click();
			Thread.sleep(LONGER_WAIT_MILLIS);

			// 6.发起买入请求
			((JavascriptExecutor) driver).executeScript("$(\"#stockTrading\").ajaxSubmit({\n"
					+ "			url : '/evercos/securities/stock/trade/trading.json',\n"
					+ "			type : \"post\",\n" + "			dataType : \"json\",\n" + "			data : {\n"
					+ "				\"stockAccount\" : '0106044666',\n"
					+ "				\"fullAccountNo\" : '0311040018017037',\n" + "				\"entrustProp\" :'U'\n"
					+ "			},\n" + "			beforeSend : function(){\n"
					+ "				console.log(\"before\")\n" + "			},\n"
					+ "			complete : function(){\n" + "				console.log(\"complete\")\n"
					+ "			},\n" + "			success : function(data) {\n" + "				console.log(data)\n"
					+ "			}\n" + "})");

			Thread.sleep(LONGER_WAIT_MILLIS);

		} catch (Exception e) {
			log.error("Auto transaction failed.", e);
		}

		Map<String, Object> result = new HashMap<String, Object>();
		return result;

	}

}
