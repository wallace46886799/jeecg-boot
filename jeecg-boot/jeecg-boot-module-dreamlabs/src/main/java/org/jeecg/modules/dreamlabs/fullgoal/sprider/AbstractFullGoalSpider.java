package org.jeecg.modules.dreamlabs.fullgoal.sprider;

import java.util.Map;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.sprider.Spider;
import org.jeecg.modules.dreamlabs.transaction.SeleniumTransaction;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractFullGoalSpider extends SeleniumTransaction implements Spider{
	
	protected void login(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception {
		log.info("Auto login start.");
		driver.get("https://etrading.fullgoal.com.cn/etrading?_="+System.currentTimeMillis());	
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		driver.findElement(By.id("lognumberMode2")).sendKeys(accountParamsMap.get("login.username"));
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		driver.findElement(By.id("passwordMode2")).sendKeys(accountParamsMap.get("login.password"));
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		////*[@id="container"]/div/div/div[2]/div[2]/div/div/div/div[2]/div[2]/div[2]/form/div[3]/div/div/button
		driver.findElement(By.id("loginForm_submit")).click();
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		log.info("Auto login done.");
	}

	protected void logout(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception {
		log.info("Auto logout start.");
		driver.findElement(By.id("logoutEtradingBt")).click();
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		
		Alert logout = driver.switchTo().alert();
		log.info("Logout message is:{}",logout.getText());
		logout.accept();
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		
		driver.manage().deleteAllCookies();
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		log.info("Auto logout done.");
	}
	
	public Map<String,Object> process(String id) throws Exception{
		return doTransaction(id);
	}
}
