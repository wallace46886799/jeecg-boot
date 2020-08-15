package org.jeecg.modules.dreamlabs.gtja.quartz.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.transaction.StockEntrustItem;
import org.jeecg.modules.dreamlabs.translog.entity.DreamlabsTransLog;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
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
		Thread.sleep(LONGER_WAIT_MILLIS);
		
		// 3.查询委托情况
		driver.get("https://i.gtja.com/evercos/securities/stock/query/entrust.html");
		Thread.sleep(LONGER_WAIT_MILLIS);
		//*[@id="current-panel"]/div
		List<StockEntrustItem> items = new ArrayList<StockEntrustItem>(); 
//		String message = "当日";
//		String messageStr = driver.findElement(By.xpath("//*[@id=\"current-panel\"]/div/div")).getText();
//		if(messageStr.contains(message)) {
//			log.info(messageStr);
//			Map<String, Object> result = new HashMap<String, Object>();
//			result.put("total", items.size());
//			result.put("transactions", items);
//			return result;
//		}
		
		List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"current-panel\"]/div"));
		int size = elements.size();
		log.info("Total size:{}",size);
		
		for (int i = 1 ; i <= size; i++) {
			//*[@id="current-panel"]/div[1]/div[1]/p[1]  委托标的
			//*[@id="current-panel"]/div[1]/div[1]/p[2]  委托类型
			
			//*[@id="current-panel"]/div[1]/div[2]/p[1]  委托时间
			//*[@id="current-panel"]/div[1]/div[2]/p[2]  委托价格
			
			//*[@id="current-panel"]/div[1]/div[3]/p[1]  委托数量
			//*[@id="current-panel"]/div[1]/div[3]/p[2]  成交数量
			
			//*[@id="current-panel"]/div[1]/div[4]/p[1]  委托状态
			//*[@id="current-panel"]/div[1]/div[4]/p[2]  成交金额
			String name = driver.findElement(By.xpath("//*[@id=\"current-panel\"]/div["+ i +"]/div[1]/p[1]")).getText();
			log.info("name:{}",name);
			String type = driver.findElement(By.xpath("//*[@id=\"current-panel\"]/div["+ i +"]/div[1]/p[2]")).getText();
			log.info("type:{}",type);
			String time = driver.findElement(By.xpath("//*[@id=\"current-panel\"]/div["+ i +"]/div[2]/p[1]")).getText();
			log.info("time:{}",time);
			String price = driver.findElement(By.xpath("//*[@id=\"current-panel\"]/div["+ i +"]/div[2]/p[2]")).getText();
			log.info("price:{}",price);
			String entrustShare = driver.findElement(By.xpath("//*[@id=\"current-panel\"]/div["+ i +"]/div[3]/p[1]")).getText();
			log.info("entrustShare:{}",entrustShare);
			String dealShare = driver.findElement(By.xpath("//*[@id=\"current-panel\"]/div["+ i +"]/div[3]/p[2]")).getText();
			log.info("entrustShare:{}",entrustShare);
			String entrustStatus = driver.findElement(By.xpath("//*[@id=\"current-panel\"]/div["+ i +"]/div[4]/p[1]")).getText();
			log.info("entrustStatus:{}",entrustStatus);
			String entrustAmount = driver.findElement(By.xpath("//*[@id=\"current-panel\"]/div["+ i +"]/div[4]/p[2]")).getText();
			log.info("entrustAmount:{}",entrustAmount);
			StockEntrustItem item = new StockEntrustItem();
			item.setCode(StrUtil.subAfter(name, " ", true));
			item.setName(StrUtil.subBefore(name, " ", true));
			item.setEntrustType(type);
			item.setPrice(this.converterRegistry.convert(BigDecimal.class, price));
			item.setTime(Convert.toDate(time));
			item.setShare(this.converterRegistry.convert(BigDecimal.class, dealShare));
			item.setEntrustShare(this.converterRegistry.convert(BigDecimal.class, entrustShare));
			item.setEntrustAmount(this.converterRegistry.convert(BigDecimal.class, entrustAmount));
			item.setEntrustStatus(entrustStatus);
			log.info("Auto IPO entrust:{}", item);
			items.add(item);
		}
		// 4.等待3秒
		Thread.sleep(LONGER_WAIT_MILLIS);
		Map<String, Object> result = new HashMap<String, Object>(); 
		result.put("total", items.size());
		result.put("items", items);
		return result;
	}
	
	protected void preTransaction(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap, DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception{
		return;
	}
	
	protected void postTransaction(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap, Map<String, Object> result) throws Exception {
		String skipLog = accountParamsMap.getOrDefault("log.skip", "false");
		if (Boolean.valueOf(skipLog)) {
			return;
		}
		@SuppressWarnings("unchecked")
		List<StockEntrustItem> items = (ArrayList<StockEntrustItem>) result.get("items");
		
		if( items==null || items.size() == 0) {
			log.info("No entrust today.");
			return;
		}
		
		List<DreamlabsTransLog> transLogs = new ArrayList<DreamlabsTransLog>();
		StringBuffer stocks = new StringBuffer();
		for (StockEntrustItem item : items) {
			DreamlabsTransLog transLog = new DreamlabsTransLog();
			transLog.setOrgId(org.getId());
			transLog.setAccountId(account.getId());
			transLog.setTargetCode(item.getCode());
			transLog.setTargetName(item.getName());
			transLog.setTransTime(item.getTime());
			transLog.setTransAmount(item.getEntrustAmount().toString());
			transLog.setTransShare(item.getEntrustShare().toString());
			transLog.setTransPrice(item.getPrice().toString());
			transLog.setStatus(item.getEntrustStatus());
			transLog.setTransType(item.getEntrustType());
			transLog.setUpdateBy("Frank");
			transLog.setUpdateTime(new Date());
			transLog.setCreateBy("Frank");
			transLog.setCreateTime(new Date());
			log.info("Log share:{}", transLog);
			transLogs.add(transLog);
			stocks.append(transLog.messageContent()).append("<br>");
		}
		insertTransLogs(transLogs);
		
		log.info("Add a message:{}",  stocks.toString());
		String today = DateUtil.today();
		String title = "【"+today+"】自动打新@【"+account.getAccountName()+"】";
		Date esSendTime = new Date(System.currentTimeMillis() + 1000L*60*5);
		ISysBaseAPI sysBaseAPI = SpringContextUtils.getBean(ISysBaseAPI.class);
		sysBaseAPI.addMessage("2", accountParamsMap.get("email.address"), title, stocks.toString(), esSendTime);
	}

}
