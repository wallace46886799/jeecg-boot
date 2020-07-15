package org.jeecg.modules.dreamlabs.gtja.quartz.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.translog.entity.DreamlabsTransLog;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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
		log.info("==============》自动打新，日期:{}，时间:{}", DateUtils.getDate("yyyy-MM-dd"), DateUtils.now());
		WebDriver driver = SpringContextUtils.getBean(WebDriver.class);

		List<DreamlabsOrg> orgs = this.queryOrgs();

		for (DreamlabsOrg org : orgs) {
			log.info("金融机构:{}", org.getOrgName());

			log.info("查询需打新的机构参数:{}", org.getOrgName());
			Map<String, String> orgParamsMap = this.queryOrgParams(org);

			List<DreamlabsAccount> accounts = this.queryAccounts();

			for (DreamlabsAccount account : accounts) {
				if (!account.getStatus().equals("1")) {
					log.info("账户冻结:{}", account.getAccountName());
					continue;
				}

				Map<String, String> accountParamsMap = this.queryAccountParams(account);
				log.info("查询需打新的账户:{},", account.getAccountName(), accountParamsMap);

				log.info("0.国泰君安站点--开始登录:{},{},{},{}", org, orgParamsMap, account, accountParamsMap);
				try {
					loginWithSelenium(driver, org, orgParamsMap, account, accountParamsMap);
				} catch (Exception e) {
					log.error("Auto login failed:{},{},{},{}", org, orgParamsMap, account, accountParamsMap);
					log.error("Auto login failed.", e);
					continue;
				}
				log.info("0.国泰君安站点--登录成功:{},{},{},{}", org, orgParamsMap, account, accountParamsMap);

				log.info("1.国泰君安站点--打新股开始:{},{},{},{}", org, orgParamsMap, account, accountParamsMap);
				try {
					handleShares(driver, org, orgParamsMap, account, accountParamsMap);
				} catch (Exception e) {
					log.error("Handle share failed:{},{},{},{}", org, orgParamsMap, account, accountParamsMap);
					log.error("Handle share failed.", e);
					continue;
				}
				log.info("1.国泰君安站点--打新股成功:{},{},{},{}", org, orgParamsMap, account, accountParamsMap);

				log.info("2.国泰君安站点--记录打新开始:{},{},{},{}", org, orgParamsMap, account, accountParamsMap);
				try {
					logShares(driver, org, orgParamsMap, account, accountParamsMap);
				} catch (Exception e) {
					log.error("Log share failed:{},{},{},{}", org, orgParamsMap, account, accountParamsMap);
					log.error("Log share failed.", e);
					continue;
				}
				log.info("2.国泰君安站点--记录打新成功:{},{},{},{}", org, orgParamsMap, account, accountParamsMap);
				
				
				
				log.info("3.国泰君安站点--退出账户开始:{},{},{},{}", org, orgParamsMap, account, accountParamsMap);
				try {
					logoutWithSelenium(driver, org, orgParamsMap, account, accountParamsMap);
				} catch (Exception e) {
					log.error("Log share failed:{},{},{},{}", org, orgParamsMap, account, accountParamsMap);
					log.error("Log share failed.", e);
					continue;
				}
				log.info("3.国泰君安站点--退出账户成功:{},{},{},{}", org, orgParamsMap, account, accountParamsMap);

//				log.info("1.国泰君安站点--查询今日新股信息");
//				String share_url = orgParamsMap.get("shares.url");
//				accountParamsMap.put("ts", String.valueOf(System.currentTimeMillis()));
//				share_url = PlaceholderUtil.replaceWithMapStr(share_url, accountParamsMap);
//				log.info("新股URL:{}",share_url);
//				driver.get(share_url);
//				log.info("1.国泰君安站点--查询今日新股信息:{}" , share_url);
//				
//				
//				log.info("2.国泰君安站点--查询今日上证额度");
//				String sh_url = orgParamsMap.get("credits.url");
//				accountParamsMap.put("account.stock", accountParamsMap.get("account.sh"));
//				accountParamsMap.put("account.type", "1");
//				sh_url = PlaceholderUtil.replaceWithMapStr(sh_url, accountParamsMap);
//				log.info("上证URL:{}",sh_url);
//				driver.get(sh_url);
//				log.info("2.国泰君安站点--查询今日上证额度:{}" ,sh_url);
//				
//			
//				log.info("3.国泰君安站点--查询今日深证额度");
//				String sz_url = orgParamsMap.get("credits.url");
//				accountParamsMap.put("account.stock", accountParamsMap.get("account.sz"));
//				accountParamsMap.put("account.type", "2");
//				sz_url = PlaceholderUtil.replaceWithMapStr(sz_url, accountParamsMap);
//				log.info("深证URL:{}",sz_url);
//				driver.get(sz_url);
//				log.info("3.国泰君安站点--查询今日深圳额度:{}" , sz_url);
//				
//				log.info("4.国泰君安站点--执行打新");
//				String ipo_url = orgParamsMap.get("ipo.url");
//				driver.get(ipo_url);
//				log.info("4.国泰君安站点--执行打新:{}" , ipo_url);
//				
//				
//				log.info("5.国泰君安站点--执行打新交易记录");
//				List<DreamlabsTransLog> logs = new ArrayList<DreamlabsTransLog>();
//				this.insertTransLogs(logs);
//				log.info("5.国泰君安站点--执行打新交易记录:{}" , logs);

			}

		}
		log.info("《==============自动打新结束，日期:{}，时间:{}", DateUtils.getDate("yyyy-MM-dd"), DateUtils.now());
	}

	private void handleShares(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception {
		// 1.新股页面
		driver.get("https://i.gtja.com/evercos/securities/stock/query/ipo/shares.html");
		driver.get("https://i.gtja.com/evercos/securities/stock/query/ipo/oneKeyBuyBuy.html");
		Thread.sleep(5000);

		// 2.一键申购
		// $("#oneKeyBuy")
		Object result = ((JavascriptExecutor) driver).executeScript("$(\"#oneKeyBuy\").click()");
		log.info("Result is {}", result);

		// 3.等待30秒
		Thread.sleep(30000);
	}

	private void logShares(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception {
		List<DreamlabsTransLog> transLogs = new ArrayList<DreamlabsTransLog>();
		List<WebElement> elements = driver.findElements(By.id("quicklyPassword"));
		for(WebElement element : elements) {
			DreamlabsTransLog transLog = new DreamlabsTransLog();
			transLog.setOrgId(org.getId());
			transLog.setAccountId(account.getId());
			transLog.setTargetCode(element.getAttribute("target_code"));
			transLog.setTargetName(element.getAttribute("target_name"));
			transLog.setTransAmount(element.getAttribute("trans_amount"));
			transLog.setTransShare(element.getAttribute("trans_share"));
			transLog.setStatus(element.getAttribute("status"));
			log.info("Log share:{}",transLog);
			transLogs.add(transLog);
		}
		insertTransLogs(transLogs);
	}

}
