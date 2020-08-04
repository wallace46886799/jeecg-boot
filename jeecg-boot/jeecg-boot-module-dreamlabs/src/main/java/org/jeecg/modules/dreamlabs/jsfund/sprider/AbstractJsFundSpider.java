package org.jeecg.modules.dreamlabs.jsfund.sprider;

import java.util.Map;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.sprider.Spider;
import org.jeecg.modules.dreamlabs.transaction.SeleniumTransaction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractJsFundSpider extends SeleniumTransaction implements Spider {

	protected void login(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap, DreamlabsAccount account,
			Map<String, String> accountParamsMap) throws Exception {
		log.info("Auto login start.");
		driver.get("https://e.jsfund.cn/passport");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		driver.findElement(By.id("loginno")).sendKeys(accountParamsMap.get("login.username"));
		Thread.sleep(LONGER_WAIT_MILLIS);
		driver.findElement(By.id("pwd")).sendKeys(accountParamsMap.get("login.password"));
		Thread.sleep(LONGER_WAIT_MILLIS);
		driver.findElement(By.id("submit_btn")).click();
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		log.info("Auto login done.");
	}

	protected void logout(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap, DreamlabsAccount account,
			Map<String, String> accountParamsMap) throws Exception {
		log.info("Auto logout start.");
		driver.findElement(By.xpath("//*[@id=\"header_id\"]/div[1]/div/div[2]/a[1]")).click();
		driver.manage().deleteAllCookies();
		Thread.sleep(LONGER_WAIT_MILLIS);
		log.info("Auto logout done.");
	}
	
	public Map<String,Object> process(String id) throws Exception{
		return doTransaction(id);
	}

}
