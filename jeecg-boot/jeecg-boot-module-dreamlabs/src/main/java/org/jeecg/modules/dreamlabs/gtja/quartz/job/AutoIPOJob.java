package org.jeecg.modules.dreamlabs.gtja.quartz.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.translog.entity.DreamlabsTransLog;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
public class AutoIPOJob extends AbstractGtjaJob {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("==============》自动打新开始，日期:{}，时间:{}", DateUtils.getDate("yyyy-MM-dd"), DateUtils.now());
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
					log.info("打新账户开始:{}", account.getAccountName());
					this.doTransaction(account.getId());
					log.info("打新账户结束:{}", account.getAccountName());
				}
				log.info("金融机构结束:{}", org.getOrgName());
			}
		} catch (Exception e) {
			log.error("Something error.", e);
		}
		log.info("《==============自动打新结束，日期:{}，时间:{}", DateUtils.getDate("yyyy-MM-dd"), DateUtils.now());
	}

	protected Map<String, Object> internalTransaction(WebDriver driver, DreamlabsOrg org,
			Map<String, String> orParamsMap, DreamlabsAccount account, Map<String, String> accountParamsMap)
			throws Exception {
		Map<String, Object> result = this.handleShares(driver, org, orParamsMap, account, accountParamsMap);
		this.logShares(driver, org, orParamsMap, account, accountParamsMap);
		return result;
	}

	private Map<String, Object> handleShares(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception {
		// 1.新股页面
		driver.get("https://i.gtja.com/evercos/securities/stock/query/ipo/shares.html");
		Thread.sleep(LONGER_WAIT_MILLIS);
		driver.get("https://i.gtja.com/evercos/securities/stock/query/ipo/oneKeyBuyBuy.html");
		Thread.sleep(LONGER_WAIT_MILLIS);

		// 2.一键申购
		// $("#oneKeyBuy")
		Object click = ((JavascriptExecutor) driver).executeScript("$(\"#oneKeyBuy\").click()");
		log.info("Result is {}", click);

		// 3.等待3秒
		Thread.sleep(LONGER_WAIT_MILLIS);
		Map<String,Object> result = new HashMap<String, Object>(); 
		return result;
	}

	private void logShares(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception {
		String skipLog = accountParamsMap.getOrDefault("log.skip", "true");
		if (Boolean.valueOf(skipLog)) {
			return;
		}
		List<DreamlabsTransLog> transLogs = new ArrayList<DreamlabsTransLog>();
		List<WebElement> elements = driver.findElements(By.id("quicklyPassword"));
		for (WebElement element : elements) {
			DreamlabsTransLog transLog = new DreamlabsTransLog();
			transLog.setOrgId(org.getId());
			transLog.setAccountId(account.getId());
			transLog.setTargetCode(element.getAttribute("target_code"));
			transLog.setTargetName(element.getAttribute("target_name"));
			transLog.setTransAmount(element.getAttribute("trans_amount"));
			transLog.setTransShare(element.getAttribute("trans_share"));
			transLog.setStatus(element.getAttribute("status"));
			log.info("Log share:{}", transLog);
			transLogs.add(transLog);
		}
		insertTransLogs(transLogs);
	}

}
