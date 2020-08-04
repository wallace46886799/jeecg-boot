package org.jeecg.modules.dreamlabs.gffund.sprider;

import java.util.Map;

import org.jeecg.modules.dreamlabs.account.entity.DreamlabsAccount;
import org.jeecg.modules.dreamlabs.org.entity.DreamlabsOrg;
import org.jeecg.modules.dreamlabs.sprider.Spider;
import org.jeecg.modules.dreamlabs.transaction.SeleniumTransaction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public abstract class AbstractGfFundSpider extends SeleniumTransaction implements Spider {

	public static final String HOST = "trade.gffunds.com.cn";

	protected void login(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap, DreamlabsAccount account,
			Map<String, String> accountParamsMap) throws Exception {
		log.info("Auto login start");
		driver.get("https://trade.gffunds.com.cn/login?_=" + System.currentTimeMillis());
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		driver.findElement(By.id("userName")).sendKeys(accountParamsMap.get("login.username"));
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		driver.findElement(By.id("password")).sendKeys(accountParamsMap.get("login.password"));
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		//// *[@id="container"]/div/div/div[2]/div[2]/div/div/div/div[2]/div[2]/div[2]/form/div[3]/div/div/button
		driver.findElement(By.xpath(
				"//*[@id=\"container\"]/div/div/div[2]/div[2]/div/div/div/div[2]/div[2]/div[2]/form/div[3]/div/div/button"))
				.click();
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		log.info("Auto login done");

	}

	protected void logout(WebDriver driver, DreamlabsOrg org, Map<String, String> orParamsMap, DreamlabsAccount account,
			Map<String, String> accountParamsMap) throws Exception {
		log.info("Auto logout start");
		driver.findElement(By.xpath("//*[@id=\"container\"]/div/div/div[2]/div[1]/div/div[1]/a/span")).click();
		driver.manage().deleteAllCookies();
		Thread.sleep(DEFAULT_WAIT_MILLIS);
		log.info("Auto logout done");
	}
	

	@Override
	public Map<String, Object> process(String id) throws Exception {
		return doTransaction(id);
	}

}
