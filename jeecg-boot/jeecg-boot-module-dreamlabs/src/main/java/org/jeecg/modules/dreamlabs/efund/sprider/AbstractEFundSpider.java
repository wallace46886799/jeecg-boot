package org.jeecg.modules.dreamlabs.efund.sprider;



import java.util.Map;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.sprider.Spider;
import org.jeecg.modules.dreamlabs.transaction.SeleniumTransaction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractEFundSpider extends SeleniumTransaction implements Spider {

	public static final String HOST = "e.efunds.com.cn";

	protected void login(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception {
		log.info("Auto login start");
		driver.get("https://e.efunds.com.cn");
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		driver.findElement(By.id("certID")).sendKeys(accountParamsMap.get("login.username"));
		Thread.sleep(LONGER_WAIT_MILLIS);
		driver.findElement(By.id("tradepassword")).sendKeys(accountParamsMap.get("login.password"));
		Thread.sleep(LONGER_WAIT_MILLIS);
		driver.findElement(By.id("submitBtn")).click();
		Thread.sleep(LONGER_WAIT_MILLIS);
		log.info("Auto login done");
	}

	protected void logout(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap,
			DreamlabsAccount account, Map<String, String> accountParamsMap) throws Exception {
		log.info("Auto logout start");
		driver.findElement(By.xpath("//*[@id=\"loginin\"]/span[1]/a[2]")).click();
		driver.manage().deleteAllCookies();
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		log.info("Auto logout done");
	}
	
	@Override
	public Map<String, Object> process(String id) throws Exception {
		return doTransaction(id);
	}
}
